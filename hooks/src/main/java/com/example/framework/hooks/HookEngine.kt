package com.example.framework.hooks

import android.os.Binder
import android.os.Process
import com.example.framework.common.ConfigLoader
import com.example.framework.common.HookMode
import com.example.framework.common.LifecycleLogWriter
import com.example.framework.common.Logger
import com.example.framework.hooks.lifecycle.UserLifecycleEvent
import com.example.framework.hooks.lifecycle.UserLifecycleMonitor
import java.io.File
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Coordinates hook registration, installation, and lifecycle diagnostics.
 */
object HookEngine {
    private val logger = Logger("HookEngine")
    private val monitor = UserLifecycleMonitor()
    private val logWriter = LifecycleLogWriter("/data/local/tmp/framework.log")
    private lateinit var baseDir: File
    private val registrationStore = HookRegistrationStore()
    private val installedTargets = mutableSetOf<String>()
    private var policyMode = HookMode.OBSERVE
    private val backend: HookBackend = RuntimeHookBackend()

    fun initialize(baseDir: File) {
        this.baseDir = baseDir
        baseDir.mkdirs()
        ConfigLoader.loadDefaultConfigs(baseDir)
        logger.info("Hook engine initialized", mapOf("baseDir" to baseDir.absolutePath))
        try {
            installFrameworkHooks()
        } catch (throwable: Throwable) {
            logger.info("Hook initialization failed", mapOf("error" to (throwable.message ?: "unknown")))
        }
    }

    fun registerJavaHook(className: String, methodName: String, callback: HookCallback): HookRegistration {
        val registration = HookRegistration(
            id = "java:${className}:${methodName}:${System.identityHashCode(callback)}",
            className = className,
            methodName = methodName,
            callback = callback
        )
        registrationStore.register(registration)
        logger.info(
            "Registered Java hook",
            mapOf("class" to className, "method" to methodName)
        )
        return registration
    }

    fun registerConstructorHook(className: String, callback: HookCallback): HookRegistration {
        val registration = HookRegistration(
            id = "ctor:${className}:${System.identityHashCode(callback)}",
            className = className,
            callback = callback,
            constructorHook = true
        )
        registrationStore.register(registration)
        logger.info("Registered constructor hook", mapOf("class" to className))
        return registration
    }

    fun registerReplacementHook(className: String, methodName: String, callback: HookCallback): HookRegistration {
        val registration = HookRegistration(
            id = "replace:${className}:${methodName}:${System.identityHashCode(callback)}",
            className = className,
            methodName = methodName,
            callback = callback,
            replacementHook = true
        )
        registrationStore.register(registration)
        logger.info("Registered replacement hook", mapOf("class" to className, "method" to methodName))
        return registration
    }

    fun removeHook(registration: HookRegistration) {
        registrationStore.remove(registration)
    }

