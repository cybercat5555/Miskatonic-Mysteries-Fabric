package com.miskatonicmysteries.common.feature.entity.ai.task;

import com.miskatonicmysteries.common.MMMidnightLibConfig;
import com.miskatonicmysteries.common.feature.entity.HasturCultistEntity;
import com.miskatonicmysteries.common.registry.MMEntities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;

import java.util.List;

import com.google.common.collect.ImmutableMap;

public class RecruitTask extends Task<VillagerEntity> {

	public RecruitTask() {
		super(ImmutableMap.of(MemoryModuleType.MOBS, MemoryModuleState.VALUE_PRESENT, MemoryModuleType.INTERACTION_TARGET,
							  MemoryModuleState.VALUE_PRESENT));
	}

	@Override
	protected void run(ServerWorld world, VillagerEntity entity, long time) {
		super.run(world, entity, time);
		Brain<?> brain = entity.getBrain();
		List<VillagerEntity> villagers = LookTargetUtil.streamSeenVillagers(entity, (villagerEntityx) -> !villagerEntityx.isBaby()).toList();
		int cultistCount = (int) villagers.stream().filter(v -> v instanceof HasturCultistEntity).count();
		float actualPercentage = cultistCount / (float) villagers.size();
		if (actualPercentage < MMMidnightLibConfig.yellowSerfPercentage && brain
				.getOptionalMemory(MemoryModuleType.INTERACTION_TARGET).get() instanceof VillagerEntity recipient) {
			if (entity instanceof HasturCultistEntity) {
				((HasturCultistEntity) entity).setCastTime(60);
			}
			HasturCultistEntity cultist = MMEntities.HASTUR_CULTIST.create(world);
			if (cultist != null) {
				cultist.refreshPositionAndAngles(recipient.getX(), recipient.getY(), recipient.getZ(), recipient.getYaw(), recipient.getPitch());
				cultist.initialize(world, world.getLocalDifficulty(cultist.getBlockPos()), SpawnReason.CONVERSION, null, null);
				cultist.setAiDisabled(recipient.isAiDisabled());
				if (recipient.hasCustomName()) {
					cultist.setCustomName(recipient.getCustomName());
					cultist.setCustomNameVisible(recipient.isCustomNameVisible());
				}
				cultist.setPersistent();
				world.spawnEntityAndPassengers(cultist);
				recipient.releaseTicketFor(MemoryModuleType.HOME);
				recipient.releaseTicketFor(MemoryModuleType.JOB_SITE);
				recipient.releaseTicketFor(MemoryModuleType.POTENTIAL_JOB_SITE);
				recipient.releaseTicketFor(MemoryModuleType.MEETING_POINT);
				recipient.remove(Entity.RemovalReason.DISCARDED);
				brain.forget(MemoryModuleType.INTERACTION_TARGET);
				cultist.reinitializeBrain(world);
			}
		}
	}

	@Override
	protected boolean shouldRun(ServerWorld world, VillagerEntity entity) {
		return entity instanceof HasturCultistEntity && ((HasturCultistEntity) entity).isAscended() && world.getMoonPhase() == 0
			&& isRecipientQualified(entity, entity.getBrain().getOptionalMemory(MemoryModuleType.INTERACTION_TARGET).get());
	}

	private boolean isRecipientQualified(VillagerEntity entity, LivingEntity recipient) {
		if (recipient instanceof VillagerEntity) {
			VillagerData data = ((VillagerEntity) recipient).getVillagerData();
			return (data.getProfession().equals(VillagerProfession.NONE) || data.getProfession().equals(VillagerProfession.NITWIT))
				&& recipient.distanceTo(entity) <= 4;
		}
		return false;
	}
}
