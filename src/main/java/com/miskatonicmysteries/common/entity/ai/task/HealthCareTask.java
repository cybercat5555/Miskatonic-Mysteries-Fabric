package com.miskatonicmysteries.common.entity.ai.task;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;

public class HealthCareTask extends Task<VillagerEntity> {
    public HealthCareTask() {
        super(ImmutableMap.of(MemoryModuleType.INTERACTION_TARGET, MemoryModuleState.VALUE_PRESENT));
    }

    @Override
    protected boolean shouldRun(ServerWorld world, VillagerEntity entity) {
        return !(world.getDimension().getMoonPhase(world.getTime()) == 8 || world.getDimension().getMoonPhase(world.getTime()) == 4) && isRecipientQualified(entity, entity.getBrain().getOptionalMemory(MemoryModuleType.INTERACTION_TARGET).get());
    }

    private boolean isRecipientQualified(VillagerEntity entity, LivingEntity recipient) {
        return recipient instanceof VillagerEntity && recipient.distanceTo(entity) <= 4 && !recipient.hasStatusEffect(StatusEffects.REGENERATION);
    }

    @Override
    protected void run(ServerWorld world, VillagerEntity entity, long time) {
        super.run(world, entity, time);

        Brain<?> brain = entity.getBrain();
        if (brain.getOptionalMemory(MemoryModuleType.INTERACTION_TARGET).isPresent()) {
            LivingEntity recipient = brain.getOptionalMemory(MemoryModuleType.INTERACTION_TARGET).get();
            if (recipient.distanceTo(entity) > 4)
                return;
            recipient.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 4800, 1, true, true));
            recipient.heal(recipient.getMaxHealth() / 2F);
            brain.forget(MemoryModuleType.INTERACTION_TARGET);
        }
    }
}
