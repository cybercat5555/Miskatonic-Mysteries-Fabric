package com.miskatonicmysteries.mixin.recipe;

import com.miskatonicmysteries.api.interfaces.PlayerProvider;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.screen.ScreenHandler;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CraftingInventory.class)
public class CraftingInventoryMixin implements PlayerProvider {

	@Shadow @Final private ScreenHandler handler;

	@Nullable
	@Override
	public PlayerEntity mm_getPlayer() {
		return handler instanceof PlayerProvider p ? p.mm_getPlayer() : null;
	}
}
