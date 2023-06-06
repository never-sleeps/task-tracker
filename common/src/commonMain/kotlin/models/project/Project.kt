package models.project

import models.user.UserId

data class Project(
    var id: ProjectId = ProjectId.NONE,

    var title: String = "",
    var description: String = "",
    var createdBy: UserId = UserId.NONE,
    val permissions: MutableSet<ProjectPermission> = mutableSetOf()
)
