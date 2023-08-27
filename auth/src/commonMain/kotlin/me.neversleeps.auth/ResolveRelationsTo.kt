package me.neversleeps.auth

import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.common.permissions.AppPrincipalModel
import me.neversleeps.common.permissions.AppPrincipalRelations

fun Project.resolveRelationsTo(principal: AppPrincipalModel): Set<AppPrincipalRelations> = setOfNotNull(
    AppPrincipalRelations.NONE,
    AppPrincipalRelations.NEW.takeIf { id == ProjectId.NONE },
    AppPrincipalRelations.OWN.takeIf { principal.id == createdBy },
)
