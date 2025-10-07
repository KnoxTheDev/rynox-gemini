package dev.knoxy.rynox.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import net.fabricmc.loader.api.FabricLoader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.nio.file.Path

class ConfigManager {
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    // Correctly resolve the path and convert to a File
    private val configFile: File = FabricLoader.getInstance().configDir.resolve("rynox.json").toFile()
    private val moduleStates = mutableMapOf<String, Boolean>()

    fun init() {
        configFile.parentFile.mkdirs()
        if (!configFile.exists()) configFile.createNewFile()
    }

    fun save() {
        val data = mapOf("modules" to moduleStates)
        FileWriter(configFile).use { writer ->
            gson.toJson(data, writer)
        }
    }

    fun load() {
        if (!configFile.exists()) return
        FileReader(configFile).use { reader ->
            val type = object : TypeToken<Map<String, Any>>() {}.type
            val data: Map<String, Any> = gson.fromJson(reader, type) ?: return
            @Suppress("UNCHECKED_CAST")
            moduleStates.putAll((data["modules"] as? Map<String, Boolean>) ?: emptyMap())
        }
    }

    fun getModuleState(name: String): Boolean? = moduleStates[name]

    fun setModuleState(name: String, enabled: Boolean) {
        moduleStates[name] = enabled
    }
}