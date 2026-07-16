package com.example.framework.service

import java.io.File
import com.example.framework.common.Logger

/**
 * Provides a thin interface for sending framework service commands.
 */
object ServiceConnector {
    private val logger = Logger("ServiceConnector")

    /**
     * Initializes the service connector for the supplied base directory.
     */
    fun initialize(baseDir: File) {
        logger.info("Service connector initialized", mapOf("baseDir" to baseDir.absolutePath))
    }

    /**
     * Sends a command to the service layer.
     */
    fun sendCommand(command: String, payload: Map<String, Any> = emptyMap()) {
        logger.info("Service command sent", mapOf("command" to command, "payload" to payload))
    }
}
