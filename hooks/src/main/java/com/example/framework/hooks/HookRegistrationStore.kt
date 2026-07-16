package com.example.framework.hooks

import java.util.Collections

/**
 * Encapsulates hook registrations, lifecycle state, and diagnostics in a thread-safe store.
 */
class HookRegistrationStore {
    private val registrations = Collections.synchronizedList(mutableListOf<HookRegistration>())
    private val registry = HookRegistry()
    private val lifecycleManager = HookLifecycleManager()
    private val diagnostics = HookDiagnostics()

    /**
     * Registers a new hook and updates the supporting registries.
     */
    fun register(registration: HookRegistration): HookRegistration {
        registrations.add(registration)
        registry.add(registration)
        lifecycleManager.register(registration)
        diagnostics.record(registration, "registered")
        return registration
    }

    /**
     * Removes a hook from all internal registries.
     */
    fun remove(registration: HookRegistration) {
        registrations.remove(registration)
        registry.remove(registration)
        lifecycleManager.disable(registration)
        lifecycleManager.remove(registration)
        diagnostics.record(registration, "removed")
    }

    /**
     * Returns the current number of registered hooks.
     */
    fun size(): Int = registrations.size

    /**
     * Returns a snapshot of the registered hooks.
     */
    fun snapshot(): List<HookRegistration> = registrations.toList()

    /**
     * Returns the lifecycle manager used by the store.
     */
    fun getLifecycleManager(): HookLifecycleManager = lifecycleManager

    /**
     * Returns the diagnostics collector used by the store.
     */
    fun getDiagnostics(): HookDiagnostics = diagnostics

    /**
     * Returns the registry used by the store.
     */
    fun getRegistry(): HookRegistry = registry

    /**
     * Clears the contents of the store.
     */
    fun clear() {
        registrations.clear()
        registry.clear()
        lifecycleManager.clear()
        diagnostics.clear()
    }
}
