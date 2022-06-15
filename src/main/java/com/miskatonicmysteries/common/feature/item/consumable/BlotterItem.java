package com.miskatonicmysteries.common.feature.item.consumable;

import com.miskatonicmysteries.api.interfaces.SpellCaster;
import com.miskatonicmysteries.api.interfaces.VillagerPartyDrug;
import com.miskatonicmysteries.common.registry.MMSpellEffects;
import com.miskatonicmysteries.common.registry.MMStatusEffects;
import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class BlotterItem extends Item implements VillagerPartyDrug {

	private boolean potent;

	public BlotterItem(boolean potent) {
		super(new Settings().group(Constants.MM_GROUP));
		this.potent = potent;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return ItemUsage.consumeHeldItem(world, user, hand);
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if (!world.isClient) {
			if (!potent) {
				user.addStatusEffect(new StatusEffectInstance(MMStatusEffects.MANIA, 2400, 0));
			} else {
				user.addStatusEffect(new StatusEffectInstance(MMStatusEffects.OTHERVIBES, 2400, 0));
			}
			stack.decrement(1);
			if (user instanceof ServerPlayerEntity) {
				Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity) user, stack);
				((ServerPlayerEntity) user).incrementStat(Stats.USED.getOrCreateStat(this));
			}
			SpellCaster.of(user).ifPresent(caster -> {
				if (!potent) {
					caster.learnEffect(MMSpellEffects.MANIA);
					caster.syncSpellData();
				}

			});
		}
		return stack;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.EAT;
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 30;
	}


	@Override
	public StatusEffectInstance getStatusEffect(VillagerEntity villager) {
		return new StatusEffectInstance(MMStatusEffects.MANIA, 600, 0);
	}
}
