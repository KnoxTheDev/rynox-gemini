package dev.knoxy.rynox.mixin;

import dev.knoxy.rynox.Rynox;
import dev.knoxy.rynox.module.ModuleManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin extends net.minecraft.entity.LivingEntity {

  protected PlayerEntityMixin(net.minecraft.world.World world) {
    super(null, world);
  }

  @Inject(at = @At("HEAD"), method = "isOnGround", cancellable = true)
  private void onIsOnGround(CallbackInfoReturnable<Boolean> cir) {
    if (Rynox.moduleManager.getModule("Fly") != null
        && Rynox.moduleManager.getModule("Fly").enabled) {
      cir.setReturnValue(true);
    }
  }

  @Inject(at = @At("TAIL"), method = "travel")
  private void onTravel(
      net.minecraft.util.math.Vec3d movementInput,
      CallbackInfo ci
  ) {
  }
}