package com.miskatonicmysteries.mixin.client;

import net.minecraft.client.network.SocialInteractionsManager;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.PlayerSkinTexture;

import java.util.Map;
import java.util.UUID;

import java.io.File;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;


@Mixin(SocialInteractionsManager.class)
public interface SocialInteractionsManagerAccessor {

	@Accessor("playerNameByUuid")
	Map<String, UUID>  getPlayerNameByUuid();
}
