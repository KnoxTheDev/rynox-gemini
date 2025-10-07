package dev.knoxy.rynox.module

enum class Category(val color: Int, val icon: String) {
  COMBAT(0xFFAA00, "⚔"),
  MOVEMENT(0xFF00AA, "🏃"),
  PLAYER(0x00FFAA, "👤"),
  RENDER(0xAA00FF, "👁"),
  MISC(0xFFFFFF, "?")
}