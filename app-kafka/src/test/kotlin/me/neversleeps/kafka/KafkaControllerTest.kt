package me.neversleeps.kafka

import me.neversleeps.api.jackson.apiResponseDeserialize
import me.neversleeps.api.jackson.v1.models.ProjectCreateResponse
import me.neversleeps.api.multiplatform.apiRequestSerialize
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateObject
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectDebugStub
import me.neversleeps.api.multiplatform.v1.models.WorkMode
import me.neversleeps.kafka.config.AppKafkaConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class KafkaControllerTest {

    @Test
    fun runKafka() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())
        val config = AppKafkaConfig()
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val app = AppKafkaConsumer(config, listOf(ConsumerStrategyV1()), consumer = consumer, producer = producer)
        val request = ProjectCreateRequest(
            requestType = "createProject",
            requestId = UUID.randomUUID().toString(),
            stub = ProjectDebugStub.SUCCESS,
            mode = WorkMode.STUB,
            data = ProjectCreateObject(
                title = "some title",
                description = "some description",
            ),
        )
        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    apiRequestSerialize(request),
                ),
            )
            app.stop()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.run()

        val message = producer.history().first()
        val result = apiResponseDeserialize<ProjectCreateResponse>(message.value())
        assertEquals(outputTopic, message.topic())
        assertEquals(request.requestId, result.requestId)
        assertEquals(request.data?.title, result.project?.title)
    }

    companion object {
        const val PARTITION = 0
    }
}
