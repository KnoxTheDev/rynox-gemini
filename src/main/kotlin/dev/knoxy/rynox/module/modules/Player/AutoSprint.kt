package dev.knoxy.rynox.module.modules.Player

import dev.knoxy.rynox.module.Category
import dev.knoxy.rynox.module.Module
import net.minecraft.client.MinecraftClient

class AutoSprint : Module("AutoSprint", Category.PLAYER, "Always sprint forward") {

  override fun onTick(client: MinecraftClient) {
    val player = client.player ?: return
    if (client.options.forwardKey.isPressed && !player.isSneaking) {
      player.setSprinting(true)
    }
  }

  override fun onDisable() {
    mc.player?.setSprinting(false)
  }
}