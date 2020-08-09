package com.miskatonicmysteries.common.entity.ai;

import com.miskatonicmysteries.common.item.ItemGun;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

import java.util.EnumSet;

public class GunAttackGoal extends Goal {
    public static final int RANGE = 10;
    private final MobEntity entity;
    private int ticksToLockIn, ticksSeen;
    private boolean movingToLeft;
    private boolean backward;
    private int combatTicks = -1;

    public GunAttackGoal(MobEntity protagonist) {
        this.entity = protagonist;
        this.setControls(EnumSet.of(Goal.Control.LOOK, Control.MOVE));
    }

    @Override
    public boolean canStart() {
        return entity.getTarget() != null && entity.isHolding(item -> item instanceof ItemGun);
    }

    @Override
    public boolean shouldContinue() {
        return canStart();
    }

    @Override
    public void start() {
        entity.setAttacking(true);
    }

    @Override
    public void stop() {
        entity.setAttacking(false);
        entity.clearActiveItem();
    }

    @Override
    public void tick() {
        ItemStack gun = entity.getMainHandStack();
        LivingEntity target = entity.getTarget();
        double distanceToTarget = this.entity.distanceTo(target);
        double range = 10;
        float speed = 1.2F;
        entity.lookAtEntity(target, 45F, 45F);
        if (entity.canSee(target)) ticksSeen++;
        if (distanceToTarget <= range && this.ticksSeen >= 20) {
            this.entity.getNavigation().stop();
            ++this.combatTicks;
        } else {
            this.entity.getNavigation().startMovingTo(target, speed);
            this.combatTicks = -1;
        }

        if (this.combatTicks >= 20) {
            if ((double) this.entity.getRandom().nextFloat() < 0.3D) {
                this.movingToLeft = !this.movingToLeft;
            }

            if ((double) this.entity.getRandom().nextFloat() < 0.3D) {
                this.backward = !this.backward;
            }

            this.combatTicks = 0;
        }

        if (this.combatTicks > -1) {
            if (distanceToTarget > RANGE * 0.75F) {
                this.backward = false;
            } else if (distanceToTarget < RANGE * 0.25F) {
                this.backward = true;
            }
            this.entity.getMoveControl().strafeTo(this.backward ? -0.5F : 0.5F, this.movingToLeft ? 0.5F : -0.5F);


            if (!gun.isEmpty() && gun.getItem() instanceof ItemGun && entity.getTarget() != null) {
                if (ItemGun.isLoaded(gun)) {
                    ticksToLockIn++;
                    if (ticksToLockIn >= 20) {
                        entity.clearActiveItem();
                        ((ItemGun) gun.getItem()).shoot(entity.world, entity, gun);
                        ticksToLockIn = 0;
                    }
                } else {
                    ItemGun.setLoading(gun, true);
                    entity.setCurrentHand(Hand.MAIN_HAND);
                }
            }
        }
    }
}
