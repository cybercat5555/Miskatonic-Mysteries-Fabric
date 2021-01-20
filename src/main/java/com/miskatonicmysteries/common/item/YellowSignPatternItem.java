package com.miskatonicmysteries.common.item;

import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.MMObjects;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPatternItem;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class YellowSignPatternItem extends LoomPatternItem {
    public YellowSignPatternItem() {
        super(MMObjects.YELLOW_SIGN_BANNER, new Item.Settings().group(Constants.MM_GROUP).maxCount(1).rarity(Rarity.UNCOMMON));
    }


    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getPlayer() != null && context.getPlayer().getOffHandStack().getItem().isIn(Constants.Tags.YELLOW_DYE)) {
            ActionResult actionResult = this.place(new ItemPlacementContext(context));
            context.getPlayer().getOffHandStack().decrement(1);
            return !actionResult.isAccepted() && this.isFood() ? this.use(context.getWorld(), context.getPlayer(), context.getHand()).getResult() : actionResult;
        }
        return ActionResult.FAIL;

    }

    public ActionResult place(ItemPlacementContext context) {
        if (!context.canPlace()) {
            return ActionResult.FAIL;
        } else {
            BlockState blockState = this.getPlacementState(context);
            if (blockState == null) {
                return ActionResult.FAIL;
            } else if (!this.place(context, blockState)) {
                return ActionResult.FAIL;
            } else {
                BlockPos blockPos = context.getBlockPos();
                World world = context.getWorld();
                PlayerEntity playerEntity = context.getPlayer();
                ItemStack itemStack = context.getStack();
                BlockState blockState2 = world.getBlockState(blockPos);
                Block block = blockState2.getBlock();
                if (block == blockState.getBlock()) {
                    if (playerEntity instanceof ServerPlayerEntity) {
                        Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity) playerEntity, blockPos, itemStack);
                    }
                }
                BlockSoundGroup blockSoundGroup = blockState2.getSoundGroup();
                world.playSound(playerEntity, blockPos, this.getPlaceSound(blockState2), SoundCategory.BLOCKS, (blockSoundGroup.getVolume() + 1.0F) / 2.0F, blockSoundGroup.getPitch() * 0.8F);
                if (playerEntity == null || !playerEntity.abilities.creativeMode) {
                    itemStack.decrement(1);
                }

                return ActionResult.success(world.isClient);
            }

        }
    }

    protected boolean place(ItemPlacementContext context, BlockState state) {
        return context.getWorld().setBlockState(context.getBlockPos(), state, 11);
    }

    @Nullable
    protected BlockState getPlacementState(ItemPlacementContext context) {
        BlockState blockState = MMObjects.YELLOW_SIGN.getPlacementState(context);
        return blockState != null && this.canPlace(context, blockState) ? blockState : null;
    }

    protected boolean canPlace(ItemPlacementContext context, BlockState state) {
        PlayerEntity playerEntity = context.getPlayer();
        ShapeContext shapeContext = playerEntity == null ? ShapeContext.absent() : ShapeContext.of(playerEntity);
        return state.canPlaceAt(context.getWorld(), context.getBlockPos()) && context.getWorld().canPlace(state, context.getBlockPos(), shapeContext);
    }

    protected SoundEvent getPlaceSound(BlockState state) {
        return state.getSoundGroup().getPlaceSound();
    }
}
