package me.neversleeps.rabbitmq.controller

import kotlinx.coroutines.* // ktlint-disable no-wildcard-imports
import me.neversleeps.rabbitmq.RabbitProcessorBase
import mu.KLogging

class RabbitController(
    private val processors: Set<RabbitProcessorBase>,
) {

    companion object : KLogging()

    fun start() = runBlocking {
        logger.info { "start init processors" }
        processors.forEach {
            try {
                launch { it.process() }
            } catch (e: RuntimeException) {
                e.printStackTrace()
            }
        }
    }
}
