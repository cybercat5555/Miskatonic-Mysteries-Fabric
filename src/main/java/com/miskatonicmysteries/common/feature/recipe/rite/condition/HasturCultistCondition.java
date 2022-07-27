package com.miskatonicmysteries.common.feature.recipe.rite.condition;

import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.entity.HasturCultistEntity;
import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class HasturCultistCondition extends EntityCondition<HasturCultistEntity> {

	public HasturCultistCondition(int amount) {
		super(new Identifier(Constants.MOD_ID, "hastur_cultists"), HasturCultistEntity.class, amount, cultist -> true);
	}

	public static void moveCultists(OctagramBlockEntity octagram) {
		Vec3d pos = octagram.getSummoningPos();
		List<HasturCultistEntity> cultists = octagram.getWorld().getEntitiesByClass(HasturCultistEntity.class,
																					octagram.getSelectionBox().expand(15, 5, 15),
																					cultist -> !cultist.isAttacking());
		for (HasturCultistEntity cultist : cultists) {
			cultist.getNavigation().startMovingTo(pos.x, pos.y, pos.z, 0.8F);
			if (cultist.getPos().distanceTo(pos) < 5) {
				cultist.getNavigation().stop();
				cultist.currentSpell = null;
				cultist.setCastTime(20);
			}
		}
	}
}
