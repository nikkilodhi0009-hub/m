package com.example.hookframework

import android.app.Application
import com.example.framework.core.FrameworkCore
import java.io.File

class HookApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val baseDir = File(filesDir, "framework")
        baseDir.mkdirs()
        FrameworkCore.initialize(baseDir)
        FrameworkCore.logUserLifecycleEvent(
            "USER STOP DETECTED",
            mapOf(
                "Time" to System.currentTimeMillis(),
                "Target User" to 0,
                "Current Foreground User" to 0,
                "Calling UID" to "unknown",
                "Calling PID" to "unknown",
                "Calling Package" to "unknown",
                "Calling Process" to "unknown",
                "Method" to "FrameworkCore.initialize",
                "Reason" to "bootstrap"
            )
        )
    }
}
