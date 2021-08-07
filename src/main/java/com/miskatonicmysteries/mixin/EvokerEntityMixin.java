package com.miskatonicmysteries.mixin;

import com.miskatonicmysteries.api.interfaces.Appeasable;
import net.minecraft.entity.mob.EvokerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EvokerEntity.class)
public class EvokerEntityMixin implements Appeasable {
    private int appeaseTicks;

    @Override
    public boolean isAppeased() {
        return getAppeasedTicks() > 0;
    }

    @Override
    public int getAppeasedTicks() {
        return appeaseTicks;
    }

    @Override
    public void setAppeasedTicks(int ticks) {
        this.appeaseTicks = ticks;
    }
}
