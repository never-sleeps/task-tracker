package me.neversleeps.kafka

import kotlinx.atomicfu.atomic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.common.CorSettings
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppCommand
import me.neversleeps.kafka.config.AppKafkaConfig
import me.neversleeps.kafka.config.corSettings
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.errors.WakeupException
import java.time.Duration
import java.util.UUID

private val log = KotlinLogging.logger {}

data class InputOutputTopics(val input: String, val output: String)

interface ConsumerStrategy {
    fun topics(config: AppKafkaConfig): InputOutputTopics
    fun serialize(source: ProjectContext): String
    fun deserialize(value: String, target: ProjectContext)
}

class AppKafkaConsumer(
    private val config: AppKafkaConfig,
    consumerStrategies: List<ConsumerStrategy>,
    setting: CorSettings = corSettings,
    private val processor: ProjectProcessor = ProjectProcessor(),
    private val consumer: Consumer<String, String> = config.createKafkaConsumer(),
    private val producer: Producer<String, String> = config.createKafkaProducer(),
) {
    private val logger = setting.loggerProvider.logger(AppKafkaConsumer::class)
    private val process = atomic(true) // multithreading
    private val topicsAndStrategyByInputTopic = consumerStrategies.associate {
        val topics = it.topics(config)
        topics.input to TopicsAndStrategy(topics.input, topics.output, it)
    }

    fun run() = runBlocking {
        try {
            consumer.subscribe(topicsAndStrategyByInputTopic.keys)
            while (process.value) {
                val records: ConsumerRecords<String, String> = withContext(Dispatchers.IO) {
                    consumer.poll(Duration.ofSeconds(1))
                }
                if (!records.isEmpty) {
                    log.info { "Receive ${records.count()} messages" }
                }

                records.forEach { record: ConsumerRecord<String, String> ->
                    log.info { "process ${record.key()} from ${record.topic()}:\n${record.value()}" }
                    val (_, outputTopic, strategy) = topicsAndStrategyByInputTopic[record.topic()] ?: throw RuntimeException("Receive message from unknown topic ${record.topic()}")

                    processor.process(
                        logger = logger,
                        logId = "kafka",
                        command = AppCommand.NONE,
                        fromTransport = { ctx -> strategy.deserialize(record.value(), ctx) },
                        sendResponse = { ctx -> sendResponse(ctx, strategy, outputTopic) },
                    )
                }
            }
        } catch (ex: WakeupException) {
            // ignore for shutdown
        } catch (ex: RuntimeException) {
            // exception handling
            withContext(NonCancellable) {
                throw ex
            }
        } finally {
            withContext(NonCancellable) {
                consumer.close()
            }
        }
    }

    private fun sendResponse(context: ProjectContext, strategy: ConsumerStrategy, outputTopic: String) {
        val json = strategy.serialize(context)
        val resRecord = ProducerRecord(
            outputTopic,
            UUID.randomUUID().toString(),
            json,
        )
        log.info { "sending ${resRecord.key()} to $outputTopic:\n$json" }
        producer.send(resRecord)
    }

    fun stop() {
        process.value = false
    }

    private data class TopicsAndStrategy(val inputTopic: String, val outputTopic: String, val strategy: ConsumerStrategy)
}
