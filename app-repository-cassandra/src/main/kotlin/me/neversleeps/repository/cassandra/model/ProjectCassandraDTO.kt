package me.neversleeps.repository.cassandra.model

import com.datastax.oss.driver.api.core.type.DataTypes
import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import me.neversleeps.common.models.AppLock
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.common.models.user.UserId

@Entity
data class ProjectCassandraDTO(
    @field:CqlName(COLUMN_ID)
    @field:PartitionKey // можно задать порядок
    var id: String? = null,
    @field:CqlName(COLUMN_TITLE)
    var title: String? = null,
    @field:CqlName(COLUMN_DESCRIPTION)
    var description: String? = null,
    @field:CqlName(COLUMN_CREATED_BY)
    var createdBy: String? = null,
    // Нельзя использовать в моделях хранения внутренние модели,
    // поскольку при изменении внутренних моделей, БД автоматически не изменится, а потому будет Runtime ошибка
    @field:CqlName(COLUMN_PERMISSION)
    var permission: ProjectPermissionEntity? = null,
    @field:CqlName(COLUMN_LOCK)
    var lock: String?,
) {
    constructor(project: Project) : this(
        createdBy = project.createdBy.takeIf { it != UserId.NONE }?.asString(),
        id = project.id.takeIf { it != ProjectId.NONE }?.asString(),
        title = project.title.takeIf { it.isNotBlank() },
        description = project.description.takeIf { it.isNotBlank() },
        permission = project.permissions.firstOrNull().toInnerModel(),
        lock = project.lock.takeIf { it != AppLock.NONE }?.asString(),
    )

    fun toTransportModel(): Project =
        Project(
            createdBy = createdBy?.let { UserId(it) } ?: UserId.NONE,
            id = id?.let { ProjectId(it) } ?: ProjectId.NONE,
            title = title ?: "",
            description = description ?: "",
            permissions = permission?.fromInnerModel()?.let { mutableSetOf(it) } ?: mutableSetOf(),
            lock = lock?.let { AppLock(it) } ?: AppLock.NONE,
        )

    companion object {
        const val TABLE_NAME = "projects"

        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_CREATED_BY = "created_by"
        const val COLUMN_PERMISSION = "permission"
        const val COLUMN_LOCK = "lock"

        fun table(keyspace: String, tableName: String) =
            SchemaBuilder
                .createTable(keyspace, tableName)
                .ifNotExists()
                .withPartitionKey(COLUMN_ID, DataTypes.TEXT)
                .withColumn(COLUMN_TITLE, DataTypes.TEXT)
                .withColumn(COLUMN_DESCRIPTION, DataTypes.TEXT)
                .withColumn(COLUMN_CREATED_BY, DataTypes.TEXT)
                .withColumn(COLUMN_PERMISSION, DataTypes.TEXT)
                .withColumn(COLUMN_LOCK, DataTypes.TEXT)
                .build()

        fun titleIndex(keyspace: String, tableName: String, locale: String = "en") =
            SchemaBuilder
                .createIndex()
                .ifNotExists()
                .usingSASI()
                .onTable(keyspace, tableName)
                .andColumn(COLUMN_TITLE)
                .withSASIOptions(mapOf("mode" to "CONTAINS", "tokenization_locale" to locale))
                .build()
    }
}
