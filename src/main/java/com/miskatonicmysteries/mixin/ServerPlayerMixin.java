package com.miskatonicmysteries.mixin;

import com.miskatonicmysteries.common.feature.interfaces.Ascendant;
import com.miskatonicmysteries.common.feature.interfaces.MalleableAffiliated;
import com.miskatonicmysteries.common.feature.interfaces.Sanity;
import com.miskatonicmysteries.common.feature.interfaces.SpellCaster;
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
            sanity.syncSanityData();
        }));

        SpellCaster.of(oldPlayer).ifPresent(oldCaster -> SpellCaster.of(this).ifPresent(caster -> {
            caster.setPowerPool(oldCaster.getPowerPool());
            oldCaster.getAvailableMediums().forEach(caster::setMediumAvailability);
            oldCaster.getLearnedEffects().forEach(effect -> caster.getLearnedEffects().add(effect));
            oldCaster.getSpells().forEach(spell -> caster.getSpells().add(spell));
            caster.syncSpellData();
        }));

        Ascendant.of(oldPlayer).ifPresent(oldAscendant -> Ascendant.of(this).ifPresent(ascendant -> {
            ascendant.setStage(oldAscendant.getStage());
        }));

        MalleableAffiliated.of(oldPlayer).ifPresent(oldAffiliation -> MalleableAffiliated.of(this).ifPresent(affiliation -> {
            affiliation.setAffiliation(oldAffiliation.getAffiliation(false), false);
            affiliation.setAffiliation(oldAffiliation.getAffiliation(true), true);
        }));
    }
}