package me.neversleeps.business.permissions

import me.neversleeps.auth.resolveFrontPermissions
import me.neversleeps.auth.resolveRelationsTo
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppState
import me.neversleeps.lib.cor.ICorChainDsl
import me.neversleeps.lib.cor.worker

fun ICorChainDsl<ProjectContext>.frontPermissions(title: String) = worker {
    this.title = title
    description = "Вычисление разрешений пользователей для фронтенда"

    on { state == AppState.RUNNING }

    handle {
        projectRepositoryDone.permissionsClient.addAll(
            resolveFrontPermissions(
                permissionsChain,
                // Повторно вычисляем отношения, поскольку они могли измениться при выполении операции
                projectRepositoryDone.resolveRelationsTo(principal),
            ),
        )

        for (project in projectsRepositoryDone) {
            project.permissionsClient.addAll(
                resolveFrontPermissions(
                    permissionsChain,
                    project.resolveRelationsTo(principal),
                ),
            )
        }
    }
}
