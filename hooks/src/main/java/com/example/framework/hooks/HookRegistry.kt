package com.example.framework.hooks

import java.util.concurrent.ConcurrentHashMap

class HookRegistry {
    private val registry = ConcurrentHashMap<String, HookRegistration>()

    fun add(registration: HookRegistration): HookRegistration {
        registry[registration.id] = registration
        return registration
    }

    fun remove(registration: HookRegistration): Boolean = registry.remove(registration.id) != null

    fun get(id: String): HookRegistration? = registry[id]

    fun list(): List<HookRegistration> = registry.values.toList()

    fun clear() {
        registry.clear()
    }
}
