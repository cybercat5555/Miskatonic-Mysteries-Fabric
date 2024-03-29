package com.miskatonicmysteries.mixin.client;

import com.miskatonicmysteries.client.MMClientEvents;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/ShaderEffect;render(F)V", ordinal = 0))
	private void noticeGlowRender(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera,
								  GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {
		MMClientEvents.glowingActive = true;
	}
}
