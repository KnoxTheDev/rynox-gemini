package dev.knoxy.rynox.module.modules.Render

import dev.knoxy.rynox.module.Category
import dev.knoxy.rynox.module.Module
import dev.knoxy.rynox.module.SliderSetting
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.RenderLayer
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Identifier
import net.minecraft.util.math.Box
import net.minecraft.client.util.math.MatrixStack

class ESP : Module("ESP", Category.RENDER, "Outlines entities through walls") {
  private val red = SliderSetting("Red", 0f, 1f, 1f)

  override fun getSettings() = listOf(red)

  override fun onTick(client: MinecraftClient) {}

  fun renderESP(client: MinecraftClient, matrices: MatrixStack) {
    client.world?.entities?.filterIsInstance<PlayerEntity>()?.forEach { entity ->
      if (entity != client.player) {
        val box = entity.boundingBox
        val buffer = client.bufferBuilders.getEffectVertexConsumers()
          .getBuffer(RenderLayer.getOutline(Identifier("rynox", "esp")))
        client.worldRenderer.drawCuboidShape(
          matrices,
          buffer,
          box,
          red.value, 0f, 0f, 1f
        )
      }
    }
  }
}