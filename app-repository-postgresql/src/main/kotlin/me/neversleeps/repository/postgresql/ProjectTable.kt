package me.neversleeps.repository.postgresql

import me.neversleeps.common.models.AppLock
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.common.models.project.ProjectPermission
import me.neversleeps.common.models.user.UserId
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object ProjectTable : Table("project") {
    val id = varchar("id", 128)
    val title = varchar("title", 128)
    val description = varchar("description", 512)
    val createdBy = varchar("created_by", 128)
    val permission = enumeration("permission", ProjectPermission::class)
    val lock = varchar("lock", 50)

    override val primaryKey = PrimaryKey(id)

    fun from(res: InsertStatement<Number>) = Project(
        id = ProjectId(res[id].toString()),
        title = res[title],
        description = res[description],
        createdBy = UserId(res[createdBy].toString()),
        permissions = mutableSetOf(res[permission]),
        lock = AppLock(res[lock]),
    )

    fun from(res: ResultRow) = Project(
        id = ProjectId(res[id].toString()),
        title = res[title],
        description = res[description],
        createdBy = UserId(res[createdBy].toString()),
        permissions = mutableSetOf(res[permission]),
        lock = AppLock(res[lock]),
    )

    fun to(it: UpdateBuilder<*>, project: Project, randomUuid: () -> String) {
        it[id] = project.id.takeIf { it != ProjectId.NONE }?.asString() ?: randomUuid()
        it[title] = project.title
        it[description] = project.description
        it[createdBy] = project.createdBy.asString()
        it[permission] = project.permissions.first()
        it[lock] = project.lock.takeIf { it != AppLock.NONE }?.asString() ?: randomUuid()
    }
}
