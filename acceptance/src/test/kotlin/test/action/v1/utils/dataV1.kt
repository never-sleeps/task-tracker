package me.neversleeps.acceptance.blackbox.test.action.v1.utils // ktlint-disable filename

import me.neversleeps.api.jackson.v1.models.ProjectCreateObject
import me.neversleeps.api.jackson.v1.models.ProjectCreateRequest
import me.neversleeps.api.jackson.v1.models.ProjectDebugStub
import me.neversleeps.api.jackson.v1.models.ProjectPermission
import me.neversleeps.api.jackson.v1.models.ProjectResponseObject

val projectResponseObject = ProjectResponseObject(
    title = "some title",
    description = "some description",
    createdBy = "8098d197-a58f-4ae4-b602-8db6a146fb17",
    permissions = setOf(ProjectPermission.READ, ProjectPermission.UPDATE, ProjectPermission.DELETE),
)

val projectCreateObject = ProjectCreateObject(
    title = "some title",
    description = "some description",
    createdBy = "8098d197-a58f-4ae4-b602-8db6a146fb17",
)

val projectCreateRequest = ProjectCreateRequest(
    requestId = "12345",
    requestType = "create",
    stub = ProjectDebugStub.SUCCESS,
    data = projectCreateObject,
)
