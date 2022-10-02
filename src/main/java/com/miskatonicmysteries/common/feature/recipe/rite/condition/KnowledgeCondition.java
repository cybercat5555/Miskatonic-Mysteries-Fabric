package com.miskatonicmysteries.common.feature.recipe.rite.condition;

import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.util.Constants;


import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class KnowledgeCondition extends RiteCondition {
	private final String knowledge;
	public KnowledgeCondition(String knowledge) {
		super(new Identifier(Constants.MOD_ID, "knowledge"));
		this.knowledge = knowledge;
		this.description = Text.translatable("desc.miskatonicmysteries.rite_fail.knowledge",
												Text.translatable(String.format("knowledge.miskatonicmysteries.%s", knowledge)));
	}

	@Override
	public boolean test(OctagramBlockEntity octagram) {
		return octagram.doesCasterHaveKnowledge(knowledge);
	}
}
