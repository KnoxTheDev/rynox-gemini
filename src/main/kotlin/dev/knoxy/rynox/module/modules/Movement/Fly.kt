package dev.knoxy.rynox.module.modules.Movement

import dev.knoxy.rynox.module.Category
import dev.knoxy.rynox.module.Module
import dev.knoxy.rynox.module.SliderSetting
import net.minecraft.client.MinecraftClient
import net.minecraft.util.math.Vec3d

class Fly : Module("Fly", Category.MOVEMENT, "Creative flight without detection") {
  private val speed = SliderSetting("Speed", 0.1f, 2f, 0.5f)
  private val vertical = SliderSetting("Vertical", 0.1f, 1f, 0.3f)

  override fun getSettings() = listOf(speed, vertical)

  override fun onEnable() {
    mc.player?.abilities?.flying = true
  }

  override fun onDisable() {
    mc.player?.abilities?.flying = false
    mc.player?.velocity = Vec3d.ZERO
  }

  override fun onTick(client: MinecraftClient) {
    val player = client.player ?: return
    val input = client.options

    var velocity = Vec3d.ZERO
    if (input.forwardKey.isPressed) velocity = velocity.add(player.getRotationVec(1f).x * speed.value)
    if (input.backKey.isPressed) velocity = velocity.add(player.getRotationVec(1f).x * -speed.value)
    if (input.leftKey.isPressed) velocity = velocity.add(player.getRotationVec(1f).z * speed.value)
    if (input.rightKey.isPressed) velocity = velocity.add(player.getRotationVec(1f).z * -speed.value)
    if (input.sneakKey.isPressed) velocity = velocity.add(0.0, -vertical.value, 0.0)
    if (input.jumpKey.isPressed) velocity = velocity.add(0.0, vertical.value, 0.0)

    player.velocity = velocity.normalize().multiply(speed.value)
  }
}