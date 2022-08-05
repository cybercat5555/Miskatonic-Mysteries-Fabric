package com.miskatonicmysteries.mixin.recipe;

import com.miskatonicmysteries.api.interfaces.PlayerProvider;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.CraftingScreenHandler;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CraftingScreenHandler.class)
public class CraftingScreenHandlerMixin implements PlayerProvider {

	@Shadow @Final private PlayerEntity player;

	@Nullable
	@Override
	public PlayerEntity mm_getPlayer() {
		return player;
	}
}
