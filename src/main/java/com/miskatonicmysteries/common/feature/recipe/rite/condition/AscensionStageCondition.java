package com.miskatonicmysteries.common.feature.recipe.rite.condition;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import org.jetbrains.annotations.Nullable;

public class AscensionStageCondition extends MultiRiteCondition {

	private final Affiliation ascensionAffiliation;
	private final int stage;

	public AscensionStageCondition(@Nullable Affiliation affiliation, int stage) {
		super(new Identifier(Constants.MOD_ID, "ascension_stage"),
			  Text.translatable("message.miskatonicmysteries.rite_fail.ascension_stage.stage"),
			  Text.translatable("message.miskatonicmysteries.rite_fail.ascension_stage.path"));
		this.ascensionAffiliation = affiliation;
		this.stage = stage;
		this.description = Text.translatable("desc.miskatonicmysteries.rite_fail.ascension_stage",
												affiliation == null ? Text.translatable("affiliation.miskatonicmysteries.null")
																	: affiliation.getLocalizedName(), stage);
	}

	@Override
	public boolean test(OctagramBlockEntity octagramBlockEntity) {
		PlayerEntity caster = octagramBlockEntity.getOriginalCaster();
		if (MiskatonicMysteriesAPI.getAscensionStage(caster) < stage) {
			messageType = 0;
			return false;
		}
		if (ascensionAffiliation != null && !ascensionAffiliation.equals(MiskatonicMysteriesAPI.getNonNullAffiliation(caster, false))) {
			messageType = 1;
			return false;
		}
		return true;
	}
}
