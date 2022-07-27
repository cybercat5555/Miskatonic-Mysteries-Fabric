package com.miskatonicmysteries.common.feature.recipe.rite.condition;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import org.jetbrains.annotations.Nullable;

public class KnowledgeCondition extends RiteCondition {
	private final String knowledge;
	public KnowledgeCondition(String knowledge) {
		super(new Identifier(Constants.MOD_ID, "knowledge"));
		this.knowledge = knowledge;
	}

	@Override
	public boolean test(OctagramBlockEntity octagram) {
		return octagram.doesCasterHaveKnowledge(knowledge);
	}
}
