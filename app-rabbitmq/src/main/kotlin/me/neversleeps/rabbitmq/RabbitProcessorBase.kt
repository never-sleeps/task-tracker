package me.neversleeps.rabbitmq

import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import me.neversleeps.rabbitmq.config.RabbitConfig
import me.neversleeps.rabbitmq.config.RabbitExchangeConfiguration
import mu.KLogging
import kotlin.coroutines.CoroutineContext

/**
 * Абстрактный класс для процессоров-consumer'ов RabbitMQ, увязывает транспортную и бизнес-части
 * @property config - настройки подключения
 * @property processorConfig - настройки Rabbit exchange
 */
abstract class RabbitProcessorBase(
    private val config: RabbitConfig,
    val processorConfig: RabbitExchangeConfiguration,
) {
    companion object : KLogging()

    suspend fun process(dispatcher: CoroutineContext = Dispatchers.IO) {
        logger.info { "connection is creating" }
        withContext(dispatcher) {
            ConnectionFactory().apply {
                host = config.host
                port = config.port
                username = config.user
                password = config.password
            }.newConnection().use { connection ->
                connection.createChannel().use { channel ->
                    val deliveryCallback = channel.getDeliveryCallback()
                    val cancelCallback = getCancelCallback()
                    runBlocking {
                        channel.describeAndListen(deliveryCallback, cancelCallback)
                    }
                }
            }
        }
    }

    /**
     * Обработка поступившего сообщения в deliverCallback
     */
    protected abstract suspend fun Channel.processMessage(message: Delivery)

    /**
     * Обработка ошибок
     */
    protected abstract fun Channel.onError(e: Throwable)

    /**
     * Callback, который вызывается при доставке сообщения consumer'у
     */
    private fun Channel.getDeliveryCallback(): DeliverCallback = DeliverCallback { _, message ->
        runBlocking {
            kotlin.runCatching {
                processMessage(message)
            }.onFailure {
                onError(it)
            }
        }
    }

    /**
     * Callback, вызываемый при отмене consumer'а
     */
    private fun getCancelCallback() = CancelCallback {
        logger.info { "[$it] was cancelled" }
    }

    private suspend fun Channel.describeAndListen(
        deliverCallback: DeliverCallback,
        cancelCallback: CancelCallback,
    ) {
        withContext(Dispatchers.IO) {
            logger.info { "start describing" }
            exchangeDeclare(processorConfig.exchange, processorConfig.exchangeType)
            // Объявляем queue
            queueDeclare(processorConfig.queueIn, false, false, false, null)
            queueDeclare(processorConfig.queueOut, false, false, false, null)
            // связываем exchange с queue по ключу
            queueBind(processorConfig.queueIn, processorConfig.exchange, processorConfig.keyIn)
            queueBind(processorConfig.queueOut, processorConfig.exchange, processorConfig.keyOut)
            // запуск consumer'a с автоотправкой подтверждения при получении сообщения
            basicConsume(processorConfig.queueIn, true, processorConfig.consumerTag, deliverCallback, cancelCallback)
            logger.info { "finish describing" }

            while (isOpen) {
                kotlin.runCatching {
                    delay(100)
                }.onFailure { e ->
                    e.printStackTrace()
                }
            }

            logger.info { "Channel for [${processorConfig.consumerTag}] was closed." }
        }
    }
}
