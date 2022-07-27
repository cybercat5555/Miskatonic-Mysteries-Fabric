package com.miskatonicmysteries.common.feature.recipe.rite.condition;

import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.world.MMDimensionalWorldState;
import com.miskatonicmysteries.common.feature.world.MMDimensionalWorldState.BiomeKnot;
import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ReverseBiomeCondition extends MultiRiteCondition {

	public ReverseBiomeCondition() {
		super(new Identifier(Constants.MOD_ID, "reverse_biome"),
			  new TranslatableText("message.miskatonicmysteries.rite_fail.reverse_biome.no_knots"),
			  new TranslatableText("message.miskatonicmysteries.rite_fail.reverse_biome.distance"),
			  new TranslatableText("message.miskatonicmysteries.rite_fail.reverse_biome.deactivate"));
	}

	@Override
	public boolean test(OctagramBlockEntity octagramBlockEntity) {
		List<BiomeKnot> knots = MMDimensionalWorldState.get((ServerWorld) octagramBlockEntity.getWorld())
			.getNearbyKnots(octagramBlockEntity.getPos(), 0).stream()
			.filter(BiomeKnot::isCore)
			.sorted(Comparator.comparingDouble(knot -> knot.getPos().getSquaredDistance(octagramBlockEntity.getSummoningPos())))
			.collect(Collectors.toList());
		if (knots.isEmpty()) {
			messageType = 0;
			return false;
		}
		BiomeKnot knot = knots.get(0);
		if (!knot.getPos().isWithinDistance(octagramBlockEntity.getSummoningPos(), 24)) {
			messageType = 1;
			return false;
		}
		if (knot.isActive()) {
			messageType = 2;
			return false;
		}
		return true;
	}
}
