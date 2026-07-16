package com.example.framework.manager

import java.io.File
import com.example.framework.common.Logger
import com.example.framework.common.ModuleMetadata

object ModuleManager {
    private val logger = Logger("ModuleManager")
    private val modules = mutableListOf<ModuleMetadata>()
    private val enabledModules = linkedSetOf<String>()

    fun initialize(baseDir: File) {
        logger.info("Module manager initialized", mapOf("baseDir" to baseDir.absolutePath))
        modules.add(ModuleMetadata("example-module", "1.0.0", "Example module", "Example Team"))
        enabledModules.add("example-module")
        discoverModules(baseDir)
    }

    fun listModules(): List<ModuleMetadata> = modules.toList()

    fun enableModule(name: String) {
        enabledModules.add(name)
        logger.info("Enabled module", mapOf("name" to name))
    }

    fun disableModule(name: String) {
        enabledModules.remove(name)
        logger.info("Disabled module", mapOf("name" to name))
    }

    fun listEnabledModules(): List<String> = enabledModules.toList()

    private fun discoverModules(baseDir: File) {
        val moduleDir = File(baseDir, "modules")
        moduleDir.mkdirs()
        moduleDir.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                logger.info("Discovered module directory", mapOf("path" to file.absolutePath))
            }
        }
    }
}
