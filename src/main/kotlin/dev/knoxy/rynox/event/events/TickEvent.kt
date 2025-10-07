package dev.knoxy.rynox.event.events

import dev.knoxy.rynox.event.CancellableEvent
import net.minecraft.client.MinecraftClient

sealed class TickEvent(open val client: MinecraftClient) : CancellableEvent() {
  class Start(val client: MinecraftClient) : TickEvent(client)
  class End(val client: MinecraftClient) : TickEvent(client)
}