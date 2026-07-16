package com.example.framework.bridge

import com.example.framework.common.Logger
import com.example.framework.service.ServiceConnector

class Bridge {
    private val logger = Logger("Bridge")

    fun connect() {
        logger.info("Bridge connected")
        ServiceConnector.sendCommand("bridge.connect")
    }
}
