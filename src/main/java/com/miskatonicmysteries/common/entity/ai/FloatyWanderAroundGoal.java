package com.miskatonicmysteries.common.entity.ai;

import net.minecraft.entity.ai.TargetFinder;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class FloatyWanderAroundGoal extends Goal {
    private PathAwareEntity entity;
    private int chance;

    public FloatyWanderAroundGoal(PathAwareEntity entity, int chance) {
        this.setControls(EnumSet.of(Goal.Control.MOVE));
        this.entity = entity;
        this.chance = chance;
    }

    public boolean canStart() {
        return entity.getNavigation().isIdle() && entity.getRandom().nextInt(chance) == 0;
    }

    public boolean shouldContinue() {
        return entity.getNavigation().isFollowingPath();
    }

    public void start() {
        Vec3d vec3d = this.getRandomLocation();
        if (vec3d != null) {
            entity.getNavigation().startMovingAlong(entity.getNavigation().findPathTo((new BlockPos(vec3d)), 2), 1.0D);
        }
    }

    @Nullable
    private Vec3d getRandomLocation() {
        Vec3d vec3d3 = entity.getRotationVec(0.0F);
        Vec3d vec3d4 = TargetFinder.findAirTarget(entity, 8, 7, vec3d3, 1.5707964F, 3, 1);
        return vec3d4 != null ? vec3d4 : TargetFinder.findGroundTarget(entity, 8, 4, -2, vec3d3, 1.5707963705062866D);
    }
}