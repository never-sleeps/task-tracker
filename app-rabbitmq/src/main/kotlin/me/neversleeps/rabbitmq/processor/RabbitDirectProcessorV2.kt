package me.neversleeps.rabbitmq.processor

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import kotlinx.datetime.Clock
import me.neversleeps.api.multiplatform.apiRequestDeserialize
import me.neversleeps.api.multiplatform.apiResponseSerialize
import me.neversleeps.api.multiplatform.v1.models.IRequest
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.helpers.addError
import me.neversleeps.common.helpers.asAppError
import me.neversleeps.common.models.AppState
import me.neversleeps.mappers.multiplatform.fromInternal.toTransport
import me.neversleeps.mappers.multiplatform.fromTransport.fromTransport
import me.neversleeps.rabbitmq.RabbitProcessorBase
import me.neversleeps.rabbitmq.config.RabbitConfig
import me.neversleeps.rabbitmq.config.RabbitExchangeConfiguration
import mu.KLogging

class RabbitDirectProcessorV2(
    config: RabbitConfig,
    processorConfig: RabbitExchangeConfiguration,
    private val processor: ProjectProcessor = ProjectProcessor(),
) : RabbitProcessorBase(config, processorConfig) {

    companion object : KLogging()

    override suspend fun Channel.processMessage(message: Delivery, projectContext: ProjectContext) {
        apiRequestDeserialize<IRequest>(String(message.body))
            .also {
                logger.info { "TYPE: ${it::class.java.simpleName}" }
                projectContext.fromTransport(it)
            }

        logger.info { "start publish" }
        val response = processor.execute(projectContext)
            .run { projectContext.toTransport() }

        apiResponseSerialize(response)
            .also {
                logger.info { "Publishing $response to ${processorConfig.exchange} exchange for keyOut ${processorConfig.keyOut}" }
                basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it.toByteArray())
            }
            .also {
                logger.info { "published" }
            }
    }

    override fun Channel.onError(e: Throwable, projectContext: ProjectContext) {
        logger.error { e.printStackTrace() }
        projectContext.state = AppState.FAILING
        projectContext.addError(error = arrayOf(e.asAppError()))
        val response = projectContext.toTransport()
        apiResponseSerialize(response)
            .also {
                basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it.toByteArray())
            }
    }
}
