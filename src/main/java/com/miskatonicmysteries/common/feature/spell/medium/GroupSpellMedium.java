package com.miskatonicmysteries.common.feature.spell.medium;

import com.miskatonicmysteries.common.feature.interfaces.Affiliated;
import com.miskatonicmysteries.common.feature.spell.SpellEffect;
import com.miskatonicmysteries.common.feature.spell.SpellMedium;
import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.util.CapabilityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class GroupSpellMedium extends SpellMedium {
    public GroupSpellMedium() {
        super(new Identifier(Constants.MOD_ID, "group"));
    }

    @Override
    public boolean cast(World world, LivingEntity caster, SpellEffect effect, int intensity) {
        boolean successfulCast = false;
        if (caster instanceof Affiliated) {
            for (Entity otherEntity : world.getOtherEntities(null, caster.getBoundingBox()
                            .expand(15 + (5 * intensity), 5 + (5 * intensity), 15 + (5 * intensity)),
                    entity -> entity instanceof LivingEntity && CapabilityUtil.getAffiliation(entity, true).equals(CapabilityUtil.getAffiliation(caster, false)))) {
                if (effect.effect(world, caster, otherEntity, otherEntity.getPos(), this, intensity, caster))
                    successfulCast = true;
            }
        } else {
            for (Entity otherEntity : world.getOtherEntities(null, caster.getBoundingBox()
                            .expand(15 + (5 * intensity), 5 + (5 * intensity), 15 + (5 * intensity)),
                    entity -> entity.getType().equals(caster.getType()))) {
                if (effect.effect(world, caster, otherEntity, otherEntity.getPos(), this, intensity, caster))
                    successfulCast = true;
            }
        }
        return !caster.isDead() && successfulCast;
    }
}
