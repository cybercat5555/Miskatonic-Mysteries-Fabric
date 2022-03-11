package com.miskatonicmysteries.mixin.entity;

import com.miskatonicmysteries.api.interfaces.OthervibeMobEntityAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ActiveTargetGoal.class)
public abstract class ActiveTargetGoalMixin<T extends LivingEntity> extends TrackTargetGoal {
    public ActiveTargetGoalMixin(MobEntity mob, boolean checkVisibility) {
        super(mob, checkVisibility);
    }

    @Shadow
    @Nullable
    protected LivingEntity targetEntity;

    @Shadow public abstract void setTargetEntity(@Nullable LivingEntity targetEntity);

    @Inject(method = "start", at = @At("HEAD"))
    private void targetOthervibes(CallbackInfo ci){
        if(target == null && !((OthervibeMobEntityAccessor) this).access((PlayerEntity) targetEntity)){
            this.setTargetEntity(null);
        }
    }
}
