package com.miskatonicmysteries.common.block;

import com.miskatonicmysteries.common.block.blockentity.energy.PowerCellBlockEntity;
import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.MMObjects;
import com.miskatonicmysteries.common.lib.util.MiscUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
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

import java.util.List;

import static net.minecraft.state.property.Properties.*;

public class PowerCellBlock extends HorizontalFacingBlock implements BlockEntityProvider, Waterloggable, Shootable {
    public PowerCellBlock() {
        super(Settings.of(Material.METAL).strength(1F, 4F).nonOpaque().requiresTool()
                .allowsSpawning((state, world, pos, type) -> false).solidBlock((state, world, pos) -> false)
                .suffocates((state, world, pos) -> false)
                .blockVision((state, world, pos) -> false));
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(WATERLOGGED, false).with(UP, false));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        if (stack.hasTag() && stack.getTag().contains(("BlockEntityTag"))) {
            CompoundTag compoundTag = stack.getSubTag("BlockEntityTag");
            if (compoundTag != null && compoundTag.contains(Constants.NBT.ENERGY)) {
                tooltip.add(MiscUtil.createPowerPercentageText(compoundTag.getDouble(Constants.NBT.ENERGY), PowerCellBlockEntity.MAX_STORAGE));
            }
        }
        super.appendTooltip(stack, world, tooltip, options);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        if (world.getBlockEntity(pos) != null) {
            ItemStack stack = getFilledStack();
            stack.getTag().put("BlockEntityTag", world.getBlockEntity(pos).toTag(new CompoundTag()));
            return stack;
        }
        return super.getPickStack(world, pos, state);
    }

    @Environment(EnvType.CLIENT)
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 1.0F;
    }

    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, WATERLOGGED, UP);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity cell = world.getBlockEntity(pos);
            if (cell instanceof PowerCellBlockEntity) {
                EnergySide side = EnergySide.fromMinecraft(hit.getSide());
                player.sendMessage(MiscUtil.createPowerPercentageText(((PowerCellBlockEntity) cell).getStored(side), PowerCellBlockEntity.MAX_STORAGE), true);
            }
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    private BlockState changeConnectionState(BlockState state, WorldView world, BlockPos pos) {
        if (world.getBlockEntity(pos) != null) {
            world.getBlockEntity(pos).markDirty();
        }
        return state.with(UP, world.getBlockEntity(pos.up()) instanceof EnergyHolder && ((EnergyHolder) world.getBlockEntity(pos.up())).getTier() == EnergyTier.LOW);
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
        changeConnectionState(state, world, pos);
        if (state.contains(WATERLOGGED) && state.get(WATERLOGGED)) {
            world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
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

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new PowerCellBlockEntity();
    }

    @Override
    public void onShot(World world, BlockPos pos, LivingEntity shooter) {
        if (!world.isClient && world.getBlockEntity(pos) instanceof EnergyStorage && ((EnergyStorage) world.getBlockEntity(pos)).getStored(EnergySide.UNKNOWN) > 64) {
            world.createExplosion(null, pos.getX() + 0.5F, pos.getY() + 0.85F, pos.getZ() + 0.5F, 1F, Explosion.DestructionType.DESTROY);
        }
    }

    public static ItemStack getFilledStack() {
        ItemStack stack = new ItemStack(MMObjects.POWER_CELL);
        CompoundTag tag = new CompoundTag();
        CompoundTag blockEntityTag = new CompoundTag();
        blockEntityTag.putDouble(Constants.NBT.ENERGY, PowerCellBlockEntity.MAX_STORAGE);
        tag.put("BlockEntityTag", blockEntityTag);
        stack.setTag(tag);
        return stack;
    }
}
