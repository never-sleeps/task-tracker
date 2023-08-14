package me.neversleeps.`in`.memory.project

import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.common.models.project.ProjectPermission
import me.neversleeps.common.models.user.UserId

abstract class BaseInitProjects(
    val op: String,
) : IInitObjects<Project> {

    fun createInitTestModel(
        suf: String,
        createdBy: UserId = UserId("owner-123"),
    ) = Project(
        id = ProjectId("project-repository-$op-$suf"),
        title = "$suf stub",
        description = "$suf stub description",
        createdBy = createdBy,
        permissions = mutableSetOf(ProjectPermission.UPDATE),
    )
}
