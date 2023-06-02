package ru.otus.otuskotlin.marketplace.common.models.task

import kotlin.jvm.JvmInline

@JvmInline
value class TaskId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = TaskId("")
    }
}
