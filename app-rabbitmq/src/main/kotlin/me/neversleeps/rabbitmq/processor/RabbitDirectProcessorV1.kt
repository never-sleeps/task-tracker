package me.neversleeps.rabbitmq.processor

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import kotlinx.datetime.Clock
import me.neversleeps.api.jackson.apiMapper
import me.neversleeps.api.jackson.v1.models.IRequest
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.helpers.addError
import me.neversleeps.common.helpers.asAppError
import me.neversleeps.common.models.AppState
import me.neversleeps.mappers.jackson.fromInternal.toTransport
import me.neversleeps.mappers.jackson.fromTransport.fromTransport
import me.neversleeps.rabbitmq.RabbitProcessorBase
import me.neversleeps.rabbitmq.config.RabbitConfig
import me.neversleeps.rabbitmq.config.RabbitExchangeConfiguration
import mu.KLogging

class RabbitDirectProcessorV1(
    config: RabbitConfig,
    processorConfig: RabbitExchangeConfiguration,
    private val processor: ProjectProcessor = ProjectProcessor(),
) : RabbitProcessorBase(config, processorConfig) {

    companion object : KLogging()

    private val projectContext = ProjectContext()

    override suspend fun Channel.processMessage(message: Delivery) {
        projectContext.apply {
            timeStart = Clock.System.now()
        }

        apiMapper.readValue(message.body, IRequest::class.java).run {
            projectContext.fromTransport(this)
                .also { logger.info { "TYPE: ${this::class.simpleName}" } }
        }
        val response = processor.execute(projectContext)
            .run { projectContext.toTransport() }

        apiMapper.writeValueAsBytes(response)
            .also {
                logger.info { "Publishing $response to ${processorConfig.exchange} exchange for keyOut ${processorConfig.keyOut}" }
                basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it)
            }.also {
                logger.info { "published" }
            }
    }

    override fun Channel.onError(e: Throwable) {
        logger.error { e.printStackTrace() }
        projectContext.state = AppState.FAILING
        projectContext.addError(error = arrayOf(e.asAppError()))
        val response = projectContext.toTransport()
        apiMapper.writeValueAsBytes(response).also {
            basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it)
        }
    }
}
