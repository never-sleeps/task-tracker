package me.neversleeps.auth

import me.neversleeps.common.models.project.ProjectPermissionClient
import me.neversleeps.common.permissions.AppPrincipalRelations
import me.neversleeps.common.permissions.AppUserPermissions

fun resolveFrontPermissions(
    permissions: Iterable<AppUserPermissions>,
    relations: Iterable<AppPrincipalRelations>,
) = mutableSetOf<ProjectPermissionClient>()
    .apply {
        for (permission in permissions) {
            for (relation in relations) {
                accessTable[permission]?.get(relation)?.let { this@apply.add(it) }
            }
        }
    }
    .toSet()

private val accessTable = mapOf(
    // READ
    AppUserPermissions.READ_OWN to mapOf(
        AppPrincipalRelations.OWN to ProjectPermissionClient.READ,
    ),
    AppUserPermissions.READ_GROUP to mapOf(
        AppPrincipalRelations.GROUP to ProjectPermissionClient.READ,
    ),
    AppUserPermissions.READ_PUBLIC to mapOf(
        AppPrincipalRelations.PUBLIC to ProjectPermissionClient.READ,
    ),
    AppUserPermissions.READ_CANDIDATE to mapOf(
        AppPrincipalRelations.MODERATABLE to ProjectPermissionClient.READ,
    ),

    // UPDATE
    AppUserPermissions.UPDATE_OWN to mapOf(
        AppPrincipalRelations.OWN to ProjectPermissionClient.UPDATE,
    ),
    AppUserPermissions.UPDATE_PUBLIC to mapOf(
        AppPrincipalRelations.MODERATABLE to ProjectPermissionClient.UPDATE,
    ),
    AppUserPermissions.UPDATE_CANDIDATE to mapOf(
        AppPrincipalRelations.MODERATABLE to ProjectPermissionClient.UPDATE,
    ),

    // DELETE
    AppUserPermissions.DELETE_OWN to mapOf(
        AppPrincipalRelations.OWN to ProjectPermissionClient.DELETE,
    ),
    AppUserPermissions.DELETE_PUBLIC to mapOf(
        AppPrincipalRelations.MODERATABLE to ProjectPermissionClient.DELETE,
    ),
    AppUserPermissions.DELETE_CANDIDATE to mapOf(
        AppPrincipalRelations.MODERATABLE to ProjectPermissionClient.DELETE,
    ),
)
