// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

package com.miskatonicmysteries.client.model.entity;

import com.miskatonicmysteries.common.entity.ByakheeEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.TargetFinder;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class ByakheeEntityModel extends EntityModel<ByakheeEntity> {
    private final ModelPart chest;
    private final ModelPart lowerBody;
    private final ModelPart tail_r1;
    private final ModelPart stomach;
    private final ModelPart lLeg01;
    private final ModelPart lLeg02;
    private final ModelPart lLeg03;
    private final ModelPart lFoot;
    private final ModelPart lFootClaw01;
    private final ModelPart lFootClaw02;
    private final ModelPart lFootClaw03;
    private final ModelPart lWebbing;
    private final ModelPart rLeg01;
    private final ModelPart rLeg02;
    private final ModelPart rLeg03;
    private final ModelPart rFoot;
    private final ModelPart rFootClaw01;
    private final ModelPart rFootClaw02;
    private final ModelPart rFootClaw03;
    private final ModelPart rWebbing;
    private final ModelPart lArm01;
    private final ModelPart lArmShoulder;
    private final ModelPart lArm02;
    private final ModelPart lWingTine01a;
    private final ModelPart lWingTine01b;
    private final ModelPart lWingTine01c;
    private final ModelPart lWingEdge01;
    private final ModelPart lWingTine02a;
    private final ModelPart lWingTine02b;
    private final ModelPart lWingTine02c;
    private final ModelPart lWingEdge02;
    private final ModelPart lWingTine03a;
    private final ModelPart lWingTine03b;
    private final ModelPart lWingTine03c;
    private final ModelPart lWingEdge03;
    private final ModelPart lWingTine04a;
    private final ModelPart lWingTine04b;
    private final ModelPart lWingTine04c;
    private final ModelPart lWingMembrane01;
    private final ModelPart lWingTine05a;
    private final ModelPart lWingTine05b;
    private final ModelPart lWingTine05c;
    private final ModelPart lWingMembrane02;
    private final ModelPart lWingMembrane03;
    private final ModelPart lHand;
    private final ModelPart lHandClaw01;
    private final ModelPart lHandClaw02;
    private final ModelPart lHandClaw03;
    private final ModelPart lHandClaw04;
    private final ModelPart lWingMembrane04;
    private final ModelPart rArm01;
    private final ModelPart rArmShoulder;
    private final ModelPart rArm02;
    private final ModelPart rWingTine01a;
    private final ModelPart rWingTine01b;
    private final ModelPart rWingTine01c;
    private final ModelPart rWingEdge01;
    private final ModelPart rWingTine02a;
    private final ModelPart rWingTine02b;
    private final ModelPart rWingTine02c;
    private final ModelPart rWingEdge02;
    private final ModelPart rWingTine03a;
    private final ModelPart rWingTine03b;
    private final ModelPart rWingTine03c;
    private final ModelPart rWingEdge03;
    private final ModelPart rWingTine04a;
    private final ModelPart rWingTine04b;
    private final ModelPart rWingTine04c;
    private final ModelPart rWingMembrane01;
    private final ModelPart rWingTine05a;
    private final ModelPart rWingTine05b;
    private final ModelPart rWingTine05c;
    private final ModelPart rWingMembrane02;
    private final ModelPart rWingMembrane03;
    private final ModelPart rHand;
    private final ModelPart rHandClaw01;
    private final ModelPart rHandClaw02;
    private final ModelPart rHandClaw03;
    private final ModelPart rHandClaw04;
    private final ModelPart rWingMembrane04;
    private final ModelPart neck01;
    private final ModelPart neck02;
    private final ModelPart neck03;
    private final ModelPart head;
    private final ModelPart rLead_r1;
    private final ModelPart lLead_r1;
    private final ModelPart lEye;
    private final ModelPart rEye;
    private final ModelPart beakTop01;
    private final ModelPart beakTop02;
    private final ModelPart lBeakFang;
    private final ModelPart rBeakFang;
    private final ModelPart beakLower;
    private final ModelPart lHeadFur;
    private final ModelPart rHeadFur;
    private final ModelPart fur01;
    private final ModelPart fur02;
    private final ModelPart fur03;
    private final ModelPart fur04;
    private final ModelPart fur05;
    private final ModelPart fur06;
    private final ModelPart fur07;
    private final ModelPart saddle;
    private final ModelPart saddleBase;
    private final ModelPart saddleBack;
    private final ModelPart saddleFront;
    private final ModelPart saddleCarpet;

    public ByakheeEntityModel() {
    	super(RenderLayer::getEntityCutout);
        textureWidth = 256;
        textureHeight = 128;
        chest = new ModelPart(this);
        chest.setPivot(0.25F, 1.25F, 4.0F);
        setRotationAngle(chest, -0.2182F, 0.0F, 0.0F);
        chest.setTextureOffset(0, 0).addCuboid(-7.0F, -7.5F, -8.5F, 14.0F, 13.0F, 18.0F, 0.0F, false);

        lowerBody = new ModelPart(this);
        lowerBody.setPivot(0.0F, -1.2F, 8.0F);
        chest.addChild(lowerBody);
        setRotationAngle(lowerBody, 0.0873F, 0.0F, 0.0F);
        lowerBody.setTextureOffset(0, 32).addCuboid(-6.0F, -5.5F, 0.5F, 12.0F, 6.0F, 18.0F, 0.0F, false);

        tail_r1 = new ModelPart(this);
        tail_r1.setPivot(-0.25F, -5.05F, 19.0F);
        lowerBody.addChild(tail_r1);
        setRotationAngle(tail_r1, 0.9163F, 0.0F, 0.0F);
        tail_r1.setTextureOffset(121, 119).addCuboid(-5.0F, -0.8F, 0.0F, 11.0F, 8.0F, 0.0F, 0.0F, false);

        stomach = new ModelPart(this);
        stomach.setPivot(0.0F, 3.2F, -1.0F);
        lowerBody.addChild(stomach);
        setRotationAngle(stomach, 0.2618F, 0.0F, 0.0F);
        stomach.setTextureOffset(0, 57).addCuboid(-4.5F, -2.5F, 0.5F, 9.0F, 5.0F, 18.0F, 0.0F, false);

        lLeg01 = new ModelPart(this);
        lLeg01.setPivot(3.9F, -1.4F, 15.1F);
        lowerBody.addChild(lLeg01);
        setRotationAngle(lLeg01, -0.5236F, -0.576F, -0.1222F);
        lLeg01.setTextureOffset(66, 0).addCuboid(-2.3F, -1.1F, -3.0F, 5.0F, 11.0F, 6.0F, 0.0F, true);

        lLeg02 = new ModelPart(this);
        lLeg02.setPivot(0.3F, 10.0F, -0.1F);
        lLeg01.addChild(lLeg02);
        lLeg02.setTextureOffset(64, 19).addCuboid(-2.0F, -0.4F, -2.5F, 4.0F, 4.0F, 5.0F, 0.0F, true);

        lLeg03 = new ModelPart(this);
        lLeg03.setPivot(0.0F, 4.1F, 0.8F);
        lLeg02.addChild(lLeg03);
        setRotationAngle(lLeg03, 0.7156F, 0.0F, 0.0873F);
        lLeg03.setTextureOffset(51, 1).addCuboid(-1.5F, -2.2F, -1.5F, 3.0F, 11.0F, 3.0F, 0.0F, true);

        lFoot = new ModelPart(this);
        lFoot.setPivot(0.0F, 8.5F, -0.5F);
        lLeg03.addChild(lFoot);
        setRotationAngle(lFoot, -0.0175F, 0.0F, -0.0524F);
        lFoot.setTextureOffset(83, 21).addCuboid(-2.0F, 0.0F, -2.8F, 4.0F, 2.0F, 5.0F, 0.0F, true);

        lFootClaw01 = new ModelPart(this);
        lFootClaw01.setPivot(-1.3F, 0.5F, -2.6F);
        lFoot.addChild(lFootClaw01);
        setRotationAngle(lFootClaw01, 0.1396F, 0.2094F, 0.0F);
        lFootClaw01.setTextureOffset(1, 7).addCuboid(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 6.0F, 0.0F, true);

        lFootClaw02 = new ModelPart(this);
        lFootClaw02.setPivot(0.0F, 0.5F, -2.6F);
        lFoot.addChild(lFootClaw02);
        setRotationAngle(lFootClaw02, 0.1396F, 0.0F, 0.0F);
        lFootClaw02.setTextureOffset(1, 7).addCuboid(-0.5F, -0.5F, -4.5F, 1.0F, 2.0F, 6.0F, 0.0F, true);

        lFootClaw03 = new ModelPart(this);
        lFootClaw03.setPivot(1.3F, 0.5F, -2.6F);
        lFoot.addChild(lFootClaw03);
        setRotationAngle(lFootClaw03, 0.1222F, -0.2618F, 0.0F);
        lFootClaw03.setTextureOffset(1, 7).addCuboid(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 6.0F, 0.0F, true);

        lWebbing = new ModelPart(this);
        lWebbing.setPivot(0.0F, 1.0F, -2.0F);
        lFoot.addChild(lWebbing);
        lWebbing.setTextureOffset(110, 110).addCuboid(-2.0F, 0.0F, -4.0F, 4.0F, 0.0F, 5.0F, 0.0F, true);

        rLeg01 = new ModelPart(this);
        rLeg01.setPivot(-3.9F, -1.4F, 15.1F);
        lowerBody.addChild(rLeg01);
        setRotationAngle(rLeg01, -0.5236F, 0.576F, 0.1222F);
        rLeg01.setTextureOffset(66, 0).addCuboid(-2.7F, -1.1F, -3.0F, 5.0F, 11.0F, 6.0F, 0.0F, false);

        rLeg02 = new ModelPart(this);
        rLeg02.setPivot(-0.3F, 10.0F, -0.1F);
        rLeg01.addChild(rLeg02);
        rLeg02.setTextureOffset(64, 19).addCuboid(-2.0F, -0.4F, -2.5F, 4.0F, 4.0F, 5.0F, 0.0F, false);

        rLeg03 = new ModelPart(this);
        rLeg03.setPivot(0.0F, 4.1F, 0.8F);
        rLeg02.addChild(rLeg03);
        setRotationAngle(rLeg03, 0.7156F, 0.0F, -0.0873F);
        rLeg03.setTextureOffset(51, 1).addCuboid(-1.5F, -2.2F, -1.5F, 3.0F, 11.0F, 3.0F, 0.0F, false);

        rFoot = new ModelPart(this);
        rFoot.setPivot(0.0F, 8.5F, -0.5F);
        rLeg03.addChild(rFoot);
        setRotationAngle(rFoot, -0.0175F, 0.0F, 0.0524F);
        rFoot.setTextureOffset(83, 21).addCuboid(-2.0F, 0.0F, -2.8F, 4.0F, 2.0F, 5.0F, 0.0F, false);

        rFootClaw01 = new ModelPart(this);
        rFootClaw01.setPivot(1.3F, 0.5F, -2.6F);
        rFoot.addChild(rFootClaw01);
        setRotationAngle(rFootClaw01, 0.1396F, -0.2094F, 0.0F);
        rFootClaw01.setTextureOffset(1, 7).addCuboid(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 6.0F, 0.0F, false);

        rFootClaw02 = new ModelPart(this);
        rFootClaw02.setPivot(0.0F, 0.5F, -2.6F);
        rFoot.addChild(rFootClaw02);
        setRotationAngle(rFootClaw02, 0.1396F, 0.0F, 0.0F);
        rFootClaw02.setTextureOffset(1, 7).addCuboid(-0.5F, -0.5F, -4.5F, 1.0F, 2.0F, 6.0F, 0.0F, false);

        rFootClaw03 = new ModelPart(this);
        rFootClaw03.setPivot(-1.3F, 0.5F, -2.6F);
        rFoot.addChild(rFootClaw03);
        setRotationAngle(rFootClaw03, 0.1222F, 0.2618F, 0.0F);
        rFootClaw03.setTextureOffset(1, 7).addCuboid(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 6.0F, 0.0F, false);

        rWebbing = new ModelPart(this);
        rWebbing.setPivot(0.0F, 1.0F, -2.0F);
        rFoot.addChild(rWebbing);
        rWebbing.setTextureOffset(110, 110).addCuboid(-2.0F, 0.0F, -4.0F, 4.0F, 0.0F, 5.0F, 0.0F, false);

        lArm01 = new ModelPart(this);
        lArm01.setPivot(6.0F, -4.0F, -3.0F);
        chest.addChild(lArm01);
        setRotationAngle(lArm01, 0.0F, -0.2618F, 0.4363F);
        lArm01.setTextureOffset(0, 82).addCuboid(0.0F, -1.5F, -2.0F, 18.0F, 4.0F, 4.0F, 0.0F, true);

        lArmShoulder = new ModelPart(this);
        lArmShoulder.setPivot(4.0F, -1.0F, 0.0F);
        lArm01.addChild(lArmShoulder);
        setRotationAngle(lArmShoulder, 0.0F, 0.0F, 0.1222F);
        lArmShoulder.setTextureOffset(0, 99).addCuboid(-4.5F, -2.0F, -2.5F, 8.0F, 4.0F, 5.0F, 0.0F, true);

        lArm02 = new ModelPart(this);
        lArm02.setPivot(17.0F, 0.5F, 0.0F);
        lArm01.addChild(lArm02);
        setRotationAngle(lArm02, 0.0F, 0.6545F, 0.9163F);
        lArm02.setTextureOffset(0, 91).addCuboid(0.0F, -1.5F, -1.5F, 28.0F, 3.0F, 3.0F, 0.0F, true);

        lWingTine01a = new ModelPart(this);
        lWingTine01a.setPivot(26.6F, 0.2F, -0.1F);
        lArm02.addChild(lWingTine01a);
        setRotationAngle(lWingTine01a, 1.5708F, -0.6545F, 0.0873F);
        lWingTine01a.setTextureOffset(34, 111).addCuboid(-1.0F, -1.4F, -1.0F, 3.0F, 11.0F, 2.0F, 0.0F, true);

        lWingTine01b = new ModelPart(this);
        lWingTine01b.setPivot(0.8F, 9.2F, 0.0F);
        lWingTine01a.addChild(lWingTine01b);
        setRotationAngle(lWingTine01b, 0.0F, 0.0F, 0.0873F);
        lWingTine01b.setTextureOffset(45, 111).addCuboid(-1.0F, 0.1F, -0.5F, 2.0F, 15.0F, 1.0F, 0.0F, true);

        lWingTine01c = new ModelPart(this);
        lWingTine01c.setPivot(0.0F, 14.6F, 0.0F);
        lWingTine01b.addChild(lWingTine01c);
        setRotationAngle(lWingTine01c, 0.0F, 0.0F, 0.192F);
        lWingTine01c.setTextureOffset(54, 111).addCuboid(0.0F, 0.1F, -0.49F, 1.0F, 15.0F, 1.0F, 0.0F, true);

        lWingEdge01 = new ModelPart(this);
        lWingEdge01.setPivot(-0.8F, 0.0F, 0.5F);
        lWingTine01a.addChild(lWingEdge01);
        setRotationAngle(lWingEdge01, 0.0F, 0.0F, 0.1047F);
        lWingEdge01.setTextureOffset(66, 82).addCuboid(-17.9F, -1.5F, -0.4F, 20.0F, 45.0F, 0.0F, 0.0F, true);

        lWingTine02a = new ModelPart(this);
        lWingTine02a.setPivot(26.6F, 0.0F, 0.9F);
        lArm02.addChild(lWingTine02a);
        setRotationAngle(lWingTine02a, 1.5708F, -0.7854F, 0.1309F);
        lWingTine02a.setTextureOffset(34, 111).addCuboid(-1.0F, -1.4F, -1.0F, 3.0F, 11.0F, 2.0F, 0.0F, true);

        lWingTine02b = new ModelPart(this);
        lWingTine02b.setPivot(0.8F, 9.2F, 0.0F);
        lWingTine02a.addChild(lWingTine02b);
        setRotationAngle(lWingTine02b, 0.0F, 0.0F, 0.0873F);
        lWingTine02b.setTextureOffset(45, 111).addCuboid(-1.0F, 0.1F, -0.5F, 2.0F, 15.0F, 1.0F, 0.0F, true);

        lWingTine02c = new ModelPart(this);
        lWingTine02c.setPivot(0.0F, 14.6F, 0.0F);
        lWingTine02b.addChild(lWingTine02c);
        setRotationAngle(lWingTine02c, 0.0F, 0.0F, 0.192F);
        lWingTine02c.setTextureOffset(54, 111).addCuboid(-0.4F, 0.1F, -0.49F, 1.0F, 11.0F, 1.0F, 0.0F, true);

        lWingEdge02 = new ModelPart(this);
        lWingEdge02.setPivot(-0.8F, 0.0F, 0.5F);
        lWingTine02a.addChild(lWingEdge02);
        setRotationAngle(lWingEdge02, 0.0F, 0.0F, 0.1047F);
        lWingEdge02.setTextureOffset(66, 82).addCuboid(-17.9F, -1.5F, -0.5F, 20.0F, 45.0F, 0.0F, 0.0F, true);

        lWingTine03a = new ModelPart(this);
        lWingTine03a.setPivot(26.6F, -0.3F, 0.9F);
        lArm02.addChild(lWingTine03a);
        setRotationAngle(lWingTine03a, 1.5708F, -0.9163F, 0.1745F);
        lWingTine03a.setTextureOffset(34, 111).addCuboid(-1.0F, -1.4F, -1.0F, 3.0F, 11.0F, 2.0F, 0.0F, true);

        lWingTine03b = new ModelPart(this);
        lWingTine03b.setPivot(0.8F, 9.2F, 0.0F);
        lWingTine03a.addChild(lWingTine03b);
        setRotationAngle(lWingTine03b, 0.0F, 0.0F, 0.0873F);
        lWingTine03b.setTextureOffset(45, 111).addCuboid(-1.0F, 0.1F, -0.5F, 2.0F, 15.0F, 1.0F, 0.0F, true);

        lWingTine03c = new ModelPart(this);
        lWingTine03c.setPivot(0.0F, 14.6F, 0.0F);
        lWingTine03b.addChild(lWingTine03c);
        setRotationAngle(lWingTine03c, 0.0F, 0.0F, 0.192F);
        lWingTine03c.setTextureOffset(54, 111).addCuboid(-0.4F, 0.1F, -0.49F, 1.0F, 11.0F, 1.0F, 0.0F, true);

        lWingEdge03 = new ModelPart(this);
        lWingEdge03.setPivot(-0.8F, 0.0F, 0.0F);
        lWingTine03a.addChild(lWingEdge03);
        setRotationAngle(lWingEdge03, 0.0F, 0.0F, 0.1047F);
        lWingEdge03.setTextureOffset(66, 82).addCuboid(-17.9F, -2.0F, 0.1F, 20.0F, 45.0F, 0.0F, 0.0F, true);

        lWingTine04a = new ModelPart(this);
        lWingTine04a.setPivot(14.6F, 0.1F, 0.9F);
        lArm02.addChild(lWingTine04a);
        setRotationAngle(lWingTine04a, 1.6232F, -0.6109F, 0.0F);
        lWingTine04a.setTextureOffset(34, 111).addCuboid(-1.0F, -1.4F, -1.0F, 3.0F, 11.0F, 2.0F, 0.0F, true);

        lWingTine04b = new ModelPart(this);
        lWingTine04b.setPivot(0.8F, 9.2F, 0.0F);
        lWingTine04a.addChild(lWingTine04b);
        setRotationAngle(lWingTine04b, 0.0F, 0.0F, 0.0873F);
        lWingTine04b.setTextureOffset(45, 111).addCuboid(-1.0F, 0.1F, -0.5F, 2.0F, 17.0F, 1.0F, 0.0F, true);

        lWingTine04c = new ModelPart(this);
        lWingTine04c.setPivot(0.0F, 16.6F, 0.0F);
        lWingTine04b.addChild(lWingTine04c);
        setRotationAngle(lWingTine04c, 0.0349F, 0.0F, 0.192F);
        lWingTine04c.setTextureOffset(27, 109).addCuboid(-1.0F, 0.1F, -0.49F, 2.0F, 8.0F, 1.0F, 0.0F, true);

        lWingMembrane01 = new ModelPart(this);
        lWingMembrane01.setPivot(-2.0F, 0.0F, 0.0F);
        lWingTine04a.addChild(lWingMembrane01);
        setRotationAngle(lWingMembrane01, 0.0F, 0.0F, 0.192F);
        lWingMembrane01.setTextureOffset(94, 32).addCuboid(-10.2F, -0.1F, 0.2F, 16.0F, 33.0F, 0.0F, 0.0F, true);

        lWingTine05a = new ModelPart(this);
        lWingTine05a.setPivot(10.6F, 0.1F, 0.9F);
        lArm02.addChild(lWingTine05a);
        setRotationAngle(lWingTine05a, 1.6057F, -0.6894F, 0.0F);
        lWingTine05a.setTextureOffset(34, 111).addCuboid(-1.0F, -1.4F, -1.0F, 3.0F, 11.0F, 2.0F, 0.0F, true);

        lWingTine05b = new ModelPart(this);
        lWingTine05b.setPivot(0.8F, 9.2F, 0.0F);
        lWingTine05a.addChild(lWingTine05b);
        setRotationAngle(lWingTine05b, 0.0F, 0.0F, 0.0873F);
        lWingTine05b.setTextureOffset(45, 111).addCuboid(-1.0F, 0.1F, -0.5F, 2.0F, 17.0F, 1.0F, 0.0F, true);

        lWingTine05c = new ModelPart(this);
        lWingTine05c.setPivot(0.0F, 16.6F, 0.0F);
        lWingTine05b.addChild(lWingTine05c);
        setRotationAngle(lWingTine05c, 0.0349F, 0.0F, 0.192F);
        lWingTine05c.setTextureOffset(27, 109).addCuboid(-1.0F, 0.1F, -0.49F, 2.0F, 8.0F, 1.0F, 0.0F, true);

        lWingMembrane02 = new ModelPart(this);
        lWingMembrane02.setPivot(-2.0F, 0.0F, 0.0F);
        lWingTine05a.addChild(lWingMembrane02);
        setRotationAngle(lWingMembrane02, 0.0F, -0.2618F, 0.2182F);
        lWingMembrane02.setTextureOffset(127, 32).addCuboid(-10.2F, -0.1F, 0.2F, 16.0F, 33.0F, 0.0F, 0.0F, true);

        lWingMembrane03 = new ModelPart(this);
        lWingMembrane03.setPivot(18.0F, -0.5F, 0.0F);
        lArm02.addChild(lWingMembrane03);
        setRotationAngle(lWingMembrane03, 0.0436F, -0.6109F, 0.2182F);
        lWingMembrane03.setTextureOffset(29, 32).addCuboid(-8.0F, 0.8F, 0.2F, 16.0F, 0.0F, 32.0F, 0.0F, true);

        lHand = new ModelPart(this);
        lHand.setPivot(26.0F, -0.5F, -0.5F);
        lArm02.addChild(lHand);
        setRotationAngle(lHand, 0.8552F, -0.3491F, -1.6144F);
        lHand.setTextureOffset(83, 21).addCuboid(-2.0F, -1.0F, -4.8F, 4.0F, 2.0F, 5.0F, 0.0F, true);

        lHandClaw01 = new ModelPart(this);
        lHandClaw01.setPivot(-1.3F, -0.5F, -4.6F);
        lHand.addChild(lHandClaw01);
        setRotationAngle(lHandClaw01, 0.0873F, 0.4363F, 0.0F);
        lHandClaw01.setTextureOffset(1, 7).addCuboid(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 6.0F, 0.0F, true);

        lHandClaw02 = new ModelPart(this);
        lHandClaw02.setPivot(0.0F, -0.5F, -4.6F);
        lHand.addChild(lHandClaw02);
        setRotationAngle(lHandClaw02, 0.0873F, 0.2618F, 0.0F);
        lHandClaw02.setTextureOffset(1, 7).addCuboid(-0.5F, -0.5F, -4.5F, 1.0F, 2.0F, 6.0F, 0.0F, true);

        lHandClaw03 = new ModelPart(this);
        lHandClaw03.setPivot(1.3F, -0.5F, -4.6F);
        lHand.addChild(lHandClaw03);
        setRotationAngle(lHandClaw03, 0.0873F, 0.0873F, 0.0F);
        lHandClaw03.setTextureOffset(1, 7).addCuboid(-0.5F, -0.5F, -4.2F, 1.0F, 2.0F, 6.0F, 0.0F, true);

        lHandClaw04 = new ModelPart(this);
        lHandClaw04.setPivot(2.3F, -0.5F, -4.3F);
        lHand.addChild(lHandClaw04);
        setRotationAngle(lHandClaw04, 0.0873F, -0.1745F, 0.0F);
        lHandClaw04.setTextureOffset(1, 7).addCuboid(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 6.0F, 0.0F, true);

        lWingMembrane04 = new ModelPart(this);
        lWingMembrane04.setPivot(8.0F, 0.2F, 0.0F);
        lArm01.addChild(lWingMembrane04);
        setRotationAngle(lWingMembrane04, 1.6057F, 0.0F, 0.0F);
        lWingMembrane04.setTextureOffset(160, 32).addCuboid(-8.0F, -0.1F, 0.2F, 22.0F, 26.0F, 0.0F, 0.0F, true);

        rArm01 = new ModelPart(this);
        rArm01.setPivot(-6.5F, -4.0F, -3.0F);
        chest.addChild(rArm01);
        setRotationAngle(rArm01, 0.0F, 0.2618F, -0.4363F);
        rArm01.setTextureOffset(0, 82).addCuboid(-18.0F, -1.5F, -2.0F, 18.0F, 4.0F, 4.0F, 0.0F, false);

        rArmShoulder = new ModelPart(this);
        rArmShoulder.setPivot(-4.0F, -1.0F, 0.0F);
        rArm01.addChild(rArmShoulder);
        setRotationAngle(rArmShoulder, 0.0F, 0.0F, -0.1222F);
        rArmShoulder.setTextureOffset(0, 99).addCuboid(-3.5F, -2.0F, -2.5F, 8.0F, 4.0F, 5.0F, 0.0F, false);

        rArm02 = new ModelPart(this);
        rArm02.setPivot(-17.0F, 0.5F, 0.0F);
        rArm01.addChild(rArm02);
        setRotationAngle(rArm02, 0.0F, -0.6545F, -0.9163F);
        rArm02.setTextureOffset(0, 91).addCuboid(-28.0F, -1.5F, -1.5F, 28.0F, 3.0F, 3.0F, 0.0F, false);

        rWingTine01a = new ModelPart(this);
        rWingTine01a.setPivot(-26.6F, 0.2F, -0.1F);
        rArm02.addChild(rWingTine01a);
        setRotationAngle(rWingTine01a, 1.5708F, 0.6545F, -0.0873F);
        rWingTine01a.setTextureOffset(34, 111).addCuboid(-2.0F, -1.4F, -1.0F, 3.0F, 11.0F, 2.0F, 0.0F, false);

        rWingTine01b = new ModelPart(this);
        rWingTine01b.setPivot(-0.8F, 9.2F, 0.0F);
        rWingTine01a.addChild(rWingTine01b);
        setRotationAngle(rWingTine01b, 0.0F, 0.0F, -0.0873F);
        rWingTine01b.setTextureOffset(45, 111).addCuboid(-1.0F, 0.1F, -0.5F, 2.0F, 15.0F, 1.0F, 0.0F, false);

        rWingTine01c = new ModelPart(this);
        rWingTine01c.setPivot(0.0F, 14.6F, 0.0F);
        rWingTine01b.addChild(rWingTine01c);
        setRotationAngle(rWingTine01c, 0.0F, 0.0F, -0.192F);
        rWingTine01c.setTextureOffset(54, 111).addCuboid(-1.0F, 0.1F, -0.49F, 1.0F, 15.0F, 1.0F, 0.0F, false);

        rWingEdge01 = new ModelPart(this);
        rWingEdge01.setPivot(0.8F, 0.0F, 0.5F);
        rWingTine01a.addChild(rWingEdge01);
        setRotationAngle(rWingEdge01, 0.0F, 0.0F, -0.1047F);
        rWingEdge01.setTextureOffset(66, 82).addCuboid(-2.1F, -1.5F, -0.4F, 20.0F, 45.0F, 0.0F, 0.0F, false);

        rWingTine02a = new ModelPart(this);
        rWingTine02a.setPivot(-26.6F, 0.0F, 0.9F);
        rArm02.addChild(rWingTine02a);
        setRotationAngle(rWingTine02a, 1.5708F, 0.7854F, -0.1309F);
        rWingTine02a.setTextureOffset(34, 111).addCuboid(-2.0F, -1.4F, -1.0F, 3.0F, 11.0F, 2.0F, 0.0F, false);

        rWingTine02b = new ModelPart(this);
        rWingTine02b.setPivot(-0.8F, 9.2F, 0.0F);
        rWingTine02a.addChild(rWingTine02b);
        setRotationAngle(rWingTine02b, 0.0F, 0.0F, -0.0873F);
        rWingTine02b.setTextureOffset(45, 111).addCuboid(-1.0F, 0.1F, -0.5F, 2.0F, 15.0F, 1.0F, 0.0F, false);

        rWingTine02c = new ModelPart(this);
        rWingTine02c.setPivot(0.0F, 14.6F, 0.0F);
        rWingTine02b.addChild(rWingTine02c);
        setRotationAngle(rWingTine02c, 0.0F, 0.0F, -0.192F);
        rWingTine02c.setTextureOffset(54, 111).addCuboid(-0.6F, 0.1F, -0.49F, 1.0F, 11.0F, 1.0F, 0.0F, false);

        rWingEdge02 = new ModelPart(this);
        rWingEdge02.setPivot(0.8F, 0.0F, 0.5F);
        rWingTine02a.addChild(rWingEdge02);
        setRotationAngle(rWingEdge02, 0.0F, 0.0F, -0.1047F);
        rWingEdge02.setTextureOffset(66, 82).addCuboid(-2.1F, -1.5F, -0.5F, 20.0F, 45.0F, 0.0F, 0.0F, false);

        rWingTine03a = new ModelPart(this);
        rWingTine03a.setPivot(-26.6F, -0.3F, 0.9F);
        rArm02.addChild(rWingTine03a);
        setRotationAngle(rWingTine03a, 1.5708F, 0.9163F, -0.1745F);
        rWingTine03a.setTextureOffset(34, 111).addCuboid(-2.0F, -1.4F, -1.0F, 3.0F, 11.0F, 2.0F, 0.0F, false);

        rWingTine03b = new ModelPart(this);
        rWingTine03b.setPivot(-0.8F, 9.2F, 0.0F);
        rWingTine03a.addChild(rWingTine03b);
        setRotationAngle(rWingTine03b, 0.0F, 0.0F, -0.0873F);
        rWingTine03b.setTextureOffset(45, 111).addCuboid(-1.0F, 0.1F, -0.5F, 2.0F, 15.0F, 1.0F, 0.0F, false);

        rWingTine03c = new ModelPart(this);
        rWingTine03c.setPivot(0.0F, 14.6F, 0.0F);
        rWingTine03b.addChild(rWingTine03c);
        setRotationAngle(rWingTine03c, 0.0F, 0.0F, -0.192F);
        rWingTine03c.setTextureOffset(54, 111).addCuboid(-0.6F, 0.1F, -0.49F, 1.0F, 11.0F, 1.0F, 0.0F, false);

        rWingEdge03 = new ModelPart(this);
        rWingEdge03.setPivot(0.8F, 0.0F, 0.0F);
        rWingTine03a.addChild(rWingEdge03);
        setRotationAngle(rWingEdge03, 0.0F, 0.0F, -0.1047F);
        rWingEdge03.setTextureOffset(66, 82).addCuboid(-2.1F, -2.0F, 0.1F, 20.0F, 45.0F, 0.0F, 0.0F, false);

        rWingTine04a = new ModelPart(this);
        rWingTine04a.setPivot(-14.6F, 0.1F, 0.9F);
        rArm02.addChild(rWingTine04a);
        setRotationAngle(rWingTine04a, 1.6232F, 0.6109F, 0.0F);
        rWingTine04a.setTextureOffset(34, 111).addCuboid(-2.0F, -1.4F, -1.0F, 3.0F, 11.0F, 2.0F, 0.0F, false);

        rWingTine04b = new ModelPart(this);
        rWingTine04b.setPivot(-0.8F, 9.2F, 0.0F);
        rWingTine04a.addChild(rWingTine04b);
        setRotationAngle(rWingTine04b, 0.0F, 0.0F, -0.0873F);
        rWingTine04b.setTextureOffset(45, 111).addCuboid(-1.0F, 0.1F, -0.5F, 2.0F, 17.0F, 1.0F, 0.0F, false);

        rWingTine04c = new ModelPart(this);
        rWingTine04c.setPivot(0.0F, 16.6F, 0.0F);
        rWingTine04b.addChild(rWingTine04c);
        setRotationAngle(rWingTine04c, 0.0349F, 0.0F, -0.192F);
        rWingTine04c.setTextureOffset(27, 109).addCuboid(-1.0F, 0.1F, -0.49F, 2.0F, 8.0F, 1.0F, 0.0F, false);

        rWingMembrane01 = new ModelPart(this);
        rWingMembrane01.setPivot(2.0F, 0.0F, 0.0F);
        rWingTine04a.addChild(rWingMembrane01);
        setRotationAngle(rWingMembrane01, 0.0F, 0.0F, -0.192F);
        rWingMembrane01.setTextureOffset(94, 32).addCuboid(-5.8F, -0.1F, 0.2F, 16.0F, 33.0F, 0.0F, 0.0F, false);

        rWingTine05a = new ModelPart(this);
        rWingTine05a.setPivot(-10.6F, 0.1F, 0.9F);
        rArm02.addChild(rWingTine05a);
        setRotationAngle(rWingTine05a, 1.6057F, 0.6894F, 0.0F);
        rWingTine05a.setTextureOffset(34, 111).addCuboid(-2.0F, -1.4F, -1.0F, 3.0F, 11.0F, 2.0F, 0.0F, false);

        rWingTine05b = new ModelPart(this);
        rWingTine05b.setPivot(-0.8F, 9.2F, 0.0F);
        rWingTine05a.addChild(rWingTine05b);
        setRotationAngle(rWingTine05b, 0.0F, 0.0F, -0.0873F);
        rWingTine05b.setTextureOffset(45, 111).addCuboid(-1.0F, 0.1F, -0.5F, 2.0F, 17.0F, 1.0F, 0.0F, false);

        rWingTine05c = new ModelPart(this);
        rWingTine05c.setPivot(0.0F, 16.6F, 0.0F);
        rWingTine05b.addChild(rWingTine05c);
        setRotationAngle(rWingTine05c, 0.0349F, 0.0F, -0.192F);
        rWingTine05c.setTextureOffset(27, 109).addCuboid(-1.0F, 0.1F, -0.49F, 2.0F, 8.0F, 1.0F, 0.0F, false);

        rWingMembrane02 = new ModelPart(this);
        rWingMembrane02.setPivot(2.0F, 0.0F, 0.0F);
        rWingTine05a.addChild(rWingMembrane02);
        setRotationAngle(rWingMembrane02, 0.0F, 0.2618F, -0.2182F);
        rWingMembrane02.setTextureOffset(127, 32).addCuboid(-5.8F, -0.1F, 0.2F, 16.0F, 33.0F, 0.0F, 0.0F, false);

        rWingMembrane03 = new ModelPart(this);
        rWingMembrane03.setPivot(-18.0F, -0.5F, 0.0F);
        rArm02.addChild(rWingMembrane03);
        setRotationAngle(rWingMembrane03, 0.0436F, 0.6109F, -0.2182F);
        rWingMembrane03.setTextureOffset(29, 32).addCuboid(-8.0F, 0.8F, 0.2F, 16.0F, 0.0F, 32.0F, 0.0F, false);

        rHand = new ModelPart(this);
        rHand.setPivot(-26.0F, -0.5F, -0.5F);
        rArm02.addChild(rHand);
        setRotationAngle(rHand, 0.8552F, 0.3491F, 1.6144F);
        rHand.setTextureOffset(83, 21).addCuboid(-2.0F, -1.0F, -4.8F, 4.0F, 2.0F, 5.0F, 0.0F, false);

        rHandClaw01 = new ModelPart(this);
        rHandClaw01.setPivot(1.3F, -0.5F, -4.6F);
        rHand.addChild(rHandClaw01);
        setRotationAngle(rHandClaw01, 0.0873F, -0.4363F, 0.0F);
        rHandClaw01.setTextureOffset(1, 7).addCuboid(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 6.0F, 0.0F, false);

        rHandClaw02 = new ModelPart(this);
        rHandClaw02.setPivot(0.0F, -0.5F, -4.6F);
        rHand.addChild(rHandClaw02);
        setRotationAngle(rHandClaw02, 0.0873F, -0.2618F, 0.0F);
        rHandClaw02.setTextureOffset(1, 7).addCuboid(-0.5F, -0.5F, -4.5F, 1.0F, 2.0F, 6.0F, 0.0F, false);

        rHandClaw03 = new ModelPart(this);
        rHandClaw03.setPivot(-1.3F, -0.5F, -4.6F);
        rHand.addChild(rHandClaw03);
        setRotationAngle(rHandClaw03, 0.0873F, -0.0873F, 0.0F);
        rHandClaw03.setTextureOffset(1, 7).addCuboid(-0.5F, -0.5F, -4.2F, 1.0F, 2.0F, 6.0F, 0.0F, false);

        rHandClaw04 = new ModelPart(this);
        rHandClaw04.setPivot(-2.3F, -0.5F, -4.3F);
        rHand.addChild(rHandClaw04);
        setRotationAngle(rHandClaw04, 0.0873F, 0.1745F, 0.0F);
        rHandClaw04.setTextureOffset(1, 7).addCuboid(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 6.0F, 0.0F, false);

        rWingMembrane04 = new ModelPart(this);
        rWingMembrane04.setPivot(-8.0F, 0.2F, 0.0F);
        rArm01.addChild(rWingMembrane04);
        setRotationAngle(rWingMembrane04, 1.6057F, 0.0F, 0.0F);
        rWingMembrane04.setTextureOffset(160, 32).addCuboid(-14.0F, -0.1F, 0.2F, 22.0F, 26.0F, 0.0F, 0.0F, false);

        neck01 = new ModelPart(this);
        neck01.setPivot(0.0F, 0.0F, -7.0F);
        chest.addChild(neck01);
        setRotationAngle(neck01, -0.3491F, 0.0F, 0.0F);
        neck01.setTextureOffset(105, 0).addCuboid(-6.0F, -7.0F, -8.0F, 12.0F, 13.0F, 10.0F, 0.0F, false);

        neck02 = new ModelPart(this);
        neck02.setPivot(0.0F, -2.6F, -5.0F);
        neck01.addChild(neck02);
        setRotationAngle(neck02, 0.5236F, 0.0F, 0.0F);
        neck02.setTextureOffset(151, 0).addCuboid(-5.0F, -5.0F, -13.0F, 10.0F, 10.0F, 13.0F, 0.0F, false);

        neck03 = new ModelPart(this);
        neck03.setPivot(0.0F, 0.7F, -11.0F);
        neck02.addChild(neck03);
        setRotationAngle(neck03, -0.3491F, 0.0F, 0.0F);
        neck03.setTextureOffset(203, 0).addCuboid(-4.0F, -4.5F, -13.0F, 8.0F, 9.0F, 13.0F, 0.0F, false);

        head = new ModelPart(this);
        head.setPivot(0.0F, -0.3F, -12.0F);
        neck03.addChild(head);
        setRotationAngle(head, -0.7854F, 0.0F, 0.0F);
        head.setTextureOffset(206, 30).addCuboid(-5.0F, -4.5F, -6.0F, 10.0F, 12.0F, 10.0F, 0.0F, false);

        rLead_r1 = new ModelPart(this);
        rLead_r1.setPivot(-3.5F, 13.45F, 0.0F);
        head.addChild(rLead_r1);
        setRotationAngle(rLead_r1, -0.2182F, 0.0F, -0.0698F);
        rLead_r1.setTextureOffset(246, 54).addCuboid(-0.75F, -49.5F, -2.0F, 0.0F, 49.0F, 2.0F, 0.0F, true);

        lLead_r1 = new ModelPart(this);
        lLead_r1.setPivot(3.0F, 13.45F, 0.0F);
        head.addChild(lLead_r1);
        setRotationAngle(lLead_r1, -0.2182F, 0.0F, 0.0698F);
        lLead_r1.setTextureOffset(246, 54).addCuboid(0.75F, -49.5F, -2.0F, 0.0F, 49.0F, 2.0F, 0.0F, false);

        lEye = new ModelPart(this);
        lEye.setPivot(3.0F, 1.7F, -3.0F);
        head.addChild(lEye);
        lEye.setTextureOffset(236, 25).addCuboid(0.0F, -3.5F, -2.0F, 3.0F, 7.0F, 5.0F, 0.0F, true);

        rEye = new ModelPart(this);
        rEye.setPivot(-3.5F, 1.7F, -3.0F);
        head.addChild(rEye);
        rEye.setTextureOffset(236, 25).addCuboid(-2.5F, -3.5F, -2.0F, 3.0F, 7.0F, 5.0F, 0.0F, false);

        beakTop01 = new ModelPart(this);
        beakTop01.setPivot(0.0F, 7.0F, 0.0F);
        head.addChild(beakTop01);
        beakTop01.setTextureOffset(110, 70).addCuboid(-3.0F, 0.0F, -2.0F, 6.0F, 13.0F, 2.0F, 0.0F, false);

        beakTop02 = new ModelPart(this);
        beakTop02.setPivot(0.0F, 0.0F, -4.0F);
        beakTop01.addChild(beakTop02);
        setRotationAngle(beakTop02, 0.1745F, 0.0F, 0.0F);
        beakTop02.setTextureOffset(130, 70).addCuboid(-2.5F, 0.3F, -1.0F, 5.0F, 13.0F, 3.0F, 0.0F, false);

        lBeakFang = new ModelPart(this);
        lBeakFang.setPivot(2.6F, 10.0F, 0.0F);
        beakTop01.addChild(lBeakFang);
        lBeakFang.setTextureOffset(129, 89).addCuboid(0.0F, -6.0F, -0.3F, 0.0F, 9.0F, 4.0F, 0.0F, true);

        rBeakFang = new ModelPart(this);
        rBeakFang.setPivot(-3.1F, 10.0F, 0.0F);
        beakTop01.addChild(rBeakFang);
        rBeakFang.setTextureOffset(129, 89).addCuboid(0.0F, -6.0F, -0.3F, 0.0F, 9.0F, 4.0F, 0.0F, false);

        beakLower = new ModelPart(this);
        beakLower.setPivot(0.0F, 7.0F, 1.0F);
        head.addChild(beakLower);
        setRotationAngle(beakLower, -0.0873F, 0.0F, 0.0F);
        beakLower.setTextureOffset(110, 90).addCuboid(-2.5F, -0.1F, -1.0F, 5.0F, 13.0F, 2.0F, 0.0F, false);

        lHeadFur = new ModelPart(this);
        lHeadFur.setPivot(2.0F, 1.0F, -4.0F);
        head.addChild(lHeadFur);
        setRotationAngle(lHeadFur, -0.2618F, 0.0F, 0.0F);
        lHeadFur.setTextureOffset(150, 94).addCuboid(-1.0F, -8.0F, -6.0F, 2.0F, 12.0F, 6.0F, 0.0F, false);

        rHeadFur = new ModelPart(this);
        rHeadFur.setPivot(-2.5F, 1.0F, -4.0F);
        head.addChild(rHeadFur);
        setRotationAngle(rHeadFur, -0.2618F, 0.0F, 0.0F);
        rHeadFur.setTextureOffset(150, 94).addCuboid(-1.0F, -8.0F, -6.0F, 2.0F, 12.0F, 6.0F, 0.0F, true);

        fur01 = new ModelPart(this);
        fur01.setPivot(0.0F, -4.0F, -9.9F);
        neck03.addChild(fur01);
        setRotationAngle(fur01, 2.1817F, 0.0F, 0.0F);
        fur01.setTextureOffset(150, 70).addCuboid(-3.5F, -1.0F, 0.0F, 7.0F, 8.0F, 2.0F, 0.0F, false);

        fur02 = new ModelPart(this);
        fur02.setPivot(0.0F, -3.3F, -7.1F);
        neck03.addChild(fur02);
        setRotationAngle(fur02, 2.1817F, 0.0F, 0.0F);
        fur02.setTextureOffset(170, 70).addCuboid(-4.0F, -1.0F, 0.0F, 8.0F, 9.0F, 2.0F, 0.1F, false);

        fur03 = new ModelPart(this);
        fur03.setPivot(0.0F, -3.7F, -12.5F);
        neck02.addChild(fur03);
        setRotationAngle(fur03, 1.8326F, 0.0F, 0.0F);
        fur03.setTextureOffset(191, 70).addCuboid(-4.5F, -1.0F, 0.0F, 9.0F, 9.0F, 2.0F, 0.0F, false);

        fur04 = new ModelPart(this);
        fur04.setPivot(0.0F, -3.3F, -7.1F);
        neck02.addChild(fur04);
        setRotationAngle(fur04, 1.8326F, 0.0F, 0.0F);
        fur04.setTextureOffset(214, 70).addCuboid(-4.5F, -1.0F, 0.0F, 9.0F, 9.0F, 2.0F, 0.0F, false);

        fur05 = new ModelPart(this);
        fur05.setPivot(0.0F, -5.7F, -6.6F);
        neck01.addChild(fur05);
        setRotationAngle(fur05, 2.2689F, 0.0F, 0.0F);
        fur05.setTextureOffset(150, 83).addCuboid(-5.0F, -1.0F, 0.0F, 10.0F, 8.0F, 2.0F, 0.0F, false);

        fur06 = new ModelPart(this);
        fur06.setPivot(0.0F, -5.7F, -3.6F);
        neck01.addChild(fur06);
        setRotationAngle(fur06, 2.1817F, 0.0F, 0.0F);
        fur06.setTextureOffset(176, 83).addCuboid(-5.0F, -1.0F, 0.0F, 10.0F, 8.0F, 2.0F, 0.0F, false);

        fur07 = new ModelPart(this);
        fur07.setPivot(0.0F, -5.7F, -5.6F);
        chest.addChild(fur07);
        setRotationAngle(fur07, 1.8326F, 0.0F, 0.0F);
        fur07.setTextureOffset(203, 83).addCuboid(-6.0F, -1.0F, 0.0F, 12.0F, 10.0F, 2.0F, 0.0F, false);

        saddle = new ModelPart(this);
        saddle.setPivot(0.0F, 20.0F, 0.0F);
        chest.addChild(saddle);

        saddleBase = new ModelPart(this);
        saddleBase.setPivot(0.0F, -28.4F, 1.0F);
        saddle.addChild(saddleBase);
        saddleBase.setTextureOffset(196, 96).addCuboid(-7.0F, -1.0F, -7.5F, 14.0F, 2.0F, 13.0F, 0.0F, false);

        saddleBack = new ModelPart(this);
        saddleBack.setPivot(0.0F, -2.0F, 4.0F);
        saddleBase.addChild(saddleBack);
        saddleBack.setTextureOffset(212, 116).addCuboid(-6.0F, -1.0F, -1.5F, 12.0F, 2.0F, 3.0F, 0.0F, false);

        saddleFront = new ModelPart(this);
        saddleFront.setPivot(0.0F, -2.0F, -6.0F);
        saddleBase.addChild(saddleFront);
        saddleFront.setTextureOffset(212, 121).addCuboid(-3.0F, -1.0F, -1.5F, 6.0F, 2.0F, 3.0F, 0.0F, false);

        saddleCarpet = new ModelPart(this);
        saddleCarpet.setPivot(0.0F, -26.0F, 0.0F);
        saddle.addChild(saddleCarpet);
        saddleCarpet.setTextureOffset(152, 99).addCuboid(-7.0F, -1.5F, -7.5F, 14.0F, 12.0F, 15.0F, 0.2F, false);
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

        chest.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }
}