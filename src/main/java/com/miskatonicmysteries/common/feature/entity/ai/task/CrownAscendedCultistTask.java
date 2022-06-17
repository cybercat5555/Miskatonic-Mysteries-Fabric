package com.miskatonicmysteries.common.feature.entity.ai.task;

import com.miskatonicmysteries.common.feature.entity.HasturCultistEntity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.server.world.ServerWorld;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableMap;

public class CrownAscendedCultistTask extends Task<LivingEntity> {

	public CrownAscendedCultistTask() {
		super(ImmutableMap.of(MemoryModuleType.MOBS, MemoryModuleState.VALUE_PRESENT, MemoryModuleType.INTERACTION_TARGET,
							  MemoryModuleState.VALUE_PRESENT));
	}

	@Override
	protected void run(ServerWorld world, LivingEntity entity, long time) {
		super.run(world, entity, time);
		if (entity instanceof HasturCultistEntity hasturCultistEntity) {
			List<HasturCultistEntity> cultistEntities = getCultists(hasturCultistEntity, false);
			if (!cultistEntities.isEmpty()) {
				cultistEntities.get(0).ascend();
				for (HasturCultistEntity hasturCultist : cultistEntities) {
					hasturCultist.setCastTime(60);
				}
			}
		}
	}

	@Override
	protected boolean shouldRun(ServerWorld world, LivingEntity entity) {
		if (entity.age < 600 && ((HasturCultistEntity) entity).isAscended()) {
			return false;
		}
		List<HasturCultistEntity> cultistEntities = getCultists(entity, true);
		int cultistCount = (int) cultistEntities.stream().filter(Objects::nonNull).count();
		return cultistCount < 1;
	}

	public static List<HasturCultistEntity> getCultists(LivingEntity cultist, boolean ascended) {
		return cultist.getBrain().getOptionalMemory(MemoryModuleType.MOBS).map((list) -> list
			.stream()
			.filter((entity) -> entity instanceof HasturCultistEntity && (!ascended || ((HasturCultistEntity) entity).isAscended()))
			.map((livingEntity) -> (HasturCultistEntity) livingEntity)
			.filter(LivingEntity::isAlive)).orElseGet(Stream::empty).collect(Collectors.toList());
	}
}
