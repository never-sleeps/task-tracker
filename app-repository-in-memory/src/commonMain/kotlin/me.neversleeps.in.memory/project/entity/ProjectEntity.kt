package me.neversleeps.`in`.memory.project.entity // ktlint-disable package-name

import me.neversleeps.common.models.AppLock
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.common.models.project.ProjectPermission
import me.neversleeps.common.models.user.UserId

data class ProjectEntity(
    var id: String? = null,
    var title: String? = null,
    var description: String? = null,
    var createdBy: String? = null,
    var permissions: String? = null,
    val lock: String? = null,
) {
    constructor(model: Project) : this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        title = model.title.takeIf { it.isNotBlank() },
        description = model.description.takeIf { it.isNotBlank() },
        createdBy = model.createdBy.asString().takeIf { it.isNotBlank() },
        permissions = model.permissions.firstOrNull()?.name ?: ProjectPermission.READ.name,
        lock = model.lock.asString().takeIf { it.isNotBlank() },
    )

    fun toInternal() = Project(
        id = id?.let { ProjectId(it) } ?: ProjectId.NONE,
        title = title ?: "",
        description = description ?: "",
        createdBy = createdBy?.let { UserId(it) } ?: UserId.NONE,
        permissions = permissions?.let { ProjectPermission.valueOf(it) }?.let { mutableSetOf(it) } ?: mutableSetOf(ProjectPermission.READ),
        lock = lock?.let { AppLock(it) } ?: AppLock.NONE,
    )
}
