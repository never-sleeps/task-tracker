package me.neversleeps.auth

import me.neversleeps.common.permissions.AppUserGroups
import me.neversleeps.common.permissions.AppUserPermissions

fun resolveChainPermissions(
    groups: Iterable<AppUserGroups>,
) = mutableSetOf<AppUserPermissions>()
    .apply {
        addAll(groups.flatMap { groupPermissionsAdmits[it] ?: emptySet() })
        removeAll(groups.flatMap { groupPermissionsDenys[it] ?: emptySet() }.toSet())
    }
    .toSet()

private val groupPermissionsAdmits = mapOf(
    AppUserGroups.USER to setOf(
        AppUserPermissions.READ_OWN,
        AppUserPermissions.READ_PUBLIC,
        AppUserPermissions.CREATE_OWN,
        AppUserPermissions.UPDATE_OWN,
        AppUserPermissions.DELETE_OWN,
        AppUserPermissions.OFFER_FOR_OWN,
    ),
    AppUserGroups.MODERATOR_MP to setOf(),
    AppUserGroups.ADMIN_AD to setOf(),
    AppUserGroups.TEST to setOf(),
    AppUserGroups.BAN_AD to setOf(),
)

private val groupPermissionsDenys = mapOf(
    AppUserGroups.USER to setOf(),
    AppUserGroups.MODERATOR_MP to setOf(),
    AppUserGroups.ADMIN_AD to setOf(),
    AppUserGroups.TEST to setOf(),
    AppUserGroups.BAN_AD to setOf(
        AppUserPermissions.UPDATE_OWN,
        AppUserPermissions.CREATE_OWN,
    ),
)
