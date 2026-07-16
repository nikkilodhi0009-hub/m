package com.example.framework.hooks.lifecycle

import com.example.framework.common.Logger

class UserLifecycleMonitor {
    private val logger = Logger("UserLifecycleMonitor")
    private val timeline = mutableListOf<UserLifecycleEvent>()

    fun onUserStart(event: UserLifecycleEvent) {
        record(event)
    }

    fun onUserStop(event: UserLifecycleEvent) {
        record(event)
    }

    fun onUserStateTransition(event: UserLifecycleEvent) {
        record(event)
    }

    fun record(event: UserLifecycleEvent) {
        timeline.add(event)
        logger.info(
            "User lifecycle event recorded",
            mapOf(
                "eventType" to event.eventType,
                "userId" to event.targetUserId,
                "method" to (event.method ?: "unknown"),
                "callerUid" to (event.callerUid ?: "unknown"),
                "callerPid" to (event.callerPid ?: "unknown"),
                "callerPackage" to (event.callerPackage ?: "unknown")
            )
        )
    }

    fun snapshot(): List<UserLifecycleEvent> = timeline.toList()

    fun buildTimeline(userId: Int): List<UserLifecycleEvent> = timeline.filter { it.targetUserId == userId }
}
