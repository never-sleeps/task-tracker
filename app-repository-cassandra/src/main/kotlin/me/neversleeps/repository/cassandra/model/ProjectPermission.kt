package me.neversleeps.repository.cassandra.model

import me.neversleeps.common.models.project.ProjectPermission

enum class ProjectPermissionEntity {
    READ,
    UPDATE,
    DELETE,
}

fun ProjectPermission?.toInnerModel(): ProjectPermissionEntity? =
    when (this) {
        ProjectPermission.READ -> ProjectPermissionEntity.READ
        ProjectPermission.UPDATE -> ProjectPermissionEntity.UPDATE
        ProjectPermission.DELETE -> ProjectPermissionEntity.DELETE
        null -> null
    }

fun ProjectPermissionEntity?.fromInnerModel(): ProjectPermission? =
    when (this) {
        ProjectPermissionEntity.READ -> ProjectPermission.READ
        ProjectPermissionEntity.UPDATE -> ProjectPermission.UPDATE
        ProjectPermissionEntity.DELETE -> ProjectPermission.DELETE
        null -> null
    }
