package me.neversleeps.common.models.project

import me.neversleeps.common.models.user.UserId

data class Project(
    var id: ProjectId = ProjectId.NONE,

    var title: String = "",
    var description: String = "",
    var createdBy: UserId = UserId.NONE,
    val permissions: MutableSet<ProjectPermission> = mutableSetOf()
)
