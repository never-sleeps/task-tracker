package me.neversleeps.business.project.validation.tests

import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.business.project.validation.validationDescriptionCorrect
import me.neversleeps.business.project.validation.validationDescriptionEmpty
import me.neversleeps.business.project.validation.validationDescriptionSymbols
import me.neversleeps.business.project.validation.validationDescriptionTrim
import me.neversleeps.business.project.validation.validationTitleCorrect
import me.neversleeps.business.project.validation.validationTitleEmpty
import me.neversleeps.business.project.validation.validationTitleSymbols
import me.neversleeps.business.project.validation.validationTitleTrim
import me.neversleeps.common.CorSettings
import me.neversleeps.common.models.AppCommand
import me.neversleeps.repository.stubs.ProjectRepositoryStub
import kotlin.test.Test

// пример теста валидации, собранного из тестовых функций-оберток
@OptIn(ExperimentalCoroutinesApi::class)
class ProjectCreateValidationTest {

    private val command = AppCommand.CREATE
    private val processor by lazy { ProjectProcessor(CorSettings(repositoryStub = ProjectRepositoryStub())) }

    @Test fun correctTitle() = validationTitleCorrect(command, processor)

    @Test fun trimTitle() = validationTitleTrim(command, processor)

    @Test fun emptyTitle() = validationTitleEmpty(command, processor)

    @Test fun badSymbolsTitle() = validationTitleSymbols(command, processor)

    @Test fun correctDescription() = validationDescriptionCorrect(command, processor)

    @Test fun trimDescription() = validationDescriptionTrim(command, processor)

    @Test fun emptyDescription() = validationDescriptionEmpty(command, processor)

    @Test fun badSymbolsDescription() = validationDescriptionSymbols(command, processor)
}
