package com.miskatonicmysteries.common.handler.ascension;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.block.StatueBlock;
import com.miskatonicmysteries.api.interfaces.SpellCaster;
import com.miskatonicmysteries.common.feature.entity.HasturCultistEntity;
import com.miskatonicmysteries.common.feature.entity.TatteredPrinceEntity;
import com.miskatonicmysteries.common.feature.world.party.Party;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMSounds;
import com.miskatonicmysteries.common.util.Util;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HasturAscensionHandler {

	public static final int ART_STAGE = 1;
	public static final int SIGN_IMMUNITY_STAGE = 2;
	public static final int GATHERING_STAGE = 2;
	public static final int SIMULACRUM_STAGE = 3;
	public static final int END_STAGE = 4;

	public static boolean offerArtToCultist(PlayerEntity player, Hand hand, HasturCultistEntity entity) {
		if (StatueBlock.isPlayerMade(player.getStackInHand(hand)) && MiskatonicMysteriesAPI.levelUp(player, ART_STAGE,
			MMAffiliations.HASTUR)) {
			player.getStackInHand(hand).decrement(1);
			entity.lookAtEntity(player, 40, 40);
			MiskatonicMysteriesAPI.guaranteeSpellPower(3, SpellCaster.of(player).get());
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 12000, 1, true, true));
			if (!player.world.isClient()) {
				entity.playSound(SoundEvents.ENTITY_VILLAGER_YES, 1F, 1F);
				player.world.spawnEntity(new ExperienceOrbEntity(player.world, entity.getX(), entity.getX(),
					entity.getX(), 15));
			}
			entity.setCastTime(100);
			return true;
		}
		return false;
	}

	public static void blessThroughPrince(LivingEntity blessTarget, TatteredPrinceEntity prince) {
		if (blessTarget instanceof PlayerEntity && MiskatonicMysteriesAPI.levelUp((PlayerEntity) blessTarget, GATHERING_STAGE,
			MMAffiliations.HASTUR)) {
			prince.playSound(MMSounds.SPELL_SPELL_CAST, 1, 1);
			Vec3d pos = Util.getYawRelativePos(prince.getPos(), 3, prince.getYaw(), prince.getPitch());
			Vec3d motionVec = new Vec3d(pos.x - blessTarget.getX(), pos.y + 2 - blessTarget.getY(),
				pos.z - blessTarget.getZ());
			if (motionVec.length() > 0.1) {
				blessTarget.setVelocity(motionVec);
				blessTarget.velocityModified = true;
				blessTarget.velocityDirty = true;
			}
			MiskatonicMysteriesAPI.grantBlessing((PlayerEntity) blessTarget, MMAffiliations.HASTUR);
		}
	}

	public static boolean canPlayerStartGathering(PlayerEntity player, World world) {
		if (player.hasStatusEffect(StatusEffects.HERO_OF_THE_VILLAGE)) {
			if (MiskatonicMysteriesAPI.getAscensionStage(player) < GATHERING_STAGE) {
				player.sendMessage(new TranslatableText("message.miskatonicmysteries.need_level"), true);
				return false;
			}
			int cultistAmount = world.getEntitiesByClass(HasturCultistEntity.class, player.getBoundingBox().expand(40), (cultist) -> true)
				.size();
			if (cultistAmount < 2) {
				player.sendMessage(new TranslatableText("message.miskatonicmysteries.need_serfs"), true);
				return false;
			}
			if (world.getClosestEntity(TatteredPrinceEntity.class,
				TargetPredicate.createNonAttackable().setBaseMaxDistance(40).ignoreVisibility(), player, player.getX(),
				player.getY(), player.getZ(), player.getBoundingBox().expand(40)) == null) {
				player.sendMessage(new TranslatableText("message.miskatonicmysteries.need_prince"), true);
				return false;
			}
			return true;
		}
		return false;
	}

	public static void holdGoldenGathering(ServerPlayerEntity serverPlayerEntity, int partyPower) {
		if (partyPower >= Party.PARTY_POWER_MAX && MiskatonicMysteriesAPI.levelUp(serverPlayerEntity, SIMULACRUM_STAGE,
			MMAffiliations.HASTUR)) {
			serverPlayerEntity.playSound(MMSounds.SPELL_SPELL_CAST, 1, 1);
			MiskatonicMysteriesAPI.grantBlessing(serverPlayerEntity, MMAffiliations.HASTUR);
		}
	}

	public static void levelSimulacrum(ServerPlayerEntity serverPlayerEntity) {
		if (MiskatonicMysteriesAPI.levelUp(serverPlayerEntity, END_STAGE, MMAffiliations.HASTUR)) {
			serverPlayerEntity.playSound(MMSounds.SPELL_SPELL_CAST, 1, 1);
			MiskatonicMysteriesAPI.grantBlessing(serverPlayerEntity, MMAffiliations.HASTUR);
		}
	}
}
