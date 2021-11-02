package com.miskatonicmysteries.mixin.client;

import com.miskatonicmysteries.client.render.ShaderHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {

	@Inject(method = "getBrightness", at = @At("RETURN"), cancellable = true)
	private void getBrightness(World world, int lightLevel, CallbackInfoReturnable<Float> cir) {
		if (ShaderHandler.clairvoyanceTime > 0) {
			cir.setReturnValue(MathHelper.clamp(ShaderHandler.clairvoyanceTime / 100F, cir.getReturnValue(), 1F));
		}
	}
}
