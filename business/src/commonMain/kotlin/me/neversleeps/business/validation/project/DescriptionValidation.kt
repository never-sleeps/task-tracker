package me.neversleeps.business.validation.project

import me.neversleeps.common.ProjectContext
import me.neversleeps.common.helpers.errorValidation
import me.neversleeps.common.helpers.fail
import me.neversleeps.lib.cor.ICorChainDsl
import me.neversleeps.lib.cor.worker

fun ICorChainDsl<ProjectContext>.validateDescriptionNotEmpty(title: String) = worker {
    this.title = title
    on { projectValidating.description.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "description",
                violationCode = "empty",
                description = "field must not be empty",
            ),
        )
    }
}

fun ICorChainDsl<ProjectContext>.validateDescriptionHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("\\p{L}")
    on { projectValidating.description.isNotEmpty() && !projectValidating.description.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "description",
                violationCode = "noContent",
                description = "field must contain letters",
            ),
        )
    }
}
