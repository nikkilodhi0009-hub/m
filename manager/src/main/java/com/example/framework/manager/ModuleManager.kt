package com.example.framework.manager

import java.io.File
import com.example.framework.common.Logger
import com.example.framework.common.ModuleMetadata

object ModuleManager {
    private val logger = Logger("ModuleManager")
    private val modules = mutableListOf<ModuleMetadata>()

    fun initialize(baseDir: File) {
        logger.info("Module manager initialized", mapOf("baseDir" to baseDir.absolutePath))
        modules.add(ModuleMetadata("example-module", "1.0.0", "Example module", "Example Team"))
    }

    fun listModules(): List<ModuleMetadata> = modules.toList()

    fun enableModule(name: String) {
        logger.info("Enabled module", mapOf("name" to name))
    }

    fun disableModule(name: String) {
        logger.info("Disabled module", mapOf("name" to name))
    }
}
