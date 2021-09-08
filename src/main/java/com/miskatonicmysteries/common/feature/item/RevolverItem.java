package com.miskatonicmysteries.common.feature.item;

import com.miskatonicmysteries.api.item.GunItem;
import com.miskatonicmysteries.common.util.Constants;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

public class RevolverItem extends GunItem {
    public RevolverItem() {
        super(new FabricItemSettings().group(Constants.MM_GROUP).maxCount(1).maxDamage(128));
    }

    @Override
    public boolean isHeavy() {
        return false;
    }

    @Override
    public int getLoadingTime() {
        return 20;
    }

    @Override
    public int getCooldown() {
        return 10;
    }

    @Override
    public int getMaxDistance() {
        return 24;
    }

    @Override
    public int getDamage() {
        return 5;
    }

    @Override
    public int getMaxShots() {
        return 6;
    }
}
