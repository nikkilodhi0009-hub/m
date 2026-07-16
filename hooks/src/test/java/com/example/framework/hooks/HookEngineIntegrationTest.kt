package com.example.framework.hooks

import org.junit.Test
import java.io.File
import kotlin.test.assertTrue

class HookEngineIntegrationTest {
    @Test
    fun initializesEngineAndReportsHealth() {
        val dir = kotlin.io.path.createTempDirectory().toFile()
        try {
            HookEngine.initialize(dir)
            val health = HookEngine.healthCheck()
            assertTrue(health.containsKey("activeHooks"))
            assertTrue(health.containsKey("registeredHooks"))
            assertTrue(health.containsKey("diagnosticCount"))
        } finally {
            dir.deleteRecursively()
        }
    }
}
