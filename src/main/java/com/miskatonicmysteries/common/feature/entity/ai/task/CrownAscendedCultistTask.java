package com.miskatonicmysteries.common.feature.entity.ai.task;

import com.miskatonicmysteries.common.feature.entity.HasturCultistEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.server.world.ServerWorld;

import java.util.Comparator;
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
			List<HasturCultistEntity> cultistEntities = getCultists(hasturCultistEntity).stream()
				.sorted(Comparator.comparingInt(Entity::getId)).collect(Collectors.toList());
			if (!cultistEntities.isEmpty()) {
				for (HasturCultistEntity hasturCultist : cultistEntities) {
					hasturCultist.setCastTime(60);
				}
			}
		}
	}

	@Override
	protected void finishRunning(ServerWorld world, LivingEntity entity, long time) {
		super.finishRunning(world, entity, time);

		if (entity instanceof HasturCultistEntity hasturCultistEntity) {
			List<HasturCultistEntity> cultistEntities = getCultists(hasturCultistEntity).stream().sorted(Comparator.comparingInt(Entity::getId))
				.collect(Collectors.toList());
			if (!cultistEntities.isEmpty()) {
				HasturCultistEntity target = cultistEntities.get(0);
				target.ascend();
				for (HasturCultistEntity hasturCultist : cultistEntities) {
					hasturCultist.setCastTime(60);
				}
				//target.initialize(world, world.getLocalDifficulty(target.getBlockPos()), SpawnReason.CONVERSION, null, )
			}
		}
	}

	@Override
	protected boolean shouldKeepRunning(ServerWorld world, LivingEntity entity, long time) {
		return ((HasturCultistEntity) entity).isCasting();
	}

	@Override
	protected boolean shouldRun(ServerWorld world, LivingEntity entity) {
		if (entity.age < 0 || ((HasturCultistEntity) entity).isAscended() || world.getMoonPhase() != 4) {
			return false;
		}
		if (((HasturCultistEntity) entity).isCasting()) {
			return false;
		}
		List<HasturCultistEntity> cultistEntities = getCultists(entity);
		int cultistCount = cultistEntities.size();
		int vassalCount = (int) cultistEntities.stream().filter(HasturCultistEntity::isAscended).count();
		return cultistCount > 1 && vassalCount < 1;
	}

	public static List<HasturCultistEntity> getCultists(LivingEntity cultist) {
		return cultist.getBrain().getOptionalMemory(MemoryModuleType.MOBS).map((list) -> list
			.stream()
			.filter((entity) -> entity instanceof HasturCultistEntity)
			.map((livingEntity) -> (HasturCultistEntity) livingEntity)
			.filter(LivingEntity::isAlive)).orElseGet(Stream::empty).collect(Collectors.toList());
	}
}
