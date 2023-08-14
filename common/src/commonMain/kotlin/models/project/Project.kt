package me.neversleeps.common.models.project

import kotlinx.datetime.Instant
import me.neversleeps.common.NONE
import me.neversleeps.common.models.AppLock
import me.neversleeps.common.models.user.UserId
import me.neversleeps.common.statemachine.ObjectState

data class Project(
    var id: ProjectId = ProjectId.NONE,

    var title: String = "",
    var description: String = "",
    var createdBy: UserId = UserId.NONE,
    var permissions: MutableSet<ProjectPermission> = mutableSetOf(),

    var objectState: ObjectState = ObjectState.NONE,
    var timePublished: Instant = Instant.NONE,
    var timeUpdated: Instant = Instant.NONE,
    var lock: AppLock = AppLock.NONE,
) {
    fun deepCopy(): Project = copy(
        permissions = permissions.toMutableSet(),
    )

    fun isEmpty() = this == NONE

    companion object {
        val NONE = Project()
    }
}
