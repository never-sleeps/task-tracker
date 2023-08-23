package me.neversleeps.acceptance.blackbox.test.action.v2.utils // ktlint-disable filename

import me.neversleeps.api.multiplatform.v1.models.ProjectCreateObject
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectDebugStub
import me.neversleeps.api.multiplatform.v1.models.ProjectResponseObject
import me.neversleeps.api.multiplatform.v1.models.WorkMode

val projectResponseObject = ProjectResponseObject(
    title = "some title",
    description = "some description",
    createdBy = "8098d197-a58f-4ae4-b602-8db6a146fb17",
    permissions = emptySet(),
)

val projectCreateObject = ProjectCreateObject(
    title = "some title",
    description = "some description",
    createdBy = "8098d197-a58f-4ae4-b602-8db6a146fb17",
)

val projectCreateRequest = ProjectCreateRequest(
    requestId = "12345",
    requestType = "createProject",
    stub = ProjectDebugStub.SUCCESS,
    mode = WorkMode.STUB,
    data = projectCreateObject,
)
