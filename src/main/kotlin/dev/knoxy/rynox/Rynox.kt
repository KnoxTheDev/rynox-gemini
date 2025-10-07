package dev.knoxy.rynox

import dev.knoxy.rynox.event.EventBus
import dev.knoxy.rynox.event.events.TickEvent
import dev.knoxy.rynox.gui.ClickGui
import dev.knoxy.rynox.module.ModuleManager
import dev.knoxy.rynox.util.ConfigManager
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.text.Text
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

        ClientTickEvents.END_CLIENT_TICK.register { client: MinecraftClient ->
            moduleManager.onTick(client)
            eventBus.post(TickEvent.End(client))
        }

        // Use a valid KeyBinding category, not raw String
        guiKey = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.rynox.gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                KeyBinding.MISC_CATEGORY
            )
        )

        ClientTickEvents.END_CLIENT_TICK.register { client: MinecraftClient ->
            while (guiKey.wasPressed()) {
                client.setScreen(ClickGui())
            }
        }

        ClientLifecycleEvents.CLIENT_STOPPING.register {
            configManager.save()
        }
    }
}
