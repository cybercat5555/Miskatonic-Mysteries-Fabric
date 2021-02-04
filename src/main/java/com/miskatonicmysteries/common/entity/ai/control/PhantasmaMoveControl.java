package com.miskatonicmysteries.common.entity.ai.control;

import com.miskatonicmysteries.common.entity.PhantasmaEntity;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class PhantasmaMoveControl extends MoveControl {
    public PhantasmaMoveControl(PhantasmaEntity entity) {
        super(entity);
    }

    @Override
    public void tick() {
        if (this.state == MoveControl.State.MOVE_TO) {
            Vec3d vec3d = new Vec3d(this.targetX - entity.getX(), this.targetY - entity.getY(), this.targetZ - entity.getZ());
            double d = vec3d.length();
            if (d < entity.getBoundingBox().getAverageSideLength()) {
                this.state = MoveControl.State.WAIT;
                entity.setVelocity(entity.getVelocity().multiply(0.5D));
            } else {
                entity.setVelocity(entity.getVelocity().add(vec3d.multiply(this.speed * 0.05D / d)));
                if (entity.getTarget() == null) {
                    Vec3d vec3d2 = entity.getVelocity();
                    entity.yaw = -((float) MathHelper.atan2(vec3d2.x, vec3d2.z)) * 57.295776F;
                } else {
                    double e = entity.getTarget().getX() - entity.getX();
                    double f = entity.getTarget().getZ() - entity.getZ();
                    entity.yaw = -((float) MathHelper.atan2(e, f)) * 57.295776F;
                }
                entity.bodyYaw = entity.yaw;
            }

        }
    }
}
