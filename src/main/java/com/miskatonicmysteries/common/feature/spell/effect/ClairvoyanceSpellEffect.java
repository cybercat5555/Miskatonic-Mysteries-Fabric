package com.miskatonicmysteries.common.feature.spell.effect;

import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.common.registry.MMStatusEffects;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ClairvoyanceSpellEffect extends SpellEffect {
    public ClairvoyanceSpellEffect() {
        super(new Identifier(Constants.MOD_ID, "clairvoyance"), null, 0xFFFFFF);
    }

    @Override
    public boolean effect(World world, LivingEntity caster, @Nullable Entity target, @Nullable Vec3d pos, SpellMedium medium, int intensity, @Nullable Entity secondaryMedium) {
        if (world.isClient && target != null) {
            spawnParticleEffectsOnTarget(caster, this, target);
        }
        if (!(target instanceof LivingEntity)) {
            return false;
        }
        ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(MMStatusEffects.CLAIRVOYANCE, 1200 + 1200 * intensity, 0, true, true));
        return true;
    }

    @Override
    public float getCooldownBase(int intensity) {
        return 40 + intensity * 40;
    }
}
