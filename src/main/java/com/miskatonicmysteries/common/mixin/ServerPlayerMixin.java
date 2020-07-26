package com.miskatonicmysteries.common.mixin;

import com.miskatonicmysteries.common.feature.sanity.ISanity;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerMixin extends PlayerEntity {
    public ServerPlayerMixin(World world, BlockPos blockPos, GameProfile gameProfile) {
        super(world, blockPos, gameProfile);
    }

    @Inject(method = "copyFrom(Lnet/minecraft/server/network/ServerPlayerEntity;Z)V", at = @At("TAIL"))
    private void copyStats(ServerPlayerEntity oldPlayer, boolean isDead, CallbackInfo info) {
        ((ISanity) this).setSanity(((ISanity) oldPlayer).getSanity());
        ((ISanity) this).getSanityCapExpansions().putAll(((ISanity) oldPlayer).getSanityCapExpansions());
    }
}