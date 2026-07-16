package com.example.framework.hooks

import java.util.concurrent.ConcurrentHashMap

/**
 * Captures diagnostic records for hook registration and installation events.
 */
class HookDiagnostics {
    private val records = ConcurrentHashMap<String, HookDiagnosticRecord>()

    /**
     * Records a diagnostic entry for the supplied registration.
     */
    fun record(registration: HookRegistration, message: String, level: String = "INFO") {
        records[registration.id] = HookDiagnosticRecord(
            registrationId = registration.id,
            message = message,
            level = level,
            timestamp = System.currentTimeMillis()
        )
    }

    /**
     * Returns a snapshot of all recorded diagnostics.
     */
    fun snapshot(): List<HookDiagnosticRecord> = records.values.toList()

    /**
     * Clears all diagnostic entries.
     */
    fun clear() {
        records.clear()
    }
}

/**
 * Represents a single hook diagnostic entry.
 */
data class HookDiagnosticRecord(
    val registrationId: String,
    val message: String,
    val level: String,
    val timestamp: Long
)
