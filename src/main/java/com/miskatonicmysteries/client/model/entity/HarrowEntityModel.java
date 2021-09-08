// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

package com.miskatonicmysteries.client.model.entity;

import com.miskatonicmysteries.common.feature.entity.HarrowEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class HarrowEntityModel extends EntityModel<HarrowEntity> {
    private final ModelPart root, main;
    private final ModelPart jaws, lowerJaw;
    private final ModelPart lLargeTentacle01, lLargeTentacle02, lLargeTentacle03;
    private final ModelPart rLargeTentacle01, rLargeTentacle02, rLargeTentacle03;
    private final ModelPart mLargeTentacle01, mLargeTentacle01b, mLargeTentacle01c;
    private final ModelPart lSmallTentacle01_r1, lSmallTentacle02_r1;
    private final ModelPart rSmallTentacle02_r1, rSmallTentacle02_r2;
    private final ModelPart mSmallTentacle07_r1, mSmallTentacle06_r1, mSmallTentacle05_r1, mSmallTentacle03_r1;

    public HarrowEntityModel(ModelPart root) {
        super((identifier) -> RenderLayer.getEntityTranslucent(identifier, false));
        this.root = root;
        this.main = root.getChild("main");
        this.jaws = main.getChild("jaws");
        this.lowerJaw = jaws.getChild("lowerJaw");
        ModelPart tentacles = main.getChild("tentacles");
        this.lLargeTentacle01 = tentacles.getChild("lLargeTentacle01");
        this.lLargeTentacle02 = lLargeTentacle01.getChild("lLargeTentacle02");
        this.lLargeTentacle03 = lLargeTentacle02.getChild("lLargeTentacle03");
        this.rLargeTentacle01 = tentacles.getChild("rLargeTentacle01");
        this.rLargeTentacle02 = rLargeTentacle01.getChild("rLargeTentacle02");
        this.rLargeTentacle03 = rLargeTentacle02.getChild("rLargeTentacle03");
        this.mLargeTentacle01 = tentacles.getChild("mLargeTentacle01");
        this.mLargeTentacle01b = mLargeTentacle01.getChild("mLargeTentacle01b");
        this.mLargeTentacle01c = mLargeTentacle01b.getChild("mLargeTentacle01c");

        this.lSmallTentacle02_r1 = tentacles.getChild("lSmallTentacle02_r1");
        this.lSmallTentacle01_r1 = tentacles.getChild("lSmallTentacle01_r1");
        this.rSmallTentacle02_r1 = tentacles.getChild("rSmallTentacle02_r1");
        this.rSmallTentacle02_r2 = tentacles.getChild("rSmallTentacle02_r2");

        this.mSmallTentacle03_r1 = tentacles.getChild("mSmallTentacle03_r1");
        this.mSmallTentacle05_r1 = tentacles.getChild("mSmallTentacle05_r1");
        this.mSmallTentacle06_r1 = tentacles.getChild("mSmallTentacle06_r1");
        this.mSmallTentacle07_r1 = tentacles.getChild("mSmallTentacle07_r1");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();
        ModelPartData main = root.addChild("main",
                ModelPartBuilder.create()
                        .cuboid(-2.0F, -11.25F, -3.0F, 4.0F, 3.0F, 3.0F),
                ModelTransform.of(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        ModelPartData jaws = main.addChild("jaws",
                ModelPartBuilder.create()
                        .uv(0, 6).cuboid(-2.0F, -2.0F, -3.0F, 4.0F, 2.0F, 3.0F, new Dilation(-0.1F, -0.1F, -0.1F)),
                ModelTransform.of(0.0F, -9.0F, -1.5F, 0.0F, 0.0F, 0.0F));
        jaws.addChild("smallUpperFang02_r1",
                ModelPartBuilder.create()
                        .uv(12, 6).cuboid(-0.25F, -0.25F, -0.15F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F, -0.2F, -0.2F)),
                ModelTransform.of(0.25F, -1.0F, -3.0F, -0.1745F, 0.0F, 0.0F));
        jaws.addChild("smallUpperFang01_r1",
                ModelPartBuilder.create()
                        .uv(12, 6).cuboid(-0.25F, -0.25F, -0.15F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F, -0.2F, -0.2F)),
                ModelTransform.of(-0.75F, -1.0F, -3.0F, -0.1309F, 0.0F, 0.0F));
        ModelPartData lowerJaw = jaws.addChild("lowerJaw",
                ModelPartBuilder.create(),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        lowerJaw.addChild("smallLowerFang02_r1",
                ModelPartBuilder.create()
                        .uv(12, 6).cuboid(-0.5F, -0.75F, -0.25F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F, -0.2F, -0.2F))
                        .cuboid(-1.5F, -0.75F, -0.25F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F, -0.2F, -0.2F)),
                ModelTransform.of(0.5F, 1.0F, -2.5F, 0.1745F, 0.0F, 0.0F));
        lowerJaw.addChild("Box_r1",
                ModelPartBuilder.create()
                        .uv(0, 11).cuboid(-2.0F, -1.0F, -3.0F, 4.0F, 2.0F, 3.0F, new Dilation(-0.25F, -0.25F, -0.25F)),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.3491F, 0.0F, 0.0F));
        ModelPartData core = main.addChild("core",
                ModelPartBuilder.create(),
                ModelTransform.of(0.0F, -9.5F, -1.5F, 0.0F, 0.0F, 0.0F));
        core.addChild("Box_r2",
                ModelPartBuilder.create()
                        .uv(18, 1).cuboid(-1.5F, -1.8F, -1.5F, 3.0F, 3.0F, 3.0F, new Dilation(-0.5F, -0.5F, -0.5F)),
                ModelTransform.of(0.0F, 0.0F, 0.0F, -0.7854F, -0.7854F, 0.7854F));
        ModelPartData tentacles = main.addChild("tentacles",
                ModelPartBuilder.create()
                        .uv(15, 10).cuboid(1.75F, -0.25F, 0.0F, 0.0F, 1.0F, 4.0F)
                        .cuboid(-1.75F, -0.25F, 0.0F, 0.0F, 1.0F, 4.0F)
                        .uv(22, 7).mirrored(true).cuboid(-0.5F, -1.25F, -0.25F, 1.0F, 0.0F, 4.0F)
                        .uv(15, 10).cuboid(-0.5F, 1.7F, -0.25F, 1.0F, 0.0F, 4.0F),
                ModelTransform.of(0.0F, -10.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        tentacles.addChild("mSmallTentacle07_r1",
                ModelPartBuilder.create()
                        .uv(15, 10).cuboid(-0.5F, 0.0F, -0.25F, 1.0F, 0.0F, 4.0F),
                ModelTransform.of(-2.0F, -0.25F, -1.0F, 0.1745F, 0.0F, -0.829F));
        tentacles.addChild("mSmallTentacle06_r1",
                ModelPartBuilder.create()
                        .uv(15, 10).cuboid(-0.5F, 0.0F, -0.25F, 1.0F, 0.0F, 4.0F),
                ModelTransform.of(2.0F, -0.25F, -1.0F, 0.1745F, 0.0F, 0.829F));
        tentacles.addChild("mSmallTentacle05_r1",
                ModelPartBuilder.create()
                        .uv(15, 10).cuboid(-0.5F, -0.05F, -0.5F, 1.0F, 0.0F, 4.0F)
                        .cuboid(1.5F, -0.05F, -0.5F, 1.0F, 0.0F, 4.0F),
                ModelTransform.of(-1.0F, -1.25F, -1.0F, 0.1745F, 0.0F, 0.0F));
        tentacles.addChild("mSmallTentacle03_r1",
                ModelPartBuilder.create()
                        .uv(15, 10).cuboid(-0.5F, 0.0F, -0.25F, 1.0F, 0.0F, 4.0F),
                ModelTransform.of(0.0F, -1.25F, -2.0F, 0.2182F, 0.0F, 0.0F));
        tentacles.addChild("rSmallTentacle02_r1",
                ModelPartBuilder.create()
                        .uv(22, 7).cuboid(-0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 4.0F),
                ModelTransform.of(-1.0F, 1.5F, 0.0F, 0.0F, 0.0F, 0.5672F));
        tentacles.addChild("rSmallTentacle02_r2",
                ModelPartBuilder.create()
                        .uv(22, 7).cuboid(-0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 4.0F),
                ModelTransform.of(-1.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.5672F));
        tentacles.addChild("lSmallTentacle02_r1",
                ModelPartBuilder.create()
                        .uv(22, 7).cuboid(-0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 4.0F),
                ModelTransform.of(1.0F, 1.5F, 0.0F, 0.0F, 0.0F, -0.5672F));
        tentacles.addChild("lSmallTentacle01_r1",
                ModelPartBuilder.create()
                        .uv(22, 7).cuboid(-0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 4.0F),
                ModelTransform.of(1.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.5672F));
        ModelPartData lLargeTentacle01 = tentacles.addChild("lLargeTentacle01",
                ModelPartBuilder.create()
                        .uv(14, 3).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(1.05F, 0.2F, -0.3F, 1.4835F, 0.0F, -0.1222F));
        ModelPartData lLargeTentacle02 = lLargeTentacle01.addChild("lLargeTentacle02",
                ModelPartBuilder.create()
                        .uv(14, 1).cuboid(-0.51F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(-0.1F, -0.1F, -0.1F)),
                ModelTransform.of(0.0F, 1.8F, 0.0F, 0.0873F, 0.0F, 0.0F));
        lLargeTentacle02.addChild("lLargeTentacle03",
                ModelPartBuilder.create()
                        .uv(14, 0).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(-0.2F, -0.2F, -0.2F)),
                ModelTransform.of(0.0F, 1.55F, 0.0F, -0.3491F, 0.0F, 0.0F));
        ModelPartData rLargeTentacle01 = tentacles.addChild("rLargeTentacle01",
                ModelPartBuilder.create()
                        .uv(14, 3).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(-1.05F, 0.2F, -0.3F, 1.4835F, 0.0F, 0.1222F));
        ModelPartData rLargeTentacle02 = rLargeTentacle01.addChild("rLargeTentacle02",
                ModelPartBuilder.create()
                        .uv(14, 1).cuboid(-0.49F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(-0.1F, -0.1F, -0.1F)),
                ModelTransform.of(0.0F, 1.8F, 0.0F, 0.0873F, 0.0F, 0.0F));
        rLargeTentacle02.addChild("rLargeTentacle03",
                ModelPartBuilder.create()
                        .uv(14, 0).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(-0.2F, -0.2F, -0.2F)),
                ModelTransform.of(0.0F, 1.55F, 0.0F, -0.3491F, 0.0F, 0.0F));
        ModelPartData mLargeTentacle01 = tentacles.addChild("mLargeTentacle01",
                ModelPartBuilder.create()
                        .uv(14, 2).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                ModelTransform.of(-0.05F, -0.05F, -0.3F, 1.2654F, 0.0F, 0.0F));
        ModelPartData mLargeTentacle01b = mLargeTentacle01.addChild("mLargeTentacle01b",
                ModelPartBuilder.create()
                        .uv(14, 1).cuboid(-0.49F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(-0.1F, -0.1F, -0.1F)),
                ModelTransform.of(0.0F, 2.8F, 0.0F, 0.3491F, 0.0F, 0.0F));
        mLargeTentacle01b.addChild("mLargeTentacle01c",
                ModelPartBuilder.create()
                        .uv(14, 0).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(-0.2F, -0.2F, -0.2F)),
                ModelTransform.of(0.0F, 1.55F, 0.0F, -0.3491F, 0.0F, 0.0F));

        return TexturedModelData.of(data, 32, 16);
    }

    @Override
    public void setAngles(HarrowEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.main.yaw = netHeadYaw * 0.017453292F;
        this.main.pitch = headPitch * 0.017453292F;

        boolean charging = entity.isCharging();
        float pitch;
        if (charging) {
            pitch = (float) Math.sin(ageInTicks * (Math.PI / 5F)) * 0.25F;
            this.jaws.pitch = (float) -Math.sin(ageInTicks * (Math.PI / 3F)) * 0.15F;
            this.lowerJaw.pitch = (float) Math.sin(ageInTicks * (Math.PI / 3F)) * 0.3F;
            this.lLargeTentacle01.pitch = 1.4835F;
            animateTentacle(lLargeTentacle01, lLargeTentacle02, lLargeTentacle03, 1.4835F, 0.0873F, -0.1491F, ageInTicks, 10F, 0.15F);
            animateTentacle(rLargeTentacle01, rLargeTentacle02, rLargeTentacle03, 1.4835F, 0.0873F, -0.1491F, ageInTicks, 10F, 0.15F);
            animateTentacle(mLargeTentacle01, mLargeTentacle01b, mLargeTentacle01c, 1.2654F, 0.3491F, -0.1491F, ageInTicks, 10F, -0.15F);
        } else {
            pitch = (float) Math.sin(ageInTicks * (Math.PI / 20F)) * 0.05F + 0.1F;
            this.jaws.pitch = 0;
            this.lowerJaw.pitch = (float) Math.sin(ageInTicks * (Math.PI / 15F)) * 0.05F - 0.1F;
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
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }

    private void animateTentacle(ModelPart segment1, ModelPart segment2, ModelPart segment3, float base1, float base2, float base3, float runVar, float speedFac, float distanceMult) {
        float sin = (float) Math.sin(runVar * (Math.PI / speedFac));
        segment1.pitch = base1 + sin * distanceMult;
        segment2.pitch = base2 - sin * distanceMult * 2;
        segment3.pitch = base3 - sin * distanceMult * 4;

    }
}