package com.example.framework.hooks.lsposed

import com.example.framework.common.Logger

class HookEntry {
    private val logger = Logger("HookEntry")

    fun initZygote(modulePath: String?) {
        logger.info("HookEntry initialized", mapOf("modulePath" to (modulePath ?: "unknown")))
    }

    fun handleLoadPackage(packageName: String, processName: String, classLoader: ClassLoader?) {
        if (packageName != "system") {
            return
        }

        logger.info("Loaded system_server package", mapOf("processName" to processName))

        try {
            val clazz = classLoader?.loadClass("com.android.server.am.ActivityManagerService")
            logger.info("ActivityManagerService loaded", mapOf("class" to (clazz?.name ?: "unknown")))
        } catch (t: Throwable) {
            logger.info("Could not load ActivityManagerService", mapOf("error" to t.message.orEmpty()))
        }

        try {
            val clazz = classLoader?.loadClass("com.android.server.pm.UserManagerService")
            logger.info("UserManagerService loaded", mapOf("class" to (clazz?.name ?: "unknown")))
        } catch (t: Throwable) {
            logger.info("Could not load UserManagerService", mapOf("error" to t.message.orEmpty()))
        }
    }
}
