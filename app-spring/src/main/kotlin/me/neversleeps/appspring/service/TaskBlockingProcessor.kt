package me.neversleeps.appspring.service

import kotlinx.coroutines.runBlocking
import me.neversleeps.business.TaskProcessor
import me.neversleeps.common.CorSettings
import me.neversleeps.common.TaskContext
import org.springframework.stereotype.Service

@Service
class TaskBlockingProcessor {
    private val processor = TaskProcessor(CorSettings())

    fun execute(context: TaskContext) = runBlocking { processor.execute(context) }
}
