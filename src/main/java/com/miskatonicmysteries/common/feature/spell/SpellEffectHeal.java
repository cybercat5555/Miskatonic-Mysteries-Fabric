package com.miskatonicmysteries.common.feature.spell;

import com.miskatonicmysteries.lib.util.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SpellEffectHeal extends SpellEffect {
    public SpellEffectHeal() {
        super(new Identifier(Constants.MOD_ID, "heal"), living -> living.getHealth() < living.getMaxHealth(), 0xDC0000);
    }

    @Override
    public boolean effect(World world, LivingEntity caster, @Nullable Entity target, SpellMedium medium, int intensity) {
        if (world.isClient && target != null) {
            spawnParticleEffectsOnTarget(this, target);
        }
        if (!(target instanceof LivingEntity)) return false;
        ((LivingEntity) target).heal(2.5F * (intensity + 1));
        return true;
    }
}
