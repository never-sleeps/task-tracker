package me.neversleeps.app

import me.neversleeps.app.base.KtorAuthConfig
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.business.TaskProcessor
import me.neversleeps.common.CorSettings

data class AppSettings(
    val appUrls: List<String> = emptyList(),
    val corSettings: CorSettings,
    val projectProcessor: ProjectProcessor = ProjectProcessor(settings = corSettings),
    val taskProcessor: TaskProcessor = TaskProcessor(settings = corSettings),
    val auth: KtorAuthConfig = KtorAuthConfig.NONE,
)
