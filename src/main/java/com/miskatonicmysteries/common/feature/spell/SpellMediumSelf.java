package com.miskatonicmysteries.common.feature.spell;

import com.miskatonicmysteries.lib.util.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SpellMediumSelf extends SpellMedium {
    public SpellMediumSelf() {
        super(new Identifier(Constants.MOD_ID, "self"));
    }

    @Override
    public boolean cast(World world, LivingEntity caster, SpellEffect effect, int intensity) {
        return !caster.isDead() && effect.effect(world, caster, caster, this, intensity);
    }
}
