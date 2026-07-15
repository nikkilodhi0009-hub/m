package com.example.framework.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Logger(private val tag: String) {
    fun info(message: String, metadata: Map<String, Any> = emptyMap()) {
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US).format(Date())
        val formatted = buildString {
            append("[$timestamp] [$tag] $message")
            if (metadata.isNotEmpty()) {
                append(" | ")
                append(metadata.entries.joinToString { "${it.key}=${it.value}" })
            }
        }
        println(formatted)
    }

    fun debug(message: String, metadata: Map<String, Any> = emptyMap()) {
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US).format(Date())
        val formatted = buildString {
            append("[$timestamp] [$tag] $message")
            if (metadata.isNotEmpty()) {
                append(" | ")
                append(metadata.entries.joinToString { "${it.key}=${it.value}" })
            }
        }
        println(formatted)
    }
}
