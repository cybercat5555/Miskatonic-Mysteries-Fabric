package com.miskatonicmysteries.common.feature.item.consumable;

import com.miskatonicmysteries.common.feature.block.blockentity.energy.PowerCellBlockEntity;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.reborn.energy.api.EnergyStorage;

public class ChemicalFuelItem extends Item {

	public static final DispenserBehavior FUEL_DISPENSER_BEHAVIOR = new ItemDispenserBehavior() {
		private final ItemDispenserBehavior defaultBehavior = new ItemDispenserBehavior();

		protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
			BlockPos pos = pointer.getPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
			if (pointer.getWorld().getBlockEntity(pos) instanceof PowerCellBlockEntity cell) {
				EnergyStorage storage = cell.energyStorage.getSideStorage(null);
				if (storage.getAmount() < storage.getCapacity()) {
					cell.energyStorage.amount = Math.min(cell.energyStorage.getCapacity(), cell.energyStorage.amount + cell.energyStorage.getCapacity() / 4);
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
		if (context.getWorld().getBlockEntity(context.getBlockPos()) instanceof PowerCellBlockEntity cell) {
			EnergyStorage storage = cell.energyStorage.getSideStorage(null);
			if (storage.getAmount() < storage.getCapacity()) {
				cell.energyStorage.amount = Math.min(cell.energyStorage.getCapacity(), cell.energyStorage.amount + cell.energyStorage.getCapacity() / 4);
				context.getPlayer().getStackInHand(context.getHand()).decrement(1);
				ItemStack itemStack = new ItemStack(Items.GLASS_BOTTLE);
				if (context.getPlayer().getStackInHand(context.getHand()).isEmpty()) {
					context.getPlayer().setStackInHand(context.getHand(), itemStack);
				} else {
					if (!context.getPlayer().getInventory().insertStack(itemStack)) {
						context.getPlayer().dropItem(itemStack, false);
					}
				}
				context.getPlayer().swingHand(context.getHand());
				return ActionResult.CONSUME;
			}
		}
		return ActionResult.PASS;
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if (!world.isClient) {
			user.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 300, 0));
			user.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0));
			stack.decrement(1);
			if (user instanceof ServerPlayerEntity sp) {
				Criteria.CONSUME_ITEM.trigger(sp, stack);
				sp.incrementStat(Stats.USED.getOrCreateStat(this));
			}
		}
		if (stack.isEmpty()) {
			return new ItemStack(Items.GLASS_BOTTLE);
		} else {
			if (user instanceof PlayerEntity player && !player.isCreative()) {
				ItemStack itemStack = new ItemStack(Items.GLASS_BOTTLE);
				if (!player.getInventory().insertStack(itemStack)) {
					player.dropItem(itemStack, false);
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
