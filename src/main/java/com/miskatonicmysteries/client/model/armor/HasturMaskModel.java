// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

package com.miskatonicmysteries.client.model.armor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class HasturMaskModel extends Model {
    private final ModelPart root;

    public HasturMaskModel(ModelPart root) {
        super(RenderLayer::getEntityCutout);
        this.root = root;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();
        ModelPartData armorHead = root.addChild("armorHead",
                ModelPartBuilder.create(),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        ModelPartData leftMaskPlate = armorHead.addChild("leftMaskPlate",
                ModelPartBuilder.create()
                        .uv(1, 0).mirrored(true).cuboid(-3.9F, -1.2F, -0.5F, 4.0F, 5.0F, 1.0F),
                ModelTransform.of(0.0F, -5.3F, -4.9F, -0.1222F, 0.1396F, 0.0F));
        ModelPartData strapL = leftMaskPlate.addChild("strapL",
                ModelPartBuilder.create()
                        .uv(12, 6).cuboid(-1.0F, -0.5F, 0.0F, 1.0F, 1.0F, 9.0F),
                ModelTransform.of(-3.3F, 0.0F, 0.0F, 0.1222F, -0.1396F, 0.0F));
        strapL.addChild("strapB",
                ModelPartBuilder.create()
                        .uv(13, 4).cuboid(-0.3F, -0.5F, -0.5F, 7.0F, 1.0F, 1.0F),
                ModelTransform.of(0.0F, 0.0F, 8.5F, 0.0F, 0.0F, 0.0F));
        ModelPartData rightMaskPlate = armorHead.addChild("rightMaskPlate",
                ModelPartBuilder.create()
                        .uv(1, 0).cuboid(-0.1F, -1.2F, -0.5F, 4.0F, 5.0F, 1.0F),
                ModelTransform.of(0.0F, -5.3F, -4.9F, -0.1222F, -0.1396F, 0.0F));
        rightMaskPlate.addChild("strapR",
                ModelPartBuilder.create()
                        .uv(12, 6).cuboid(0.0F, -0.4F, 0.0F, 1.0F, 1.0F, 9.0F),
                ModelTransform.of(3.3F, 0.0F, 0.0F, 0.1222F, 0.1396F, 0.0F));
        return TexturedModelData.of(data, 32, 32);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}