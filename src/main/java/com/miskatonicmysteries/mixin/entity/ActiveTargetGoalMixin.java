package com.miskatonicmysteries.mixin.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(ActiveTargetGoal.class)
public abstract class ActiveTargetGoalMixin<T extends LivingEntity> extends TrackTargetGoal {

	@Shadow
	@Nullable
	protected LivingEntity targetEntity;

	public ActiveTargetGoalMixin(MobEntity mob, boolean checkVisibility) {
		super(mob, checkVisibility);
	}

	@Shadow
	public abstract void setTargetEntity(@Nullable LivingEntity targetEntity);
/*
    @Inject(method = "start", at = @At("HEAD"))
    private void targetOthervibes(CallbackInfo ci){
        if(target == null && !((OthervibeMobEntityAccessor) this).access((PlayerEntity) targetEntity)){
            this.setTargetEntity(null);
        }
    }

 */
}
