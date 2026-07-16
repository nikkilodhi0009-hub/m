package com.example.framework.common

import java.io.File

class LifecycleLogWriter(private val path: String = "/data/local/tmp/framework.log") {
    fun append(event: String) {
        val file = File(path)
        file.parentFile?.mkdirs()
        file.appendText(event + System.lineSeparator())
    }

    fun appendEvent(header: String, details: Map<String, Any?>) {
        val builder = StringBuilder()
        builder.appendLine("======== $header ========")
        details.forEach { (key, value) ->
            builder.appendLine("$key: ${value ?: "unknown"}")
        }
        builder.appendLine("====================================")
        append(builder.toString())
    }
}
