package com.miskatonicmysteries.mixin.villagers;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.interfaces.Ascendant;
import com.miskatonicmysteries.api.interfaces.BiomeAffected;
import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.api.interfaces.VillagerPartyDrug;
import com.miskatonicmysteries.common.MMMidnightLibConfig;
import com.miskatonicmysteries.common.feature.entity.HasturCultistEntity;
import com.miskatonicmysteries.common.feature.world.biome.HasturBiomeEffect;
import com.miskatonicmysteries.common.feature.world.party.MMPartyState;
import com.miskatonicmysteries.common.feature.world.party.Party;
import com.miskatonicmysteries.common.handler.InsanityHandler;
import com.miskatonicmysteries.common.registry.MMBlessings;
import com.miskatonicmysteries.common.registry.MMWorld;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityInteraction;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.village.VillageGossipType;
import net.minecraft.village.VillagerGossips;
import net.minecraft.world.World;

import java.util.Optional;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin extends MerchantEntity {

	@Shadow
	@Final
	private VillagerGossips gossip;

	private VillagerEntityMixin(EntityType<? extends MerchantEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "onInteractionWith", at = @At("HEAD"), cancellable = true)
	private void onInteractionWith(EntityInteraction interaction, Entity entity, CallbackInfo ci) {
		Optional<Ascendant> optionalAscendant = Ascendant.of(entity);
		if (optionalAscendant.isPresent() && MiskatonicMysteriesAPI.hasBlessing(optionalAscendant.get(),
																				MMBlessings.CHARMING_PERSONALITY)) {
			if (interaction == EntityInteraction.ZOMBIE_VILLAGER_CURED) {
				this.gossip.startGossip(entity.getUuid(), VillageGossipType.MAJOR_POSITIVE, 40);
				this.gossip.startGossip(entity.getUuid(), VillageGossipType.MINOR_POSITIVE, 60);
			} else if (interaction == EntityInteraction.TRADE) {
				this.gossip.startGossip(entity.getUuid(), VillageGossipType.TRADING, 6);
				if (entity instanceof LivingEntity) {
					((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 2400, 0
						, true, false, false));
					((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 2400, 0
						, true, false, false));
					((LivingEntity) entity).heal(5F);
				}
			}
			//completely ignore bad interactions
			ci.cancel();
		}
	}

	@Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
	private void playerInteract(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		if (InsanityHandler.calculateSanityFactor(Sanity.of(player)) < MMMidnightLibConfig.villagerStopTradingPercentage) {
			sayNo();
			cir.setReturnValue(ActionResult.success(player.world.isClient));
		}

		ItemStack item = player.getStackInHand(hand);
		VillagerEntity $this = (VillagerEntity) (Object) this;
		if (item.getItem() instanceof VillagerPartyDrug drug) {
			if (drug.canDrug($this)) {
				if (world instanceof ServerWorld s) {
					Party party = MMPartyState.get(s).getParty(getBlockPos());
					if (party != null) {
						addStatusEffect(drug.getStatusEffect($this));
						party.addPartyPower(Party.DRUGS_BONUS);
						item.decrement(1);
						this.playSound(this.getTradingSound(true), this.getSoundVolume(), this.getSoundPitch());
						world.sendEntityStatus(this, (byte) 14);
					}
				}
				cir.setReturnValue(ActionResult.CONSUME);
			} else {
				sayNo();
			}
		}
	}

	@Inject(method = "onStruckByLightning", at = @At("HEAD"), cancellable = true)
	private void onStruckByLightning(ServerWorld world, LightningEntity lightning, CallbackInfo ci) {
		if(HasturCultistEntity.onVillagerStruckByLightning((VillagerEntity) (Object) this, world)) {
			releaseAllTickets();
			discard();
			ci.cancel();
		}
	}

	@Shadow
	protected abstract void sayNo();

	@Shadow protected abstract void releaseAllTickets();

	@Inject(method = "prepareOffersFor", at = @At("TAIL"))
	private void prepareOffersFor(PlayerEntity player, CallbackInfo ci) {
		if (this instanceof BiomeAffected affected && affected.getCurrentBiomeEffect() == MMWorld.HASTUR_BIOME_EFFECT) {
			HasturBiomeEffect.modifyVillagerOffers((VillagerEntity) (Object) this, player, getOffers());
		}
	}
}
