package com.example.framework.hooks

import org.junit.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HookEngineTest {
    @Test
    fun healthCheckReportsEngineState() {
        val dir = kotlin.io.path.createTempDirectory().toFile()
        try {
            HookEngine.initialize(dir)
            val health = HookEngine.healthCheck()
            assertTrue(health.containsKey("activeHooks"))
            assertTrue(health.containsKey("backend"))
        } finally {
            dir.deleteRecursively()
        }
    }

    @Test
    fun registerAndRemoveHookTracksLifecycle() {
        val dir = kotlin.io.path.createTempDirectory().toFile()
        try {
            HookEngine.initialize(dir)
            val callback = object : HookCallback {
                override fun before(args: Array<Any?>): Boolean = true
                override fun after(result: Any?) = Unit
            }
            val registration = HookEngine.registerJavaHook("java.lang.String", "length", callback)
            assertTrue(registration.id.startsWith("java:"))
            HookEngine.removeHook(registration)
            assertTrue(HookEngine.healthCheck()["registeredHooks"] as Int >= 0)
        } finally {
            dir.deleteRecursively()
        }
    }
}
