import models.AppCommand
import kotlin.reflect.KClass

class UnknownRequestClass(clazz: KClass<*>) :
    RuntimeException("Class $clazz cannot be mapped to Application Context")

class UnknownCommandMapping(command: AppCommand) :
    Throwable("Unknown command $command at mapping toTransport stage")
