package com.miskatonicmysteries.common.feature.spell.effect;

import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ResistanceSpellEffect extends SpellEffect {

	public ResistanceSpellEffect() {
		super(new Identifier(Constants.MOD_ID, "resistance"), null, 0x81808C);
	}

	@Override
	public boolean effect(World world, LivingEntity caster, @Nullable Entity target, @Nullable Vec3d pos, SpellMedium medium, int intensity,
						  @Nullable Entity secondaryMedium) {
		if (world.isClient && target != null) {
			spawnParticleEffectsOnTarget(caster, this, target);
		}
		if (!(target instanceof LivingEntity)) {
			return false;
		}
		((LivingEntity) target)
			.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 200 + 200 * intensity, Math.min(intensity, 2), true, true));
		return true;
	}

	@Override
	public float getCooldownBase(int intensity) {
		return 180 + intensity * 80;
	}
}
