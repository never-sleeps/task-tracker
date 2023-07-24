package me.neversleeps.business.validation.project

import me.neversleeps.common.ProjectContext
import me.neversleeps.common.helpers.errorValidation
import me.neversleeps.common.helpers.fail
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.lib.cor.ICorChainDsl
import me.neversleeps.lib.cor.worker

fun ICorChainDsl<ProjectContext>.validateIdNotEmpty(title: String) = worker {
    this.title = title
    on { projectValidating.id.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "id",
                violationCode = "empty",
                description = "field must not be empty",
            ),
        )
    }
}

fun ICorChainDsl<ProjectContext>.validateIdProperFormat(title: String) = worker {
    this.title = title

    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { projectValidating.id != ProjectId.NONE && !projectValidating.id.asString().matches(regExp) }
    handle {
        val encodedId = projectValidating.id.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
                field = "id",
                violationCode = "badFormat",
                description = "value $encodedId must contain only letters and numbers",
            ),
        )
    }
}
