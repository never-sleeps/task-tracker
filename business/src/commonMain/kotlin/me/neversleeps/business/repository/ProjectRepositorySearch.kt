package me.neversleeps.business.repository

import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppState
import me.neversleeps.common.repository.project.DbProjectFilterRequest
import me.neversleeps.lib.cor.ICorChainDsl
import me.neversleeps.lib.cor.worker

fun ICorChainDsl<ProjectContext>.repositorySearch(title: String) = worker {
    this.title = title
    description = "Поиск проектов в БД по фильтру"
    on { state == AppState.RUNNING }
    handle {
        val request = DbProjectFilterRequest(
            searchText = projectSearchFilterValidated.searchText,
            createdBy = projectSearchFilterValidated.createdBy,
        )
        val result = projectRepository.searchProjects(request)
        val resultAds = result.data
        if (result.isSuccess && resultAds != null) {
            projectsRepositoryDone = resultAds.toMutableList()
        } else {
            state = AppState.FAILING
            errors.addAll(result.errors)
        }
    }
}
