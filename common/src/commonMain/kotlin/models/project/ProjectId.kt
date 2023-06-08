package me.neversleeps.common.models.project

import kotlin.jvm.JvmInline

@JvmInline
value class ProjectId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = ProjectId("")
    }
}
