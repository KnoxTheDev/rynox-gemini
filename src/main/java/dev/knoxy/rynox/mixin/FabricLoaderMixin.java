package dev.knoxy.rynox.mixin;

import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.fabricmc.loader.impl.entrypoint.EntrypointUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(value = FabricLoaderImpl.class, remap = false)
public class FabricLoaderMixin {

  @Inject(at = @At("HEAD"), method = "getAllMods", cancellable = true)
  private void filterModList(CallbackInfoReturnable<List<ModContainer>> cir) {
    List<ModContainer> originalList =
        ((FabricLoaderImpl) (Object) this).internal.getAllMods();
    List<ModContainer> filtered = originalList.stream()
        .filter(mod -> !mod.getMetadata().getId().equals("rynox"))
        .collect(Collectors.toList());
    cir.setReturnValue(filtered);
  }

  @Inject(at = @At("HEAD"), method = "load")
  private void suppressModLog(CallbackInfo ci) {
    if (EntrypointUtils.getModContainers().stream()
        .anyMatch(mod -> mod.getMetadata().getId().equals("rynox"))) {
      ci.cancel();
    }
  }
}