package com.example.framework.hooks.lifecycle

import java.util.Date

data class UserLifecycleEvent(
    val eventType: String,
    val targetUserId: Int,
    val timestamp: Date,
    val callerUid: Int? = null,
    val callerPid: Int? = null,
    val callerPackage: String? = null,
    val callerProcess: String? = null,
    val binderCaller: String? = null,
    val reason: String? = null,
    val method: String? = null,
    val stackTrace: String? = null,
    val stateBefore: String? = null,
    val stateAfter: String? = null,
    val details: Map<String, Any> = emptyMap()
)
