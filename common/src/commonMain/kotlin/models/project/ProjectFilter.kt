package ru.otus.otuskotlin.marketplace.common.models.project

import ru.otus.otuskotlin.marketplace.common.models.user.UserId

data class ProjectFilter(
    var searchText: String = "",
    var createdBy: UserId = UserId.NONE
)
