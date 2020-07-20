package com.miskatonicmysteries.common.item;

public class ItemRifle extends ItemGun {
    public ItemRifle() {
        super();
        //ModelPredicateProviderRegistry
    }

    @Override
    public boolean isHeavy() {
        return true;
    }

    @Override
    public int getLoadingTime() {
        return 40;
    }

    @Override
    public int getCooldown() {
        return 20;
    }

    @Override
    public int getMaxDistance() {
        return 64;
    }

    @Override
    public int getDamage() {
        return 15;
    }

    @Override
    public int getMaxShots() {
        return 1;
    }
}
