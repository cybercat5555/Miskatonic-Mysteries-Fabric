package com.miskatonicmysteries.common.mixin;

import com.miskatonicmysteries.common.item.ItemGun;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    /*@Inject(method = "clearActiveItem()V", at = @At("HEAD"), cancellable = true)
    private void dontStopUsingGun(CallbackInfo info){
        PlayerEntity p;
        if (activeItemStack.getItem() instanceof ItemGun){
            if (((PlayerEntity) (Object) this).getItemCooldownManager().isCoolingDown(activeItemStack.getItem()) && !ItemGun.isLoaded(activeItemStack))
                info.cancel();
        }
    }*/
}