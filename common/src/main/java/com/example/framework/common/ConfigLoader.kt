package com.example.framework.common

import java.io.File

object ConfigLoader {
    fun loadDefaultConfigs(baseDir: File) {
        val configDir = File(baseDir, "config")
        configDir.mkdirs()

        val frameworkConfig = File(configDir, "framework.json")
        if (!frameworkConfig.exists()) {
            frameworkConfig.writeText("""{"mode":"observe","debug":true,"allowUnknownTargets":true,"maxHookRegistrations":256}""")
        }

        val hooksConfig = File(configDir, "hooks.json")
        if (!hooksConfig.exists()) {
            hooksConfig.writeText("""{"userLifecycle":true,"packageMonitoring":true,"binderMonitoring":false,"requireVerification":true}""")
        }

        val loggingConfig = File(configDir, "logging.json")
        if (!loggingConfig.exists()) {
            loggingConfig.writeText("""{"enableStackTrace":true,"logToFile":"/data/local/tmp/framework.log","maxLogLines":10000}""")
        }
    }

    fun loadConfig(baseDir: File, name: String): String? {
        val file = File(File(baseDir, "config"), name)
        return if (file.exists()) file.readText() else null
    }

    fun saveConfig(baseDir: File, name: String, content: String) {
        val file = File(File(baseDir, "config"), name)
        file.parentFile?.mkdirs()
        file.writeText(content)
    }
}
