package me.neversleeps.acceptance.blackbox.test

import me.neversleeps.acceptance.blackbox.docker.RabbitDockerCompose
import me.neversleeps.acceptance.blackbox.fixture.BaseFunSpec
import me.neversleeps.acceptance.blackbox.fixture.client.RabbitClient

class AcceptanceRabbitTest : BaseFunSpec(RabbitDockerCompose, {
    val client = RabbitClient(RabbitDockerCompose)

    testApiV1(client)
    testApiV2(client)
})
