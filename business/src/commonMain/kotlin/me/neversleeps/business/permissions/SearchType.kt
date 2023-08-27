package me.neversleeps.business.permissions

import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppState
import me.neversleeps.common.models.project.AppSearchPermissions
import me.neversleeps.common.permissions.AppUserPermissions
import me.neversleeps.lib.cor.ICorChainDsl
import me.neversleeps.lib.cor.chain
import me.neversleeps.lib.cor.worker

fun ICorChainDsl<ProjectContext>.searchTypes(title: String) = chain {
    this.title = title
    description = "Добавление ограничений в поисковый запрос согласно правам доступа и пр. политикам"
    on { state == AppState.RUNNING }
    worker("Определение типа поиска") {
        projectSearchFilterValidated.searchPermissions = setOfNotNull(
            AppSearchPermissions.OWN.takeIf { permissionsChain.contains(AppUserPermissions.SEARCH_OWN) },
            AppSearchPermissions.PUBLIC.takeIf { permissionsChain.contains(AppUserPermissions.SEARCH_PUBLIC) },
            AppSearchPermissions.REGISTERED.takeIf { permissionsChain.contains(AppUserPermissions.SEARCH_REGISTERED) },
        ).toMutableSet()
    }
}
