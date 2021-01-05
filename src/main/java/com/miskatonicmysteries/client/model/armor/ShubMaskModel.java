// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

package com.miskatonicmysteries.client.model.armor;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class ShubMaskModel extends Model {
    private final ModelPart armorHead;
    private final ModelPart leftMaskPlate;
    private final ModelPart leftEar;
    private final ModelPart leftHorn00;
    private final ModelPart leftHorn01a;
    private final ModelPart leftHorn02;
    private final ModelPart leftHorn03;
    private final ModelPart strapL;
    private final ModelPart strapB;
    private final ModelPart rightMaskPlate;
    private final ModelPart rightEar;
    private final ModelPart rightHorn00;
    private final ModelPart rightHorn01a;
    private final ModelPart rightHorn02;
    private final ModelPart rightHorn03;
    private final ModelPart strapR;

    public ShubMaskModel() {
        super(RenderLayer::getEntityCutout);
        textureWidth = 32;
        textureHeight = 32;
        armorHead = new ModelPart(this);
        armorHead.setPivot(0.0F, 0.0F, 0.0F);


        leftMaskPlate = new ModelPart(this);
        leftMaskPlate.setPivot(0.0F, -5.3F, -4.9F);
        armorHead.addChild(leftMaskPlate);
        setRotationAngle(leftMaskPlate, -0.1222F, 0.1396F, 0.0F);
        leftMaskPlate.setTextureOffset(2, 0).addCuboid(-3.9F, -2.5F, -0.5F, 4.0F, 7.0F, 1.0F, 0.0F, true);

        leftEar = new ModelPart(this);
        leftEar.setPivot(-3.8F, -1.0F, 0.2F);
        leftMaskPlate.addChild(leftEar);
        setRotationAngle(leftEar, 0.3142F, 0.0F, 0.2094F);
        leftEar.setTextureOffset(14, 0).addCuboid(-2.6F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F, 0.0F, false);

        leftHorn00 = new ModelPart(this);
        leftHorn00.setPivot(-1.7F, -2.0F, 0.6F);
        leftMaskPlate.addChild(leftHorn00);
        setRotationAngle(leftHorn00, 0.0F, 0.0F, -0.384F);
        leftHorn00.setTextureOffset(4, 9).addCuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);

        leftHorn01a = new ModelPart(this);
        leftHorn01a.setPivot(0.0F, -1.1F, -0.4F);
        leftHorn00.addChild(leftHorn01a);
        setRotationAngle(leftHorn01a, 0.0F, 0.0F, -0.2094F);
        leftHorn01a.setTextureOffset(4, 9).addCuboid(-0.3F, -2.6F, -0.3F, 1.0F, 2.0F, 1.0F, 0.2F, false);

        leftHorn02 = new ModelPart(this);
        leftHorn02.setPivot(0.2F, -2.6F, 0.0F);
        leftHorn01a.addChild(leftHorn02);
        setRotationAngle(leftHorn02, 0.0F, 0.0F, -0.3142F);
        leftHorn02.setTextureOffset(12, 9).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        leftHorn03 = new ModelPart(this);
        leftHorn03.setPivot(0.0F, -1.8F, 0.0F);
        leftHorn02.addChild(leftHorn03);
        setRotationAngle(leftHorn03, 0.0F, 0.0F, -0.3142F);
        leftHorn03.setTextureOffset(12, 9).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        strapL = new ModelPart(this);
        strapL.setPivot(-3.3F, 0.0F, 0.0F);
        leftMaskPlate.addChild(strapL);
        setRotationAngle(strapL, 0.1222F, -0.1396F, 0.0F);
        strapL.setTextureOffset(12, 6).addCuboid(-1.0F, -0.5F, 0.0F, 1.0F, 1.0F, 9.0F, 0.0F, false);

        strapB = new ModelPart(this);
        strapB.setPivot(0.0F, 0.0F, 8.5F);
        strapL.addChild(strapB);
        strapB.setTextureOffset(13, 4).addCuboid(-0.3F, -0.5F, -0.5F, 7.0F, 1.0F, 1.0F, 0.0F, false);

        rightMaskPlate = new ModelPart(this);
        rightMaskPlate.setPivot(0.0F, -5.3F, -4.9F);
        armorHead.addChild(rightMaskPlate);
        setRotationAngle(rightMaskPlate, -0.1222F, -0.1396F, 0.0F);
        rightMaskPlate.setTextureOffset(2, 0).addCuboid(-0.1F, -2.5F, -0.5F, 4.0F, 7.0F, 1.0F, 0.0F, false);

        rightEar = new ModelPart(this);
        rightEar.setPivot(3.8F, -1.0F, 0.2F);
        rightMaskPlate.addChild(rightEar);
        setRotationAngle(rightEar, 0.3142F, 0.0F, -0.2094F);
        rightEar.setTextureOffset(14, 0).addCuboid(-0.4F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F, 0.0F, true);

        rightHorn00 = new ModelPart(this);
        rightHorn00.setPivot(1.7F, -2.0F, 0.6F);
        rightMaskPlate.addChild(rightHorn00);
        setRotationAngle(rightHorn00, 0.0F, 0.0F, 0.384F);
        rightHorn00.setTextureOffset(4, 9).addCuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);

        rightHorn01a = new ModelPart(this);
        rightHorn01a.setPivot(-0.3F, -1.1F, -0.4F);
        rightHorn00.addChild(rightHorn01a);
        setRotationAngle(rightHorn01a, 0.0F, 0.0F, 0.2094F);
        rightHorn01a.setTextureOffset(4, 9).addCuboid(-0.4F, -2.6F, -0.3F, 1.0F, 2.0F, 1.0F, 0.2F, false);

        rightHorn02 = new ModelPart(this);
        rightHorn02.setPivot(0.2F, -2.6F, 0.0F);
        rightHorn01a.addChild(rightHorn02);
        setRotationAngle(rightHorn02, 0.0F, 0.0F, 0.3142F);
        rightHorn02.setTextureOffset(12, 9).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        rightHorn03 = new ModelPart(this);
        rightHorn03.setPivot(0.0F, -1.8F, 0.0F);
        rightHorn02.addChild(rightHorn03);
        setRotationAngle(rightHorn03, 0.0F, 0.0F, 0.3142F);
        rightHorn03.setTextureOffset(12, 9).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        strapR = new ModelPart(this);
        strapR.setPivot(3.3F, 0.0F, 0.0F);
        rightMaskPlate.addChild(strapR);
        setRotationAngle(strapR, 0.1222F, 0.1396F, 0.0F);
        strapR.setTextureOffset(12, 6).addCuboid(0.0F, -0.4F, 0.0F, 1.0F, 1.0F, 9.0F, 0.0F, false);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        ImmutableList.of(this.armorHead).forEach((modelRenderer) -> {
            modelRenderer.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        });

    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }

}