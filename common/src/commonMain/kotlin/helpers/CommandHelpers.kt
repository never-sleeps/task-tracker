package me.neversleeps.common.helpers

import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppCommand

fun ProjectContext.isUpdatableCommand() =
    this.command in listOf(AppCommand.CREATE, AppCommand.UPDATE, AppCommand.DELETE)
