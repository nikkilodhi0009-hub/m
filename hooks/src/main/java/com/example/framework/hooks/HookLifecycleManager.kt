package com.example.framework.hooks

import java.util.concurrent.ConcurrentHashMap

/**
 * Lifecycle states that a hook can transition through.
 */
enum class HookState {
    PENDING,
    INSTALLED,
    FAILED,
    ROLLED_BACK
}

/**
 * Tracks the lifecycle state of a hook registration.
 */
data class HookLifecycleEntry(
    val registration: HookRegistration,
    var state: HookState = HookState.PENDING,
    var lastError: String? = null,
    var installCount: Int = 0
)

/**
 * Maintains strict, thread-safe lifecycle state for hook registrations.
 */
class HookLifecycleManager {
    private val entries = ConcurrentHashMap<String, HookLifecycleEntry>()

    /**
     * Registers a new hook lifecycle entry in the initial pending state.
     */
    fun register(registration: HookRegistration): HookLifecycleEntry {
        val entry = HookLifecycleEntry(registration)
        entries[registration.id] = entry
        return entry
    }

    /**
     * Returns the lifecycle entry for a registration, if present.
     */
    fun getEntry(registration: HookRegistration): HookLifecycleEntry? = entries[registration.id]

    /**
     * Marks a registration as installed.
     */
    fun enable(registration: HookRegistration): Boolean {
        return transition(registration.id, HookState.INSTALLED) { entry ->
            entry.state = HookState.INSTALLED
            entry.lastError = null
        }
    }

    /**
     * Marks a registration as failed.
     */
    fun disable(registration: HookRegistration): Boolean {
        return transition(registration.id, HookState.FAILED) { entry ->
            entry.state = HookState.FAILED
        }
    }

    /**
     * Marks a registration as installed and increments the installation counter.
     */
    fun markInstalled(registration: HookRegistration): Boolean {
        return transition(registration.id, HookState.INSTALLED) { entry ->
            entry.state = HookState.INSTALLED
            entry.installCount += 1
            entry.lastError = null
        }
    }

    /**
     * Records a lifecycle failure with the supplied reason.
     */
    fun markFailed(registration: HookRegistration, reason: String): Boolean {
        return transition(registration.id, HookState.FAILED) { entry ->
            entry.state = HookState.FAILED
            entry.lastError = reason
        }
    }

    /**
     * Rolls back a registration to a rolled-back state.
     */
    fun rollback(registration: HookRegistration): Boolean {
        return transition(registration.id, HookState.ROLLED_BACK) { entry ->
            entry.state = HookState.ROLLED_BACK
        }
    }

    /**
     * Removes a registration from the lifecycle manager.
     */
    fun remove(registration: HookRegistration): Boolean {
        return entries.remove(registration.id) != null
    }

    /**
     * Returns a snapshot of the current lifecycle entries.
     */
    fun snapshot(): List<HookLifecycleEntry> = entries.values.map { entry ->
        entry.copy(state = entry.state, lastError = entry.lastError, installCount = entry.installCount)
    }

    /**
     * Clears all lifecycle entries.
     */
    fun clear() {
        entries.clear()
    }

    private fun transition(id: String, targetState: HookState, mutator: (HookLifecycleEntry) -> Unit): Boolean {
        val entry = entries[id] ?: return false
        synchronized(entry) {
            validateTransition(entry.state, targetState)
            entry.state = targetState
            mutator(entry)
        }
        return true
    }

    private fun validateTransition(from: HookState, to: HookState) {
        val allowed = when (from) {
            HookState.PENDING -> setOf(HookState.INSTALLED, HookState.FAILED, HookState.ROLLED_BACK)
            HookState.INSTALLED -> setOf(HookState.INSTALLED, HookState.FAILED, HookState.ROLLED_BACK)
            HookState.FAILED -> setOf(HookState.FAILED, HookState.INSTALLED, HookState.ROLLED_BACK)
            HookState.ROLLED_BACK -> setOf(HookState.ROLLED_BACK)
        }
        require(to in allowed) {
            "Invalid lifecycle transition from $from to $to"
        }
    }
}
