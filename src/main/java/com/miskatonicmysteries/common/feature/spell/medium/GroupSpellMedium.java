package com.miskatonicmysteries.common.feature.spell.medium;

import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
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
							  entity -> entity instanceof LivingEntity);
		for (Entity otherEntity : entities) {
			if (effect.effect(world, caster, otherEntity, otherEntity.getPos(), this, intensity, caster)) {
				successfulCast = true;
			}
		}
		return !caster.isDead() && successfulCast;
	}

	@Override
	public float getCooldownModifier(LivingEntity caster) {
		return 3F;
	}
}
