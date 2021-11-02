package com.miskatonicmysteries.common.feature.item.consumable;

import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.api.interfaces.SpellCaster;
import com.miskatonicmysteries.common.registry.MMSpellEffects;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class CirrhosusFleshItem extends Item {

	public static final FoodComponent BAD_FOOD = new FoodComponent.Builder().hunger(8).saturationModifier(1)
		.alwaysEdible()
		.statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 600, 1), 1F)
		.meat()
		.build();

	public CirrhosusFleshItem() {
		super(new Settings().group(Constants.MM_GROUP).food(BAD_FOOD).maxCount(1).rarity(Rarity.UNCOMMON));
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return ItemUsage.consumeHeldItem(world, user, hand);
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if (!world.isClient) {
			Sanity.of(user).ifPresent(sanity -> {
				sanity.addSanityCapExpansion(Constants.Misc.ATE_CIRRHOSUS_FLESH, -25);
				sanity.setSanity(sanity.getSanity() - 25, true);
				sanity.syncSanityData();
			});
			SpellCaster.of(user).ifPresent(caster -> {
				caster.learnEffect(MMSpellEffects.TENTACLES);
				caster.learnEffect(MMSpellEffects.PULSE);
				caster.syncSpellData();
			});
			if (user instanceof ServerPlayerEntity) {
				Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity) user, stack);
				((ServerPlayerEntity) user).incrementStat(Stats.USED.getOrCreateStat(this));
			}
		}
		user.eatFood(world, stack);
		return stack;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.EAT;
	}
}