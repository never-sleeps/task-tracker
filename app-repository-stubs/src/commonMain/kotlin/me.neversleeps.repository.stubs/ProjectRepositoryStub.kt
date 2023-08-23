package me.neversleeps.repository.stubs

import ProjectStub
import me.neversleeps.common.models.user.UserId
import me.neversleeps.common.repository.project.DbProjectFilterRequest
import me.neversleeps.common.repository.project.DbProjectIdRequest
import me.neversleeps.common.repository.project.DbProjectRequest
import me.neversleeps.common.repository.project.DbProjectResponse
import me.neversleeps.common.repository.project.DbProjectsResponse
import me.neversleeps.common.repository.project.IProjectRepository

class ProjectRepositoryStub : IProjectRepository {
    override suspend fun createProject(request: DbProjectRequest): DbProjectResponse {
        return DbProjectResponse(
            data = ProjectStub.prepareResult { },
            isSuccess = true,
        )
    }

    override suspend fun readProject(request: DbProjectIdRequest): DbProjectResponse {
        return DbProjectResponse(
            data = ProjectStub.prepareResult { },
            isSuccess = true,
        )
    }

    override suspend fun updateProject(request: DbProjectRequest): DbProjectResponse {
        return DbProjectResponse(
            data = ProjectStub.prepareResult { },
            isSuccess = true,
        )
    }

    override suspend fun deleteProject(request: DbProjectIdRequest): DbProjectResponse {
        return DbProjectResponse(
            data = ProjectStub.prepareResult { },
            isSuccess = true,
        )
    }

    override suspend fun searchProjects(request: DbProjectFilterRequest): DbProjectsResponse {
        return DbProjectsResponse(
            data = ProjectStub.prepareSearchList(searchText = "", createdBy = UserId("someone")),
            isSuccess = true,
        )
    }
}
