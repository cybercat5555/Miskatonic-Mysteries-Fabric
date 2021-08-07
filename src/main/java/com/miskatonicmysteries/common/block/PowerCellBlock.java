package com.miskatonicmysteries.common.block;

import com.miskatonicmysteries.common.block.blockentity.energy.PowerCellBlockEntity;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.Util;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.tag.FluidTags;
import net.minecraft.text.Text;
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
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.EnergyHolder;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyStorage;
import team.reborn.energy.EnergyTier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.state.property.Properties.*;

public class PowerCellBlock extends HorizontalFacingBlock implements BlockEntityProvider, Waterloggable, Shootable {
    public static final VoxelShape OUTLINE_SHAPE = createCuboidShape(1, 0, 1, 15, 15, 15);

    public static final Map<Direction, Property<Boolean>> DIRECTION_PROPERTY_MAP = new HashMap<>();

    static {
        DIRECTION_PROPERTY_MAP.put(Direction.NORTH, NORTH);
        DIRECTION_PROPERTY_MAP.put(Direction.EAST, EAST);
        DIRECTION_PROPERTY_MAP.put(Direction.SOUTH, SOUTH);
        DIRECTION_PROPERTY_MAP.put(Direction.WEST, WEST);
    }

    public PowerCellBlock() {
        super(Settings.of(Material.METAL).strength(2F, 4F).nonOpaque().requiresTool()
                .allowsSpawning((state, world, pos, type) -> false).solidBlock((state, world, pos) -> false)
                .suffocates((state, world, pos) -> false)
                .blockVision((state, world, pos) -> false));
        setDefaultState(getStateManager().getDefaultState().with(WATERLOGGED, false).with(FACING, Direction.NORTH).with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false));
    }

    public static ItemStack getFilledStack() {
        ItemStack stack = new ItemStack(MMObjects.POWER_CELL);
        NbtCompound tag = new NbtCompound();
        NbtCompound blockEntityTag = new NbtCompound();
        blockEntityTag.putDouble(Constants.NBT.ENERGY, PowerCellBlockEntity.MAX_STORAGE);
        tag.put(Constants.NBT.BLOCK_ENTITY_TAG, blockEntityTag);
        stack.setTag(tag);
        return stack;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return OUTLINE_SHAPE;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        if (stack.hasTag() && stack.getTag().contains((Constants.NBT.BLOCK_ENTITY_TAG))) {
            NbtCompound compoundTag = stack.getSubTag(Constants.NBT.BLOCK_ENTITY_TAG);
            if (compoundTag != null && compoundTag.contains(Constants.NBT.ENERGY)) {
                tooltip.add(Util.createPowerPercentageText(compoundTag.getDouble(Constants.NBT.ENERGY), PowerCellBlockEntity.MAX_STORAGE));
            }
        }
        super.appendTooltip(stack, world, tooltip, options);
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return world.getBlockEntity(pos) instanceof PowerCellBlockEntity ? (int) (16 * ((PowerCellBlockEntity) world.getBlockEntity(pos)).getStored(EnergySide.UNKNOWN) / PowerCellBlockEntity.MAX_STORAGE) : 0;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            world.updateComparators(pos, state.getBlock());
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        if (world.getBlockEntity(pos) != null) {
            ItemStack stack = getFilledStack();
            stack.getTag().put(Constants.NBT.BLOCK_ENTITY_TAG, world.getBlockEntity(pos).writeNbt(new NbtCompound()));
            return stack;
        }
        return super.getPickStack(world, pos, state);
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
        builder.add(WATERLOGGED, FACING, NORTH, EAST, SOUTH, WEST);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity cell = world.getBlockEntity(pos);
            if (cell instanceof PowerCellBlockEntity) {
                EnergySide side = EnergySide.fromMinecraft(hit.getSide());
                player.sendMessage(Util.createPowerPercentageText(((PowerCellBlockEntity) cell).getStored(side), PowerCellBlockEntity.MAX_STORAGE), true);
            }
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    private BlockState changeConnectionState(BlockState state, WorldView world, BlockPos pos) {
        if (world.getBlockEntity(pos) != null) {
            world.getBlockEntity(pos).markDirty();
        }
        for (Direction direction : DIRECTION_PROPERTY_MAP.keySet()) {
            BlockPos offsetPos = pos.offset(direction);
            if (world.getBlockEntity(offsetPos) instanceof EnergyHolder
                    && ((EnergyHolder) world.getBlockEntity(offsetPos)).getTier() == EnergyTier.LOW
                    && ((EnergyHolder) world.getBlockEntity(offsetPos)).getMaxInput(EnergySide.fromMinecraft(direction)) > 0) {
                state = state.with(DIRECTION_PROPERTY_MAP.get(direction), true);
            } else {
                state = state.with(DIRECTION_PROPERTY_MAP.get(direction), false);
            }
        }
        return state;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        final BlockState state = changeConnectionState(this.getDefaultState().with(FACING, ctx.getPlayerFacing()), ctx.getWorld(), ctx.getBlockPos());
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

        return changeConnectionState(state, world, pos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.contains(WATERLOGGED) && state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
        if (!state.get(Properties.WATERLOGGED) && fluidState.getFluid() == Fluids.WATER) {
            if (!world.isClient()) {
                world.setBlockState(pos, state.with(Properties.WATERLOGGED, true), 3);
                world.getFluidTickScheduler().schedule(pos, fluidState.getFluid(), fluidState.getFluid().getTickRate(world));
            }
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PowerCellBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (world1, pos, state1, blockEntity) -> PowerCellBlockEntity.tick((PowerCellBlockEntity) blockEntity);
    }

    @Override
    public void onShot(World world, BlockPos pos, LivingEntity shooter) {
        if (!world.isClient && world.getBlockEntity(pos) instanceof EnergyStorage && ((EnergyStorage) world.getBlockEntity(pos)).getStored(EnergySide.UNKNOWN) > 64) {
            world.createExplosion(null, pos.getX() + 0.5F, pos.getY() + 0.85F, pos.getZ() + 0.5F, 1F, Explosion.DestructionType.DESTROY);
        }
    }
}
