package com.miskatonicmysteries.common.handler.ascension;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.block.StatueBlock;
import com.miskatonicmysteries.common.entity.HasturCultistEntity;
import com.miskatonicmysteries.common.feature.world.MMWorldState;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.SoundPacket;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMSounds;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class HasturAscensionHandler {
    public static final int SIGN_IMMUNITY_STAGE = 2;

    public static boolean offerArtToCultist(PlayerEntity player, Hand hand, HasturCultistEntity entity) {
        if (StatueBlock.isPlayerMade(player.getStackInHand(hand)) && MiskatonicMysteriesAPI.levelUp(player, 1, MMAffiliations.HASTUR)) {
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

    public static void markVillage(BlockPos villagePos, MMWorldState worldState, PlayerEntity player) {
        if (!worldState.getMarkedVillages().contains(villagePos) && worldState.getUniquelyMarkedVillages(player) >= 3 && MiskatonicMysteriesAPI.levelUp(player, 2, MMAffiliations.HASTUR)) {
            player.sendMessage(new TranslatableText("message.miskatonicmysteries.yellow_sign_ascend").formatted(Formatting.GOLD), true);
            player.playSound(MMSounds.MAGIC, 1, 1);
            SoundPacket.send(player);
        }
    }
}
