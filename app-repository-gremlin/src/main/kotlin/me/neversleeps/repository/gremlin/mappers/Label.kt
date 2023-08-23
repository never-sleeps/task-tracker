package me.neversleeps.repository.gremlin.mappers

import me.neversleeps.common.models.project.Project

fun Project.label(): String? = this::class.simpleName
