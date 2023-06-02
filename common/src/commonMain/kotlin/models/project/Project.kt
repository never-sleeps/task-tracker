package ru.otus.otuskotlin.marketplace.common.models.project

import ru.otus.otuskotlin.marketplace.common.models.user.UserId

data class Project(
    var id: ProjectId = ProjectId.NONE,

    var title: String = "",
    var description: String = "",
    var createdBy: UserId = UserId.NONE
)
