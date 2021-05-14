package com.miskatonicmysteries.common.feature.spell.effect;

import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class HealSpellEffect extends SpellEffect {
    public HealSpellEffect() {
        super(new Identifier(Constants.MOD_ID, "heal"), null, 0xDC0000);
    }

    @Override
    public boolean effect(World world, LivingEntity caster, @Nullable Entity target, @Nullable Vec3d pos, SpellMedium medium, int intensity, @Nullable Entity secondaryMedium) {
        if (target != null) {
            if (world.isClient) {
                spawnParticleEffectsOnTarget(caster, this, target);
            }
            if (!(target instanceof LivingEntity)){
                return false;
            }
            ((LivingEntity) target).heal(2F * (intensity + 1));
            return true;
        }
        return false;
    }

    @Override
    public float getCooldownBase(int intensity) {
        return 60 + intensity * 20;
    }
}
