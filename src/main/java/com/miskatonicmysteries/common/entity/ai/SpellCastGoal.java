package com.miskatonicmysteries.common.entity.ai;

import com.miskatonicmysteries.common.entity.EntityHasturCultist;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.feature.spell.SpellEffect;
import com.miskatonicmysteries.common.lib.ModRegistries;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.StatusEffects;

import java.util.EnumSet;

public class SpellCastGoal<T extends EntityHasturCultist> extends Goal {
    private T caster;
    private int progress;
    private int cooldown;

    public SpellCastGoal(T caster) {
        this.caster = caster;
        this.setControls(EnumSet.of(Control.MOVE));
        cooldown = 20;
        progress = 0;
    }


    @Override
    public boolean canStart() {
        return cooldown-- == 0 && caster.isAscended() && (caster.getTarget() != null && caster.distanceTo(caster.getTarget()) >= 4);
    }

    @Override
    public boolean shouldContinue() {
        return caster.isCasting() && !caster.isDead() && progress <= 100 && cooldown == 0;
    }

    @Override
    public void start() {
        SpellEffect effect = ModRegistries.EFFECT_HEAL;
        if (caster.hasStatusEffect(StatusEffects.RESISTANCE)) effect = ModRegistries.EFFECT_RESISTANCE;
        caster.currentSpell = new Spell(ModRegistries.MEDIUM_GROUP, effect, 2 + caster.getRandom().nextInt(2));
    }

    @Override
    public void stop() {
        caster.setCasting(false);
        caster.currentSpell = null;
        cooldown = 100;
    }

    @Override
    public void tick() {
        if (progress <= 100) {
            progress++;
        } else {
            caster.currentSpell.cast(caster);
            stop();
        }
    }
}
