package com.miskatonicmysteries.common.feature.item;

import com.miskatonicmysteries.common.feature.entity.painting.HomeyPaintingEntity;
import com.miskatonicmysteries.common.feature.entity.painting.MagicPaintingEntity;
import com.miskatonicmysteries.common.feature.entity.painting.ManosPaintingEntity;
import com.miskatonicmysteries.common.feature.entity.painting.PulsePaintingEntity;
import com.miskatonicmysteries.common.feature.entity.painting.WallPaintingEntity;
import com.miskatonicmysteries.common.registry.MMEntities.PaintingMotives;
import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.painting.PaintingMotive;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

public class EnchantedCanvasItem extends Item {

	private final List<PaintingMotive> motives = new ArrayList<>();

	public EnchantedCanvasItem() {
		super(new FabricItemSettings().rarity(Rarity.UNCOMMON).group(Constants.MM_GROUP));
		motives.add(PaintingMotives.GUARDIAN);
		motives.add(PaintingMotives.SHINING_GATES);
		motives.add(PaintingMotives.BLACK_STAR);
		motives.add(PaintingMotives.SOUP_TIME);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos blockPos = context.getBlockPos();
		Direction direction = context.getSide();
		BlockPos attachmentPos = blockPos.offset(direction);
		PlayerEntity player = context.getPlayer();
		ItemStack itemStack = context.getStack();
		if (player == null || !canPlaceOn(player, direction, itemStack, attachmentPos)) {
			return ActionResult.FAIL;
		}
		if (!world.isClient) {
			ArrayList<PaintingMotive> choices = Lists.newArrayList();
			for (PaintingMotive motive : motives) {
				MagicPaintingEntity entity = getEntityFromMotive(world, motive, attachmentPos, direction, player);
				if (entity.canStayAttached()) {
					choices.add(motive);
				}
			}
			if (!choices.isEmpty()) {
				PaintingMotive motive = choices.get(player.getRandom().nextInt(choices.size()));
				MagicPaintingEntity entity = getEntityFromMotive(world, motive, attachmentPos, direction, player);
				NbtCompound nbtCompound = itemStack.getNbt();
				if (nbtCompound != null) {
					EntityType.loadFromEntityNbt(world, player, entity, nbtCompound);
				}
				entity.onPlace();
				world.emitGameEvent(player, GameEvent.ENTITY_PLACE, blockPos);
				world.spawnEntity(entity);
				itemStack.decrement(1);
				return ActionResult.CONSUME;
			}
			return ActionResult.FAIL;
		}
		return ActionResult.CONSUME;
	}

	private MagicPaintingEntity getEntityFromMotive(World world, PaintingMotive motive, BlockPos pos, Direction direction, PlayerEntity player) {
		if (motive == PaintingMotives.SOUP_TIME) {
			return new HomeyPaintingEntity(world, pos, direction, player.getUuid());
		}else if (motive == PaintingMotives.BLACK_STAR) {
			return new PulsePaintingEntity(world, pos, direction, player.getUuid());
		}else if (motive == PaintingMotives.SHINING_GATES) {
			return new WallPaintingEntity(world, pos, direction, player.getUuid());
		}else {
			return new ManosPaintingEntity(world, pos, direction, player.getUuid());
		}
	}

	protected boolean canPlaceOn(PlayerEntity player, Direction side, ItemStack stack, BlockPos pos) {
		return !side.getAxis().isVertical() && player.canPlaceOn(pos, side, stack);
	}
}
