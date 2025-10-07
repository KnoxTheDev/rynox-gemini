package dev.knoxy.rynox.mixin;

import dev.knoxy.rynox.Rynox;
import dev.knoxy.rynox.event.events.PacketEvent;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
  public ClientPlayerEntityMixin(
      net.minecraft.world.World world,
      net.minecraft.client.network.ClientPlayerInteractionManager interactionManager
  ) {
    super(world, interactionManager);
  }

  @Inject(at = @At("HEAD"), method = "sendMovementPacket")
  private void onSendMovement(PlayerMoveC2SPacket packet, CallbackInfo ci) {
    Rynox.eventBus.post(new PacketEvent.Send<>(packet));
  }
}