package dev.knoxy.rynox.event.events

import dev.knoxy.rynox.event.CancellableEvent
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.Camera
import net.minecraft.client.util.math.MatrixStack

class RenderEvent(
  val client: MinecraftClient,
  val matrices: MatrixStack,
  val camera: Camera,
  val tickDelta: Float
) : CancellableEvent()