package com.miskatonicmysteries.mixin.world;

import com.miskatonicmysteries.common.registry.MMWorld;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.util.registry.BuiltinRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StructurePools.class)
public class StructurePoolRegistryMixin {

	@Inject(method = "register", at = @At("HEAD"), cancellable = true)
	private static void inject(StructurePool pool, CallbackInfoReturnable<StructurePool> info) {
		MMWorld.specialInject(pool);
	}
}
