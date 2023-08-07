package me.neversleeps.business.project.statemachine

import me.neversleeps.business.statemachine.SMSignal
import me.neversleeps.business.statemachine.SMStateResolver
import me.neversleeps.common.statemachine.ObjectState
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.days

class StateMachineTest {

    @Test
    fun newToNew() {
        val machine = SMStateResolver()
        val signal = SMSignal(
            state = ObjectState.NEW,
            duration = 1.days,
        )
        val transition = machine.resolve(signal)
        assertEquals(ObjectState.NEW, transition.state)
        assertContains(transition.description, "без изменений", ignoreCase = true)
    }

    @Test
    fun newToAdult() {
        val machine = SMStateResolver()
        val signal = SMSignal(
            state = ObjectState.NEW,
            duration = 10.days,
        )
        val transition = machine.resolve(signal)
        assertEquals(ObjectState.ADULT, transition.state)
        assertContains(transition.description, "теперь взрослый", ignoreCase = true)
    }

    @Test
    fun adultToOld() {
        val machine = SMStateResolver()
        val signal = SMSignal(
            state = ObjectState.ADULT,
            duration = 101.days,
        )
        val transition = machine.resolve(signal)
        assertEquals(ObjectState.OLD, transition.state)
        assertContains(transition.description, "устарел", ignoreCase = true)
    }

    @Test
    fun newToOld() {
        val machine = SMStateResolver()
        val signal = SMSignal(
            state = ObjectState.NEW,
            duration = 101.days,
        )
        val transition = machine.resolve(signal)
        assertEquals(ObjectState.ERROR, transition.state)
        assertContains(transition.description, "Unprovided transition occurred", ignoreCase = true)
    }
}
