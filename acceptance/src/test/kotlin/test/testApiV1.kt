package me.neversleeps.acceptance.blackbox.test // ktlint-disable filename

import io.kotest.core.spec.style.FunSpec
import me.neversleeps.acceptance.blackbox.fixture.client.Client
import me.neversleeps.acceptance.blackbox.test.action.v1.createProject

fun FunSpec.testApiV1(client: Client, prefix: String = "") {
    context("${prefix}v1") {
        test("Create Project ok") {
            client.createProject()
        }
    }
}
