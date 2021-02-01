package com.miskatonicmysteries.common.handler.ascension;

import com.miskatonicmysteries.common.entity.HasturCultistEntity;
import com.miskatonicmysteries.common.feature.Affiliation;
import com.miskatonicmysteries.common.lib.util.CapabilityUtil;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;

public class HasturAscensionHandler {

    public static boolean offerArtToCultist(PlayerEntity player, Hand hand, HasturCultistEntity entity) {
        if (CapabilityUtil.levelUp(player, 1, Affiliation.HASTUR)) { //todo add "player made" tag to statues (this nbt info is stored in the item too), so random statues can't just be used. Also gives a tooltip on who made it
            player.getStackInHand(hand).decrement(1);
            entity.lookAtEntity(player, 40, 40);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 12000, 1, true, true));
            if (!player.world.isClient()) {
                entity.playSound(SoundEvents.ENTITY_VILLAGER_YES, 1F, 1F);
                player.world.spawnEntity(new ExperienceOrbEntity(player.world, entity.getX(), entity.getX(), entity.getX(), 15));
            }
            entity.setCastTime(100);
            return true;
        }
        return false;
    }
}
