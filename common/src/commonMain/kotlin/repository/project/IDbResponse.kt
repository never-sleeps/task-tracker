package me.neversleeps.common.repository.project

import me.neversleeps.common.models.AppError

interface IDbResponse<T> {
    val data: T?
    val isSuccess: Boolean
    val errors: List<AppError>
}
