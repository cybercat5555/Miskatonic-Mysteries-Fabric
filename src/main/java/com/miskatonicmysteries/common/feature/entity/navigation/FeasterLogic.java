package com.miskatonicmysteries.common.feature.entity.navigation;

import com.miskatonicmysteries.common.feature.entity.FeasterEntity;
import net.minecraft.entity.player.PlayerEntity;

public class FeasterLogic {
	private final FeasterEntity feasterEntity;
	private int ticksTillNextNavigationSwitch = 20 * 3;

	public FeasterLogic(FeasterEntity feasterEntity) {
		this.feasterEntity = feasterEntity;
	}

	public void updateLogic() {

		if (feasterEntity.isFlying() && feasterEntity.navigationType != 1) {
			feasterEntity.changeEntityNavigation(1);
		}
		if (!feasterEntity.isFlying() && feasterEntity.navigationType != 0) {
			feasterEntity.changeEntityNavigation(0);
		}
		if (feasterEntity.isFlying()) {
			if (feasterEntity.isOnGround()) {
				feasterEntity.setFlying(false);
			}
		} else {
			if (!feasterEntity.isOnGround()) {
				feasterEntity.setFlying(true);
			}
		}


		if(--this.ticksTillNextNavigationSwitch < 0){
			if(this.feasterEntity.getTarget() == null){
				this.ticksTillNextNavigationSwitch = 200 + this.feasterEntity.getRandom().nextInt(200);
				cycleNavigationType();
			}else if(this.feasterEntity.getTarget() instanceof PlayerEntity player && this.feasterEntity.squaredDistanceTo(player) > 16 && this.feasterEntity.navigationType == 1){
				this.ticksTillNextNavigationSwitch = this.feasterEntity.getRandom().nextInt(20 * 3);
				cycleNavigationType();
			}else{

			}
		}
	}

	public void cycleNavigationType(){
		switch (this.feasterEntity.navigationType){
			case 1 -> this.feasterEntity.navigationType = 0;
			case 0 -> this.feasterEntity.navigationType = 1;
		}
	}
}
