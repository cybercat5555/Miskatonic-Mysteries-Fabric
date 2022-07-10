package com.miskatonicmysteries.mixin.client;

import com.miskatonicmysteries.api.interfaces.HiddenEntity;
import com.miskatonicmysteries.api.interfaces.OthervibeMobEntityAccessor;
import com.miskatonicmysteries.api.interfaces.RenderTransformable;
import com.miskatonicmysteries.common.feature.entity.HallucinationEntity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.MobEntity;

import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(MobEntityRenderer.class)
public class MobEntityRendererMixin<T extends MobEntity> {

	@Inject(method = "shouldRender", at = @At("RETURN"), cancellable = true)
	private void shouldRender(T mobEntity, Frustum frustum, double d, double e, double f, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue() && mobEntity instanceof HiddenEntity h && h.isHidden()) {
			cir.setReturnValue(HallucinationEntity.canSeeThroughMagic(MinecraftClient.getInstance().player));
		}
		if (((OthervibeMobEntityAccessor) mobEntity).access(MinecraftClient.getInstance().player)) {
			cir.setReturnValue(false);
		}
	}
}
