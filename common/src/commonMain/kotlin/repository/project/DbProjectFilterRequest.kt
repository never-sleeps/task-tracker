package me.neversleeps.common.repository.project

import me.neversleeps.common.models.user.UserId

data class DbProjectFilterRequest(
    val searchText: String = "",
    val createdBy: UserId = UserId.NONE,
)
