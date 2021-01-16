package com.miskatonicmysteries.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.miskatonicmysteries.common.entity.HasturCultistEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;

/**
 * cultist_Hastur - cybercat5555
 * Created using Tabula 8.0.0
 */
@Environment(EnvType.CLIENT)
public class HasturCultistEntityModel extends BipedEntityModel<HasturCultistEntity> {
    public ModelPart robe;
    public ModelPart rightArmFolded;
    public ModelPart middleArmFolded;
    public ModelPart nose;
    public ModelPart hoodFringeL01;
    public ModelPart hoodFringeR01;
    public ModelPart hoodFringeL02;
    public ModelPart hoodFringeR03;
    public ModelPart hoodPipe01;
    public ModelPart hoodLSide02;
    public ModelPart hoodRSide02;
    public ModelPart leftMaskPlate;
    public ModelPart rightMaskPlate;
    public ModelPart hoodPipe02;
    public ModelPart leftArmFolded;

    public HasturCultistEntityModel() {
        super(1, 0, 128, 64);
        this.hoodFringeL01 = new ModelPart(this, 64, 40);
        this.hoodFringeL01.setPivot(2.3F, -9.2F, 0.1F);
        this.hoodFringeL01.addCuboid(0.2F, -0.8F, -5.09F, 1.0F, 10.0F, 9.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(hoodFringeL01, 0.0F, 0.0F, -0.22689280275926282F);
        this.hoodFringeR03 = new ModelPart(this, 88, 39);
        this.hoodFringeR03.mirror = true;
        this.hoodFringeR03.setPivot(-0.6F, -9.9F, 0.1F);
        this.hoodFringeR03.addCuboid(-3.2F, -0.6F, -5.08F, 4.0F, 1.0F, 9.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(hoodFringeR03, 0.0F, 0.0F, -0.2617993877991494F);
        this.middleArmFolded = new ModelPart(this, 40, 38);
        this.middleArmFolded.setPivot(0.0F, 3.0F, -1.0F);
        this.middleArmFolded.addCuboid(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(middleArmFolded, -0.7499680008872521F, 0.0F, 0.0F);
        this.rightLeg = new ModelPart(this, 0, 22);
        this.rightLeg.setPivot(-2.0F, 12.0F, 0.0F);
        this.rightLeg.addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.hoodFringeR01 = new ModelPart(this, 64, 40);
        this.hoodFringeR01.mirror = true;
        this.hoodFringeR01.setPivot(-1.7F, -9.2F, 0.1F);
        this.hoodFringeR01.addCuboid(-1.9F, -0.6F, -5.09F, 1.0F, 10.0F, 9.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(hoodFringeR01, 0.0F, 0.0F, 0.22689280275926282F);
        this.leftArm = new ModelPart(this, 40, 46);
        this.leftArm.mirror = true;
        this.leftArm.setPivot(5.0F, 2.0F, 0.0F);
        this.leftArm.addCuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.robe = new ModelPart(this, 0, 38);
        this.robe.setPivot(0.0F, 0.0F, 0.0F);
        this.robe.addCuboid(-4.0F, 0.0F, -3.0F, 8.0F, 20.0F, 6.0F, 0.5F, 0.5F, 0.5F);
        this.hoodFringeL02 = new ModelPart(this, 88, 39);
        this.hoodFringeL02.setPivot(3.3F, -9.2F, 0.1F);
        this.hoodFringeL02.addCuboid(-3.6F, -0.6F, -5.08F, 4.0F, 1.0F, 9.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(hoodFringeL02, 0.0F, 0.0F, 0.2617993877991494F);
        this.hoodRSide02 = new ModelPart(this, 79, 51);
        this.hoodRSide02.mirror = true;
        this.hoodRSide02.setPivot(-5.15F, 0.1F, 0.3F);
        this.hoodRSide02.addCuboid(-0.6F, -0.9F, -5.25F, 6.0F, 2.0F, 9.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(hoodRSide02, 0.0F, 0.0F, 0.3141592653589793F);
        this.head = new ModelPart(this, 0, 0);
        this.head.setPivot(0.0F, 0.0F, 0.0F);
        this.head.addCuboid(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        this.torso = new ModelPart(this, 16, 20);
        this.torso.setPivot(0.0F, 0.0F, 0.0F);
        this.torso.addCuboid(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.leftMaskPlate = new ModelPart(this, 68, 0);
        this.leftMaskPlate.setPivot(0.0F, -4.4F, -4.9F);
        this.leftMaskPlate.addCuboid(-0.1F, -2.5F, -0.5F, 4.0F, 7.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftMaskPlate, -0.12217304763960307F, -0.13962634015954636F, 0.0F);
        this.hoodPipe02 = new ModelPart(this, 109, 33);
        this.hoodPipe02.setPivot(0.0F, -0.3F, 1.0F);
        this.hoodPipe02.addCuboid(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(hoodPipe02, -0.45378560551852565F, 0.0F, 0.0F);
        this.hoodPipe01 = new ModelPart(this, 75, 23);
        this.hoodPipe01.setPivot(0.0F, -7.3F, 2.7F);
        this.hoodPipe01.addCuboid(-4.5F, -2.5F, 0.0F, 9.0F, 10.7F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.rightArm = new ModelPart(this, 40, 46);
        this.rightArm.setPivot(-5.0F, 2.0F, 0.0F);
        this.rightArm.addCuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.leftArmFolded = new ModelPart(this, 44, 22);
        this.leftArmFolded.mirror = true;
        this.leftArmFolded.setPivot(0.0F, 0.0F, 0.0F);
        this.leftArmFolded.addCuboid(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.leftLeg = new ModelPart(this, 0, 22);
        this.leftLeg.mirror = true;
        this.leftLeg.setPivot(2.0F, 12.0F, 0.0F);
        this.leftLeg.addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.nose = new ModelPart(this, 24, 0);
        this.nose.setPivot(0.0F, -2.0F, 0.0F);
        this.nose.addCuboid(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.rightMaskPlate = new ModelPart(this, 68, 0);
        this.rightMaskPlate.mirror = true;
        this.rightMaskPlate.setPivot(0.0F, -4.4F, -4.9F);
        this.rightMaskPlate.addCuboid(-3.9F, -2.5F, -0.5F, 4.0F, 7.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightMaskPlate, -0.12217304763960307F, 0.13962634015954636F, 0.0F);
        this.rightArmFolded = new ModelPart(this, 44, 22);
        this.rightArmFolded.setPivot(0.0F, 3.0F, -1.0F);
        this.rightArmFolded.addCuboid(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightArmFolded, -0.7499680008872521F, 0.0F, 0.0F);
        this.hoodLSide02 = new ModelPart(this, 79, 51);
        this.hoodLSide02.setPivot(5.15F, 0.1F, 0.3F);
        this.hoodLSide02.addCuboid(-5.5F, -0.9F, -5.25F, 6.0F, 2.0F, 9.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(hoodLSide02, 0.0F, 0.0F, -0.3141592653589793F);
        this.head.addChild(this.hoodFringeL01);
        this.head.addChild(this.hoodFringeR03);
        this.head.addChild(this.hoodFringeR01);
        this.head.addChild(this.hoodFringeL02);
        this.head.addChild(this.hoodRSide02);
        this.head.addChild(this.leftMaskPlate);
        this.hoodPipe01.addChild(this.hoodPipe02);
        this.head.addChild(this.hoodPipe01);
        this.rightArmFolded.addChild(this.leftArmFolded);
        this.head.addChild(this.nose);
        this.head.addChild(this.rightMaskPlate);
        this.head.addChild(this.hoodLSide02);
        helmet.visible = false;
    }

    @Override
    public void animateModel(HasturCultistEntity livingEntity, float f, float g, float h) {
        rightArmPose = livingEntity.getMainHandStack().isEmpty() ? ArmPose.EMPTY : livingEntity.isBlocking() && livingEntity.getMainHandStack().getItem().equals(Items.SHIELD) ? ArmPose.BLOCK : ArmPose.ITEM;
        leftArmPose = livingEntity.getOffHandStack().isEmpty() ? ArmPose.EMPTY : livingEntity.isBlocking() && livingEntity.getOffHandStack().getItem().equals(Items.SHIELD) ? ArmPose.BLOCK : ArmPose.ITEM;
        if (livingEntity.isLeftHanded()) {
            ArmPose tempPose = rightArmPose;
            rightArmPose = leftArmPose;
            leftArmPose = tempPose;
        }
        /*armsFolded = !livingEntity.isCasting();
        livingEntity.getItemsHand().forEach(stack -> {
            if (!stack.isEmpty()) armsFolded = false;
        });*/

        super.animateModel(livingEntity, f, g, h);
    }

    @Override
    public void setAngles(HasturCultistEntity livingEntity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        super.setAngles(livingEntity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);

        if (livingEntity.getHeadRollingTimeLeft() > 0) {
            this.head.roll = 0.3F * MathHelper.sin(0.45F * animationProgress);
            this.head.pitch = 0.4F;
        } else {
            this.head.roll = 0.0F;
        }

        if (livingEntity.isCasting()) {
            this.rightArm.pivotZ = 0.0F;
            this.rightArm.pivotX = -5.0F;
            this.leftArm.pivotZ = 0.0F;
            this.leftArm.pivotX = 5.0F;
            this.rightArm.pitch = MathHelper.cos(animationProgress * 0.6662F) * 0.25F;
            this.leftArm.pitch = MathHelper.cos(animationProgress * 0.6662F) * 0.25F;
            this.rightArm.roll = 2.3561945F;
            this.leftArm.roll = -2.3561945F;
            this.rightArm.yaw = 0.0F;
            this.leftArm.yaw = 0.0F;
        }
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        //if (!armsFolded) {
            ImmutableList.of(this.rightLeg, this.leftArm, this.robe, this.head, this.torso, this.rightArm, this.leftLeg).forEach((ModelPart) -> {
                ModelPart.render(matrices, vertices, light, overlay, red, green, blue, alpha);
            });
        /*} else {
            ImmutableList.of(this.middleArmFolded, this.rightLeg, this.robe, this.head, this.torso, this.leftLeg, this.rightArmFolded).forEach((ModelPart) -> {
                ModelPart.render(matrices, vertices, light, overlay, red, green, blue, alpha);
            });
        }*/
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelPart modelPart, float x, float y, float z) {
        modelPart.pitch = x;
        modelPart.yaw = y;
        modelPart.roll = z;
    }
}
