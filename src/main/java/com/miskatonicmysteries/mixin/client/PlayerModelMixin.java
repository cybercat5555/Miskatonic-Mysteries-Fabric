package com.miskatonicmysteries.mixin.client;

import com.miskatonicmysteries.api.item.GunItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(BipedEntityModel.class)
public abstract class PlayerModelMixin {

	@Shadow
	public ModelPart head;
	@Shadow
	public ModelPart rightArm;
	@Shadow
	public ModelPart leftArm;

	@Shadow
	public BipedEntityModel.ArmPose leftArmPose;
	@Shadow
	public BipedEntityModel.ArmPose rightArmPose;

	@Inject(method = "positionLeftArm", at = @At("HEAD"), cancellable = true)
	private void setGunAnglesLeft(LivingEntity entity, CallbackInfo info) {
		if (entity.getActiveItem().getItem() instanceof GunItem && leftArmPose == BipedEntityModel.ArmPose.BOW_AND_ARROW) {
			GunItem gun = (GunItem) entity.getActiveItem().getItem();
			if (!GunItem.isLoading(entity.getActiveItem())) {
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

	@Inject(method = "positionRightArm", at = @At("HEAD"), cancellable = true)
	private void setGunAnglesRight(LivingEntity entity, CallbackInfo info) {
		if (entity.getActiveItem().getItem() instanceof GunItem && rightArmPose == BipedEntityModel.ArmPose.BOW_AND_ARROW) {
			GunItem gun = (GunItem) entity.getActiveItem().getItem();
			if (!GunItem.isLoading(entity.getActiveItem())) {
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
