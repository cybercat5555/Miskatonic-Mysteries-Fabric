package com.miskatonicmysteries.common.handler;

import com.miskatonicmysteries.common.entity.ProtagonistEntity;
import com.miskatonicmysteries.common.feature.world.MMWorldState;
import com.miskatonicmysteries.common.registry.MMEntities;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.Util;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ProtagonistHandler {

    public static boolean spawnProtagonist(ServerWorld world, PlayerEntity player) {
        MMWorldState worldState = MMWorldState.get(world);
        ProtagonistEntity.ProtagonistData data = worldState.getProtagonistDataFor(player);
        if (!data.spawned) {
            BlockPos pos = Util.getPossibleMobSpawnPos(world, player, 50, 16, 14, MMEntities.PROTAGONIST);
            if (pos != null) {
                ProtagonistEntity protagonist = new ProtagonistEntity(MMEntities.PROTAGONIST, world);
                protagonist.setPosition(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D);
                protagonist.setTargetUUID(player.getUuid());
                protagonist.setData(data);
                protagonist.initialize(world, world.getLocalDifficulty(pos), SpawnReason.EVENT, null, null);
                data.spawned = true;
                world.spawnEntity(protagonist);
                protagonist.refreshPositionAndAngles(pos, 0, 0);
                protagonist.getNavigation().startMovingTo(player, 1D);
                worldState.markDirty();
                if (data.level >= Constants.DataTrackers.PROTAGONIST_MAX_LEVEL - 1) {
                    for (int i = 0; i < data.level - 1; i++) spawnProtagonistReinforcements(world, player);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean spawnProtagonistReinforcements(ServerWorld world, PlayerEntity player) {
        BlockPos pos = Util.getPossibleMobSpawnPos(world, player, 50, 16, 14, MMEntities.PROTAGONIST);
        if (pos != null) {
            ProtagonistEntity protagonist = new ProtagonistEntity(MMEntities.PROTAGONIST, world);
            protagonist.setPosition(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D);
            protagonist.initialize(world, world.getLocalDifficulty(pos), SpawnReason.REINFORCEMENT, null, null);
            world.spawnEntity(protagonist);
            protagonist.refreshPositionAndAngles(pos, 0, 0);
            protagonist.getNavigation().startMovingTo(player, 1D);
            return true;
        }
        return false;
    }

    public static void createProtagonist(World world, PlayerEntity player) {
        MMWorldState worldState = MMWorldState.get(world);
        worldState.addProtagonist(player, new ProtagonistEntity.ProtagonistData(0, world.random.nextInt(4), false));
    }

    public static void levelProtagonist(World world, ProtagonistEntity protagonist) {
        MMWorldState worldState = MMWorldState.get(world);
        ProtagonistEntity.ProtagonistData data = worldState.getProtagonistDataFor(protagonist);
        data.level++;
        worldState.markDirty();
    }

    public static void removeProtagonist(World world, ProtagonistEntity protagonist) {
        MMWorldState worldState = MMWorldState.get(world);
        worldState.removeProtagonist(protagonist);
    }

    public static void setSpawnState(ProtagonistEntity protagonist, boolean spawnState) {
        MMWorldState worldState = MMWorldState.get(protagonist.world);
        ProtagonistEntity.ProtagonistData data = worldState.getProtagonistDataFor(protagonist);
        if (data != null) data.spawned = spawnState;
        worldState.markDirty();
    }
}
