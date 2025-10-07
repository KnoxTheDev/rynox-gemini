package dev.knoxy.rynox.mixin;

import dev.knoxy.rynox.Rynox;
import dev.knoxy.rynox.event.events.RenderEvent;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {

  @Inject(
      at = @At("HEAD"),
      method = "render(Lnet/minecraft/entity/Entity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
  )
  private void onRender(
      T entity,
      float yaw,
      float tickDelta,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumers,
      int light,
      CallbackInfo ci
  ) {
    RenderEvent event = new RenderEvent(
        net.minecraft.client.MinecraftClient.getInstance(),
        matrices,
        net.minecraft.client.MinecraftClient.getInstance().gameRenderer.camera,
        tickDelta
    );
    Rynox.eventBus.post(event);
  }
}