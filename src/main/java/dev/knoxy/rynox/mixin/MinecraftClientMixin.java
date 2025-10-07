package dev.knoxy.rynox.mixin;

import dev.knoxy.rynox.Rynox;
import dev.knoxy.rynox.event.events.TickEvent;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

  @Inject(at = @At("HEAD"), method = "tick")
  private void onTick(CallbackInfo ci) {
    Rynox.eventBus.post(new TickEvent.Start((MinecraftClient) (Object) this));
  }

  @Inject(at = @At("TAIL"), method = "tick")
  private void onPostTick(CallbackInfo ci) {
    Rynox.eventBus.post(new TickEvent.End((MinecraftClient) (Object) this));
  }
}