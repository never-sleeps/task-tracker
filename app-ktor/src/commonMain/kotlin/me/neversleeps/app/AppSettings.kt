package me.neversleeps.app

import me.neversleeps.business.ProjectProcessor
import me.neversleeps.business.TaskProcessor
import me.neversleeps.common.CorSettings

data class AppSettings(
    val appUrls: List<String>,
    val corSettings: CorSettings,
    val projectProcessor: ProjectProcessor,
    val taskProcessor: TaskProcessor
)
