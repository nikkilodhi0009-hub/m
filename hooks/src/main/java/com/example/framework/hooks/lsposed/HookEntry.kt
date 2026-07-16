package com.example.framework.hooks.lsposed

import com.example.framework.common.Logger
import com.example.framework.hooks.HookEngine
import com.example.framework.hooks.ReflectionHelper
import java.io.File

class HookEntry {
    private val logger = Logger("HookEntry")

    fun initZygote(modulePath: String?) {
        logger.info("HookEntry initialized", mapOf("modulePath" to (modulePath ?: "unknown")))
        initializeRuntime()
    }

    fun handleLoadPackage(packageName: String, processName: String, classLoader: ClassLoader?) {
        if (packageName != "system") {
            return
        }

        logger.info("Loaded system_server package", mapOf("processName" to processName))
        initializeRuntime()

        val targetClasses = listOf(
            "com.android.server.am.ActivityManagerService",
            "com.android.server.am.UserController",
            "com.android.server.pm.UserManagerService",
            "com.android.server.pm.PackageManagerService"
        )

        targetClasses.forEach { className ->
            try {
                val clazz = classLoader?.loadClass(className) ?: Class.forName(className)
                logger.info("Framework class discovered", mapOf("class" to clazz.name))
                val methods = listOf(
                    "startUser",
                    "startUserInBackground",
                    "startUserInForeground",
                    "stopUser",
                    "stopUserWithDelayedLocking",
                    "finishUserStopped",
                    "dispatchUserSwitch",
                    "dispatchUserStarted",
                    "dispatchUserStopped",
                    "forceStopPackage",
                    "killPackageProcesses",
                    "killApplication",
                    "killUid",
                    "killBackgroundProcesses"
                )
                methods.forEach { methodName ->
                    val method = ReflectionHelper.findMethod(clazz, methodName)
                    if (method != null) {
                        logger.info("Framework method discovered", mapOf("class" to className, "method" to methodName))
                    }
                }
            } catch (t: Throwable) {
                logger.info("Could not load framework class", mapOf("class" to className, "error" to (t.message ?: "unknown")))
            }
        }
    }

    private fun initializeRuntime() {
        val baseDir = File("/data/local/tmp/framework")
        baseDir.mkdirs()
        invokeInitializer("com.example.framework.core.FrameworkCore", baseDir)
        invokeInitializer("com.example.framework.manager.ModuleManager", baseDir)
        invokeInitializer("com.example.framework.service.ServiceConnector", baseDir)
        HookEngine.installFrameworkHooks()

        val reportFile = File(baseDir, "hook-installation-report.log")
        reportFile.writeText(
            "Hook installation report\n" +
                "backend=runtime-hook-backend\n" +
                "targets=com.android.server.am.ActivityManagerService,com.android.server.am.UserController,com.android.server.pm.UserManagerService,com.android.server.pm.PackageManagerService\n" +
                "mode=OBSERVE\n"
        )
    }

    private fun invokeInitializer(className: String, baseDir: File) {
        try {
            val clazz = Class.forName(className)
            val method = clazz.methods.firstOrNull { it.name == "initialize" && it.parameterCount == 1 }
            method?.invoke(null, baseDir)
        } catch (t: Throwable) {
            logger.info("Could not initialize framework component", mapOf("class" to className, "error" to (t.message ?: "unknown")))
        }
    }
}