    fun installFrameworkHooks() {
        val targets = listOf(
            HookTarget(
                classNames = listOf(
                    "com.android.server.am.ActivityManagerService",
                    "com.android.server.am.UserController"
                ),
                methodNames = listOf(
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
            ),
            HookTarget(
                classNames = listOf(
                    "com.android.server.pm.UserManagerService",
                    "com.android.server.am.UserController"
                ),
                methodNames = listOf(
                    "startUser",
                    "stopUser",
                    "finishUserStopped"
                )
            ),
            HookTarget(
                classNames = listOf(
                    "com.android.server.pm.PackageManagerService"
                ),
                methodNames = listOf(
                    "getPackageInfo",
                    "getPackageUid",
                    "deletePackage"
                )
            )
        )

        targets.forEach { target ->
            target.classNames.forEach { className ->
                target.methodNames.forEach { methodName ->
                    val registrationId = "${className}:${methodName}"
                    if (installedTargets.contains(registrationId)) {
                        return@forEach
                    }

                    val registration = registerJavaHook(className, methodName, object : HookCallback {
                        override fun before(args: Array<Any?>): Boolean {
                            return true
                        }

                        override fun after(result: Any?) {
                            dispatchHookEvent(className, methodName, args = emptyArray(), result = result)
                        }
                    })

                    val result = backend.installMethodHook(registration)
                    val verify = backend.verifyHook(registration)
                    if (result.installed && verify.installed) {
                        registrationStore.getLifecycleManager().enable(registration)
                        registrationStore.getLifecycleManager().markInstalled(registration)
                        registrationStore.getDiagnostics().record(registration, "installed and verified")
                    } else {
                        registrationStore.getLifecycleManager().disable(registration)
                        registrationStore.getLifecycleManager().markFailed(registration, verify.reason)
                        if (result.installed) {
                            registrationStore.getLifecycleManager().rollback(registration)
                        }
                        registrationStore.getDiagnostics().record(registration, "installation failed: ${verify.reason}", "ERROR")
                    }
                    installedTargets.add(registrationId)
                    logger.info(
                        "Hook backend report",
                        mapOf(
                            "class" to className,
                            "method" to methodName,
                            "installed" to result.installed,
                            "verified" to verify.installed,
                            "backend" to backend.name()
                        )
                    )
                }
            }
        }
    }

    fun refreshConfiguration(baseDir: File) {
        this.baseDir = baseDir
        ConfigLoader.loadDefaultConfigs(baseDir)
        logger.info("Hook configuration reloaded", mapOf("baseDir" to baseDir.absolutePath))
    }

    fun healthCheck(): Map<String, Any> {
        val lifecycleManager = registrationStore.getLifecycleManager()
        val active = lifecycleManager.snapshot().count { it.state == HookState.INSTALLED }
        return mapOf(
            "activeHooks" to active,
            "registeredHooks" to registrationStore.size(),
            "diagnosticCount" to registrationStore.getDiagnostics().snapshot().size,
            "backend" to backend.name()
        )
    }

    fun invokeHookedMethod(target: Any?, methodName: String, args: Array<Any?> = emptyArray()): Any? {
        if (target == null) {
            return null
        }

        val method = ReflectionHelper.findMethod(target.javaClass, methodName)
            ?: return null

        val matchingRegistrations = registrationStore.snapshot().filter {
            it.methodName == methodName && (
                it.className == target.javaClass.name ||
                    it.className == target.javaClass.canonicalName ||
                    it.className.endsWith(".${target.javaClass.simpleName}")
                ) && registrationStore.getLifecycleManager().getEntry(it)?.state == HookState.INSTALLED
        }

        matchingRegistrations.forEach { registration ->
            registration.callback.before(args)
        }

        return try {
            val result = method.invoke(target, *args)
            matchingRegistrations.forEach { registration ->
                registration.callback.after(result)
            }
            dispatchHookEvent(target.javaClass.name, methodName, args, result)
            result
        } catch (throwable: Throwable) {
            matchingRegistrations.forEach { registration ->
                registration.callback.after(null)
            }
            dispatchHookEvent(target.javaClass.name, methodName, args, null, throwable)
            throw throwable
        }
    }

    fun dispatchRuntimeHook(className: String, methodName: String, args: Array<Any?> = emptyArray(), result: Any? = null, throwable: Throwable? = null) {
        val matchingRegistrations = registrationStore.snapshot().filter {
            it.methodName == methodName && (
                it.className == className ||
                    it.className.endsWith(".$className")
                ) && registrationStore.getLifecycleManager().getEntry(it)?.state == HookState.INSTALLED
        }

        matchingRegistrations.forEach { registration ->
            registration.callback.before(args)
            registration.callback.after(result)
        }
        dispatchHookEvent(className, methodName, args, result, throwable)
    }

    fun invokeConstructorHook(className: String, args: Array<Any?> = emptyArray()): Any? {
        return try {
            val targetClass = ReflectionHelper.findClass(className) ?: return null
            val constructor = ReflectionHelper.findConstructor(targetClass, args.mapNotNull { it?.javaClass }) ?: return null
            constructor.newInstance(*args)
        } catch (throwable: Throwable) {
            logger.info("Constructor hook failed", mapOf("class" to className, "error" to (throwable.message ?: "unknown")))
            null
        }
    }

    fun getMonitor(): UserLifecycleMonitor = monitor

    fun getPolicyMode(): HookMode = policyMode

    fun setPolicyMode(mode: HookMode) {
        policyMode = mode
    }

    private fun dispatchHookEvent(
        className: String,
        methodName: String,
        args: Array<Any?>,
        result: Any?,
        throwable: Throwable? = null
    ) {
        val targetUserId = extractTargetUserId(args)
        val event = UserLifecycleEvent(
            eventType = "FRAMEWORK_HOOK",
            targetUserId = targetUserId,
            timestamp = Date(),
            callerUid = collectCallerUid(),
            callerPid = collectCallerPid(),
            callerPackage = collectCallingPackage(),
            callerProcess = collectCallingProcess(),
            binderCaller = collectBinderCaller(),
            reason = policyMode.name,
            method = methodName,
            stackTrace = collectStackTrace(throwable),
            stateBefore = className,
            stateAfter = result?.javaClass?.simpleName ?: "void",
            details = hashMapOf<String, Any>().apply {
                put("className", className)
                put("arguments", args.map { it?.toString() ?: "null" })
                put("result", result?.toString() ?: "null")
                put("thread", Thread.currentThread().name)
                put("pid", Process.myPid())
                put("policyMode", policyMode.name)
            }
        )

        monitor.record(event)
        logWriter.appendEvent(
            "USER EVENT",
            hashMapOf<String, Any?>().apply {
                put("Method", methodName)
                put("Target User", targetUserId)
                put("Caller UID", event.callerUid ?: "unknown")
                put("Caller PID", event.callerPid ?: "unknown")
                put("Package", event.callerPackage ?: "unknown")
                put("Process", event.callerProcess ?: "unknown")
                put("Arguments", args.joinToString(prefix = "[", postfix = "]") { it?.toString() ?: "null" })
                put("Stack Trace", event.stackTrace ?: "n/a")
                put("Result", result?.toString() ?: "null")
                put("Execution Time", nowTimestamp())
                put("Exception", throwable?.message ?: "none")
            }
        )
    }

    private fun extractTargetUserId(args: Array<Any?>): Int {
        return args.firstOrNull { it is Int } as? Int ?: 0
    }

    private fun collectCallerUid(): Int? {
        return try {
            Binder.getCallingUid()
        } catch (_: Throwable) {
            null
        }
    }

    private fun collectCallerPid(): Int? {
        return try {
            Binder.getCallingPid()
        } catch (_: Throwable) {
            null
        }
    }

    private fun collectCallingPackage(): String? {
        return try {
            val uid = collectCallerUid() ?: return null
            val activityThreadClass = Class.forName("android.app.ActivityThread")
            val currentApplicationMethod = activityThreadClass.getMethod("currentApplication")
            val application = currentApplicationMethod.invoke(null)
            val context = application as? android.content.Context
            val packageManager = context?.packageManager
            packageManager?.getPackagesForUid(uid)?.firstOrNull() ?: "uid:$uid"
        } catch (_: Throwable) {
            null
        }
    }

    private fun collectCallingProcess(): String? {
        return try {
            android.os.Process.myProcessName()
        } catch (_: Throwable) {
            "unknown"
        }
    }

    private fun collectBinderCaller(): String? {
        return try {
            val uid = collectCallerUid() ?: return null
            "uid:$uid"
        } catch (_: Throwable) {
            null
        }
    }

    private fun collectStackTrace(throwable: Throwable?): String? {
        if (throwable != null) {
            return throwable.stackTraceToString()
        }
        return Thread.currentThread().stackTrace
            .joinToString(separator = System.lineSeparator()) { it.toString() }
    }

    private fun nowTimestamp(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)
        return formatter.format(Date())
    }
}

interface HookCallback {
    fun before(args: Array<Any?> = emptyArray()): Boolean
    fun after(result: Any? = null): Unit
}

data class HookRegistration(
    val id: String,
    val className: String,
    val methodName: String? = null,
    val callback: HookCallback,
    val constructorHook: Boolean = false,
    val replacementHook: Boolean = false
)

private data class HookTarget(
    val classNames: List<String>,
    val methodNames: List<String>
)
