package com.miskatonicmysteries.common.feature.item;

import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.registry.MMSounds;
import com.miskatonicmysteries.common.util.Constants;
import java.util.Random;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class InfestedWheatItem extends Item {

	public InfestedWheatItem() {
		super(new Item.Settings().group(Constants.MM_GROUP));
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos sourcePos = context.getBlockPos();
		if (world.getBlockState(sourcePos).isSolidBlock(world, sourcePos)) {
			sourcePos = sourcePos.offset(context.getSide());
		}
		Position usePos = context.getPlayer() != null ? context.getPlayer().getPos() : null;
		if (usePos != null && !sourcePos.isWithinDistance(usePos, 3)) {
			return ActionResult.FAIL;
		}
		Random random = world.getRandom();
		if (!world.isClient) {
			world.playSound(sourcePos.getX(), sourcePos.getY(), sourcePos.getZ(), MMSounds.ITEM_INFESTED_WHEAT_USE, SoundCategory.BLOCKS,
				1.0f, 1.0f, false);
			float luck = context.getPlayer() != null ? context.getPlayer().getLuck() : 0F;
			int wheatFound = 0;
			for (BlockPos pos : BlockPos.iterateOutwards(sourcePos, 2, 1, 2)) {
				BlockState checked = world.getBlockState(pos);
				if (checked.getBlock() == Blocks.WHEAT && checked.get(CropBlock.AGE) == 7) {
					wheatFound++;
					if (random.nextFloat() < (0.4F + luck / 4F)) {
						world.setBlockState(pos, MMObjects.INFESTED_WHEAT_CROP.getDefaultState());
					}
				}
				if (wheatFound >= 9) {
					break;
				}
			}
			context.getStack().decrement(1);
		} else {
			if (context.getPlayer() != null) {
				for (int i = 0; i < 10; i++) {
					world.addParticle(new DustParticleEffect(
							new Vec3f(0, MathHelper.nextFloat(random, 0, 0.125F), MathHelper.nextFloat(random, 0, 0.2F)),
							MathHelper.nextFloat(random, 1.5F, 2F)
						),
						sourcePos.getX() + MathHelper.nextGaussian(random, 0, 0.5F),
						sourcePos.getY() + random.nextFloat(),
						sourcePos.getZ() + MathHelper.nextGaussian(random, 0, 0.5F),
						0, 0, 0
					);
				}
			}
		}
		return ActionResult.CONSUME;
	}
}
