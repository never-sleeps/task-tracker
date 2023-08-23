package me.neversleeps.repository.cassandra

import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.mapper.MapperContext
import com.datastax.oss.driver.api.mapper.entity.EntityHelper
import com.datastax.oss.driver.api.querybuilder.QueryBuilder
import me.neversleeps.common.models.user.UserId
import me.neversleeps.common.repository.project.DbProjectFilterRequest
import me.neversleeps.repository.cassandra.model.ProjectCassandraDTO
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.function.BiConsumer

class ProjectCassandraSearchProvider(
    private val context: MapperContext,
    private val entityHelper: EntityHelper<ProjectCassandraDTO>,
) {
    fun search(filter: DbProjectFilterRequest): CompletionStage<Collection<ProjectCassandraDTO>> {
        var select = entityHelper.selectStart().allowFiltering()

        if (filter.searchText.isNotBlank()) {
            select = select
                .whereColumn(ProjectCassandraDTO.COLUMN_TITLE)
                .like(QueryBuilder.literal("%${filter.searchText}%"))
        }
        if (filter.createdBy != UserId.NONE) {
            select = select
                .whereColumn(ProjectCassandraDTO.COLUMN_CREATED_BY)
                .isEqualTo(QueryBuilder.literal(filter.createdBy.asString(), context.session.context.codecRegistry))
        }

        val asyncFetcher = AsyncFetcher()

        context.session
            .executeAsync(select.build())
            .whenComplete(asyncFetcher)

        return asyncFetcher.stage
    }

    inner class AsyncFetcher : BiConsumer<AsyncResultSet?, Throwable?> {
        private val buffer = mutableListOf<ProjectCassandraDTO>()
        private val future = CompletableFuture<Collection<ProjectCassandraDTO>>()
        val stage: CompletionStage<Collection<ProjectCassandraDTO>> = future

        override fun accept(resultSet: AsyncResultSet?, t: Throwable?) {
            when {
                t != null -> future.completeExceptionally(t)
                resultSet == null -> future.completeExceptionally(IllegalStateException("ResultSet should not be null"))
                else -> {
                    buffer.addAll(resultSet.currentPage().map { entityHelper.get(it, false) })
                    if (resultSet.hasMorePages()) {
                        resultSet.fetchNextPage().whenComplete(this)
                    } else {
                        future.complete(buffer)
                    }
                }
            }
        }
    }
}
