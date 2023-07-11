package me.neversleeps.rabbitmq.config

data class RabbitConfig(
    val host: String = HOST,
    val port: Int = PORT,
    val user: String = RABBIT_USER,
    val password: String = RABBIT_PASSWORD,
) {
    companion object {
        const val HOST = "localhost"
        const val PORT = 5672
        const val RABBIT_USER = "guest"
        const val RABBIT_PASSWORD = "guest"
    }
}
