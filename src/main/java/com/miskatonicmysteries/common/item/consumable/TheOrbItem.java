package com.miskatonicmysteries.common.item.consumable;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.api.interfaces.SpellCaster;
import com.miskatonicmysteries.common.registry.MMSpellMediums;
import com.miskatonicmysteries.common.registry.MMStatusEffects;
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

public class TheOrbItem extends Item {
    public static final FoodComponent ORB_FOOD = new FoodComponent.Builder().hunger(0).saturationModifier(0)
            .alwaysEdible()
            .statusEffect(new StatusEffectInstance(MMStatusEffects.MANIA, 1200, 1), 0.9F)
            .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 1200, 1), 0.5F)
            .statusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 2400, 1), 0.4F)
            .statusEffect(new StatusEffectInstance(StatusEffects.WITHER, 600, 0), 0.25F)
            .meat() //because the Orb was not cursed enough yet
            .build();

    public TheOrbItem() {
        super(new Settings().group(Constants.MM_GROUP).food(ORB_FOOD).maxCount(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient) {
            Sanity.of(user).ifPresent(sanity -> {
                sanity.addSanityCapExpansion(Constants.Misc.ATE_ORB_EXTENSION, -50);
                sanity.setSanity(sanity.getSanity() - 25, true);
                sanity.syncSanityData();
            });
            SpellCaster.of(user).ifPresent(caster -> {
                caster.learnMedium(MMSpellMediums.PROJECTILE);
                MiskatonicMysteriesAPI.guaranteeSpellPower(3, caster);
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

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 60;
    }
}