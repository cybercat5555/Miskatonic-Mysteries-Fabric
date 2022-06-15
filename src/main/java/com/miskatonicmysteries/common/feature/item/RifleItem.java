package com.miskatonicmysteries.common.feature.item;

import com.miskatonicmysteries.api.item.GunItem;
import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

public class RifleItem extends GunItem {

	public RifleItem() {
		super(new FabricItemSettings().group(Constants.MM_GROUP).maxCount(1).maxDamage(128));
	}

	@Override
	public int getLoadingTime() {
		return 40;
	}

	@Override
	public int getMaxShots() {
		return 1;
	}

	@Override
	public boolean isHeavy() {
		return true;
	}

	@Override
	public int getCooldown() {
		return 20;
	}

	@Override
	public int getDamage() {
		return 15;
	}

	@Override
	public int getMaxDistance() {
		return 64;
	}
}
