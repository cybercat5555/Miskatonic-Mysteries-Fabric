package com.miskatonicmysteries.common.feature.recipe.rite.condition;

import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.world.MMDimensionalWorldState;
import com.miskatonicmysteries.common.feature.world.MMDimensionalWorldState.BiomeKnot;
import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.stream.Collectors;

public class KnotCondition extends RiteCondition {

	public KnotCondition() {
		super(new Identifier(Constants.MOD_ID, "knots"));
	}

	@Override
	public boolean test(OctagramBlockEntity octagramBlockEntity) {
		List<BiomeKnot> nearbyKnots = MMDimensionalWorldState.get((ServerWorld) octagramBlockEntity.getWorld())
			.getNearbyKnots(octagramBlockEntity.getPos(), 64)
			.stream().filter(knot -> knot.isActive() && knot.isCore()).collect(Collectors.toList());
		return nearbyKnots.isEmpty();
	}
}
