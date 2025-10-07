package dev.knoxy.rynox.event.events

import dev.knoxy.rynox.event.CancellableEvent
import net.minecraft.network.Packet

sealed class PacketEvent<T : Packet<*>>(open val packet: T) : CancellableEvent() {
  class Send<T : Packet<*>>(packet: T) : PacketEvent<T>(packet)
  class Receive<T : Packet<*>>(packet: T) : PacketEvent<T>(packet)
}