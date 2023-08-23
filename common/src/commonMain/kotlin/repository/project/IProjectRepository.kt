package me.neversleeps.common.repository.project

interface IProjectRepository {
    suspend fun createProject(request: DbProjectRequest): DbProjectResponse
    suspend fun readProject(request: DbProjectIdRequest): DbProjectResponse
    suspend fun updateProject(request: DbProjectRequest): DbProjectResponse
    suspend fun deleteProject(request: DbProjectIdRequest): DbProjectResponse
    suspend fun searchProjects(request: DbProjectFilterRequest): DbProjectsResponse

    companion object {
        val NONE = object : IProjectRepository {
            override suspend fun createProject(request: DbProjectRequest): DbProjectResponse {
                TODO("Not yet implemented")
            }

            override suspend fun readProject(request: DbProjectIdRequest): DbProjectResponse {
                TODO("Not yet implemented")
            }

            override suspend fun updateProject(request: DbProjectRequest): DbProjectResponse {
                TODO("Not yet implemented")
            }

            override suspend fun deleteProject(request: DbProjectIdRequest): DbProjectResponse {
                TODO("Not yet implemented")
            }

            override suspend fun searchProjects(request: DbProjectFilterRequest): DbProjectsResponse {
                TODO("Not yet implemented")
            }
        }
    }
}
