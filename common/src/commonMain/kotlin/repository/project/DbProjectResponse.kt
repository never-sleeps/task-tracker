package me.neversleeps.common.repository.project

import me.neversleeps.common.models.AppError
import me.neversleeps.common.models.project.Project

data class DbProjectResponse(
    override val data: Project?,
    override val isSuccess: Boolean,
    override val errors: List<AppError> = emptyList()
): IDbResponse<Project> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbProjectResponse(null, true)
        fun success(result: Project) = DbProjectResponse(result, true)
        fun error(errors: List<AppError>) = DbProjectResponse(null, false, errors)
        fun error(error: AppError) = DbProjectResponse(null, false, listOf(error))
    }
}
