package com.miskatonicmysteries.common;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.interfaces.*;
import com.miskatonicmysteries.common.feature.world.party.MMPartyState;
import com.miskatonicmysteries.common.handler.ascension.HasturAscensionHandler;
import com.miskatonicmysteries.common.util.Constants;
import dev.onyxstudios.cca.api.v3.entity.PlayerSyncCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.block.BellBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public class MMServerEvents {
	public static void init() {
		UseBlockCallback.EVENT.register(MMServerEvents::interactBlock);
		ServerTickEvents.END_WORLD_TICK.register(MMServerEvents::tick);
		ServerPlayerEvents.COPY_FROM.register(MMServerEvents::copyFromPlayer);
		ServerPlayerEvents.AFTER_RESPAWN.register(MMServerEvents::afterRespawn);
		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(MMServerEvents::afterKilledOtherEntity);
		PlayerSyncCallback.EVENT.register(MMServerEvents::onPlayerSync);
	}

	private static void tick(ServerWorld serverWorld) {
		MMPartyState.get(serverWorld).tick();
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
		Ascendant.of(newPlayer).ifPresent(Ascendant::syncBlessingData);
		Knowledge.of(newPlayer).ifPresent(Knowledge::syncKnowledge);
	}

	private static void afterKilledOtherEntity(ServerWorld world, Entity entity, LivingEntity killedEntity) {
		if (entity instanceof PlayerEntity && killedEntity instanceof EvokerEntity) {
			MiskatonicMysteriesAPI.addKnowledge(Constants.Misc.EVOKER_KNOWLEDGE, (PlayerEntity) entity);
		}
	}

	private static ActionResult interactBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
		if (world instanceof ServerWorld serverWorld && world.getBlockState(hitResult.getBlockPos()).getBlock() instanceof BellBlock) {
			if (HasturAscensionHandler.canPlayerStartGathering(player, world) && MMPartyState.get(serverWorld).tryStartParty(serverWorld, hitResult.getBlockPos())) {
				player.removeStatusEffect(StatusEffects.HERO_OF_THE_VILLAGE);
			}
		}
		return ActionResult.PASS;
	}

	private static void onPlayerSync(ServerPlayerEntity player) {
		Sanity.of(player).ifPresent(Sanity::syncSanityData);
		SpellCaster.of(player).ifPresent(SpellCaster::syncSpellData);
		Ascendant.of(player).ifPresent(Ascendant::syncBlessingData);
		Knowledge.of(player).ifPresent(Knowledge::syncKnowledge);
	}
}
