package com.miskatonicmysteries.common.feature.recipe.rite.condition;

import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.entity.TatteredPrinceEntity;
import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class TatteredPrinceCondition extends EntityCondition<TatteredPrinceEntity> {

	public TatteredPrinceCondition() {
		super(new Identifier(Constants.MOD_ID, "tattered_prince"), TatteredPrinceEntity.class, 1, cultist -> true);
	}

	public static void movePrince(OctagramBlockEntity octagram) {
		Vec3d pos = octagram.getSummoningPos();
		List<TatteredPrinceEntity> princes = octagram.getWorld().getEntitiesByClass(TatteredPrinceEntity.class,
																					octagram.getSelectionBox().expand(8, 5, 8),
																					prince -> !prince.isAttacking());
		for (TatteredPrinceEntity prince : princes) {
			prince.getNavigation().startMovingTo(pos.x, pos.y, pos.z, 0.8F);
			prince.lookAtEntity(octagram.getOriginalCaster(), 180, 45);
			if (octagram.tickCount < 20) {
				prince.startBlessing();
			}
			if (prince.getPos().distanceTo(pos) < 5) {
				prince.getNavigation().stop();
			}
		}
	}
}
