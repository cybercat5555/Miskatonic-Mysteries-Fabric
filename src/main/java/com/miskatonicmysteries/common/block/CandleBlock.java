package com.miskatonicmysteries.common.block;

import com.miskatonicmysteries.client.particle.ModParticles;
import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.util.InventoryUtil;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import javax.annotation.Nullable;
import java.util.Random;

import static net.minecraft.state.property.Properties.LIT;
import static net.minecraft.state.property.Properties.WATERLOGGED;

//this is in many ways just an enhanced sea pickle
public class CandleBlock extends Block implements Waterloggable {
    public static IntProperty COUNT = IntProperty.of("count", 1, 4);
    public static final VoxelShape SHAPE_1 = Block.createCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 6.0D, 10.0D);
    public static final VoxelShape SHAPE_2 = Block.createCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 6.0D, 13.0D);
    public static final VoxelShape SHAPE_3 = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 6.0D, 14.0D);
    public static final VoxelShape SHAPE_4 = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 7.0D, 14.0D);

    public CandleBlock() {
        super(Settings.of(Material.WOOL).strength(0.2F, 0.1F).nonOpaque().luminance(state -> state.get(LIT) ? Math.min((int) (Math.sqrt(state.get(COUNT)) * 8), 15) : 0).sounds(BlockSoundGroup.WOOL));
        this.setDefaultState(getDefaultState().with(COUNT, 1).with(LIT, false).with(WATERLOGGED, false));
    }

    @Override
    public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
        if (!state.get(Properties.WATERLOGGED) && fluidState.getFluid() == Fluids.WATER) {
            if (!world.isClient()) {
                world.setBlockState(pos, state.with(Properties.WATERLOGGED, true).with(LIT, false), 3);
                world.getFluidTickScheduler().schedule(pos, fluidState.getFluid(), fluidState.getFluid().getTickRate(world));
            }
            return true;
        }
        return false;
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = ctx.getWorld().getBlockState(ctx.getBlockPos());
        if (blockState.isOf(this)) {
            return blockState.with(COUNT, Math.min(4, blockState.get(COUNT) + 1));
        } else {
            final BlockState state = super.getPlacementState(ctx);
            if (state.contains(Properties.WATERLOGGED)) {
                final FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
                final boolean source = fluidState.isIn(FluidTags.WATER) && fluidState.getLevel() == 8;
                return state.with(Properties.WATERLOGGED, source);
            }
            return state;
        }
    }


    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        if (state.contains(Properties.WATERLOGGED) && state.get(Properties.WATERLOGGED)) {
            world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.getItem() instanceof FlintAndSteelItem && !state.get(LIT) && !state.get(WATERLOGGED)) {
            world.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.4F + 0.8F);
            stack.damage(1, player, (p) -> p.sendToolBreakStatus(hand));
            world.setBlockState(pos, state.with(LIT, true));
            return ActionResult.SUCCESS;
        } else if (stack.isEmpty() && player.isSneaking()) {
            if (state.get(LIT))
                world.setBlockState(pos, state.with(LIT, false));
            else {
                InventoryUtil.giveItem(world, player, new ItemStack(this));
                world.setBlockState(pos, state.get(COUNT) > 1 ? state.with(COUNT, state.get(COUNT) - 1) : Blocks.AIR.getDefaultState());
            }
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(COUNT, LIT, WATERLOGGED);
        super.appendProperties(builder);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return !world.getBlockState(pos.down()).getCollisionShape(world, pos.down()).getFace(Direction.UP).isEmpty();
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        return context.getStack().getItem() == this.asItem() && state.get(COUNT) < 4 || super.canReplace(state, context);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(LIT)) {
            int candles = state.get(COUNT);
            double x = (double) pos.getX() + 0.5D;
            double y = (double) pos.getY() + 0.55D;
            double z = (double) pos.getZ() + 0.5D;
            switch (candles) {
                case 1:
                    ModParticles.spawnCandleParticle(world, x, y, z, 1, false);
                    break;
                case 2:
                    ModParticles.spawnCandleParticle(world, x - 3 * Constants.BLOCK_BIT, y, z - 3 * Constants.BLOCK_BIT, 1, false);
                    ModParticles.spawnCandleParticle(world, x + 2 * Constants.BLOCK_BIT, y - 2 * Constants.BLOCK_BIT, z + 2 * Constants.BLOCK_BIT, 1, false);
                    break;
                case 3:
                    ModParticles.spawnCandleParticle(world, x, y, z + 3 * Constants.BLOCK_BIT, 1, false);
                    ModParticles.spawnCandleParticle(world, x - 4 * Constants.BLOCK_BIT, y - 2 * Constants.BLOCK_BIT, z - 4 * Constants.BLOCK_BIT, 1, false);
                    ModParticles.spawnCandleParticle(world, x + 2 * Constants.BLOCK_BIT, y, z - 2 * Constants.BLOCK_BIT, 1, false);
                    break;
                case 4:
                    ModParticles.spawnCandleParticle(world, x - 4 * Constants.BLOCK_BIT, y, z - 4 * Constants.BLOCK_BIT, 1, false);
                    ModParticles.spawnCandleParticle(world, x + 3 * Constants.BLOCK_BIT, y - 2 * Constants.BLOCK_BIT, z + 4 * Constants.BLOCK_BIT, 1, false);
                    ModParticles.spawnCandleParticle(world, x - 4 * Constants.BLOCK_BIT, y, z + 2 * Constants.BLOCK_BIT, 1, false);
                    ModParticles.spawnCandleParticle(world, x + 3 * Constants.BLOCK_BIT, y, z - 4 * Constants.BLOCK_BIT, 1, false);
                    break;
            }
        }
        super.randomDisplayTick(state, world, pos, random);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch (state.get(COUNT)) {
            case 1:
            default:
                return SHAPE_1;
            case 2:
                return SHAPE_2;
            case 3:
                return SHAPE_3;
            case 4:
                return SHAPE_4;
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.contains(Properties.WATERLOGGED) && state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }
}
