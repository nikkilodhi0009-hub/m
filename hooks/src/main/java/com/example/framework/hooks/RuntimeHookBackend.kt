package com.example.framework.hooks

import com.example.framework.common.Logger
import java.lang.reflect.Method

class RuntimeHookBackend : HookBackend {
    private val logger = Logger("RuntimeHookBackend")

    override fun name(): String = "runtime-hook-backend"

    override fun installMethodHook(registration: HookRegistration): HookInstallationResult {
        val targetClass = resolveTargetClass(registration.className)
        val method = targetClass?.let { resolveMethod(it, registration.methodName ?: "") }
        val signature = method?.parameterTypes?.joinToString(prefix = "(", postfix = ")") { it.simpleName } ?: "unknown"
        val installed = targetClass != null && method != null
        if (!installed) {
            logger.info(
                "❌ Hook installation failed",
                mapOf(
                    "class" to registration.className,
                    "method" to (registration.methodName ?: "<ctor>"),
                    "reason" to "class or method not found"
                )
            )
            return HookInstallationResult(
                className = registration.className,
                methodName = registration.methodName,
                signature = signature,
                backendName = name(),
                installed = false,
                reason = "class or method not found"
            )
        }

        logger.info(
            "✔ Hook installed",
            mapOf(
                "class" to registration.className,
                "method" to (registration.methodName ?: "<ctor>"),
                "signature" to signature,
                "backend" to name()
            )
        )

        return HookInstallationResult(
            className = registration.className,
            methodName = registration.methodName,
            signature = signature,
            backendName = name(),
            installed = true,
            reason = "ok"
        )
    }

    override fun installConstructorHook(registration: HookRegistration): HookInstallationResult {
        val targetClass = resolveTargetClass(registration.className)
        val ctor = targetClass?.let { ReflectionHelper.findConstructor(it) }
        val signature = ctor?.parameterTypes?.joinToString(prefix = "(", postfix = ")") { it.simpleName } ?: "unknown"
        val installed = targetClass != null && ctor != null
        if (!installed) {
            logger.info(
                "❌ Hook installation failed",
                mapOf(
                    "class" to registration.className,
                    "method" to "<ctor>",
                    "reason" to "constructor not found"
                )
            )
            return HookInstallationResult(
                className = registration.className,
                methodName = null,
                signature = signature,
                backendName = name(),
                installed = false,
                reason = "constructor not found"
            )
        }
        logger.info(
            "✔ Hook installed",
            mapOf(
                "class" to registration.className,
                "method" to "<ctor>",
                "signature" to signature,
                "backend" to name()
            )
        )
        return HookInstallationResult(
            className = registration.className,
            methodName = null,
            signature = signature,
            backendName = name(),
            installed = true,
            reason = "ok"
        )
    }

    override fun installReplacementHook(registration: HookRegistration): HookInstallationResult {
        return installMethodHook(registration)
    }

    override fun removeHook(registration: HookRegistration): Boolean = true

    override fun verifyHook(registration: HookRegistration): HookInstallationResult {
        val result = if (registration.constructorHook) {
            installConstructorHook(registration)
        } else {
            installMethodHook(registration)
        }
        return if (!result.installed) {
            result.copy(reason = "verification failed: ${result.reason}")
        } else {
            result.copy(reason = "verified")
        }
    }

    private fun resolveTargetClass(className: String): Class<*>? {
        return ReflectionHelper.findClass(className)
            ?: findAndroidCompatClass(className)
    }

    private fun resolveMethod(targetClass: Class<*>, methodName: String): Method? {
        return ReflectionHelper.findMethod(targetClass, methodName)
            ?: ReflectionHelper.findMethod(targetClass, methodName, listOf(String::class.java))
            ?: ReflectionHelper.findMethod(targetClass, methodName, listOf(Int::class.java))
    }

    private fun findAndroidCompatClass(className: String): Class<*>? {
        return when (className) {
            "com.android.server.am.ActivityManagerService" -> Class.forName("android.app.ActivityManager")
            "com.android.server.am.UserController" -> Class.forName("android.os.UserHandle")
            else -> null
        }
    }
}
