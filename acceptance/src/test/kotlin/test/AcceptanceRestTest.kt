package me.neversleeps.acceptance.blackbox.test

import io.kotest.core.annotation.Ignored
import me.neversleeps.acceptance.blackbox.docker.KtorDockerCompose
import me.neversleeps.acceptance.blackbox.docker.SpringDockerCompose
import me.neversleeps.acceptance.blackbox.fixture.BaseFunSpec
import me.neversleeps.acceptance.blackbox.fixture.client.RestClient
import me.neversleeps.acceptance.blackbox.fixture.docker.DockerCompose

@Ignored
open class AcceptanceRestTestBase(dockerCompose: DockerCompose) :
    BaseFunSpec(
        dockerCompose,
        {
            val restClient = RestClient(dockerCompose)
            testApiV1(restClient, "rest ")
            testApiV2(restClient, "rest ")
        },
    )

class AcceptanceRestSpringTest : AcceptanceRestTestBase(SpringDockerCompose)

class AcceptanceRestKtorTest : AcceptanceRestTestBase(KtorDockerCompose)
