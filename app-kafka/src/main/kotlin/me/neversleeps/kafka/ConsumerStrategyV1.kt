package me.neversleeps.kafka

import me.neversleeps.api.jackson.apiRequestDeserialize
import me.neversleeps.api.jackson.apiResponseSerialize
import me.neversleeps.api.jackson.v1.models.IRequest
import me.neversleeps.api.jackson.v1.models.IResponse
import me.neversleeps.common.ProjectContext
import me.neversleeps.mappers.jackson.fromInternal.toTransport
import me.neversleeps.mappers.jackson.fromTransport.fromTransport

class ConsumerStrategyV1 : ConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV1, config.kafkaTopicOutV1)
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