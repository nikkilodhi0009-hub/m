package com.example.framework.hooks

import java.util.concurrent.ConcurrentHashMap

enum class HookState {
    PENDING,
    INSTALLED,
    FAILED,
    ROLLED_BACK
}

data class HookLifecycleEntry(
    val registration: HookRegistration,
    var state: HookState = HookState.PENDING,
    var lastError: String? = null,
    var installCount: Int = 0
)

class HookLifecycleManager {
    private val entries = ConcurrentHashMap<String, HookLifecycleEntry>()

    fun register(registration: HookRegistration): HookLifecycleEntry {
        val entry = HookLifecycleEntry(registration)
        entries[registration.id] = entry
        return entry
    }

    fun getEntry(registration: HookRegistration): HookLifecycleEntry? = entries[registration.id]

    fun enable(registration: HookRegistration) {
        entries[registration.id]?.let { entry ->
            entry.state = HookState.INSTALLED
        }
    }

    fun disable(registration: HookRegistration) {
        entries[registration.id]?.let { entry ->
            entry.state = HookState.FAILED
        }
    }

    fun markInstalled(registration: HookRegistration) {
        entries[registration.id]?.let { entry ->
            entry.state = HookState.INSTALLED
            entry.installCount += 1
            entry.lastError = null
        }
    }

    fun markFailed(registration: HookRegistration, reason: String) {
        entries[registration.id]?.let { entry ->
            entry.state = HookState.FAILED
            entry.lastError = reason
        }
    }

    fun rollback(registration: HookRegistration): Boolean {
        return entries[registration.id]?.let { entry ->
            entry.state = HookState.ROLLED_BACK
            true
        } ?: false
    }

    fun remove(registration: HookRegistration): Boolean {
        return entries.remove(registration.id) != null
    }

    fun snapshot(): List<HookLifecycleEntry> = entries.values.toList()

    fun clear() {
        entries.clear()
    }
}
