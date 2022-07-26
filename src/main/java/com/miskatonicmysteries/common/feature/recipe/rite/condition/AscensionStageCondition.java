package com.miskatonicmysteries.common.feature.recipe.rite.condition;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import org.jetbrains.annotations.Nullable;

public class AscensionStageCondition extends RiteCondition {
	private final Affiliation ascensionAffiliation;
	private final int stage;
	public AscensionStageCondition(@Nullable Affiliation affiliation, int stage) {
		super(new Identifier(Constants.MOD_ID, "ascension_stage"));
		this.ascensionAffiliation = affiliation;
		this.stage = stage;
	}

	@Override
	public boolean test(OctagramBlockEntity octagramBlockEntity) {
		PlayerEntity caster = octagramBlockEntity.getOriginalCaster();
		return (ascensionAffiliation == null || ascensionAffiliation.equals(MiskatonicMysteriesAPI.getNonNullAffiliation(caster, false)))
			&& MiskatonicMysteriesAPI.getAscensionStage(caster) >= stage;
	}
}
