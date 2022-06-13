package com.miskatonicmysteries.common.feature.entity.navigation;

import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.mob.MobEntity;

public class FeasterMoveController {

    public void tick() {
    }

    public static class GroundMover extends MoveControl {
        public GroundMover(MobEntity entity) {
            super(entity);
        }
    }

    public static class FlightMover extends MoveControl {
        public FlightMover(MobEntity entity) {
            super(entity);
        }
    }
}
