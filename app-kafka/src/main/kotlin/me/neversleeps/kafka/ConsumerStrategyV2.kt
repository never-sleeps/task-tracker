package me.neversleeps.kafka

import me.neversleeps.api.multiplatform.apiRequestDeserialize
import me.neversleeps.api.multiplatform.apiResponseSerialize
import me.neversleeps.api.multiplatform.v1.models.IRequest
import me.neversleeps.api.multiplatform.v1.models.IResponse
import me.neversleeps.common.ProjectContext
import me.neversleeps.kafka.config.AppKafkaConfig
import me.neversleeps.mappers.multiplatform.fromInternal.toTransport
import me.neversleeps.mappers.multiplatform.fromTransport.fromTransport

class ConsumerStrategyV2 : ConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV2, config.kafkaTopicOutV2)
    }

    override fun serialize(source: ProjectContext): String {
        val response: IResponse = source.toTransport()
        return apiResponseSerialize(response)
    }

    override fun deserialize(value: String, target: ProjectContext) {
        val request: IRequest = apiRequestDeserialize(value)
        target.fromTransport(request)
    }
}
