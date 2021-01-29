package com.miskatonicmysteries.mixin;

import com.miskatonicmysteries.common.feature.interfaces.Ascendant;
import com.miskatonicmysteries.common.feature.interfaces.Sanity;
import com.miskatonicmysteries.common.feature.interfaces.SpellCaster;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    private void onPlayerConnectInject(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        Sanity.of(player).ifPresent(Sanity::syncSanityData);
        SpellCaster.of(player).ifPresent(SpellCaster::syncSpellData);
        Ascendant.of(player).ifPresent(Ascendant::syncBlessingData);
    }
}
