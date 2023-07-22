package me.neversleeps.business.workers

import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppState
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.common.models.user.UserId
import me.neversleeps.common.stubs.ProjectDebugStub
import me.neversleeps.lib.cor.ICorChainDsl
import me.neversleeps.lib.cor.worker

fun ICorChainDsl<ProjectContext>.projectStubCreateSuccess(title: String) = worker {
    this.title = title
    on { stubCase == ProjectDebugStub.SUCCESS && state == AppState.RUNNING }
    handle {
        state = AppState.FINISHING
        val stub = ProjectStub.prepareResult {
            projectRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
            projectRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
            projectRequest.createdBy.takeIf { it != UserId.NONE }?.also { this.createdBy = it }
            projectRequest.permissions.takeIf { it.isEmpty() }?.also { this.permissions = it }
        }
        projectResponse = stub
    }
}

fun ICorChainDsl<ProjectContext>.projectStubReadSuccess(title: String) = worker {
    this.title = title
    on { stubCase == ProjectDebugStub.SUCCESS && state == AppState.RUNNING }
    handle {
        state = AppState.FINISHING
        val stub = ProjectStub.prepareResult {
            projectRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
        }
        projectResponse = stub
    }
}

fun ICorChainDsl<ProjectContext>.projectStubUpdateSuccess(title: String) = worker {
    this.title = title
    on { stubCase == ProjectDebugStub.SUCCESS && state == AppState.RUNNING }
    handle {
        state = AppState.FINISHING
        val stub = ProjectStub.prepareResult {
            projectRequest.id.takeIf { it != ProjectId.NONE }?.also { this.id = it }
            projectRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
            projectRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
            projectRequest.createdBy.takeIf { it != UserId.NONE }?.also { this.createdBy = it }
            projectRequest.permissions.takeIf { it.isEmpty() }?.also { this.permissions = it }
        }
        projectResponse = stub
    }
}

fun ICorChainDsl<ProjectContext>.projectStubDeleteSuccess(title: String) = worker {
    this.title = title
    on { stubCase == ProjectDebugStub.SUCCESS && state == AppState.RUNNING }
    handle {
        state = AppState.FINISHING
        val stub = ProjectStub.prepareResult {
            projectRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
        }
        projectResponse = stub
    }
}

fun ICorChainDsl<ProjectContext>.projectStubSearchSuccess(title: String) = worker {
    this.title = title
    on { stubCase == ProjectDebugStub.SUCCESS && state == AppState.RUNNING }
    handle {
        state = AppState.FINISHING
        projectResponse = ProjectStub.prepareResult {
            projectRequest.id.takeIf { it != ProjectId.NONE }?.also { this.id = it }
        }
        projectsResponse.addAll(
            ProjectStub.prepareSearchList(
                searchText = projectSearchFilterRequest.searchText,
                createdBy = projectSearchFilterRequest.createdBy,
            ),
        )
    }
}
