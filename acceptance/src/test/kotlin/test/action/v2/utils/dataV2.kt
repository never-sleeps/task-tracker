package me.neversleeps.acceptance.blackbox.test.action.v2.utils // ktlint-disable filename

import me.neversleeps.api.multiplatform.v1.models.ProjectCreateObject
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectDebugStub
import me.neversleeps.api.multiplatform.v1.models.ProjectPermission
import me.neversleeps.api.multiplatform.v1.models.ProjectResponseObject

val projectResponseObject = ProjectResponseObject(
    title = "some title",
    description = "some description",
    createdBy = "8098d197-a58f-4ae4-b602-8db6a146fb17",
    permissions = setOf(ProjectPermission.READ, ProjectPermission.UPDATE, ProjectPermission.DELETE),
)

val projectCreateObject = ProjectCreateObject(
    title = "some title",
    description = "some description",
    createdBy = "some-author-id",
)
val projectCreateRequest = ProjectCreateRequest(
    requestId = "12345",
    requestType = "createProject",
    stub = ProjectDebugStub.SUCCESS,
    data = projectCreateObject,
)
