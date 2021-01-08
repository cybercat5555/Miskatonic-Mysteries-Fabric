package com.miskatonicmysteries.common.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.PathAwareEntity;

public class FarRangeMeleeAttackGoal extends MeleeAttackGoal {
    public FarRangeMeleeAttackGoal(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle) {
        super(mob, speed, pauseWhenMobIdle);
    }

    @Override
    protected double getSquaredMaxAttackDistance(LivingEntity entity) {
        return super.getSquaredMaxAttackDistance(entity) + 1; //:)
    }
}
