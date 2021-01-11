package com.miskatonicmysteries.common.feature.spell.effect;

import com.miskatonicmysteries.common.feature.Affiliated;
import com.miskatonicmysteries.common.feature.spell.SpellEffect;
import com.miskatonicmysteries.common.feature.spell.SpellMedium;
import com.miskatonicmysteries.common.lib.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class DamageSpellEffect extends SpellEffect {
    public DamageSpellEffect() {
        super(new Identifier(Constants.MOD_ID, "damage"), null, 0xFFFFFF);
    }

    @Override
    public int getColor(@Nullable LivingEntity caster) {
        return caster instanceof Affiliated ? ((Affiliated) caster).getAffiliation(true).getIntColor() : super.getColor(caster);
    }

    @Override
    public boolean effect(World world, LivingEntity caster, @Nullable Entity target, @Nullable Vec3d pos, SpellMedium medium, int intensity, @Nullable Entity secondaryMedium) {
        if (target != null) {
            target.damage(DamageSource.MAGIC, 2.5F * (intensity + 1));
            return true;
        }
        return false;
    }
}
