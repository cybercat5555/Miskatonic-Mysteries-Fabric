package com.miskatonicmysteries.common.item;

import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.ModRegistries;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class IncantationYogItem extends Item {
    public IncantationYogItem() {
        super(new Settings().group(Constants.MM_GROUP));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        //store coordinates of first block in incantation_targets
        user.applyStatusEffect(new StatusEffectInstance(ModRegistries.MANIA, 2400, 0));
        stack.decrement(1);
        return stack;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 20;
    }
}
