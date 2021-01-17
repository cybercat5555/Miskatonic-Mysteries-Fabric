// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

package com.miskatonicmysteries.client.model.armor;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class ShubAlternateMaskModel extends Model {
    private final ModelPart armorHead;
    private final ModelPart head;
    private final ModelPart midEye_r1;
    private final ModelPart lUpperJaw;
    private final ModelPart snout;
    private final ModelPart rUpperJaw;
    private final ModelPart lHorn01;
    private final ModelPart lHorn2;
    private final ModelPart lHorn3;
    private final ModelPart lHorn4;
    private final ModelPart rHorn01;
    private final ModelPart rHorn02;
    private final ModelPart rHorn03;
    private final ModelPart rHorn04;
    private final ModelPart strapL;
    private final ModelPart strapB;
    private final ModelPart strapF;
    private final ModelPart strapR;

    public ShubAlternateMaskModel() {
        super(RenderLayer::getEntityCutout);
        textureWidth = 32;
        textureHeight = 32;
        armorHead = new ModelPart(this);
        armorHead.setPivot(0.0F, 0.0F, 0.0F);

        head = new ModelPart(this);
        head.setPivot(0.0F, -6.4F, -0.8F);
        armorHead.addChild(head);
        setRotationAngle(head, 0.9599F, 0.0F, 0.0F);
        head.setTextureOffset(0, 0).addCuboid(-3.0F, -5.1F, -5.1F, 6.0F, 6.0F, 6.0F, 0.0F, false);

        midEye_r1 = new ModelPart(this);
        midEye_r1.setPivot(0.0F, -5.0F, -3.0F);
        head.addChild(midEye_r1);
        setRotationAngle(midEye_r1, 1.6144F, 0.0F, 0.0F);
        midEye_r1.setTextureOffset(3, 2).addCuboid(-0.5F, -1.6F, 0.2F, 1.0F, 2.0F, 0.0F, 0.0F, false);

        lUpperJaw = new ModelPart(this);
        lUpperJaw.setPivot(-1.9F, -2.0F, -4.8F);
        head.addChild(lUpperJaw);
        setRotationAngle(lUpperJaw, 0.1396F, -0.2269F, 0.0F);
        lUpperJaw.setTextureOffset(17, 13).addCuboid(-1.0F, -1.5F, -4.1F, 2.0F, 3.0F, 5.0F, 0.0F, true);

        snout = new ModelPart(this);
        snout.setPivot(0.0F, -4.2F, -4.7F);
        head.addChild(snout);
        setRotationAngle(snout, 0.5236F, 0.0F, 0.0F);
        snout.setTextureOffset(0, 13).addCuboid(-1.5F, -1.0F, -4.8F, 3.0F, 2.0F, 5.0F, 0.0F, false);

        rUpperJaw = new ModelPart(this);
        rUpperJaw.setPivot(1.9F, -2.0F, -4.8F);
        head.addChild(rUpperJaw);
        setRotationAngle(rUpperJaw, 0.1396F, 0.2269F, 0.0F);
        rUpperJaw.setTextureOffset(17, 13).addCuboid(-1.0F, -1.5F, -4.1F, 2.0F, 3.0F, 5.0F, 0.0F, false);

        lHorn01 = new ModelPart(this);
        lHorn01.setPivot(1.4F, -4.2F, 0.3F);
        head.addChild(lHorn01);
        setRotationAngle(lHorn01, -1.0908F, 0.3054F, 0.3491F);
        lHorn01.setTextureOffset(0, 25).addCuboid(-0.7F, -2.4F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        lHorn2 = new ModelPart(this);
        lHorn2.setPivot(0.3F, -2.4F, 0.0F);
        lHorn01.addChild(lHorn2);
        setRotationAngle(lHorn2, -0.2182F, 0.0F, 0.2618F);
        lHorn2.setTextureOffset(2, 26).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.25F, false);

        lHorn3 = new ModelPart(this);
        lHorn3.setPivot(-0.3F, -2.0F, 0.0F);
        lHorn2.addChild(lHorn3);
        setRotationAngle(lHorn3, -0.2618F, 0.0F, 0.2618F);
        lHorn3.setTextureOffset(1, 21).addCuboid(-0.4F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        lHorn4 = new ModelPart(this);
        lHorn4.setPivot(0.0F, -2.0F, 0.0F);
        lHorn3.addChild(lHorn4);
        setRotationAngle(lHorn4, -0.1309F, 0.0F, -0.4363F);
        lHorn4.setTextureOffset(21, 28).addCuboid(-0.4F, -1.6F, -0.5F, 1.0F, 2.0F, 1.0F, -0.1F, false);

        rHorn01 = new ModelPart(this);
        rHorn01.setPivot(-1.4F, -4.2F, 0.3F);
        head.addChild(rHorn01);
        setRotationAngle(rHorn01, -1.0908F, -0.3054F, -0.3491F);
        rHorn01.setTextureOffset(0, 25).addCuboid(-1.3F, -2.4F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);

        rHorn02 = new ModelPart(this);
        rHorn02.setPivot(-0.3F, -2.4F, 0.0F);
        rHorn01.addChild(rHorn02);
        setRotationAngle(rHorn02, -0.2182F, 0.0F, -0.2618F);
        rHorn02.setTextureOffset(2, 26).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.25F, true);

        rHorn03 = new ModelPart(this);
        rHorn03.setPivot(0.3F, -2.0F, 0.0F);
        rHorn02.addChild(rHorn03);
        setRotationAngle(rHorn03, -0.2618F, 0.0F, -0.2618F);
        rHorn03.setTextureOffset(1, 21).addCuboid(-0.6F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        rHorn04 = new ModelPart(this);
        rHorn04.setPivot(0.0F, -2.0F, 0.0F);
        rHorn03.addChild(rHorn04);
        setRotationAngle(rHorn04, -0.1309F, 0.0F, 0.4363F);
        rHorn04.setTextureOffset(21, 28).addCuboid(-0.6F, -1.6F, -0.5F, 1.0F, 2.0F, 1.0F, -0.1F, true);

        strapL = new ModelPart(this);
        strapL.setPivot(3.3F, -5.3F, -4.4F);
        armorHead.addChild(strapL);
        strapL.setTextureOffset(0, 22).addCuboid(0.0F, -0.5F, 0.0F, 1.0F, 1.0F, 9.0F, 0.0F, true);

        strapB = new ModelPart(this);
        strapB.setPivot(0.0F, 0.0F, 8.5F);
        strapL.addChild(strapB);
        strapB.setTextureOffset(13, 26).addCuboid(-6.95F, -0.49F, -0.51F, 7.0F, 1.0F, 1.0F, 0.0F, true);

        strapF = new ModelPart(this);
        strapF.setPivot(0.0F, 0.0F, 0.5F);
        strapL.addChild(strapF);
        strapF.setTextureOffset(13, 4).addCuboid(-6.95F, -0.49F, -0.51F, 7.0F, 1.0F, 1.0F, 0.0F, true);

        strapR = new ModelPart(this);
        strapR.setPivot(-3.3F, -5.55F, -4.4F);
        armorHead.addChild(strapR);
        strapR.setTextureOffset(0, 22).addCuboid(-1.0F, -0.25F, 0.0F, 1.0F, 1.0F, 9.0F, 0.0F, true);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        ImmutableList.of(this.armorHead).forEach((modelRenderer) -> modelRenderer.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha));

    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }

}