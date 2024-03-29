package me.neversleeps.business

import kotlinx.datetime.Clock
import me.neversleeps.business.general.initRepo
import me.neversleeps.business.general.prepareResult
import me.neversleeps.business.groups.projectOperation
import me.neversleeps.business.groups.projectStubs
import me.neversleeps.business.permissions.accessValidation
import me.neversleeps.business.permissions.chainPermissions
import me.neversleeps.business.permissions.frontPermissions
import me.neversleeps.business.permissions.searchTypes
import me.neversleeps.business.repository.repositoryCreate
import me.neversleeps.business.repository.repositoryDelete
import me.neversleeps.business.repository.repositoryPrepareCreate
import me.neversleeps.business.repository.repositoryPrepareDelete
import me.neversleeps.business.repository.repositoryPrepareUpdate
import me.neversleeps.business.repository.repositoryRead
import me.neversleeps.business.repository.repositorySearch
import me.neversleeps.business.repository.repositoryUpdate
import me.neversleeps.business.statemachine.computeState
import me.neversleeps.business.validation.project.finishAdFilterValidation
import me.neversleeps.business.validation.project.finishAdValidation
import me.neversleeps.business.validation.project.projectValidation
import me.neversleeps.business.validation.project.validateDescriptionHasContent
import me.neversleeps.business.validation.project.validateDescriptionNotEmpty
import me.neversleeps.business.validation.project.validateIdNotEmpty
import me.neversleeps.business.validation.project.validateIdProperFormat
import me.neversleeps.business.validation.project.validateLockNotEmpty
import me.neversleeps.business.validation.project.validateLockProperFormat
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
import me.neversleeps.common.models.AppLock
import me.neversleeps.common.models.AppState
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.lib.cor.chain
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
            initRepo("Инициализация репозитория")

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
                chainPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика сохранения"
                    repositoryPrepareCreate("Подготовка объекта для сохранения")
                    accessValidation("Вычисление прав доступа")
                    repositoryCreate("Создание объявления в БД")
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
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
                chainPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика чтения"
                    repositoryRead("Чтение проекта из БД")
                    accessValidation("Вычисление прав доступа")
                    worker {
                        title = "Подготовка ответа для Read"
                        on { state == AppState.RUNNING }
                        handle { projectRepositoryDone = projectRepositoryRead }
                    }
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
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
                    worker("Очистка lock") { projectValidating.lock = AppLock(projectValidating.lock.asString().trim()) }
                    worker("Очистка заголовка") { projectValidating.title = projectValidating.title.trim() }
                    worker("Очистка описания") { projectValidating.description = projectValidating.description.trim() }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    validateLockNotEmpty("Проверка на непустой lock")
                    validateLockProperFormat("Проверка формата lock")
                    validateTitleNotEmpty("Проверка на непустой заголовок")
                    validateTitleHasContent("Проверка на наличие содержания в заголовке")
                    validateDescriptionNotEmpty("Проверка на непустое описание")
                    validateDescriptionHasContent("Проверка на наличие содержания в описании")

                    finishAdValidation("Успешное завершение процедуры валидации")
                }
                chainPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика сохранения"
                    repositoryRead("Чтение проекта из БД")
                    accessValidation("Вычисление прав доступа")
                    repositoryPrepareUpdate("Подготовка объекта для обновления")
                    repositoryUpdate("Обновление объявления в БД")
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
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
                    worker("Очистка lock") { projectValidating.lock = AppLock(projectValidating.lock.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    validateLockNotEmpty("Проверка на непустой lock")
                    validateLockProperFormat("Проверка формата lock")
                    finishAdValidation("Успешное завершение процедуры валидации")
                }
                chainPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика удаления"
                    repositoryRead("Чтение проекта из БД")
                    accessValidation("Вычисление прав доступа")
                    repositoryPrepareDelete("Подготовка объекта для удаления")
                    repositoryDelete("Удаление проекта из БД")
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
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
                chainPermissions("Вычисление разрешений для пользователя")
                searchTypes("Подготовка поискового запроса")
                repositorySearch("Поиск проектов в БД по фильтру")
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
            }
        }.build()
    }
}
