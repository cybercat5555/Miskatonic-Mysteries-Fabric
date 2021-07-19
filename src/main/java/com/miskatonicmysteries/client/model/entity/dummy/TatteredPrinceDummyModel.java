// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

package com.miskatonicmysteries.client.model.entity.dummy;

import com.miskatonicmysteries.common.entity.TatteredPrinceEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class TatteredPrinceDummyModel extends EntityModel<TatteredPrinceEntity> {
    private final ModelPart chest;
    private final ModelPart stomach;
    private final ModelPart lLowerTent01a;
    private final ModelPart lLowerTent01b;
    private final ModelPart lLowerTent01c;
    private final ModelPart lLowerTent01d;
    private final ModelPart lLowerTent01e;
    private final ModelPart rLowerTent01a;
    private final ModelPart rLowerTent01b;
    private final ModelPart rLowerTent01c;
    private final ModelPart rLowerTent01d;
    private final ModelPart rLowerTent01e;
    private final ModelPart fSkirt01;
    private final ModelPart fSkirt02;
    private final ModelPart lLowerTent02a;
    private final ModelPart lLowerTent02b;
    private final ModelPart lLowerTent02c;
    private final ModelPart lLowerTent02d;
    private final ModelPart lLowerTent02e;
    private final ModelPart rLowerTent02a;
    private final ModelPart rLowerTent02b;
    private final ModelPart rLowerTent02c;
    private final ModelPart rLowerTent02d;
    private final ModelPart rLowerTent02e;
    private final ModelPart lSkirt01;
    private final ModelPart lSkirt02;
    private final ModelPart rSkirt01;
    private final ModelPart rSkirt02;
    private final ModelPart lLowerTent03a;
    private final ModelPart lLowerTent03b;
    private final ModelPart lLowerTent03c;
    private final ModelPart lLowerTent03d;
    private final ModelPart lLowerTent03e;
    private final ModelPart rLowerTent03a;
    private final ModelPart rLowerTent03b;
    private final ModelPart rLowerTent03c;
    private final ModelPart rLowerTent03d;
    private final ModelPart rLowerTent03e;
    private final ModelPart LowerTent04a;
    private final ModelPart LowerTent04b;
    private final ModelPart LowerTent04c;
    private final ModelPart LowerTent04d;
    private final ModelPart LowerTent04e;
    private final ModelPart bSkirt01;
    private final ModelPart head;
    private final ModelPart hoodLTop;
    private final ModelPart hoodRTop;
    private final ModelPart hoodLSide01;
    private final ModelPart hoodRSide01;
    private final ModelPart hoodM;
    private final ModelPart hoodLSide02;
    private final ModelPart hoodRSide02;
    private final ModelPart hoodPipe01;
    private final ModelPart hoodPipe02;
    private final ModelPart lMaskPlate;
    private final ModelPart lHorn01a;
    private final ModelPart lHorn01b;
    private final ModelPart lHorn02a;
    private final ModelPart lHorn02b;
    private final ModelPart lHorn03a;
    private final ModelPart lHorn03b;
    private final ModelPart lHorn03c;
    private final ModelPart lHorn03d;
    private final ModelPart lHorn03e;
    private final ModelPart rMaskPlate;
    private final ModelPart rHorn01a;
    private final ModelPart rHorn01b;
    private final ModelPart rHorn02a;
    private final ModelPart rHorn02b;
    private final ModelPart rHorn03a;
    private final ModelPart rHorn03b;
    private final ModelPart rHorn03c;
    private final ModelPart rHorn03d;
    private final ModelPart rHorn03e;
    private final ModelPart chestCloth;
    private final ModelPart lArm01;
    private final ModelPart lArm02;
    private final ModelPart lSleeve01;
    private final ModelPart lSleeve02;
    private final ModelPart rArm01;
    private final ModelPart rArm02;
    private final ModelPart rSleeve01;
    private final ModelPart rSleeve02;
    private final ModelPart lWing01;
    private final ModelPart lWing02;
    private final ModelPart lWing03;
    private final ModelPart lWing04;
    private final ModelPart lFeathers03;
    private final ModelPart lFeathers02;
    private final ModelPart lFeathers01;
    private final ModelPart rWing01;
    private final ModelPart rWing02;
    private final ModelPart rWing03;
    private final ModelPart rWing04;
    private final ModelPart rFeathers03;
    private final ModelPart lFeathers3;
    private final ModelPart lFeathers4;

    public TatteredPrinceDummyModel() {
        textureWidth = 256;
        textureHeight = 128;
        chest = new ModelPart(this);
        chest.setPivot(0.0F, -20.1F, 0.0F);
        chest.setTextureOffset(0, 0).addCuboid(-8.0F, -9.0F, -5.5F, 16.0F, 9.0F, 11.0F, 0.0F, false);

        stomach = new ModelPart(this);
        stomach.setPivot(0.0F, 0.0F, 0.0F);
        chest.addChild(stomach);
        stomach.setTextureOffset(0, 20).addCuboid(-6.5F, 0.0F, -4.5F, 13.0F, 18.0F, 9.0F, 0.0F, false);

        lLowerTent01a = new ModelPart(this);
        lLowerTent01a.setPivot(2.8F, 17.6F, -0.5F);
        stomach.addChild(lLowerTent01a);
        setRotationAngle(lLowerTent01a, -0.1396F, -0.2269F, 0.0F);
        lLowerTent01a.setTextureOffset(68, 59).addCuboid(-3.0F, -1.0F, -3.0F, 6.0F, 17.0F, 6.0F, 0.0F, true);

        lLowerTent01b = new ModelPart(this);
        lLowerTent01b.setPivot(0.0F, 14.7F, 0.0F);
        lLowerTent01a.addChild(lLowerTent01b);
        setRotationAngle(lLowerTent01b, 1.3614F, 0.0F, 0.0F);
        lLowerTent01b.setTextureOffset(92, 59).addCuboid(-2.5F, -2.5F, -12.0F, 5.0F, 5.0F, 12.0F, 0.0F, true);

        lLowerTent01c = new ModelPart(this);
        lLowerTent01c.setPivot(0.0F, 0.0F, -11.4F);
        lLowerTent01b.addChild(lLowerTent01c);
        setRotationAngle(lLowerTent01c, -1.2392F, 0.0F, 0.0F);
        lLowerTent01c.setTextureOffset(92, 78).addCuboid(-2.0F, -2.0F, -12.0F, 4.0F, 4.0F, 12.0F, 0.0F, true);

        lLowerTent01d = new ModelPart(this);
        lLowerTent01d.setPivot(0.0F, 0.0F, -11.4F);
        lLowerTent01c.addChild(lLowerTent01d);
        setRotationAngle(lLowerTent01d, 0.0F, 0.3142F, 0.0F);
        lLowerTent01d.setTextureOffset(92, 95).addCuboid(-1.5F, -1.5F, -9.0F, 3.0F, 3.0F, 9.0F, 0.0F, true);

        lLowerTent01e = new ModelPart(this);
        lLowerTent01e.setPivot(0.0F, 0.0F, -8.7F);
        lLowerTent01d.addChild(lLowerTent01e);
        setRotationAngle(lLowerTent01e, 0.0F, -0.1745F, 0.0F);
        lLowerTent01e.setTextureOffset(109, 95).addCuboid(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 6.0F, 0.0F, true);

        rLowerTent01a = new ModelPart(this);
        rLowerTent01a.setPivot(-2.8F, 17.6F, -0.5F);
        stomach.addChild(rLowerTent01a);
        setRotationAngle(rLowerTent01a, -0.1396F, 0.2269F, 0.0F);
        rLowerTent01a.setTextureOffset(68, 59).addCuboid(-3.0F, -1.0F, -3.0F, 6.0F, 17.0F, 6.0F, 0.0F, false);

        rLowerTent01b = new ModelPart(this);
        rLowerTent01b.setPivot(0.0F, 14.7F, 0.0F);
        rLowerTent01a.addChild(rLowerTent01b);
        setRotationAngle(rLowerTent01b, 1.3614F, 0.0F, 0.0F);
        rLowerTent01b.setTextureOffset(92, 59).addCuboid(-2.5F, -2.5F, -12.0F, 5.0F, 5.0F, 12.0F, 0.0F, false);

        rLowerTent01c = new ModelPart(this);
        rLowerTent01c.setPivot(0.0F, 0.0F, -11.4F);
        rLowerTent01b.addChild(rLowerTent01c);
        setRotationAngle(rLowerTent01c, -1.2392F, 0.0F, 0.0F);
        rLowerTent01c.setTextureOffset(92, 78).addCuboid(-2.0F, -2.0F, -12.0F, 4.0F, 4.0F, 12.0F, 0.0F, false);

        rLowerTent01d = new ModelPart(this);
        rLowerTent01d.setPivot(0.0F, 0.0F, -11.4F);
        rLowerTent01c.addChild(rLowerTent01d);
        setRotationAngle(rLowerTent01d, 0.0F, -0.3142F, 0.0F);
        rLowerTent01d.setTextureOffset(92, 95).addCuboid(-1.5F, -1.5F, -9.0F, 3.0F, 3.0F, 9.0F, 0.0F, false);

        rLowerTent01e = new ModelPart(this);
        rLowerTent01e.setPivot(0.0F, 0.0F, -8.7F);
        rLowerTent01d.addChild(rLowerTent01e);
        setRotationAngle(rLowerTent01e, 0.0F, 0.1745F, 0.0F);
        rLowerTent01e.setTextureOffset(109, 95).addCuboid(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);

        fSkirt01 = new ModelPart(this);
        fSkirt01.setPivot(0.0F, 17.3F, -3.7F);
        stomach.addChild(fSkirt01);
        setRotationAngle(fSkirt01, -0.1745F, 0.0F, 0.0F);
        fSkirt01.setTextureOffset(33, 68).addCuboid(-6.5F, 0.0F, -1.0F, 13.0F, 13.0F, 2.0F, 0.0F, false);

        fSkirt02 = new ModelPart(this);
        fSkirt02.setPivot(0.0F, 12.6F, 0.0F);
        fSkirt01.addChild(fSkirt02);
        setRotationAngle(fSkirt02, -0.1222F, 0.0F, 0.0F);
        fSkirt02.setTextureOffset(32, 84).addCuboid(-6.5F, 0.0F, -1.0F, 13.0F, 10.0F, 2.0F, 0.0F, false);

        lLowerTent02a = new ModelPart(this);
        lLowerTent02a.setPivot(2.0F, 16.7F, -0.5F);
        stomach.addChild(lLowerTent02a);
        setRotationAngle(lLowerTent02a, 0.0F, 0.0F, -0.2269F);
        lLowerTent02a.setTextureOffset(68, 59).addCuboid(-3.0F, -1.0F, -3.0F, 6.0F, 17.0F, 6.0F, 0.0F, true);

        lLowerTent02b = new ModelPart(this);
        lLowerTent02b.setPivot(0.0F, 14.7F, 0.0F);
        lLowerTent02a.addChild(lLowerTent02b);
        setRotationAngle(lLowerTent02b, 1.5708F, -1.3963F, 0.0F);
        lLowerTent02b.setTextureOffset(92, 59).addCuboid(-2.5F, -2.7F, -10.8F, 5.0F, 5.0F, 12.0F, 0.0F, true);

        lLowerTent02c = new ModelPart(this);
        lLowerTent02c.setPivot(0.0F, 0.0F, -10.2F);
        lLowerTent02b.addChild(lLowerTent02c);
        setRotationAngle(lLowerTent02c, -1.1345F, 0.0873F, 0.0F);
        lLowerTent02c.setTextureOffset(92, 78).addCuboid(-2.0F, -2.0F, -12.0F, 4.0F, 4.0F, 12.0F, 0.0F, true);

        lLowerTent02d = new ModelPart(this);
        lLowerTent02d.setPivot(0.0F, 0.0F, -11.4F);
        lLowerTent02c.addChild(lLowerTent02d);
        setRotationAngle(lLowerTent02d, -0.2443F, 0.3142F, 0.0F);
        lLowerTent02d.setTextureOffset(92, 95).addCuboid(-1.5F, -1.5F, -9.0F, 3.0F, 3.0F, 9.0F, 0.0F, true);

        lLowerTent02e = new ModelPart(this);
        lLowerTent02e.setPivot(0.0F, 0.0F, -8.7F);
        lLowerTent02d.addChild(lLowerTent02e);
        setRotationAngle(lLowerTent02e, 0.0F, -0.1745F, 0.0F);
        lLowerTent02e.setTextureOffset(109, 95).addCuboid(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 6.0F, 0.0F, true);

        rLowerTent02a = new ModelPart(this);
        rLowerTent02a.setPivot(-2.0F, 16.7F, -0.5F);
        stomach.addChild(rLowerTent02a);
        setRotationAngle(rLowerTent02a, 0.0F, 0.0F, 0.2269F);
        rLowerTent02a.setTextureOffset(68, 59).addCuboid(-3.0F, -1.0F, -3.0F, 6.0F, 17.0F, 6.0F, 0.0F, false);

        rLowerTent02b = new ModelPart(this);
        rLowerTent02b.setPivot(0.0F, 14.7F, 0.0F);
        rLowerTent02a.addChild(rLowerTent02b);
        setRotationAngle(rLowerTent02b, 1.5708F, 1.3963F, 0.0F);
        rLowerTent02b.setTextureOffset(92, 59).addCuboid(-2.5F, -2.7F, -10.8F, 5.0F, 5.0F, 12.0F, 0.0F, false);

        rLowerTent02c = new ModelPart(this);
        rLowerTent02c.setPivot(0.0F, 0.0F, -10.2F);
        rLowerTent02b.addChild(rLowerTent02c);
        setRotationAngle(rLowerTent02c, -1.1345F, -0.0873F, 0.0F);
        rLowerTent02c.setTextureOffset(92, 78).addCuboid(-2.0F, -2.0F, -12.0F, 4.0F, 4.0F, 12.0F, 0.0F, false);

        rLowerTent02d = new ModelPart(this);
        rLowerTent02d.setPivot(0.0F, 0.0F, -11.4F);
        rLowerTent02c.addChild(rLowerTent02d);
        setRotationAngle(rLowerTent02d, -0.2443F, -0.3142F, 0.0F);
        rLowerTent02d.setTextureOffset(92, 95).addCuboid(-1.5F, -1.5F, -9.0F, 3.0F, 3.0F, 9.0F, 0.0F, false);

        rLowerTent02e = new ModelPart(this);
        rLowerTent02e.setPivot(0.0F, 0.0F, -8.7F);
        rLowerTent02d.addChild(rLowerTent02e);
        setRotationAngle(rLowerTent02e, 0.0F, 0.1745F, 0.0F);
        rLowerTent02e.setTextureOffset(109, 95).addCuboid(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);

        lSkirt01 = new ModelPart(this);
        lSkirt01.setPivot(5.5F, 16.9F, 0.5F);
        stomach.addChild(lSkirt01);
        setRotationAngle(lSkirt01, 0.0F, 0.0F, -0.1745F);
        lSkirt01.setTextureOffset(31, 103).addCuboid(-1.0F, 0.0F, -6.0F, 2.0F, 13.0F, 12.0F, 0.0F, false);

        lSkirt02 = new ModelPart(this);
        lSkirt02.setPivot(-0.1F, 12.8F, 0.0F);
        lSkirt01.addChild(lSkirt02);
        setRotationAngle(lSkirt02, 0.0F, 0.0F, -0.1745F);
        lSkirt02.setTextureOffset(60, 105).addCuboid(-1.0F, 0.0F, -6.0F, 2.0F, 10.0F, 12.0F, 0.0F, false);

        rSkirt01 = new ModelPart(this);
        rSkirt01.setPivot(-5.5F, 16.9F, 0.5F);
        stomach.addChild(rSkirt01);
        setRotationAngle(rSkirt01, 0.0F, 0.0F, 0.1745F);
        rSkirt01.setTextureOffset(31, 103).addCuboid(-1.0F, 0.0F, -6.0F, 2.0F, 13.0F, 12.0F, 0.0F, true);

        rSkirt02 = new ModelPart(this);
        rSkirt02.setPivot(0.1F, 12.8F, 0.0F);
        rSkirt01.addChild(rSkirt02);
        setRotationAngle(rSkirt02, 0.0F, 0.0F, 0.1745F);
        rSkirt02.setTextureOffset(60, 105).addCuboid(-1.0F, 0.0F, -6.0F, 2.0F, 10.0F, 12.0F, 0.0F, true);

        lLowerTent03a = new ModelPart(this);
        lLowerTent03a.setPivot(2.0F, 16.7F, 0.4F);
        stomach.addChild(lLowerTent03a);
        setRotationAngle(lLowerTent03a, 0.0F, -1.0472F, -0.0349F);
        lLowerTent03a.setTextureOffset(68, 59).addCuboid(-3.0F, -1.0F, -3.0F, 6.0F, 17.0F, 6.0F, 0.0F, true);

        lLowerTent03b = new ModelPart(this);
        lLowerTent03b.setPivot(0.0F, 14.7F, 0.0F);
        lLowerTent03a.addChild(lLowerTent03b);
        setRotationAngle(lLowerTent03b, 1.3614F, -1.3963F, 0.0F);
        lLowerTent03b.setTextureOffset(92, 59).addCuboid(-2.5F, -2.7F, -10.8F, 5.0F, 5.0F, 12.0F, 0.0F, true);

        lLowerTent03c = new ModelPart(this);
        lLowerTent03c.setPivot(0.0F, 0.0F, -10.2F);
        lLowerTent03b.addChild(lLowerTent03c);
        setRotationAngle(lLowerTent03c, -1.1694F, -0.0873F, 0.0F);
        lLowerTent03c.setTextureOffset(92, 78).addCuboid(-2.0F, -2.0F, -12.0F, 4.0F, 4.0F, 12.0F, 0.0F, true);

        lLowerTent03d = new ModelPart(this);
        lLowerTent03d.setPivot(0.0F, 0.0F, -11.4F);
        lLowerTent03c.addChild(lLowerTent03d);
        setRotationAngle(lLowerTent03d, -0.192F, 0.3142F, 0.0F);
        lLowerTent03d.setTextureOffset(92, 95).addCuboid(-1.5F, -1.5F, -9.0F, 3.0F, 3.0F, 9.0F, 0.0F, true);

        lLowerTent03e = new ModelPart(this);
        lLowerTent03e.setPivot(0.0F, 0.0F, -8.7F);
        lLowerTent03d.addChild(lLowerTent03e);
        setRotationAngle(lLowerTent03e, 0.0F, -0.1745F, 0.0F);
        lLowerTent03e.setTextureOffset(109, 95).addCuboid(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 6.0F, 0.0F, true);

        rLowerTent03a = new ModelPart(this);
        rLowerTent03a.setPivot(-2.0F, 16.7F, 0.4F);
        stomach.addChild(rLowerTent03a);
        setRotationAngle(rLowerTent03a, 0.0F, 1.0472F, 0.0349F);
        rLowerTent03a.setTextureOffset(68, 59).addCuboid(-3.0F, -1.0F, -3.0F, 6.0F, 17.0F, 6.0F, 0.0F, false);

        rLowerTent03b = new ModelPart(this);
        rLowerTent03b.setPivot(0.0F, 14.7F, 0.0F);
        rLowerTent03a.addChild(rLowerTent03b);
        setRotationAngle(rLowerTent03b, 1.3614F, 1.3963F, 0.0F);
        rLowerTent03b.setTextureOffset(92, 59).addCuboid(-2.5F, -2.7F, -10.8F, 5.0F, 5.0F, 12.0F, 0.0F, false);

        rLowerTent03c = new ModelPart(this);
        rLowerTent03c.setPivot(0.0F, 0.0F, -10.2F);
        rLowerTent03b.addChild(rLowerTent03c);
        setRotationAngle(rLowerTent03c, -1.1694F, 0.0873F, 0.0F);
        rLowerTent03c.setTextureOffset(92, 78).addCuboid(-2.0F, -2.0F, -12.0F, 4.0F, 4.0F, 12.0F, 0.0F, false);

        rLowerTent03d = new ModelPart(this);
        rLowerTent03d.setPivot(0.0F, 0.0F, -11.4F);
        rLowerTent03c.addChild(rLowerTent03d);
        setRotationAngle(rLowerTent03d, -0.192F, -0.3142F, 0.0F);
        rLowerTent03d.setTextureOffset(92, 95).addCuboid(-1.5F, -1.5F, -9.0F, 3.0F, 3.0F, 9.0F, 0.0F, false);

        rLowerTent03e = new ModelPart(this);
        rLowerTent03e.setPivot(0.0F, 0.0F, -8.7F);
        rLowerTent03d.addChild(rLowerTent03e);
        setRotationAngle(rLowerTent03e, 0.0F, 0.1745F, 0.0F);
        rLowerTent03e.setTextureOffset(109, 95).addCuboid(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);

        LowerTent04a = new ModelPart(this);
        LowerTent04a.setPivot(0.0F, 17.7F, 1.4F);
        stomach.addChild(LowerTent04a);
        setRotationAngle(LowerTent04a, -0.2618F, -3.1416F, 0.0F);
        LowerTent04a.setTextureOffset(68, 59).addCuboid(-3.0F, -1.0F, -3.0F, 6.0F, 17.0F, 6.0F, 0.0F, false);

        LowerTent04b = new ModelPart(this);
        LowerTent04b.setPivot(0.0F, 14.7F, 0.0F);
        LowerTent04a.addChild(LowerTent04b);
        setRotationAngle(LowerTent04b, 1.4835F, 0.0F, 0.0F);
        LowerTent04b.setTextureOffset(92, 59).addCuboid(-2.5F, -2.7F, -10.8F, 5.0F, 5.0F, 12.0F, 0.0F, false);

        LowerTent04c = new ModelPart(this);
        LowerTent04c.setPivot(0.0F, 0.0F, -10.2F);
        LowerTent04b.addChild(LowerTent04c);
        setRotationAngle(LowerTent04c, -1.0472F, 0.0F, 0.0F);
        LowerTent04c.setTextureOffset(92, 78).addCuboid(-2.0F, -2.0F, -12.0F, 4.0F, 4.0F, 12.0F, 0.0F, false);

        LowerTent04d = new ModelPart(this);
        LowerTent04d.setPivot(0.0F, 0.0F, -11.4F);
        LowerTent04c.addChild(LowerTent04d);
        setRotationAngle(LowerTent04d, -0.2269F, 0.0F, 0.0F);
        LowerTent04d.setTextureOffset(92, 95).addCuboid(-1.5F, -1.5F, -9.0F, 3.0F, 3.0F, 9.0F, 0.0F, false);

        LowerTent04e = new ModelPart(this);
        LowerTent04e.setPivot(0.0F, 0.0F, -8.7F);
        LowerTent04d.addChild(LowerTent04e);
        LowerTent04e.setTextureOffset(109, 95).addCuboid(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);

        bSkirt01 = new ModelPart(this);
        bSkirt01.setPivot(0.0F, 17.3F, 4.1F);
        stomach.addChild(bSkirt01);
        setRotationAngle(bSkirt01, 0.2967F, 0.0F, 0.0F);
        bSkirt01.setTextureOffset(166, 99).addCuboid(-6.5F, 0.0F, -1.0F, 13.0F, 13.0F, 2.0F, 0.0F, false);

        head = new ModelPart(this);
        head.setPivot(0.0F, -8.9F, 0.0F);
        chest.addChild(head);
        head.setTextureOffset(56, 0).addCuboid(-5.0F, -11.0F, -5.0F, 10.0F, 11.0F, 10.0F, 0.0F, false);

        hoodLTop = new ModelPart(this);
        hoodLTop.setPivot(3.55F, -7.0F, 0.0F);
        head.addChild(hoodLTop);
        setRotationAngle(hoodLTop, 0.0F, 0.0F, 0.3142F);
        hoodLTop.setTextureOffset(53, 23).addCuboid(-5.0F, -4.0F, -5.4F, 5.0F, 2.0F, 11.0F, 0.0F, true);

        hoodRTop = new ModelPart(this);
        hoodRTop.setPivot(-3.55F, -7.0F, 0.0F);
        head.addChild(hoodRTop);
        setRotationAngle(hoodRTop, 0.0F, 0.0F, -0.3142F);
        hoodRTop.setTextureOffset(53, 23).addCuboid(0.0F, -4.0F, -5.4F, 5.0F, 2.0F, 11.0F, 0.0F, false);

        hoodLSide01 = new ModelPart(this);
        hoodLSide01.setPivot(2.6F, -9.3F, -0.2F);
        head.addChild(hoodLSide01);
        setRotationAngle(hoodLSide01, 0.0F, 0.0F, -0.2793F);
        hoodLSide01.setTextureOffset(88, 21).addCuboid(0.0F, -0.3F, -5.29F, 2.0F, 9.0F, 10.0F, 0.0F, false);

        hoodRSide01 = new ModelPart(this);
        hoodRSide01.setPivot(-2.6F, -9.3F, -0.2F);
        head.addChild(hoodRSide01);
        setRotationAngle(hoodRSide01, 0.0F, 0.0F, 0.2793F);
        hoodRSide01.setTextureOffset(88, 21).addCuboid(-2.0F, -0.3F, -5.29F, 2.0F, 9.0F, 10.0F, 0.0F, true);

        hoodM = new ModelPart(this);
        hoodM.setPivot(0.0F, -9.4F, -4.6F);
        head.addChild(hoodM);
        hoodM.setTextureOffset(44, 0).addCuboid(-4.5F, -1.0F, -0.9F, 9.0F, 2.0F, 1.0F, 0.0F, false);

        hoodLSide02 = new ModelPart(this);
        hoodLSide02.setPivot(5.15F, -0.7F, -0.1F);
        head.addChild(hoodLSide02);
        setRotationAngle(hoodLSide02, 0.0F, 0.0F, -0.3142F);
        hoodLSide02.setTextureOffset(97, 42).addCuboid(-5.1F, -0.9F, -5.45F, 7.0F, 2.0F, 10.0F, 0.0F, false);

        hoodRSide02 = new ModelPart(this);
        hoodRSide02.setPivot(-5.15F, -0.7F, -0.1F);
        head.addChild(hoodRSide02);
        setRotationAngle(hoodRSide02, 0.0F, 0.0F, 0.3142F);
        hoodRSide02.setTextureOffset(97, 42).addCuboid(-1.9F, -0.9F, -5.45F, 7.0F, 2.0F, 10.0F, 0.0F, true);

        hoodPipe01 = new ModelPart(this);
        hoodPipe01.setPivot(0.0F, -8.6F, 4.2F);
        head.addChild(hoodPipe01);
        setRotationAngle(hoodPipe01, -0.4014F, 0.0F, 0.0F);
        hoodPipe01.setTextureOffset(98, 0).addCuboid(-3.5F, -2.5F, 0.0F, 7.0F, 7.0F, 4.0F, 0.0F, false);

        hoodPipe02 = new ModelPart(this);
        hoodPipe02.setPivot(0.0F, -0.3F, 3.1F);
        hoodPipe01.addChild(hoodPipe02);
        setRotationAngle(hoodPipe02, -0.4538F, 0.0F, 0.0F);
        hoodPipe02.setTextureOffset(108, 13).addCuboid(-2.0F, -2.0F, 0.0F, 4.0F, 5.0F, 4.0F, 0.0F, false);

        lMaskPlate = new ModelPart(this);
        lMaskPlate.setPivot(0.0F, 0.0F, 0.0F);
        head.addChild(lMaskPlate);
        setRotationAngle(lMaskPlate, 0.0F, -0.3491F, 0.0F);
        lMaskPlate.setTextureOffset(34, 57).addCuboid(-2.5F, -10.0F, -6.5F, 5.0F, 8.0F, 1.0F, 0.0F, false);

        lHorn01a = new ModelPart(this);
        lHorn01a.setPivot(1.3F, -8.5F, -6.0F);
        lMaskPlate.addChild(lHorn01a);
        setRotationAngle(lHorn01a, 0.0873F, 0.0F, 0.3491F);
        lHorn01a.setTextureOffset(0, 0).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        lHorn01b = new ModelPart(this);
        lHorn01b.setPivot(0.0F, -1.8F, 0.0F);
        lHorn01a.addChild(lHorn01b);
        setRotationAngle(lHorn01b, -0.1222F, 0.8727F, 0.2094F);
        lHorn01b.setTextureOffset(6, 0).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        lHorn02a = new ModelPart(this);
        lHorn02a.setPivot(0.2F, -9.5F, -6.0F);
        lMaskPlate.addChild(lHorn02a);
        setRotationAngle(lHorn02a, 0.0873F, 0.0F, 0.2967F);
        lHorn02a.setTextureOffset(0, 0).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        lHorn02b = new ModelPart(this);
        lHorn02b.setPivot(0.0F, -1.8F, 0.0F);
        lHorn02a.addChild(lHorn02b);
        setRotationAngle(lHorn02b, -0.1222F, 0.8727F, 0.1396F);
        lHorn02b.setTextureOffset(6, 0).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        lHorn03a = new ModelPart(this);
        lHorn03a.setPivot(-1.1F, -9.9F, -6.0F);
        lMaskPlate.addChild(lHorn03a);
        setRotationAngle(lHorn03a, 0.0F, 0.0F, 0.1396F);
        lHorn03a.setTextureOffset(0, 0).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        lHorn03b = new ModelPart(this);
        lHorn03b.setPivot(0.0F, -1.7F, 0.0F);
        lHorn03a.addChild(lHorn03b);
        setRotationAngle(lHorn03b, 0.0F, 0.0F, 0.4363F);
        lHorn03b.setTextureOffset(0, 0).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        lHorn03c = new ModelPart(this);
        lHorn03c.setPivot(0.1F, -2.7F, 0.0F);
        lHorn03b.addChild(lHorn03c);
        setRotationAngle(lHorn03c, 0.0F, 0.0F, -0.576F);
        lHorn03c.setTextureOffset(0, 0).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        lHorn03d = new ModelPart(this);
        lHorn03d.setPivot(0.1F, -1.6F, 0.0F);
        lHorn03c.addChild(lHorn03d);
        setRotationAngle(lHorn03d, 0.0F, 0.0F, -0.4363F);
        lHorn03d.setTextureOffset(0, 0).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        lHorn03e = new ModelPart(this);
        lHorn03e.setPivot(0.0F, -1.6F, 0.0F);
        lHorn03d.addChild(lHorn03e);
        setRotationAngle(lHorn03e, 0.0F, 0.0F, 0.7679F);
        lHorn03e.setTextureOffset(0, 0).addCuboid(-0.6F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        rMaskPlate = new ModelPart(this);
        rMaskPlate.setPivot(0.0F, 0.0F, 0.0F);
        head.addChild(rMaskPlate);
        setRotationAngle(rMaskPlate, 0.0F, 0.3491F, 0.0F);
        rMaskPlate.setTextureOffset(34, 57).addCuboid(-2.5F, -10.0F, -6.5F, 5.0F, 8.0F, 1.0F, 0.0F, true);

        rHorn01a = new ModelPart(this);
        rHorn01a.setPivot(-1.3F, -9.5F, -6.0F);
        rMaskPlate.addChild(rHorn01a);
        setRotationAngle(rHorn01a, 0.0873F, 0.0F, -0.4538F);
        rHorn01a.setTextureOffset(0, 0).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        rHorn01b = new ModelPart(this);
        rHorn01b.setPivot(0.0F, -1.8F, 0.0F);
        rHorn01a.addChild(rHorn01b);
        setRotationAngle(rHorn01b, -0.1222F, -0.8727F, -0.2094F);
        rHorn01b.setTextureOffset(6, 0).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        rHorn02a = new ModelPart(this);
        rHorn02a.setPivot(-0.2F, -9.8F, -6.0F);
        rMaskPlate.addChild(rHorn02a);
        setRotationAngle(rHorn02a, 0.0873F, 0.0F, -0.3491F);
        rHorn02a.setTextureOffset(0, 0).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        rHorn02b = new ModelPart(this);
        rHorn02b.setPivot(-0.1F, -2.8F, 0.0F);
        rHorn02a.addChild(rHorn02b);
        setRotationAngle(rHorn02b, -0.1222F, -0.8727F, -0.1396F);
        rHorn02b.setTextureOffset(6, 0).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        rHorn03a = new ModelPart(this);
        rHorn03a.setPivot(1.3F, -9.9F, -6.0F);
        rMaskPlate.addChild(rHorn03a);
        setRotationAngle(rHorn03a, 0.0F, 0.0F, -0.1396F);
        rHorn03a.setTextureOffset(0, 0).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        rHorn03b = new ModelPart(this);
        rHorn03b.setPivot(0.0F, -1.7F, 0.0F);
        rHorn03a.addChild(rHorn03b);
        setRotationAngle(rHorn03b, 0.0F, 0.0F, -0.4363F);
        rHorn03b.setTextureOffset(0, 0).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        rHorn03c = new ModelPart(this);
        rHorn03c.setPivot(-0.1F, -2.7F, 0.0F);
        rHorn03b.addChild(rHorn03c);
        setRotationAngle(rHorn03c, 0.0F, 0.0F, 0.576F);
        rHorn03c.setTextureOffset(0, 0).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        rHorn03d = new ModelPart(this);
        rHorn03d.setPivot(-0.1F, -2.6F, 0.0F);
        rHorn03c.addChild(rHorn03d);
        setRotationAngle(rHorn03d, 0.0F, 0.0F, 0.4363F);
        rHorn03d.setTextureOffset(0, 0).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        rHorn03e = new ModelPart(this);
        rHorn03e.setPivot(0.0F, -2.6F, 0.0F);
        rHorn03d.addChild(rHorn03e);
        setRotationAngle(rHorn03e, 0.0F, 0.0F, -0.7679F);
        rHorn03e.setTextureOffset(0, 0).addCuboid(-0.4F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        chestCloth = new ModelPart(this);
        chestCloth.setPivot(0.0F, 0.0F, 0.0F);
        chest.addChild(chestCloth);
        chestCloth.setTextureOffset(40, 39).addCuboid(-8.0F, 0.0F, -5.5F, 16.0F, 7.0F, 11.0F, 0.0F, false);

        lArm01 = new ModelPart(this);
        lArm01.setPivot(6.8F, -7.9F, 0.0F);
        chest.addChild(lArm01);
        setRotationAngle(lArm01, 0.0F, 0.0F, -0.192F);
        lArm01.setTextureOffset(0, 47).addCuboid(-3.0F, -0.5F, -4.5F, 7.0F, 15.0F, 9.0F, 0.0F, false);

        lArm02 = new ModelPart(this);
        lArm02.setPivot(0.4F, 13.0F, 0.0F);
        lArm01.addChild(lArm02);
        setRotationAngle(lArm02, 0.0F, 0.0F, 0.0524F);
        lArm02.setTextureOffset(0, 71).addCuboid(-3.0F, 0.0F, -4.0F, 6.0F, 15.0F, 8.0F, 0.0F, false);

        lSleeve01 = new ModelPart(this);
        lSleeve01.setPivot(0.0F, 12.0F, 3.8F);
        lArm02.addChild(lSleeve01);
        lSleeve01.setTextureOffset(0, 95).addCuboid(-2.9F, -3.0F, 0.2F, 6.0F, 6.0F, 11.0F, 0.0F, false);

        lSleeve02 = new ModelPart(this);
        lSleeve02.setPivot(0.0F, -2.8F, -0.2F);
        lSleeve01.addChild(lSleeve02);
        setRotationAngle(lSleeve02, -0.7854F, 0.0F, 0.0F);
        lSleeve02.setTextureOffset(0, 112).addCuboid(-2.5F, -5.8F, -4.5F, 5.0F, 6.0F, 10.0F, 0.0F, false);

        rArm01 = new ModelPart(this);
        rArm01.setPivot(-6.8F, -7.9F, 0.0F);
        chest.addChild(rArm01);
        setRotationAngle(rArm01, 0.0F, 0.0F, 0.1484F);
        rArm01.setTextureOffset(0, 47).addCuboid(-4.0F, -0.5F, -4.5F, 7.0F, 15.0F, 9.0F, 0.0F, true);

        rArm02 = new ModelPart(this);
        rArm02.setPivot(-0.4F, 13.0F, 0.0F);
        rArm01.addChild(rArm02);
        setRotationAngle(rArm02, 0.0F, 0.0F, 0.0087F);
        rArm02.setTextureOffset(0, 71).addCuboid(-3.0F, 0.0F, -4.0F, 6.0F, 15.0F, 8.0F, 0.0F, true);

        rSleeve01 = new ModelPart(this);
        rSleeve01.setPivot(0.0F, 12.0F, 3.8F);
        rArm02.addChild(rSleeve01);
        rSleeve01.setTextureOffset(0, 95).addCuboid(-3.1F, -3.0F, 0.2F, 6.0F, 6.0F, 11.0F, 0.0F, true);

        rSleeve02 = new ModelPart(this);
        rSleeve02.setPivot(0.0F, -2.8F, -0.2F);
        rSleeve01.addChild(rSleeve02);
        setRotationAngle(rSleeve02, -0.7854F, 0.0F, 0.0F);
        rSleeve02.setTextureOffset(0, 112).addCuboid(-2.5F, -5.8F, -4.5F, 5.0F, 6.0F, 10.0F, 0.0F, true);

        lWing01 = new ModelPart(this);
        lWing01.setPivot(1.8F, -5.9F, 4.7F);
        chest.addChild(lWing01);
        setRotationAngle(lWing01, 0.3491F, 0.6458F, 0.0F);
        lWing01.setTextureOffset(143, 49).addCuboid(-1.0F, -1.5F, 0.0F, 2.0F, 4.0F, 8.0F, 0.0F, true);

        lWing02 = new ModelPart(this);
        lWing02.setPivot(0.1F, 0.2F, 7.5F);
        lWing01.addChild(lWing02);
        setRotationAngle(lWing02, 0.0873F, 0.1396F, 0.0F);
        lWing02.setTextureOffset(143, 62).addCuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 9.0F, 0.0F, true);

        lWing03 = new ModelPart(this);
        lWing03.setPivot(0.1F, 0.4F, 8.0F);
        lWing02.addChild(lWing03);
        setRotationAngle(lWing03, 0.3812F, 0.0F, 0.0F);
        lWing03.setTextureOffset(148, 74).addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 11.0F, 2.0F, 0.0F, true);

        lWing04 = new ModelPart(this);
        lWing04.setPivot(0.0F, 10.9F, 0.2F);
        lWing03.addChild(lWing04);
        setRotationAngle(lWing04, -0.4712F, 0.0F, 0.0F);
        lWing04.setTextureOffset(151, 90).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 11.0F, 1.0F, 0.0F, true);

        lFeathers03 = new ModelPart(this);
        lFeathers03.setPivot(0.0F, 2.0F, 0.0F);
        lWing04.addChild(lFeathers03);
        lFeathers03.setTextureOffset(138, 0).addCuboid(0.0F, -2.3F, -15.9F, 0.0F, 30.0F, 16.0F, 0.0F, true);

        lFeathers02 = new ModelPart(this);
        lFeathers02.setPivot(-0.1F, 2.3F, 0.0F);
        lWing03.addChild(lFeathers02);
        setRotationAngle(lFeathers02, -0.4363F, 0.0F, 0.0F);
        lFeathers02.setTextureOffset(171, 13).addCuboid(-0.5F, -0.6F, -19.1F, 1.0F, 20.0F, 22.0F, 0.0F, true);

        lFeathers01 = new ModelPart(this);
        lFeathers01.setPivot(0.2F, 0.0F, 0.0F);
        lWing02.addChild(lFeathers01);
        setRotationAngle(lFeathers01, -0.1745F, 0.0F, 0.0F);
        lFeathers01.setTextureOffset(187, 58).addCuboid(-0.5F, -0.6F, -11.6F, 1.0F, 16.0F, 23.0F, 0.0F, true);

        rWing01 = new ModelPart(this);
        rWing01.setPivot(-1.8F, -5.9F, 4.7F);
        chest.addChild(rWing01);
        setRotationAngle(rWing01, 0.3491F, -0.6458F, 0.0F);
        rWing01.setTextureOffset(143, 49).addCuboid(-1.0F, -1.5F, 0.0F, 2.0F, 4.0F, 8.0F, 0.0F, false);

        rWing02 = new ModelPart(this);
        rWing02.setPivot(-0.1F, 0.2F, 7.5F);
        rWing01.addChild(rWing02);
        setRotationAngle(rWing02, 0.0873F, -0.1396F, 0.0F);
        rWing02.setTextureOffset(143, 62).addCuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 9.0F, 0.0F, false);

        rWing03 = new ModelPart(this);
        rWing03.setPivot(-0.1F, 0.4F, 8.0F);
        rWing02.addChild(rWing03);
        setRotationAngle(rWing03, 0.3812F, 0.0F, 0.0F);
        rWing03.setTextureOffset(148, 74).addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 11.0F, 2.0F, 0.0F, false);

        rWing04 = new ModelPart(this);
        rWing04.setPivot(0.0F, 10.9F, 0.2F);
        rWing03.addChild(rWing04);
        setRotationAngle(rWing04, -0.4712F, 0.0F, 0.0F);
        rWing04.setTextureOffset(151, 90).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 11.0F, 1.0F, 0.0F, false);

        rFeathers03 = new ModelPart(this);
        rFeathers03.setPivot(0.0F, 2.0F, 0.0F);
        rWing04.addChild(rFeathers03);
        rFeathers03.setTextureOffset(138, 0).addCuboid(0.0F, -2.3F, -15.9F, 0.0F, 30.0F, 16.0F, 0.0F, false);

        lFeathers3 = new ModelPart(this);
        lFeathers3.setPivot(0.1F, 2.3F, 0.0F);
        rWing03.addChild(lFeathers3);
        setRotationAngle(lFeathers3, -0.4363F, 0.0F, 0.0F);
        lFeathers3.setTextureOffset(171, 13).addCuboid(-0.5F, -0.6F, -19.1F, 1.0F, 20.0F, 22.0F, 0.0F, false);

        lFeathers4 = new ModelPart(this);
        lFeathers4.setPivot(-0.2F, 0.0F, 0.0F);
        rWing02.addChild(lFeathers4);
        setRotationAngle(lFeathers4, -0.1745F, 0.0F, 0.0F);
        lFeathers4.setTextureOffset(187, 58).addCuboid(-0.5F, -0.6F, -11.6F, 1.0F, 16.0F, 23.0F, 0.0F, false);
    }

    @Override
    public void setAngles(TatteredPrinceEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //previously the render function, render code was moved to a method below
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        chest.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }

}