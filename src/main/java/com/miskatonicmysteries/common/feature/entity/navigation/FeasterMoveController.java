package com.miskatonicmysteries.common.feature.entity.navigation;

import com.miskatonicmysteries.common.feature.entity.FeasterEntity;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class FeasterMoveController {
    private Vec3d flightTarget;
    private FeasterEntity feasterEntity;

    public FeasterMoveController(FeasterEntity feasterEntity) {
        this.feasterEntity = feasterEntity;
    }

    public void flyingTick() {
        LivingEntity entity = feasterEntity.getTarget();
        if(feasterEntity.getTarget() != null && feasterEntity.getTarget().isAlive()){
            setFlightTarget(new Vec3d(entity.getX(), entity.getY() + entity.getHeight()/2, entity.getZ()));
        }
    }

    public Vec3d getFlightTarget() {
        return flightTarget == null ? Vec3d.ZERO : flightTarget;
    }

    public void setFlightTarget(Vec3d flightTarget){
        this.flightTarget = flightTarget;
    }

    public static class GroundMoveControl extends MoveControl {
        public GroundMoveControl(FeasterEntity feasterEntity) {
            super(feasterEntity);
        }
    }

    public static class FlightMoveControl extends MoveControl {
        private final FeasterEntity feasterEntity;
        public FlightMoveControl(FeasterEntity feasterEntity) {
            super(feasterEntity);
            this.feasterEntity = feasterEntity;
        }

        @Override
        public void tick() {
            if (this.state == MoveControl.State.MOVE_TO) {
                feasterEntity.setNoGravity(true);
                double deltaX = feasterEntity.feasterMoveController.getFlightTarget().x - this.feasterEntity.getX();
                double deltaY = feasterEntity.feasterMoveController.getFlightTarget().y - this.feasterEntity.getY();
                double deltaZ = feasterEntity.feasterMoveController.getFlightTarget().z - this.feasterEntity.getZ();
                double distance = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
                if (distance < 2.500000277905201E-7) {
                    this.feasterEntity.setUpwardSpeed(0.0F);
                    this.feasterEntity.setForwardSpeed(0.0F);
                    return;
                }
                float newSpeed;
                if (this.feasterEntity.isOnGround()) {
                    newSpeed = (float) (this.speed * this.feasterEntity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
                } else {
                    newSpeed = (float) (this.speed * this.feasterEntity.getAttributeValue(EntityAttributes.GENERIC_FLYING_SPEED));
                }
                newSpeed *= Math.min(1, distance * 0.01 + 0.5F);
                feasterEntity.setMovementSpeed(newSpeed);

                double j = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
                if (Math.abs(deltaY) > 9.999999747378752E-6D || Math.abs(j) > 9.999999747378752E-6D) {
                    this.feasterEntity.setUpwardSpeed(deltaY > 0.0D ? newSpeed : -newSpeed);
                }
            }else{
                if (!feasterEntity.hasNoGravity()) {
                    this.feasterEntity.setNoGravity(false);
                }
            }
        }
    }
}































