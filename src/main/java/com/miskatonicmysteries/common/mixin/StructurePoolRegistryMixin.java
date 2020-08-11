package com.miskatonicmysteries.common.mixin;

import com.miskatonicmysteries.common.feature.world.structures.ModifiableStructurePool;
import com.miskatonicmysteries.common.handler.callback.StructurePoolRegistryCallback;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StructurePoolRegistry.class)
public class StructurePoolRegistryMixin {
    //code taken from https://github.com/FoundationGames/Sandwichable/tree/master/remappedSrc/io/github/foundationgames/sandwichable (credit goes to Foundationgames and Draylar)
    @Inject(method = "add", at = @At("HEAD"))
    private void inject(StructurePool pool, CallbackInfo info) {
        StructurePoolRegistryCallback.EVENT.invoker().add(new ModifiableStructurePool(pool));
    }
}
