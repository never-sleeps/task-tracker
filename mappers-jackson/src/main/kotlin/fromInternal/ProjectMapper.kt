package me.neversleeps.mappers.jackson.fromInternal

import me.neversleeps.api.jackson.v1.models.ProjectPermission
import me.neversleeps.api.jackson.v1.models.ProjectResponseObject
import me.neversleeps.common.models.project.Project

fun List<Project>.toTransport(): List<ProjectResponseObject> =
    this.map { it.toTransport() }

fun Project.toTransport(): ProjectResponseObject = ProjectResponseObject(
    id = this.id.asString(),
    title = this.title,
    description = this.description,
    createdBy = this.createdBy.asString(),
    permissions = this.permissions.toTransport(),
)

fun Set<me.neversleeps.common.models.project.ProjectPermission>.toTransport(): Set<ProjectPermission> = this
    .map { it.toTransport() }
    .toSet()

fun me.neversleeps.common.models.project.ProjectPermission.toTransport(): ProjectPermission = when (this) {
    me.neversleeps.common.models.project.ProjectPermission.READ -> ProjectPermission.READ
    me.neversleeps.common.models.project.ProjectPermission.UPDATE -> ProjectPermission.UPDATE
    me.neversleeps.common.models.project.ProjectPermission.DELETE -> ProjectPermission.DELETE
}
