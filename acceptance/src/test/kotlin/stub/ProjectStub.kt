package me.neversleeps.acceptance.stub

import me.neversleeps.api.multiplatform.v1.models.ProjectCreateObject
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectDeleteRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectDeleteResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectReadRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectReadResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectResponseObject
import me.neversleeps.api.multiplatform.v1.models.ProjectUpdateObject
import me.neversleeps.api.multiplatform.v1.models.ProjectUpdateRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectUpdateResponse

object ProjectStub {
    val createRequest = ProjectCreateRequest(
        requestId = "request-id",
        requestType = "createProject",
        data = ProjectCreateObject(
            title = "some title",
            description = "some description",
            createdBy = "some-author-id",
        ),
    )

    val updateRequest = ProjectUpdateRequest(
        requestId = "request-id",
        requestType = "updateProject",
        data = ProjectUpdateObject(
            id = "project-id",
            title = "some title",
            description = "some description",
            createdBy = "some-author-id",
        ),
    )

    val readRequest = ProjectReadRequest(
        requestId = "request-id",
        requestType = "readProject",
        id = "project-id",
    )

    val deleteRequest = ProjectDeleteRequest(
        requestId = "request-id",
        requestType = "deleteProject",
        id = "project-id",
    )

    val createResponse = ProjectCreateResponse(
        requestId = "request-id",
        responseType = "createProject",
        project = ProjectResponseObject(
            id = "project-id",
            title = "some title",
            description = "some description",
            createdBy = "some-author-id",
        ),
    )

    val updateResponse = ProjectUpdateResponse(
        requestId = "request-id",
        responseType = "readProject",
        project = ProjectResponseObject(
            id = "project-id",
            title = "some title",
            description = "some description",
            createdBy = "some-author-id",
        ),
    )

    val readResponse = ProjectReadResponse(
        requestId = "request-id",
        responseType = "readProject",
        project = ProjectResponseObject(
            id = "project-id",
            title = "some title",
            description = "some description",
            createdBy = "some-author-id",
        ),
    )

    val deleteResponse = ProjectDeleteResponse(
        requestId = "request-id",
        responseType = "readProject",
        project = ProjectResponseObject(
            id = "project-id",
            title = "some title",
            description = "some description",
            createdBy = "some-author-id",
        ),
    )
}
