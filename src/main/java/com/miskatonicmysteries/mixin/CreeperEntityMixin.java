package com.miskatonicmysteries.mixin;

import com.miskatonicmysteries.api.interfaces.Hallucination;
import net.minecraft.entity.mob.CreeperEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreeperEntity.class)
public class CreeperEntityMixin {
    @Inject(method = "explode", at = @At("HEAD"), cancellable = true)
    private void explode(CallbackInfo ci) {
        if (Hallucination.of(this).isPresent() && Hallucination.of(this).get().getHallucinationTarget().isPresent()) {
            ci.cancel();
        }
    }
}
