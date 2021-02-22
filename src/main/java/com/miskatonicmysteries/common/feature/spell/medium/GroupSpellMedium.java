package com.miskatonicmysteries.common.feature.spell.medium;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class GroupSpellMedium extends SpellMedium {
    public GroupSpellMedium() {
        super(new Identifier(Constants.MOD_ID, "group"));
    }

    @Override
    public boolean cast(World world, LivingEntity caster, SpellEffect effect, int intensity, boolean backfires) {
        boolean successfulCast = false;
        if (caster instanceof Affiliated) {
            for (Entity otherEntity : world.getOtherEntities(null, caster.getBoundingBox()
                            .expand(15 + (5 * intensity), 5 + (5 * intensity), 15 + (5 * intensity)),
                    entity -> entity instanceof LivingEntity && MiskatonicMysteriesAPI.getNonNullAffiliation(entity, true).equals(MiskatonicMysteriesAPI.getNonNullAffiliation(caster, false)))) {
                if (effect.effect(world, caster, otherEntity, otherEntity.getPos(), this, intensity, caster, effect.backfires(caster)))
                    successfulCast = true;
            }
        } else {
            for (Entity otherEntity : world.getOtherEntities(null, caster.getBoundingBox()
                            .expand(15 + (5 * intensity), 5 + (5 * intensity), 15 + (5 * intensity)),
                    entity -> entity.getType().equals(caster.getType()))) {
                if (effect.effect(world, caster, otherEntity, otherEntity.getPos(), this, intensity, caster, backfires))
                    successfulCast = true;
            }
        }
        return !caster.isDead() && successfulCast;
    }

    @Override
    public float getBurnoutRate(LivingEntity caster) {
        return 0.8F;
    }
}
