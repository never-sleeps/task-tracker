package me.neversleeps.mappers.multiplatform.fromTransport

import me.neversleeps.api.multiplatform.v1.models.ProjectCreateObject
import me.neversleeps.api.multiplatform.v1.models.ProjectDebugStub
import me.neversleeps.api.multiplatform.v1.models.ProjectSearchFilter
import me.neversleeps.api.multiplatform.v1.models.ProjectUpdateObject
import me.neversleeps.common.models.AppLock
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectId

fun ProjectDebugStub?.toInternal(): me.neversleeps.common.stubs.ProjectDebugStub = when (this) {
    ProjectDebugStub.SUCCESS -> me.neversleeps.common.stubs.ProjectDebugStub.SUCCESS
    ProjectDebugStub.NOT_FOUND -> me.neversleeps.common.stubs.ProjectDebugStub.NOT_FOUND
    ProjectDebugStub.BAD_ID -> me.neversleeps.common.stubs.ProjectDebugStub.BAD_ID
    ProjectDebugStub.BAD_TITLE -> me.neversleeps.common.stubs.ProjectDebugStub.BAD_TITLE
    ProjectDebugStub.BAD_SEARCH_TEXT -> me.neversleeps.common.stubs.ProjectDebugStub.BAD_SEARCH_TEXT
    ProjectDebugStub.BAD_SEARCHСREATED_BY -> me.neversleeps.common.stubs.ProjectDebugStub.BAD_SEARCH_CREATED_BY
    ProjectDebugStub.PERMISSION_ERROR -> me.neversleeps.common.stubs.ProjectDebugStub.PERMISSION_ERROR
    null -> me.neversleeps.common.stubs.ProjectDebugStub.NONE
}

fun ProjectCreateObject.toInternal(): Project = Project(
    title = this.title ?: "",
    description = this.description ?: "",
    createdBy = this.createdBy.toUserId(),
)

fun ProjectUpdateObject.toInternal(lock: String?): Project = Project(
    id = this.id.toProjectId(),
    title = this.title ?: "",
    description = this.description ?: "",
    createdBy = this.createdBy.toUserId(),
    lock = lock?.let { AppLock(it) } ?: AppLock.NONE,
)

fun ProjectSearchFilter.toInternal(): me.neversleeps.common.models.project.ProjectSearchFilter =
    me.neversleeps.common.models.project.ProjectSearchFilter(
        searchText = this.searchText ?: "",
        createdBy = this.createdBy.toUserId(),
    )

fun String?.toProjectId() = this?.let { ProjectId(it) } ?: ProjectId.NONE

fun String?.toProjectWithId(lock: String? = null) =
    Project(
        id = this.toProjectId(),
        lock = lock?.let { AppLock(it) } ?: AppLock.NONE,
    )
