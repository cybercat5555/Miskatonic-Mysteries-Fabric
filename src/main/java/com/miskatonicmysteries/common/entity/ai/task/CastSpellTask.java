package com.miskatonicmysteries.common.entity.ai.task;

import com.google.common.collect.ImmutableMap;
import com.miskatonicmysteries.api.interfaces.CastingMob;
import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.registry.MMSpellEffects;
import com.miskatonicmysteries.common.registry.MMSpellMediums;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;

public class CastSpellTask extends Task<VillagerEntity> {
    private int timer;

    public CastSpellTask() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleState.VALUE_ABSENT));
    }

    @Override
    protected boolean shouldRun(ServerWorld world, VillagerEntity entity) {
        return entity instanceof CastingMob && LookTargetUtil.isVisibleInMemory(entity, getTarget(entity)) && ((CastingMob) entity).getCastTime() <= 0 && getTarget(entity).distanceTo(entity) > 3;
    }

    private LivingEntity getTarget(MobEntity mobEntity) {
        return mobEntity.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get();
    }

    @Override
    protected void run(ServerWorld world, VillagerEntity entity, long time) {
        if (entity instanceof CastingMob) {
            timer = 60;
            SpellEffect effect = MMSpellEffects.KNOCKBACK;
            SpellMedium medium = MMSpellMediums.MOB_TARGET;
            LivingEntity target = getTarget(entity);
            int intensity = 1 + entity.getRandom().nextInt(2);
            if (entity.getRandom().nextBoolean() && entity.getHealth() < entity.getMaxHealth()) {
                effect = MMSpellEffects.HEAL;
                medium = MMSpellMediums.GROUP;
            } else if (entity.getRandom().nextBoolean() && target.distanceTo(entity) > 12) {
                effect = MMSpellEffects.DAMAGE;
                medium = MMSpellMediums.BOLT;
                intensity = 1;
                timer = 40;
            } else if (!entity.hasStatusEffect(StatusEffects.RESISTANCE)) {
                effect = MMSpellEffects.RESISTANCE;
                medium = MMSpellMediums.GROUP;
            }
            ((CastingMob) entity).setCurrentSpell(new Spell(medium, effect, intensity));
            ((CastingMob) entity).setCastTime(timer);
            LookTargetUtil.lookAt(entity, target);
        }
    }
}
