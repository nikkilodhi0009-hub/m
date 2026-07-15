package com.example.framework.hooks.lifecycle

import com.example.framework.common.Logger
import java.util.Date

class UserLifecycleMonitor {
    private val logger = Logger("UserLifecycleMonitor")
    private val timeline = mutableListOf<UserLifecycleEvent>()

    fun onUserStart(event: UserLifecycleEvent) {
        timeline.add(event)
        logger.info(
            "User start detected",
            mapOf(
                "userId" to event.targetUserId,
                "stateBefore" to (event.stateBefore ?: "unknown"),
                "stateAfter" to (event.stateAfter ?: "unknown")
            )
        )
    }

    fun onUserStop(event: UserLifecycleEvent) {
        timeline.add(event)
        logger.info(
            "User stop detected",
            mapOf(
                "userId" to event.targetUserId,
                "reason" to (event.reason ?: "unknown")
            )
        )
    }

    fun onUserStateTransition(event: UserLifecycleEvent) {
        timeline.add(event)
    }

    fun snapshot(): List<UserLifecycleEvent> = timeline.toList()

    fun buildTimeline(userId: Int): List<UserLifecycleEvent> = timeline.filter { it.targetUserId == userId }
}
