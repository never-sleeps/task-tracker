package exception // ktlint-disable filename

import me.neversleeps.common.models.AppCommand

class UnknownCommand(command: AppCommand) : Throwable("Unknown command $command")
