// Made with Blockbench 3.7.5
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
public class ShubAlternateMaskModel extends Model {
    private final ModelPart root;

    public ShubAlternateMaskModel(ModelPart root) {
        super(RenderLayer::getEntityCutout);
        this.root = root;

    }

    public static TexturedModelData getTexturedModelData() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();
        ModelPartData armorHead = root.addChild("armorHead",
                ModelPartBuilder.create(),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        ModelPartData head = armorHead.addChild("head",
                ModelPartBuilder.create()
                        .cuboid(-4.0F, -5.1F, -5.1F, 8.0F, 6.0F, 6.0F, new Dilation(0.15F, 0.15F, 0.15F)),
                ModelTransform.of(0.0F, -6.4F, 1.2F, 0.9599F, 0.0F, 0.0F));
        head.addChild("lUpperJaw",
                ModelPartBuilder.create()
                        .uv(16, 16).cuboid(-1.0F, -1.5F, -5.1F, 2.0F, 3.0F, 6.0F),
                ModelTransform.of(-2.9F, -2.0F, -4.8F, 0.1396F, -0.2269F, 0.0F));
        head.addChild("snout",
                ModelPartBuilder.create()
                        .uv(0, 12).cuboid(-2.5F, -1.0F, -5.8F, 5.0F, 2.0F, 6.0F),
                ModelTransform.of(0.0F, -4.2F, -4.7F, 0.4363F, 0.0F, 0.0F));
        head.addChild("rUpperJaw",
                ModelPartBuilder.create()
                        .uv(16, 16).cuboid(-1.0F, -1.5F, -5.1F, 2.0F, 3.0F, 6.0F),
                ModelTransform.of(2.9F, -2.0F, -4.8F, 0.1396F, 0.2269F, 0.0F));
        ModelPartData lHorn01 = head.addChild("lHorn01",
                ModelPartBuilder.create()
                        .uv(0, 25).cuboid(-0.7F, -2.4F, -1.0F, 2.0F, 2.0F, 2.0F),
                ModelTransform.of(1.4F, -4.2F, 0.3F, -1.0908F, 0.3054F, 0.3491F));
        ModelPartData lHorn2 = lHorn01.addChild("lHorn2",
                ModelPartBuilder.create()
                        .uv(1, 21).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.25F, 0.25F, 0.25F)),
                ModelTransform.of(0.3F, -2.4F, 0.0F, -0.2182F, 0.0F, 0.2618F));
        ModelPartData lHorn3 = lHorn2.addChild("lHorn3",
                ModelPartBuilder.create()
                        .uv(1, 21).cuboid(-0.4F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(-0.3F, -2.0F, 0.0F, -0.2618F, 0.0F, 0.2618F));
        lHorn3.addChild("lHorn4",
                ModelPartBuilder.create()
                        .uv(21, 28).cuboid(-0.4F, -1.6F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(-0.1F, -0.1F, -0.1F)),
                ModelTransform.of(0.0F, -2.0F, 0.0F, -0.1309F, 0.0F, -0.4363F));
        ModelPartData rHorn01 = head.addChild("rHorn01",
                ModelPartBuilder.create()
                        .uv(0, 25).cuboid(-1.3F, -2.4F, -1.0F, 2.0F, 2.0F, 2.0F),
                ModelTransform.of(-1.4F, -4.2F, 0.3F, -1.0908F, -0.3054F, -0.3491F));
        ModelPartData rHorn02 = rHorn01.addChild("rHorn02",
                ModelPartBuilder.create()
                        .uv(1, 21).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.25F, 0.25F, 0.25F)),
                ModelTransform.of(-0.3F, -2.4F, 0.0F, -0.2182F, 0.0F, -0.2618F));
        ModelPartData rHorn03 = rHorn02.addChild("rHorn03",
                ModelPartBuilder.create()
                        .uv(1, 21).cuboid(-0.6F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.3F, -2.0F, 0.0F, -0.2618F, 0.0F, -0.2618F));
        rHorn03.addChild("rHorn04",
                ModelPartBuilder.create()
                        .uv(21, 28).cuboid(-0.6F, -1.6F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(-0.1F, -0.1F, -0.1F)),
                ModelTransform.of(0.0F, -2.0F, 0.0F, -0.1309F, 0.0F, 0.4363F));
        ModelPartData strapL = armorHead.addChild("strapL",
                ModelPartBuilder.create()
                        .uv(4, 26).cuboid(0.0F, -0.5F, 4.0F, 1.0F, 1.0F, 5.0F),
                ModelTransform.of(3.3F, -5.3F, -4.4F, 0.0F, 0.0F, 0.0F));
        strapL.addChild("strapB",
                ModelPartBuilder.create()
                        .uv(13, 26).cuboid(-6.95F, -0.49F, -0.51F, 7.0F, 1.0F, 1.0F),
                ModelTransform.of(0.0F, 0.0F, 8.5F, 0.0F, 0.0F, 0.0F));
        armorHead.addChild("strapR",
                ModelPartBuilder.create()
                        .uv(4, 26).cuboid(-1.0F, -0.25F, 4.0F, 1.0F, 1.0F, 5.0F),
                ModelTransform.of(-3.3F, -5.55F, -4.4F, 0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(data, 32, 32);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}