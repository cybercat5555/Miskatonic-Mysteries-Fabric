package com.miskatonicmysteries.common.feature.entity.navigation;

import com.miskatonicmysteries.common.feature.entity.FeasterEntity;

public class FeasterLogic {

	private final FeasterEntity feasterEntity;

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


	}
}
