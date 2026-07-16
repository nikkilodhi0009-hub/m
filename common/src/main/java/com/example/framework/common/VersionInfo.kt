package com.example.framework.common

/**
 * Captures the framework version and target platform metadata.
 */
data class VersionInfo(
    val name: String,
    val version: String,
    val targetPlatform: String
)
