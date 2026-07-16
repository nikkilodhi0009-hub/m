package com.example.framework.hooks

interface HookBackend {
    fun name(): String

    fun installMethodHook(registration: HookRegistration): HookInstallationResult

    fun installConstructorHook(registration: HookRegistration): HookInstallationResult

    fun installReplacementHook(registration: HookRegistration): HookInstallationResult

    fun removeHook(registration: HookRegistration): Boolean

    fun verifyHook(registration: HookRegistration): HookInstallationResult
}

data class HookInstallationResult(
    val className: String,
    val methodName: String?,
    val signature: String?,
    val backendName: String,
    val installed: Boolean,
    val reason: String = "ok"
)
