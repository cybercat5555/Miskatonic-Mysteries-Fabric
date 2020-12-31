package com.miskatonicmysteries.common.entity.ai;

import com.miskatonicmysteries.common.entity.HasturCultistEntity;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.feature.spell.SpellEffect;
import com.miskatonicmysteries.common.feature.spell.SpellMedium;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.StatusEffects;

import java.util.EnumSet;

public class SpellCastGoal<T extends HasturCultistEntity> extends Goal {
    private T caster;
    private int progress;
    private int castTime;
    public SpellCastGoal(T caster) {
        this.caster = caster;
        this.setControls(EnumSet.of(Control.MOVE));
        progress = 0;
        castTime = 100;
    }


    @Override
    public boolean canStart() {
        return caster.isAscended() && caster.getRandom().nextFloat() < 0.25F && (caster.getTarget() != null && caster.distanceTo(caster.getTarget()) >= 3);
    }

    @Override
    public boolean shouldContinue() {
        return caster.currentSpell != null && caster.isCasting() && !caster.isDead() && progress <= castTime;
    }

    @Override
    public void start() {
        castTime = 60;
        SpellEffect effect = SpellEffect.HEAL;
        SpellMedium medium = SpellMedium.GROUP;
        int intensity = 2 + caster.getRandom().nextInt(2);
        if (caster.getTarget() != null && caster.getTarget().distanceTo(caster) < 6) {
            effect = SpellEffect.KNOCKBACK;
            medium = SpellMedium.MOB_TARGET;
            intensity++;
        } else if (caster.hasStatusEffect(StatusEffects.RESISTANCE)) effect = SpellEffect.RESISTANCE;
        caster.currentSpell = new Spell(medium, effect, intensity);
        caster.setCasting(true);
    }

    @Override
    public void stop() {
        progress = 0;
        caster.setCasting(false);
    }

    @Override
    public void tick() {
        if (caster.getTarget() != null && caster.currentSpell != null && caster.currentSpell.medium == SpellMedium.MOB_TARGET) {
            caster.lookAtEntity(caster.getTarget(), 30, 30);
        }
        progress++;
        if (++progress >= castTime) {
            caster.currentSpell.cast(caster);
            stop();
        }
    }
}
