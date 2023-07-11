package me.neversleeps.acceptance.blackbox.test

import io.kotest.core.annotation.Ignored
import me.neversleeps.acceptance.blackbox.docker.KafkaDockerCompose
import me.neversleeps.acceptance.blackbox.fixture.BaseFunSpec
import me.neversleeps.acceptance.blackbox.fixture.client.KafkaClient

@Ignored
class AcceptanceKafkaTest : BaseFunSpec(KafkaDockerCompose, {
    val client = KafkaClient(KafkaDockerCompose)

    testApiV1(client)
    testApiV2(client)
})
