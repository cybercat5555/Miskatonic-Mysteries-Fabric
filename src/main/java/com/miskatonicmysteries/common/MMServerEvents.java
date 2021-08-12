package com.miskatonicmysteries.common;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.interfaces.*;
import com.miskatonicmysteries.common.util.Constants;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class MMServerEvents {
	public static void init() {
		ServerPlayerEvents.COPY_FROM.register(MMServerEvents::copyFromPlayer);
		ServerPlayerEvents.AFTER_RESPAWN.register(MMServerEvents::afterRespawn);
		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(MMServerEvents::afterKilledOtherEntity);
	}

	private static void copyFromPlayer(ServerPlayerEntity oldPlayer, ServerPlayerEntity player, boolean isDead) {
		Sanity.of(oldPlayer).ifPresent(oldSanity -> Sanity.of(player).ifPresent(sanity -> {
			sanity.setSanity(oldSanity.getSanity(), true);
			sanity.getSanityCapExpansions().putAll(oldSanity.getSanityCapExpansions());
			sanity.syncSanityData();
		}));

		SpellCaster.of(oldPlayer).ifPresent(oldCaster -> SpellCaster.of(player).ifPresent(caster -> {
			caster.setMaxSpells(oldCaster.getMaxSpells());
			caster.setPowerPool(oldCaster.getPowerPool());
			caster.getLearnedEffects().addAll(oldCaster.getLearnedEffects());
			caster.getLearnedMediums().addAll(oldCaster.getLearnedMediums());
			caster.getSpells().addAll(oldCaster.getSpells());
			caster.syncSpellData();
		}));

		Ascendant.of(oldPlayer).ifPresent(oldAscendant -> Ascendant.of(player).ifPresent(ascendant -> {
			ascendant.setAscensionStage(oldAscendant.getAscensionStage());
			ascendant.getBlessings().addAll(oldAscendant.getBlessings());
		}));

		MalleableAffiliated.of(oldPlayer).ifPresent(oldAffiliation -> MalleableAffiliated.of(player).ifPresent(affiliation -> {
			affiliation.setAffiliation(oldAffiliation.getAffiliation(false), false);
			affiliation.setAffiliation(oldAffiliation.getAffiliation(true), true);
		}));

		Knowledge.of(oldPlayer).ifPresent(oldKnowledge -> Knowledge.of(player).ifPresent(knowledge -> {
			for (String knowledgeId : oldKnowledge.getKnowledge()) {
				knowledge.addKnowledge(knowledgeId);
			}
			knowledge.syncKnowledge();
		}));
	}

	private static void afterRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
		Sanity.of(newPlayer).ifPresent(Sanity::syncSanityData);
		SpellCaster.of(newPlayer).ifPresent(SpellCaster::syncSpellData);
		Knowledge.of(newPlayer).ifPresent(Knowledge::syncKnowledge);
	}

	private static void afterKilledOtherEntity(ServerWorld world, Entity entity, LivingEntity killedEntity) {
		if (entity instanceof PlayerEntity && killedEntity instanceof EvokerEntity) {
			MiskatonicMysteriesAPI.addKnowledge(Constants.Misc.EVOKER_KNOWLEDGE, (PlayerEntity) entity);
		}
	}
}
