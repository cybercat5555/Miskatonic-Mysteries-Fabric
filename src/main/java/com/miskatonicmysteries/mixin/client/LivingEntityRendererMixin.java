package com.miskatonicmysteries.mixin.client;

import com.miskatonicmysteries.api.interfaces.RenderTransformable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
	@Inject(method = "setupTransforms", at = @At("HEAD"))
	private void applyTransformations(LivingEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta, CallbackInfo ci) {
		if (entity instanceof RenderTransformable r && r.mm_getSquishTicks() > 0) {
			float scale = 1F - 0.2F * MathHelper.sin((r.mm_getSquishTicks() - tickDelta) / 19F * MathHelper.PI);
			matrices.scale(scale, 1, scale);
		}
	}
}
