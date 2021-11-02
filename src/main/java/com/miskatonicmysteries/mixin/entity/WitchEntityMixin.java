package com.miskatonicmysteries.mixin.entity;

import com.miskatonicmysteries.api.interfaces.Appeasable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitchEntity.class)
public abstract class WitchEntityMixin extends RaiderEntity implements Appeasable {

	private int appeaseTicks;

	protected WitchEntityMixin(EntityType<? extends RaiderEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "attack", at = @At("HEAD"), cancellable = true)
	private void tryAttack(LivingEntity target, float pullProgress, CallbackInfo ci) {
		if (isAppeased()) {
			ci.cancel();
		}
	}

	@Override
	public boolean isAppeased() {
		return appeaseTicks > 0;
	}

	@Override
	public int getAppeasedTicks() {
		return appeaseTicks;
	}

	@Override
	public void setAppeasedTicks(int ticks) {
		this.appeaseTicks = ticks;
	}
}
