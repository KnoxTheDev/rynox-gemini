package dev.knoxy.rynox.gui

import dev.knoxy.rynox.Rynox
import dev.knoxy.rynox.module.Category
import dev.knoxy.rynox.module.ModuleManager
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import kotlin.math.min

class ClickGui : Screen(Text.literal("Rynox ClickGUI")) {
  private val moduleManager: ModuleManager = Rynox.moduleManager
  private val panels = Category.values().map {
    CategoryPanel(it, 10f, 10f + (it.ordinal * 120f))
  }
  private var draggingPanel: CategoryPanel? = null
  private var dragOffsetX = 0f
  private var dragOffsetY = 0f

  override fun init() {
    super.init()
  }

  override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
    // FIXED: push() and pop() are called directly on the context
    context.push()

    context.fill(0, 0, width, height, 0x80000000.toInt())

    panels.forEach { panel ->
      panel.render(context, mouseX.toFloat(), mouseY.toFloat())
    }

    context.pop()
    super.render(context, mouseX, mouseY, delta)
  }

  override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
    panels.forEach { panel ->
      if (panel.isHovered(mouseX.toFloat(), mouseY.toFloat())) {
        if (panel.headerHovered(mouseX.toFloat(), mouseY.toFloat())) {
          draggingPanel = panel
          dragOffsetX = mouseX.toFloat() - panel.x
          dragOffsetY = mouseY.toFloat() - panel.y
          return true
        } else {
          panel.mouseClicked(mouseX.toFloat(), mouseY.toFloat(), button)
        }
      }
    }
    return super.mouseClicked(mouseX, mouseY, button)
  }

  override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
    draggingPanel = null
    panels.forEach { it.mouseReleased() }
    return super.mouseReleased(mouseX, mouseY, button)
  }

  override fun mouseDragged(
    mouseX: Double,
    mouseY: Double,
    button: Int,
    deltaX: Double,
    deltaY: Double
  ): Boolean {
    draggingPanel?.let { panel ->
      panel.x = (mouseX.toFloat() - dragOffsetX).coerceAtLeast(0f)
      panel.y = (mouseY.toFloat() - dragOffsetY).coerceAtLeast(0f)
    }
    return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
  }

  inner class CategoryPanel(val category: Category, var x: Float, var y: Float) {
    private val width = 100f
    private val headerHeight = 12f
    private val moduleHeight = 12f
    private var expanded = true
    private var scrollOffset = 0f

    fun render(context: DrawContext, mouseX: Float, mouseY: Float) {
      val h = if (expanded) height else headerHeight
      context.fill(
        x.toInt(),
        y.toInt(),
        (x + width).toInt(),
        (y + h).toInt(),
        0xFF202020.toInt()
      )

      context.fill(
        x.toInt(),
        y.toInt(),
        (x + width).toInt(),
        (y + headerHeight).toInt(),
        category.color
      )
      // FIXED: textRenderer must be passed as the first argument
      context.drawTextWithShadow(
        textRenderer,
        Text.literal("${category.icon} ${category.name}"),
        (x + 2f).toInt(),
        (y + 2f).toInt(),
        0xFFFFFFFF.toInt()
      )

      if (expanded) {
        val modules = moduleManager.getModulesByCategory(category)
        modules.forEachIndexed { index, module ->
          val modY = y + headerHeight + (index * moduleHeight) - scrollOffset
          if (modY > y + headerHeight && modY < y + height - 10) {
            context.fill(
              (x + 2).toInt(),
              modY.toInt(),
              (x + width - 2).toInt(),
              (modY + moduleHeight).toInt(),
              if (module.enabled) 0xFF404040.toInt() else 0xFF303030.toInt()
            )
            // FIXED: textRenderer must be passed as the first argument
            context.drawTextWithShadow(
              textRenderer,
              Text.literal(module.name),
              (x + 4f).toInt(),
              (modY + 2f).toInt(),
              if (module.enabled) 0xFF00FF00.toInt() else 0xFFFFFFFF.toInt()
            )
          }
        }

        val contentHeight = modules.size * moduleHeight
        if (contentHeight > height - headerHeight) {
          val barHeight = (height / contentHeight) * (height - headerHeight)
          val scrollY = y + headerHeight + scrollOffset * (height - headerHeight) / contentHeight
          context.fill(
            (x + width - 4).toInt(),
            scrollY.toInt(),
            x.toInt() + width.toInt(),
            (scrollY + barHeight).toInt(),
            0xFF808080.toInt()
          )
        }
      }
    }

    fun isHovered(mx: Float, my: Float): Boolean {
      val h = if (expanded) height else headerHeight
      return mx >= x && mx <= x + width && my >= y && my <= y + h
    }

    fun headerHovered(mx: Float, my: Float): Boolean =
      mx >= x && mx <= x + width && my >= y && my <= y + headerHeight

    fun mouseClicked(mx: Float, my: Float, button: Int) {
      if (button == 0) {
        val modules = moduleManager.getModulesByCategory(category)
        modules.forEachIndexed { index, module ->
          val modY = y + headerHeight + (index * moduleHeight) - scrollOffset
          if (mx >= x && mx <= x + width && my >= modY && my <= modY + moduleHeight) {
            module.toggle()
            moduleManager.saveToConfig()
          }
        }
      } else if (button == 1) {
        expanded = !expanded
      }
    }

    fun mouseReleased() {}

    private val height: Float
      get() = headerHeight + min(
        120f,
        moduleManager.getModulesByCategory(category).size * moduleHeight
      ) + 10f
  }
}