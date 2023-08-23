package me.neversleeps.business.project.validation.tests

import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.business.project.validation.validationIdCorrect
import me.neversleeps.business.project.validation.validationIdEmpty
import me.neversleeps.business.project.validation.validationIdFormat
import me.neversleeps.business.project.validation.validationIdTrim
import me.neversleeps.common.CorSettings
import me.neversleeps.common.models.AppCommand
import me.neversleeps.repository.stubs.ProjectRepositoryStub
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProjectDeleteValidationTest {

    private val command = AppCommand.DELETE
    private lateinit var processor: ProjectProcessor

    @BeforeTest
    fun beforeEach() {
        processor = ProjectProcessor(CorSettings(repositoryStub = ProjectRepositoryStub()))
    }

    @Test fun correctId() = validationIdCorrect(command, processor)

    @Test fun trimId() = validationIdTrim(command, processor)

    @Test fun emptyId() = validationIdEmpty(command, processor)

    @Test fun badFormatId() = validationIdFormat(command, processor)
}
