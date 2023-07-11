package me.neversleeps.rabbitmq

import me.neversleeps.business.ProjectProcessor
import me.neversleeps.rabbitmq.config.RabbitConfig
import me.neversleeps.rabbitmq.config.RabbitExchangeConfiguration
import me.neversleeps.rabbitmq.controller.RabbitController
import me.neversleeps.rabbitmq.processor.RabbitDirectProcessorV1
import me.neversleeps.rabbitmq.processor.RabbitDirectProcessorV2

fun main() {
    val config = RabbitConfig(
        host = System.getenv("RABBIT_HOST") ?: RabbitConfig.HOST,
    )
    val projectProcessor = ProjectProcessor()

    val producerConfigV1 = RabbitExchangeConfiguration(
        keyIn = "in-v1",
        keyOut = "out-v1",
        exchange = "transport-exchange-v1",
        queueIn = "v1-queue",
        queueOut = "v1-queue-out",
        consumerTag = "v1-consumer",
        exchangeType = "direct",
    )

    val producerConfigV2 = RabbitExchangeConfiguration(
        keyIn = "in-v2",
        keyOut = "out-v2",
        exchange = "transport-exchange-v2",
        queueIn = "v2-queue",
        queueOut = "v2-queue-out",
        consumerTag = "v2-consumer",
        exchangeType = "direct",
    )

    val processor by lazy {
        RabbitDirectProcessorV1(
            config = config,
            processorConfig = producerConfigV1,
            processor = projectProcessor,
        )
    }

    val processor2 by lazy {
        RabbitDirectProcessorV2(
            config = config,
            processorConfig = producerConfigV2,
            processor = projectProcessor,
        )
    }
    val controller by lazy {
        RabbitController(
            processors = setOf(processor, processor2),
        )
    }
    controller.start()
}
