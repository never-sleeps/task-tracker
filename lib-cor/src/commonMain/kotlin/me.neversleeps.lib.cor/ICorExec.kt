package me.neversleeps.lib.cor

/**
 * Блок кода, который обрабатывает контекст.
 */
interface ICorExec<T> {
    val title: String
    val description: String
    suspend fun exec(context: T)
}
