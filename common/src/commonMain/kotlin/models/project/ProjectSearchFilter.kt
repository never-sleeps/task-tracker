package me.neversleeps.common.models.project

import me.neversleeps.common.models.user.UserId

data class ProjectSearchFilter(
    var searchText: String = "",
    var createdBy: UserId = UserId.NONE,
    var searchPermissions: MutableSet<AppSearchPermissions> = mutableSetOf(),
)

enum class AppSearchPermissions {
    OWN,
    PUBLIC,
    REGISTERED,
}
