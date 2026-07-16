package com.example.framework.hooks

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class HookLifecycleManagerTest {
    @Test
    fun tracksLifecycleTransitions() {
        val registration = HookRegistration(
            id = "test-hook",
            className = "java.lang.String",
            methodName = "length",
            callback = object : HookCallback {
                override fun before(args: Array<Any?>): Boolean = true
                override fun after(result: Any?) = Unit
            }
        )
        val manager = HookLifecycleManager()

        val entry = manager.register(registration)
        assertEquals(HookState.PENDING, entry.state)
        manager.enable(registration)
        assertEquals(HookState.INSTALLED, manager.getEntry(registration)?.state)

        assertTrue(manager.markInstalled(registration))
        assertEquals(1, manager.getEntry(registration)?.installCount)
        assertNull(manager.getEntry(registration)?.lastError)

        assertTrue(manager.markFailed(registration, "boom"))
        assertEquals(HookState.FAILED, manager.getEntry(registration)?.state)
        assertEquals("boom", manager.getEntry(registration)?.lastError)

        assertTrue(manager.rollback(registration))
        assertEquals(HookState.ROLLED_BACK, manager.getEntry(registration)?.state)
        assertTrue(manager.remove(registration))
        assertFalse(manager.remove(registration))
    }

    @Test
    fun rejectsInvalidTransitions() {
        val registration = HookRegistration(
            id = "invalid-transition",
            className = "java.lang.String",
            callback = object : HookCallback {
                override fun before(args: Array<Any?>): Boolean = true
                override fun after(result: Any?) = Unit
            }
        )
        val manager = HookLifecycleManager()
        manager.register(registration)

        assertTrue(manager.enable(registration))
        assertTrue(manager.rollback(registration))
        val exception = kotlin.runCatching { manager.enable(registration) }.exceptionOrNull()
        assertTrue(exception is IllegalArgumentException)
    }
}
