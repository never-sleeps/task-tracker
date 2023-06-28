package me.neversleeps.appspring.config

import kotlinx.serialization.json.Json
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter

@Configuration
class SerializationConfiguration {
    @Bean
    fun messageConverter(): KotlinSerializationJsonHttpMessageConverter {
        return KotlinSerializationJsonHttpMessageConverter(
            Json {
                ignoreUnknownKeys = true
            },
        )
    }
}
