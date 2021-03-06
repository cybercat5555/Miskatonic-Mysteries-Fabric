package com.miskatonicmysteries.mixin.villagers;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.interfaces.Ascendant;
import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.common.handler.InsanityHandler;
import com.miskatonicmysteries.common.registry.MMBlessings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityInteraction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.village.VillageGossipType;
import net.minecraft.village.VillagerGossips;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin {
    @Shadow
    @Final
    private VillagerGossips gossip;

    @Shadow
    protected abstract void sayNo();

    @Inject(method = "onInteractionWith", at = @At("HEAD"), cancellable = true)
    private void onInteractionWith(EntityInteraction interaction, Entity entity, CallbackInfo ci) {
        Optional<Ascendant> optionalAscendant = Ascendant.of(entity);
        if (optionalAscendant.isPresent() && MiskatonicMysteriesAPI.hasBlessing(optionalAscendant.get(), MMBlessings.CHARMING_PERSONALITY)) {
            if (interaction == EntityInteraction.ZOMBIE_VILLAGER_CURED) {
                this.gossip.startGossip(entity.getUuid(), VillageGossipType.MAJOR_POSITIVE, 40);
                this.gossip.startGossip(entity.getUuid(), VillageGossipType.MINOR_POSITIVE, 60);
            } else if (interaction == EntityInteraction.TRADE) {
                this.gossip.startGossip(entity.getUuid(), VillageGossipType.TRADING, 6);
                if (entity instanceof LivingEntity) {
                    ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 2400, 0, true, false, false));
                    ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 2400, 0, true, false, false));
                    ((LivingEntity) entity).heal(5F);
                }
            }
            //completely ignore bad interactions
            ci.cancel();
        }
    }

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void playerInteract(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (InsanityHandler.calculateSanityFactor(Sanity.of(player)) < 0.4F) {
            sayNo();
            cir.setReturnValue(ActionResult.success(player.world.isClient));
        }
    }
}
