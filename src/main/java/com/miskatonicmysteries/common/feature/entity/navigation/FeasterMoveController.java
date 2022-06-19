package com.miskatonicmysteries.common.feature.entity.navigation;

import com.miskatonicmysteries.common.feature.entity.FeasterEntity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class FeasterMoveController {

	private Vec3d flightTarget;
	private final FeasterEntity feasterEntity;

	public FeasterMoveController(FeasterEntity feasterEntity) {
		this.feasterEntity = feasterEntity;
	}

	public void flyingTick() {
		LivingEntity entity = feasterEntity.getTarget();
		if (feasterEntity.getTarget() != null && feasterEntity.getTarget().isAlive()) {
			setFlightTarget(new Vec3d(entity.getX(), entity.getY() + entity.getHeight() / 2, entity.getZ()));
		}
	}

	public Vec3d getFlightTarget() {
		return flightTarget == null ? Vec3d.ZERO : flightTarget;
	}

	public void setFlightTarget(Vec3d flightTarget) {
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
				this.state = MoveControl.State.WAIT;
				feasterEntity.setNoGravity(true);
				float deltaX = (float) (feasterEntity.feasterMoveController.getFlightTarget().x - this.feasterEntity.getX());
				float deltaY = (float) (feasterEntity.feasterMoveController.getFlightTarget().y - this.feasterEntity.getY());
				float deltaZ = (float) (feasterEntity.feasterMoveController.getFlightTarget().z - this.feasterEntity.getZ());
				double planeDistance = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ);
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
				float headYaw = feasterEntity.headYaw + 90.0F;
				double motionScaleX = speed * MathHelper.cos(headYaw * MathHelper.PI / 180.0F) * Math.abs(deltaX / distance);
				double motionScaleY = speed * MathHelper.sin((float)(-(MathHelper.atan2(-deltaY, planeDistance) * 180 / MathHelper.PI)) * MathHelper.PI / 180.0F) * Math.abs(deltaY / distance);
				double motionScaleZ = speed * MathHelper.sin(headYaw * MathHelper.PI / 180.0F) * Math.abs(deltaZ / distance);
				double motionMax = 0.2D;

				feasterEntity.setVelocity(feasterEntity.getVelocity().add(
						Math.min(motionScaleX * 0.2D, motionMax),
						Math.min(motionScaleY * 0.2D, motionMax),
						Math.min(motionScaleZ * 0.2D, motionMax)));
			} else {
				if (!feasterEntity.hasNoGravity()) {
					this.feasterEntity.setNoGravity(false);
				}
				this.entity.setUpwardSpeed(0.0F);
				this.entity.setForwardSpeed(0.0F);
			}
		}
	}
}































