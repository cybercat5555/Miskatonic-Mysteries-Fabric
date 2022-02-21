package com.miskatonicmysteries.common.feature.entity.ai.task;

import com.google.common.collect.ImmutableMap;
import com.miskatonicmysteries.common.feature.entity.HasturCultistEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CrownAscendedCultist extends Task<LivingEntity> {
    public CrownAscendedCultist() {
        super(ImmutableMap.of(MemoryModuleType.MOBS, MemoryModuleState.VALUE_PRESENT, MemoryModuleType.INTERACTION_TARGET,
        MemoryModuleState.VALUE_PRESENT));
    }

    @Override
    protected boolean shouldRun(ServerWorld world, LivingEntity entity) {
        List<HasturCultistEntity> cultistEntities = streamCultistEntities(entity, (hastur) -> (hastur instanceof HasturCultistEntity)).collect(Collectors.toList());
        int cultistCount = (int) cultistEntities.stream().filter(Objects::nonNull).count();
        if (cultistCount >= 3) {
            return cultistEntities.stream().filter(HasturCultistEntity::isAscended).collect(Collectors.toList()).isEmpty();
        }
        return false;
    }

    @Override
    protected void run(ServerWorld world, LivingEntity entity, long time) {
        super.run(world, entity, time);
        if(entity instanceof HasturCultistEntity hasturCultistEntity){
            List<HasturCultistEntity> cultistEntities = streamCultistEntities(hasturCultistEntity, (hastur) -> (hastur instanceof HasturCultistEntity)).collect(Collectors.toList());
            HasturCultistEntity first = cultistEntities.get(0);
            cultistEntities.remove(0);
            for(HasturCultistEntity hasturCultist : cultistEntities){
                hasturCultist.setCastTime(60);
            }
            if(!hasturCultistEntity.isCasting()){
                first.ascend();
            }
        }
    }

    public static Stream<HasturCultistEntity> streamCultistEntities(LivingEntity cultist, Predicate<VillagerEntity> filter) {
        return cultist.getBrain().getOptionalMemory(MemoryModuleType.MOBS).map((list) -> list
        .stream()
        .filter((entity) -> entity instanceof HasturCultistEntity && entity != cultist)
        .map((livingEntity) -> (HasturCultistEntity)livingEntity)
        .filter(LivingEntity::isAlive)
        .filter(filter)).orElseGet(Stream::empty);
    }
}
