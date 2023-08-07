package me.neversleeps.business.statemachine

import me.neversleeps.common.statemachine.ObjectState
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

// глобальной логики тут нет. just for learning %)
class SMStateResolver {
    fun resolve(signal: SMSignal): SMTransition {
        require(signal.duration >= 0.milliseconds) { "Publication duration cannot be negative" }
        val sig = Signal(
            state = signal.state,
            duration = SMDuration.values().first { signal.duration >= it.min && signal.duration < it.max },
        )

        return TR_MATRIX[sig] ?: TR_ERROR
    }

    companion object {
        private enum class SMDuration(val min: Duration, val max: Duration) {
            D_NEW(0.seconds, 7.days),
            D_ADULT(7.days, 100.days),
            D_OLD(100.days, Int.MAX_VALUE.seconds),
        }
        private data class Signal(
            val state: ObjectState,
            val duration: SMDuration,
        )

        private val TR_MATRIX = mapOf(
            Signal(ObjectState.NEW, SMDuration.D_NEW)
                to SMTransition(ObjectState.NEW, "Новый, без изменений"),
            Signal(ObjectState.NEW, SMDuration.D_ADULT)
                to SMTransition(ObjectState.ADULT, "Объект теперь взрослый"),

            Signal(ObjectState.ADULT, SMDuration.D_ADULT)
                to SMTransition(ObjectState.ADULT, "Объект взрослый, без изменений"),
            Signal(ObjectState.ADULT, SMDuration.D_OLD)
                to SMTransition(ObjectState.OLD, "Объект устарел"),
            Signal(ObjectState.OLD, SMDuration.D_OLD)
                to SMTransition(ObjectState.OLD, "Объект старый, без изменений"),
        )
        private val TR_ERROR = SMTransition(ObjectState.ERROR, "Unprovided transition occurred")
    }
}
