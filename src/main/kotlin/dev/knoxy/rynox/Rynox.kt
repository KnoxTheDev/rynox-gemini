package dev.knoxy.rynox

import dev.knoxy.rynox.event.EventBus
import dev.knoxy.rynox.gui.ClickGui
import dev.knoxy.rynox.module.ModuleManager
import dev.knoxy.rynox.util.ConfigManager
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW

object Rynox : ClientModInitializer {
  val eventBus = EventBus()
  val moduleManager = ModuleManager()
  val configManager = ConfigManager()

  private lateinit var guiKey: KeyBinding

  override fun onInitializeClient() {
    moduleManager.init()
    configManager.init()
    eventBus.register(moduleManager)

    ClientTickEvents.END_CLIENT_TICK.register { client ->
      moduleManager.onTick(client)
      eventBus.post(event.TickEvent.End(client))
    }

    guiKey = KeyBindingHelper.registerKeyBinding(
      KeyBinding(
        "key.rynox.gui",
        InputUtil.Type.KEYSYM,
        GLFW.GLFW_KEY_RIGHT_SHIFT,
        "category.rynox"
      )
    )

    ClientTickEvents.END_CLIENT_TICK.register { client ->
      while (guiKey.wasPressed()) {
        client.setScreen(ClickGui())
      }
    }

    ClientTickEvents.START_WORLD_TICK.register { world ->
      configManager.load()
    }

    ClientLifecycleEvents.CLIENT_STOPPING.register { client ->
      configManager.save()
    }
  }
}