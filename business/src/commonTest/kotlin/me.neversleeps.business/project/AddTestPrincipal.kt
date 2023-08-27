package me.neversleeps.business.project

import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.user.UserId
import me.neversleeps.common.permissions.AppPrincipalModel
import me.neversleeps.common.permissions.AppUserGroups

fun ProjectContext.addTestPrincipal(userId: UserId = UserId("321")) {
    principal = AppPrincipalModel(
        id = userId,
        groups = setOf(
            AppUserGroups.USER,
            AppUserGroups.TEST,
        ),
    )
}
