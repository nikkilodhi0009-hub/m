package com.example.framework.common

/**
 * Describes a module that can be loaded by the framework.
 */
data class ModuleMetadata(
    val name: String,
    val version: String,
    val description: String,
    val author: String
)
