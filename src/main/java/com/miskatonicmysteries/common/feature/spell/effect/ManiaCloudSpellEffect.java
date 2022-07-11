package com.miskatonicmysteries.common.feature.spell.effect;

import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.common.registry.MMStatusEffects;
import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

import javax.annotation.Nullable;

public class ManiaCloudSpellEffect extends SpellEffect {

	public ManiaCloudSpellEffect() {
		super(new Identifier(Constants.MOD_ID, "mania_cloud"), null, 0xAA0000);
	}

	@Override
	public boolean effect(World world, LivingEntity caster, @Nullable Entity target, @Nullable Vec3d pos, SpellMedium medium, int intensity,
						  @Nullable Entity secondaryMedium) {

		System.out.println(world.isClient);
		if (pos != null) {
			Box box = new Box(pos.add(-2, -2, -2), pos.add(2, 2, 2));
			List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, box, entity -> entity != caster);
			if (world.isClient) {
				System.out.println("awawaw");
				spawnParticles(world, caster, pos);
			}
			for (LivingEntity entity : entities) {
				entity.addStatusEffect(new StatusEffectInstance(MMStatusEffects.MANIA, 200 + 200 * intensity, Math.min(intensity, 2), false, true));
			}
			return true;
		}
		return false;
	}

	@Environment(EnvType.CLIENT)
	private void spawnParticles(World world, LivingEntity caster, Vec3d pos) {
		float[] rgb = {
			((getColor(caster) >> 16) & 255) / 255F,
			((getColor(caster) >> 8) & 255) / 255F,
			(getColor(caster) & 255) / 255F
		};
		for (int i = 0; i < 45; i++) {
			world.addParticle(ParticleTypes.ENTITY_EFFECT,
							  pos.getX() + MathHelper.nextGaussian(world.getRandom(), 0, 2),
							  pos.getY() + MathHelper.nextGaussian(world.getRandom(), 0, 2),
							  pos.getZ() + MathHelper.nextGaussian(world.getRandom(), 0, 2),
							  rgb[0], rgb[1], rgb[2]);
		}
	}
}
