package me.neversleeps.business

import kotlinx.datetime.Clock
import me.neversleeps.business.groups.projectOperation
import me.neversleeps.business.groups.projectStubs
import me.neversleeps.business.workers.projectInitStatus
import me.neversleeps.business.workers.projectStubCreateSuccess
import me.neversleeps.business.workers.projectStubDbError
import me.neversleeps.business.workers.projectStubDeleteSuccess
import me.neversleeps.business.workers.projectStubNotFound
import me.neversleeps.business.workers.projectStubPermissionError
import me.neversleeps.business.workers.projectStubReadSuccess
import me.neversleeps.business.workers.projectStubSearchSuccess
import me.neversleeps.business.workers.projectStubUpdateSuccess
import me.neversleeps.business.workers.projectStubValidationBadId
import me.neversleeps.business.workers.projectStubValidationBadSearchCreatedBy
import me.neversleeps.business.workers.projectStubValidationBadSearchText
import me.neversleeps.business.workers.projectStubValidationBadTitle
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppCommand
import me.neversleeps.lib.cor.rootChain
import me.neversleeps.logging.common.ILogWrapper
import me.neversleeps.mappers.log1.toLog

class ProjectProcessor {
    suspend fun execute(ctx: ProjectContext) = BusinessChain.exec(ctx)

    suspend fun <T> process(
        logger: ILogWrapper,
        logId: String,
        command: AppCommand,
        fromTransport: suspend (ProjectContext) -> Unit,
        sendResponse: suspend (ProjectContext) -> T,
    ): T {
        val ctx = ProjectContext(timeStart = Clock.System.now())
        var realCommand = command

        return logger.doWithLogging(id = logId) {
            fromTransport(ctx)
            realCommand = ctx.command

            logger.info(
                msg = "$realCommand request is got",
                data = ctx.toLog("$logId-got"),
            )

            execute(ctx)

            logger.info(
                msg = "$realCommand request is handled",
                data = ctx.toLog("$logId-handled"),
            )
            sendResponse(ctx)
        }
    }

    companion object {
        private val BusinessChain = rootChain<ProjectContext> {
            projectInitStatus("Инициализация статуса")
            projectOperation("Создание проекта", AppCommand.CREATE) { // т.е. будет выполняться для команды AppCommand.CREATE
                projectStubs("Обработка стабов") {
                    projectStubCreateSuccess("Имитация успешной обработки")
                    projectStubValidationBadId("Имитация ошибки валидации id")
                    projectStubValidationBadTitle("Имитация ошибки валидации заголовка")
                    projectStubDbError("Имитация ошибки работы с БД")
                    projectStubPermissionError("Имитация отсутствия доступа")
                }
            }
            projectOperation("Получить проект", AppCommand.READ) {
                projectStubs("Обработка стабов") {
                    projectStubReadSuccess("Имитация успешной обработки")
                    projectStubValidationBadId("Имитация ошибки валидации id")
                    projectStubNotFound("Имитация ошибки отсутствия запрошенного объекта")
                    projectStubDbError("Имитация ошибки работы с БД")
                    projectStubPermissionError("Имитация отсутствия доступа")
                }
            }
            projectOperation("Обновить проект", AppCommand.UPDATE) {
                projectStubs("Обработка стабов") {
                    projectStubUpdateSuccess("Имитация успешной обработки")
                    projectStubValidationBadId("Имитация ошибки валидации id")
                    projectStubValidationBadTitle("Имитация ошибки валидации заголовка")
                    projectStubNotFound("Имитация ошибки отсутствия запрошенного объекта")
                    projectStubDbError("Имитация ошибки работы с БД")
                    projectStubPermissionError("Имитация отсутствия доступа")
                }
            }
            projectOperation("Удалить проект", AppCommand.DELETE) {
                projectStubs("Обработка стабов") {
                    projectStubDeleteSuccess("Имитация успешной обработки")
                    projectStubValidationBadId("Имитация ошибки валидации id")
                    projectStubNotFound("Имитация ошибки отсутствия запрошенного объекта")
                    projectStubDbError("Имитация ошибки работы с БД")
                    projectStubPermissionError("Имитация отсутствия доступа")
                }
            }
            projectOperation("Поиск проектов", AppCommand.SEARCH) {
                projectStubs("Обработка стабов") {
                    projectStubSearchSuccess("Имитация успешной обработки")
                    projectStubValidationBadId("Имитация ошибки валидации id")
                    projectStubNotFound("Имитация ошибки отсутствия запрошенного объекта")
                    projectStubDbError("Имитация ошибки работы с БД")
                    projectStubPermissionError("Имитация отсутствия доступа")
                    projectStubValidationBadSearchText("Имитация ошибки валидации поля searchText")
                    projectStubValidationBadSearchCreatedBy("Имитация ошибки валидации поля searchCreatedBy")
                }
            }
        }.build()
    }
}
