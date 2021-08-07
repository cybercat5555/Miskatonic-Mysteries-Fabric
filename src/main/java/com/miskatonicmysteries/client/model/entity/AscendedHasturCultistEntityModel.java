// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

package com.miskatonicmysteries.client.model.entity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelPartNames;

public class AscendedHasturCultistEntityModel extends HasturCultistEntityModel {
    public AscendedHasturCultistEntityModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();
        ModelPartData rightArm = root.addChild(EntityModelPartNames.RIGHT_ARM,
                ModelPartBuilder.create()
                        .uv(40, 46).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F)
                        .uv(46, 23).cuboid(-3.0F, 6.0F, -2.0F, 4.0F, 2.0F, 8.0F, new Dilation(0.15F, 0.15F, 0.15F)),
                ModelTransform.of(-5.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        root.addChild(EntityModelPartNames.LEFT_LEG,
                ModelPartBuilder.create()
                        .uv(0, 22).mirrored(true).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                ModelTransform.of(2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        root.addChild(EntityModelPartNames.BODY,
                ModelPartBuilder.create()
                        .uv(16, 20).cuboid(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        ModelPartData head = root.addChild(EntityModelPartNames.HEAD,
                ModelPartBuilder.create()
                        .cuboid(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        head.addChild("hoodFringeL01",
                ModelPartBuilder.create()
                        .uv(64, 40).cuboid(0.2F, -0.8F, -5.09F, 1.0F, 10.0F, 9.0F),
                ModelTransform.of(2.3F, -9.2F, 0.1F, 0.0F, 0.0F, -0.2268928F));
        head.addChild("hoodFringeR03",
                ModelPartBuilder.create()
                        .uv(88, 39).mirrored(true).cuboid(-3.2F, -0.6F, -5.08F, 4.0F, 1.0F, 9.0F),
                ModelTransform.of(-0.6F, -9.9F, 0.1F, 0.0F, 0.0F, -0.2617994F));
        head.addChild("hoodFringeR01",
                ModelPartBuilder.create()
                        .uv(64, 40).mirrored(true).cuboid(-1.9F, -0.6F, -5.09F, 1.0F, 10.0F, 9.0F),
                ModelTransform.of(-1.7F, -9.2F, 0.1F, 0.0F, 0.0F, 0.2268928F));
        head.addChild("hoodFringeL02",
                ModelPartBuilder.create()
                        .uv(88, 39).cuboid(-3.6F, -0.6F, -5.08F, 4.0F, 1.0F, 9.0F),
                ModelTransform.of(3.3F, -9.2F, 0.1F, 0.0F, 0.0F, 0.2617994F));
        head.addChild("hoodRSide02",
                ModelPartBuilder.create()
                        .uv(79, 51).mirrored(true).cuboid(-0.6F, -0.9F, -5.25F, 6.0F, 2.0F, 9.0F),
                ModelTransform.of(-5.15F, 0.1F, 0.3F, 0.0F, 0.0F, 0.31415927F));
        head.addChild("leftMaskPlate",
                ModelPartBuilder.create()
                        .uv(68, 0).cuboid(-0.1F, -2.5F, -0.5F, 4.0F, 7.0F, 1.0F),
                ModelTransform.of(0.0F, -4.4F, -4.9F, -0.12217305F, -0.13962634F, 0.0F));
        ModelPartData hoodPipe01 = head.addChild("hoodPipe01",
                ModelPartBuilder.create()
                        .uv(75, 23).cuboid(-4.5F, -2.5F, 0.0F, 9.0F, 10.7F, 2.0F),
                ModelTransform.of(0.0F, -7.3F, 2.7F, 0.0F, 0.0F, 0.0F));
        hoodPipe01.addChild("hoodPipe02",
                ModelPartBuilder.create()
                        .uv(109, 33).cuboid(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 4.0F),
                ModelTransform.of(0.0F, -0.3F, 1.0F, -0.4537856F, 0.0F, 0.0F));
        head.addChild("nose",
                ModelPartBuilder.create()
                        .uv(24, 0).cuboid(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F),
                ModelTransform.of(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        head.addChild("rightMaskPlate",
                ModelPartBuilder.create()
                        .uv(68, 0).mirrored(true).cuboid(-3.9F, -2.5F, -0.5F, 4.0F, 7.0F, 1.0F),
                ModelTransform.of(0.0F, -4.4F, -4.9F, -0.12217305F, 0.13962634F, 0.0F));
        head.addChild("hoodLSide02",
                ModelPartBuilder.create()
                        .uv(79, 51).cuboid(-5.5F, -0.9F, -5.25F, 6.0F, 2.0F, 9.0F),
                ModelTransform.of(5.15F, 0.1F, 0.3F, 0.0F, 0.0F, -0.31415927F));
        root.addChild(EntityModelPartNames.RIGHT_LEG,
                ModelPartBuilder.create()
                        .uv(0, 22).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                ModelTransform.of(-2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        root.addChild("robe",
                ModelPartBuilder.create()
                        .uv(0, 38).cuboid(-4.0F, 0.0F, -3.0F, 8.0F, 20.0F, 6.0F, new Dilation(0.5F, 0.5F, 0.5F)),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        ModelPartData leftArm = root.addChild(EntityModelPartNames.LEFT_ARM,
                ModelPartBuilder.create()
                        .uv(40, 46).mirrored(true).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F)
                        .uv(46, 23).cuboid(-1.0F, 6.0F, -2.0F, 4.0F, 2.0F, 8.0F, new Dilation(0.15F, 0.15F, 0.15F)),
                ModelTransform.of(5.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        ModelPartData rightArmFolded = root.addChild("rightArmFolded",
                ModelPartBuilder.create()
                        .uv(44, 22).cuboid(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F),
                ModelTransform.of(0.0F, 3.0F, -1.0F, -0.749968F, 0.0F, 0.0F));
        rightArmFolded.addChild("leftArmFolded",
                ModelPartBuilder.create()
                        .uv(44, 22).mirrored(true).cuboid(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        root.addChild("middleArmFolded",
                ModelPartBuilder.create()
                        .uv(40, 38).cuboid(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F),
                ModelTransform.of(0.0F, 3.0F, -1.0F, -0.749968F, 0.0F, 0.0F));
        root.addChild(EntityModelPartNames.HAT, ModelPartBuilder.create(), ModelTransform.NONE);

        rightArm.addChild("rArmSleeveSlope_r1",
                ModelPartBuilder.create()
                        .uv(49, 37).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 2.0F, 4.0F, new Dilation(0.1F, 0.1F, 0.1F)),
                ModelTransform.of(-2.0F, 6.75F, 2.75F, -0.5236F, 0.0F, 0.0F));
        head.addChild("clothMask_r1",
                ModelPartBuilder.create()
                        .uv(48, 1).cuboid(-4.5F, 2.0F, 0.0F, 9.0F, 14.0F, 1.0F),
                ModelTransform.of(0.0F, -11.0F, -4.25F, -0.0873F, 0.0F, 0.0F));
        ModelPartData crown = head.addChild("crown",
                ModelPartBuilder.create()
                        .uv(73, 2).cuboid(-4.5F, -9.25F, -5.0F, 9.0F, 1.0F, 10.0F, new Dilation(0.1F, 0.1F, 0.1F)),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        ModelPartData lAntler = crown.addChild("lAntler",
                ModelPartBuilder.create(),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        lAntler.addChild("lAntler09_r1",
                ModelPartBuilder.create()
                        .uv(79, 13).cuboid(-1.0F, -3.0F, -0.25F, 1.0F, 3.0F, 1.0F, new Dilation(-0.1F, -0.1F, -0.1F)),
                ModelTransform.of(4.75F, -15.5F, -4.75F, 0.0F, 0.0F, -0.6981F));
        lAntler.addChild("lAntler08_r1",
                ModelPartBuilder.create()
                        .uv(73, 16).cuboid(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, new Dilation(-0.1F, -0.1F, -0.1F)),
                ModelTransform.of(3.0F, -11.25F, -4.75F, 0.0F, 0.0F, -0.3927F));
        lAntler.addChild("lAntler07_r1",
                ModelPartBuilder.create()
                        .uv(73, 15).cuboid(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(2.75F, -9.25F, -5.0F, 0.0F, 0.0F, 0.1309F));
        lAntler.addChild("lAntler06_r1",
                ModelPartBuilder.create()
                        .uv(77, 15).cuboid(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, new Dilation(-0.1F, -0.1F, -0.1F)),
                ModelTransform.of(6.75F, -11.75F, -5.0F, 0.0F, 0.0F, 0.829F));
        lAntler.addChild("lAntler05_r1",
                ModelPartBuilder.create()
                        .uv(78, 15).cuboid(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(5.0F, -11.0F, -5.0F, 0.0F, 0.0F, 1.1781F));
        lAntler.addChild("lAntler04_r1",
                ModelPartBuilder.create()
                        .uv(76, 14).cuboid(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(5.25F, -14.0F, -5.0F, 0.0F, 0.0F, -0.3491F));
        lAntler.addChild("lAntler03_r1",
                ModelPartBuilder.create()
                        .uv(73, 15).cuboid(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.1F, 0.1F, 0.1F)),
                ModelTransform.of(5.25F, -12.0F, -5.0F, 0.0F, 0.0F, -0.0436F));
        lAntler.addChild("lAntler02_r1",
                ModelPartBuilder.create()
                        .uv(78, 15).cuboid(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.15F, 0.15F, 0.15F)),
                ModelTransform.of(4.25F, -10.0F, -5.0F, 0.0F, 0.0F, 0.4363F));
        lAntler.addChild("lAntler01_r1",
                ModelPartBuilder.create()
                        .uv(73, 15).cuboid(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.2F, 0.2F, 0.2F)),
                ModelTransform.of(2.75F, -8.5F, -5.0F, 0.0F, 0.0F, 0.829F));
        ModelPartData rAntler = crown.addChild("rAntler",
                ModelPartBuilder.create(),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        rAntler.addChild("rAntler09_r1",
                ModelPartBuilder.create()
                        .uv(79, 13).cuboid(0.0F, -3.0F, -0.25F, 1.0F, 3.0F, 1.0F, new Dilation(-0.1F, -0.1F, -0.1F)),
                ModelTransform.of(-4.75F, -15.5F, -4.75F, 0.0F, 0.0F, 0.6981F));
        rAntler.addChild("rAntler08_r1",
                ModelPartBuilder.create()
                        .uv(73, 16).cuboid(0.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, new Dilation(-0.1F, -0.1F, -0.1F)),
                ModelTransform.of(-3.0F, -11.25F, -4.75F, 0.0F, 0.0F, 0.3927F));
        rAntler.addChild("rAntler07_r1",
                ModelPartBuilder.create()
                        .uv(73, 15).cuboid(0.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(-2.75F, -9.25F, -5.0F, 0.0F, 0.0F, -0.1309F));
        rAntler.addChild("rAntler06_r1",
                ModelPartBuilder.create()
                        .uv(77, 15).cuboid(0.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, new Dilation(-0.1F, -0.1F, -0.1F)),
                ModelTransform.of(-6.75F, -11.75F, -5.0F, 0.0F, 0.0F, -0.829F));
        rAntler.addChild("rAntler05_r1",
                ModelPartBuilder.create()
                        .uv(78, 15).cuboid(0.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(-5.0F, -11.0F, -5.0F, 0.0F, 0.0F, -1.1781F));
        rAntler.addChild("rAntler04_r1",
                ModelPartBuilder.create()
                        .uv(76, 14).cuboid(0.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(-5.25F, -14.0F, -5.0F, 0.0F, 0.0F, 0.3491F));
        rAntler.addChild("rAntler03_r1",
                ModelPartBuilder.create()
                        .uv(73, 15).cuboid(0.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.1F, 0.1F, 0.1F)),
                ModelTransform.of(-5.25F, -12.0F, -5.0F, 0.0F, 0.0F, 0.0436F));
        rAntler.addChild("rAntler02_r1",
                ModelPartBuilder.create()
                        .uv(78, 15).cuboid(0.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.15F, 0.15F, 0.15F)),
                ModelTransform.of(-4.25F, -10.0F, -5.0F, 0.0F, 0.0F, -0.4363F));
        rAntler.addChild("rAntler01_r1",
                ModelPartBuilder.create()
                        .uv(73, 15).cuboid(0.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.2F, 0.2F, 0.2F)),
                ModelTransform.of(-2.75F, -8.5F, -5.0F, 0.0F, 0.0F, -0.829F));
        leftArm.addChild("lArmSleeveSlope_r1",
                ModelPartBuilder.create()
                        .uv(49, 37).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 2.0F, 4.0F, new Dilation(0.1F, 0.1F, 0.1F)),
                ModelTransform.of(2.0F, 6.75F, 2.75F, -0.5236F, 0.0F, 0.0F));
        return TexturedModelData.of(data, 128, 64);
    }
}