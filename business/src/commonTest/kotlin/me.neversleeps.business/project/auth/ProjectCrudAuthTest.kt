package me.neversleeps.business.project.auth

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.common.CorSettings
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppCommand
import me.neversleeps.common.models.AppState
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.common.models.project.ProjectPermissionClient
import me.neversleeps.common.models.user.UserId
import me.neversleeps.common.permissions.AppPrincipalModel
import me.neversleeps.common.permissions.AppUserGroups
import me.neversleeps.common.stubs.WorkMode
import me.neversleeps.`in`.memory.project.ProjectRepositoryInMemory
import kotlin.test.* // ktlint-disable no-wildcard-imports

/**
 * @crud - экземпляр класса-фасада бизнес-логики
 * @context - контекст, смапленный из транспортной модели запроса
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ProjectCrudAuthTest {
    @Test
    fun createSuccessTest() = runTest {
        val userId = UserId("123")
        val repository = ProjectRepositoryInMemory()
        val processor = ProjectProcessor(
            settings = CorSettings(
                repositoryTest = repository,
            ),
        )
        val context = ProjectContext(
            workMode = WorkMode.TEST,
            projectRequest = ProjectStub.prepareResult {
                permissionsClient.clear()
                id = ProjectId.NONE
            },
            command = AppCommand.CREATE,
            principal = AppPrincipalModel(
                id = userId,
                groups = setOf(
                    AppUserGroups.USER,
                    AppUserGroups.TEST,
                ),
            ),
        )

        processor.execute(context)
        assertEquals(AppState.FINISHING, context.state)
        with(context.projectResponse) {
            assertTrue { id.asString().isNotBlank() }
            assertContains(permissionsClient, ProjectPermissionClient.READ)
            assertContains(permissionsClient, ProjectPermissionClient.UPDATE)
            assertContains(permissionsClient, ProjectPermissionClient.DELETE)
//            assertFalse { permissionsClient.contains(PermissionModel.CONTACT) }
        }
    }

    @Test
    fun readSuccessTest() = runTest {
        val projectObj = ProjectStub.get()
        val userId = projectObj.createdBy
        val adId = projectObj.id
        val repo = ProjectRepositoryInMemory(initObjects = listOf(projectObj))
        val processor = ProjectProcessor(
            settings = CorSettings(
                repositoryTest = repo,
            ),
        )
        val context = ProjectContext(
            command = AppCommand.READ,
            workMode = WorkMode.TEST,
            projectRequest = Project(id = adId),
            principal = AppPrincipalModel(
                id = userId,
                groups = setOf(
                    AppUserGroups.USER,
                    AppUserGroups.TEST,
                ),
            ),
        )

        processor.execute(context)
        assertEquals(AppState.FINISHING, context.state)
        with(context.projectResponse) {
            assertTrue { id.asString().isNotBlank() }
            assertContains(permissionsClient, ProjectPermissionClient.READ)
            assertContains(permissionsClient, ProjectPermissionClient.UPDATE)
            assertContains(permissionsClient, ProjectPermissionClient.DELETE)
//            assertFalse { context.responseAd.permissions.contains(PermissionModel.CONTACT) }
        }
    }
}
