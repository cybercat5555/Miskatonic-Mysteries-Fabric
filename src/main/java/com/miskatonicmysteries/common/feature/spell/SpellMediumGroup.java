package com.miskatonicmysteries.common.feature.spell;

import com.miskatonicmysteries.common.feature.Affiliated;
import com.miskatonicmysteries.common.lib.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SpellMediumGroup extends SpellMedium {
    public SpellMediumGroup() {
        super(new Identifier(Constants.MOD_ID, "group"));
    }

    @Override
    public boolean cast(World world, LivingEntity caster, SpellEffect effect, int intensity) {
        boolean successfulCast = false;
        if (caster instanceof Affiliated) {
            for (Entity otherEntity : world.getOtherEntities(null, caster.getBoundingBox()
                            .expand(15 + (5 * intensity), 5 + (5 * intensity), 15 + (5 * intensity)),
                    entity -> entity instanceof Affiliated && ((Affiliated) entity).getAffiliation().equals(((Affiliated) caster).getAffiliation()))) {
                if (effect.effect(world, caster, otherEntity, this, intensity)) successfulCast = true;
            }
        } else {
            for (Entity otherEntity : world.getOtherEntities(null, caster.getBoundingBox()
                            .expand(15 + (5 * intensity), 5 + (5 * intensity), 15 + (5 * intensity)),
                    entity -> entity.getType().equals(caster.getType()))) {
                if (effect.effect(world, caster, otherEntity, this, intensity)) successfulCast = true;
            }
        }
        return !caster.isDead() && successfulCast;
    }
}
