package me.neversleeps.app.multiplatform

import io.ktor.network.selector.* // ktlint-disable no-wildcard-imports
import io.ktor.websocket.* // ktlint-disable no-wildcard-imports
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString
import me.neversleeps.api.multiplatform.apiMapper
import me.neversleeps.api.multiplatform.v1.models.IRequest
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.helpers.addError
import me.neversleeps.common.helpers.asAppError
import me.neversleeps.common.helpers.isUpdatableCommand
import me.neversleeps.mappers.multiplatform.fromInternal.toTransportCreate
import me.neversleeps.mappers.multiplatform.fromTransport.fromTransport
import kotlinx.serialization.decodeFromString
import me.neversleeps.mappers.multiplatform.fromInternal.toTransportInit

private val mutex = Mutex()
private val sessions = mutableSetOf<WebSocketSession>()

suspend fun DefaultWebSocketSession.wsHandlerV2(processor: ProjectProcessor) {
    mutex.withLock { sessions.add(this) }

    val projectContext = ProjectContext()
    val initProject = apiMapper.encodeToString(projectContext.toTransportInit())
    outgoing.send(Frame.Text(initProject))

    incoming.receiveAsFlow().filterIsInstance<Frame.Text>().mapNotNull {
        val frame = it as? Frame.Text ?: return@mapNotNull
        val jsonStr = frame.readText()
        val context = ProjectContext()

        // Handle without flow destruction
        try {
            val request = apiMapper.decodeFromString<IRequest>(jsonStr)
            context.fromTransport(request)
            processor.execute(context)

            val result = apiMapper.encodeToString(context.toTransportCreate())

            // If change request, response is sent to everyone
            if (context.isUpdatableCommand()) {
                mutex.withLock {
                    sessions.forEach { it.send(Frame.Text(result)) }
                }
            } else {
                outgoing.send(Frame.Text(result))
            }
        } catch (e: ClosedChannelCancellationException) {
            mutex.withLock { sessions.clear() }
        } catch (t: Throwable) {
            context.addError(t.asAppError())

            val result = apiMapper.encodeToString(context.toTransportCreate())
            outgoing.send(Frame.Text(result))
        }
    }.collect()
}
