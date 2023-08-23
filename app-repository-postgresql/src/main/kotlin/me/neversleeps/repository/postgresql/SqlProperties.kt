package me.neversleeps.repository.postgresql

open class SqlProperties(
    val url: String = "jdbc:postgresql://localhost:5432/tracker",
    val user: String = "postgres",
    val password: String = "tracker-pass",
    val schema: String = "tracker",
    // Delete tables at startup - needed for testing
    val dropDatabase: Boolean = false,
)
