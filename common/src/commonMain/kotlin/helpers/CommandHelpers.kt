package ru.otus.otuskotlin.marketplace.common.helpers

import ru.otus.otuskotlin.marketplace.common.ProjectContext
import ru.otus.otuskotlin.marketplace.common.models.AppCommand

fun ProjectContext.isUpdatableCommand() =
    this.command in listOf(AppCommand.CREATE, AppCommand.UPDATE, AppCommand.DELETE)
