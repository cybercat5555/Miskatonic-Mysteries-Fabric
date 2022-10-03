package com.miskatonicmysteries.mixin.client;

import net.minecraft.client.network.SocialInteractionsManager;

import java.util.Map;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;


@Mixin(SocialInteractionsManager.class)
public interface SocialInteractionsManagerAccessor {

	@Accessor("playerNameByUuid")
	Map<String, UUID>  getPlayerNameByUuid();
}
