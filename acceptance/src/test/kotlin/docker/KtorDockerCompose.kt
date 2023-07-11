package me.neversleeps.acceptance.blackbox.docker

import me.neversleeps.acceptance.blackbox.fixture.docker.AbstractDockerCompose

object KtorDockerCompose : AbstractDockerCompose(
    "app-ktor_1",
    8080,
    "ktor/docker-compose-ktor.yml",
)
