package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;

import net.minecraft.entity.Entity;

public interface Triggerable {
	boolean isReadyToTrigger(OctagramBlockEntity octagram);

	void trigger(OctagramBlockEntity octagram, Entity trigger);
}
