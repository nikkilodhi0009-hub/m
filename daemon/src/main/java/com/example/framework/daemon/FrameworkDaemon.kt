package com.example.framework.daemon

import com.example.framework.common.Logger

/**
 * Lightweight daemon wrapper for framework startup.
 */
class FrameworkDaemon {
    private val logger = Logger("FrameworkDaemon")

    /**
     * Starts the framework daemon.
     */
    fun start() {
        logger.info("Daemon started")
    }
}
