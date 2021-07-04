package com.miskatonicmysteries.mixin;

import com.miskatonicmysteries.api.interfaces.Appeasable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.EvokerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EvokerEntity.class)
public class EvokerEntityMixin implements Appeasable {
    private int appeaseTicks;
    @Override
    public boolean isAppeased() {
        return getAppeasedTicks() > 0;
    }

    @Override
    public void setAppeasedTicks(int ticks) {
        this.appeaseTicks = ticks;
    }

    @Override
    public int getAppeasedTicks() {
        return appeaseTicks;
    }
}
