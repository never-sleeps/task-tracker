package fromTransport

import me.neversleeps.api.multiplatform.v1.models.ProjectCreateObject
import me.neversleeps.api.multiplatform.v1.models.ProjectDebugStub
import me.neversleeps.api.multiplatform.v1.models.ProjectSearchFilter
import me.neversleeps.api.multiplatform.v1.models.ProjectUpdateObject
import models.project.Project
import models.project.ProjectId

fun ProjectDebugStub?.toInternal(): stubs.ProjectDebugStub = when (this) {
    ProjectDebugStub.SUCCESS -> stubs.ProjectDebugStub.SUCCESS
    ProjectDebugStub.NOT_FOUND -> stubs.ProjectDebugStub.NOT_FOUND
    ProjectDebugStub.BAD_ID -> stubs.ProjectDebugStub.BAD_ID
    ProjectDebugStub.BAD_TITLE -> stubs.ProjectDebugStub.BAD_TITLE
    ProjectDebugStub.BAD_SEARCH_TEXT -> stubs.ProjectDebugStub.BAD_SEARCH_TEXT
    ProjectDebugStub.BAD_SEARCHСREATED_BY -> stubs.ProjectDebugStub.BAD_SEARCH_CREATED_BY
    ProjectDebugStub.PERMISSION_ERROR -> stubs.ProjectDebugStub.PERMISSION_ERROR
    null -> stubs.ProjectDebugStub.NONE
}

fun ProjectCreateObject.toInternal(): Project = Project(
    title = this.title ?: "",
    description = this.description ?: "",
    createdBy = this.createdBy.toUserId(),
)

fun ProjectUpdateObject.toInternal(): Project = Project(
    id = this.id.toProjectId(),
    title = this.title ?: "",
    description = this.description ?: "",
    createdBy = this.createdBy.toUserId(),
)

fun ProjectSearchFilter.toInternal(): models.project.ProjectSearchFilter = models.project.ProjectSearchFilter(
    searchText = this.searchText ?: "",
    createdBy = this.createdBy.toUserId(),
)

fun String?.toProjectId() = this?.let { ProjectId(it) } ?: ProjectId.NONE

fun String?.toProjectWithId() = Project(id = this.toProjectId())
