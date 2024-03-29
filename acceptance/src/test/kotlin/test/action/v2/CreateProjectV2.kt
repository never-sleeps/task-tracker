package me.neversleeps.acceptance.blackbox.test.action.v2

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import me.neversleeps.acceptance.blackbox.fixture.client.Client
import me.neversleeps.acceptance.blackbox.test.action.v2.utils.haveSuccessResult
import me.neversleeps.acceptance.blackbox.test.action.v2.utils.projectCreateRequest
import me.neversleeps.acceptance.blackbox.test.action.v2.utils.projectResponseObject
import me.neversleeps.acceptance.blackbox.test.action.v2.utils.sendAndReceive
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectResponseObject

suspend fun Client.createProject(request: ProjectCreateRequest = projectCreateRequest): ProjectResponseObject =
    createProject(request) {
        it should haveSuccessResult
        it.project shouldBe projectResponseObject.copy(id = it.project?.id)
        it.project!!
    }

suspend fun <T> Client.createProject(
    request: ProjectCreateRequest = projectCreateRequest,
    block: (ProjectCreateResponse) -> T,
): T =
    withClue("createProjectV2: $request") {
        val response = sendAndReceive("project/create", request) as ProjectCreateResponse
        response.asClue(block)
    }
