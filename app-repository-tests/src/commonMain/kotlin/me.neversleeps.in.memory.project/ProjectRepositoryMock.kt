package me.neversleeps.`in`.memory.project

import me.neversleeps.common.repository.project.DbProjectFilterRequest
import me.neversleeps.common.repository.project.DbProjectIdRequest
import me.neversleeps.common.repository.project.DbProjectRequest
import me.neversleeps.common.repository.project.DbProjectResponse
import me.neversleeps.common.repository.project.DbProjectsResponse
import me.neversleeps.common.repository.project.IProjectRepository

class ProjectRepositoryMock(
    private val invokeCreateProject: (DbProjectRequest) -> DbProjectResponse = { DbProjectResponse.MOCK_SUCCESS_EMPTY },
    private val invokeReadProject: (DbProjectIdRequest) -> DbProjectResponse = { DbProjectResponse.MOCK_SUCCESS_EMPTY },
    private val invokeUpdateProject: (DbProjectRequest) -> DbProjectResponse = { DbProjectResponse.MOCK_SUCCESS_EMPTY },
    private val invokeDeleteProject: (DbProjectIdRequest) -> DbProjectResponse = { DbProjectResponse.MOCK_SUCCESS_EMPTY },
    private val invokeSearchProject: (DbProjectFilterRequest) -> DbProjectsResponse = { DbProjectsResponse.MOCK_SUCCESS_EMPTY },
): IProjectRepository {

    override suspend fun createProject(request: DbProjectRequest): DbProjectResponse {
        return invokeCreateProject(request)
    }

    override suspend fun readProject(request: DbProjectIdRequest): DbProjectResponse {
        return invokeReadProject(request)
    }

    override suspend fun updateProject(request: DbProjectRequest): DbProjectResponse {
        return invokeUpdateProject(request)
    }

    override suspend fun deleteProject(request: DbProjectIdRequest): DbProjectResponse {
        return invokeDeleteProject(request)
    }

    override suspend fun searchProjects(request: DbProjectFilterRequest): DbProjectsResponse {
        return invokeSearchProject(request)
    }
}
