package com.example.framework.service

import java.io.File
import com.example.framework.common.Logger

object ServiceConnector {
    private val logger = Logger("ServiceConnector")

    fun initialize(baseDir: File) {
        logger.info("Service connector initialized", mapOf("baseDir" to baseDir.absolutePath))
    }

    fun sendCommand(command: String, payload: Map<String, Any> = emptyMap()) {
        logger.info("Service command sent", mapOf("command" to command, "payload" to payload))
    }
}
