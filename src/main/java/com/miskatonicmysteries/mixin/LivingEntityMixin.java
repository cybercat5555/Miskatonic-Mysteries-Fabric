package com.miskatonicmysteries.mixin;

import com.miskatonicmysteries.common.feature.interfaces.DropManipulator;
import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.MMMiscRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements DropManipulator {
    private boolean overrideDrops;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public boolean hasOverridenDrops() {
        return overrideDrops;
    }

    @Override
    public void setDropOveride(boolean dropOveride) {
        overrideDrops = dropOveride;
    }

    @Inject(method = "drop", at = @At("HEAD"), cancellable = true)
    public void dropLoot(CallbackInfo info) {
        if (hasOverridenDrops()) {
            info.cancel();
        }
    }

    @Shadow
    public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow
    public abstract Random getRandom();

    @Shadow
    public abstract @Nullable StatusEffectInstance getStatusEffect(StatusEffect effect);

    @Inject(method = "heal", at = @At("HEAD"), cancellable = true)
    private void preventHeal(float amount, CallbackInfo callbackInfo) {
        if (hasStatusEffect(MMMiscRegistries.StatusEffects.BLEED) && getRandom().nextFloat() < 0.4 + 0.2 * getStatusEffect(MMMiscRegistries.StatusEffects.BLEED).getAmplifier()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "writeCustomDataToTag(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("TAIL"))
    private void writeMiscData(CompoundTag compoundTag, CallbackInfo info) {
        compoundTag.putBoolean(Constants.NBT.SHOULD_DROP, hasOverridenDrops());
    }

    @Inject(method = "readCustomDataFromTag(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("TAIL"))
    public void readMiscData(CompoundTag compoundTag, CallbackInfo info) {
        setDropOveride(compoundTag.getBoolean(Constants.NBT.SHOULD_DROP));
    }
}
