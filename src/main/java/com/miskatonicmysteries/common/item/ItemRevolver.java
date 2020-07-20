package com.miskatonicmysteries.common.item;

public class ItemRevolver extends ItemGun {
    public ItemRevolver() {
        super();
        //ModelPredicateProviderRegistry
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
