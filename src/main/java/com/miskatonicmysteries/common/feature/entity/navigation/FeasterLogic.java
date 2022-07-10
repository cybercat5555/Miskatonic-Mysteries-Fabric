package com.miskatonicmysteries.common.feature.entity.navigation;

import com.miskatonicmysteries.common.feature.entity.FeasterEntity;
import net.minecraft.entity.player.PlayerEntity;

public class FeasterLogic {
	private final FeasterEntity feasterEntity;
	private int ticksTillNextNavigationSwitch = 20 * 3;
	private int flightTicks = 0;

	public FeasterLogic(FeasterEntity feasterEntity) {
		this.feasterEntity = feasterEntity;
	}

	public void updateLogic() {
		/*if(feasterEntity.isFlying()){
			flightTicks++;
		}else{
			if(shouldLand() && !feasterEntity.isOnGround()){
				feasterEntity.setVelocity(feasterEntity.getVelocity().add(0.0D,-0.1D,0.0D));
			}
		}


		if(feasterEntity.isOnGround() || feasterEntity.isSubmergedInWater()){
			flightTicks = 0;
		}

		if (feasterEntity.isFlying() && feasterEntity.navigationType != 1) {
			feasterEntity.changeEntityNavigation(1);
		}
		if (!feasterEntity.isFlying() && feasterEntity.navigationType != 0) {
			feasterEntity.changeEntityNavigation(0);
		}
		if (feasterEntity.isFlying()) {*/
			/*
			if (feasterEntity.isOnGround()) {
				feasterEntity.changeEntityNavigation(0);
			}

			 */
		} /*else {
			if (!feasterEntity.isOnGround()) {
				feasterEntity.changeEntityNavigation(1);
			}
		}



		if(--this.ticksTillNextNavigationSwitch < 0){
			if(this.feasterEntity.getTarget() == null || shouldLand()){
				ticksTillNextNavigationSwitch = 200 + this.feasterEntity.getRandom().nextInt(200);
				feasterEntity.changeEntityNavigation(0);
			}else if(feasterEntity.getTarget() instanceof PlayerEntity player && feasterEntity.squaredDistanceTo(player) > 8 && feasterEntity.navigationType == 0){
				ticksTillNextNavigationSwitch = this.feasterEntity.getRandom().nextInt(20 * 3);
				feasterEntity.changeEntityNavigation(1);
			}else{

			}
		}
	}*/

	public boolean shouldLand() {
		return flightTicks > 20 * 30;
	}

	public boolean shouldFly(){
		return feasterEntity.getTarget() == null;
	}
}
