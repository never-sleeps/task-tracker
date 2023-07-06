package me.neversleeps.rabbitmq

import me.neversleeps.rabbitmq.config.RabbitConfig
import me.neversleeps.rabbitmq.config.RabbitExchangeConfiguration
import me.neversleeps.rabbitmq.controller.RabbitController
import me.neversleeps.rabbitmq.processor.RabbitDirectProcessorV1
import me.neversleeps.rabbitmq.processor.RabbitDirectProcessorV2
import org.testcontainers.containers.RabbitMQContainer

abstract class AbstractRabbitMqTest {
    companion object {
        const val EXCHANGE_TYPE = "direct"
        const val TRANSPORT_EXCHANGE_V1 = "transport-exchange-v1"
        const val TRANSPORT_EXCHANGE_V2 = "transport-exchange-v2"
    }

    val container by lazy {
        // Образ "rabbitmq:3-management" предназначен для дебага, он содержит панель управления на порту httpPort
        // Образ "rabbitmq:latest" минимальный и не содержит панель управления
        RabbitMQContainer("rabbitmq:latest").apply {
            withExposedPorts(5672, 15672)
            withUser(RabbitConfig.RABBIT_USER, RabbitConfig.RABBIT_PASSWORD)
            start()
        }
    }
    val config by lazy {
        RabbitConfig(
            port = container.getMappedPort(5672),
            host = container.host,
        )
    }
    val processorConfigV1 = RabbitExchangeConfiguration(
        keyIn = "in-v1",
        keyOut = "out-v1",
        exchange = TRANSPORT_EXCHANGE_V1,
        queueIn = "v1-queue",
        queueOut = "v1-queue-out",
        consumerTag = "v1-consumer",
        exchangeType = EXCHANGE_TYPE,
    )
    val processorV1 by lazy {
        RabbitDirectProcessorV1(
            config = config,
            processorConfig = processorConfigV1,
        )
    }
    val processorConfigV2 = RabbitExchangeConfiguration(
        keyIn = "in-v2",
        keyOut = "out-v2",
        exchange = TRANSPORT_EXCHANGE_V2,
        queueIn = "v2-queue",
        queueOut = "v2-queue-out",
        consumerTag = "v2-consumer",
        exchangeType = EXCHANGE_TYPE,
    )

    val processorV2 by lazy {
        RabbitDirectProcessorV2(
            config = config,
            processorConfig = processorConfigV2,
        )
    }
    val controller by lazy {
        RabbitController(
            processors = setOf(processorV1, processorV2),
        )
    }
}
