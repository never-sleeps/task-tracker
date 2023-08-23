package me.neversleeps.common.repository.project

import me.neversleeps.common.helpers.errorRepositoryConcurrency
import me.neversleeps.common.models.AppError
import me.neversleeps.common.models.AppLock
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.helpers.errorEmptyId as appErrorEmptyId
import me.neversleeps.common.helpers.errorNotFound as appErrorNotFound

data class DbProjectResponse(
    override val data: Project?,
    override val isSuccess: Boolean,
    override val errors: List<AppError> = emptyList(),
) : IDbResponse<Project> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbProjectResponse(null, true)
        fun success(result: Project) = DbProjectResponse(result, true)
        fun error(errors: List<AppError>) = DbProjectResponse(null, false, errors)
        fun error(error: AppError, data: Project? = null) = DbProjectResponse(data, false, listOf(error))

        fun errorConcurrent(lock: AppLock, project: Project?) =
            error(
                errorRepositoryConcurrency(lock, project?.lock?.let { AppLock(it.asString()) }),
                project,
            )

        val errorEmptyId = error(appErrorEmptyId)
        val errorNotFound = error(appErrorNotFound)
    }
}
