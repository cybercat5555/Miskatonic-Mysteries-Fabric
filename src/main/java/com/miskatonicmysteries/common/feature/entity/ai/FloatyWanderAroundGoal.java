package com.miskatonicmysteries.common.feature.entity.ai;

import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

import org.jetbrains.annotations.Nullable;

public class FloatyWanderAroundGoal extends Goal {

	private final PathAwareEntity entity;
	private final int chance;

	public FloatyWanderAroundGoal(PathAwareEntity entity, int chance) {
		this.setControls(EnumSet.of(Control.MOVE));
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
		return FuzzyTargeting.find(entity, 8, 7);
	}
}