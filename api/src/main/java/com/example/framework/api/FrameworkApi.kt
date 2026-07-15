package com.example.framework.api

import com.example.framework.common.Logger
import com.example.framework.common.ModuleMetadata
import java.io.File

class FrameworkApi(private val baseDir: File) {
    private val logger = Logger("FrameworkApi")

    fun registerHook(hookName: String, callback: () -> Unit) {
        logger.info("Registered hook", mapOf("hook" to hookName))
        callback.invoke()
    }

    fun registerLifecycleCallback(callbackName: String, callback: () -> Unit) {
        logger.info("Registered lifecycle callback", mapOf("callback" to callbackName))
        callback.invoke()
    }

    fun log(message: String) {
        logger.info(message)
    }

    fun getModuleMetadata(): ModuleMetadata = ModuleMetadata(
        name = "example-module",
        version = "1.0.0",
        description = "Example module for the Android 10 hook framework",
        author = "Example Team"
    )
}
