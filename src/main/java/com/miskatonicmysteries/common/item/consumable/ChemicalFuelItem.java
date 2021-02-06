package com.miskatonicmysteries.common.item.consumable;

import com.miskatonicmysteries.common.block.blockentity.energy.PowerCellBlockEntity;
import com.miskatonicmysteries.common.lib.Constants;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergySide;

public class ChemicalFuelItem extends Item {
    public static final DispenserBehavior FUEL_DISPENSER_BEHAVIOR = new ItemDispenserBehavior() {
        private final ItemDispenserBehavior defaultBehavior = new ItemDispenserBehavior();

        protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            BlockPos pos = pointer.getBlockPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
            if (pointer.getWorld().getBlockEntity(pos) instanceof PowerCellBlockEntity) {
                PowerCellBlockEntity cell = (PowerCellBlockEntity) pointer.getWorld().getBlockEntity(pos);
                Energy.of(cell).set(cell.getStored(EnergySide.UNKNOWN) + cell.getMaxStoredPower() / 4F);
                Item remainder = stack.getItem().getRecipeRemainder();
                stack.decrement(1);
                if (stack.isEmpty()) {
                    return new ItemStack(remainder);
                } else {
                    if (((DispenserBlockEntity) pointer.getBlockEntity()).addToFirstFreeSlot(new ItemStack(remainder)) < 0) {
                        defaultBehavior.dispense(pointer, new ItemStack(remainder));
                    }
                    return stack;
                }
            }
            return super.dispenseSilently(pointer, stack);
        }
    };

    public ChemicalFuelItem() {
        super(new Settings().group(Constants.MM_GROUP).recipeRemainder(Items.GLASS_BOTTLE));
        DispenserBlock.registerBehavior(this, FUEL_DISPENSER_BEHAVIOR);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().getBlockEntity(context.getBlockPos()) instanceof PowerCellBlockEntity) {
            PowerCellBlockEntity cell = (PowerCellBlockEntity) context.getWorld().getBlockEntity(context.getBlockPos());
            Energy.of(cell).set(cell.getStored(EnergySide.UNKNOWN) + cell.getMaxStoredPower() / 4F);
            context.getPlayer().getStackInHand(context.getHand()).decrement(1);
            ItemStack itemStack = new ItemStack(Items.GLASS_BOTTLE);
            if (context.getPlayer().getStackInHand(context.getHand()).isEmpty()) {
                context.getPlayer().setStackInHand(context.getHand(), itemStack);
            } else {
                if (!context.getPlayer().inventory.insertStack(itemStack)) {
                    context.getPlayer().dropItem(itemStack, false);
                }
            }
            context.getPlayer().swingHand(context.getHand());
            return ActionResult.CONSUME;
        }
        return ActionResult.PASS;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        user.applyStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 300, 0));
        user.applyStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0));
        stack.decrement(1);
        if (user instanceof ServerPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity) user, stack);
            ((ServerPlayerEntity) user).incrementStat(Stats.USED.getOrCreateStat(this));
        }
        if (stack.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        } else {
            if (user instanceof PlayerEntity && !((PlayerEntity) user).abilities.creativeMode) {
                ItemStack itemStack = new ItemStack(Items.GLASS_BOTTLE);
                PlayerEntity playerEntity = (PlayerEntity) user;
                if (!playerEntity.inventory.insertStack(itemStack)) {
                    playerEntity.dropItem(itemStack, false);
                }
            }

            return stack;
        }
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 32;
    }
}
