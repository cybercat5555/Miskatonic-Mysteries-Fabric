// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

package com.miskatonicmysteries.client.model.entity;

import net.minecraft.client.model.ModelPart;

public class AscendedHasturCultistEntityModel extends HasturCultistEntityModel {
    public AscendedHasturCultistEntityModel() {
        super();
        ModelPart clothMask_r1 = new ModelPart(this);
        clothMask_r1.setPivot(0.0F, -11.0F, -4.25F);
        head.addChild(clothMask_r1);
        setRotationAngle(clothMask_r1, -0.0873F, 0.0F, 0.0F);
        clothMask_r1.setTextureOffset(48, 1).addCuboid(-4.5F, 2.0F, 0.0F, 9.0F, 14.0F, 1.0F, 0.0F, false);

        ModelPart crown = new ModelPart(this);
        crown.setPivot(0.0F, 0.0F, 0.0F);
        head.addChild(crown);
        crown.setTextureOffset(73, 2).addCuboid(-4.5F, -9.25F, -5.0F, 9.0F, 1.0F, 10.0F, 0.1F, false);

        ModelPart lAntler = new ModelPart(this);
        lAntler.setPivot(0.0F, 0.0F, 0.0F);
        crown.addChild(lAntler);


        ModelPart lAntler09_r1 = new ModelPart(this);
        lAntler09_r1.setPivot(4.75F, -15.5F, -4.75F);
        lAntler.addChild(lAntler09_r1);
        setRotationAngle(lAntler09_r1, 0.0F, 0.0F, -0.6981F);
        lAntler09_r1.setTextureOffset(79, 13).addCuboid(-1.0F, -3.0F, -0.25F, 1.0F, 3.0F, 1.0F, -0.1F, false);

        ModelPart lAntler08_r1 = new ModelPart(this);
        lAntler08_r1.setPivot(3.0F, -11.25F, -4.75F);
        lAntler.addChild(lAntler08_r1);
        setRotationAngle(lAntler08_r1, 0.0F, 0.0F, -0.3927F);
        lAntler08_r1.setTextureOffset(73, 16).addCuboid(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, -0.1F, false);

        ModelPart lAntler07_r1 = new ModelPart(this);
        lAntler07_r1.setPivot(2.75F, -9.25F, -5.0F);
        lAntler.addChild(lAntler07_r1);
        setRotationAngle(lAntler07_r1, 0.0F, 0.0F, 0.1309F);
        lAntler07_r1.setTextureOffset(73, 15).addCuboid(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelPart lAntler06_r1 = new ModelPart(this);
        lAntler06_r1.setPivot(6.75F, -11.75F, -5.0F);
        lAntler.addChild(lAntler06_r1);
        setRotationAngle(lAntler06_r1, 0.0F, 0.0F, 0.829F);
        lAntler06_r1.setTextureOffset(77, 15).addCuboid(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, -0.1F, false);

        ModelPart lAntler05_r1 = new ModelPart(this);
        lAntler05_r1.setPivot(5.0F, -11.0F, -5.0F);
        lAntler.addChild(lAntler05_r1);
        setRotationAngle(lAntler05_r1, 0.0F, 0.0F, 1.1781F);
        lAntler05_r1.setTextureOffset(78, 15).addCuboid(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelPart lAntler04_r1 = new ModelPart(this);
        lAntler04_r1.setPivot(5.25F, -14.0F, -5.0F);
        lAntler.addChild(lAntler04_r1);
        setRotationAngle(lAntler04_r1, 0.0F, 0.0F, -0.3491F);
        lAntler04_r1.setTextureOffset(76, 14).addCuboid(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelPart lAntler03_r1 = new ModelPart(this);
        lAntler03_r1.setPivot(5.25F, -12.0F, -5.0F);
        lAntler.addChild(lAntler03_r1);
        setRotationAngle(lAntler03_r1, 0.0F, 0.0F, -0.0436F);
        lAntler03_r1.setTextureOffset(73, 15).addCuboid(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.1F, false);

        ModelPart lAntler02_r1 = new ModelPart(this);
        lAntler02_r1.setPivot(4.25F, -10.0F, -5.0F);
        lAntler.addChild(lAntler02_r1);
        setRotationAngle(lAntler02_r1, 0.0F, 0.0F, 0.4363F);
        lAntler02_r1.setTextureOffset(78, 15).addCuboid(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.15F, false);

        ModelPart lAntler01_r1 = new ModelPart(this);
        lAntler01_r1.setPivot(2.75F, -8.5F, -5.0F);
        lAntler.addChild(lAntler01_r1);
        setRotationAngle(lAntler01_r1, 0.0F, 0.0F, 0.829F);
        lAntler01_r1.setTextureOffset(73, 15).addCuboid(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.2F, false);

        ModelPart rAntler = new ModelPart(this);
        rAntler.setPivot(0.0F, 0.0F, 0.0F);
        crown.addChild(rAntler);


        ModelPart rAntler09_r1 = new ModelPart(this);
        rAntler09_r1.setPivot(-4.75F, -15.5F, -4.75F);
        rAntler.addChild(rAntler09_r1);
        setRotationAngle(rAntler09_r1, 0.0F, 0.0F, 0.6981F);
        rAntler09_r1.setTextureOffset(79, 13).addCuboid(0.0F, -3.0F, -0.25F, 1.0F, 3.0F, 1.0F, -0.1F, true);

        ModelPart rAntler08_r1 = new ModelPart(this);
        rAntler08_r1.setPivot(-3.0F, -11.25F, -4.75F);
        rAntler.addChild(rAntler08_r1);
        setRotationAngle(rAntler08_r1, 0.0F, 0.0F, 0.3927F);
        rAntler08_r1.setTextureOffset(73, 16).addCuboid(0.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, -0.1F, true);

        ModelPart rAntler07_r1 = new ModelPart(this);
        rAntler07_r1.setPivot(-2.75F, -9.25F, -5.0F);
        rAntler.addChild(rAntler07_r1);
        setRotationAngle(rAntler07_r1, 0.0F, 0.0F, -0.1309F);
        rAntler07_r1.setTextureOffset(73, 15).addCuboid(0.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        ModelPart rAntler06_r1 = new ModelPart(this);
        rAntler06_r1.setPivot(-6.75F, -11.75F, -5.0F);
        rAntler.addChild(rAntler06_r1);
        setRotationAngle(rAntler06_r1, 0.0F, 0.0F, -0.829F);
        rAntler06_r1.setTextureOffset(77, 15).addCuboid(0.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, -0.1F, true);

        ModelPart rAntler05_r1 = new ModelPart(this);
        rAntler05_r1.setPivot(-5.0F, -11.0F, -5.0F);
        rAntler.addChild(rAntler05_r1);
        setRotationAngle(rAntler05_r1, 0.0F, 0.0F, -1.1781F);
        rAntler05_r1.setTextureOffset(78, 15).addCuboid(0.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        ModelPart rAntler04_r1 = new ModelPart(this);
        rAntler04_r1.setPivot(-5.25F, -14.0F, -5.0F);
        rAntler.addChild(rAntler04_r1);
        setRotationAngle(rAntler04_r1, 0.0F, 0.0F, 0.3491F);
        rAntler04_r1.setTextureOffset(76, 14).addCuboid(0.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        ModelPart rAntler03_r1 = new ModelPart(this);
        rAntler03_r1.setPivot(-5.25F, -12.0F, -5.0F);
        rAntler.addChild(rAntler03_r1);
        setRotationAngle(rAntler03_r1, 0.0F, 0.0F, 0.0436F);
        rAntler03_r1.setTextureOffset(73, 15).addCuboid(0.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.1F, true);

        ModelPart rAntler02_r1 = new ModelPart(this);
        rAntler02_r1.setPivot(-4.25F, -10.0F, -5.0F);
        rAntler.addChild(rAntler02_r1);
        setRotationAngle(rAntler02_r1, 0.0F, 0.0F, -0.4363F);
        rAntler02_r1.setTextureOffset(78, 15).addCuboid(0.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.15F, true);

        ModelPart rAntler01_r1 = new ModelPart(this);
        rAntler01_r1.setPivot(-2.75F, -8.5F, -5.0F);
        rAntler.addChild(rAntler01_r1);
        setRotationAngle(rAntler01_r1, 0.0F, 0.0F, -0.829F);
        rAntler01_r1.setTextureOffset(73, 15).addCuboid(0.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.2F, true);

        ModelPart lArmSleeveSlope_r1 = new ModelPart(this);
        lArmSleeveSlope_r1.setPivot(2.0F, 6.75F, 2.75F);
        leftArm.addChild(lArmSleeveSlope_r1);
        setRotationAngle(lArmSleeveSlope_r1, -0.5236F, 0.0F, 0.0F);
        lArmSleeveSlope_r1.setTextureOffset(49, 37).addCuboid(-3.0F, -2.0F, -2.0F, 4.0F, 2.0F, 4.0F, 0.1F, false);

        ModelPart rArmSleeveSlope_r1 = new ModelPart(this);
        rArmSleeveSlope_r1.setPivot(-2.0F, 6.75F, 2.75F);
        rightArm.addChild(rArmSleeveSlope_r1);
        setRotationAngle(rArmSleeveSlope_r1, -0.5236F, 0.0F, 0.0F);
        rArmSleeveSlope_r1.setTextureOffset(49, 37).addCuboid(-1.0F, -2.0F, -2.0F, 4.0F, 2.0F, 4.0F, 0.1F, true);

        leftArm.setTextureOffset(46, 23).addCuboid(-1.0F, 6.0F, -2.0F, 4.0F, 2.0F, 8.0F, 0.15F, false);
        rightArm.setTextureOffset(46, 23).addCuboid(-3.0F, 6.0F, -2.0F, 4.0F, 2.0F, 8.0F, 0.15F, true);

    }


    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }

}