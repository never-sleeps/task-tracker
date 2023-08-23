package me.neversleeps.appspring.config

import me.neversleeps.repository.postgresql.SqlProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

// Нужна аннотация @ConstructorBinding, ее нельзя поставить над методом c @Bean, пока так
@ConfigurationProperties("sql")
class SqlPropertiesEx
@ConstructorBinding
constructor(
    url: String,
    user: String,
    password: String,
    schema: String,
    dropDatabase: Boolean,
) : SqlProperties(url, user, password, schema, dropDatabase)
