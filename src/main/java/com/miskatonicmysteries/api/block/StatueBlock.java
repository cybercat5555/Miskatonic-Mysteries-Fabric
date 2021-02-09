package com.miskatonicmysteries.api.block;

import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.block.blockentity.StatueBlockEntity;
import com.miskatonicmysteries.common.registry.MMMiscRegistries;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
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
import net.minecraft.text.TranslatableText;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.state.property.Properties.WATERLOGGED;

public class StatueBlock extends Block implements Waterloggable, BlockEntityProvider, Affiliated {
    public static final Map<StatusEffect, Integer> POSITIVE_STATUE_EFFECTS = new HashMap<>();
    public static final Map<StatusEffect, Integer> NEGATIVE_STATUE_EFFECTS = new HashMap<>();

    static {
        POSITIVE_STATUE_EFFECTS.put(StatusEffects.SPEED, 2);
        POSITIVE_STATUE_EFFECTS.put(StatusEffects.REGENERATION, 3);
        POSITIVE_STATUE_EFFECTS.put(StatusEffects.STRENGTH, 4);
        POSITIVE_STATUE_EFFECTS.put(StatusEffects.RESISTANCE, 4);
        POSITIVE_STATUE_EFFECTS.put(StatusEffects.LUCK, 4);

        NEGATIVE_STATUE_EFFECTS.put(MMMiscRegistries.StatusEffects.MANIA, 2);
        NEGATIVE_STATUE_EFFECTS.put(StatusEffects.POISON, 4);
        NEGATIVE_STATUE_EFFECTS.put(StatusEffects.NAUSEA, 3);
        NEGATIVE_STATUE_EFFECTS.put(StatusEffects.UNLUCK, 5);
        NEGATIVE_STATUE_EFFECTS.put(MMMiscRegistries.StatusEffects.BLEED, 4);
        NEGATIVE_STATUE_EFFECTS.put(StatusEffects.WITHER, 6);
        NEGATIVE_STATUE_EFFECTS.put(StatusEffects.HUNGER, 4);
        NEGATIVE_STATUE_EFFECTS.put(StatusEffects.WEAKNESS, 3);
    }

    public static List<StatueBlock> STATUES = new ArrayList<>();
    private Affiliation affiliation;
    private static final VoxelShape SHAPE = createCuboidShape(4, 0, 4, 12, 16, 12);

    private boolean buffed;

    public StatueBlock(Affiliation affiliation, boolean buffed, Settings settings) {
        super(settings.nonOpaque());
        this.affiliation = affiliation;
        setDefaultState(getDefaultState().with(Properties.ROTATION, 0));
        STATUES.add(this);
        this.buffed = buffed;
    }

    public void selectStatusEffects(LivingEntity entity, Affiliated affiliated) {
        boolean buffed = isBuffed();
        if (getAffiliation(false).equals(affiliated.getAffiliation(false))) {
            int duration = (buffed ? 9600 : 4800);
            for (StatusEffect statusEffect : POSITIVE_STATUE_EFFECTS.keySet()) {
                if (entity.world.getRandom().nextInt(POSITIVE_STATUE_EFFECTS.get(statusEffect)) == 0) {
                    entity.addStatusEffect(new StatusEffectInstance(statusEffect, duration, buffed && statusEffect != StatusEffects.REGENERATION ? 1 : 0, true, false, false));
                }
            }
        } else {
            int duration = (buffed ? 1200 : 600);
            for (StatusEffect statusEffect : NEGATIVE_STATUE_EFFECTS.keySet()) {
                if (entity.world.getRandom().nextInt(NEGATIVE_STATUE_EFFECTS.get(statusEffect)) == 0) {
                    entity.addStatusEffect(new StatusEffectInstance(statusEffect, duration, buffed ? 1 : 0, true, true, false));
                }
            }
        }
    }

    public boolean isBuffed() {
        return buffed;
    }

    @Override
    public void appendTooltip(ItemStack stack, @org.jetbrains.annotations.Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        if (stack.hasTag() && stack.getTag().contains((Constants.NBT.BLOCK_ENTITY_TAG))) {
            CompoundTag compoundTag = stack.getSubTag(Constants.NBT.BLOCK_ENTITY_TAG);
            if (compoundTag != null && compoundTag.contains(Constants.NBT.PLAYER_NAME)) {
                tooltip.add(new TranslatableText("tooltip.miskatonicmysteries.created_by", compoundTag.getString(Constants.NBT.PLAYER_NAME)).formatted(Formatting.GRAY));
            }
        }
        super.appendTooltip(stack, world, tooltip, options);
    }

    public static ItemStack setCreator(ItemStack stack, PlayerEntity player) {
        if (player == null) {
            return stack;
        }
        if (!stack.hasTag()) {
            stack.setTag(new CompoundTag());
        }
        CompoundTag blockEntityTag = new CompoundTag();
        blockEntityTag.putString(Constants.NBT.PLAYER_NAME, player.getName().asString());
        blockEntityTag.putUuid(Constants.NBT.PLAYER_UUID, player.getUuid());
        stack.getTag().put(Constants.NBT.BLOCK_ENTITY_TAG, blockEntityTag);
        return stack;
    }

    public static boolean isPlayerMade(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains(Constants.NBT.BLOCK_ENTITY_TAG) && stack.getSubTag(Constants.NBT.BLOCK_ENTITY_TAG).contains(Constants.NBT.PLAYER_NAME);
    }

    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(Properties.ROTATION, rotation.rotate(state.get(Properties.ROTATION), 16));
    }

    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.with(Properties.ROTATION, mirror.mirror(state.get(Properties.ROTATION), 16));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }


    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        final BlockState state = this.getDefaultState().with(Properties.ROTATION, MathHelper.floor((double) (ctx.getPlayerYaw() * 16.0F / 360.0F) + 0.5D) & 15);
        if (state.contains(WATERLOGGED)) {
            final FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
            final boolean source = fluidState.isIn(FluidTags.WATER) && fluidState.getLevel() == 8;
            return state.with(WATERLOGGED, source);
        }
        return state;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(Properties.ROTATION, WATERLOGGED);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        if (state.contains(WATERLOGGED) && state.get(WATERLOGGED)) {
            world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.contains(WATERLOGGED) && state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new StatueBlockEntity();
    }

    @Override
    public Affiliation getAffiliation(boolean apparent) {
        return affiliation;
    }

    @Override
    public boolean isSupernatural() {
        return true;
    }
}
