package com.example.framework.bridge

import com.example.framework.common.Logger
import com.example.framework.service.ServiceConnector

/**
 * Connects the framework bridge layer to the service connector.
 */
class Bridge {
    private val logger = Logger("Bridge")

    /**
     * Establishes a bridge connection and sends the initial command.
     */
    fun connect() {
        logger.info("Bridge connected")
        ServiceConnector.sendCommand("bridge.connect")
    }
}
