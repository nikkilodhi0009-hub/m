package com.example.framework.common

import org.junit.Test
import java.io.File
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
}
