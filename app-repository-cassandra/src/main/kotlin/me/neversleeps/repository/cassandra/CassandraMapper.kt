package me.neversleeps.repository.cassandra

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace
import com.datastax.oss.driver.api.mapper.annotations.DaoTable
import com.datastax.oss.driver.api.mapper.annotations.Mapper
import me.neversleeps.repository.cassandra.ProjectCassandraDAO

@Mapper
interface CassandraMapper {
    @DaoFactory
    fun projectDao(@DaoKeyspace keyspace: String, @DaoTable tableName: String): ProjectCassandraDAO

    companion object {
        fun builder(session: CqlSession) = CassandraMapperBuilder(session)
    }
}
