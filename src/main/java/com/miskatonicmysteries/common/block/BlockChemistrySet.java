package com.miskatonicmysteries.common.block;

import com.miskatonicmysteries.common.block.blockentity.BlockEntityChemistrySet;
import com.miskatonicmysteries.common.feature.PotentialItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.client.particle.FireSmokeParticle;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

import static net.minecraft.state.property.Properties.*;

public class BlockChemistrySet extends HorizontalFacingBlock implements BlockEntityProvider {
    public BlockChemistrySet() {
        super(Settings.of(Material.METAL).nonOpaque().requiresTool().strength(1F, 4F)
                .allowsSpawning((state, world, pos, type) -> false).solidBlock((state, world, pos) -> false)
                .suffocates((state, world, pos) -> false)
                .blockVision((state, world, pos) -> false)
                .lightLevel(state -> state.get(LIT) ? 10 : 0));
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(LIT, false).with(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.empty();
    }

    public VoxelShape getVisualShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
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
        builder.add(LIT, FACING, WATERLOGGED);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BlockEntityChemistrySet) {
                ItemScatterer.spawn(world, pos, ((BlockEntityChemistrySet) blockEntity).getItems());
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        BlockEntityChemistrySet blockEntity = (BlockEntityChemistrySet) world.getBlockEntity(pos);
        if (stack.getItem() instanceof FlintAndSteelItem && !state.get(LIT) && !state.get(WATERLOGGED)) {
            stack.damage(1, player, (p) -> p.sendToolBreakStatus(hand));
            world.setBlockState(pos, state.with(LIT, blockEntity.canBeLit(player)));
            return ActionResult.SUCCESS;
        } else if (!stack.isEmpty()) {
            if (blockEntity.containsPotentialItems()) {
                blockEntity.markDirty();
                if(blockEntity.convertPotentialItem(player, hand)) return ActionResult.SUCCESS;
            }
            for (int i = 0; i < blockEntity.size(); i++) {
                if (blockEntity.getStack(i).isEmpty() && blockEntity.isValid(i, stack) && blockEntity.canPlayerUse(player)) {
                    blockEntity.setStack(i, stack.split(1));
                    blockEntity.markDirty();
                    return ActionResult.CONSUME;
                }
            }
        } else {
            for (int i = 4; i >= 0; i--) {
                if (!blockEntity.getStack(i).isEmpty() && blockEntity.canPlayerUse(player)) {
                    world.spawnEntity(new ItemEntity(world, player.getX(), player.getY() + 0.5, player.getZ(), blockEntity.removeStack(i))); //might spawn those more efficiently
                    blockEntity.markDirty();
                    return ActionResult.SUCCESS;
                }
            }
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing());
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
        if (state.get(LIT)) {
            world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5F + random.nextGaussian() / 3F, pos.getY() + 0.5F + random.nextGaussian() / 4F, pos.getZ() + 0.5F + random.nextGaussian() / 3F, 0, 0, 0);
        }else if (world.getBlockEntity(pos) instanceof BlockEntityChemistrySet){
            BlockEntityChemistrySet cheemset = (BlockEntityChemistrySet)world.getBlockEntity(pos);
            if (cheemset.containsPotentialItems()) {
                cheemset.getPotentialItems().forEach(p -> {
                    if (!p.isEmpty() && random.nextBoolean())
                        world.addParticle(ParticleTypes.ENTITY_EFFECT, pos.getX() + 0.5F + random.nextGaussian() / 4F, pos.getY() + 0.5F + random.nextGaussian() / 3F, pos.getZ() + 0.5F + random.nextGaussian() / 4F, cheemset.smokeColor[0], cheemset.smokeColor[1], cheemset.smokeColor[2]);
                });
            }
        }
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new BlockEntityChemistrySet();
    }
}
