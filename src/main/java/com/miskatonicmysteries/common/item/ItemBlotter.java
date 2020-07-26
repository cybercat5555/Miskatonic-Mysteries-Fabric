package com.miskatonicmysteries.common.item;

import com.miskatonicmysteries.lib.Constants;
import com.miskatonicmysteries.lib.ModRegistries;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class ItemBlotter extends Item {
    public ItemBlotter() {
        super(new Item.Settings().group(Constants.MM_GROUP));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return TypedActionResult.consume(user.getActiveItem());
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        user.applyStatusEffect(new StatusEffectInstance(ModRegistries.MANIA, 2400, 0));
        stack.decrement(1);
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
}
