// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

package com.miskatonicmysteries.client.model.entity;

import com.miskatonicmysteries.common.entity.ByakheeEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class ByakheeEntityModel extends EntityModel<ByakheeEntity> {
    private final ModelPart root, head;
    private final ModelPart rLead_r1, lLead_r1;
    private final ModelPart lLeg01, lLeg02, lFoot;
    private final ModelPart rLeg01, rLeg02, rFoot;
    private final ModelPart lArm01, lArm02, lHand;
    private final ModelPart rArm01, rArm02, rHand;
    private final ModelPart lWingTine01a, lWingTine02a, lWingTine03a, lWingTine04a, lWingTine05a;
    private final ModelPart lWingMembrane02, lWingMembrane03;
    private final ModelPart rWingTine01a, rWingTine02a, rWingTine03a, rWingTine04a, rWingTine05a;
    private final ModelPart rWingMembrane02, rWingMembrane03;
    private final ModelPart neck03, tail_r1;
    public ByakheeEntityModel(ModelPart root) {
    	super(RenderLayer::getEntityCutout);
    	this.root = root;
    	ModelPart chest = root.getChild("chest");
    	this.neck03 = chest.getChild("neck01").getChild("neck02").getChild("neck03");
    	this.head = neck03.getChild("head");
    	this.rLead_r1 = head.getChild("rLead_r1");
    	this.lLead_r1 = head.getChild("lLead_r1");
    	ModelPart lowerBody = chest.getChild("lowerBody");
    	this.lLeg01 = lowerBody.getChild("lLeg01");
    	this.lLeg02 = lLeg01.getChild("lLeg02");
    	this.lFoot = lLeg02.getChild("lLeg03").getChild("lFoot");
    	this.rLeg01 = lowerBody.getChild("rLeg01");
        this.rLeg02 = rLeg01.getChild("rLeg02");
        this.rFoot = rLeg02.getChild("rLeg03").getChild("rFoot");
        this.rArm01 = chest.getChild("rArm01");
        this.rArm02 = rArm01.getChild("rArm02");
        this.rHand = rArm02.getChild("rHand");
        this.lArm01 = chest.getChild("lArm01");
        this.lArm02 = lArm01.getChild("lArm02");
        this.lHand = lArm02.getChild("lHand");
        this.lWingTine01a = lArm02.getChild("lWingTine01a");
        this.lWingTine02a = lArm02.getChild("lWingTine02a");
        this.lWingTine03a = lArm02.getChild("lWingTine03a");
        this.lWingTine04a = lArm02.getChild("lWingTine04a");
        this.lWingTine05a = lArm02.getChild("lWingTine05a");
        this.rWingTine01a = rArm02.getChild("rWingTine01a");
        this.rWingTine02a = rArm02.getChild("rWingTine02a");
        this.rWingTine03a = rArm02.getChild("rWingTine03a");
        this.rWingTine04a = rArm02.getChild("rWingTine04a");
        this.rWingTine05a = rArm02.getChild("rWingTine05a");
        this.lWingMembrane02= lWingTine05a.getChild("lWingMembrane02");
        this.lWingMembrane03 = lArm02.getChild("lWingMembrane03");
        this.rWingMembrane02 = rWingTine05a.getChild("rWingMembrane02");
        this.rWingMembrane03 = rArm02.getChild("rWingMembrane03");
        this.tail_r1 = lowerBody.getChild("tail_r1");

    }

    public static TexturedModelData getTexturedModelData(){
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();
        ModelPartData chest = root.addChild("chest",
                ModelPartBuilder.create()
                        .cuboid(-7.0F, -7.5F, -8.5F, 14.0F, 13.0F, 18.0F),
                ModelTransform.of(0.25F, 1.25F, 4.0F, -0.2182F, 0.0F, 0.0F));
        ModelPartData lowerBody = chest.addChild("lowerBody",
                ModelPartBuilder.create()
                        .uv(0, 32).cuboid(-6.0F, -5.5F, 0.5F, 12.0F, 6.0F, 18.0F),
                ModelTransform.of(0.0F, -1.2F, 8.0F, 0.0873F, 0.0F, 0.0F));
        lowerBody.addChild("tail_r1",
                ModelPartBuilder.create()
                        .uv(121, 119).cuboid(-5.0F, -0.8F, 0.0F, 11.0F, 8.0F, 0.0F),
                ModelTransform.of(-0.25F, -5.05F, 19.0F, 0.9163F, 0.0F, 0.0F));
        lowerBody.addChild("stomach",
                ModelPartBuilder.create()
                        .uv(0, 57).cuboid(-4.5F, -2.5F, 0.5F, 9.0F, 5.0F, 18.0F),
                ModelTransform.of(0.0F, 3.2F, -1.0F, 0.2618F, 0.0F, 0.0F));
        ModelPartData lLeg01 = lowerBody.addChild("lLeg01",
                ModelPartBuilder.create()
                        .uv(66, 0).mirrored(true).cuboid(-2.3F, -1.1F, -3.0F, 5.0F, 11.0F, 6.0F),
                ModelTransform.of(3.9F, -1.4F, 15.1F, -0.5236F, -0.576F, -0.1222F));
        ModelPartData lLeg02 = lLeg01.addChild("lLeg02",
                ModelPartBuilder.create()
                        .uv(64, 19).mirrored(true).cuboid(-2.0F, -0.4F, -2.5F, 4.0F, 4.0F, 5.0F),
                ModelTransform.of(0.3F, 10.0F, -0.1F, 0.0F, 0.0F, 0.0F));
        ModelPartData lLeg03 = lLeg02.addChild("lLeg03",
                ModelPartBuilder.create()
                        .uv(51, 1).mirrored(true).cuboid(-1.5F, -2.2F, -1.5F, 3.0F, 11.0F, 3.0F),
                ModelTransform.of(0.0F, 4.1F, 0.8F, 0.7156F, 0.0F, 0.0873F));
        ModelPartData lFoot = lLeg03.addChild("lFoot",
                ModelPartBuilder.create()
                        .uv(83, 21).mirrored(true).cuboid(-2.0F, 0.0F, -2.8F, 4.0F, 2.0F, 5.0F),
                ModelTransform.of(0.0F, 8.5F, -0.5F, -0.0175F, 0.0F, -0.0524F));
        lFoot.addChild("lFootClaw01",
                ModelPartBuilder.create()
                        .uv(1, 7).mirrored(true).cuboid(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 6.0F),
                ModelTransform.of(-1.3F, 0.5F, -2.6F, 0.1396F, 0.2094F, 0.0F));
        lFoot.addChild("lFootClaw02",
                ModelPartBuilder.create()
                        .uv(1, 7).mirrored(true).cuboid(-0.5F, -0.5F, -4.5F, 1.0F, 2.0F, 6.0F),
                ModelTransform.of(0.0F, 0.5F, -2.6F, 0.1396F, 0.0F, 0.0F));
        lFoot.addChild("lFootClaw03",
                ModelPartBuilder.create()
                        .uv(1, 7).mirrored(true).cuboid(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 6.0F),
                ModelTransform.of(1.3F, 0.5F, -2.6F, 0.1222F, -0.2618F, 0.0F));
        lFoot.addChild("lWebbing",
                ModelPartBuilder.create()
                        .uv(110, 110).mirrored(true).cuboid(-2.0F, 0.0F, -4.0F, 4.0F, 0.0F, 5.0F),
                ModelTransform.of(0.0F, 1.0F, -2.0F, 0.0F, 0.0F, 0.0F));
        ModelPartData rLeg01 = lowerBody.addChild("rLeg01",
                ModelPartBuilder.create()
                        .uv(66, 0).cuboid(-2.7F, -1.1F, -3.0F, 5.0F, 11.0F, 6.0F),
                ModelTransform.of(-3.9F, -1.4F, 15.1F, -0.5236F, 0.576F, 0.1222F));
        ModelPartData rLeg02 = rLeg01.addChild("rLeg02",
                ModelPartBuilder.create()
                        .uv(64, 19).cuboid(-2.0F, -0.4F, -2.5F, 4.0F, 4.0F, 5.0F),
                ModelTransform.of(-0.3F, 10.0F, -0.1F, 0.0F, 0.0F, 0.0F));
        ModelPartData rLeg03 = rLeg02.addChild("rLeg03",
                ModelPartBuilder.create()
                        .uv(51, 1).cuboid(-1.5F, -2.2F, -1.5F, 3.0F, 11.0F, 3.0F),
                ModelTransform.of(0.0F, 4.1F, 0.8F, 0.7156F, 0.0F, -0.0873F));
        ModelPartData rFoot = rLeg03.addChild("rFoot",
                ModelPartBuilder.create()
                        .uv(83, 21).cuboid(-2.0F, 0.0F, -2.8F, 4.0F, 2.0F, 5.0F),
                ModelTransform.of(0.0F, 8.5F, -0.5F, -0.0175F, 0.0F, 0.0524F));
        rFoot.addChild("rFootClaw01",
                ModelPartBuilder.create()
                        .uv(1, 7).cuboid(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 6.0F),
                ModelTransform.of(1.3F, 0.5F, -2.6F, 0.1396F, -0.2094F, 0.0F));
        rFoot.addChild("rFootClaw02",
                ModelPartBuilder.create()
                        .uv(1, 7).cuboid(-0.5F, -0.5F, -4.5F, 1.0F, 2.0F, 6.0F),
                ModelTransform.of(0.0F, 0.5F, -2.6F, 0.1396F, 0.0F, 0.0F));
        rFoot.addChild("rFootClaw03",
                ModelPartBuilder.create()
                        .uv(1, 7).cuboid(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 6.0F),
                ModelTransform.of(-1.3F, 0.5F, -2.6F, 0.1222F, 0.2618F, 0.0F));
        rFoot.addChild("rWebbing",
                ModelPartBuilder.create()
                        .uv(110, 110).cuboid(-2.0F, 0.0F, -4.0F, 4.0F, 0.0F, 5.0F),
                ModelTransform.of(0.0F, 1.0F, -2.0F, 0.0F, 0.0F, 0.0F));
        ModelPartData lArm01 = chest.addChild("lArm01",
                ModelPartBuilder.create()
                        .uv(0, 82).mirrored(true).cuboid(0.0F, -1.5F, -2.0F, 18.0F, 4.0F, 4.0F),
                ModelTransform.of(6.0F, -4.0F, -3.0F, 0.0F, -0.2618F, 0.4363F));
        lArm01.addChild("lArmShoulder",
                ModelPartBuilder.create()
                        .uv(0, 99).mirrored(true).cuboid(-4.5F, -2.0F, -2.5F, 8.0F, 4.0F, 5.0F),
                ModelTransform.of(4.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.1222F));
        ModelPartData lArm02 = lArm01.addChild("lArm02",
                ModelPartBuilder.create()
                        .uv(0, 91).mirrored(true).cuboid(0.0F, -1.5F, -1.5F, 28.0F, 3.0F, 3.0F),
                ModelTransform.of(17.0F, 0.5F, 0.0F, 0.0F, 0.6545F, 0.9163F));
        ModelPartData lWingTine01a = lArm02.addChild("lWingTine01a",
                ModelPartBuilder.create()
                        .uv(34, 111).mirrored(true).cuboid(-1.0F, -1.4F, -1.0F, 3.0F, 11.0F, 2.0F),
                ModelTransform.of(26.6F, 0.2F, -0.1F, 1.5708F, -0.6545F, 0.0873F));
        ModelPartData lWingTine01b = lWingTine01a.addChild("lWingTine01b",
                ModelPartBuilder.create()
                        .uv(45, 111).mirrored(true).cuboid(-1.0F, 0.1F, -0.5F, 2.0F, 15.0F, 1.0F),
                ModelTransform.of(0.8F, 9.2F, 0.0F, 0.0F, 0.0F, 0.0873F));
        lWingTine01b.addChild("lWingTine01c",
                ModelPartBuilder.create()
                        .uv(54, 111).mirrored(true).cuboid(0.0F, 0.1F, -0.49F, 1.0F, 15.0F, 1.0F),
                ModelTransform.of(0.0F, 14.6F, 0.0F, 0.0F, 0.0F, 0.192F));
        lWingTine01a.addChild("lWingEdge01",
                ModelPartBuilder.create()
                        .uv(66, 82).mirrored(true).cuboid(-17.9F, -1.5F, -0.4F, 20.0F, 45.0F, 0.0F),
                ModelTransform.of(-0.8F, 0.0F, 0.5F, 0.0F, 0.0F, 0.1047F));
        ModelPartData lWingTine02a = lArm02.addChild("lWingTine02a",
                ModelPartBuilder.create()
                        .uv(34, 111).mirrored(true).cuboid(-1.0F, -1.4F, -1.0F, 3.0F, 11.0F, 2.0F),
                ModelTransform.of(26.6F, 0.0F, 0.9F, 1.5708F, -0.7854F, 0.1309F));
        ModelPartData lWingTine02b = lWingTine02a.addChild("lWingTine02b",
                ModelPartBuilder.create()
                        .uv(45, 111).mirrored(true).cuboid(-1.0F, 0.1F, -0.5F, 2.0F, 15.0F, 1.0F),
                ModelTransform.of(0.8F, 9.2F, 0.0F, 0.0F, 0.0F, 0.0873F));
        lWingTine02b.addChild("lWingTine02c",
                ModelPartBuilder.create()
                        .uv(54, 111).mirrored(true).cuboid(-0.4F, 0.1F, -0.49F, 1.0F, 11.0F, 1.0F),
                ModelTransform.of(0.0F, 14.6F, 0.0F, 0.0F, 0.0F, 0.192F));
        lWingTine02a.addChild("lWingEdge02",
                ModelPartBuilder.create()
                        .uv(66, 82).mirrored(true).cuboid(-17.9F, -1.5F, -0.5F, 20.0F, 45.0F, 0.0F),
                ModelTransform.of(-0.8F, 0.0F, 0.5F, 0.0F, 0.0F, 0.1047F));
        ModelPartData lWingTine03a = lArm02.addChild("lWingTine03a",
                ModelPartBuilder.create()
                        .uv(34, 111).mirrored(true).cuboid(-1.0F, -1.4F, -1.0F, 3.0F, 11.0F, 2.0F),
                ModelTransform.of(26.6F, -0.3F, 0.9F, 1.5708F, -0.9163F, 0.1745F));
        ModelPartData lWingTine03b = lWingTine03a.addChild("lWingTine03b",
                ModelPartBuilder.create()
                        .uv(45, 111).mirrored(true).cuboid(-1.0F, 0.1F, -0.5F, 2.0F, 15.0F, 1.0F),
                ModelTransform.of(0.8F, 9.2F, 0.0F, 0.0F, 0.0F, 0.0873F));
        lWingTine03b.addChild("lWingTine03c",
                ModelPartBuilder.create()
                        .uv(54, 111).mirrored(true).cuboid(-0.4F, 0.1F, -0.49F, 1.0F, 11.0F, 1.0F),
                ModelTransform.of(0.0F, 14.6F, 0.0F, 0.0F, 0.0F, 0.192F));
        lWingTine03a.addChild("lWingEdge03",
                ModelPartBuilder.create()
                        .uv(66, 82).mirrored(true).cuboid(-17.9F, -2.0F, 0.1F, 20.0F, 45.0F, 0.0F),
                ModelTransform.of(-0.8F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1047F));
        ModelPartData lWingTine04a = lArm02.addChild("lWingTine04a",
                ModelPartBuilder.create()
                        .uv(34, 111).mirrored(true).cuboid(-1.0F, -1.4F, -1.0F, 3.0F, 11.0F, 2.0F),
                ModelTransform.of(14.6F, 0.1F, 0.9F, 1.6232F, -0.6109F, 0.0F));
        ModelPartData lWingTine04b = lWingTine04a.addChild("lWingTine04b",
                ModelPartBuilder.create()
                        .uv(45, 111).mirrored(true).cuboid(-1.0F, 0.1F, -0.5F, 2.0F, 17.0F, 1.0F),
                ModelTransform.of(0.8F, 9.2F, 0.0F, 0.0F, 0.0F, 0.0873F));
        lWingTine04b.addChild("lWingTine04c",
                ModelPartBuilder.create()
                        .uv(27, 109).mirrored(true).cuboid(-1.0F, 0.1F, -0.49F, 2.0F, 8.0F, 1.0F),
                ModelTransform.of(0.0F, 16.6F, 0.0F, 0.0349F, 0.0F, 0.192F));
        lWingTine04a.addChild("lWingMembrane01",
                ModelPartBuilder.create()
                        .uv(94, 32).mirrored(true).cuboid(-10.2F, -0.1F, 0.2F, 16.0F, 33.0F, 0.0F),
                ModelTransform.of(-2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.192F));
        ModelPartData lWingTine05a = lArm02.addChild("lWingTine05a",
                ModelPartBuilder.create()
                        .uv(34, 111).mirrored(true).cuboid(-1.0F, -1.4F, -1.0F, 3.0F, 11.0F, 2.0F),
                ModelTransform.of(10.6F, 0.1F, 0.9F, 1.6057F, -0.6894F, 0.0F));
        ModelPartData lWingTine05b = lWingTine05a.addChild("lWingTine05b",
                ModelPartBuilder.create()
                        .uv(45, 111).mirrored(true).cuboid(-1.0F, 0.1F, -0.5F, 2.0F, 17.0F, 1.0F),
                ModelTransform.of(0.8F, 9.2F, 0.0F, 0.0F, 0.0F, 0.0873F));
        lWingTine05b.addChild("lWingTine05c",
                ModelPartBuilder.create()
                        .uv(27, 109).mirrored(true).cuboid(-1.0F, 0.1F, -0.49F, 2.0F, 8.0F, 1.0F),
                ModelTransform.of(0.0F, 16.6F, 0.0F, 0.0349F, 0.0F, 0.192F));
        lWingTine05a.addChild("lWingMembrane02",
                ModelPartBuilder.create()
                        .uv(127, 32).mirrored(true).cuboid(-10.2F, -0.1F, 0.2F, 16.0F, 33.0F, 0.0F),
                ModelTransform.of(-2.0F, 0.0F, 0.0F, 0.0F, -0.2618F, 0.2182F));
        lArm02.addChild("lWingMembrane03",
                ModelPartBuilder.create()
                        .uv(29, 32).mirrored(true).cuboid(-8.0F, 0.8F, 0.2F, 16.0F, 0.0F, 32.0F),
                ModelTransform.of(18.0F, -0.5F, 0.0F, 0.0436F, -0.6109F, 0.2182F));
        ModelPartData lHand = lArm02.addChild("lHand",
                ModelPartBuilder.create()
                        .uv(83, 21).mirrored(true).cuboid(-2.0F, -1.0F, -4.8F, 4.0F, 2.0F, 5.0F),
                ModelTransform.of(26.0F, -0.5F, -0.5F, 0.8552F, -0.3491F, -1.6144F));
        lHand.addChild("lHandClaw01",
                ModelPartBuilder.create()
                        .uv(1, 7).mirrored(true).cuboid(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 6.0F),
                ModelTransform.of(-1.3F, -0.5F, -4.6F, 0.0873F, 0.4363F, 0.0F));
        lHand.addChild("lHandClaw02",
                ModelPartBuilder.create()
                        .uv(1, 7).mirrored(true).cuboid(-0.5F, -0.5F, -4.5F, 1.0F, 2.0F, 6.0F),
                ModelTransform.of(0.0F, -0.5F, -4.6F, 0.0873F, 0.2618F, 0.0F));
        lHand.addChild("lHandClaw03",
                ModelPartBuilder.create()
                        .uv(1, 7).mirrored(true).cuboid(-0.5F, -0.5F, -4.2F, 1.0F, 2.0F, 6.0F),
                ModelTransform.of(1.3F, -0.5F, -4.6F, 0.0873F, 0.0873F, 0.0F));
        lHand.addChild("lHandClaw04",
                ModelPartBuilder.create()
                        .uv(1, 7).mirrored(true).cuboid(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 6.0F),
                ModelTransform.of(2.3F, -0.5F, -4.3F, 0.0873F, -0.1745F, 0.0F));
        lArm01.addChild("lWingMembrane04",
                ModelPartBuilder.create()
                        .uv(160, 32).mirrored(true).cuboid(-8.0F, -0.1F, 0.2F, 22.0F, 26.0F, 0.0F),
                ModelTransform.of(8.0F, 0.2F, 0.0F, 1.6057F, 0.0F, 0.0F));
        ModelPartData rArm01 = chest.addChild("rArm01",
                ModelPartBuilder.create()
                        .uv(0, 82).cuboid(-18.0F, -1.5F, -2.0F, 18.0F, 4.0F, 4.0F),
                ModelTransform.of(-6.5F, -4.0F, -3.0F, 0.0F, 0.2618F, -0.4363F));
        rArm01.addChild("rArmShoulder",
                ModelPartBuilder.create()
                        .uv(0, 99).cuboid(-3.5F, -2.0F, -2.5F, 8.0F, 4.0F, 5.0F),
                ModelTransform.of(-4.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.1222F));
        ModelPartData rArm02 = rArm01.addChild("rArm02",
                ModelPartBuilder.create()
                        .uv(0, 91).cuboid(-28.0F, -1.5F, -1.5F, 28.0F, 3.0F, 3.0F),
                ModelTransform.of(-17.0F, 0.5F, 0.0F, 0.0F, -0.6545F, -0.9163F));
        ModelPartData rWingTine01a = rArm02.addChild("rWingTine01a",
                ModelPartBuilder.create()
                        .uv(34, 111).cuboid(-2.0F, -1.4F, -1.0F, 3.0F, 11.0F, 2.0F),
                ModelTransform.of(-26.6F, 0.2F, -0.1F, 1.5708F, 0.6545F, -0.0873F));
        ModelPartData rWingTine01b = rWingTine01a.addChild("rWingTine01b",
                ModelPartBuilder.create()
                        .uv(45, 111).cuboid(-1.0F, 0.1F, -0.5F, 2.0F, 15.0F, 1.0F),
                ModelTransform.of(-0.8F, 9.2F, 0.0F, 0.0F, 0.0F, -0.0873F));
        rWingTine01b.addChild("rWingTine01c",
                ModelPartBuilder.create()
                        .uv(54, 111).cuboid(-1.0F, 0.1F, -0.49F, 1.0F, 15.0F, 1.0F),
                ModelTransform.of(0.0F, 14.6F, 0.0F, 0.0F, 0.0F, -0.192F));
        rWingTine01a.addChild("rWingEdge01",
                ModelPartBuilder.create()
                        .uv(66, 82).cuboid(-2.1F, -1.5F, -0.4F, 20.0F, 45.0F, 0.0F),
                ModelTransform.of(0.8F, 0.0F, 0.5F, 0.0F, 0.0F, -0.1047F));
        ModelPartData rWingTine02a = rArm02.addChild("rWingTine02a",
                ModelPartBuilder.create()
                        .uv(34, 111).cuboid(-2.0F, -1.4F, -1.0F, 3.0F, 11.0F, 2.0F),
                ModelTransform.of(-26.6F, 0.0F, 0.9F, 1.5708F, 0.7854F, -0.1309F));
        ModelPartData rWingTine02b = rWingTine02a.addChild("rWingTine02b",
                ModelPartBuilder.create()
                        .uv(45, 111).cuboid(-1.0F, 0.1F, -0.5F, 2.0F, 15.0F, 1.0F),
                ModelTransform.of(-0.8F, 9.2F, 0.0F, 0.0F, 0.0F, -0.0873F));
        rWingTine02b.addChild("rWingTine02c",
                ModelPartBuilder.create()
                        .uv(54, 111).cuboid(-0.6F, 0.1F, -0.49F, 1.0F, 11.0F, 1.0F),
                ModelTransform.of(0.0F, 14.6F, 0.0F, 0.0F, 0.0F, -0.192F));
        rWingTine02a.addChild("rWingEdge02",
                ModelPartBuilder.create()
                        .uv(66, 82).cuboid(-2.1F, -1.5F, -0.5F, 20.0F, 45.0F, 0.0F),
                ModelTransform.of(0.8F, 0.0F, 0.5F, 0.0F, 0.0F, -0.1047F));
        ModelPartData rWingTine03a = rArm02.addChild("rWingTine03a",
                ModelPartBuilder.create()
                        .uv(34, 111).cuboid(-2.0F, -1.4F, -1.0F, 3.0F, 11.0F, 2.0F),
                ModelTransform.of(-26.6F, -0.3F, 0.9F, 1.5708F, 0.9163F, -0.1745F));
        ModelPartData rWingTine03b = rWingTine03a.addChild("rWingTine03b",
                ModelPartBuilder.create()
                        .uv(45, 111).cuboid(-1.0F, 0.1F, -0.5F, 2.0F, 15.0F, 1.0F),
                ModelTransform.of(-0.8F, 9.2F, 0.0F, 0.0F, 0.0F, -0.0873F));
        rWingTine03b.addChild("rWingTine03c",
                ModelPartBuilder.create()
                        .uv(54, 111).cuboid(-0.6F, 0.1F, -0.49F, 1.0F, 11.0F, 1.0F),
                ModelTransform.of(0.0F, 14.6F, 0.0F, 0.0F, 0.0F, -0.192F));
        rWingTine03a.addChild("rWingEdge03",
                ModelPartBuilder.create()
                        .uv(66, 82).cuboid(-2.1F, -2.0F, 0.1F, 20.0F, 45.0F, 0.0F),
                ModelTransform.of(0.8F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1047F));
        ModelPartData rWingTine04a = rArm02.addChild("rWingTine04a",
                ModelPartBuilder.create()
                        .uv(34, 111).cuboid(-2.0F, -1.4F, -1.0F, 3.0F, 11.0F, 2.0F),
                ModelTransform.of(-14.6F, 0.1F, 0.9F, 1.6232F, 0.6109F, 0.0F));
        ModelPartData rWingTine04b = rWingTine04a.addChild("rWingTine04b",
                ModelPartBuilder.create()
                        .uv(45, 111).cuboid(-1.0F, 0.1F, -0.5F, 2.0F, 17.0F, 1.0F),
                ModelTransform.of(-0.8F, 9.2F, 0.0F, 0.0F, 0.0F, -0.0873F));
        rWingTine04b.addChild("rWingTine04c",
                ModelPartBuilder.create()
                        .uv(27, 109).cuboid(-1.0F, 0.1F, -0.49F, 2.0F, 8.0F, 1.0F),
                ModelTransform.of(0.0F, 16.6F, 0.0F, 0.0349F, 0.0F, -0.192F));
        rWingTine04a.addChild("rWingMembrane01",
                ModelPartBuilder.create()
                        .uv(94, 32).cuboid(-5.8F, -0.1F, 0.2F, 16.0F, 33.0F, 0.0F),
                ModelTransform.of(2.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.192F));
        ModelPartData rWingTine05a = rArm02.addChild("rWingTine05a",
                ModelPartBuilder.create()
                        .uv(34, 111).cuboid(-2.0F, -1.4F, -1.0F, 3.0F, 11.0F, 2.0F),
                ModelTransform.of(-10.6F, 0.1F, 0.9F, 1.6057F, 0.6894F, 0.0F));
        ModelPartData rWingTine05b = rWingTine05a.addChild("rWingTine05b",
                ModelPartBuilder.create()
                        .uv(45, 111).cuboid(-1.0F, 0.1F, -0.5F, 2.0F, 17.0F, 1.0F),
                ModelTransform.of(-0.8F, 9.2F, 0.0F, 0.0F, 0.0F, -0.0873F));
        rWingTine05b.addChild("rWingTine05c",
                ModelPartBuilder.create()
                        .uv(27, 109).cuboid(-1.0F, 0.1F, -0.49F, 2.0F, 8.0F, 1.0F),
                ModelTransform.of(0.0F, 16.6F, 0.0F, 0.0349F, 0.0F, -0.192F));
        rWingTine05a.addChild("rWingMembrane02",
                ModelPartBuilder.create()
                        .uv(127, 32).cuboid(-5.8F, -0.1F, 0.2F, 16.0F, 33.0F, 0.0F),
                ModelTransform.of(2.0F, 0.0F, 0.0F, 0.0F, 0.2618F, -0.2182F));
        rArm02.addChild("rWingMembrane03",
                ModelPartBuilder.create()
                        .uv(29, 32).cuboid(-8.0F, 0.8F, 0.2F, 16.0F, 0.0F, 32.0F),
                ModelTransform.of(-18.0F, -0.5F, 0.0F, 0.0436F, 0.6109F, -0.2182F));
        ModelPartData rHand = rArm02.addChild("rHand",
                ModelPartBuilder.create()
                        .uv(83, 21).cuboid(-2.0F, -1.0F, -4.8F, 4.0F, 2.0F, 5.0F),
                ModelTransform.of(-26.0F, -0.5F, -0.5F, 0.8552F, 0.3491F, 1.6144F));
        rHand.addChild("rHandClaw01",
                ModelPartBuilder.create()
                        .uv(1, 7).cuboid(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 6.0F),
                ModelTransform.of(1.3F, -0.5F, -4.6F, 0.0873F, -0.4363F, 0.0F));
        rHand.addChild("rHandClaw02",
                ModelPartBuilder.create()
                        .uv(1, 7).cuboid(-0.5F, -0.5F, -4.5F, 1.0F, 2.0F, 6.0F),
                ModelTransform.of(0.0F, -0.5F, -4.6F, 0.0873F, -0.2618F, 0.0F));
        rHand.addChild("rHandClaw03",
                ModelPartBuilder.create()
                        .uv(1, 7).cuboid(-0.5F, -0.5F, -4.2F, 1.0F, 2.0F, 6.0F),
                ModelTransform.of(-1.3F, -0.5F, -4.6F, 0.0873F, -0.0873F, 0.0F));
        rHand.addChild("rHandClaw04",
                ModelPartBuilder.create()
                        .uv(1, 7).cuboid(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 6.0F),
                ModelTransform.of(-2.3F, -0.5F, -4.3F, 0.0873F, 0.1745F, 0.0F));
        rArm01.addChild("rWingMembrane04",
                ModelPartBuilder.create()
                        .uv(160, 32).cuboid(-14.0F, -0.1F, 0.2F, 22.0F, 26.0F, 0.0F),
                ModelTransform.of(-8.0F, 0.2F, 0.0F, 1.6057F, 0.0F, 0.0F));
        ModelPartData neck01 = chest.addChild("neck01",
                ModelPartBuilder.create()
                        .uv(105, 0).cuboid(-6.0F, -7.0F, -8.0F, 12.0F, 13.0F, 10.0F),
                ModelTransform.of(0.0F, 0.0F, -7.0F, -0.3491F, 0.0F, 0.0F));
        ModelPartData neck02 = neck01.addChild("neck02",
                ModelPartBuilder.create()
                        .uv(151, 0).cuboid(-5.0F, -5.0F, -13.0F, 10.0F, 10.0F, 13.0F),
                ModelTransform.of(0.0F, -2.6F, -5.0F, 0.5236F, 0.0F, 0.0F));
        ModelPartData neck03 = neck02.addChild("neck03",
                ModelPartBuilder.create()
                        .uv(203, 0).cuboid(-4.0F, -4.5F, -13.0F, 8.0F, 9.0F, 13.0F),
                ModelTransform.of(0.0F, 0.7F, -11.0F, -0.3491F, 0.0F, 0.0F));
        ModelPartData head = neck03.addChild("head",
                ModelPartBuilder.create()
                        .uv(206, 30).cuboid(-5.0F, -4.5F, -6.0F, 10.0F, 12.0F, 10.0F),
                ModelTransform.of(0.0F, -0.3F, -12.0F, -0.7854F, 0.0F, 0.0F));
        head.addChild("rLead_r1",
                ModelPartBuilder.create()
                        .uv(246, 54).mirrored(true).cuboid(-0.75F, -49.5F, -2.0F, 0.0F, 49.0F, 2.0F),
                ModelTransform.of(-3.5F, 13.45F, 0.0F, -0.2182F, 0.0F, -0.0698F));
        head.addChild("lLead_r1",
                ModelPartBuilder.create()
                        .uv(246, 54).cuboid(0.75F, -49.5F, -2.0F, 0.0F, 49.0F, 2.0F),
                ModelTransform.of(3.0F, 13.45F, 0.0F, -0.2182F, 0.0F, 0.0698F));
        head.addChild("lEye",
                ModelPartBuilder.create()
                        .uv(236, 25).mirrored(true).cuboid(0.0F, -3.5F, -2.0F, 3.0F, 7.0F, 5.0F),
                ModelTransform.of(3.0F, 1.7F, -3.0F, 0.0F, 0.0F, 0.0F));
        head.addChild("rEye",
                ModelPartBuilder.create()
                        .uv(236, 25).cuboid(-2.5F, -3.5F, -2.0F, 3.0F, 7.0F, 5.0F),
                ModelTransform.of(-3.5F, 1.7F, -3.0F, 0.0F, 0.0F, 0.0F));
        ModelPartData beakTop01 = head.addChild("beakTop01",
                ModelPartBuilder.create()
                        .uv(110, 70).cuboid(-3.0F, 0.0F, -2.0F, 6.0F, 13.0F, 2.0F),
                ModelTransform.of(0.0F, 7.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        beakTop01.addChild("beakTop02",
                ModelPartBuilder.create()
                        .uv(130, 70).cuboid(-2.5F, 0.3F, -1.0F, 5.0F, 13.0F, 3.0F),
                ModelTransform.of(0.0F, 0.0F, -4.0F, 0.1745F, 0.0F, 0.0F));
        beakTop01.addChild("lBeakFang",
                ModelPartBuilder.create()
                        .uv(129, 89).mirrored(true).cuboid(0.0F, -6.0F, -0.3F, 0.0F, 9.0F, 4.0F),
                ModelTransform.of(2.6F, 10.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        beakTop01.addChild("rBeakFang",
                ModelPartBuilder.create()
                        .uv(129, 89).cuboid(0.0F, -6.0F, -0.3F, 0.0F, 9.0F, 4.0F),
                ModelTransform.of(-3.1F, 10.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        head.addChild("beakLower",
                ModelPartBuilder.create()
                        .uv(110, 90).cuboid(-2.5F, -0.1F, -1.0F, 5.0F, 13.0F, 2.0F),
                ModelTransform.of(0.0F, 7.0F, 1.0F, -0.0873F, 0.0F, 0.0F));
        head.addChild("lHeadFur",
                ModelPartBuilder.create()
                        .uv(150, 94).cuboid(-1.0F, -8.0F, -6.0F, 2.0F, 12.0F, 6.0F),
                ModelTransform.of(2.0F, 1.0F, -4.0F, -0.2618F, 0.0F, 0.0F));
        head.addChild("rHeadFur",
                ModelPartBuilder.create()
                        .uv(150, 94).mirrored(true).cuboid(-1.0F, -8.0F, -6.0F, 2.0F, 12.0F, 6.0F),
                ModelTransform.of(-2.5F, 1.0F, -4.0F, -0.2618F, 0.0F, 0.0F));
        neck03.addChild("fur01",
                ModelPartBuilder.create()
                        .uv(150, 70).cuboid(-3.5F, -1.0F, 0.0F, 7.0F, 8.0F, 2.0F),
                ModelTransform.of(0.0F, -4.0F, -9.9F, 2.1817F, 0.0F, 0.0F));
        neck03.addChild("fur02",
                ModelPartBuilder.create()
                        .uv(170, 70).cuboid(-4.0F, -1.0F, 0.0F, 8.0F, 9.0F, 2.0F, new Dilation(0.1F, 0.1F, 0.1F)),
                ModelTransform.of(0.0F, -3.3F, -7.1F, 2.1817F, 0.0F, 0.0F));
        neck02.addChild("fur03",
                ModelPartBuilder.create()
                        .uv(191, 70).cuboid(-4.5F, -1.0F, 0.0F, 9.0F, 9.0F, 2.0F),
                ModelTransform.of(0.0F, -3.7F, -12.5F, 1.8326F, 0.0F, 0.0F));
        neck02.addChild("fur04",
                ModelPartBuilder.create()
                        .uv(214, 70).cuboid(-4.5F, -1.0F, 0.0F, 9.0F, 9.0F, 2.0F),
                ModelTransform.of(0.0F, -3.3F, -7.1F, 1.8326F, 0.0F, 0.0F));
        neck01.addChild("fur05",
                ModelPartBuilder.create()
                        .uv(150, 83).cuboid(-5.0F, -1.0F, 0.0F, 10.0F, 8.0F, 2.0F),
                ModelTransform.of(0.0F, -5.7F, -6.6F, 2.2689F, 0.0F, 0.0F));
        neck01.addChild("fur06",
                ModelPartBuilder.create()
                        .uv(176, 83).cuboid(-5.0F, -1.0F, 0.0F, 10.0F, 8.0F, 2.0F),
                ModelTransform.of(0.0F, -5.7F, -3.6F, 2.1817F, 0.0F, 0.0F));
        chest.addChild("fur07",
                ModelPartBuilder.create()
                        .uv(203, 83).cuboid(-6.0F, -1.0F, 0.0F, 12.0F, 10.0F, 2.0F),
                ModelTransform.of(0.0F, -5.7F, -5.6F, 1.8326F, 0.0F, 0.0F));
        ModelPartData saddle = chest.addChild("saddle",
                ModelPartBuilder.create(),
                ModelTransform.of(0.0F, 20.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        ModelPartData saddleBase = saddle.addChild("saddleBase",
                ModelPartBuilder.create()
                        .uv(196, 96).cuboid(-7.0F, -1.0F, -7.5F, 14.0F, 2.0F, 13.0F),
                ModelTransform.of(0.0F, -28.4F, 1.0F, 0.0F, 0.0F, 0.0F));
        saddleBase.addChild("saddleBack",
                ModelPartBuilder.create()
                        .uv(212, 116).cuboid(-6.0F, -1.0F, -1.5F, 12.0F, 2.0F, 3.0F),
                ModelTransform.of(0.0F, -2.0F, 4.0F, 0.0F, 0.0F, 0.0F));
        saddleBase.addChild("saddleFront",
                ModelPartBuilder.create()
                        .uv(212, 121).cuboid(-3.0F, -1.0F, -1.5F, 6.0F, 2.0F, 3.0F),
                ModelTransform.of(0.0F, -2.0F, -6.0F, 0.0F, 0.0F, 0.0F));
        saddle.addChild("saddleCarpet",
                ModelPartBuilder.create()
                        .uv(152, 99).cuboid(-7.0F, -1.5F, -7.5F, 14.0F, 12.0F, 15.0F, new Dilation(0.2F, 0.2F, 0.2F)),
                ModelTransform.of(0.0F, -26.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(data, 256, 128);
    }

    @Override
    public void setAngles(ByakheeEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yaw = netHeadYaw * 0.017453292F;
        this.rLead_r1.visible = entity.hasPassengers();
        this.lLead_r1.visible = entity.hasPassengers();

        if (entity.headShakeTicks > 0){
            this.head.roll = 0.3F * MathHelper.sin( 6.28F * (entity.headShakeTicks - MinecraftClient.getInstance().getTickDelta()) / 20F);
        }else{
            this.head.roll = 0;
        }
        if (entity.isGliding()){
            this.lLeg01.pitch = 1.0036F;
            this.lLeg01.yaw = 0.0349F;
            this.rLeg01.pitch = 1.0036F;
            this.rLeg01.yaw = -0.0349F;

            this.lLeg02.pitch = 0;
            this.rLeg02.pitch = 0;

            this.lFoot.pitch = 1.2915F;
            this.rFoot.pitch = 1.2915F;


            this.lArm01.yaw = 0;
            this.rArm01.yaw = 0;
            this.lArm01.roll = -(1 + MathHelper.sin(ageInTicks * 0.125F)) * 0.4F;
            this.rArm01.roll = (1 + MathHelper.sin(ageInTicks * 0.125F)) * 0.4F;
            this.lArm02.yaw = 0.6545F;
            this.lArm02.roll = 0;
            this.rArm02.yaw = -0.6545F;
            this.rArm02.roll = 0;

            this.lWingTine01a.yaw = 1.1781F;
            this.rWingTine01a.yaw = -1.1781F;

            this.lWingTine02a.yaw = 0.829F;
            this.lWingTine02a.roll = 0.0873F;
            this.rWingTine02a.yaw = -0.829F;
            this.rWingTine02a.roll = -0.0873F;

            this.lWingTine03a.yaw = 0.5236F;
            this.lWingTine03a.roll = 0.0873F;
            this.rWingTine03a.yaw = -0.5236F;
            this.rWingTine03a.roll = -0.0873F;

            this.lWingTine04a.yaw = 0.2182F;
            this.rWingTine04a.yaw = -0.2182F;

            this.lWingTine05a.yaw = -0.2094F;
            this.rWingTine05a.yaw = 0.2094F;

            this.lWingMembrane02.yaw = 0;
            this.rWingMembrane02.yaw = 0;
            this.lWingMembrane03.yaw = 0.3054F;
            this.rWingMembrane03.yaw = -0.3054F;

            this.lHand.pitch = -0.0611F;
            this.lHand.roll = 0;
            this.rHand.pitch = -0.0611F;
            this.rHand.roll = 0;
            this.head.pitch = -1.3963F + headPitch * 0.017453292F;
            this.rLead_r1.pitch = -0.0436F - headPitch * 0.017453292F;
            this.lLead_r1.pitch = -0.0436F - headPitch * 0.017453292F;

            this.neck03.pitch = 0.0873F;
            this.tail_r1.pitch = 1.309F;
        }else {
            this.rLeg01.pitch = -0.5236F - (MathHelper.cos(limbSwing * 0.25F)) * 0.2F * limbSwingAmount;
            this.rLeg01.yaw = 0.576F;
            this.lLeg01.pitch = -0.5236F + (MathHelper.cos(limbSwing * 0.25F)) * 0.2F * limbSwingAmount;
            this.lLeg01.yaw = -0.576F;

            this.rLeg02.pitch = -(MathHelper.sin(limbSwing * 0.25F)) * 0.2F * limbSwingAmount;
            this.lLeg02.pitch = (MathHelper.sin(limbSwing * 0.25F)) * 0.2F * limbSwingAmount;

            this.rFoot.pitch = 0.0175F + MathHelper.cos(limbSwing * 0.25F) * 0.4F * limbSwingAmount;
            this.lFoot.pitch = -0.0175F - MathHelper.cos(limbSwing * 0.25F) * 0.4F * limbSwingAmount;

            this.lArm01.yaw = -0.2618F + (MathHelper.sin(limbSwing * 0.25F)) * 0.3F * limbSwingAmount;
            this.lArm01.roll = 0.4363F;
            this.rArm01.yaw = 0.2618F + (MathHelper.sin(limbSwing * 0.25F)) * 0.3F * limbSwingAmount;
            this.rArm01.roll = -0.4363F;

            this.lArm02.yaw = 0.6545F + (MathHelper.cos(limbSwing * 0.25F) + 0.95F) * 0.3F * limbSwingAmount;
            this.lArm02.roll = 0.9163F;
            this.rArm02.yaw = -0.6545F + (MathHelper.cos(limbSwing * 0.25F) - 0.95F) * 0.3F * limbSwingAmount;
            this.rArm02.roll = -0.9163F;

            this.lWingTine01a.yaw = -0.6545F;
            this.rWingTine01a.yaw = 0.6545F;

            this.lWingTine02a.yaw = -0.7854F;
            this.lWingTine02a.roll = 0.1309F;
            this.rWingTine02a.yaw = 0.7854F;
            this.rWingTine02a.roll = -0.1309F;

            this.lWingTine03a.yaw = -0.9163F;
            this.lWingTine03a.roll = 0.1745F;
            this.rWingTine03a.yaw = 0.9163F;
            this.rWingTine03a.roll = -0.1745F;

            this.lWingTine04a.yaw = -0.6109F;
            this.rWingTine04a.yaw = 0.6109F;

            this.lWingTine05a.yaw = -0.6894F;
            this.rWingTine05a.yaw = 0.6894F;


            this.lWingMembrane02.yaw = -0.2618F;
            this.rWingMembrane02.yaw = 0.2618F;

            this.lWingMembrane03.yaw = -0.6109F;
            this.rWingMembrane03.yaw = 0.6109F;

            this.lHand.pitch = 0.8552F + MathHelper.cos(limbSwing * 0.25F) * 0.4F * limbSwingAmount;
            this.lHand.roll = -1.6144F;
            this.rHand.pitch = 0.8552F - MathHelper.cos(limbSwing * 0.25F) * 0.4F * limbSwingAmount;
            this.rHand.roll = 1.6144F;

            this.head.pitch = -0.7854F + headPitch * 0.017453292F;
            this.rLead_r1.pitch = -0.2182F - headPitch * 0.017453292F;
            this.lLead_r1.pitch = -0.2182F - headPitch * 0.017453292F;

            this.neck03.pitch = -0.3491F;
            this.tail_r1.pitch = 0.9163F;
        }
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        root.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }
}