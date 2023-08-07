package me.neversleeps.rabbitmq.processor

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import me.neversleeps.api.jackson.apiMapper
import me.neversleeps.api.jackson.v1.models.IRequest
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.common.CorSettings
import me.neversleeps.common.models.AppCommand
import me.neversleeps.mappers.jackson.fromInternal.toTransport
import me.neversleeps.mappers.jackson.fromTransport.fromTransport
import me.neversleeps.rabbitmq.RabbitProcessorBase
import me.neversleeps.rabbitmq.config.RabbitConfig
import me.neversleeps.rabbitmq.config.RabbitExchangeConfiguration
import me.neversleeps.rabbitmq.config.corSettings
import mu.KLogging

class RabbitDirectProcessorV1(
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
            logId = "rabbit-v1",
            command = AppCommand.NONE,
            fromTransport = { ctx ->
                apiMapper.readValue(message.body, IRequest::class.java).run {
                    ctx.fromTransport(this).also {
                        println("TYPE: ${this::class.simpleName}")
                    }
                }
            },
            sendResponse = { ctx ->
                val response = ctx.toTransport()
                apiMapper.writeValueAsBytes(response).also {
                    println("Publishing $response to ${processorConfig.exchange} exchange for keyOut ${processorConfig.keyOut}")
                    basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it)
                }.also {
                    println("published")
                }
            },
        )
    }
}
