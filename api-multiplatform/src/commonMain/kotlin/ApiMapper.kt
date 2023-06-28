package me.neversleeps.api.multiplatform

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import me.neversleeps.api.multiplatform.v1.models.IRequest
import me.neversleeps.api.multiplatform.v1.models.IResponse
import me.neversleeps.api.multiplatform.v1.models.InitBaseResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectDeleteRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectDeleteResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectReadRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectReadResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectSearchRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectSearchResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectUpdateRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectUpdateResponse
import me.neversleeps.api.multiplatform.v1.models.TaskCreateRequest
import me.neversleeps.api.multiplatform.v1.models.TaskCreateResponse
import me.neversleeps.api.multiplatform.v1.models.TaskDeleteRequest
import me.neversleeps.api.multiplatform.v1.models.TaskDeleteResponse
import me.neversleeps.api.multiplatform.v1.models.TaskReadRequest
import me.neversleeps.api.multiplatform.v1.models.TaskReadResponse
import me.neversleeps.api.multiplatform.v1.models.TaskSearchRequest
import me.neversleeps.api.multiplatform.v1.models.TaskSearchResponse
import me.neversleeps.api.multiplatform.v1.models.TaskUpdateRequest
import me.neversleeps.api.multiplatform.v1.models.TaskUpdateResponse

/**
 * Добавляйте сюда элементы при появлении новых наследников IRequest / IResponse
 */
internal val infos = listOf(
    info(InitBaseResponse::class, IResponse::class, "init") { copy(responseType = it) },

    info(ProjectCreateRequest::class, IRequest::class, "createProject") { copy(requestType = it) },
    info(ProjectReadRequest::class, IRequest::class, "readProject") { copy(requestType = it) },
    info(ProjectUpdateRequest::class, IRequest::class, "updateProject") { copy(requestType = it) },
    info(ProjectDeleteRequest::class, IRequest::class, "deleteProject") { copy(requestType = it) },
    info(ProjectSearchRequest::class, IRequest::class, "searchProject") { copy(requestType = it) },

    info(ProjectCreateResponse::class, IResponse::class, "createProject") { copy(responseType = it) },
    info(ProjectReadResponse::class, IResponse::class, "readProject") { copy(responseType = it) },
    info(ProjectUpdateResponse::class, IResponse::class, "updateProject") { copy(responseType = it) },
    info(ProjectDeleteResponse::class, IResponse::class, "deleteProject") { copy(responseType = it) },
    info(ProjectSearchResponse::class, IResponse::class, "searchProject") { copy(responseType = it) },

    info(TaskCreateRequest::class, IRequest::class, "createTask") { copy(requestType = it) },
    info(TaskReadRequest::class, IRequest::class, "readTask") { copy(requestType = it) },
    info(TaskUpdateRequest::class, IRequest::class, "updateTask") { copy(requestType = it) },
    info(TaskDeleteRequest::class, IRequest::class, "deleteTask") { copy(requestType = it) },
    info(TaskSearchRequest::class, IRequest::class, "searchTask") { copy(requestType = it) },

    info(TaskCreateResponse::class, IResponse::class, "createTask") { copy(responseType = it) },
    info(TaskReadResponse::class, IResponse::class, "readTask") { copy(responseType = it) },
    info(TaskUpdateResponse::class, IResponse::class, "updateTask") { copy(responseType = it) },
    info(TaskDeleteResponse::class, IResponse::class, "deleteTask") { copy(responseType = it) },
    info(TaskSearchResponse::class, IResponse::class, "searchTask") { copy(responseType = it) },
)

val apiMapper = Json {
    classDiscriminator = "_"
    encodeDefaults = true
    ignoreUnknownKeys = true

    serializersModule = SerializersModule {
        setupPolymorphic()
    }
}

fun apiRequestSerialize(request: IRequest): String = apiMapper.encodeToString(request)

@Suppress("UNCHECKED_CAST")
fun <T : IRequest> apiRequestDeserialize(json: String): T =
    apiMapper.decodeFromString<IRequest>(json) as T

fun apiResponseSerialize(response: IResponse): String = apiMapper.encodeToString(response)

@Suppress("UNCHECKED_CAST")
fun <T : IResponse> apiResponseDeserialize(json: String): T =
    apiMapper.decodeFromString<IResponse>(json) as T
