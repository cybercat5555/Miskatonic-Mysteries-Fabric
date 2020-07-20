package com.miskatonicmysteries.client.mixin;

import com.miskatonicmysteries.common.item.ItemGun;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class PlayerModelMixin{
    @Shadow public ModelPart head;
    @Shadow public ModelPart helmet;
    @Shadow public ModelPart torso;
    @Shadow public ModelPart rightArm;
    @Shadow public ModelPart leftArm;
    @Shadow public ModelPart rightLeg;
    @Shadow public ModelPart leftLeg;

    @Shadow public BipedEntityModel.ArmPose leftArmPose;
    @Shadow public BipedEntityModel.ArmPose rightArmPose;

    @Inject(method = "method_30155(Lnet/minecraft/entity/LivingEntity;)V", at = @At("HEAD"), cancellable = true)
    private void setGunAnglesLeft(LivingEntity entity, CallbackInfo info){
        if (entity.getActiveItem().getItem() instanceof ItemGun && leftArmPose == BipedEntityModel.ArmPose.BOW_AND_ARROW){
            ItemGun gun = (ItemGun) entity.getActiveItem().getItem();
            if (!ItemGun.isLoading(entity.getActiveItem())) {
                this.leftArm.yaw = 0.1F + this.head.yaw;
                this.leftArm.pitch = -1.5707964F + this.head.pitch;
                if (gun.isHeavy()) {
                    this.rightArm.yaw = -0.1F + this.head.yaw - 0.4F;
                    this.rightArm.pitch = -1.5707964F + this.head.pitch;
                }
            }
            info.cancel();
        }
    }

    @Inject(method = "method_30154(Lnet/minecraft/entity/LivingEntity;)V", at = @At("HEAD"), cancellable = true)
    private void setGunAnglesRight(LivingEntity entity, CallbackInfo info){
        if (entity.getActiveItem().getItem() instanceof ItemGun && rightArmPose == BipedEntityModel.ArmPose.BOW_AND_ARROW){
            ItemGun gun = (ItemGun) entity.getActiveItem().getItem();
            if (!ItemGun.isLoading(entity.getActiveItem())){
                this.rightArm.yaw = -0.1F + this.head.yaw;
                this.rightArm.pitch = -1.5707964F + this.head.pitch;
                if (gun.isHeavy()) {
                    this.leftArm.yaw = 0.1F + this.head.yaw + 0.4F;
                    this.leftArm.pitch = -1.5707964F + this.head.pitch;
                }
            }
            info.cancel();
        }
    }
}
