package me.neversleeps.common.repository.project

import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectId

data class DbProjectIdRequest(
    val id: ProjectId,
) {
    constructor(ad: Project): this(ad.id)
}
