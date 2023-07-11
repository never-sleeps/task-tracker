package me.neversleeps.acceptance.blackbox.docker

import me.neversleeps.acceptance.blackbox.fixture.docker.AbstractDockerCompose

object RabbitDockerCompose : AbstractDockerCompose(
    "rabbit_1",
    5672,
    "rabbit/docker-compose-rabbit.yml",
) {
    override val user: String
        get() = "guest"

    override val password: String
        get() = "guest"
}
