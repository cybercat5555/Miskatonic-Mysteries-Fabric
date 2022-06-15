package com.miskatonicmysteries.common.feature.entity.ai.task;

import com.miskatonicmysteries.common.feature.entity.util.CastingMob;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.registry.MMSpellEffects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;

import com.google.common.collect.ImmutableMap;

public class CastSpellTask extends Task<VillagerEntity> {

	private int timer;

	public CastSpellTask() {
		super(ImmutableMap
				  .of(MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT,
					  MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleState.VALUE_ABSENT));
	}

	@Override
	protected void run(ServerWorld world, VillagerEntity entity, long time) {
		if (entity instanceof CastingMob) {
			timer = 60;
			Spell spell = ((CastingMob) entity).selectSpell();
			if (spell.effect == MMSpellEffects.DAMAGE) {
				timer = 40;
			}
			((CastingMob) entity).setCurrentSpell(spell);
			((CastingMob) entity).setCastTime(timer);
			LookTargetUtil.lookAt(entity, getTarget(entity));
		}
	}

	@Override
	protected boolean shouldRun(ServerWorld world, VillagerEntity entity) {
		return entity instanceof CastingMob && LookTargetUtil.isVisibleInMemory(entity, getTarget(entity))
			&& ((CastingMob) entity).getCastTime() <= 0 && getTarget(entity).distanceTo(entity) > 3;
	}

	private LivingEntity getTarget(MobEntity mobEntity) {
		return mobEntity.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get();
	}
}
