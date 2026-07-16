package com.example.framework.common

import org.junit.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ConfigLoaderTest {
    @Test
    fun writesDefaultConfigsWhenMissing() {
        val tempDir = kotlin.io.path.createTempDirectory().toFile()
        try {
            ConfigLoader.loadDefaultConfigs(tempDir)
            assertTrue(File(tempDir, "config/framework.json").exists())
            assertTrue(File(tempDir, "config/hooks.json").exists())
            assertTrue(File(tempDir, "config/logging.json").exists())
        } finally {
            tempDir.deleteRecursively()
        }
    }

    @Test
    fun savesAndLoadsConfigContent() {
        val tempDir = kotlin.io.path.createTempDirectory().toFile()
        try {
            ConfigLoader.saveConfig(tempDir, "custom.json", "{\"hello\":\"world\"}")
            assertEquals("{\"hello\":\"world\"}", ConfigLoader.loadConfig(tempDir, "custom.json"))
        } finally {
            tempDir.deleteRecursively()
        }
    }

    @Test
    fun returnsNullWhenConfigIsMissing() {
        val tempDir = kotlin.io.path.createTempDirectory().toFile()
        try {
            assertNull(ConfigLoader.loadConfig(tempDir, "missing.json"))
        } finally {
            tempDir.deleteRecursively()
        }
    }

    @Test
    fun lifecycleLogWriterAppendsFormattedEvents() {
        val tempDir = kotlin.io.path.createTempDirectory().toFile()
        val logFile = File(tempDir, "lifecycle.log")
        try {
            val writer = LifecycleLogWriter(logFile.absolutePath)
            writer.appendEvent("TEST_EVENT", mapOf("foo" to "bar", "count" to 2))
            val contents = logFile.readText()
            assertNotNull(contents)
            assertTrue(contents.contains("TEST_EVENT"))
            assertTrue(contents.contains("count: 2"))
        } finally {
            tempDir.deleteRecursively()
        }
    }
}
