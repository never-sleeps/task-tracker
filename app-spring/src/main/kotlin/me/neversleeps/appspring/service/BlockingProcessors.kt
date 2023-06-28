package me.neversleeps.appspring.service

import kotlinx.coroutines.runBlocking
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.business.TaskProcessor
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.TaskContext
import org.springframework.stereotype.Service

@Service
class ProjectBlockingProcessor {
    private val processor = ProjectProcessor()

    fun execute(context: ProjectContext) = runBlocking { processor.execute(context) }
}

@Service
class TaskBlockingProcessor {
    private val processor = TaskProcessor()

    fun execute(context: TaskContext) = runBlocking { processor.execute(context) }
}
