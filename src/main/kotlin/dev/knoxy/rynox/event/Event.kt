package dev.knoxy.rynox.event

interface Event {
  fun isCancelled(): Boolean
  fun cancel()
}

open class CancellableEvent : Event {
  private var cancelled = false

  override fun isCancelled() = cancelled

  override fun cancel() {
    cancelled = true
  }
}