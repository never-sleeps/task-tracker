package me.neversleeps.acceptance.blackbox.docker

import me.neversleeps.acceptance.blackbox.fixture.docker.AbstractDockerCompose

object SpringDockerCompose : AbstractDockerCompose(
    "app-spring_1",
    8080,
    "spring/docker-compose-spring.yml",
)
