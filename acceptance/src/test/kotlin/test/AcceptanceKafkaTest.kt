package me.neversleeps.acceptance.blackbox.test

import io.kotest.core.annotation.Ignored
import me.neversleeps.acceptance.blackbox.docker.KafkaDockerCompose
import me.neversleeps.acceptance.blackbox.fixture.BaseFunSpec
import me.neversleeps.acceptance.blackbox.fixture.client.KafkaClient

// java.net.UnknownHostException: kafka: nodename nor servname provided, or not known
@Ignored
class AcceptanceKafkaTest : BaseFunSpec(KafkaDockerCompose, {
    val client = KafkaClient(KafkaDockerCompose)

    testApiV1(client)
    testApiV2(client)
})
