package com.miskatonicmysteries.common.block;

import com.miskatonicmysteries.common.block.blockentity.energy.PowerCellBlockEntity;
import com.miskatonicmysteries.common.block.blockentity.energy.ResonatorBlockEntity;
import com.miskatonicmysteries.common.registry.MMParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.EnergySide;

import java.util.Random;

import static net.minecraft.state.property.Properties.*;

public class ResonatorBlock extends HorizontalFacingBlock implements BlockEntityProvider, Waterloggable, Shootable {
    public static final VoxelShape SHAPE = createCuboidShape(2, 0, 2, 14, 24, 14);

    public ResonatorBlock() {
        super(Settings.of(Material.METAL).nonOpaque().requiresTool().strength(1F, 4F)
                .allowsSpawning((state, world, pos, type) -> false).solidBlock((state, world, pos) -> false)
                .suffocates((state, world, pos) -> false)
                .blockVision((state, world, pos) -> false)
                .luminance((state -> state.get(POWERED) ? 10 : 0)));
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(POWERED, false).with(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }


    @Environment(EnvType.CLIENT)
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(POWERED, FACING, WATERLOGGED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        final BlockState state = this.getDefaultState().with(FACING, ctx.getPlayerFacing());
        if (state.contains(WATERLOGGED)) {
            final FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
            final boolean source = fluidState.isIn(FluidTags.WATER) && fluidState.getLevel() == 8;
            return state.with(WATERLOGGED, source);
        }
        return state;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        if (state.contains(WATERLOGGED) && state.get(WATERLOGGED)) {
            world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (hand == Hand.MAIN_HAND && hit.getSide() == state.get(FACING).getOpposite()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity == null) {
                return ActionResult.FAIL;
            }
            if (world.isClient) {
                return ActionResult.SUCCESS;
            }
            world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.2F, state.get(POWERED) ? 0.6F : 0.5F);
            if (state.get(POWERED)) {
                if (player.isSneaking()) {
                    world.setBlockState(pos, state.with(POWERED, false));
                    blockEntity.markDirty();
                }
            } else if (blockEntity instanceof ResonatorBlockEntity && ((ResonatorBlockEntity) blockEntity).getStored(EnergySide.UNKNOWN) > 0) {
                world.setBlockState(pos, state.with(POWERED, true));
                blockEntity.markDirty();
            } else {
                player.sendMessage(new TranslatableText("message.miskatonicmysteries.resonator_needs_energy"), true);
                return ActionResult.FAIL;
            }
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
        boolean powered = state.get(POWERED);
        if (powered) {
            world.addParticle(MMParticles.AMBIENT, pos.getX() + 0.5F + random.nextGaussian() * 2, pos.getY() + 0.5F + random.nextGaussian() * 1.5F, pos.getZ() + 0.5F + random.nextGaussian() * 2, 0.75F, 0, 1);
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.contains(WATERLOGGED) && state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
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
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ResonatorBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (world1, pos, state1, blockEntity) -> ResonatorBlockEntity.tick((ResonatorBlockEntity) blockEntity);
    }

    @Override
    public void onShot(World world, BlockPos pos, LivingEntity shooter) {
        if (!world.isClient && world.getBlockState(pos).get(POWERED)) {
            world.createExplosion(null, pos.getX() + 0.5F, pos.getY() + 0.85F, pos.getZ() + 0.5F, 7.0F, Explosion.DestructionType.DESTROY);
        }
    }
}
