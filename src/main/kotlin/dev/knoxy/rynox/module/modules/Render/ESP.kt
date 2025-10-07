package dev.knoxy.rynox.module.modules.Render

import dev.knoxy.rynox.module.Category
import dev.knoxy.rynox.module.Module
import dev.knoxy.rynox.module.SliderSetting
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.Box

class ESP : Module("ESP", Category.RENDER, "Outlines entities through walls") {
  private val color = SliderSetting("Red", 0f, 1f, 1f)

  override fun getSettings() = listOf(color)

  override fun onTick(client: MinecraftClient) {}

  fun renderESP(client: MinecraftClient, matrices: net.minecraft.client.util.math.MatrixStack) {
    client.world?.entities?.filterIsInstance<PlayerEntity>()?.forEach { entity ->
      if (entity != client.player) {
        val box = entity.boundingBox
        client.worldRenderer?.drawCuboidShape(
          matrices,
          client.bufferBuilders.getEffectVertexConsumers()
            .getBuffer(RenderLayer.getOutline("rynox_esp")),
          box,
          1f, 0f, 0f, 1f
        )
      }
    }
  }
}