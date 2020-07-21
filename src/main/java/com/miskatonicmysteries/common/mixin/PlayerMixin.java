package com.miskatonicmysteries.common.mixin;

import com.miskatonicmysteries.common.item.ItemGun;
import com.miskatonicmysteries.lib.Constants;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.miskatonicmysteries.lib.Constants.DataTrackers.*;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker()V", at = @At("TAIL"))
    private void addMiskStats(CallbackInfo info){
        dataTracker.startTracking(SANITY, SANITY_CAP);
    }
}