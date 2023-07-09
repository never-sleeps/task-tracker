package me.neversleeps.kafka // ktlint-disable filename

fun main() {
    val config = AppKafkaConfig()
    val consumer = AppKafkaConsumer(config, listOf(ConsumerStrategyV1(), ConsumerStrategyV2()))
    consumer.run()
}
