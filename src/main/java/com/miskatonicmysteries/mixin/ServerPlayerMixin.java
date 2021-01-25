package com.miskatonicmysteries.mixin;

import com.miskatonicmysteries.common.feature.interfaces.Sanity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerMixin {
    @Inject(method = "copyFrom(Lnet/minecraft/server/network/ServerPlayerEntity;Z)V", at = @At("TAIL"))
    private void copyStats(ServerPlayerEntity oldPlayer, boolean isDead, CallbackInfo info) {
        Sanity.of(oldPlayer).ifPresent(oldSanity -> Sanity.of(this).ifPresent(sanity -> {
            sanity.setSanity(oldSanity.getSanity(), true);
            sanity.getSanityCapExpansions().putAll(oldSanity.getSanityCapExpansions());
        }));
    }
}