package com.miskatonicmysteries.mixin.entity;

import com.miskatonicmysteries.api.interfaces.HiddenEntity;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

	@Shadow
	public World world;

	@Inject(method = "isCollidable", at = @At("RETURN"), cancellable = true)
	private void isCollidable(CallbackInfoReturnable<Boolean> cir) {
		if (this instanceof HiddenEntity h && h.isHidden()) {
			cir.setReturnValue(false);
		}
	}
}
