package com.miskatonicmysteries.common.feature.world.biome;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.VisionPacket;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMStatusEffects;
import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;

import java.util.List;

public class HasturBiomeEffect extends BiomeEffect {

	public HasturBiomeEffect() {
		super(MMAffiliations.HASTUR);
	}

	public static void modifyVillagerOffers(VillagerEntity villagerEntity, PlayerEntity player, TradeOfferList offers) {
		for (TradeOffer offer : offers) {
			if (offer.getOriginalFirstBuyItem().getCount() > 1) {
				int reduction = (int) Math.floor(
					(MiskatonicMysteriesAPI
						 .getNonNullAffiliation(player, false) == MMAffiliations.HASTUR ?
					 MathHelper.clamp(MiskatonicMysteriesAPI.getAscensionStage(player) * 0.2, 0.2, 0.8) :
					 (MiskatonicMysteriesAPI.getNonNullAffiliation(player, true) == MMAffiliations.HASTUR ? 0.2 : 0.1))
						* (double) offer.getOriginalFirstBuyItem().getCount());
				offer.increaseSpecialPrice(-reduction);
			}
		}
	}

	public static void onWakeUp(PlayerEntity p, BlockPos sleepingPos) {
		if (p instanceof ServerPlayerEntity sp && MiskatonicMysteriesAPI.getNonNullAffiliation(p, false) != MMAffiliations.HASTUR) {
			if (p.getRandom().nextBoolean()) {
				if (p.getRandom().nextBoolean()) {
					VisionPacket.send(sp, new Identifier(Constants.MOD_ID, "hastur_mania_%d".formatted(p.getRandom().nextInt(3))));
				}
				p.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 600, 1));
				p.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 600, 1));
				p.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 100, 0));
			}
		}
	}

	@Override
	public void tickFor(LivingEntity entity) {
		if (entity.age % 20 == 0) {
			if (MiskatonicMysteriesAPI
				.getNonNullAffiliation(entity, false) == getAffiliation(false) || entity instanceof VillagerEntity) {
				entity.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 400, 0, true, false, true));
				entity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 400, 0, true, false, true));
				if (entity instanceof VillagerEntity) {
					entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 400, 0, true, false, true));
				}
			} else if (entity instanceof PlayerEntity || entity instanceof Monster) {
				entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100, 0, true, true, true));
				if (entity instanceof PlayerEntity && entity.getRandom().nextFloat() < 0.2) {
					List<Entity> mindFrickEntities = entity.world.getOtherEntities(entity, entity.getBoundingBox()
						.expand(8, 6, 8), target -> MiskatonicMysteriesAPI
						.getNonNullAffiliation(target, false) == getAffiliation(false) && entity.canSee(target));
					if (mindFrickEntities.size() > 0) {
						entity.addStatusEffect(new StatusEffectInstance(MMStatusEffects.MANIA, mindFrickEntities
																								   .size() > 2 ? 400 : 200,
																		mindFrickEntities.size() > 2 ? 1 : 0));
					}
				} else if (entity instanceof ZombieVillagerEntity && entity.getRandom().nextFloat() < 0.2) {
					List<Entity> nearbyHasturEntities = entity.world.getOtherEntities(entity, entity.getBoundingBox()
						.expand(3, 1, 3), target -> MiskatonicMysteriesAPI
						.getNonNullAffiliation(target, false) == getAffiliation(false));
					if (nearbyHasturEntities.size() > 0) {
						entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 1200, 1));
						entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 1200, 0));
					}
				}
			}
		}
	}
}
