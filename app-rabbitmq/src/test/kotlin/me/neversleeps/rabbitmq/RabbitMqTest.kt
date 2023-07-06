package me.neversleeps.rabbitmq

import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.neversleeps.api.jackson.apiMapper
import me.neversleeps.api.jackson.v1.models.ProjectDebugStub
import me.neversleeps.api.multiplatform.apiRequestSerialize
import me.neversleeps.api.multiplatform.apiResponseDeserialize
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import me.neversleeps.api.jackson.v1.models.ProjectCreateObject as ProjectCreateObjectV1
import me.neversleeps.api.jackson.v1.models.ProjectCreateRequest as ProjectCreateRequestV1
import me.neversleeps.api.jackson.v1.models.ProjectCreateResponse as ProjectCreateResponseV1
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateObject as ProjectCreateObjectV2
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateRequest as ProjectCreateRequestV2
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateResponse as ProjectCreateResponseV2

class RabbitMqTest : AbstractRabbitMqTest() {

    @OptIn(DelicateCoroutinesApi::class)
    @BeforeTest
    fun tearUp() {
        println("init controller")
        GlobalScope.launch {
            controller.start()
        }
        Thread.sleep(6000) // await when controller starts producers
        println("controller initiated")
    }

    @Test
    fun createProjectTestV1() {
        val processorConfig = processorV1.processorConfig

        getConnection().createChannel().use { channel ->
            channel.exchangeDeclare(processorConfig.exchange, EXCHANGE_TYPE)
            val queueOut = channel.queueDeclare().queue
            channel.queueBind(queueOut, processorConfig.exchange, processorConfig.keyOut)

            var responseJson = ""
            val deliverCallback = DeliverCallback { consumerTag, delivery ->
                responseJson = String(delivery.body, Charsets.UTF_8)
                println(" [x] Received by $consumerTag: '$responseJson'")
            }
            channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

            val body = apiMapper.writeValueAsBytes(projectCreateRequestV1)

            // when
            channel.basicPublish(processorConfig.exchange, processorConfig.keyIn, null, body)
            Thread.sleep(3000) // waiting for message processing

            // then
            println("RESPONSE: $responseJson")
            val response = apiMapper.readValue(responseJson, ProjectCreateResponseV1::class.java)
            val expected = ProjectStub.get()
            assertEquals(expected.title, response.project?.title)
            assertEquals(expected.description, response.project?.description)
        }
    }

    @Test
    fun createProjectTestV2() {
        val processorConfig = processorV2.processorConfig

        getConnection().createChannel().use { channel ->
            channel.exchangeDeclare(processorConfig.exchange, EXCHANGE_TYPE)
            val queueOut = channel.queueDeclare().queue
            channel.queueBind(queueOut, processorConfig.exchange, processorConfig.keyOut)

            var responseJson = ""
            val deliverCallback = DeliverCallback { consumerTag, delivery ->
                responseJson = String(delivery.body, Charsets.UTF_8)
                println(" [x] Received by $consumerTag: '$responseJson'")
            }
            channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

            val body = apiRequestSerialize(projectCreateRequestV2).toByteArray()

            // when
            channel.basicPublish(processorConfig.exchange, processorConfig.keyIn, null, body)
            Thread.sleep(3000) // waiting for message processing

            // then
            println("RESPONSE: $responseJson")
            val response = apiResponseDeserialize<ProjectCreateResponseV2>(responseJson)
            val expected = ProjectStub.get()
            assertEquals(expected.title, response.project?.title)
            assertEquals(expected.description, response.project?.description)
        }
    }

    private fun getConnection(): Connection =
        ConnectionFactory()
            .apply {
                host = config.host
                port = config.port
                username = config.user
                password = config.password
            }
            .newConnection()

    private val projectCreateRequestV1 =
        ProjectCreateRequestV1(
            requestId = "12345",
            stub = ProjectDebugStub.SUCCESS,
            data = ProjectCreateObjectV1(
                title = "some title",
                description = "some description",
            ),
        )

    private val projectCreateRequestV2 =
        ProjectCreateRequestV2(
            requestId = "request-id",
            requestType = "createProject",
            data = ProjectCreateObjectV2(
                title = "some title",
                description = "some description",
                createdBy = "some-author-id",
            ),
        )
}
