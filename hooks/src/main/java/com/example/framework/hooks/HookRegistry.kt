package com.example.framework.hooks

import java.util.concurrent.ConcurrentHashMap

/**
 * Maintains a thread-safe registry of hook registrations.
 */
class HookRegistry {
    private val registry = ConcurrentHashMap<String, HookRegistration>()

    /**
     * Registers a hook with the internal map.
     */
    fun add(registration: HookRegistration): HookRegistration {
        registry[registration.id] = registration
        return registration
    }

    /**
     * Removes a hook from the registry.
     */
    fun remove(registration: HookRegistration): Boolean = registry.remove(registration.id) != null

    /**
     * Returns a hook by its registration id.
     */
    fun get(id: String): HookRegistration? = registry[id]

    /**
     * Returns a snapshot of the registered hooks.
     */
    fun snapshot(): List<HookRegistration> = registry.values.toList()

    /**
     * Returns the current size of the registry.
     */
    fun size(): Int = registry.size

    /**
     * Returns whether the registry contains a hook with the supplied id.
     */
    fun contains(id: String): Boolean = registry.containsKey(id)

    /**
     * Clears the registry.
     */
    fun clear() {
        registry.clear()
    }
}
