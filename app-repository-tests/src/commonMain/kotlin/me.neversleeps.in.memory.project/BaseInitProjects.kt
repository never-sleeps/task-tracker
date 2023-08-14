package me.neversleeps.`in`.memory.project

import me.neversleeps.common.models.AppLock
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.common.models.project.ProjectPermission
import me.neversleeps.common.models.user.UserId

abstract class BaseInitProjects(
    val op: String,
) : IInitObjects<Project> {

    open val lockOld: AppLock = AppLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: AppLock = AppLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        createdBy: UserId = UserId("owner-123"),
        lock: AppLock = lockOld,
    ) = Project(
        id = ProjectId("project-repository-$op-$suf"),
        title = "$suf stub",
        description = "$suf stub description",
        createdBy = createdBy,
        permissions = mutableSetOf(ProjectPermission.UPDATE),
        lock = lock,
    )
}
