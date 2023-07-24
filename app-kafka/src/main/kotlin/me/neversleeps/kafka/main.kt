package me.neversleeps.kafka // ktlint-disable filename

import me.neversleeps.kafka.config.AppKafkaConfig

fun main() {
    val config = AppKafkaConfig()
    val consumer = AppKafkaConsumer(config, listOf(ConsumerStrategyV1(), ConsumerStrategyV2()))
    consumer.run()
}
