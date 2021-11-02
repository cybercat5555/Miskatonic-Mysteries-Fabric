package com.miskatonicmysteries.common.feature.sanity;

import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.api.registry.InsanityEvent;
import com.miskatonicmysteries.common.feature.entity.HallucinationEntity;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.Util;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.SpawnSettings;

public class SpawnHallucinationInsanityEvent extends InsanityEvent {

	public SpawnHallucinationInsanityEvent() {
		super(new Identifier(Constants.MOD_ID, "spawn_hallucination"), 0.7F, 700);
	}

	@Override
	public boolean execute(PlayerEntity player, Sanity sanity) {
		if (!player.world.isClient) {
			ServerWorld world = (ServerWorld) player.world;
			BlockPos pos = Util.getPossibleMobSpawnPos(world, player, 50, 16, 14, EntityType.ARMOR_STAND); //dummy entity type
			if (pos != null) {
				Pool<SpawnSettings.SpawnEntry> entryPool = world.getBiome(pos).getSpawnSettings().getSpawnEntries(SpawnGroup.MONSTER);
				HallucinationEntity hallucination = entryPool.getOrEmpty(world.random).map(entry -> {
					HallucinationEntity entity = new HallucinationEntity(world);
					entity.setEntityHallucination(entry.type);
					entity.setHallucinationTarget(player.getUuid());
					entity.setPosition(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D);
					entity.setTarget(player);
					entity.initialize(world, world.getLocalDifficulty(pos), SpawnReason.EVENT, null, null);
					return entity;
				}).orElse(null);
				if (hallucination != null) {
					world.spawnEntity(hallucination);
					hallucination.refreshPositionAndAngles(pos, world.random.nextInt(360), 90);
					hallucination.getNavigation().startMovingTo(player, 1D);
					return true;
				}
				return false;
			}
		}
		return super.execute(player, sanity);
	}
}
