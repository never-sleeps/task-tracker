package me.neversleeps.repository.gremlin.mappers

import me.neversleeps.common.models.AppLock
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.common.models.project.ProjectPermission
import me.neversleeps.common.models.user.UserId
import me.neversleeps.repository.gremlin.ProjectGremlinConst.FIELD_CREATED_BY
import me.neversleeps.repository.gremlin.ProjectGremlinConst.FIELD_DESCRIPTION
import me.neversleeps.repository.gremlin.ProjectGremlinConst.FIELD_ID
import me.neversleeps.repository.gremlin.ProjectGremlinConst.FIELD_LOCK
import me.neversleeps.repository.gremlin.ProjectGremlinConst.FIELD_PERMISSION
import me.neversleeps.repository.gremlin.ProjectGremlinConst.FIELD_TITLE
import me.neversleeps.repository.gremlin.ProjectGremlinConst.FIELD_TMP_RESULT
import me.neversleeps.repository.gremlin.ProjectGremlinConst.RESULT_SUCCESS
import me.neversleeps.repository.gremlin.exceptions.WrongEnumException
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.`__` as gr
import org.apache.tinkerpop.gremlin.structure.Vertex
import org.apache.tinkerpop.gremlin.structure.VertexProperty

fun GraphTraversal<Vertex, Vertex>.addProject(project: Project): GraphTraversal<Vertex, Vertex> =
    this
        .property(VertexProperty.Cardinality.single, FIELD_TITLE, project.title.takeIf { it.isNotBlank() })
        .property(VertexProperty.Cardinality.single, FIELD_DESCRIPTION, project.description.takeIf { it.isNotBlank() })
        .property(VertexProperty.Cardinality.single, FIELD_LOCK, project.lock.takeIf { it != AppLock.NONE }?.asString())
        .property(VertexProperty.Cardinality.single, FIELD_CREATED_BY, project.createdBy.asString().takeIf { it.isNotBlank() }) // здесь можно сделать ссылку на объект владельца
        .property(VertexProperty.Cardinality.single, FIELD_PERMISSION, project.permissions.firstOrNull()?.name)

fun GraphTraversal<Vertex, Vertex>.projectList(result: String = RESULT_SUCCESS): GraphTraversal<Vertex, MutableMap<String, Any>> =
    project<Any?>(
        FIELD_ID,
        FIELD_CREATED_BY,
        FIELD_LOCK,
        FIELD_TITLE,
        FIELD_DESCRIPTION,
        FIELD_PERMISSION,
        FIELD_TMP_RESULT,
    )
        .by(gr.id<Vertex>())
        .by(FIELD_CREATED_BY)
//        .by(gr.inE("Owns").outV().id())
        .by(FIELD_LOCK)
        .by(FIELD_TITLE)
        .by(FIELD_DESCRIPTION)
        .by(FIELD_PERMISSION)
        .by(gr.constant(result))
//        .by(elementMap<Vertex, Map<Any?, Any?>>())

fun Map<String, Any?>.toProject(): Project = Project(
    id = (this[FIELD_ID] as? String)?.let { ProjectId(it) } ?: ProjectId.NONE,
    createdBy = (this[FIELD_CREATED_BY] as? String)?.let { UserId(it) } ?: UserId.NONE,
    lock = (this[FIELD_LOCK] as? String)?.let { AppLock(it) } ?: AppLock.NONE,
    title = (this[FIELD_TITLE] as? String) ?: "",
    description = (this[FIELD_DESCRIPTION] as? String) ?: "",
    permissions = when (val value = this[FIELD_PERMISSION]) {
        ProjectPermission.READ.name -> mutableSetOf(ProjectPermission.READ)
        ProjectPermission.UPDATE.name -> mutableSetOf(ProjectPermission.UPDATE)
        ProjectPermission.DELETE.name -> mutableSetOf(ProjectPermission.DELETE)
        null -> mutableSetOf()
        else -> throw WrongEnumException(
            "Cannot convert object from DB. permission = '$value' cannot be converted to ${ProjectPermission::class}",
        )
    },
)
