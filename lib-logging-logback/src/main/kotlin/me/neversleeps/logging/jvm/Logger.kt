package me.neversleeps.logging.jvm

import ch.qos.logback.classic.Logger
import me.neversleeps.logging.common.ILogWrapper
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

/**
 * Generate internal MpLogContext logger
 *
 * @param logger Logback instance from [LoggerFactory.getLogger()]
 */
fun mpLoggerLogback(logger: Logger): ILogWrapper = LogWrapperLogback(
    logger = logger,
    loggerId = logger.name,
)

fun mpLoggerLogback(clazz: KClass<*>): ILogWrapper = mpLoggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)

@Suppress("unused")
fun mpLoggerLogback(loggerId: String): ILogWrapper = mpLoggerLogback(LoggerFactory.getLogger(loggerId) as Logger)
