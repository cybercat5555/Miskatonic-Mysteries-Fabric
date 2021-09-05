package com.miskatonicmysteries.common.feature.effect;

import com.miskatonicmysteries.api.interfaces.HiddenEntity;
import com.miskatonicmysteries.common.entity.HallucinationEntity;
import com.miskatonicmysteries.common.feature.recipe.rite.SpawnerTrapRite;
import com.miskatonicmysteries.common.registry.MMStatusEffects;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class ClairvoyanceStatusEffect extends StatusEffect {
	public ClairvoyanceStatusEffect() {
		super(StatusEffectType.NEUTRAL, 0xFFFFFF);
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		super.applyUpdateEffect(entity, amplifier);

		if (entity.age % 20 == 0) {
			MMStatusEffects.intoxicatedUpdate(entity, amplifier);
		}

		if (entity.world instanceof ServerWorld serverWorld && entity.age % 200 == 0 && entity.getRandom().nextFloat() < amplifier * 0.1F) {
			spawnSpecialHallucination(entity, serverWorld);
		}
	}

	private void spawnSpecialHallucination(LivingEntity entity, ServerWorld serverWorld) {
		Optional<EntityType<?>> typeOptional =
				SpawnerTrapRite.getRandomSpawnEntity(type -> type.isIn(Constants.Tags.BROKEN_VEIL_MOBS),
						entity.getRandom());
		if (typeOptional.isPresent()) {
			EntityType<?> spawnType = typeOptional.get();
			BlockPos spawnPos = Util.getPossibleMobSpawnPos(serverWorld, entity, 10, 24, 16, spawnType);
			if (spawnPos == null) {
				return;
			}
			Entity e = spawnType.create(serverWorld);
			if (e instanceof MobEntity m && e instanceof HiddenEntity h) {
				e.setPosition(spawnPos.getX() + 0.5D, spawnPos.getY() + 1.0D, spawnPos.getZ() + 0.5D);
				h.setHidden(true);
				m.setTarget(entity);
				m.initialize(serverWorld, serverWorld.getLocalDifficulty(spawnPos), SpawnReason.EVENT, null, null);
				serverWorld.spawnEntity(e);
				e.refreshPositionAndAngles(spawnPos, serverWorld.random.nextInt(360), 90);
				m.getNavigation().startMovingTo(entity, 1D);
			}
		}
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}
}
