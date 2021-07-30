package com.miskatonicmysteries.client.model.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;

/**
 * statue_shub - cybecat5555
 * Created using Tabula 8.0.0
 */
@Environment(EnvType.CLIENT)
public class ShubStatueModel extends StatueModel {

    public ShubStatueModel(ModelPart root) {
        super(RenderLayer::getEntitySolid, root, root.getChild("body"), root.getChild("plinth"), root.getChild("body").getChild("chest").getChild("head"));
    }

    public static TexturedModelData getTexturedModelData(){
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();
        ModelPartData body = root.addChild("body",
                ModelPartBuilder.create()
                        .uv(19, 17).cuboid(-2.5F, -4.0F, -1.5F, 5.0F, 4.0F, 3.0F),
                ModelTransform.of(0.0F, 16.2F, 0.8F, 0.43633232F, 0.0F, 0.0F));
        ModelPartData rTentacle01a = body.addChild("rTentacle01a",
                ModelPartBuilder.create()
                        .uv(41, 4).mirrored(true).cuboid(-1.5F, -1.5F, -1.0F, 3.0F, 3.0F, 4.0F),
                ModelTransform.of(0.9F, -1.8F, 0.7F, -0.4537856F, 0.13962634F, -0.08726646F));
        ModelPartData rTentacle01b = rTentacle01a.addChild("rTentacle01b",
                ModelPartBuilder.create()
                        .uv(41, 12).mirrored(true).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F),
                ModelTransform.of(0.0F, -0.4F, 2.5F, -0.5235988F, -0.13962634F, 0.0F));
        ModelPartData rTentacle01c = rTentacle01b.addChild("rTentacle01c",
                ModelPartBuilder.create()
                        .uv(41, 12).mirrored(true).cuboid(-0.99F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F),
                ModelTransform.of(0.0F, 0.0F, 1.6F, -0.5235988F, 0.0F, 0.0F));
        ModelPartData rTentacle01d = rTentacle01c.addChild("rTentacle01d",
                ModelPartBuilder.create()
                        .uv(52, 10).mirrored(true).cuboid(-1.01F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F),
                ModelTransform.of(0.0F, 0.0F, 1.6F, -0.43633232F, 0.0F, 0.0F));
        ModelPartData rTentacle01e = rTentacle01d.addChild("rTentacle01e",
                ModelPartBuilder.create()
                        .uv(40, 17).mirrored(true).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 3.0F),
                ModelTransform.of(0.0F, -0.1F, 3.5F, 0.6981317F, 0.0F, 0.0F));
        rTentacle01e.addChild("rTentacle01f",
                ModelPartBuilder.create()
                        .uv(51, 17).mirrored(true).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 3.0F),
                ModelTransform.of(0.0F, 0.0F, 2.8F, 0.40142572F, 0.0F, 0.0F));
        ModelPartData lTentacle01a = body.addChild("lTentacle01a",
                ModelPartBuilder.create()
                        .uv(41, 4).cuboid(-1.5F, -1.5F, -1.0F, 3.0F, 3.0F, 4.0F),
                ModelTransform.of(-0.9F, -1.8F, 0.7F, -0.4537856F, -0.13962634F, 0.08726646F));
        ModelPartData lTentacle01b = lTentacle01a.addChild("lTentacle01b",
                ModelPartBuilder.create()
                        .uv(41, 12).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F),
                ModelTransform.of(0.0F, -0.4F, 2.5F, -0.5235988F, 0.13962634F, 0.0F));
        ModelPartData lTentacle01c = lTentacle01b.addChild("lTentacle01c",
                ModelPartBuilder.create()
                        .uv(41, 12).cuboid(-1.01F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F),
                ModelTransform.of(0.0F, 0.0F, 1.6F, -0.5235988F, 0.0F, 0.0F));
        ModelPartData lTentacle01d = lTentacle01c.addChild("lTentacle01d",
                ModelPartBuilder.create()
                        .uv(52, 10).cuboid(-0.99F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F),
                ModelTransform.of(0.0F, 0.0F, 1.6F, -0.43633232F, 0.0F, 0.0F));
        ModelPartData lTentacle01e = lTentacle01d.addChild("lTentacle01e",
                ModelPartBuilder.create()
                        .uv(40, 17).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 3.0F),
                ModelTransform.of(0.0F, -0.1F, 3.5F, 0.6981317F, 0.0F, 0.0F));
        lTentacle01e.addChild("lTentacle01f",
                ModelPartBuilder.create()
                        .uv(51, 17).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 3.0F),
                ModelTransform.of(0.0F, 0.0F, 2.8F, 0.40142572F, 0.0F, 0.0F));
        ModelPartData lTentacle02a = body.addChild("lTentacle02a",
                ModelPartBuilder.create()
                        .uv(41, 12).cuboid(-1.0F, -1.0F, 0.4F, 2.0F, 2.0F, 2.0F),
                ModelTransform.of(-1.3F, -5.0F, 0.6F, -0.2268928F, -0.9599311F, 0.0F));
        ModelPartData lTentacle02b = lTentacle02a.addChild("lTentacle02b",
                ModelPartBuilder.create()
                        .uv(41, 12).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F),
                ModelTransform.of(0.0F, 0.0F, 1.6F, -0.6981317F, -0.31415927F, 0.31415927F));
        ModelPartData lTentacle02c = lTentacle02b.addChild("lTentacle02c",
                ModelPartBuilder.create()
                        .uv(52, 10).cuboid(-1.01F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F),
                ModelTransform.of(0.0F, 0.0F, 1.6F, -0.43633232F, 0.0F, 0.0F));
        ModelPartData lTentacle02d = lTentacle02c.addChild("lTentacle02d",
                ModelPartBuilder.create()
                        .uv(40, 17).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 3.0F),
                ModelTransform.of(0.0F, 0.0F, 3.3F, 0.6981317F, 0.0F, 0.0F));
        lTentacle02d.addChild("lTentacle02e",
                ModelPartBuilder.create()
                        .uv(51, 17).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 3.0F),
                ModelTransform.of(0.0F, 0.0F, 2.8F, 0.5235988F, 0.0F, 0.0F));
        ModelPartData chest = body.addChild("chest",
                ModelPartBuilder.create()
                        .uv(0, 17).cuboid(-3.0F, -4.0F, -1.5F, 6.0F, 5.0F, 3.0F),
                ModelTransform.of(0.0F, -4.3F, 0.0F, -0.17453292F, 0.0F, 0.0F));
        ModelPartData lTentacle03a = chest.addChild("lTentacle03a",
                ModelPartBuilder.create()
                        .uv(41, 12).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F),
                ModelTransform.of(-1.3F, -2.7F, 1.6F, -0.61086524F, -0.61086524F, 0.5235988F));
        ModelPartData lTentacle03b = lTentacle03a.addChild("lTentacle03b",
                ModelPartBuilder.create()
                        .uv(52, 10).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F),
                ModelTransform.of(0.0F, 0.0F, 1.1F, -0.43633232F, -0.5235988F, 0.0F));
        ModelPartData lTentacle03c = lTentacle03b.addChild("lTentacle03c",
                ModelPartBuilder.create()
                        .uv(51, 17).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 3.0F),
                ModelTransform.of(0.0F, 0.0F, 3.3F, 0.34906584F, 0.0F, 0.0F));
        lTentacle03c.addChild("lTentacle03d",
                ModelPartBuilder.create()
                        .uv(51, 17).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 3.0F),
                ModelTransform.of(0.0F, 0.0F, 2.8F, 0.5235988F, 0.0F, 0.0F));
        ModelPartData rTentacle03a = chest.addChild("rTentacle03a",
                ModelPartBuilder.create()
                        .uv(41, 12).mirrored(true).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F),
                ModelTransform.of(1.3F, -2.7F, 1.6F, -0.61086524F, 0.61086524F, -0.5235988F));
        ModelPartData rTentacle03b = rTentacle03a.addChild("rTentacle03b",
                ModelPartBuilder.create()
                        .uv(52, 10).mirrored(true).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F),
                ModelTransform.of(0.0F, 0.0F, 1.1F, -0.43633232F, 0.5235988F, 0.0F));
        ModelPartData rTentacle03c = rTentacle03b.addChild("rTentacle03c",
                ModelPartBuilder.create()
                        .uv(51, 17).mirrored(true).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 3.0F),
                ModelTransform.of(0.0F, 0.0F, 3.3F, 0.34906584F, 0.0F, 0.0F));
        rTentacle03c.addChild("rTentacle03d",
                ModelPartBuilder.create()
                        .uv(51, 17).mirrored(true).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 3.0F),
                ModelTransform.of(0.0F, 0.0F, 2.8F, 0.5235988F, 0.0F, 0.0F));
        chest.addChild("boobs",
                ModelPartBuilder.create()
                        .uv(0, 28).cuboid(-2.5F, 0.0F, -1.0F, 5.0F, 2.0F, 2.0F),
                ModelTransform.of(0.0F, -2.9F, -0.7F, -1.0471976F, 0.0F, 0.0F));
        ModelPartData lArm01 = chest.addChild("lArm01",
                ModelPartBuilder.create()
                        .uv(33, 0).cuboid(0.2F, -0.5F, -1.0F, 2.0F, 4.0F, 2.0F),
                ModelTransform.of(2.1F, -3.5F, 0.4F, -0.43633232F, 0.0F, 0.17453292F));
        ModelPartData lArm02 = lArm01.addChild("lArm02",
                ModelPartBuilder.create()
                        .uv(32, 6).cuboid(-1.2F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F),
                ModelTransform.of(1.3F, 3.3F, 0.0F, -0.2268928F, 0.0F, 0.0F));
        lArm02.addChild("lHoof02",
                ModelPartBuilder.create()
                        .uv(32, 13).cuboid(-0.5F, -0.5F, -0.8F, 1.0F, 2.0F, 2.0F),
                ModelTransform.of(-0.7F, 4.1F, -0.6F, 0.34906584F, 0.034906585F, -0.13962634F));
        lArm02.addChild("lHoof01",
                ModelPartBuilder.create()
                        .uv(32, 13).cuboid(-0.5F, -0.5F, -0.8F, 1.0F, 2.0F, 2.0F),
                ModelTransform.of(0.4F, 3.95F, -0.6F, 0.34906584F, -0.034906585F, -0.13962634F));
        ModelPartData head = chest.addChild("head",
                ModelPartBuilder.create()
                        .uv(25, 24).cuboid(-2.5F, -3.6F, -2.0F, 5.0F, 4.0F, 4.0F),
                ModelTransform.of(0.0F, -4.4F, 0.0F, 0.0F, 0.0F, 0.0F));
        head.addChild("snout",
                ModelPartBuilder.create()
                        .uv(45, 25).cuboid(-1.5F, -1.8F, -2.2F, 3.0F, 3.0F, 3.0F),
                ModelTransform.of(0.0F, -1.0F, -1.9F, 0.17453292F, 0.0F, 0.0F));
        ModelPartData rHorn00 = head.addChild("rHorn00",
                ModelPartBuilder.create()
                        .uv(43, 0).mirrored(true).cuboid(-1.0F, -2.0F, -0.4F, 2.0F, 2.0F, 1.0F),
                ModelTransform.of(-1.5F, -2.8F, 1.0F, -0.31415927F, 0.2617994F, -0.80285144F));
        ModelPartData rHorn01a = rHorn00.addChild("rHorn01a",
                ModelPartBuilder.create()
                        .uv(50, 0).mirrored(true).cuboid(-0.2F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.2F, -1.5F, 0.11F, -0.10471976F, 0.0F, -0.43633232F));
        rHorn01a.addChild("rHorn01b",
                ModelPartBuilder.create()
                        .uv(50, 0).mirrored(true).cuboid(-0.8F, -2.0F, -0.49F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        ModelPartData rHorn02a = rHorn01a.addChild("rHorn02a",
                ModelPartBuilder.create()
                        .uv(55, 0).mirrored(true).cuboid(-0.4F, -1.7F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, -1.9F, 0.11F, -0.10471976F, 0.0F, -0.43633232F));
        rHorn02a.addChild("rHorn02b",
                ModelPartBuilder.create()
                        .uv(55, 0).mirrored(true).cuboid(-0.7F, -1.7F, -0.49F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        rHorn02a.addChild("rHorn03",
                ModelPartBuilder.create()
                        .uv(55, 0).mirrored(true).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, -1.6F, 0.0F, 0.0F, 0.0F, 0.2617994F));
        head.addChild("snoutSnope",
                ModelPartBuilder.create()
                        .uv(45, 25).cuboid(-1.51F, -0.6F, -2.5F, 3.0F, 1.0F, 3.0F),
                ModelTransform.of(0.0F, -2.9F, -1.9F, 0.34906584F, 0.0F, 0.0F));
        ModelPartData lHorn00 = head.addChild("lHorn00",
                ModelPartBuilder.create()
                        .uv(43, 0).cuboid(-1.0F, -2.0F, -0.4F, 2.0F, 2.0F, 1.0F),
                ModelTransform.of(1.5F, -2.8F, 1.0F, -0.31415927F, -0.2617994F, 0.80285144F));
        ModelPartData lHorn01a = lHorn00.addChild("lHorn01a",
                ModelPartBuilder.create()
                        .uv(50, 0).cuboid(-0.8F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(-0.1F, -1.5F, 0.11F, -0.10471976F, 0.0F, 0.43633232F));
        ModelPartData lHorn02a = lHorn01a.addChild("lHorn02a",
                ModelPartBuilder.create()
                        .uv(55, 0).cuboid(-0.7F, -1.7F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.1F, -1.9F, 0.11F, -0.10471976F, 0.0F, 0.43633232F));
        lHorn02a.addChild("lHorn03",
                ModelPartBuilder.create()
                        .uv(55, 0).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, -1.6F, 0.0F, 0.0F, 0.0F, -0.2617994F));
        lHorn02a.addChild("lHorn02b",
                ModelPartBuilder.create()
                        .uv(55, 0).cuboid(-0.4F, -1.7F, -0.49F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        lHorn01a.addChild("lHorn01b",
                ModelPartBuilder.create()
                        .uv(50, 0).cuboid(-0.3F, -2.0F, -0.49F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        head.addChild("rEar",
                ModelPartBuilder.create()
                        .uv(56, 24).mirrored(true).cuboid(-2.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F),
                ModelTransform.of(-2.0F, -2.1F, 0.7F, 0.5235988F, 0.0F, -0.31415927F));
        head.addChild("lEar",
                ModelPartBuilder.create()
                        .uv(56, 24).cuboid(0.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F),
                ModelTransform.of(2.0F, -2.1F, 0.7F, 0.5235988F, 0.0F, 0.31415927F));
        ModelPartData rArm01 = chest.addChild("rArm01",
                ModelPartBuilder.create()
                        .uv(33, 0).mirrored(true).cuboid(-1.8F, -0.5F, -1.0F, 2.0F, 4.0F, 2.0F),
                ModelTransform.of(-2.5F, -3.5F, 0.4F, -0.43633232F, 0.0F, -0.17453292F));
        ModelPartData rArm02 = rArm01.addChild("rArm02",
                ModelPartBuilder.create()
                        .uv(32, 6).mirrored(true).cuboid(-0.7F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F),
                ModelTransform.of(-1.0F, 3.3F, 0.0F, -0.2268928F, 0.0F, 0.0F));
        rArm02.addChild("rHoof01",
                ModelPartBuilder.create()
                        .uv(32, 13).mirrored(true).cuboid(-0.5F, -0.5F, -0.8F, 1.0F, 2.0F, 2.0F),
                ModelTransform.of(-0.3F, 3.95F, -0.6F, 0.34906584F, 0.034906585F, 0.13962634F));
        rArm02.addChild("rHoof02",
                ModelPartBuilder.create()
                        .uv(32, 13).mirrored(true).cuboid(-0.5F, -0.5F, -0.8F, 1.0F, 2.0F, 2.0F),
                ModelTransform.of(0.8F, 4.1F, -0.6F, 0.34906584F, -0.034906585F, 0.13962634F));
        ModelPartData rTentacle00a = body.addChild("rTentacle00a",
                ModelPartBuilder.create()
                        .uv(41, 4).mirrored(true).cuboid(-1.5F, -1.5F, -1.0F, 3.0F, 3.0F, 4.0F),
                ModelTransform.of(1.5F, -1.5F, -1.1F, -0.4537856F, 0.2617994F, -0.08726646F));
        ModelPartData rTentacle00b = rTentacle00a.addChild("rTentacle00b",
                ModelPartBuilder.create()
                        .uv(41, 12).mirrored(true).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F),
                ModelTransform.of(0.0F, -0.4F, 2.5F, -0.40142572F, 0.2617994F, 0.0F));
        ModelPartData rTentacle00c = rTentacle00b.addChild("rTentacle00c",
                ModelPartBuilder.create()
                        .uv(41, 12).mirrored(true).cuboid(-0.99F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F),
                ModelTransform.of(0.0F, 0.0F, 1.6F, -0.43633232F, 0.0F, 0.0F));
        ModelPartData rTentacle00d = rTentacle00c.addChild("rTentacle00d",
                ModelPartBuilder.create()
                        .uv(52, 10).mirrored(true).cuboid(-1.01F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F),
                ModelTransform.of(0.0F, 0.0F, 1.6F, -0.57595867F, 0.0F, 0.0F));
        ModelPartData rTentacle00e = rTentacle00d.addChild("rTentacle00e",
                ModelPartBuilder.create()
                        .uv(40, 17).mirrored(true).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 3.0F),
                ModelTransform.of(0.0F, -0.1F, 3.5F, 0.5235988F, 0.0F, 0.0F));
        rTentacle00e.addChild("rTentacle00f",
                ModelPartBuilder.create()
                        .uv(51, 17).mirrored(true).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 3.0F),
                ModelTransform.of(0.0F, 0.0F, 2.8F, 0.5235988F, 0.0F, 0.0F));
        ModelPartData lTentacle00a = body.addChild("lTentacle00a",
                ModelPartBuilder.create()
                        .uv(41, 4).cuboid(-1.5F, -1.5F, -1.0F, 3.0F, 3.0F, 4.0F),
                ModelTransform.of(-1.5F, -1.5F, -1.1F, -0.4537856F, -0.2617994F, 0.08726646F));
        ModelPartData lTentacle00b = lTentacle00a.addChild("lTentacle00b",
                ModelPartBuilder.create()
                        .uv(41, 12).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F),
                ModelTransform.of(0.0F, -0.4F, 2.5F, -0.40142572F, -0.2617994F, 0.0F));
        ModelPartData lTentacle00c = lTentacle00b.addChild("lTentacle00c",
                ModelPartBuilder.create()
                        .uv(41, 12).cuboid(-1.01F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F),
                ModelTransform.of(0.0F, 0.0F, 1.6F, -0.43633232F, 0.0F, 0.0F));
        ModelPartData lTentacle00d = lTentacle00c.addChild("lTentacle00d",
                ModelPartBuilder.create()
                        .uv(52, 10).cuboid(-0.909F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F),
                ModelTransform.of(0.0F, 0.0F, 1.6F, -0.57595867F, 0.0F, 0.0F));
        ModelPartData lTentacle00e = lTentacle00d.addChild("lTentacle00e",
                ModelPartBuilder.create()
                        .uv(40, 17).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 3.0F),
                ModelTransform.of(0.0F, -0.1F, 3.5F, 0.5235988F, 0.0F, 0.0F));
        lTentacle00e.addChild("lTentacle00f",
                ModelPartBuilder.create()
                        .uv(51, 17).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 3.0F),
                ModelTransform.of(0.0F, 0.0F, 2.8F, 0.5235988F, 0.0F, 0.0F));
        ModelPartData rTentacle02a = body.addChild("rTentacle02a",
                ModelPartBuilder.create()
                        .uv(41, 12).mirrored(true).cuboid(-1.0F, -1.0F, 0.4F, 2.0F, 2.0F, 2.0F),
                ModelTransform.of(1.3F, -5.0F, 0.6F, -0.2268928F, 0.9599311F, 0.0F));
        ModelPartData rTentacle02b = rTentacle02a.addChild("rTentacle02b",
                ModelPartBuilder.create()
                        .uv(41, 12).mirrored(true).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F),
                ModelTransform.of(0.0F, 0.0F, 1.6F, -0.6981317F, 0.31415927F, -0.31415927F));
        ModelPartData rTentacle02c = rTentacle02b.addChild("rTentacle02c",
                ModelPartBuilder.create()
                        .uv(52, 10).mirrored(true).cuboid(-1.01F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F),
                ModelTransform.of(0.0F, 0.0F, 1.6F, -0.43633232F, 0.0F, 0.0F));
        ModelPartData rTentacle02d = rTentacle02c.addChild("rTentacle02d",
                ModelPartBuilder.create()
                        .uv(40, 17).mirrored(true).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 3.0F),
                ModelTransform.of(0.0F, 0.0F, 3.3F, 0.6981317F, 0.0F, 0.0F));
        rTentacle02d.addChild("rTentacle02e",
                ModelPartBuilder.create()
                        .uv(51, 17).mirrored(true).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 3.0F),
                ModelTransform.of(0.0F, 0.0F, 2.8F, 0.5235988F, 0.0F, 0.0F));
        root.addChild("plinth",
                ModelPartBuilder.create()
                        .cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
                ModelTransform.of(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(data, 64, 32);
    }
}
