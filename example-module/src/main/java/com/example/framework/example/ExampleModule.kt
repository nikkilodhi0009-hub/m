package com.example.framework.example

import com.example.framework.api.FrameworkApi
import com.example.framework.common.Logger

/**
 * Example module implementation that registers lifecycle and hook callbacks.
 */
class ExampleModule(private val api: FrameworkApi) {
    private val logger = Logger("ExampleModule")

    /**
     * Registers the module's example hook and lifecycle callbacks.
     */
    fun initialize() {
        api.registerHook("user-stop-monitor", callback = {
            logger.info("Example module hook invoked")
        })

        api.registerLifecycleCallback("module-ready") {
            logger.info("Example module lifecycle callback invoked")
        }
    }
}
