package me.neversleeps.auth

import me.neversleeps.common.models.AppCommand
import me.neversleeps.common.permissions.AppPrincipalRelations
import me.neversleeps.common.permissions.AppUserPermissions

fun checkPermitted(
    command: AppCommand,
    relations: Iterable<AppPrincipalRelations>,
    permissions: Iterable<AppUserPermissions>,
) =
    relations.asSequence().flatMap { relation ->
        permissions.map { permission ->
            AccessTableConditions(
                command = command,
                permission = permission,
                relation = relation,
            )
        }
    }.any {
        accessTable[it] != null
    }

private data class AccessTableConditions(
    val command: AppCommand,
    val permission: AppUserPermissions,
    val relation: AppPrincipalRelations
)

private val accessTable = mapOf(
    // Create
    AccessTableConditions(
        command = AppCommand.CREATE,
        permission = AppUserPermissions.CREATE_OWN,
        relation = AppPrincipalRelations.NEW,
    ) to true,

    // Read
    AccessTableConditions(
        command = AppCommand.READ,
        permission = AppUserPermissions.READ_OWN,
        relation = AppPrincipalRelations.OWN,
    ) to true,
    AccessTableConditions(
        command = AppCommand.READ,
        permission = AppUserPermissions.READ_PUBLIC,
        relation = AppPrincipalRelations.PUBLIC,
    ) to true,

    // Update
    AccessTableConditions(
        command = AppCommand.UPDATE,
        permission = AppUserPermissions.UPDATE_OWN,
        relation = AppPrincipalRelations.OWN,
    ) to true,

    // Delete
    AccessTableConditions(
        command = AppCommand.DELETE,
        permission = AppUserPermissions.DELETE_OWN,
        relation = AppPrincipalRelations.OWN,
    ) to true,

    // Search
    AccessTableConditions(
        command = AppCommand.SEARCH,
        permission = AppUserPermissions.OFFER_FOR_OWN,
        relation = AppPrincipalRelations.OWN,
    ) to true,
)
