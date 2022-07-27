package com.miskatonicmysteries.common.feature.recipe.rite.condition;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class MultiRiteCondition extends RiteCondition{
	protected final Text[] messages;
	protected int messageType = 0;

	protected MultiRiteCondition(Identifier id, Text... messages) {
		super(id);
		this.messages = messages;
	}

	@Override
	protected void sendFailMessage(PlayerEntity caster) {
		caster.sendMessage(messages[messageType], true);
	}
}
