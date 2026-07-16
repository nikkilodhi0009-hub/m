package com.example.framework.core

import java.io.File
import com.example.framework.api.FrameworkApi
import com.example.framework.common.ConfigLoader
import com.example.framework.common.HookMode
import com.example.framework.common.LifecycleLogWriter
import com.example.framework.common.Logger
import com.example.framework.common.VersionInfo
import com.example.framework.hooks.HookEngine
import com.example.framework.manager.ModuleManager
import com.example.framework.service.ServiceConnector

/**
 * Coordinates initialization and lifecycle logging for the framework core.
 */
object FrameworkCore {
    private lateinit var appContext: File
    private val logger = Logger("FrameworkCore")
    private val versionInfo = VersionInfo("android10-hook-framework", "1.0.0", "Android 10")
    private val logWriter = LifecycleLogWriter()

    /**
     * Initializes the framework core and its dependent subsystems.
     */
    fun initialize(baseDir: File) {
        appContext = baseDir
        ConfigLoader.loadDefaultConfigs(baseDir)
        logger.info("Framework initialized", mapOf("version" to versionInfo.version))
        logWriter.appendEvent(
            "USER STOP DETECTED",
            mapOf(
                "Time" to "initializing",
                "Target User" to "n/a",
                "Mode" to HookMode.OBSERVE.name
            )
        )
        HookEngine.initialize(baseDir)
        ModuleManager.initialize(baseDir)
        ServiceConnector.initialize(baseDir)
    }

    /**
     * Logs an informational message through the framework logger.
     */
    fun logInfo(message: String, metadata: Map<String, Any> = emptyMap()) {
        logger.info(message, metadata)
    }

    /**
     * Writes a user lifecycle event to the framework log.
     */
    fun logUserLifecycleEvent(header: String, metadata: Map<String, Any?>) {
        logWriter.appendEvent(header, metadata)
    }

    /**
     * Returns a framework API instance bound to the initialized context.
     */
    fun getApi(): FrameworkApi = FrameworkApi(appContext)

    /**
     * Returns the current framework version metadata.
     */
    fun getVersionInfo(): VersionInfo = versionInfo
}
