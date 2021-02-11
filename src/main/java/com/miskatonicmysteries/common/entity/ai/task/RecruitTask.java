package com.miskatonicmysteries.common.entity.ai.task;

import com.google.common.collect.ImmutableMap;
import com.miskatonicmysteries.common.MiskatonicMysteries;
import com.miskatonicmysteries.common.entity.HasturCultistEntity;
import com.miskatonicmysteries.common.registry.MMEntities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.List;
import java.util.stream.Collectors;

public class RecruitTask extends Task<VillagerEntity> {
    public RecruitTask() {
        super(ImmutableMap.of(MemoryModuleType.MOBS, MemoryModuleState.VALUE_PRESENT, MemoryModuleType.INTERACTION_TARGET, MemoryModuleState.VALUE_PRESENT));
    }

    @Override
    protected boolean shouldRun(ServerWorld world, VillagerEntity entity) {
        return entity instanceof HasturCultistEntity && ((HasturCultistEntity) entity).isAscended() && world.getDimension().getMoonPhase(world.getTime()) == 0 && isRecipientQualified(entity, entity.getBrain().getOptionalMemory(MemoryModuleType.INTERACTION_TARGET).get());
    }

    private boolean isRecipientQualified(VillagerEntity entity, LivingEntity recipient) {
        return recipient instanceof VillagerEntity && recipient.distanceTo(entity) <= 4;
    }

    @Override
    protected void run(ServerWorld world, VillagerEntity entity, long time) {
        super.run(world, entity, time);
        Brain<?> brain = entity.getBrain();
        List<VillagerEntity> villagers = LookTargetUtil.streamSeenVillagers(entity, (villagerEntityx) -> !villagerEntityx.isBaby()).collect(Collectors.toList());
        int cultistCount = (int) villagers.stream().filter(v -> v instanceof HasturCultistEntity).count();
        float actualPercentage = cultistCount / (float) villagers.size();
        if (actualPercentage < MiskatonicMysteries.config.entities.yellowSerfPercentage && brain.getOptionalMemory(MemoryModuleType.INTERACTION_TARGET).get() instanceof VillagerEntity) {
            if (entity instanceof HasturCultistEntity) {
                ((HasturCultistEntity) entity).setCastTime(60);
            }
            VillagerEntity recipient = (VillagerEntity) brain.getOptionalMemory(MemoryModuleType.INTERACTION_TARGET).get();
            HasturCultistEntity cultist = MMEntities.HASTUR_CULTIST.create(world);
            cultist.refreshPositionAndAngles(recipient.getX(), recipient.getY(), recipient.getZ(), recipient.yaw, recipient.pitch);
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
            recipient.remove();
            brain.forget(MemoryModuleType.INTERACTION_TARGET);
            cultist.reinitializeBrain(world);
        }

    }
}
