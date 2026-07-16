package com.example.framework.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Writes concise structured log messages to standard output.
 */
class Logger(private val tag: String) {
    private val timestampFormatter = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)
    }

    /**
     * Logs an informational message.
     */
    fun info(message: String, metadata: Map<String, Any> = emptyMap()) {
        log(message, metadata, level = "INFO")
    }

    /**
     * Logs a debug-level message.
     */
    fun debug(message: String, metadata: Map<String, Any> = emptyMap()) {
        log(message, metadata, level = "DEBUG")
    }

    private fun log(message: String, metadata: Map<String, Any>, level: String) {
        val timestamp = timestampFormatter.get().format(Date())
        val formatted = buildString {
            append("[$timestamp] [$level] [$tag] $message")
            if (metadata.isNotEmpty()) {
                append(" | ")
                val sortedEntries = metadata.entries.toList().sortedBy { it.key }
                append(sortedEntries.joinToString(separator = " ") { "${it.key}=${it.value}" })
            }
        }
        println(formatted)
    }
}
