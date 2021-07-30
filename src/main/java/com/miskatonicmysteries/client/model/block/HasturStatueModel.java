package com.miskatonicmysteries.client.model.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;

/**
 * statue_hastur - cybecat5555
 * Created using Tabula 8.0.0
 */
@Environment(EnvType.CLIENT)
public class HasturStatueModel extends StatueModel {
    public HasturStatueModel(ModelPart root) {
        super(RenderLayer::getEntitySolid, root);
    }

    public static TexturedModelData getTexturedModelData(){
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();
        ModelPartData body = root.addChild("body",
                ModelPartBuilder.create()
                        .uv(0, 16).cuboid(-2.5F, -8.0F, -2.5F, 5.0F, 10.0F, 5.0F),
                ModelTransform.of(0.0F, 14.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        ModelPartData head = body.addChild("head",
                ModelPartBuilder.create()
                        .uv(32, 0).cuboid(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F),
                ModelTransform.of(0.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        head.addChild("rCrown",
                ModelPartBuilder.create()
                        .uv(43, 18).cuboid(-0.5F, -1.7F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(-1.2F, -3.3F, -0.9F, 0.13962634F, 0.0F, 0.0F));
        head.addChild("pipe",
                ModelPartBuilder.create()
                        .cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F),
                ModelTransform.of(0.0F, -3.1F, 1.4F, -0.5235988F, 0.0F, 0.0F));
        ModelPartData lAntler01 = head.addChild("lAntler01",
                ModelPartBuilder.create()
                        .uv(57, 0).cuboid(-0.5F, -1.6F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(1.3F, -3.5F, -0.1F, -0.6981317F, 0.34906584F, 0.6981317F));
        ModelPartData lAntler02 = lAntler01.addChild("lAntler02",
                ModelPartBuilder.create()
                        .uv(57, 0).cuboid(-0.5F, -1.8F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, -1.5F, 0.0F, -0.7853982F, 0.0F, 0.13962634F));
        ModelPartData lAntler03 = lAntler02.addChild("lAntler03",
                ModelPartBuilder.create()
                        .uv(57, 0).cuboid(-0.5F, -1.8F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, -1.7F, 0.0F, 0.5235988F, 0.0F, -0.31415927F));
        lAntler03.addChild("lAntler04",
                ModelPartBuilder.create()
                        .uv(57, 0).cuboid(-0.5F, -1.8F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, -1.8F, 0.0F, -0.61086524F, 0.0F, -0.27925268F));
        ModelPartData lAntler06 = lAntler03.addChild("lAntler06",
                ModelPartBuilder.create()
                        .uv(57, 0).cuboid(-0.5F, -1.8F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, -1.4F, 0.0F, 1.0471976F, 0.5235988F, 0.0F));
        lAntler06.addChild("lAntler08",
                ModelPartBuilder.create()
                        .uv(57, 0).cuboid(-0.5F, -1.6F, -0.51F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(-0.1F, -1.8F, 0.0F, 0.0F, 0.0F, -0.5235988F));
        ModelPartData lAntler05 = lAntler02.addChild("lAntler05",
                ModelPartBuilder.create()
                        .uv(57, 0).cuboid(-0.5F, -1.8F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 1.7453293F, 0.34906584F, 0.0F));
        lAntler05.addChild("lAntler07",
                ModelPartBuilder.create()
                        .uv(57, 0).cuboid(-0.5F, -1.6F, -0.51F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(-0.1F, -1.8F, 0.0F, 0.0F, 0.0F, -0.5235988F));
        head.addChild("mCrown",
                ModelPartBuilder.create()
                        .uv(43, 18).cuboid(-0.5F, -1.7F, -0.49F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, -4.1F, -1.5F, 0.20943952F, 0.0F, 0.0F));
        ModelPartData rAntler01 = head.addChild("rAntler01",
                ModelPartBuilder.create()
                        .uv(57, 0).mirrored(true).cuboid(-0.5F, -1.6F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(-1.3F, -3.5F, -0.4F, -0.6981317F, -0.34906584F, -0.6981317F));
        ModelPartData rAntler02 = rAntler01.addChild("rAntler02",
                ModelPartBuilder.create()
                        .uv(57, 0).mirrored(true).cuboid(-0.5F, -1.8F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, -1.5F, 0.0F, -0.7853982F, 0.0F, -0.13962634F));
        ModelPartData rAntler03 = rAntler02.addChild("rAntler03",
                ModelPartBuilder.create()
                        .uv(57, 0).mirrored(true).cuboid(-0.5F, -1.8F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, -1.7F, 0.0F, 0.5235988F, 0.0F, 0.31415927F));
        ModelPartData rAntler06 = rAntler03.addChild("rAntler06",
                ModelPartBuilder.create()
                        .uv(57, 0).mirrored(true).cuboid(-0.5F, -1.8F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, -1.4F, 0.0F, 1.0471976F, -0.5235988F, 0.0F));
        rAntler06.addChild("rAntler08",
                ModelPartBuilder.create()
                        .uv(57, 0).mirrored(true).cuboid(-0.5F, -1.6F, -0.51F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.1F, -1.8F, 0.0F, 0.0F, 0.0F, 0.5235988F));
        rAntler03.addChild("rAntler04",
                ModelPartBuilder.create()
                        .uv(57, 0).mirrored(true).cuboid(-0.5F, -1.8F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, -1.8F, 0.0F, -0.61086524F, 0.0F, 0.27925268F));
        ModelPartData rAntler05 = rAntler02.addChild("rAntler05",
                ModelPartBuilder.create()
                        .uv(57, 0).mirrored(true).cuboid(-0.5F, -1.8F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 1.7453293F, -0.34906584F, 0.0F));
        rAntler05.addChild("rAntler07",
                ModelPartBuilder.create()
                        .uv(57, 0).mirrored(true).cuboid(-0.5F, -1.6F, -0.51F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.1F, -1.8F, 0.0F, 0.0F, 0.0F, 0.5235988F));
        head.addChild("lCrown",
                ModelPartBuilder.create()
                        .uv(43, 18).cuboid(-0.5F, -1.7F, -0.5F, 1.0F, 1.0F, 1.0F),
                ModelTransform.of(1.2F, -3.3F, -0.9F, 0.13962634F, 0.0F, 0.0F));
        ModelPartData lWing02a = body.addChild("lWing02a",
                ModelPartBuilder.create()
                        .uv(33, 9).cuboid(-0.5F, 0.0F, 0.8F, 1.0F, 5.0F, 3.0F),
                ModelTransform.of(0.6F, -7.8F, 0.1F, 0.08726646F, 0.9599311F, 0.0F));
        ModelPartData lWing02b = lWing02a.addChild("lWing02b",
                ModelPartBuilder.create()
                        .uv(42, 12).cuboid(-0.49F, -0.2F, -1.7F, 1.0F, 3.0F, 2.0F),
                ModelTransform.of(0.0F, 4.5F, 3.3F, 0.43633232F, 0.0F, 0.0F));
        ModelPartData lWing02c = lWing02b.addChild("lWing02c",
                ModelPartBuilder.create()
                        .uv(42, 12).cuboid(-0.5F, 0.0F, -0.9F, 1.0F, 2.0F, 2.0F),
                ModelTransform.of(0.0F, 2.5F, -0.9F, 0.34906584F, 0.0F, 0.0F));
        ModelPartData lWing02d = lWing02c.addChild("lWing02d",
                ModelPartBuilder.create()
                        .uv(58, 16).cuboid(-0.49F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, 1.7F, 0.0F, 0.6981317F, 0.0F, 0.0F));
        lWing02d.addChild("lWing02e",
                ModelPartBuilder.create()
                        .uv(58, 16).cuboid(-0.5F, 0.0F, -0.45F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, 1.7F, -0.1F, 0.5235988F, 0.0F, 0.0F));
        ModelPartData rWing01a = body.addChild("rWing01a",
                ModelPartBuilder.create()
                        .uv(50, 0).mirrored(true).cuboid(-0.5F, 0.0F, 0.8F, 1.0F, 6.0F, 4.0F),
                ModelTransform.of(-1.0F, -8.6F, 1.3F, 0.08726646F, -0.7853982F, 0.0F));
        ModelPartData rWing01b = rWing01a.addChild("rWing01b",
                ModelPartBuilder.create()
                        .uv(49, 11).mirrored(true).cuboid(-0.51F, -0.2F, -2.5F, 1.0F, 3.0F, 3.0F),
                ModelTransform.of(0.0F, 5.9F, 4.3F, -0.17453292F, 0.0F, 0.0F));
        ModelPartData rWing01c = rWing01b.addChild("rWing01c",
                ModelPartBuilder.create()
                        .uv(57, 10).mirrored(true).cuboid(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F),
                ModelTransform.of(0.0F, 2.5F, -0.9F, 0.2617994F, 0.0F, 0.0F));
        ModelPartData lWing01d_1 = rWing01c.addChild("lWing01d_1",
                ModelPartBuilder.create()
                        .uv(58, 16).mirrored(true).cuboid(-0.51F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, 1.7F, 0.0F, 0.57595867F, 0.0F, 0.0F));
        lWing01d_1.addChild("rWing01e",
                ModelPartBuilder.create()
                        .uv(58, 16).mirrored(true).cuboid(-0.5F, 0.0F, -0.45F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, 1.7F, -0.1F, 0.34906584F, 0.0F, 0.0F));
        ModelPartData rWing02a = body.addChild("rWing02a",
                ModelPartBuilder.create()
                        .uv(33, 9).mirrored(true).cuboid(-0.5F, 0.0F, 0.8F, 1.0F, 5.0F, 3.0F),
                ModelTransform.of(-0.6F, -7.8F, 0.1F, 0.08726646F, -0.9599311F, 0.0F));
        ModelPartData rWing02b = rWing02a.addChild("rWing02b",
                ModelPartBuilder.create()
                        .uv(42, 12).mirrored(true).cuboid(-0.51F, -0.2F, -1.7F, 1.0F, 3.0F, 2.0F),
                ModelTransform.of(0.0F, 4.5F, 3.3F, 0.43633232F, 0.0F, 0.0F));
        ModelPartData rWing02c = rWing02b.addChild("rWing02c",
                ModelPartBuilder.create()
                        .uv(42, 12).mirrored(true).cuboid(-0.5F, 0.0F, -0.9F, 1.0F, 2.0F, 2.0F),
                ModelTransform.of(0.0F, 2.5F, -0.9F, 0.34906584F, 0.0F, 0.0F));
        ModelPartData rWing02d = rWing02c.addChild("rWing02d",
                ModelPartBuilder.create()
                        .uv(58, 16).mirrored(true).cuboid(-0.51F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, 1.7F, 0.0F, 0.6981317F, 0.0F, 0.0F));
        rWing02d.addChild("rWing02e",
                ModelPartBuilder.create()
                        .uv(58, 16).mirrored(true).cuboid(-0.5F, 0.0F, -0.45F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, 1.7F, -0.1F, 0.5235988F, 0.0F, 0.0F));
        body.addChild("fabric",
                ModelPartBuilder.create()
                        .uv(23, 22).cuboid(-3.0F, 0.0F, -3.0F, 6.0F, 2.0F, 6.0F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        ModelPartData lWing01a = body.addChild("lWing01a",
                ModelPartBuilder.create()
                        .uv(50, 0).cuboid(-0.5F, 0.0F, 0.8F, 1.0F, 6.0F, 4.0F),
                ModelTransform.of(1.0F, -8.6F, 1.3F, 0.08726646F, 0.7853982F, 0.0F));
        ModelPartData lWing01b = lWing01a.addChild("lWing01b",
                ModelPartBuilder.create()
                        .uv(49, 11).cuboid(-0.49F, -0.2F, -2.5F, 1.0F, 3.0F, 3.0F),
                ModelTransform.of(0.0F, 5.9F, 4.3F, -0.17453292F, 0.0F, 0.0F));
        ModelPartData lWing01c = lWing01b.addChild("lWing01c",
                ModelPartBuilder.create()
                        .uv(57, 10).cuboid(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F),
                ModelTransform.of(0.0F, 2.5F, -0.9F, 0.2617994F, 0.0F, 0.0F));
        ModelPartData lWing01d = lWing01c.addChild("lWing01d",
                ModelPartBuilder.create()
                        .uv(58, 16).cuboid(-0.49F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, 1.7F, 0.0F, 0.57595867F, 0.0F, 0.0F));
        lWing01d.addChild("lWing01e",
                ModelPartBuilder.create()
                        .uv(58, 16).cuboid(-0.5F, 0.0F, -0.45F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, 1.7F, -0.1F, 0.34906584F, 0.0F, 0.0F));
        root.addChild("plinth",
                ModelPartBuilder.create()
                        .cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
                ModelTransform.of(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(data, 64, 32);
    }
}
