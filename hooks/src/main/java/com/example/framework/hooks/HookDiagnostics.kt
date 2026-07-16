package com.example.framework.hooks

import java.util.concurrent.ConcurrentHashMap

class HookDiagnostics {
    private val records = ConcurrentHashMap<String, HookDiagnosticRecord>()

    fun record(registration: HookRegistration, message: String, level: String = "INFO") {
        records[registration.id] = HookDiagnosticRecord(
            registrationId = registration.id,
            message = message,
            level = level
        )
    }

    fun snapshot(): List<HookDiagnosticRecord> = records.values.toList()

    fun clear() {
        records.clear()
    }
}

data class HookDiagnosticRecord(
    val registrationId: String,
    val message: String,
    val level: String
)
