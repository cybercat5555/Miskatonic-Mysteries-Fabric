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

public class HasturMaskModel extends Model {
    private final ModelPart armorHead;
    private final ModelPart leftMaskPlate;
    private final ModelPart strapL;
    private final ModelPart strapB;
    private final ModelPart rightMaskPlate;
    private final ModelPart strapR;

    public HasturMaskModel() {
        super(RenderLayer::getEntityCutout);
        textureWidth = 32;
        textureHeight = 32;
        armorHead = new ModelPart(this);
        armorHead.setPivot(0.0F, 0.0F, 0.0F);

        leftMaskPlate = new ModelPart(this);
        leftMaskPlate.setPivot(0.0F, -5.3F, -4.9F);
        armorHead.addChild(leftMaskPlate);
        setRotationAngle(leftMaskPlate, -0.1222F, 0.1396F, 0.0F);
        leftMaskPlate.setTextureOffset(1, 0).addCuboid(-3.9F, -1.2F, -0.5F, 4.0F, 5.0F, 1.0F, 0.0F, true);

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
        rightMaskPlate.setTextureOffset(1, 0).addCuboid(-0.1F, -1.2F, -0.5F, 4.0F, 5.0F, 1.0F, 0.0F, false);

        strapR = new ModelPart(this);
        strapR.setPivot(3.3F, 0.0F, 0.0F);
        rightMaskPlate.addChild(strapR);
        setRotationAngle(strapR, 0.1222F, 0.1396F, 0.0F);
        strapR.setTextureOffset(12, 6).addCuboid(0.0F, -0.4F, 0.0F, 1.0F, 1.0F, 9.0F, 0.0F, false);
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