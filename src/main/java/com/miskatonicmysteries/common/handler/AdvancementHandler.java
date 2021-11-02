package com.miskatonicmysteries.common.handler;

import net.minecraft.advancement.Advancement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class AdvancementHandler {

	public static void grantAdvancement(Identifier id, String criterion, ServerPlayerEntity player) throws NullPointerException {
		Advancement advancement = player.getServer().getAdvancementLoader().get(id);
		player.getAdvancementTracker().grantCriterion(advancement, criterion);
	}

	public static boolean hasAdvancement(Identifier id, ServerPlayerEntity player) {
		return player.getAdvancementTracker().getProgress(player.getServer().getAdvancementLoader().get(id)).isDone();
	}
}
