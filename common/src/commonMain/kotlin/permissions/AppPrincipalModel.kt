package me.neversleeps.common.permissions

import me.neversleeps.common.models.user.UserId

data class AppPrincipalModel(
    val id: UserId = UserId.NONE,
    val fname: String = "",
    val mname: String = "",
    val lname: String = "",
    val groups: Set<AppUserGroups> = emptySet()
) {
    companion object {
        val NONE = AppPrincipalModel()
    }
}
