package com.miskatonicmysteries.mixin.entity;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.interfaces.Appeasable;
import com.miskatonicmysteries.api.interfaces.BiomeAffected;
import com.miskatonicmysteries.api.interfaces.DropManipulator;
import com.miskatonicmysteries.api.interfaces.HiddenEntity;
import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.entity.HallucinationEntity;
import com.miskatonicmysteries.common.feature.world.biome.BiomeEffect;
import com.miskatonicmysteries.common.registry.MMStatusEffects;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements DropManipulator, BiomeAffected {
    @Unique
    private boolean overrideDrops;
    @Unique
    private BiomeEffect currentBiomeEffect;

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

    @Override
    public BiomeEffect getCurrentBiomeEffect() {
        return currentBiomeEffect;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        if (!world.isClient) {
            if (age % 20 == 0) {
                currentBiomeEffect = MiskatonicMysteriesAPI.getBiomeEffect(world, getBlockPos());
            }

            if (currentBiomeEffect != null) {
                currentBiomeEffect.tickFor((LivingEntity) (Object) this);
            }
        }
    }

    @Inject(method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("RETURN"), cancellable = true)
    private void canTarget(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && this instanceof HiddenEntity h && h.isHidden()) {
            cir.setReturnValue(HallucinationEntity.canSeeThroughMagic(target));
        }
    }

    @Inject(method = "onKilledBy", at = @At("HEAD"))
    private void onKilledBy(@Nullable LivingEntity adversary, CallbackInfo ci) {
        if (!world.isClient && getType().isIn(Constants.Tags.VALID_SACRIFICES)) {
            OctagramBlockEntity.onEntitySacrificed(world, getBlockPos());
        }
    }

    @Inject(method = "eatFood", at = @At("HEAD"), cancellable = true)
    private void eatFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (hasStatusEffect(MMStatusEffects.EXOTIC_CRAVINGS)) {
            if (!Constants.Tags.GROSS_FOOD.contains(stack.getItem())) {
                damage(DamageSource.STARVE, 4);
            } else {
                StatusEffectInstance instance = getStatusEffect(MMStatusEffects.EXOTIC_CRAVINGS);
                removeStatusEffect(MMStatusEffects.EXOTIC_CRAVINGS);
                addStatusEffect(new StatusEffectInstance(instance.getEffectType(), instance
                        .getDuration() - (200 - instance.getAmplifier() * 40), instance.getAmplifier(), false, true));
            }
        }
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

    @Shadow
    public abstract boolean damage(DamageSource source, float amount);

    @Shadow
    public abstract boolean removeStatusEffect(StatusEffect type);

    @Shadow
    public abstract boolean addStatusEffect(StatusEffectInstance effect);

    @Inject(method = "heal", at = @At("HEAD"), cancellable = true)
    private void preventHeal(float amount, CallbackInfo callbackInfo) {
        if (hasStatusEffect(MMStatusEffects.BLEED) && getRandom()
                .nextFloat() < 0.4 + 0.2 * getStatusEffect(MMStatusEffects.BLEED).getAmplifier()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeMiscData(NbtCompound compoundTag, CallbackInfo info) {
        compoundTag.putBoolean(Constants.NBT.SHOULD_DROP, hasOverridenDrops());
        Appeasable.of(this).ifPresent(appeasable -> {
            compoundTag.putInt(Constants.NBT.APPEASE_TICKS, appeasable.getAppeasedTicks());
        });
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readMiscData(NbtCompound compoundTag, CallbackInfo info) {
        setDropOveride(compoundTag.getBoolean(Constants.NBT.SHOULD_DROP));
        Appeasable.of(this).ifPresent(appeasable -> {
            appeasable.setAppeasedTicks(compoundTag.getInt(Constants.NBT.APPEASE_TICKS));
        });
    }

    @Inject(method = "canTarget(Lnet/minecraft/entity/EntityType;)Z", at = @At("HEAD"), cancellable = true)
    private void appease(EntityType<?> type, CallbackInfoReturnable<Boolean> cir) {
        if (Appeasable.of(this).isPresent() && Appeasable.of(this).get().isAppeased()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "handleStatus", at = @At("HEAD"), cancellable = true)
    public void handleStatus(byte status, CallbackInfo ci) {
        if (this instanceof Appeasable && status == 14) {
            for (int i = 0; i < 5; ++i) {
                double d = this.random.nextGaussian() * 0.02D;
                double e = this.random.nextGaussian() * 0.02D;
                double f = this.random.nextGaussian() * 0.02D;
                this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getParticleX(1.0D), this
                        .getRandomBodyY() + 1.0D, this.getParticleZ(1.0D), d, e, f);
            }
            ci.cancel();
        }
    }
}
