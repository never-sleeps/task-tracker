package me.neversleeps.business.workers

import me.neversleeps.common.TaskContext
import me.neversleeps.common.models.AppError
import me.neversleeps.common.models.AppState
import me.neversleeps.common.stubs.TaskDebugStub
import me.neversleeps.lib.cor.ICorChainDsl
import me.neversleeps.lib.cor.worker

fun ICorChainDsl<TaskContext>.taskStubValidationBadId(title: String) = worker {
    this.title = title
    on { stubCase == TaskDebugStub.BAD_ID && state == AppState.RUNNING }
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

fun ICorChainDsl<TaskContext>.taskStubValidationBadTitle(title: String) = worker {
    this.title = title
    on { stubCase == TaskDebugStub.BAD_TITLE && state == AppState.RUNNING }
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

fun ICorChainDsl<TaskContext>.taskStubValidationBadSearchCreatedBy(title: String) = worker {
    this.title = title
    on { stubCase == TaskDebugStub.BAD_SEARCH_CREATED_BY && state == AppState.RUNNING }
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

fun ICorChainDsl<TaskContext>.taskStubValidationBadSearchExecutor(title: String) = worker {
    this.title = title
    on { stubCase == TaskDebugStub.BAD_SEARCH_EXECUTOR && state == AppState.RUNNING }
    handle {
        state = AppState.FAILING
        this.errors.add(
            AppError(
                group = "validation",
                code = "validation-search-executor",
                field = "searchExecutor",
                message = "Wrong searchExecutor field",
            ),
        )
    }
}

fun ICorChainDsl<TaskContext>.taskStubNotFound(title: String) = worker {
    this.title = title
    on { stubCase == TaskDebugStub.NOT_FOUND && state == AppState.RUNNING }
    handle {
        state = AppState.FAILING
        this.errors.add(
            AppError(
                group = "internal",
                code = "internal-not-found",
                message = "object not found",
            ),
        )
    }
}

fun ICorChainDsl<TaskContext>.taskStubPermissionError(title: String) = worker {
    this.title = title
    on { stubCase == TaskDebugStub.PERMISSION_ERROR && state == AppState.RUNNING }
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

fun ICorChainDsl<TaskContext>.taskStubDbError(title: String) = worker {
    this.title = title
    on { stubCase == TaskDebugStub.DB_ERROR && state == AppState.RUNNING }
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
