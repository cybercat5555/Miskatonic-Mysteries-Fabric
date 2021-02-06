package com.miskatonicmysteries.common.entity;

import com.miskatonicmysteries.common.entity.ai.FloatyWanderAroundGoal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class AberrationEntity extends PhantasmaEntity implements Monster {
    public AberrationEntity(EntityType<? extends PhantasmaEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public int getMaxVariants() {
        return 1;
    }

    @Override
    public void tick() {
        if (getTarget() != null && distanceTo(getTarget()) < 2) {
            swingHand(Hand.MAIN_HAND, false);
        }
        super.tick();
    }

    @Override
    public void tickMovement() {
        if (this.handSwinging) {
            ++this.handSwingTicks;
            if (this.handSwingTicks >= 20) {
                this.handSwingTicks = 0;
                this.handSwinging = false;
            }
        } else {
            this.handSwingTicks = 0;
        }
        this.handSwingProgress = (float) this.handSwingTicks / 20F;
        super.tickMovement();
    }

    @Override
    public void swingHand(Hand hand) {
        super.swingHand(hand);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new MeleeAttackGoal(this, 1, false));
        this.goalSelector.add(1, new FloatyWanderAroundGoal(this, 100));
        this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.targetSelector.add(0, new FollowTargetGoal(this, PlayerEntity.class, 10, true, false, null));
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 20, this::animationPredicate));
    }

    @Override
    public void dealDamage(LivingEntity attacker, Entity target) {
        super.dealDamage(attacker, target);
    }

    @Override
    protected void applyDamage(DamageSource source, float amount) {
        super.applyDamage(source, amount);
    }

    @Override
    public boolean tryAttack(Entity target) {
        return super.tryAttack(target);
    }

    @Override
    public void swingHand(Hand hand, boolean bl) {
        super.swingHand(hand, bl);
    }

    public <P extends IAnimatable> PlayState animationPredicate(AnimationEvent<P> event) {
        float limbSwingAmount = event.getLimbSwingAmount();
        boolean isMoving = !(limbSwingAmount > -0.15F && limbSwingAmount < 0.15F);
        if (isAttacking() && !isDead() && handSwinging) { //somehow not synced properly???
            event.getController().setAnimation(new AnimationBuilder().addAnimation("attack", true));
            return PlayState.CONTINUE;
        }
        if (isMoving) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("floating", true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
        }
        return PlayState.CONTINUE;
    }
}
