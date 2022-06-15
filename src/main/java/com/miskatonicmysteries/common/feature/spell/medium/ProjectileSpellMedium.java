package com.miskatonicmysteries.common.feature.spell.medium;

import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.common.feature.entity.SpellProjectileEntity;
import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ProjectileSpellMedium extends SpellMedium {

	public ProjectileSpellMedium() {
		super(new Identifier(Constants.MOD_ID, "projectile"));
	}

	@Override
	public boolean cast(World world, LivingEntity caster, SpellEffect effect, int intensity) {
		if (!world.isClient) {
			SpellProjectileEntity projectile = new SpellProjectileEntity(caster.world, caster, effect, intensity);
			projectile.setVelocity(caster, caster.getPitch(), caster.getHeadYaw(), 0, 1, 0);
			projectile.setYaw(caster.getHeadYaw());
			projectile.setPitch(caster.getPitch());
			return world.spawnEntity(projectile);
		}
		return true;
	}

	@Override
	public float getCooldownModifier(LivingEntity caster) {
		return 0.9F;
	}
}
