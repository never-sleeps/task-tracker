package me.neversleeps.business

import me.neversleeps.business.groups.taskOperation
import me.neversleeps.business.groups.taskStubs
import me.neversleeps.business.workers.taskInitStatus
import me.neversleeps.business.workers.taskStubCreateSuccess
import me.neversleeps.business.workers.taskStubDbError
import me.neversleeps.business.workers.taskStubDeleteSuccess
import me.neversleeps.business.workers.taskStubNotFound
import me.neversleeps.business.workers.taskStubPermissionError
import me.neversleeps.business.workers.taskStubReadSuccess
import me.neversleeps.business.workers.taskStubSearchSuccess
import me.neversleeps.business.workers.taskStubUpdateSuccess
import me.neversleeps.business.workers.taskStubValidationBadId
import me.neversleeps.business.workers.taskStubValidationBadSearchCreatedBy
import me.neversleeps.business.workers.taskStubValidationBadSearchExecutor
import me.neversleeps.business.workers.taskStubValidationBadTitle
import me.neversleeps.common.TaskContext
import me.neversleeps.common.models.AppCommand
import me.neversleeps.lib.cor.rootChain

class TaskProcessor {
    suspend fun execute(ctx: TaskContext) = BusinessChain.exec(ctx)

    companion object {
        private val BusinessChain = rootChain<TaskContext> {
            taskInitStatus("Инициализация статуса")
            taskOperation("Создание задачи", AppCommand.CREATE) { // т.е. будет выполняться для команды AppCommand.CREATE
                taskStubs("Обработка стабов") {
                    taskStubCreateSuccess("Имитация успешной обработки")
                    taskStubValidationBadId("Имитация ошибки валидации id")
                    taskStubValidationBadTitle("Имитация ошибки валидации заголовка")
                    taskStubDbError("Имитация ошибки работы с БД")
                    taskStubPermissionError("Имитация отсутствия доступа")
                }
            }
            taskOperation("Получить задачу", AppCommand.READ) {
                taskStubs("Обработка стабов") {
                    taskStubReadSuccess("Имитация успешной обработки")
                    taskStubValidationBadId("Имитация ошибки валидации id")
                    taskStubNotFound("Имитация ошибки отсутствия запрошенного объекта")
                    taskStubDbError("Имитация ошибки работы с БД")
                    taskStubPermissionError("Имитация отсутствия доступа")
                }
            }
            taskOperation("Обновить задачу", AppCommand.UPDATE) {
                taskStubs("Обработка стабов") {
                    taskStubUpdateSuccess("Имитация успешной обработки")
                    taskStubValidationBadId("Имитация ошибки валидации id")
                    taskStubValidationBadTitle("Имитация ошибки валидации заголовка")
                    taskStubNotFound("Имитация ошибки отсутствия запрошенного объекта")
                    taskStubDbError("Имитация ошибки работы с БД")
                    taskStubPermissionError("Имитация отсутствия доступа")
                }
            }
            taskOperation("Удалить задачу", AppCommand.DELETE) {
                taskStubs("Обработка стабов") {
                    taskStubDeleteSuccess("Имитация успешной обработки")
                    taskStubValidationBadId("Имитация ошибки валидации id")
                    taskStubNotFound("Имитация ошибки отсутствия запрошенного объекта")
                    taskStubDbError("Имитация ошибки работы с БД")
                    taskStubPermissionError("Имитация отсутствия доступа")
                }
            }
            taskOperation("Поиск задач", AppCommand.SEARCH) {
                taskStubs("Обработка стабов") {
                    taskStubSearchSuccess("Имитация успешной обработки")
                    taskStubValidationBadId("Имитация ошибки валидации id")
                    taskStubNotFound("Имитация ошибки отсутствия запрошенного объекта")
                    taskStubDbError("Имитация ошибки работы с БД")
                    taskStubPermissionError("Имитация отсутствия доступа")
                    taskStubValidationBadSearchCreatedBy("Имитация ошибки валидации поля searchCreatedBy")
                    taskStubValidationBadSearchExecutor("Имитация ошибки валидации поля searchExecutor")
                }
            }
        }.build()
    }
}
