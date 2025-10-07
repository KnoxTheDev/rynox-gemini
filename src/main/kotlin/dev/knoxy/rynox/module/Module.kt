package dev.knoxy.rynox.module

import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

abstract class Module(
  val name: String,
  val category: Category,
  val description: String = ""
) {
  var enabled = false
  protected val mc = MinecraftClient.getInstance()

  open fun onEnable() {}

  open fun onDisable() {}

  open fun onTick(client: MinecraftClient) {}

  fun toggle() {
    enabled = !enabled
    if (enabled) onEnable() else onDisable()
  }

  fun getDisplayName(): Text = Text.literal("[$name] ${if (enabled) "§aON" else "§cOFF"}")

  open fun getSettings(): List<Setting> = emptyList()
}

abstract class Setting(val name: String, open var value: Any)

class SliderSetting(
  name: String,
  val min: Float,
  val max: Float,
  initial: Float
) : Setting(name, initial) {
  override var value: Float
    get() = super.value as Float
    set(value) {
      super.value = value.coerceIn(min, max)
    }
}

class ModeSetting(name: String, val modes: List<String>, initial: String) :
  Setting(name, initial)