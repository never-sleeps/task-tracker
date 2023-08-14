package me.neversleeps.business.validation.project

import me.neversleeps.common.ProjectContext
import me.neversleeps.common.helpers.errorValidation
import me.neversleeps.common.helpers.fail
import me.neversleeps.common.models.AppLock
import me.neversleeps.lib.cor.ICorChainDsl
import me.neversleeps.lib.cor.worker

fun ICorChainDsl<ProjectContext>.validateLockNotEmpty(title: String) = worker {
    this.title = title
    on { projectValidating.lock.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "lock",
                violationCode = "empty",
                description = "field must not be empty",
            ),
        )
    }
}

fun ICorChainDsl<ProjectContext>.validateLockProperFormat(title: String) = worker {
    this.title = title

    // Может быть вынесен в ProjectId для реализации различных форматов
    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { projectValidating.lock != AppLock.NONE && !projectValidating.lock.asString().matches(regExp) }
    handle {
        val encodedId = projectValidating.lock.asString()
        fail(
            errorValidation(
                field = "lock",
                violationCode = "badFormat",
                description = "value $encodedId must contain only",
            ),
        )
    }
}
