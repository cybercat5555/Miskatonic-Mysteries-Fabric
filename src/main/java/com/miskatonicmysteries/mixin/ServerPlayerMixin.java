package com.miskatonicmysteries.mixin;

import com.miskatonicmysteries.common.feature.sanity.Sanity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerMixin {
    @Inject(method = "copyFrom(Lnet/minecraft/server/network/ServerPlayerEntity;Z)V", at = @At("TAIL"))
    private void copyStats(ServerPlayerEntity oldPlayer, boolean isDead, CallbackInfo info) {
        if (oldPlayer instanceof Sanity) {
            ((Sanity) this).setSanity(((Sanity) oldPlayer).getSanity(), true);
            ((Sanity) this).getSanityCapExpansions().putAll(((Sanity) oldPlayer).getSanityCapExpansions());
        }
    }
}