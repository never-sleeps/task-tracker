package me.neversleeps.app.jackson

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.network.selector.* // ktlint-disable no-wildcard-imports
import io.ktor.websocket.* // ktlint-disable no-wildcard-imports
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.neversleeps.api.jackson.apiMapper
import me.neversleeps.api.jackson.v1.models.IRequest
import me.neversleeps.app.AppSettings
import me.neversleeps.common.TaskContext
import me.neversleeps.common.helpers.addError
import me.neversleeps.common.helpers.asAppError
import me.neversleeps.common.helpers.isUpdatableCommand
import me.neversleeps.mappers.jackson.fromInternal.toTransportCreate
import me.neversleeps.mappers.jackson.fromInternal.toTransportInit
import me.neversleeps.mappers.jackson.fromTransport.fromTransport

class WsTaskControllerV1 {
    private val mutex = Mutex()
    private val sessions = mutableSetOf<WebSocketSession>()

    suspend fun handle(session: WebSocketSession, appSettings: AppSettings) {
        mutex.withLock {
            sessions.add(session)
        }

        val taskContext = TaskContext()
        val initTask = apiMapper.writeValueAsString(taskContext.toTransportInit())
        session.outgoing.send(Frame.Text(initTask))

        session.incoming.receiveAsFlow().filterIsInstance<Frame.Text>().mapNotNull {
            val frame = it as? Frame.Text ?: return@mapNotNull
            val jsonStr = frame.readText()
            val context = TaskContext()

            // Handle without flow destruction
            try {
                val request = apiMapper.readValue<IRequest>(jsonStr)
                context.fromTransport(request)
                appSettings.taskProcessor.execute(context)

                val result = apiMapper.writeValueAsString(context.toTransportCreate())

                // If change request, response is sent to everyone
                if (context.isUpdatableCommand()) {
                    mutex.withLock {
                        sessions.forEach {
                            it.send(Frame.Text(result))
                        }
                    }
                } else {
                    session.outgoing.send(Frame.Text(result))
                }
            } catch (e: ClosedChannelCancellationException) {
                mutex.withLock {
                    sessions.clear()
                }
            } catch (t: Throwable) {
                context.addError(t.asAppError())

                val result = apiMapper.writeValueAsString(context.toTransportCreate())
                session.outgoing.send(Frame.Text(result))
            }
        }.collect()
    }
}
