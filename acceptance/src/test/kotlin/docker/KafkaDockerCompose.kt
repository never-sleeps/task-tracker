package me.neversleeps.acceptance.blackbox.docker

import me.neversleeps.acceptance.blackbox.fixture.docker.AbstractDockerCompose

object KafkaDockerCompose : AbstractDockerCompose(
    "kafka_1",
    9093,
    "kafka/docker-compose-kafka.yml",
)
