package com.miskatonicmysteries.common.feature.entity.navigation;

import net.minecraft.entity.ai.pathing.BirdPathNodeMaker;
import net.minecraft.entity.ai.pathing.LandPathNodeMaker;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

public class FeasterPathNodeMaker extends MobNavigation {

	public FeasterPathNodeMaker(@NotNull final MobEntity entity, final World world, NavType type) {
		super(entity, world);
		switch (type) {
			case FLYING -> this.nodeMaker = new BirdPathNodeMaker();
			case WALKING -> this.nodeMaker = new LandPathNodeMaker();
		}
		this.nodeMaker.setCanSwim(true);
	}

	public enum NavType {
		WALKING,
		FLYING
	}
}
