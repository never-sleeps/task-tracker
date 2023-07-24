package me.neversleeps.appspring.api.v1.controller.webflux

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import me.neversleeps.api.jackson.v1.models.IResponse
import me.neversleeps.api.jackson.v1.models.ProjectCreateRequest
import me.neversleeps.api.jackson.v1.models.ProjectDeleteRequest
import me.neversleeps.api.jackson.v1.models.ProjectReadRequest
import me.neversleeps.api.jackson.v1.models.ProjectSearchRequest
import me.neversleeps.api.jackson.v1.models.ProjectUpdateRequest
import me.neversleeps.appspring.config.CorConfig
import me.neversleeps.appspring.v1.controller.V1ProjectController
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.common.ProjectContext
import me.neversleeps.mappers.jackson.fromInternal.toTransportCreate
import me.neversleeps.mappers.jackson.fromInternal.toTransportDelete
import me.neversleeps.mappers.jackson.fromInternal.toTransportRead
import me.neversleeps.mappers.jackson.fromInternal.toTransportSearch
import me.neversleeps.mappers.jackson.fromInternal.toTransportUpdate
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

// Temporary simple test with stubs for WEBFLUX controllers
@WebFluxTest(V1ProjectController::class, CorConfig::class)
internal class ProjectControllerTest {

    @Autowired
    private lateinit var webClient: WebTestClient

    @MockkBean(relaxUnitFun = true)
    private lateinit var processor: ProjectProcessor

    @Test
    fun createProject() {
        testStub(
            url = "/api/v1/project/create",
            requestObj = ProjectCreateRequest(),
            responseObj = ProjectContext().toTransportCreate(),
        )
    }

    @Test
    fun readProject() {
        testStub(
            url = "/api/v1/project/read",
            requestObj = ProjectReadRequest(),
            responseObj = ProjectContext().toTransportRead(),
        )
    }

    @Test
    fun updateProject() {
        testStub(
            url = "/api/v1/project/update",
            requestObj = ProjectUpdateRequest(),
            responseObj = ProjectContext().toTransportUpdate(),
        )
    }

    @Test
    fun deleteProject() {
        testStub(
            url = "/api/v1/project/delete",
            requestObj = ProjectDeleteRequest(),
            responseObj = ProjectContext().toTransportDelete(),
        )
    }

    @Test
    fun searchProject() {
        testStub(
            url = "/api/v1/project/search",
            requestObj = ProjectSearchRequest(),
            responseObj = ProjectContext().toTransportSearch(),
        )
    }

    private inline fun <reified Req : Any, reified Res : IResponse> testStub(
        url: String,
        requestObj: Req,
        responseObj: Res,
    ) {
        coEvery { processor.process<Res>(any(), any(), any(), any(), any()) } returns responseObj

        webClient
            .post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                Assertions.assertThat(it).isEqualTo(responseObj)
            }
        coVerify { processor.process<Res>(any(), any(), any(), any(), any()) }
    }
}
