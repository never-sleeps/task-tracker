package me.neversleeps.business.permissions

import me.neversleeps.auth.checkPermitted
import me.neversleeps.auth.resolveRelationsTo
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.helpers.fail
import me.neversleeps.common.models.AppError
import me.neversleeps.common.models.AppState
import me.neversleeps.lib.cor.ICorChainDsl
import me.neversleeps.lib.cor.chain
import me.neversleeps.lib.cor.worker

fun ICorChainDsl<ProjectContext>.accessValidation(title: String) = chain {
    this.title = title
    description = "Вычисление прав доступа по группе принципала и таблице прав доступа"
    on { state == AppState.RUNNING }
    worker("Вычисление отношения проекта к принципалу") {
        projectRepositoryRead.principalRelations = projectRepositoryRead.resolveRelationsTo(principal)
    }
    worker("Вычисление доступа к проекту") {
        permitted = checkPermitted(command, projectRepositoryRead.principalRelations, permissionsChain)
    }
    worker {
        this.title = "Валидация прав доступа"
        description = "Проверка наличия прав для выполнения операции"
        on { !permitted }
        handle {
            fail(AppError(message = "User is not allowed to perform this operation"))
        }
    }
}

