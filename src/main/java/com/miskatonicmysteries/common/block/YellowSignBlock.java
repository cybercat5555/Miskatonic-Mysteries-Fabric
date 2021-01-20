package com.miskatonicmysteries.common.block;

import com.miskatonicmysteries.common.handler.networking.packet.c2s.InvokeManiaPacket;
import com.miskatonicmysteries.common.lib.util.MiscUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

import static net.minecraft.state.property.Properties.FACING;
import static net.minecraft.state.property.Properties.IN_WALL;

public class YellowSignBlock extends Block {
    public static final VoxelShape GROUND_SHAPE = createCuboidShape(0, 0, 0, 16, 1, 16);

    public static final VoxelShape NORTH_SHAPE = createCuboidShape(0, 0, 15, 16, 16, 16);
    public static final VoxelShape EAST_SHAPE = createCuboidShape(0, 0, 0, 1, 16, 16);
    public static final VoxelShape SOUTH_SHAPE = createCuboidShape(0, 0, 0, 16, 16, 1);
    public static final VoxelShape WEST_SHAPE = createCuboidShape(15, 0, 0, 16, 16, 16);

    public YellowSignBlock() {
        super(FabricBlockSettings.of(Material.CARPET, MaterialColor.YELLOW).noCollision().hardness(1).resistance(3F));
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH).with(IN_WALL, false));
    }


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, IN_WALL);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(5) == 0) {
            MinecraftClient client = MinecraftClient.getInstance();
            Vec3d posTracked = client.player.raycast(100, client.getTickDelta(), false).getPos();
            if (posTracked != null && pos.isWithinDistance(posTracked, 1.5F) && !MiscUtil.isImmuneToYellowSign(client.player)) {
                InvokeManiaPacket.send(1, 200 + random.nextInt(200));
            }
        }
        super.randomDisplayTick(state, world, pos, random);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(IN_WALL)) {
            switch (state.get(FACING)) {
                case EAST:
                    return EAST_SHAPE;
                case WEST:
                    return WEST_SHAPE;
                case SOUTH:
                    return SOUTH_SHAPE;
                default:
                    return NORTH_SHAPE;
            }
        }
        return GROUND_SHAPE;
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        if (isPlacementValid(ctx.getWorld(), ctx.getBlockPos(), ctx.getSide())) {
            return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite()).with(IN_WALL, ctx.getSide() != Direction.UP);
        }
        return null;
    }

    public boolean isPlacementValid(WorldView world, BlockPos pos, Direction direction) {
        return direction != Direction.DOWN && world.getBlockState(pos.offset(direction.getOpposite())).isSideSolidFullSquare(world, pos, direction);
    }
}
