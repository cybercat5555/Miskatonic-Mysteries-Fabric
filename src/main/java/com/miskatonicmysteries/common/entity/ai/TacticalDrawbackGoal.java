package com.miskatonicmysteries.common.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

import java.util.EnumSet;

public class TacticalDrawbackGoal<T extends MobEntity> extends Goal {
    private final float squaredRange = 25;
    private T mob;
    private int waitCooldown;
    private int targetSeeingTicker;

    public TacticalDrawbackGoal(T mob) {
        this.mob = mob;
        this.setControls(EnumSet.of(Control.MOVE));
    }


    @Override
    public boolean canStart() {
        return mob.getTarget() != null && (mob.age - mob.getLastAttackedTime()) <= 20 && (mob.age - mob.getLastAttackedTime()) > 10 && mob.getRandom().nextFloat() < 0.1F;
    }

    @Override
    public boolean shouldContinue() {
        return waitCooldown >= 0;
    }

    @Override
    public void start() {
        waitCooldown = 30 + mob.getRandom().nextInt(40);
    }

    @Override
    public void stop() {
        this.mob.clearActiveItem();
    }

    @Override
    public void tick() {
        LivingEntity livingEntity = this.mob.getTarget();
        boolean shield = mob.isHolding(Items.SHIELD);
        if (livingEntity != null) {
            double distanceTo = this.mob.squaredDistanceTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
            boolean canSee = this.mob.getVisibilityCache().canSee(livingEntity);
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
                this.mob.getNavigation().stop();
            }

            this.mob.getMoveControl().strafeTo(distanceTo > squaredRange ? 0.75F : -0.5F, 0);
            this.mob.lookAtEntity(livingEntity, 30.0F, 30.0F);
            this.mob.getLookControl().lookAt(livingEntity, 30.0F, 30.0F);

            if (distanceTo < (shield ? 9 : 16) && this.mob.getRandom().nextFloat() < 0.25F) {
                waitCooldown = 0;
                this.mob.clearActiveItem();
                this.mob.swingHand(Hand.MAIN_HAND);
                this.mob.tryAttack(mob.getTarget());
            }
            if (this.mob.isUsingItem() && !canSee && this.targetSeeingTicker < -60) {
                this.mob.clearActiveItem();
            } else if (--waitCooldown >= 0 && shield) {
                this.mob.setCurrentHand(ProjectileUtil.getHandPossiblyHolding(this.mob, Items.SHIELD));
            }
        }
    }
}
