// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

package com.miskatonicmysteries.client.model.armor;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;

public class CultistRobesModel extends BipedEntityModel<LivingEntity> {
    private final EquipmentSlot slot;
    private final boolean wearingBoots;

    private final ModelPart lowerLeftSkirt;
    private final ModelPart lowerRightSkirt;

    public CultistRobesModel(EquipmentSlot slot, boolean wearingBoots) {
        super(RenderLayer::getArmorCutoutNoCull, 1, 0, 128, 128);
        this.slot = slot;
        this.wearingBoots = wearingBoots;

        ModelPart mainHood = new ModelPart(this);
        mainHood.setPivot(0.0F, 0.0F, 0.0F);
        head.addChild(mainHood);
        mainHood.setTextureOffset(74, 0).addCuboid(-4.0F, -9.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.55F, false);

        ModelPart hoodFringeL01 = new ModelPart(this);
        hoodFringeL01.setPivot(-2.3F, -9.2F, 0.1F);
        mainHood.addChild(hoodFringeL01);
        setRotationAngle(hoodFringeL01, 0.0F, 0.0F, 0.2269F);
        hoodFringeL01.setTextureOffset(64, 40).addCuboid(-1.2F, -0.8F, -5.09F, 1.0F, 10.0F, 9.0F, 0.0F, true);

        ModelPart hoodFringeR01 = new ModelPart(this);
        hoodFringeR01.setPivot(1.7F, -9.2F, 0.1F);
        mainHood.addChild(hoodFringeR01);
        setRotationAngle(hoodFringeR01, 0.0F, 0.0F, -0.2269F);
        hoodFringeR01.setTextureOffset(64, 40).addCuboid(0.9F, -0.6F, -5.09F, 1.0F, 10.0F, 9.0F, 0.0F, false);

        ModelPart hoodFringeL02 = new ModelPart(this);
        hoodFringeL02.setPivot(-3.3F, -9.45F, 0.1F);
        mainHood.addChild(hoodFringeL02);
        setRotationAngle(hoodFringeL02, 0.0F, 0.0F, -0.2618F);
        hoodFringeL02.setTextureOffset(88, 39).addCuboid(-0.4F, -0.6F, -5.07F, 4.0F, 1.0F, 9.0F, 0.0F, false);

        ModelPart hoodFringeR03 = new ModelPart(this);
        hoodFringeR03.setPivot(0.6F, -10.15F, 0.1F);
        mainHood.addChild(hoodFringeR03);
        setRotationAngle(hoodFringeR03, 0.0F, 0.0F, 0.2618F);
        hoodFringeR03.setTextureOffset(88, 39).addCuboid(-0.8F, -0.6F, -5.08F, 4.0F, 1.0F, 9.0F, 0.0F, false);

        ModelPart hoodPipe01 = new ModelPart(this);
        hoodPipe01.setPivot(0.0F, -7.3F, 2.7F);
        mainHood.addChild(hoodPipe01);
        hoodPipe01.setTextureOffset(75, 23).addCuboid(-4.5F, -2.5F, 0.0F, 9.0F, 10.0F, 2.0F, -0.01F, false);

        ModelPart hoodPipe02 = new ModelPart(this);
        hoodPipe02.setPivot(0.0F, -0.3F, 1.0F);
        hoodPipe01.addChild(hoodPipe02);
        setRotationAngle(hoodPipe02, -0.4538F, 0.0F, 0.0F);
        hoodPipe02.setTextureOffset(109, 33).addCuboid(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        ModelPart hoodLSide02 = new ModelPart(this);
        hoodLSide02.setPivot(-5.15F, 0.1F, 0.3F);
        mainHood.addChild(hoodLSide02);
        setRotationAngle(hoodLSide02, 0.0F, 0.0F, 0.3142F);
        hoodLSide02.setTextureOffset(79, 51).addCuboid(-0.5F, -0.9F, -5.25F, 6.0F, 2.0F, 9.0F, 0.0F, false);

        ModelPart hoodRSide02 = new ModelPart(this);
        hoodRSide02.setPivot(5.15F, 0.1F, 0.3F);
        mainHood.addChild(hoodRSide02);
        setRotationAngle(hoodRSide02, 0.0F, 0.0F, -0.3142F);
        hoodRSide02.setTextureOffset(79, 51).addCuboid(-5.4F, -0.9F, -5.25F, 6.0F, 2.0F, 9.0F, 0.0F, false);

        ModelPart armorBody = new ModelPart(this);
        armorBody.setPivot(0.0F, 0.0F, 0.0F);
        armorBody.setTextureOffset(0, 81).addCuboid(-4.5F, -0.01F, -2.5F, 9.0F, 12.0F, 5.0F, 0.0F, false);
        //medaillon and hood fringe
        armorBody.setTextureOffset(1, 104).addCuboid(-1.5F, 4.5F, -3.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        armorBody.setTextureOffset(8, 109).addCuboid(2.55F, -0.75F, -2.65F, 1.0F, 1.0F, 5.0F, 0.0F, false);
        armorBody.setTextureOffset(8, 109).addCuboid(-3.55F, -0.75F, -2.65F, 1.0F, 1.0F, 5.0F, 0.0F, true);
        armorBody.setTextureOffset(6, 113).addCuboid(-3.3F, -0.76F, 1.36F, 6.0F, 1.0F, 1.0F, 0.0F, true);
        armorBody.setTextureOffset(82, 76).addCuboid(-5.0F, -0.25F, -3.0F, 10.0F, 1.0F, 6.0F, 0.25F, true);
        armorBody.setTextureOffset(83, 91).addCuboid(-5.0F, 1.25F, -3.0F, 10.0F, 4.0F, 6.0F, 0.25F, true);
        torso.addChild(armorBody);

        ModelPart medallionStrapR_r1 = new ModelPart(this);
        medallionStrapR_r1.setPivot(-1.0F, 3.5F, -3.0F);
        armorBody.addChild(medallionStrapR_r1);
        setRotationAngle(medallionStrapR_r1, 0.0F, 0.0F, -0.4363F);
        medallionStrapR_r1.setTextureOffset(2, 111).addCuboid(-1.3F, -4.5F, 0.1F, 1.0F, 6.0F, 1.0F, 0.0F, true);

        ModelPart medallionStrapL_r1 = new ModelPart(this);
        medallionStrapL_r1.setPivot(1.0F, 3.5F, -3.0F);
        armorBody.addChild(medallionStrapL_r1);
        setRotationAngle(medallionStrapL_r1, 0.0F, 0.0F, 0.4363F);
        medallionStrapL_r1.setTextureOffset(2, 111).addCuboid(0.3F, -4.5F, 0.1F, 1.0F, 6.0F, 1.0F, 0.0F, false);


        ModelPart armorRightArm = new ModelPart(this);
        armorRightArm.setPivot(1, 0.0F, 0.0F);
        armorRightArm.setTextureOffset(47, 82).addCuboid(-4.5F, -2.9F, -2.5F, 5.0F, 12.0F, 5.0F, -0.1F, false);
        rightArm.addChild(armorRightArm);

        ModelPart rShoulder_r1 = new ModelPart(this);
        rShoulder_r1.setPivot(0.0F, -1.0F, 0.0F);
        armorRightArm.addChild(rShoulder_r1);
        setRotationAngle(rShoulder_r1, 0.0F, 3.1416F, 0.0873F);
        rShoulder_r1.setTextureOffset(0, 68).addCuboid(0.25F, -2.0F, -3.0F, 5.0F, 5.0F, 6.0F, 0.0F, false);

        ModelPart lArmSleeve02_r1 = new ModelPart(this);
        lArmSleeve02_r1.setPivot(-2.0F, 7.0F, 2.0F);
        armorRightArm.addChild(lArmSleeve02_r1);
        setRotationAngle(lArmSleeve02_r1, 0.5236F, 0.0F, 0.0F);
        lArmSleeve02_r1.setTextureOffset(50, 100).addCuboid(-2.3F, -2.2F, -0.5F, 4.0F, 4.0F, 2.0F, 0.0F, true);

        ModelPart armorLeftArm = new ModelPart(this);
        armorLeftArm.setPivot(-1, 0.0F, 0.0F);
        armorLeftArm.setTextureOffset(47, 82).addCuboid(-0.5F, -2.9F, -2.5F, 5.0F, 12.0F, 5.0F, -0.1F, true);
        leftArm.addChild(armorLeftArm);

        ModelPart lShoulder_r1 = new ModelPart(this);
        lShoulder_r1.setPivot(0.0F, -1.0F, 0.0F);
        armorLeftArm.addChild(lShoulder_r1);
        setRotationAngle(lShoulder_r1, 0.0F, 0.0F, -0.0873F);
        lShoulder_r1.setTextureOffset(0, 68).addCuboid(0.25F, -2.0F, -3.0F, 5.0F, 5.0F, 6.0F, 0.0F, false);

        ModelPart lArmSleeve02_r2 = new ModelPart(this);
        lArmSleeve02_r2.setPivot(2.0F, 7.0F, 2.0F);
        armorLeftArm.addChild(lArmSleeve02_r2);
        setRotationAngle(lArmSleeve02_r2, 0.5236F, 0.0F, 0.0F);
        lArmSleeve02_r2.setTextureOffset(50, 100).addCuboid(-1.7F, -2.2F, -0.5F, 4.0F, 4.0F, 2.0F, 0.0F, false);

        ModelPart armorLeftLeg = new ModelPart(this);
        armorLeftLeg.setPivot(-3.9f, 0.0F, 0.0F);
        leftLeg.addChild(armorLeftLeg);

        ModelPart tunicLeftFront = new ModelPart(this);
        tunicLeftFront.setPivot(1.99F, 0.0F, -2.1F);
        armorLeftLeg.addChild(tunicLeftFront);
        setRotationAngle(tunicLeftFront, -3.0194F, 0.0F, 3.1416F);
        tunicLeftFront.setTextureOffset(53, 65).addCuboid(-4.0F, 0.0F, -1.0F, 4.0F, 7.0F, 1.0F, 0.0F, false);

        ModelPart tunicFront = new ModelPart(this);
        tunicFront.setPivot(0.01F, 10.0F, -1.0F);
        tunicLeftFront.addChild(tunicFront);
        setRotationAngle(tunicFront, 0.2618F, 0.0F, 0.0F);

        ModelPart tunicLeft = new ModelPart(this);
        tunicLeft.setPivot(6.01F, 0.0F, -2.1F);
        armorLeftLeg.addChild(tunicLeft);
        setRotationAngle(tunicLeft, 0.1222F, 1.5708F, 0.0F);
        tunicLeft.setTextureOffset(23, 65).addCuboid(-4.0F, 0.0F, -1.0F, 4.0F, 7.0F, 1.0F, 0.0F, false);

        ModelPart tunicLeft2 = new ModelPart(this);
        tunicLeft2.setPivot(0.01F, 10.0F, -1.0F);
        tunicLeft.addChild(tunicLeft2);
        setRotationAngle(tunicLeft2, 0.2618F, 0.0F, 0.0F);

        ModelPart tunicLeftBack = new ModelPart(this);
        tunicLeftBack.setPivot(1.99F, 0.0F, 2.1F);
        armorLeftLeg.addChild(tunicLeftBack);
        setRotationAngle(tunicLeftBack, 3.0194F, 0.0F, 3.1416F);
        tunicLeftBack.setTextureOffset(53, 65).addCuboid(-4.0F, 0.0F, 0.0F, 4.0F, 7.0F, 1.0F, 0.0F, false);

        ModelPart tunicLeftBack2 = new ModelPart(this);
        tunicLeftBack2.setPivot(0.01F, 10.0F, 1.0F);
        tunicLeftBack.addChild(tunicLeftBack2);
        setRotationAngle(tunicLeftBack2, -0.2618F, 0.0F, 0.0F);

        lowerLeftSkirt = new ModelPart(this);
        lowerLeftSkirt.setPivot(11.0F, 0.0F, 0.0F);
        armorLeftLeg.addChild(lowerLeftSkirt);

        ModelPart tunicLowerFront = new ModelPart(this);
        tunicLowerFront.setPivot(-6.0F, 10.0F, -3.1F);
        lowerLeftSkirt.addChild(tunicLowerFront);
        setRotationAngle(tunicLowerFront, -3.0194F, 0.0F, -3.1416F);
        tunicLowerFront.setTextureOffset(53, 72).addCuboid(-1.01F, -3.1F, -0.77F, 4.0F, 3.0F, 1.0F, 0.0F, false);

        ModelPart tunicFrontFringe_r1 = new ModelPart(this);
        tunicFrontFringe_r1.setPivot(1.0F, -1.0F, -0.4F);
        tunicLowerFront.addChild(tunicFrontFringe_r1);
        setRotationAngle(tunicFrontFringe_r1, 0.2618F, 0.0F, 0.0F);
        tunicFrontFringe_r1.setTextureOffset(53, 77).addCuboid(-2.0F, 0.8F, -0.6F, 4.0F, 2.0F, 1.0F, 0.0F, false);

        ModelPart tunicLowerLeft = new ModelPart(this);
        tunicLowerLeft.setPivot(-4.99F, 0.0F, -2.1F);
        lowerLeftSkirt.addChild(tunicLowerLeft);
        setRotationAngle(tunicLowerLeft, 0.1222F, 1.5708F, 0.0F);
        tunicLowerLeft.setTextureOffset(23, 72).addCuboid(-4.0F, 7.0F, -1.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);

        ModelPart tunicLeft4 = new ModelPart(this);
        tunicLeft4.setPivot(0.01F, 10.0F, -1.0F);
        tunicLowerLeft.addChild(tunicLeft4);
        setRotationAngle(tunicLeft4, 0.2618F, 0.0F, 0.0F);
        tunicLeft4.setTextureOffset(23, 78).addCuboid(-4.0F, 0.0F, 0.0F, 4.0F, 2.0F, 1.0F, 0.0F, false);

        ModelPart tunicLeftBack3 = new ModelPart(this);
        tunicLeftBack3.setPivot(-9.01F, 0.0F, 2.1F);
        lowerLeftSkirt.addChild(tunicLeftBack3);
        setRotationAngle(tunicLeftBack3, 3.0194F, 0.0F, 3.1416F);
        tunicLeftBack3.setTextureOffset(53, 72).addCuboid(-4.0F, 7.0F, 0.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);

        ModelPart tunicLeftBack4 = new ModelPart(this);
        tunicLeftBack4.setPivot(0.01F, 10.0F, 1.0F);
        tunicLeftBack3.addChild(tunicLeftBack4);
        setRotationAngle(tunicLeftBack4, -0.2618F, 0.0F, 0.0F);
        tunicLeftBack4.setTextureOffset(53, 77).addCuboid(-4.0F, 0.0F, -1.0F, 4.0F, 2.0F, 1.0F, 0.0F, false);

        ModelPart armorRightLeg = new ModelPart(this);
        armorRightLeg.setPivot(3.9f, 0.0F, 0.0F);
        rightLeg.addChild(armorRightLeg);

        ModelPart tunicRightFront = new ModelPart(this);
        tunicRightFront.setPivot(-2.01F, 0.0F, -2.1F);
        armorRightLeg.addChild(tunicRightFront);
        setRotationAngle(tunicRightFront, -0.1222F, 0.0F, 0.0F);
        tunicRightFront.setTextureOffset(53, 65).addCuboid(-4.0F, 0.0F, 0.0F, 4.0F, 7.0F, 1.0F, 0.0F, false);

        ModelPart tunicRight = new ModelPart(this);
        tunicRight.setPivot(-6.01F, 0.0F, -2.1F);
        armorRightLeg.addChild(tunicRight);
        setRotationAngle(tunicRight, -0.1222F, 1.5708F, 0.0F);
        tunicRight.setTextureOffset(24, 65).addCuboid(-4.0F, 0.0F, 0.0F, 4.0F, 7.0F, 1.0F, 0.0F, false);

        ModelPart tunicRightBack = new ModelPart(this);
        tunicRightBack.setPivot(-2.01F, 0.0F, 2.1F);
        armorRightLeg.addChild(tunicRightBack);
        setRotationAngle(tunicRightBack, 0.1222F, 0.0F, 0.0F);
        tunicRightBack.setTextureOffset(53, 65).addCuboid(-4.0F, 0.0F, -1.0F, 4.0F, 7.0F, 1.0F, 0.0F, false);

        lowerRightSkirt = new ModelPart(this);
        lowerRightSkirt.setPivot(-4.0F, 0.0F, 0.0F);
        armorRightLeg.addChild(lowerRightSkirt);

        ModelPart tunicLowerFront2 = new ModelPart(this);
        tunicLowerFront2.setPivot(1.99F, 0.0F, -2.1F);
        lowerRightSkirt.addChild(tunicLowerFront2);
        setRotationAngle(tunicLowerFront2, -0.1222F, 0.0F, 0.0F);
        tunicLowerFront2.setTextureOffset(53, 71).addCuboid(-4.0F, 7.0F, 0.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);

        ModelPart tunicRightFront4 = new ModelPart(this);
        tunicRightFront4.setPivot(0.01F, 10.0F, 1.0F);
        tunicLowerFront2.addChild(tunicRightFront4);
        setRotationAngle(tunicRightFront4, -0.2618F, 0.0F, 0.0F);
        tunicRightFront4.setTextureOffset(53, 77).addCuboid(-4.0F, 0.0F, -1.0F, 4.0F, 2.0F, 1.0F, 0.0F, false);

        ModelPart tunicRight3 = new ModelPart(this);
        tunicRight3.setPivot(-2.01F, 0.0F, -2.1F);
        lowerRightSkirt.addChild(tunicRight3);
        setRotationAngle(tunicRight3, -0.1222F, 1.5708F, 0.0F);
        tunicRight3.setTextureOffset(24, 72).addCuboid(-4.0F, 7.0F, 0.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);

        ModelPart tunicRight4 = new ModelPart(this);
        tunicRight4.setPivot(0.01F, 10.0F, 1.0F);
        tunicRight3.addChild(tunicRight4);
        setRotationAngle(tunicRight4, -0.2618F, 0.0F, 0.0F);
        tunicRight4.setTextureOffset(23, 78).addCuboid(-4.0F, 0.0F, -1.0F, 4.0F, 2.0F, 1.0F, 0.0F, false);

        ModelPart tunicRightBack3 = new ModelPart(this);
        tunicRightBack3.setPivot(1.99F, 0.0F, 2.1F);
        lowerRightSkirt.addChild(tunicRightBack3);
        setRotationAngle(tunicRightBack3, 0.1222F, 0.0F, 0.0F);
        tunicRightBack3.setTextureOffset(53, 72).addCuboid(-4.0F, 7.0F, -1.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);

        ModelPart tunicRightBack4 = new ModelPart(this);
        tunicRightBack4.setPivot(0.01F, 10.0F, -1.0F);
        tunicRightBack3.addChild(tunicRightBack4);
        setRotationAngle(tunicRightBack4, 0.2618F, 0.0F, 0.0F);
        tunicRightBack4.setTextureOffset(53, 77).addCuboid(-4.0F, 0.0F, 0.0F, 4.0F, 2.0F, 1.0F, 0.0F, false);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        head.visible = slot == EquipmentSlot.HEAD;
        helmet.visible = slot == EquipmentSlot.HEAD;
        torso.visible = slot == EquipmentSlot.CHEST;
        leftArm.visible = slot == EquipmentSlot.CHEST;
        rightArm.visible = slot == EquipmentSlot.CHEST;
        leftLeg.visible = slot == EquipmentSlot.LEGS;
        rightLeg.visible = slot == EquipmentSlot.LEGS;
        lowerLeftSkirt.visible = !wearingBoots;
        lowerRightSkirt.visible = !wearingBoots;
        super.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }

    private void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }
}