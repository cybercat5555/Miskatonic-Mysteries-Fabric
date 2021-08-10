package com.miskatonicmysteries.mixin.client;

import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.PlayerSkinTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.io.File;


@Mixin(PlayerSkinTexture.class)
public interface PlayerSkinTextureAccessor {
	@Accessor("cacheFile")
	File getCacheFile();

	@Invoker("remapTexture")
	public NativeImage invokeRemapTexture(NativeImage nativeImage);
}
