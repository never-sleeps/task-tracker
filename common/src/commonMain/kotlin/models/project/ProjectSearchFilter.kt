package models.project

import models.user.UserId

data class ProjectSearchFilter(
    var searchText: String = "",
    var createdBy: UserId = UserId.NONE
)
