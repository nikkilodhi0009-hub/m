package com.example.framework.daemon

import com.example.framework.common.Logger

class FrameworkDaemon {
    private val logger = Logger("FrameworkDaemon")

    fun start() {
        logger.info("Daemon started")
    }
}
