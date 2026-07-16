package com.example.framework.common

import java.io.File

object ConfigLoader {
    fun loadDefaultConfigs(baseDir: File) {
        val configDir = File(baseDir, "config")
        configDir.mkdirs()

        val frameworkConfig = File(configDir, "framework.json")
        if (!frameworkConfig.exists()) {
            frameworkConfig.writeText("""{"mode":"observe","debug":true}""")
        }

        val hooksConfig = File(configDir, "hooks.json")
        if (!hooksConfig.exists()) {
            hooksConfig.writeText("""{"userLifecycle":true,"packageMonitoring":true,"binderMonitoring":false}""")
        }

        val loggingConfig = File(configDir, "logging.json")
        if (!loggingConfig.exists()) {
            loggingConfig.writeText("""{"enableStackTrace":true,"logToFile":"/data/local/tmp/framework.log"}""")
        }
    }
}
