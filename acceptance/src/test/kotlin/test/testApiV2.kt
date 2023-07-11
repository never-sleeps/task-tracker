package me.neversleeps.acceptance.blackbox.test // ktlint-disable filename

import io.kotest.core.spec.style.FunSpec
import me.neversleeps.acceptance.blackbox.fixture.client.Client
import me.neversleeps.acceptance.blackbox.test.action.v2.createProject

fun FunSpec.testApiV2(client: Client, prefix: String = "") {
    context("${prefix}v2") {
        test("Create Project ok") {
            client.createProject()
        }
    }
}
