package dev.knoxy.rynox.module.modules.Combat

import dev.knoxy.rynox.module.Category
import dev.knoxy.rynox.module.Module
import dev.knoxy.rynox.module.SliderSetting
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class KillAura : Module("KillAura", Category.COMBAT, "Auto-attacks nearby entities") {
  private val range = SliderSetting("Range", 3.5f, 6f, 4.2f)
  private val cps = SliderSetting("CPS", 8f, 20f, 12f)

  private var lastAttack = 0L
  private var currentTarget: LivingEntity? = null

  override fun getSettings() = listOf(range, cps)

  override fun onTick(client: MinecraftClient) {
    if (client.interactionManager?.isBreakingBlock == true) return

    currentTarget = findTarget(client)
    currentTarget?.let { target ->
      if (System.currentTimeMillis() - lastAttack > (1000 / cps.value)) {
        rotateTo(target, client)
        client.interactionManager?.attackEntity(client.player!!, target)
        client.player!!.swingHand(Hand.MAIN_HAND)
        lastAttack = System.currentTimeMillis()
      }
    }
  }

  private fun findTarget(client: MinecraftClient): LivingEntity? {
    return client.world?.getOtherEntities(
      client.player!!,
      client.player!!.boundingBox.expand(range.value.toDouble())
    )?.filterIsInstance<LivingEntity>()?.filter {
      it !is client.player && it.health > 0 && !it.isDead
    }?.minByOrNull { client.player!!.distanceTo(it) } as LivingEntity?
  }

  private fun rotateTo(target: LivingEntity, client: MinecraftClient) {
    val delta = target.pos.subtract(client.player!!.pos)
    val yaw = (atan2(delta.z, delta.x) * 180 / Math.PI - 90).toFloat()
    val pitch = -(atan2(delta.y, sqrt(delta.x * delta.x + delta.z * delta.z)) * 180 / Math.PI).toFloat()

    client.player!!.yaw = yaw
    client.player!!.pitch = pitch.coerceIn(-90f, 90f)
  }
}