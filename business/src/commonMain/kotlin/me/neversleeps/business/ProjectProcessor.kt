package me.neversleeps.business

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

class ProjectProcessor {
    suspend fun execute(ctx: ProjectContext) = BusinessChain.exec(ctx)

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
