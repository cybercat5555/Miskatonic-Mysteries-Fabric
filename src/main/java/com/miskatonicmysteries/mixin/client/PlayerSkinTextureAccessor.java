package com.miskatonicmysteries.mixin.client;

import net.minecraft.client.texture.PlayerSkinTexture;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.io.File;


@Mixin(PlayerSkinTexture.class)
public interface PlayerSkinTextureAccessor {
	@Accessor("cacheFile")
	File getCacheFile();
}
