package me.neversleeps.business.workers

import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppError
import me.neversleeps.common.models.AppState
import me.neversleeps.common.stubs.ProjectDebugStub
import me.neversleeps.lib.cor.ICorChainDsl
import me.neversleeps.lib.cor.worker

fun ICorChainDsl<ProjectContext>.projectStubValidationBadId(title: String) = worker {
    this.title = title
    on { stubCase == ProjectDebugStub.BAD_ID && state == AppState.RUNNING }
    handle {
        state = AppState.FAILING
        this.errors.add(
            AppError(
                group = "validation",
                code = "validation-id",
                field = "id",
                message = "Wrong id field",
            ),
        )
    }
}

fun ICorChainDsl<ProjectContext>.projectStubValidationBadTitle(title: String) = worker {
    this.title = title
    on { stubCase == ProjectDebugStub.BAD_TITLE && state == AppState.RUNNING }
    handle {
        state = AppState.FAILING
        this.errors.add(
            AppError(
                group = "validation",
                code = "validation-title",
                field = "title",
                message = "Wrong title field",
            ),
        )
    }
}

fun ICorChainDsl<ProjectContext>.projectStubValidationBadSearchText(title: String) = worker {
    this.title = title
    on { stubCase == ProjectDebugStub.BAD_SEARCH_TEXT && state == AppState.RUNNING }
    handle {
        state = AppState.FAILING
        this.errors.add(
            AppError(
                group = "validation",
                code = "validation-search-text",
                field = "searchText",
                message = "Wrong searchText field",
            ),
        )
    }
}

fun ICorChainDsl<ProjectContext>.projectStubValidationBadSearchCreatedBy(title: String) = worker {
    this.title = title
    on { stubCase == ProjectDebugStub.BAD_SEARCH_CREATED_BY && state == AppState.RUNNING }
    handle {
        state = AppState.FAILING
        this.errors.add(
            AppError(
                group = "validation",
                code = "validation-search-created-by",
                field = "searchCreatedBy",
                message = "Wrong searchCreatedBy field",
            ),
        )
    }
}

fun ICorChainDsl<ProjectContext>.projectStubNotFound(title: String) = worker {
    this.title = title
    on { stubCase == ProjectDebugStub.NOT_FOUND && state == AppState.RUNNING }
    handle {
        state = AppState.FAILING
        this.errors.add(
            AppError(
                group = "internal",
                code = "internal-not-found",
                message = "Not found object",
            ),
        )
    }
}

fun ICorChainDsl<ProjectContext>.projectStubPermissionError(title: String) = worker {
    this.title = title
    on { stubCase == ProjectDebugStub.PERMISSION_ERROR && state == AppState.RUNNING }
    handle {
        state = AppState.FAILING
        this.errors.add(
            AppError(
                group = "internal",
                code = "permission-error",
                message = "permission-error",
            ),
        )
    }
}

fun ICorChainDsl<ProjectContext>.projectStubDbError(title: String) = worker {
    this.title = title
    on { stubCase == ProjectDebugStub.DB_ERROR && state == AppState.RUNNING }
    handle {
        state = AppState.FAILING
        this.errors.add(
            AppError(
                group = "internal",
                code = "internal-db",
                message = "Internal error",
            ),
        )
    }
}
