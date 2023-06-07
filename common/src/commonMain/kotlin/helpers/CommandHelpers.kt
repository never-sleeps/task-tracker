package helpers

import ProjectContext
import models.AppCommand

fun ProjectContext.isUpdatableCommand() =
    this.command in listOf(AppCommand.CREATE, AppCommand.UPDATE, AppCommand.DELETE)
