package me.neversleeps.business.validation.project

import me.neversleeps.common.ProjectContext
import me.neversleeps.common.helpers.errorValidation
import me.neversleeps.common.helpers.fail
import me.neversleeps.lib.cor.ICorChainDsl
import me.neversleeps.lib.cor.worker

fun ICorChainDsl<ProjectContext>.validateTitleNotEmpty(title: String) = worker {
    this.title = title
    on { projectValidating.title.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "title",
                violationCode = "empty",
                description = "field must not be empty",
            ),
        )
    }
}

fun ICorChainDsl<ProjectContext>.validateTitleHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("\\p{L}")
    on { projectValidating.title.isNotEmpty() && !projectValidating.title.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "title",
                violationCode = "noContent",
                description = "field must contain leters",
            ),
        )
    }
}
