package me.neversleeps.kafka.config

class AppKafkaConfig(
    val kafkaHosts: List<String> = KAFKA_HOSTS,
    val kafkaGroupId: String = KAFKA_GROUP_ID,
    val kafkaTopicInV1: String = KAFKA_TOPIC_IN_V1,
    val kafkaTopicOutV1: String = KAFKA_TOPIC_OUT_V1,
    val kafkaTopicInV2: String = KAFKA_TOPIC_IN_V2,
    val kafkaTopicOutV2: String = KAFKA_TOPIC_OUT_V2,
) {
    companion object {
        private const val KAFKA_HOST_VAR = "KAFKA_HOSTS"
        private const val KAFKA_TOPIC_IN_V1_VAR = "KAFKA_TOPIC_IN_V1"
        private const val KAFKA_TOPIC_OUT_V1_VAR = "KAFKA_TOPIC_OUT_V1"
        private const val KAFKA_TOPIC_IN_V2_VAR = "KAFKA_TOPIC_IN_V2"
        private const val KAFKA_TOPIC_OUT_V2_VAR = "KAFKA_TOPIC_OUT_V2"
        private const val KAFKA_GROUP_ID_VAR = "KAFKA_GROUP_ID"

        // by lazy – к переменной окружения будет происходить обращение только в случае обращения к переменной KAFKA_HOSTS
        val KAFKA_HOSTS by lazy { (System.getenv(KAFKA_HOST_VAR) ?: "").split("\\s*[,;]\\s*") }
        val KAFKA_GROUP_ID by lazy { System.getenv(KAFKA_GROUP_ID_VAR) ?: "task-tracker" }
        val KAFKA_TOPIC_IN_V1 by lazy { System.getenv(KAFKA_TOPIC_IN_V1_VAR) ?: "task-tracker-in-v1" }
        val KAFKA_TOPIC_OUT_V1 by lazy { System.getenv(KAFKA_TOPIC_OUT_V1_VAR) ?: "task-tracker-out-v1" }
        val KAFKA_TOPIC_IN_V2 by lazy { System.getenv(KAFKA_TOPIC_IN_V2_VAR) ?: "task-tracker-in-v2" }
        val KAFKA_TOPIC_OUT_V2 by lazy { System.getenv(KAFKA_TOPIC_OUT_V2_VAR) ?: "task-tracker-out-v2" }
    }
}
