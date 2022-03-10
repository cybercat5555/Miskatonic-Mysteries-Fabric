package com.miskatonicmysteries.mixin.client;

import com.miskatonicmysteries.api.interfaces.HiddenEntity;
import com.miskatonicmysteries.common.feature.entity.HallucinationEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class ClientEntityMixin {

	@Shadow
	public World world;



	@Inject(method = "playSound", at = @At("HEAD"), cancellable = true)
	private void playSound(SoundEvent sound, float volume, float pitch, CallbackInfo ci) {
		if (this instanceof HiddenEntity h && h.isHidden()) {
			if (!world.isClient || !HallucinationEntity.canSeeThroughMagic(MinecraftClient.getInstance().player)) {
				ci.cancel();
			}
		}
	}
/*
	@Inject(method = "isInvisibleTo", at = @At("HEAD"), cancellable = true)
	private void othervibesMobInvisibility(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {

	}

 */
}
