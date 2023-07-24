package me.neversleeps.appspring.config

import me.neversleeps.business.ProjectProcessor
import me.neversleeps.common.CorSettings
import me.neversleeps.logging.common.LoggerProvider
import me.neversleeps.logging.jvm.mpLoggerLogback
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CorConfig {
    @Bean
    fun loggerProvider(): LoggerProvider = LoggerProvider { mpLoggerLogback(it) }

    @Bean
    fun corSettings(loggerProvider: LoggerProvider): CorSettings = CorSettings(
        loggerProvider = loggerProvider,
    )

    @Bean
    fun processor() = ProjectProcessor()
}
