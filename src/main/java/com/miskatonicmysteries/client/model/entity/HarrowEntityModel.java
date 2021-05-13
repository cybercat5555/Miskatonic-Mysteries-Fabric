// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

package com.miskatonicmysteries.client.model.entity;

import com.miskatonicmysteries.common.entity.HarrowEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class HarrowEntityModel extends EntityModel<HarrowEntity> {
    private final ModelPart main;
    private final ModelPart jaws;
    private final ModelPart smallUpperFang02_r1;
    private final ModelPart smallUpperFang01_r1;
    private final ModelPart lowerJaw;
    private final ModelPart smallLowerFang02_r1;
    private final ModelPart Box_r1;
    private final ModelPart core;
    private final ModelPart Box_r2;
    private final ModelPart tentacles;
    private final ModelPart mSmallTentacle07_r1;
    private final ModelPart mSmallTentacle06_r1;
    private final ModelPart mSmallTentacle05_r1;
    private final ModelPart mSmallTentacle03_r1;
    private final ModelPart rSmallTentacle02_r1;
    private final ModelPart rSmallTentacle02_r2;
    private final ModelPart lSmallTentacle02_r1;
    private final ModelPart lSmallTentacle01_r1;
    private final ModelPart lLargeTentacle01;
    private final ModelPart lLargeTentacle02;
    private final ModelPart lLargeTentacle03;
    private final ModelPart rLargeTentacle01;
    private final ModelPart rLargeTentacle02;
    private final ModelPart rLargeTentacle03;
    private final ModelPart mLargeTentacle01;
    private final ModelPart mLargeTentacle01b;
    private final ModelPart mLargeTentacle01c;

    public HarrowEntityModel() {
    	super((identifier) -> RenderLayer.getEntityTranslucent(identifier, false));
        textureWidth = 32;
        textureHeight = 16;
        main = new ModelPart(this);
        main.setPivot(0.0F, 24.0F, 0.0F);
        main.setTextureOffset(0, 0).addCuboid(-2.0F, -11.25F, -3.0F, 4.0F, 3.0F, 3.0F, 0.0F, false);

        jaws = new ModelPart(this);
        jaws.setPivot(0.0F, -9.0F, -1.5F);
        main.addChild(jaws);
        jaws.setTextureOffset(0, 6).addCuboid(-2.0F, -2.0F, -3.0F, 4.0F, 2.0F, 3.0F, -0.1F, false);

        smallUpperFang02_r1 = new ModelPart(this);
        smallUpperFang02_r1.setPivot(0.25F, -1.0F, -3.0F);
        jaws.addChild(smallUpperFang02_r1);
        setRotationAngle(smallUpperFang02_r1, -0.1745F, 0.0F, 0.0F);
        smallUpperFang02_r1.setTextureOffset(12, 6).addCuboid(-0.25F, -0.25F, -0.15F, 1.0F, 1.0F, 1.0F, -0.2F, false);

        smallUpperFang01_r1 = new ModelPart(this);
        smallUpperFang01_r1.setPivot(-0.75F, -1.0F, -3.0F);
        jaws.addChild(smallUpperFang01_r1);
        setRotationAngle(smallUpperFang01_r1, -0.1309F, 0.0F, 0.0F);
        smallUpperFang01_r1.setTextureOffset(12, 6).addCuboid(-0.25F, -0.25F, -0.15F, 1.0F, 1.0F, 1.0F, -0.2F, false);

        lowerJaw = new ModelPart(this);
        lowerJaw.setPivot(0.0F, 0.0F, 0.0F);
        jaws.addChild(lowerJaw);


        smallLowerFang02_r1 = new ModelPart(this);
        smallLowerFang02_r1.setPivot(0.5F, 1.0F, -2.5F);
        lowerJaw.addChild(smallLowerFang02_r1);
        setRotationAngle(smallLowerFang02_r1, 0.1745F, 0.0F, 0.0F);
        smallLowerFang02_r1.setTextureOffset(12, 6).addCuboid(-0.5F, -0.75F, -0.25F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        smallLowerFang02_r1.setTextureOffset(12, 6).addCuboid(-1.5F, -0.75F, -0.25F, 1.0F, 1.0F, 1.0F, -0.2F, false);

        Box_r1 = new ModelPart(this);
        Box_r1.setPivot(0.0F, 0.0F, 0.0F);
        lowerJaw.addChild(Box_r1);
        setRotationAngle(Box_r1, 0.3491F, 0.0F, 0.0F);
        Box_r1.setTextureOffset(0, 11).addCuboid(-2.0F, -1.0F, -3.0F, 4.0F, 2.0F, 3.0F, -0.25F, false);

        core = new ModelPart(this);
        core.setPivot(0.0F, -9.5F, -1.5F);
        main.addChild(core);


        Box_r2 = new ModelPart(this);
        Box_r2.setPivot(0.0F, 0.0F, 0.0F);
        core.addChild(Box_r2);
        setRotationAngle(Box_r2, -0.7854F, -0.7854F, 0.7854F);
        Box_r2.setTextureOffset(18, 1).addCuboid(-1.5F, -1.8F, -1.5F, 3.0F, 3.0F, 3.0F, -0.5F, false);

        tentacles = new ModelPart(this);
        tentacles.setPivot(0.0F, -10.0F, 0.0F);
        main.addChild(tentacles);
        tentacles.setTextureOffset(15, 10).addCuboid(1.75F, -0.25F, 0.0F, 0.0F, 1.0F, 4.0F, 0.0F, false);
        tentacles.setTextureOffset(15, 10).addCuboid(-1.75F, -0.25F, 0.0F, 0.0F, 1.0F, 4.0F, 0.0F, true);
        tentacles.setTextureOffset(22, 7).addCuboid(-0.5F, -1.25F, -0.25F, 1.0F, 0.0F, 4.0F, 0.0F, true);
        tentacles.setTextureOffset(15, 10).addCuboid(-0.5F, 1.7F, -0.25F, 1.0F, 0.0F, 4.0F, 0.0F, true);

        mSmallTentacle07_r1 = new ModelPart(this);
        mSmallTentacle07_r1.setPivot(-2.0F, -0.25F, -1.0F);
        tentacles.addChild(mSmallTentacle07_r1);
        setRotationAngle(mSmallTentacle07_r1, 0.1745F, 0.0F, -0.829F);
        mSmallTentacle07_r1.setTextureOffset(15, 10).addCuboid(-0.5F, 0.0F, -0.25F, 1.0F, 0.0F, 4.0F, 0.0F, false);

        mSmallTentacle06_r1 = new ModelPart(this);
        mSmallTentacle06_r1.setPivot(2.0F, -0.25F, -1.0F);
        tentacles.addChild(mSmallTentacle06_r1);
        setRotationAngle(mSmallTentacle06_r1, 0.1745F, 0.0F, 0.829F);
        mSmallTentacle06_r1.setTextureOffset(15, 10).addCuboid(-0.5F, 0.0F, -0.25F, 1.0F, 0.0F, 4.0F, 0.0F, true);

        mSmallTentacle05_r1 = new ModelPart(this);
        mSmallTentacle05_r1.setPivot(-1.0F, -1.25F, -1.0F);
        tentacles.addChild(mSmallTentacle05_r1);
        setRotationAngle(mSmallTentacle05_r1, 0.1745F, 0.0F, 0.0F);
        mSmallTentacle05_r1.setTextureOffset(15, 10).addCuboid(-0.5F, -0.05F, -0.5F, 1.0F, 0.0F, 4.0F, 0.0F, false);
        mSmallTentacle05_r1.setTextureOffset(15, 10).addCuboid(1.5F, -0.05F, -0.5F, 1.0F, 0.0F, 4.0F, 0.0F, true);

        mSmallTentacle03_r1 = new ModelPart(this);
        mSmallTentacle03_r1.setPivot(0.0F, -1.25F, -2.0F);
        tentacles.addChild(mSmallTentacle03_r1);
        setRotationAngle(mSmallTentacle03_r1, 0.2182F, 0.0F, 0.0F);
        mSmallTentacle03_r1.setTextureOffset(15, 10).addCuboid(-0.5F, 0.0F, -0.25F, 1.0F, 0.0F, 4.0F, 0.0F, true);

        rSmallTentacle02_r1 = new ModelPart(this);
        rSmallTentacle02_r1.setPivot(-1.0F, 1.5F, 0.0F);
        tentacles.addChild(rSmallTentacle02_r1);
        setRotationAngle(rSmallTentacle02_r1, 0.0F, 0.0F, 0.5672F);
        rSmallTentacle02_r1.setTextureOffset(22, 7).addCuboid(-0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 4.0F, 0.0F, true);

        rSmallTentacle02_r2 = new ModelPart(this);
        rSmallTentacle02_r2.setPivot(-1.0F, -1.0F, 0.0F);
        tentacles.addChild(rSmallTentacle02_r2);
        setRotationAngle(rSmallTentacle02_r2, 0.0F, 0.0F, -0.5672F);
        rSmallTentacle02_r2.setTextureOffset(22, 7).addCuboid(-0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 4.0F, 0.0F, true);

        lSmallTentacle02_r1 = new ModelPart(this);
        lSmallTentacle02_r1.setPivot(1.0F, 1.5F, 0.0F);
        tentacles.addChild(lSmallTentacle02_r1);
        setRotationAngle(lSmallTentacle02_r1, 0.0F, 0.0F, -0.5672F);
        lSmallTentacle02_r1.setTextureOffset(22, 7).addCuboid(-0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 4.0F, 0.0F, false);

        lSmallTentacle01_r1 = new ModelPart(this);
        lSmallTentacle01_r1.setPivot(1.0F, -1.0F, 0.0F);
        tentacles.addChild(lSmallTentacle01_r1);
        setRotationAngle(lSmallTentacle01_r1, 0.0F, 0.0F, 0.5672F);
        lSmallTentacle01_r1.setTextureOffset(22, 7).addCuboid(-0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 4.0F, 0.0F, false);

        lLargeTentacle01 = new ModelPart(this);
        lLargeTentacle01.setPivot(1.05F, 0.2F, -0.3F);
        tentacles.addChild(lLargeTentacle01);
        setRotationAngle(lLargeTentacle01, 1.4835F, 0.0F, -0.1222F);
        lLargeTentacle01.setTextureOffset(14, 3).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        lLargeTentacle02 = new ModelPart(this);
        lLargeTentacle02.setPivot(0.0F, 1.8F, 0.0F);
        lLargeTentacle01.addChild(lLargeTentacle02);
        setRotationAngle(lLargeTentacle02, 0.0873F, 0.0F, 0.0F);
        lLargeTentacle02.setTextureOffset(14, 1).addCuboid(-0.51F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, -0.1F, true);

        lLargeTentacle03 = new ModelPart(this);
        lLargeTentacle03.setPivot(0.0F, 1.55F, 0.0F);
        lLargeTentacle02.addChild(lLargeTentacle03);
        setRotationAngle(lLargeTentacle03, -0.3491F, 0.0F, 0.0F);
        lLargeTentacle03.setTextureOffset(14, 0).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, -0.2F, true);

        rLargeTentacle01 = new ModelPart(this);
        rLargeTentacle01.setPivot(-1.05F, 0.2F, -0.3F);
        tentacles.addChild(rLargeTentacle01);
        setRotationAngle(rLargeTentacle01, 1.4835F, 0.0F, 0.1222F);
        rLargeTentacle01.setTextureOffset(14, 3).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        rLargeTentacle02 = new ModelPart(this);
        rLargeTentacle02.setPivot(0.0F, 1.8F, 0.0F);
        rLargeTentacle01.addChild(rLargeTentacle02);
        setRotationAngle(rLargeTentacle02, 0.0873F, 0.0F, 0.0F);
        rLargeTentacle02.setTextureOffset(14, 1).addCuboid(-0.49F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, -0.1F, false);

        rLargeTentacle03 = new ModelPart(this);
        rLargeTentacle03.setPivot(0.0F, 1.55F, 0.0F);
        rLargeTentacle02.addChild(rLargeTentacle03);
        setRotationAngle(rLargeTentacle03, -0.3491F, 0.0F, 0.0F);
        rLargeTentacle03.setTextureOffset(14, 0).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, -0.2F, false);

        mLargeTentacle01 = new ModelPart(this);
        mLargeTentacle01.setPivot(-0.05F, -0.05F, -0.3F);
        tentacles.addChild(mLargeTentacle01);
        setRotationAngle(mLargeTentacle01, 1.2654F, 0.0F, 0.0F);
        mLargeTentacle01.setTextureOffset(14, 2).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        mLargeTentacle01b = new ModelPart(this);
        mLargeTentacle01b.setPivot(0.0F, 2.8F, 0.0F);
        mLargeTentacle01.addChild(mLargeTentacle01b);
        setRotationAngle(mLargeTentacle01b, 0.3491F, 0.0F, 0.0F);
        mLargeTentacle01b.setTextureOffset(14, 1).addCuboid(-0.49F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, -0.1F, false);

        mLargeTentacle01c = new ModelPart(this);
        mLargeTentacle01c.setPivot(0.0F, 1.55F, 0.0F);
        mLargeTentacle01b.addChild(mLargeTentacle01c);
        setRotationAngle(mLargeTentacle01c, -0.3491F, 0.0F, 0.0F);
        mLargeTentacle01c.setTextureOffset(14, 0).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, -0.2F, false);
    }

    @Override
    public void setAngles(HarrowEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.main.yaw = netHeadYaw * 0.017453292F;
        this.main.pitch = headPitch * 0.017453292F;

        boolean charging = entity.isCharging();
        float pitch;
        if (charging){
            pitch = (float) Math.sin(ageInTicks * (Math.PI / 5F)) * 0.25F;
            this.jaws.pitch = (float) -Math.sin(ageInTicks * (Math.PI / 3F)) * 0.15F ;
            this.lowerJaw.pitch = (float) Math.sin(ageInTicks * (Math.PI / 3F)) * 0.3F ;
            this.lLargeTentacle01.pitch = 1.4835F;
            animateTentacle(lLargeTentacle01, lLargeTentacle02, lLargeTentacle03, 1.4835F, 0.0873F, -0.1491F, ageInTicks, 10F, 0.15F);
            animateTentacle(rLargeTentacle01, rLargeTentacle02, rLargeTentacle03, 1.4835F, 0.0873F, -0.1491F, ageInTicks, 10F, 0.15F);
            animateTentacle(mLargeTentacle01, mLargeTentacle01b, mLargeTentacle01c, 1.2654F, 0.3491F, -0.1491F, ageInTicks, 10F, -0.15F);
        }else{
            pitch = (float) Math.sin(ageInTicks * (Math.PI / 20F)) * 0.05F + 0.1F;
            this.jaws.pitch = 0;
            this.lowerJaw.pitch = (float) Math.sin(ageInTicks * (Math.PI / 15F)) * 0.05F - 0.1F ;
            animateTentacle(lLargeTentacle01, lLargeTentacle02, lLargeTentacle03, 1.4835F, 0.0873F, -0.1491F, ageInTicks, 25F, 0.05F);
            animateTentacle(rLargeTentacle01, rLargeTentacle02, rLargeTentacle03, 1.4835F, 0.0873F, -0.1491F, ageInTicks, 25F, 0.05F);
            animateTentacle(mLargeTentacle01, mLargeTentacle01b, mLargeTentacle01c, 1.2654F, 0.3491F, -0.1491F, ageInTicks, 25F, -0.05F);
        }
        this.lSmallTentacle02_r1.pitch = pitch;
        this.rSmallTentacle02_r1.pitch = pitch;
        this.lSmallTentacle01_r1.pitch = -pitch;
        this.rSmallTentacle02_r2.pitch = -pitch;

        this.mSmallTentacle07_r1.pitch = pitch;
        this.mSmallTentacle06_r1.pitch = pitch;
        this.mSmallTentacle05_r1.pitch = pitch;
        this.mSmallTentacle03_r1.pitch = pitch;
     //   }else{
     //  }
        //previously the render function, render code was moved to a method below
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        System.out.println(alpha);
        main.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }

    private void animateTentacle(ModelPart segment1, ModelPart segment2, ModelPart segment3, float base1, float base2, float base3, float runVar, float speedFac, float distanceMult){
        float sin = (float) Math.sin(runVar * (Math.PI / speedFac));
        segment1.pitch = base1 + sin * distanceMult;
        segment2.pitch = base2 - sin * distanceMult * 2;
        segment3.pitch = base3 - sin * distanceMult * 4;

    }
}