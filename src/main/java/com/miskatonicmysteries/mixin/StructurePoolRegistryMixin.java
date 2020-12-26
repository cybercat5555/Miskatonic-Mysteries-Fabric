package com.miskatonicmysteries.mixin;

import com.miskatonicmysteries.common.lib.ModWorld;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.util.registry.BuiltinRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StructurePools.class)
public class StructurePoolRegistryMixin {
    @Inject(method = "register(Lnet/minecraft/structure/pool/StructurePool;)Lnet/minecraft/structure/pool/StructurePool;", at = @At("HEAD"), cancellable = true)
    private static void inject(StructurePool pool, CallbackInfoReturnable<StructurePool> info) {
        pool = ModWorld.specialInject(pool);
        info.setReturnValue(BuiltinRegistries.add(BuiltinRegistries.STRUCTURE_POOL, pool.getId(), pool));
    }
}
