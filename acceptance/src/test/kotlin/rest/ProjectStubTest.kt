package rest

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCaseSeverityLevel
import io.kotest.matchers.shouldBe
import stub.ProjectStub

class ProjectStubTest : BehaviorSpec({
    severity = TestCaseSeverityLevel.MINOR

    given("Create new project") {
        `when`("Create new project") {
            val response = RestClient.request(ProjectPaths.create, ProjectStub.createRequest)
            then("Project is created") {
                response shouldBe ProjectStub.createResponse
            }
        }
    }

    given("Read project") {
        `when`("Get project by id") {
            then("Project data") {
                val response = RestClient.request(ProjectPaths.read, ProjectStub.readRequest)
                response shouldBe ProjectStub.readResponse
            }
        }
    }

    given("project card") {
        `when`("Updating existing project by required fields") {
            then("Project is updated") {
                val response = RestClient.request(ProjectPaths.update, ProjectStub.updateRequest)
                response shouldBe ProjectStub.updateResponse
            }
        }
    }

    given("Delete project") {
        `when`("Delete project") {
            then("Project is deleted") {
                val response = RestClient.request(ProjectPaths.delete, ProjectStub.deleteRequest)
                response shouldBe ProjectStub.deleteResponse
            }
        }
    }
})
