package com.miskatonicmysteries.common;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.block.StatueBlock;
import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.interfaces.BiomeAffected;
import com.miskatonicmysteries.api.interfaces.Knowledge;
import com.miskatonicmysteries.api.interfaces.MalleableAffiliated;
import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.api.interfaces.SpellCaster;
import com.miskatonicmysteries.common.feature.effect.LazarusStatusEffect;
import com.miskatonicmysteries.common.feature.entity.HallucinationEntity;
import com.miskatonicmysteries.common.feature.entity.ProtagonistEntity;
import com.miskatonicmysteries.common.feature.world.MMDimensionalWorldState;
import com.miskatonicmysteries.common.feature.world.biome.HasturBiomeEffect;
import com.miskatonicmysteries.common.feature.world.party.MMPartyState;
import com.miskatonicmysteries.common.handler.ascension.HasturAscensionHandler;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.registry.MMSpellEffects;
import com.miskatonicmysteries.common.registry.MMSpellMediums;
import com.miskatonicmysteries.common.registry.MMWorld;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.InventoryUtil;
import dev.onyxstudios.cca.api.v3.entity.PlayerSyncCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BellBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class MMServerEvents {

	public static void init() {
		UseBlockCallback.EVENT.register(MMServerEvents::interactBlock);
		ServerTickEvents.END_WORLD_TICK.register(MMServerEvents::tick);
		ServerPlayerEvents.COPY_FROM.register(MMServerEvents::copyFromPlayer);
		ServerPlayerEvents.AFTER_RESPAWN.register(MMServerEvents::afterRespawn);
		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(MMServerEvents::afterKilledOtherEntity);
		PlayerSyncCallback.EVENT.register(MMServerEvents::onPlayerSync);
		EntitySleepEvents.STOP_SLEEPING.register(MMServerEvents::onStopSleeping);
	}

	private static void tick(ServerWorld serverWorld) {
		MMPartyState.get(serverWorld).tick();
		MMDimensionalWorldState.get(serverWorld).tick(serverWorld);
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

		MalleableAffiliated.of(oldPlayer)
			.ifPresent(oldAffiliation -> MalleableAffiliated.of(player).ifPresent(affiliation -> {
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
			SpellCaster.of(entity).ifPresent(spellCaster -> {
				if (!spellCaster.getLearnedEffects().contains(MMSpellEffects.HARROWS)) {
					spellCaster.learnEffect(MMSpellEffects.HARROWS);
					spellCaster.syncSpellData();
				}
			});
		}
	}

	private static ActionResult interactBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
		if (world instanceof ServerWorld serverWorld && world.getBlockState(hitResult.getBlockPos())
			.getBlock() instanceof BellBlock) {
			if (HasturAscensionHandler.canPlayerStartGathering(player, world) && MMPartyState.get(serverWorld)
				.tryStartParty(serverWorld, hitResult.getBlockPos())) {
				player.removeStatusEffect(StatusEffects.HERO_OF_THE_VILLAGE);
			}
		}
		return ActionResult.PASS;
	}

	private static void onPlayerSync(ServerPlayerEntity player) {
		Sanity.of(player).ifPresent(Sanity::syncSanityData);
		SpellCaster.of(player).ifPresent(SpellCaster::syncSpellData);
		Knowledge.of(player).ifPresent(Knowledge::syncKnowledge);
	}

	public static void playerDamagePre(PlayerEntity player, DamageSource source, float amount,
		CallbackInfoReturnable<Boolean> infoReturnable) {
		if (source
			.getAttacker() instanceof ProtagonistEntity && !(source instanceof Constants.DamageSources.ProtagonistDamageSource)) {
			infoReturnable.setReturnValue(player
				.damage(new Constants.DamageSources.ProtagonistDamageSource(source.getAttacker()), amount));
			return;
		}
		if (source.getAttacker() instanceof HallucinationEntity && source != Constants.DamageSources.INSANITY) {
			infoReturnable.setReturnValue(player.damage(Constants.DamageSources.INSANITY, amount));
			return;
		}
		if (source == DamageSource.LIGHTNING_BOLT) {
			SpellCaster.of(player).ifPresent(spellCaster -> {
				if (!spellCaster.getLearnedMediums().contains(MMSpellMediums.BOLT)) {
					spellCaster.learnMedium(MMSpellMediums.BOLT);
					spellCaster.syncSpellData();
				}
			});
			return;
		}

		if (source.isMagic() && source.getAttacker() instanceof GuardianEntity) {
			SpellCaster.of(player).ifPresent(spellCaster -> {
				if (!spellCaster.getLearnedMediums().contains(MMSpellMediums.VISION)) {
					spellCaster.learnMedium(MMSpellMediums.VISION);
					spellCaster.syncSpellData();
				}
			});
		}
	}

	public static boolean playerDamageDeath(PlayerEntity player, DamageSource source, float amount,
		CallbackInfoReturnable<Boolean> infoReturnable) {
		if (player.isDead() && !source.isOutOfWorld()) {
			if (InventoryUtil.getSlotForItemInHotbar(player, MMObjects.RE_AGENT_SYRINGE) >= 0) {
				player.getInventory().getStack(InventoryUtil.getSlotForItemInHotbar(player,
					MMObjects.RE_AGENT_SYRINGE)).decrement(1);
				if (LazarusStatusEffect.revive(player)) {
					infoReturnable.setReturnValue(false);
					return false;
				}
			} else if (source instanceof Constants.DamageSources.ProtagonistDamageSource) {
				MiskatonicMysteriesAPI.resetProgress(player);
				if (source.getSource() instanceof ProtagonistEntity) {
					((ProtagonistEntity) source.getAttacker()).removeAfterTargetKill();
				}
				return true;
			}
		}
		return player.isDead();
	}

	private static void onStopSleeping(LivingEntity entity, BlockPos sleepingPos) {
		World world = entity.world;
		if (!world.isClient && entity instanceof PlayerEntity p && p.canResetTimeBySleeping ()) {
			if (p instanceof BiomeAffected affected && affected.getCurrentBiomeEffect() == MMWorld.HASTUR_BIOME_EFFECT) {
				HasturBiomeEffect.onWakeUp(p, sleepingPos);
			}
			if (world.random.nextFloat() < MiskatonicMysteries.config.entities.statueEffectChance) {
				Iterable<BlockPos> positions = BlockPos.iterateOutwards(entity.getBlockPos(), 10, 10, 10);
				for (BlockPos position : positions) {
					if (world.getBlockState(position).getBlock() instanceof StatueBlock) {
						((StatueBlock) world.getBlockState(position).getBlock())
							.selectStatusEffects(p, (Affiliated) p);
						break;
					}
				}
			}
		}
	}
}
