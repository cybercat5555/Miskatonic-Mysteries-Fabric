package com.miskatonicmysteries.mixin.entity;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.interfaces.Appeasable;
import com.miskatonicmysteries.api.interfaces.BiomeAffected;
import com.miskatonicmysteries.api.interfaces.DropManipulator;
import com.miskatonicmysteries.api.interfaces.HiddenEntity;
import com.miskatonicmysteries.api.interfaces.RenderTransformable;
import com.miskatonicmysteries.common.feature.effect.BleedStatusEffect;
import com.miskatonicmysteries.common.feature.effect.BrainDrainStatusEffect;
import com.miskatonicmysteries.common.feature.effect.ExoticCravingsStatusEffect;
import com.miskatonicmysteries.common.feature.entity.HallucinationEntity;
import com.miskatonicmysteries.common.feature.world.biome.BiomeEffect;
import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements DropManipulator, BiomeAffected, RenderTransformable {
	@Unique int mm_squishTicks = 0;

	@Unique
	private boolean overrideDrops;
	@Unique
	private BiomeEffect currentBiomeEffect;
	@Unique
	private DamageSource currentDamageSource;

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
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
		} else {
			if (mm_getSquishTicks() > 0) {
				mm_squishTicks--;
			}
		}
	}

	@Inject(method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("RETURN"), cancellable = true)
	private void canTarget(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue() && this instanceof HiddenEntity h && h.isHidden()) {
			cir.setReturnValue(HallucinationEntity.canSeeThroughMagic(target));
		}
	}

	@Inject(method = "eatFood", at = @At("HEAD"), cancellable = true)
	private void eatFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
		ExoticCravingsStatusEffect.onFoodEaten((LivingEntity) (Object) this, stack);
	}

	@Shadow
	public abstract boolean hasStatusEffect(StatusEffect effect);

	@Shadow
	public abstract @Nullable StatusEffectInstance getStatusEffect(StatusEffect effect);

	@Shadow
	public abstract boolean damage(DamageSource source, float amount);

	@Shadow
	public abstract boolean removeStatusEffect(StatusEffect type);

	@Shadow
	public abstract boolean addStatusEffect(StatusEffectInstance effect);

	@Inject(method = "drop", at = @At("HEAD"), cancellable = true)
	public void dropLoot(CallbackInfo info) {
		if (hasOverridenDrops()) {
			info.cancel();
		}
	}

	@Inject(method = "heal", at = @At("HEAD"), cancellable = true)
	private void preventHeal(float amount, CallbackInfo callbackInfo) {
		BleedStatusEffect.onTryHeal((LivingEntity) (Object) this, callbackInfo);
	}

	@Inject(method = "damage", at = @At("HEAD"))
	private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		currentDamageSource = source;
	}

	@ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
	private float modifyDamage(float original) {
		return BrainDrainStatusEffect.onDamage((LivingEntity) (Object) this, currentDamageSource, original);
	}

	@Shadow
	public abstract Random getRandom();

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

	@Override
	public void setDropOveride(boolean dropOveride) {
		overrideDrops = dropOveride;
	}

	@Override
	public boolean hasOverridenDrops() {
		return overrideDrops;
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

	@Override
	public int mm_getSquishTicks() {
		return mm_squishTicks;
	}

	@Override
	public void mm_squish() {
		this.mm_squishTicks = 20;
	}
}
