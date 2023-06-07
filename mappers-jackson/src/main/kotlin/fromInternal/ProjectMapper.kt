package fromInternal

import me.neversleeps.api.jackson.v1.models.ProjectPermission
import me.neversleeps.api.jackson.v1.models.ProjectResponseObject
import models.project.Project

fun List<Project>.toTransport(): List<ProjectResponseObject> =
    this.map { it.toTransport() }

fun Project.toTransport(): ProjectResponseObject = ProjectResponseObject(
    id = this.id.asString(),
    title = this.title,
    description = this.description,
    createdBy = this.createdBy.asString(),
    permissions = this.permissions.toTransport(),
)

fun Set<models.project.ProjectPermission>.toTransport(): Set<ProjectPermission> = this
    .map { it.toTransport() }
    .toSet()

fun models.project.ProjectPermission.toTransport(): ProjectPermission = when (this) {
    models.project.ProjectPermission.READ -> ProjectPermission.READ
    models.project.ProjectPermission.UPDATE -> ProjectPermission.UPDATE
    models.project.ProjectPermission.DELETE -> ProjectPermission.DELETE
}
