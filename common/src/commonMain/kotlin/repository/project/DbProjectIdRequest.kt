package me.neversleeps.common.repository.project

import me.neversleeps.common.models.AppLock
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectId

data class DbProjectIdRequest(
    val id: ProjectId,
    val lock: AppLock = AppLock.NONE,
) {
    constructor(project: Project) : this(project.id, project.lock)
}
