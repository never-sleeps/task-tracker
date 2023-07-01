package me.neversleeps.v2

import me.neversleeps.IHandleStrategy

sealed interface IV2HandleStrategy : IHandleStrategy {
    override val version: String
        get() = V2

    companion object {
        const val V2 = "v2"
        private val strategies = listOf(
            CreateProjectHandler,
            ReadProjectHandler,
            UpdateProjectHandler,
            DeleteProjectHandler,
            SearchProjectHandler,

            CreateTaskHandler,
            ReadTaskHandler,
            UpdateTaskHandler,
            DeleteTaskHandler,
            SearchTaskHandler,
        )
        val strategiesByDiscriminator = strategies.associateBy { it.path }
    }
}
