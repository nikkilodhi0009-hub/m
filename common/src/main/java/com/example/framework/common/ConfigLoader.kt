package com.example.framework.common

import java.io.File
import java.io.IOException

/**
 * Loads and persists the framework configuration files used by the runtime.
 */
object ConfigLoader {
    private const val CONFIG_DIRECTORY_NAME = "config"

    private const val FRAMEWORK_CONFIG_NAME = "framework.json"
    private const val HOOKS_CONFIG_NAME = "hooks.json"
    private const val LOGGING_CONFIG_NAME = "logging.json"

    private const val DEFAULT_FRAMEWORK_CONFIG = """{"mode":"observe","debug":true,"allowUnknownTargets":true,"maxHookRegistrations":256}"""
    private const val DEFAULT_HOOKS_CONFIG = """{"userLifecycle":true,"packageMonitoring":true,"binderMonitoring":false,"requireVerification":true}"""
    private const val DEFAULT_LOGGING_CONFIG = """{"enableStackTrace":true,"logToFile":"/data/local/tmp/framework.log","maxLogLines":10000}"""

    /**
     * Ensures that the default configuration files exist under the supplied base directory.
     */
    fun loadDefaultConfigs(baseDir: File) {
        val configDir = File(baseDir, CONFIG_DIRECTORY_NAME)
        if (!configDir.exists() && !configDir.mkdirs()) {
            throw IOException("Unable to create config directory at ${configDir.absolutePath}")
        }

        writeDefaultConfigIfMissing(File(configDir, FRAMEWORK_CONFIG_NAME), DEFAULT_FRAMEWORK_CONFIG)
        writeDefaultConfigIfMissing(File(configDir, HOOKS_CONFIG_NAME), DEFAULT_HOOKS_CONFIG)
        writeDefaultConfigIfMissing(File(configDir, LOGGING_CONFIG_NAME), DEFAULT_LOGGING_CONFIG)
    }

    /**
     * Returns the configured logging path, if present.
     */
    fun getLoggingPath(baseDir: File): String? {
        val config = loadConfig(baseDir, LOGGING_CONFIG_NAME) ?: return null
        return Regex(""""logToFile"\s*:\s*"([^"]+)"""").find(config)?.groupValues?.get(1)
    }

    /**
     * Reads a config file from the configured directory if it exists.
     */
    fun loadConfig(baseDir: File, name: String): String? {
        val file = File(File(baseDir, CONFIG_DIRECTORY_NAME), name)
        return try {
            if (file.exists() && file.isFile) file.readText(charset = Charsets.UTF_8) else null
        } catch (_: IOException) {
            null
        }
    }

    /**
     * Persists a config file under the configured directory.
     */
    fun saveConfig(baseDir: File, name: String, content: String) {
        val file = File(File(baseDir, CONFIG_DIRECTORY_NAME), name)
        file.parentFile?.mkdirs()
        file.writeText(content, charset = Charsets.UTF_8)
    }

    private fun writeDefaultConfigIfMissing(file: File, content: String) {
        if (file.exists()) {
            return
        }
        file.parentFile?.mkdirs()
        file.writeText(content, charset = Charsets.UTF_8)
    }
}
