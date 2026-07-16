package com.example.framework.api

import com.example.framework.common.Logger
import com.example.framework.common.ModuleMetadata
import java.io.File

/**
 * Public facade for framework-level interactions used by modules.
 */
class FrameworkApi(private val baseDir: File) {
    private val logger = Logger("FrameworkApi")

    /**
     * Registers a hook callback with the framework.
     */
    fun registerHook(hookName: String, callback: () -> Unit) {
        logger.info("Registered hook", mapOf("hook" to hookName))
        callback.invoke()
    }

    /**
     * Registers a lifecycle callback with the framework.
     */
    fun registerLifecycleCallback(callbackName: String, callback: () -> Unit) {
        logger.info("Registered lifecycle callback", mapOf("callback" to callbackName))
        callback.invoke()
    }

    /**
     * Emits a framework log entry.
     */
    fun log(message: String) {
        logger.info(message)
    }

    /**
     * Returns the module metadata exposed by the example module.
     */
    fun getModuleMetadata(): ModuleMetadata = ModuleMetadata(
        name = "example-module",
        version = "1.0.0",
        description = "Example module for the Android 10 hook framework",
        author = "Example Team"
    )
}
