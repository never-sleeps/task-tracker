package me.neversleeps.appspring.config

import me.neversleeps.business.ProjectProcessor
import me.neversleeps.common.CorSettings
import me.neversleeps.common.repository.project.IProjectRepository
import me.neversleeps.`in`.memory.project.ProjectRepositoryInMemory
import me.neversleeps.logging.common.LoggerProvider
import me.neversleeps.logging.jvm.mpLoggerLogback
import me.neversleeps.repository.postgresql.RepositoryProjectSQL
import me.neversleeps.repository.postgresql.SqlProperties
import me.neversleeps.repository.stubs.ProjectRepositoryStub
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(SqlPropertiesEx::class)
class CorConfig {
    @Bean
    fun loggerProvider(): LoggerProvider = LoggerProvider { mpLoggerLogback(it) }

    @Bean(name = ["prodRepository"])
    @ConditionalOnProperty(value = ["prod-repository"], havingValue = "sql")
    fun prodRepository(sqlProperties: SqlProperties) = RepositoryProjectSQL(sqlProperties)

    @Bean
    fun testRepository() = ProjectRepositoryInMemory()

    @Bean
    fun stubRepository() = ProjectRepositoryStub()

    @Bean
    fun corSettings(
        @Qualifier("prodRepository") prodRepository: IProjectRepository?,
        @Qualifier("testRepository") testRepository: IProjectRepository,
        @Qualifier("stubRepository") stubRepository: IProjectRepository,
    ): CorSettings = CorSettings(
        loggerProvider = loggerProvider(),
        repositoryStub = stubRepository,
        repositoryTest = testRepository,
        repositoryProd = prodRepository ?: testRepository,
    )

    @Bean
    fun processor(corSettings: CorSettings) = ProjectProcessor(corSettings)
}
