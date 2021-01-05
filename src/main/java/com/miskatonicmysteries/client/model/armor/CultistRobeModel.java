// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

package com.miskatonicmysteries.client.model.armor;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public class CultistRobeModel extends BipedEntityModel<LivingEntity> {
    private final ModelPart armorHead;
    private final ModelPart mainHood;
    private final ModelPart hoodFringeL01;
    private final ModelPart hoodFringeR01;
    private final ModelPart hoodFringeL02;
    private final ModelPart hoodFringeR03;
    private final ModelPart hoodPipe01;
    private final ModelPart hoodPipe02;
    private final ModelPart hoodLSide02;
    private final ModelPart hoodRSide02;
    private final ModelPart armorBody;
    private final ModelPart medallionStrapR_r1;
    private final ModelPart medallionStrapL_r1;
    private final ModelPart armorRightArm;
    private final ModelPart armorLeftArm;
    private final ModelPart armorLeftLeg;
    private final ModelPart tunicLeftFront;
    private final ModelPart tunicFront;
    private final ModelPart tunicLeft;
    private final ModelPart tunicLeft2;
    private final ModelPart tunicLeftBack;
    private final ModelPart tunicLeftBack2;
    private final ModelPart lowerLeftSkirt;
    private final ModelPart tunicLowerFront;
    private final ModelPart tunicFrontFringe_r1;
    private final ModelPart tunicLowerLeft;
    private final ModelPart tunicLeft4;
    private final ModelPart tunicLeftBack3;
    private final ModelPart tunicLeftBack4;
    private final ModelPart armorRightLeg;
    private final ModelPart tunicRightFront;
    private final ModelPart tunicRight;
    private final ModelPart tunicRightBack;
    private final ModelPart tunicRightBack2;
    private final ModelPart lowerRightSkirt;
    private final ModelPart tunicLowerFront2;
    private final ModelPart tunicRightFront4;
    private final ModelPart tunicRight3;
    private final ModelPart tunicRight4;
    private final ModelPart tunicRightBack3;
    private final ModelPart tunicRightBack4;

    public CultistRobeModel() {
        super(1);
        textureWidth = 128;
        textureHeight = 128;
        armorHead = new ModelPart(this);
        armorHead.setPivot(0.0F, 0.0F, 0.0F);
        head.addChild(armorHead);


        mainHood = new ModelPart(this);
        mainHood.setPivot(0.0F, 0.0F, 0.0F);
        armorHead.addChild(mainHood);
        mainHood.setTextureOffset(74, 0).addCuboid(-4.0F, -9.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.55F, false);

        hoodFringeL01 = new ModelPart(this);
        hoodFringeL01.setPivot(-2.3F, -9.2F, 0.1F);
        mainHood.addChild(hoodFringeL01);
        setRotationAngle(hoodFringeL01, 0.0F, 0.0F, 0.2269F);
        hoodFringeL01.setTextureOffset(64, 40).addCuboid(-1.2F, -0.8F, -5.09F, 1.0F, 10.0F, 9.0F, 0.0F, true);

        hoodFringeR01 = new ModelPart(this);
        hoodFringeR01.setPivot(1.7F, -9.2F, 0.1F);
        mainHood.addChild(hoodFringeR01);
        setRotationAngle(hoodFringeR01, 0.0F, 0.0F, -0.2269F);
        hoodFringeR01.setTextureOffset(64, 40).addCuboid(0.9F, -0.6F, -5.09F, 1.0F, 10.0F, 9.0F, 0.0F, false);

        hoodFringeL02 = new ModelPart(this);
        hoodFringeL02.setPivot(-3.3F, -9.45F, 0.1F);
        mainHood.addChild(hoodFringeL02);
        setRotationAngle(hoodFringeL02, 0.0F, 0.0F, -0.2618F);
        hoodFringeL02.setTextureOffset(88, 39).addCuboid(-0.4F, -0.6F, -5.07F, 4.0F, 1.0F, 9.0F, 0.0F, false);

        hoodFringeR03 = new ModelPart(this);
        hoodFringeR03.setPivot(0.6F, -10.15F, 0.1F);
        mainHood.addChild(hoodFringeR03);
        setRotationAngle(hoodFringeR03, 0.0F, 0.0F, 0.2618F);
        hoodFringeR03.setTextureOffset(88, 39).addCuboid(-0.8F, -0.6F, -5.08F, 4.0F, 1.0F, 9.0F, 0.0F, false);

        hoodPipe01 = new ModelPart(this);
        hoodPipe01.setPivot(0.0F, -7.3F, 2.7F);
        mainHood.addChild(hoodPipe01);
        hoodPipe01.setTextureOffset(75, 23).addCuboid(-4.5F, -2.5F, 0.0F, 9.0F, 10.0F, 2.0F, -0.01F, false);

        hoodPipe02 = new ModelPart(this);
        hoodPipe02.setPivot(0.0F, -0.3F, 1.0F);
        hoodPipe01.addChild(hoodPipe02);
        setRotationAngle(hoodPipe02, -0.4538F, 0.0F, 0.0F);
        hoodPipe02.setTextureOffset(109, 33).addCuboid(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        hoodLSide02 = new ModelPart(this);
        hoodLSide02.setPivot(-5.15F, 0.1F, 0.3F);
        mainHood.addChild(hoodLSide02);
        setRotationAngle(hoodLSide02, 0.0F, 0.0F, 0.3142F);
        hoodLSide02.setTextureOffset(79, 51).addCuboid(-0.5F, -0.9F, -5.25F, 6.0F, 2.0F, 9.0F, 0.0F, false);

        hoodRSide02 = new ModelPart(this);
        hoodRSide02.setPivot(5.15F, 0.1F, 0.3F);
        mainHood.addChild(hoodRSide02);
        setRotationAngle(hoodRSide02, 0.0F, 0.0F, -0.3142F);
        hoodRSide02.setTextureOffset(79, 51).addCuboid(-5.4F, -0.9F, -5.25F, 6.0F, 2.0F, 9.0F, 0.0F, false);

        armorBody = new ModelPart(this);
        armorBody.setPivot(0.0F, 0.0F, 0.0F);
        torso.addChild(armorBody);
        armorBody.setTextureOffset(0, 81).addCuboid(-4.5F, -0.01F, -2.5F, 9.0F, 12.0F, 5.0F, 0.0F, false);
        armorBody.setTextureOffset(1, 104).addCuboid(-1.5F, 4.5F, -3.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        armorBody.setTextureOffset(8, 109).addCuboid(2.55F, -0.75F, -2.65F, 1.0F, 1.0F, 5.0F, 0.0F, false);
        armorBody.setTextureOffset(8, 109).addCuboid(-3.55F, -0.75F, -2.65F, 1.0F, 1.0F, 5.0F, 0.0F, true);
        armorBody.setTextureOffset(6, 113).addCuboid(-3.3F, -0.76F, 1.36F, 6.0F, 1.0F, 1.0F, 0.0F, true);
        armorBody.setTextureOffset(82, 76).addCuboid(-5.0F, -0.25F, -3.0F, 10.0F, 1.0F, 6.0F, 0.25F, true);
        armorBody.setTextureOffset(83, 91).addCuboid(-5.0F, 1.25F, -3.0F, 10.0F, 4.0F, 6.0F, 0.25F, true);

        medallionStrapR_r1 = new ModelPart(this);
        medallionStrapR_r1.setPivot(-1.0F, 3.5F, -3.0F);
        armorBody.addChild(medallionStrapR_r1);
        setRotationAngle(medallionStrapR_r1, 0.0F, 0.0F, -0.4363F);
        medallionStrapR_r1.setTextureOffset(2, 111).addCuboid(-1.3F, -4.5F, 0.1F, 1.0F, 6.0F, 1.0F, 0.0F, true);

        medallionStrapL_r1 = new ModelPart(this);
        medallionStrapL_r1.setPivot(1.0F, 3.5F, -3.0F);
        armorBody.addChild(medallionStrapL_r1);
        setRotationAngle(medallionStrapL_r1, 0.0F, 0.0F, 0.4363F);
        medallionStrapL_r1.setTextureOffset(2, 111).addCuboid(0.3F, -4.5F, 0.1F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        armorRightArm = new ModelPart(this);
        armorRightArm.setPivot(0.0F, 0.0F, 0.0F);
        rightArm.addChild(armorRightArm);
        armorRightArm.setTextureOffset(47, 82).addCuboid(-4.5F, -2.9F, -2.5F, 5.0F, 12.0F, 5.0F, -0.1F, false);
        armorRightArm.setTextureOffset(50, 100).addCuboid(-4.3F, 5.0F, 2.4F, 4.0F, 4.0F, 2.0F, 0.0F, true);

        armorLeftArm = new ModelPart(this);
        armorLeftArm.setPivot(0.0F, 0.0F, 0.0F);
        leftArm.addChild(armorLeftArm);
        armorLeftArm.setTextureOffset(47, 82).addCuboid(-0.5F, -2.9F, -2.5F, 5.0F, 12.0F, 5.0F, -0.1F, true);
        armorLeftArm.setTextureOffset(50, 100).addCuboid(0.3F, 5.0F, 2.4F, 4.0F, 4.0F, 2.0F, 0.0F, false);

        armorLeftLeg = new ModelPart(this);
        armorLeftLeg.setPivot(0.0F, 0.0F, 0.0F);
        leftLeg.addChild(armorLeftLeg);


        tunicLeftFront = new ModelPart(this);
        tunicLeftFront.setPivot(1.99F, 0.0F, -2.1F);
        armorLeftLeg.addChild(tunicLeftFront);
        setRotationAngle(tunicLeftFront, -3.0194F, 0.0F, 3.1416F);
        tunicLeftFront.setTextureOffset(53, 65).addCuboid(-4.0F, 0.0F, -1.0F, 4.0F, 7.0F, 1.0F, 0.0F, false);

        tunicFront = new ModelPart(this);
        tunicFront.setPivot(0.01F, 10.0F, -1.0F);
        tunicLeftFront.addChild(tunicFront);
        setRotationAngle(tunicFront, 0.2618F, 0.0F, 0.0F);


        tunicLeft = new ModelPart(this);
        tunicLeft.setPivot(6.01F, 0.0F, -2.1F);
        armorLeftLeg.addChild(tunicLeft);
        setRotationAngle(tunicLeft, 0.1222F, 1.5708F, 0.0F);
        tunicLeft.setTextureOffset(23, 65).addCuboid(-4.0F, 0.0F, -1.0F, 4.0F, 7.0F, 1.0F, 0.0F, false);

        tunicLeft2 = new ModelPart(this);
        tunicLeft2.setPivot(0.01F, 10.0F, -1.0F);
        tunicLeft.addChild(tunicLeft2);
        setRotationAngle(tunicLeft2, 0.2618F, 0.0F, 0.0F);


        tunicLeftBack = new ModelPart(this);
        tunicLeftBack.setPivot(1.99F, 0.0F, 2.1F);
        armorLeftLeg.addChild(tunicLeftBack);
        setRotationAngle(tunicLeftBack, 3.0194F, 0.0F, 3.1416F);
        tunicLeftBack.setTextureOffset(53, 65).addCuboid(-4.0F, 0.0F, 0.0F, 4.0F, 7.0F, 1.0F, 0.0F, false);

        tunicLeftBack2 = new ModelPart(this);
        tunicLeftBack2.setPivot(0.01F, 10.0F, 1.0F);
        tunicLeftBack.addChild(tunicLeftBack2);
        setRotationAngle(tunicLeftBack2, -0.2618F, 0.0F, 0.0F);


        lowerLeftSkirt = new ModelPart(this);
        lowerLeftSkirt.setPivot(11.0F, 0.0F, 0.0F);
        armorLeftLeg.addChild(lowerLeftSkirt);


        tunicLowerFront = new ModelPart(this);
        tunicLowerFront.setPivot(-6.0F, 10.0F, -3.1F);
        lowerLeftSkirt.addChild(tunicLowerFront);
        setRotationAngle(tunicLowerFront, -3.0194F, 0.0F, -3.1416F);
        tunicLowerFront.setTextureOffset(53, 72).addCuboid(-1.01F, -3.1F, -0.77F, 4.0F, 3.0F, 1.0F, 0.0F, false);

        tunicFrontFringe_r1 = new ModelPart(this);
        tunicFrontFringe_r1.setPivot(1.0F, -1.0F, -0.4F);
        tunicLowerFront.addChild(tunicFrontFringe_r1);
        setRotationAngle(tunicFrontFringe_r1, 0.2618F, 0.0F, 0.0F);
        tunicFrontFringe_r1.setTextureOffset(53, 77).addCuboid(-2.0F, 0.8F, -0.6F, 4.0F, 2.0F, 1.0F, 0.0F, false);

        tunicLowerLeft = new ModelPart(this);
        tunicLowerLeft.setPivot(-4.99F, 0.0F, -2.1F);
        lowerLeftSkirt.addChild(tunicLowerLeft);
        setRotationAngle(tunicLowerLeft, 0.1222F, 1.5708F, 0.0F);
        tunicLowerLeft.setTextureOffset(23, 72).addCuboid(-4.0F, 7.0F, -1.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);

        tunicLeft4 = new ModelPart(this);
        tunicLeft4.setPivot(0.01F, 10.0F, -1.0F);
        tunicLowerLeft.addChild(tunicLeft4);
        setRotationAngle(tunicLeft4, 0.2618F, 0.0F, 0.0F);
        tunicLeft4.setTextureOffset(23, 78).addCuboid(-4.0F, 0.0F, 0.0F, 4.0F, 2.0F, 1.0F, 0.0F, false);

        tunicLeftBack3 = new ModelPart(this);
        tunicLeftBack3.setPivot(-9.01F, 0.0F, 2.1F);
        lowerLeftSkirt.addChild(tunicLeftBack3);
        setRotationAngle(tunicLeftBack3, 3.0194F, 0.0F, 3.1416F);
        tunicLeftBack3.setTextureOffset(53, 72).addCuboid(-4.0F, 7.0F, 0.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);

        tunicLeftBack4 = new ModelPart(this);
        tunicLeftBack4.setPivot(0.01F, 10.0F, 1.0F);
        tunicLeftBack3.addChild(tunicLeftBack4);
        setRotationAngle(tunicLeftBack4, -0.2618F, 0.0F, 0.0F);
        tunicLeftBack4.setTextureOffset(53, 77).addCuboid(-4.0F, 0.0F, -1.0F, 4.0F, 2.0F, 1.0F, 0.0F, false);

        armorRightLeg = new ModelPart(this);
        armorRightLeg.setPivot(0.0F, 0.0F, 0.0F);
        rightLeg.addChild(armorRightLeg);


        tunicRightFront = new ModelPart(this);
        tunicRightFront.setPivot(-2.01F, 0.0F, -2.1F);
        armorRightLeg.addChild(tunicRightFront);
        setRotationAngle(tunicRightFront, -0.1222F, 0.0F, 0.0F);
        tunicRightFront.setTextureOffset(53, 65).addCuboid(-4.0F, 0.0F, 0.0F, 4.0F, 7.0F, 1.0F, 0.0F, false);

        tunicRight = new ModelPart(this);
        tunicRight.setPivot(-6.01F, 0.0F, -2.1F);
        armorRightLeg.addChild(tunicRight);
        setRotationAngle(tunicRight, -0.1222F, 1.5708F, 0.0F);
        tunicRight.setTextureOffset(24, 65).addCuboid(-4.0F, 0.0F, 0.0F, 4.0F, 7.0F, 1.0F, 0.0F, false);

        tunicRightBack = new ModelPart(this);
        tunicRightBack.setPivot(-2.01F, 0.0F, 2.1F);
        armorRightLeg.addChild(tunicRightBack);
        setRotationAngle(tunicRightBack, 0.1222F, 0.0F, 0.0F);
        tunicRightBack.setTextureOffset(53, 65).addCuboid(-4.0F, 0.0F, -1.0F, 4.0F, 7.0F, 1.0F, 0.0F, false);

        tunicRightBack2 = new ModelPart(this);
        tunicRightBack2.setPivot(0.01F, 10.0F, -1.0F);
        tunicRightBack.addChild(tunicRightBack2);
        setRotationAngle(tunicRightBack2, 0.2618F, 0.0F, 0.0F);


        lowerRightSkirt = new ModelPart(this);
        lowerRightSkirt.setPivot(-4.0F, 0.0F, 0.0F);
        armorRightLeg.addChild(lowerRightSkirt);


        tunicLowerFront2 = new ModelPart(this);
        tunicLowerFront2.setPivot(1.99F, 0.0F, -2.1F);
        lowerRightSkirt.addChild(tunicLowerFront2);
        setRotationAngle(tunicLowerFront2, -0.1222F, 0.0F, 0.0F);
        tunicLowerFront2.setTextureOffset(53, 71).addCuboid(-4.0F, 7.0F, 0.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);

        tunicRightFront4 = new ModelPart(this);
        tunicRightFront4.setPivot(0.01F, 10.0F, 1.0F);
        tunicLowerFront2.addChild(tunicRightFront4);
        setRotationAngle(tunicRightFront4, -0.2618F, 0.0F, 0.0F);
        tunicRightFront4.setTextureOffset(53, 77).addCuboid(-4.0F, 0.0F, -1.0F, 4.0F, 2.0F, 1.0F, 0.0F, false);

        tunicRight3 = new ModelPart(this);
        tunicRight3.setPivot(-2.01F, 0.0F, -2.1F);
        lowerRightSkirt.addChild(tunicRight3);
        setRotationAngle(tunicRight3, -0.1222F, 1.5708F, 0.0F);
        tunicRight3.setTextureOffset(24, 72).addCuboid(-4.0F, 7.0F, 0.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);

        tunicRight4 = new ModelPart(this);
        tunicRight4.setPivot(0.01F, 10.0F, 1.0F);
        tunicRight3.addChild(tunicRight4);
        setRotationAngle(tunicRight4, -0.2618F, 0.0F, 0.0F);
        tunicRight4.setTextureOffset(23, 78).addCuboid(-4.0F, 0.0F, -1.0F, 4.0F, 2.0F, 1.0F, 0.0F, false);

        tunicRightBack3 = new ModelPart(this);
        tunicRightBack3.setPivot(1.99F, 0.0F, 2.1F);
        lowerRightSkirt.addChild(tunicRightBack3);
        setRotationAngle(tunicRightBack3, 0.1222F, 0.0F, 0.0F);
        tunicRightBack3.setTextureOffset(53, 72).addCuboid(-4.0F, 7.0F, -1.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);

        tunicRightBack4 = new ModelPart(this);
        tunicRightBack4.setPivot(0.01F, 10.0F, -1.0F);
        tunicRightBack3.addChild(tunicRightBack4);
        setRotationAngle(tunicRightBack4, 0.2618F, 0.0F, 0.0F);
        tunicRightBack4.setTextureOffset(53, 77).addCuboid(-4.0F, 0.0F, 0.0F, 4.0F, 2.0F, 1.0F, 0.0F, false);
    }

    @Override
    public void setAngles(LivingEntity livingEntity, float f, float g, float h, float i, float j) {
        super.setAngles(livingEntity, f, g, h, i, j);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        super.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }

}