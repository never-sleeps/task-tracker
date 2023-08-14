package me.neversleeps.business.project.validation.tests

import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.business.project.validation.validationDescriptionCorrect
import me.neversleeps.business.project.validation.validationDescriptionEmpty
import me.neversleeps.business.project.validation.validationDescriptionSymbols
import me.neversleeps.business.project.validation.validationDescriptionTrim
import me.neversleeps.business.project.validation.validationIdCorrect
import me.neversleeps.business.project.validation.validationIdEmpty
import me.neversleeps.business.project.validation.validationIdFormat
import me.neversleeps.business.project.validation.validationIdTrim
import me.neversleeps.business.project.validation.validationTitleCorrect
import me.neversleeps.business.project.validation.validationTitleEmpty
import me.neversleeps.business.project.validation.validationTitleSymbols
import me.neversleeps.business.project.validation.validationTitleTrim
import me.neversleeps.common.CorSettings
import me.neversleeps.common.models.AppCommand
import me.neversleeps.`in`.memory.project.ProjectRepositoryInMemory
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProjectUpdateValidationTest {

    private val command = AppCommand.UPDATE
    private lateinit var processor: ProjectProcessor
    val projectInit = ProjectStub.get()

    @BeforeTest
    fun beforeEach() {
        val repoTest = ProjectRepositoryInMemory(initObjects = listOf(projectInit))
        processor = ProjectProcessor(CorSettings(repositoryTest = repoTest))
    }

    @Test fun correctTitle() = validationTitleCorrect(command, processor)

    @Test fun trimTitle() = validationTitleTrim(command, processor)

    @Test fun emptyTitle() = validationTitleEmpty(command, processor)

    @Test fun badSymbolsTitle() = validationTitleSymbols(command, processor)

    @Test fun correctDescription() = validationDescriptionCorrect(command, processor)

    @Test fun trimDescription() = validationDescriptionTrim(command, processor)

    @Test fun emptyDescription() = validationDescriptionEmpty(command, processor)

    @Test fun badSymbolsDescription() = validationDescriptionSymbols(command, processor)

    @Test fun correctId() = validationIdCorrect(command, processor)

    @Test fun trimId() = validationIdTrim(command, processor)

    @Test fun emptyId() = validationIdEmpty(command, processor)

    @Test fun badFormatId() = validationIdFormat(command, processor)
}
