package me.neversleeps.business

import kotlinx.datetime.Clock
import me.neversleeps.business.groups.projectOperation
import me.neversleeps.business.groups.projectStubs
import me.neversleeps.business.statemachine.computeState
import me.neversleeps.business.validation.project.finishAdFilterValidation
import me.neversleeps.business.validation.project.finishAdValidation
import me.neversleeps.business.validation.project.projectValidation
import me.neversleeps.business.validation.project.validateDescriptionHasContent
import me.neversleeps.business.validation.project.validateDescriptionNotEmpty
import me.neversleeps.business.validation.project.validateIdNotEmpty
import me.neversleeps.business.validation.project.validateIdProperFormat
import me.neversleeps.business.validation.project.validateTitleHasContent
import me.neversleeps.business.validation.project.validateTitleNotEmpty
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
import me.neversleeps.common.CorSettings
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.helpers.asAppError
import me.neversleeps.common.helpers.fail
import me.neversleeps.common.models.AppCommand
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.lib.cor.rootChain
import me.neversleeps.lib.cor.worker
import me.neversleeps.logging.common.ILogWrapper
import me.neversleeps.mappers.log1.toLog

class ProjectProcessor(val settings: CorSettings) {
    suspend fun execute(ctx: ProjectContext) = BusinessChain.exec(
        ctx.apply { this.settings = this@ProjectProcessor.settings },
    )

    suspend fun <T> process(
        logger: ILogWrapper,
        logId: String,
        command: AppCommand,
        fromTransport: suspend (ProjectContext) -> Unit,
        sendResponse: suspend (ProjectContext) -> T,
        toLog: ProjectContext.(logId: String) -> Any,
    ): T {
        val ctx = ProjectContext(timeStart = Clock.System.now())
        var realCommand = command

        return try {
            logger.doWithLogging(id = logId) {
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
        } catch (e: Throwable) {
            logger.doWithLogging(id = "$logId-failure") {
                logger.error(msg = "$realCommand handling failed")

                ctx.command = realCommand
                ctx.fail(e.asAppError())
                execute(ctx)
                sendResponse(ctx)
            }
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
                projectValidation {
                    worker("Копируем поля в projectValidating") { projectValidating = projectRequest.deepCopy() }
                    worker("Очистка id") { projectValidating.id = ProjectId.NONE }
                    worker("Очистка заголовка") { projectValidating.title = projectValidating.title.trim() }
                    worker("Очистка описания") { projectValidating.description = projectValidating.description.trim() }
                    validateTitleNotEmpty("Проверка, что заголовок не пуст")
                    validateTitleHasContent("Проверка символов")
                    validateDescriptionNotEmpty("Проверка, что описание не пусто")
                    validateDescriptionHasContent("Проверка символов")

                    finishAdValidation("Завершение проверок")
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

                projectValidation {
                    worker("Копируем поля в projectValidating") { projectValidating = projectRequest.deepCopy() }
                    worker("Очистка id") { projectValidating.id = ProjectId(projectValidating.id.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")

                    finishAdValidation("Успешное завершение процедуры валидации")
                }
                computeState("Вычисление состояния проекта")
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
                projectValidation {
                    worker("Копируем поля в projectValidating") { projectValidating = projectRequest.deepCopy() }
                    worker("Очистка id") { projectValidating.id = ProjectId(projectValidating.id.asString().trim()) }
                    worker("Очистка заголовка") { projectValidating.title = projectValidating.title.trim() }
                    worker("Очистка описания") { projectValidating.description = projectValidating.description.trim() }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    validateTitleNotEmpty("Проверка на непустой заголовок")
                    validateTitleHasContent("Проверка на наличие содержания в заголовке")
                    validateDescriptionNotEmpty("Проверка на непустое описание")
                    validateDescriptionHasContent("Проверка на наличие содержания в описании")

                    finishAdValidation("Успешное завершение процедуры валидации")
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
                projectValidation {
                    worker("Копируем поля в projectValidating") { projectValidating = projectRequest.deepCopy() }
                    worker("Очистка id") { projectValidating.id = ProjectId(projectValidating.id.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    finishAdValidation("Успешное завершение процедуры валидации")
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
                projectValidation {
                    worker("Копируем поля в projectSearchFilterValidating") {
                        projectSearchFilterValidating = projectSearchFilterRequest.copy()
                    }
                    finishAdFilterValidation("Успешное завершение процедуры валидации")
                }
            }
        }.build()
    }
}
