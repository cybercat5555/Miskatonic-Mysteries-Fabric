package com.miskatonicmysteries.common.handler.ascension;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.block.StatueBlock;
import com.miskatonicmysteries.api.interfaces.SpellCaster;
import com.miskatonicmysteries.common.entity.HasturCultistEntity;
import com.miskatonicmysteries.common.entity.TatteredPrinceEntity;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMSounds;
import com.miskatonicmysteries.common.util.Util;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

public class HasturAscensionHandler {
    public static final int SIGN_IMMUNITY_STAGE = 2;

    public static boolean offerArtToCultist(PlayerEntity player, Hand hand, HasturCultistEntity entity) {
        if (StatueBlock.isPlayerMade(player.getStackInHand(hand)) && MiskatonicMysteriesAPI.levelUp(player, 1, MMAffiliations.HASTUR)) {
            player.getStackInHand(hand).decrement(1);
            entity.lookAtEntity(player, 40, 40);
            MiskatonicMysteriesAPI.guaranteeSpellPower(3, SpellCaster.of(player).get());
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

    public static void blessThroughPrince(LivingEntity blessTarget, TatteredPrinceEntity prince) {
        if (blessTarget instanceof PlayerEntity && MiskatonicMysteriesAPI.levelUp((PlayerEntity) blessTarget, 2, MMAffiliations.HASTUR)) {
            prince.playSound(MMSounds.MAGIC, 1, 1);
            Vec3d pos = Util.getYawRelativePos(prince.getPos(), 3, prince.getYaw(), prince.getPitch());
            Vec3d motionVec = new Vec3d(pos.x - blessTarget.getX(), pos.y + 2 - blessTarget.getY(), pos.z - blessTarget.getZ());
            if (motionVec.length() > 0.1) {
                blessTarget.setVelocity(motionVec);
                blessTarget.velocityModified = true;
                blessTarget.velocityDirty = true;
            }
            MiskatonicMysteriesAPI.grantBlessing((PlayerEntity) blessTarget, MMAffiliations.HASTUR);
        }
    }
}
