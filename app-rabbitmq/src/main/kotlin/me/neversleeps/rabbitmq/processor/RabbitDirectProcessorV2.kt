package me.neversleeps.rabbitmq.processor

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import me.neversleeps.api.multiplatform.apiRequestDeserialize
import me.neversleeps.api.multiplatform.apiResponseSerialize
import me.neversleeps.api.multiplatform.v1.models.IRequest
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.common.CorSettings
import me.neversleeps.common.models.AppCommand
import me.neversleeps.mappers.multiplatform.fromInternal.toTransport
import me.neversleeps.mappers.multiplatform.fromTransport.fromTransport
import me.neversleeps.rabbitmq.RabbitProcessorBase
import me.neversleeps.rabbitmq.config.RabbitConfig
import me.neversleeps.rabbitmq.config.RabbitExchangeConfiguration
import me.neversleeps.rabbitmq.config.corSettings
import me.neversleeps.rabbitmq.config.rabbitLogger
import mu.KLogging

class RabbitDirectProcessorV2(
    config: RabbitConfig,
    processorConfig: RabbitExchangeConfiguration,
    settings: CorSettings = corSettings,
    private val processor: ProjectProcessor = ProjectProcessor(settings),
) : RabbitProcessorBase(config, processorConfig) {

    companion object : KLogging()

    private val logger = settings.loggerProvider.logger(RabbitDirectProcessorV1::class)

    override suspend fun Channel.processMessage(message: Delivery) {
        processor.process(
            logger = logger,
            logId = "rabbit-v2",
            command = AppCommand.NONE,
            fromTransport = { ctx ->
                apiRequestDeserialize<IRequest>(String(message.body)).also {
                    println("TYPE: ${it::class.java.simpleName}")
                    ctx.fromTransport(it)
                }
            },
            sendResponse = { ctx ->
                rabbitLogger.info("start publish")
                val response = ctx.toTransport()
                apiResponseSerialize(response).also {
                    println("Publishing $response to ${processorConfig.exchange} exchange for keyOut ${processorConfig.keyOut}")
                    basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it.toByteArray())
                }.also {
                    println("published")
                }
            },
        )
    }
}
