package com.example.framework.hooks

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HookRegistryTest {
    @Test
    fun storesAndRemovesRegistrations() {
        val registry = HookRegistry()
        val registration = HookRegistration(
            id = "registry-entry",
            className = "java.lang.String",
            methodName = "length",
            callback = object : HookCallback {
                override fun before(args: Array<Any?>): Boolean = true
                override fun after(result: Any?) = Unit
            }
        )

        registry.add(registration)
        assertTrue(registry.contains(registration.id))
        assertEquals(1, registry.size())
        assertEquals(registration, registry.get(registration.id))

        assertTrue(registry.remove(registration))
        assertFalse(registry.contains(registration.id))
    }
}
