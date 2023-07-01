package me.neversleeps.v1

import me.neversleeps.IHandleStrategy

sealed interface IV1HandleStrategy : IHandleStrategy {
    override val version: String
        get() = V1

    companion object {
        const val V1 = "v1"
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
