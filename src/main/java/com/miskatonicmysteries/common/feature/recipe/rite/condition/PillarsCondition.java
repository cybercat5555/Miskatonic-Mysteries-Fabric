package com.miskatonicmysteries.common.feature.recipe.rite.condition;

import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.util.Identifier;

public class PillarsCondition extends RiteCondition {
	private final Affiliation affiliation;
	public PillarsCondition(Affiliation affiliation) {
		super(new Identifier(Constants.MOD_ID, "pillars"));
		this.affiliation = affiliation;
	}

	@Override
	public boolean test(OctagramBlockEntity octagramBlockEntity) {
		return octagramBlockEntity.checkPillars(affiliation);
	}

	@Override
	public boolean shouldCheckWhileRunning() {
		return true;
	}
}
