package com.miskatonicmysteries.common.feature.spell.medium;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.common.feature.entity.HarrowEntity;
import com.miskatonicmysteries.common.feature.entity.TentacleEntity;
import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

public class GroupSpellMedium extends SpellMedium {

	public GroupSpellMedium() {
		super(new Identifier(Constants.MOD_ID, "group"));
	}

	@Override
	public boolean cast(World world, LivingEntity caster, SpellEffect effect, int intensity) {
		boolean successfulCast = false;
		List<Entity> entities = world
			.getOtherEntities(null, caster.getBoundingBox().expand(3 + (4 * intensity), 3 + (4 * intensity), 3 + (4 * intensity)),
							  entity -> isValidTarget(caster, entity));
		for (Entity otherEntity : entities) {
			if (effect.effect(world, caster, otherEntity, otherEntity.getPos(), this, intensity, caster)) {
				successfulCast = true;
			}
		}
		return !caster.isDead() && successfulCast;
	}

	private boolean isValidTarget(LivingEntity caster, Entity entity) {
		if (entity instanceof LivingEntity) {
			if (entity == caster.getAttacker() || entity instanceof TentacleEntity || entity instanceof HarrowEntity) {
				return false;
			}
			if (MiskatonicMysteriesAPI.isDefiniteAffiliated(caster)) {
				return MiskatonicMysteriesAPI.getNonNullAffiliation(caster, false) == MiskatonicMysteriesAPI.getNonNullAffiliation(entity, false);
			}
			return true;
		}
		return false;
	}

	@Override
	public float getCooldownModifier(LivingEntity caster) {
		return 3F;
	}
}
