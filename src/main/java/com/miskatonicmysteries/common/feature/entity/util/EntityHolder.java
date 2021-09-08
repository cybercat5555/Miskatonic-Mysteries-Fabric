package com.miskatonicmysteries.common.feature.entity.util;

import net.minecraft.entity.Entity;

public interface EntityHolder {
    Entity getHeldEntity();

    void setHeldEntity(Entity entity);
}
