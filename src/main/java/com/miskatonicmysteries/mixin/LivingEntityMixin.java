package com.miskatonicmysteries.mixin;

import com.miskatonicmysteries.common.lib.ModRegistries;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow
    public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow
    public abstract Random getRandom();

    @Shadow
    public abstract @Nullable StatusEffectInstance getStatusEffect(StatusEffect effect);

    @Inject(method = "heal", at = @At("HEAD"), cancellable = true)
    private void preventHeal(float amount, CallbackInfo callbackInfo) {
        if (hasStatusEffect(ModRegistries.BLEED) && getRandom().nextFloat() < 0.4 + 0.2 * getStatusEffect(ModRegistries.BLEED).getAmplifier()) {
            callbackInfo.cancel();
        }
    }
}
