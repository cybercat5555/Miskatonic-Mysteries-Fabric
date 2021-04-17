package com.miskatonicmysteries.mixin;

import com.miskatonicmysteries.api.interfaces.Appeasable;
import com.miskatonicmysteries.api.item.MMBookItem;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.Constants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitchEntity.class)
public abstract class WitchEntityMixin extends RaiderEntity implements Appeasable {
    private int appeaseTicks;
    private int holdTicks;

    protected WitchEntityMixin(EntityType<? extends RaiderEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void tryAttack(LivingEntity target, float pullProgress, CallbackInfo ci) {
        if (isAppeased()) {
            ci.cancel();
        }
    }

    @Inject(method = "tickMovement", at = @At("HEAD"), cancellable = true)
    private void tickMovement(CallbackInfo ci) {
        if (!world.isClient && isAppeased()) {
            if (getEquippedStack(EquipmentSlot.MAINHAND).getItem() != MMObjects.NECRONOMICON) {
                setAppeasedTicks(getAppeasedTicks() - 1);
            } else {
                if (getHoldTicks() > 0){
                    setHoldTicks(getHoldTicks() - 1);
                }else {
                    dropStack(MMBookItem.addKnowledge(Constants.Misc.WITCH_KNOWLEDGE, getEquippedStack(EquipmentSlot.MAINHAND)));
                    equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                }
            }
            super.tickMovement();
            ci.cancel();
        }
    }

    @Override
    public boolean isAppeased() {
        return appeaseTicks > 0;
    }

    @Override
    public void setAppeasedTicks(int ticks) {
        this.appeaseTicks = ticks;
    }

    @Override
    public int getAppeasedTicks() {
        return appeaseTicks;
    }

    @Override
    public void setHoldTicks(int holdTicks) {
        this.holdTicks = holdTicks;
    }

    @Override
    public int getHoldTicks() {
        return holdTicks;
    }

    @Environment(EnvType.CLIENT)
    @Inject(method = "handleStatus", at = @At("HEAD"), cancellable = true)
    public void handleStatus(byte status, CallbackInfo ci) {
        if (status == 14) {
            for (int i = 0; i < 5; ++i) {
                double d = this.random.nextGaussian() * 0.02D;
                double e = this.random.nextGaussian() * 0.02D;
                double f = this.random.nextGaussian() * 0.02D;
                this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getParticleX(1.0D), this.getRandomBodyY() + 1.0D, this.getParticleZ(1.0D), d, e, f);
            }
            ci.cancel();
        }
        super.handleStatus(status);
    }
}
