package dev.knoxy.rynox.module

import dev.knoxy.rynox.Rynox
import dev.knoxy.rynox.event.Event
import dev.knoxy.rynox.module.modules.Combat.KillAura
import dev.knoxy.rynox.module.modules.Movement.Fly
import dev.knoxy.rynox.module.modules.Player.AutoSprint
import dev.knoxy.rynox.module.modules.Render.ESP
import dev.knoxy.rynox.util.ConfigManager
import net.minecraft.client.MinecraftClient

class ModuleManager {
  val modules = mutableListOf<Module>()
  private val configManager: ConfigManager = Rynox.configManager

  fun init() {
    modules.addAll(listOf(
      KillAura(),
      Fly(),
      ESP(),
      AutoSprint()
    ))
    loadFromConfig()
  }

  fun getModule(name: String): Module? = modules.find {
    it.name.equals(name, ignoreCase = true)
  }

  fun getModulesByCategory(category: Category): List<Module> =
    modules.filter { it.category == category }

  fun onTick(client: MinecraftClient) {
    modules.forEach { if (it.enabled) it.onTick(client) }
  }

  private fun loadFromConfig() {
    modules.forEach { module ->
      val saved = configManager.getModuleState(module.name)
      if (saved != null) {
        module.enabled = saved
        if (module.enabled) module.onEnable()
      }
    }
  }

  fun saveToConfig() {
    modules.forEach { configManager.setModuleState(it.name, it.enabled) }
  }

  fun onEvent(event: Event) {}
}