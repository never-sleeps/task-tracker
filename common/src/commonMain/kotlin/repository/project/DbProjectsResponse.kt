package me.neversleeps.common.repository.project

import me.neversleeps.common.models.AppError
import me.neversleeps.common.models.project.Project

data class DbProjectsResponse(
    override val data: List<Project>?,
    override val isSuccess: Boolean,
    override val errors: List<AppError> = emptyList(),
): IDbResponse<List<Project>> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbProjectsResponse(emptyList(), true)
        fun success(result: List<Project>) = DbProjectsResponse(result, true)
        fun error(errors: List<AppError>) = DbProjectsResponse(null, false, errors)
        fun error(error: AppError) = DbProjectsResponse(null, false, listOf(error))
    }
}
