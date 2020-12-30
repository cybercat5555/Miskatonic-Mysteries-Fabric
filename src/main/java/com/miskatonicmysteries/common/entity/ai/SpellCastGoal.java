package com.miskatonicmysteries.common.entity.ai;

import com.miskatonicmysteries.common.entity.HasturCultistEntity;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.feature.spell.SpellEffect;
import com.miskatonicmysteries.common.lib.ModRegistries;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.StatusEffects;

import java.util.EnumSet;

public class SpellCastGoal<T extends HasturCultistEntity> extends Goal {
    private T caster;
    private int progress;

    public SpellCastGoal(T caster) {
        this.caster = caster;
        this.setControls(EnumSet.of(Control.MOVE));
        progress = 0;
    }


    @Override
    public boolean canStart() {
        return caster.isAscended() && caster.getRandom().nextFloat() < 0.1F && (caster.getTarget() != null && caster.distanceTo(caster.getTarget()) >= 4);
    }

    @Override
    public boolean shouldContinue() {
        return caster.currentSpell != null && caster.isCasting() && !caster.isDead() && progress <= 100 && (caster.getTarget() == null || caster.getTarget().distanceTo(caster) >= 4);
    }

    @Override
    public void start() {
        SpellEffect effect = ModRegistries.EFFECT_HEAL;
        if (caster.hasStatusEffect(StatusEffects.RESISTANCE)) effect = ModRegistries.EFFECT_RESISTANCE;
        caster.currentSpell = new Spell(ModRegistries.MEDIUM_GROUP, effect, 2 + caster.getRandom().nextInt(2));
        caster.setCasting(true);
    }

    @Override
    public void stop() {
        progress = 0;
        caster.setCasting(false);
    }

    @Override
    public void tick() {
        progress++;
        if (++progress >= 100) {
            caster.currentSpell.cast(caster);
            stop();
        }
    }
}
