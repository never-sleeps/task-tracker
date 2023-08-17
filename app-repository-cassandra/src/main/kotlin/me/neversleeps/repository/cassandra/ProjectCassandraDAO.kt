package me.neversleeps.repository.cassandra

import com.datastax.oss.driver.api.mapper.annotations.Dao
import com.datastax.oss.driver.api.mapper.annotations.Delete
import com.datastax.oss.driver.api.mapper.annotations.Insert
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider
import com.datastax.oss.driver.api.mapper.annotations.Select
import com.datastax.oss.driver.api.mapper.annotations.Update
import me.neversleeps.common.repository.project.DbProjectFilterRequest
import me.neversleeps.repository.cassandra.model.ProjectCassandraDTO
import java.util.concurrent.CompletionStage

@Dao
interface ProjectCassandraDAO {
    @Insert
    fun create(dto: ProjectCassandraDTO): CompletionStage<Unit>

    @Select
    fun read(id: String): CompletionStage<ProjectCassandraDTO?>

    @Update(customIfClause = "lock = :prevLock")
    fun update(dto: ProjectCassandraDTO, prevLock: String): CompletionStage<Boolean>

    @Delete(
        customWhereClause = "id = :id",
        customIfClause = "lock = :prevLock",
        entityClass = [ProjectCassandraDTO::class],
    )
    fun delete(id: String, prevLock: String): CompletionStage<Boolean>

    @QueryProvider(
        providerClass = ProjectCassandraSearchProvider::class,
        entityHelpers = [ProjectCassandraDTO::class],
    )
    fun search(filter: DbProjectFilterRequest): CompletionStage<Collection<ProjectCassandraDTO>>
}
