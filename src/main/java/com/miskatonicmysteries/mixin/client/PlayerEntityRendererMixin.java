package com.miskatonicmysteries.mixin.client;

import com.miskatonicmysteries.api.item.trinkets.MaskTrinketItem;
import com.miskatonicmysteries.common.MMMidnightLibConfig;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
@Environment(EnvType.CLIENT)
public class PlayerEntityRendererMixin {

	@Inject(method = "renderLabelIfPresent", at = @At("HEAD"), cancellable = true)
	private void renderLabel(AbstractClientPlayerEntity abstractClientPlayerEntity, Text text, MatrixStack matrixStack,
							 VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		if (MMMidnightLibConfig.masksConcealNameplates && !MaskTrinketItem.getMask(abstractClientPlayerEntity).isEmpty()) {
			ci.cancel();
		}
	}
}
