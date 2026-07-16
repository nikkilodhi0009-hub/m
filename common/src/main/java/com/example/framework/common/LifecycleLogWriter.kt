package com.example.framework.common

import java.io.File
import java.io.IOException

/**
 * Appends lifecycle diagnostics to a log file in a stable and resilient manner.
 */
class LifecycleLogWriter(private val path: String = "/data/local/tmp/framework.log") {
    private val separator = "===================================="

    /**
     * Appends a raw event line to the configured log file.
     */
    fun append(event: String) {
        writeLine(event)
    }

    /**
     * Formats a structured event block and appends it to the configured log file.
     */
    fun appendEvent(header: String, details: Map<String, Any?>) {
        val builder = StringBuilder()
        builder.appendLine("======== $header ========")
        details.entries.sortedBy { it.key.lowercase() }.forEach { (key, value) ->
            builder.appendLine("$key: ${value ?: "unknown"}")
        }
        builder.appendLine(separator)
        append(builder.toString())
    }

    private fun writeLine(rawContent: String) {
        try {
            val file = File(path)
            file.parentFile?.mkdirs()
            file.appendText(rawContent + System.lineSeparator(), charset = Charsets.UTF_8)
        } catch (_: IOException) {
            System.err.println("Unable to append lifecycle log entry to $path")
        }
    }
}
