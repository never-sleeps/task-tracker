package me.neversleeps.business.statemachine

import me.neversleeps.common.statemachine.ObjectState
import kotlin.time.Duration

data class SMSignal(
    val state: ObjectState,
    val duration: Duration,
)
