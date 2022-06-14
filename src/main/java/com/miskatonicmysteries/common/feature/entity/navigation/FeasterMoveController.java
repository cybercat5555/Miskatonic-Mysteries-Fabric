package com.miskatonicmysteries.common.feature.entity.navigation;

import com.miskatonicmysteries.common.feature.entity.FeasterEntity;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Vec3d;

public class FeasterMoveController {
    private Vec3d flightTarget;

    public void flyingTick() {
    }

    public Vec3d getFlightTarget() {
        return flightTarget == null ? Vec3d.ZERO : flightTarget;
    }

    public void setFlightTarget(Vec3d flightTarget){
        this.flightTarget = flightTarget;
    }

    public static class GroundMoveControl extends MoveControl {
        public GroundMoveControl(MobEntity entity) {
            super(entity);
        }

        @Override
        public void tick() {
            super.tick();
            if(this.state == State.STRAFE){

            }else if(this.state == State.JUMPING){

            }else if(this.state == State.MOVE_TO){

            }else{

            }
        }
    }

    public static class FlightMoveControl extends MoveControl {
        private FeasterEntity feasterEntity;
        public FlightMoveControl(FeasterEntity feasterEntity) {
            super(feasterEntity);
            this.feasterEntity = feasterEntity;
        }

        @Override
        public void tick() {
            super.tick();

        }
    }
}
