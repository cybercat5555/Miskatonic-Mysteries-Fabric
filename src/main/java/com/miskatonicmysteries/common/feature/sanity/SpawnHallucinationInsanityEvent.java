package com.miskatonicmysteries.common.feature.sanity;

import com.miskatonicmysteries.api.interfaces.Hallucination;
import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.api.registry.InsanityEvent;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.SpawnSettings;

import java.util.List;
import java.util.Optional;

public class SpawnHallucinationInsanityEvent extends InsanityEvent {
    public SpawnHallucinationInsanityEvent() {
        super(new Identifier(Constants.MOD_ID, "spawn_hallucination"), 0.7F, 700);
    }

    @Override
    public boolean execute(PlayerEntity player, Sanity sanity) {
        if (!player.world.isClient) {
            ServerWorld world = (ServerWorld) player.world;
            BlockPos pos = Util.getPossibleMobSpawnPos(world, player, 50, 16, 14);
            if (pos != null) {
                List<SpawnSettings.SpawnEntry> entry = world.getBiome(pos).getSpawnSettings().getSpawnEntry(SpawnGroup.MONSTER);
                EntityType<?> type = entry.get(world.random.nextInt(entry.size())).type;
                Entity entity = type.create(world);
                if (entity instanceof MobEntity) {
                    MobEntity mob = (MobEntity) entity;
                    mob.updatePosition(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D);
                    mob.setTarget(player);
                    Hallucination.of(mob).ifPresent(hallucination -> hallucination.setHallucinationTarget(Optional.of(player.getUuid())));
                    mob.initialize(world, world.getLocalDifficulty(pos), SpawnReason.EVENT, null, null);
                    world.spawnEntity(mob);
                    mob.refreshPositionAndAngles(pos, world.random.nextInt(360), 90);
                    mob.getNavigation().startMovingTo(player, 1D);
                    return true;
                }
                return false;
            }
        }
        return super.execute(player, sanity);
    }
}
