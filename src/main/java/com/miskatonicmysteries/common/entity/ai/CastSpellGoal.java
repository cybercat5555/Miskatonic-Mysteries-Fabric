package com.miskatonicmysteries.common.entity.ai;

import com.miskatonicmysteries.common.entity.CastingMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;

import java.util.EnumSet;

public class CastSpellGoal<T extends PathAwareEntity & CastingMob> extends Goal {
    private final T entity;
    private int targetSeeingTicker;
    private final float squaredRange = 25;
    private int internalCooldown;

    public CastSpellGoal(T caster) {
        this.entity = caster;
        this.setControls(EnumSet.of(Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (internalCooldown > 0) {
            internalCooldown--;
        }
        return internalCooldown <= 0 && entity.getTarget() != null; //might add non-combat spells?
    }

    @Override
    public boolean shouldContinue() {
        return canStart() && entity.getCurrentSpell() != null;
    }

    @Override
    public void start() {
        entity.setAttacking(true);
        entity.setCurrentSpell(entity.selectSpell());
        entity.setCastTime(100);
    }

    @Override
    public void stop() {
        entity.setAttacking(false);
        entity.setCurrentSpell(null);
        entity.setCastTime(0);
        internalCooldown = 20;
    }

    @Override
    public void tick() {
        if (entity.getCurrentSpell() == null) {
            stop();
            return;
        }
        if (entity.isAttacking() && entity.getTarget() != null) {
            LivingEntity target = entity.getTarget();
            double distanceTo = entity.squaredDistanceTo(target.getX(), target.getY(), target.getZ());
            boolean canSee = entity.getVisibilityCache().canSee(target);
            boolean bl2 = this.targetSeeingTicker > 0;

            if (canSee != bl2) {
                this.targetSeeingTicker = 0;
            }

            if (canSee) {
                ++this.targetSeeingTicker;
            } else {
                --this.targetSeeingTicker;
            }

            if (distanceTo <= (double) this.squaredRange && this.targetSeeingTicker >= 20) {
                entity.getNavigation().stop();
            }

            if (distanceTo > squaredRange) {
                entity.getMoveControl().strafeTo(-0.5F, 0);
            }
            entity.lookAtEntity(target, 30.0F, 30.0F);
            entity.getLookControl().lookAt(target, 30.0F, 30.0F);
        }
        entity.setCastTime(entity.getCastTime() - 1);
        if (entity.getCastTime() <= 0) {
            entity.getCurrentSpell().cast(entity);
            stop();
            internalCooldown = 40;
        }
    }
}
