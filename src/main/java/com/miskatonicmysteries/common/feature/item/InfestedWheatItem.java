package com.miskatonicmysteries.common.feature.item;

import com.miskatonicmysteries.common.feature.block.InfestedWheatCropBlock;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.InfestWheatPacket;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

import java.util.Random;

import javax.annotation.Nullable;

public class InfestedWheatItem extends Item {

	public InfestedWheatItem() {
		super(new Item.Settings().group(Constants.MM_GROUP));
		DispenserBlock.registerBehavior(this, new FallibleItemDispenserBehavior() {
			@Override
			protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
				this.setSuccess(true);
				ServerWorld world = pointer.getWorld();
				BlockPos blockPos = pointer.getPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
				if (!infestWheat(null, stack, world, blockPos, world.random)) {
					setSuccess(false);
				}
				return stack;
			}
		});
	}

	private boolean infestWheat(@Nullable PlayerEntity playerEntity, ItemStack stack, World world, BlockPos sourcePos, Random random) {
		float luck = playerEntity != null ? playerEntity.getLuck() : 0F;
		int wheatFound = 0;
		for (BlockPos pos : BlockPos.iterateOutwards(sourcePos, 2, 1, 2)) {
			BlockState checked = world.getBlockState(pos);
			if (checked.getBlock() == Blocks.WHEAT && checked.get(CropBlock.AGE) >= 6) {
				wheatFound++;
				if (random.nextFloat() < (0.4F + luck / 4F)) {
					world.setBlockState(pos, MMObjects.INFESTED_WHEAT_CROP.getDefaultState()
						.with(InfestedWheatCropBlock.AGE, checked.get(CropBlock.AGE) - 6));
				}
			}
			if (wheatFound >= 9) {
				break;
			}
		}
		stack.decrement(1);
		if (wheatFound > 0) {
			InfestWheatPacket.send((ServerWorld) world, sourcePos);
			return true;
		}
		return false;
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		DispenserBehavior.registerDefaults();
		World world = context.getWorld();
		BlockPos sourcePos = context.getBlockPos();
		if (!world.isClient) {
			if (world.getBlockState(sourcePos).isSolidBlock(world, sourcePos)) {
				sourcePos = sourcePos.offset(context.getSide());
			}
			Position usePos = context.getPlayer() != null ? context.getPlayer().getPos() : null;
			if (usePos != null && !sourcePos.isWithinDistance(usePos, 3)) {
				return ActionResult.FAIL;
			}
			Random random = world.getRandom();
			infestWheat(context.getPlayer(), context.getStack(), world, sourcePos, random);
		}
		return ActionResult.CONSUME;
	}
}
