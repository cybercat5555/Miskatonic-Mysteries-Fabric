package com.miskatonicmysteries.mixin.villagers;

import com.miskatonicmysteries.common.feature.blessing.Blessing;
import com.miskatonicmysteries.common.feature.interfaces.Ascendant;
import com.miskatonicmysteries.common.lib.util.CapabilityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityInteraction;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.village.VillageGossipType;
import net.minecraft.village.VillagerGossips;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(VillagerEntity.class)
public class VillagerEntityMixin {
    @Shadow
    @Final
    private VillagerGossips gossip;

    @Inject(method = "onInteractionWith", at = @At("HEAD"), cancellable = true)
    private void onInteractionWith(EntityInteraction interaction, Entity entity, CallbackInfo ci) {
        Optional<Ascendant> optionalAscendant = Ascendant.of(entity);
        if (optionalAscendant.isPresent() && CapabilityUtil.hasBlessing(optionalAscendant.get(), Blessing.CHARMING_PERSONALITY)) {
            if (interaction == EntityInteraction.ZOMBIE_VILLAGER_CURED) {
                this.gossip.startGossip(entity.getUuid(), VillageGossipType.MAJOR_POSITIVE, 35);
                this.gossip.startGossip(entity.getUuid(), VillageGossipType.MINOR_POSITIVE, 40);
            } else if (interaction == EntityInteraction.TRADE) {
                this.gossip.startGossip(entity.getUuid(), VillageGossipType.TRADING, 6);
            }
            //completely ignore bad interactions
            ci.cancel();
        }
    }
}
