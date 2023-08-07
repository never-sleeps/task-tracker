package me.neversleeps.business.statemachine

import me.neversleeps.common.statemachine.ObjectState

data class SMTransition(
    val state: ObjectState,
    val description: String,
)
