// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

package com.miskatonicmysteries.client.model.entity.dummy;

import com.miskatonicmysteries.common.entity.TatteredPrinceEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class TatteredPrinceDummyModel extends EntityModel<TatteredPrinceEntity> {
    private final ModelPart chest;

    public TatteredPrinceDummyModel(ModelPart root) {
        super(RenderLayer::getEntityCutout);
        this.chest = root.getChild("chest");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();
        ModelPartData chest = root.addChild("chest",
                ModelPartBuilder.create()
                        .cuboid(-8.0F, -9.0F, -5.5F, 16.0F, 9.0F, 11.0F),
                ModelTransform.of(0.0F, -20.1F, 0.0F, 0.0F, 0.0F, 0.0F));
        ModelPartData stomach = chest.addChild("stomach",
                ModelPartBuilder.create()
                        .uv(0, 20).cuboid(-6.5F, 0.0F, -4.5F, 13.0F, 18.0F, 9.0F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        ModelPartData lLowerTent01a = stomach.addChild("lLowerTent01a",
                ModelPartBuilder.create()
                        .uv(68, 59).cuboid(-3.0F, -1.0F, -3.0F, 6.0F, 17.0F, 6.0F),
                ModelTransform.of(2.8F, 17.6F, -0.5F, -0.1396F, -0.2269F, 0.0F));
        ModelPartData lLowerTent01b = lLowerTent01a.addChild("lLowerTent01b",
                ModelPartBuilder.create()
                        .uv(92, 59).cuboid(-2.5F, -2.5F, -12.0F, 5.0F, 5.0F, 12.0F),
                ModelTransform.of(0.0F, 14.7F, 0.0F, 1.3614F, 0.0F, 0.0F));
        ModelPartData lLowerTent01c = lLowerTent01b.addChild("lLowerTent01c",
                ModelPartBuilder.create()
                        .uv(92, 78).cuboid(-2.0F, -2.0F, -12.0F, 4.0F, 4.0F, 12.0F),
                ModelTransform.of(0.0F, 0.0F, -11.4F, -1.2392F, 0.0F, 0.0F));
        ModelPartData lLowerTent01d = lLowerTent01c.addChild("lLowerTent01d",
                ModelPartBuilder.create()
                        .uv(92, 95).cuboid(-1.5F, -1.5F, -9.0F, 3.0F, 3.0F, 9.0F),
                ModelTransform.of(0.0F, 0.0F, -11.4F, 0.0F, 0.3142F, 0.0F));
        lLowerTent01d.addChild("lLowerTent01e",
                ModelPartBuilder.create()
                        .uv(109, 95).cuboid(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 6.0F),
                ModelTransform.of(0.0F, 0.0F, -8.7F, 0.0F, -0.1745F, 0.0F));
        ModelPartData rLowerTent01a = stomach.addChild("rLowerTent01a",
                ModelPartBuilder.create()
                        .uv(68, 59).cuboid(-3.0F, -1.0F, -3.0F, 6.0F, 17.0F, 6.0F),
                ModelTransform.of(-2.8F, 17.6F, -0.5F, -0.1396F, 0.2269F, 0.0F));
        ModelPartData rLowerTent01b = rLowerTent01a.addChild("rLowerTent01b",
                ModelPartBuilder.create()
                        .uv(92, 59).cuboid(-2.5F, -2.5F, -12.0F, 5.0F, 5.0F, 12.0F),
                ModelTransform.of(0.0F, 14.7F, 0.0F, 1.3614F, 0.0F, 0.0F));
        ModelPartData rLowerTent01c = rLowerTent01b.addChild("rLowerTent01c",
                ModelPartBuilder.create()
                        .uv(92, 78).cuboid(-2.0F, -2.0F, -12.0F, 4.0F, 4.0F, 12.0F),
                ModelTransform.of(0.0F, 0.0F, -11.4F, -1.2392F, 0.0F, 0.0F));
        ModelPartData rLowerTent01d = rLowerTent01c.addChild("rLowerTent01d",
                ModelPartBuilder.create()
                        .uv(92, 95).cuboid(-1.5F, -1.5F, -9.0F, 3.0F, 3.0F, 9.0F),
                ModelTransform.of(0.0F, 0.0F, -11.4F, 0.0F, -0.3142F, 0.0F));
        rLowerTent01d.addChild("rLowerTent01e",
                ModelPartBuilder.create()
                        .uv(109, 95).cuboid(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 6.0F),
                ModelTransform.of(0.0F, 0.0F, -8.7F, 0.0F, 0.1745F, 0.0F));
        ModelPartData fSkirt01 = stomach.addChild("fSkirt01",
                ModelPartBuilder.create()
                        .uv(33, 68).cuboid(-6.5F, 0.0F, -1.0F, 13.0F, 13.0F, 2.0F),
                ModelTransform.of(0.0F, 17.3F, -3.7F, -0.1745F, 0.0F, 0.0F));
        fSkirt01.addChild("fSkirt02",
                ModelPartBuilder.create()
                        .uv(32, 84).cuboid(-6.5F, 0.0F, -1.0F, 13.0F, 10.0F, 2.0F),
                ModelTransform.of(0.0F, 12.6F, 0.0F, -0.1222F, 0.0F, 0.0F));
        ModelPartData lLowerTent02a = stomach.addChild("lLowerTent02a",
                ModelPartBuilder.create()
                        .uv(68, 59).cuboid(-3.0F, -1.0F, -3.0F, 6.0F, 17.0F, 6.0F),
                ModelTransform.of(2.0F, 16.7F, -0.5F, 0.0F, 0.0F, -0.2269F));
        ModelPartData lLowerTent02b = lLowerTent02a.addChild("lLowerTent02b",
                ModelPartBuilder.create()
                        .uv(92, 59).cuboid(-2.5F, -2.7F, -10.8F, 5.0F, 5.0F, 12.0F),
                ModelTransform.of(0.0F, 14.7F, 0.0F, 1.5708F, -1.3963F, 0.0F));
        ModelPartData lLowerTent02c = lLowerTent02b.addChild("lLowerTent02c",
                ModelPartBuilder.create()
                        .uv(92, 78).cuboid(-2.0F, -2.0F, -12.0F, 4.0F, 4.0F, 12.0F),
                ModelTransform.of(0.0F, 0.0F, -10.2F, -1.1345F, 0.0873F, 0.0F));
        ModelPartData lLowerTent02d = lLowerTent02c.addChild("lLowerTent02d",
                ModelPartBuilder.create()
                        .uv(92, 95).cuboid(-1.5F, -1.5F, -9.0F, 3.0F, 3.0F, 9.0F),
                ModelTransform.of(0.0F, 0.0F, -11.4F, -0.2443F, 0.3142F, 0.0F));
        lLowerTent02d.addChild("lLowerTent02e",
                ModelPartBuilder.create()
                        .uv(109, 95).cuboid(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 6.0F),
                ModelTransform.of(0.0F, 0.0F, -8.7F, 0.0F, -0.1745F, 0.0F));
        ModelPartData rLowerTent02a = stomach.addChild("rLowerTent02a",
                ModelPartBuilder.create()
                        .uv(68, 59).cuboid(-3.0F, -1.0F, -3.0F, 6.0F, 17.0F, 6.0F),
                ModelTransform.of(-2.0F, 16.7F, -0.5F, 0.0F, 0.0F, 0.2269F));
        ModelPartData rLowerTent02b = rLowerTent02a.addChild("rLowerTent02b",
                ModelPartBuilder.create()
                        .uv(92, 59).cuboid(-2.5F, -2.7F, -10.8F, 5.0F, 5.0F, 12.0F),
                ModelTransform.of(0.0F, 14.7F, 0.0F, 1.5708F, 1.3963F, 0.0F));
        ModelPartData rLowerTent02c = rLowerTent02b.addChild("rLowerTent02c",
                ModelPartBuilder.create()
                        .uv(92, 78).cuboid(-2.0F, -2.0F, -12.0F, 4.0F, 4.0F, 12.0F),
                ModelTransform.of(0.0F, 0.0F, -10.2F, -1.1345F, -0.0873F, 0.0F));
        ModelPartData rLowerTent02d = rLowerTent02c.addChild("rLowerTent02d",
                ModelPartBuilder.create()
                        .uv(92, 95).cuboid(-1.5F, -1.5F, -9.0F, 3.0F, 3.0F, 9.0F),
                ModelTransform.of(0.0F, 0.0F, -11.4F, -0.2443F, -0.3142F, 0.0F));
        rLowerTent02d.addChild("rLowerTent02e",
                ModelPartBuilder.create()
                        .uv(109, 95).cuboid(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 6.0F),
                ModelTransform.of(0.0F, 0.0F, -8.7F, 0.0F, 0.1745F, 0.0F));
        ModelPartData lSkirt01 = stomach.addChild("lSkirt01",
                ModelPartBuilder.create()
                        .uv(31, 103).cuboid(-1.0F, 0.0F, -6.0F, 2.0F, 13.0F, 12.0F),
                ModelTransform.of(5.5F, 16.9F, 0.5F, 0.0F, 0.0F, -0.1745F));
        lSkirt01.addChild("lSkirt02",
                ModelPartBuilder.create()
                        .uv(60, 105).cuboid(-1.0F, 0.0F, -6.0F, 2.0F, 10.0F, 12.0F),
                ModelTransform.of(-0.1F, 12.8F, 0.0F, 0.0F, 0.0F, -0.1745F));
        ModelPartData rSkirt01 = stomach.addChild("rSkirt01",
                ModelPartBuilder.create()
                        .uv(31, 103).cuboid(-1.0F, 0.0F, -6.0F, 2.0F, 13.0F, 12.0F),
                ModelTransform.of(-5.5F, 16.9F, 0.5F, 0.0F, 0.0F, 0.1745F));
        rSkirt01.addChild("rSkirt02",
                ModelPartBuilder.create()
                        .uv(60, 105).cuboid(-1.0F, 0.0F, -6.0F, 2.0F, 10.0F, 12.0F),
                ModelTransform.of(0.1F, 12.8F, 0.0F, 0.0F, 0.0F, 0.1745F));
        ModelPartData lLowerTent03a = stomach.addChild("lLowerTent03a",
                ModelPartBuilder.create()
                        .uv(68, 59).cuboid(-3.0F, -1.0F, -3.0F, 6.0F, 17.0F, 6.0F),
                ModelTransform.of(2.0F, 16.7F, 0.4F, 0.0F, -1.0472F, -0.0349F));
        ModelPartData lLowerTent03b = lLowerTent03a.addChild("lLowerTent03b",
                ModelPartBuilder.create()
                        .uv(92, 59).cuboid(-2.5F, -2.7F, -10.8F, 5.0F, 5.0F, 12.0F),
                ModelTransform.of(0.0F, 14.7F, 0.0F, 1.3614F, -1.3963F, 0.0F));
        ModelPartData lLowerTent03c = lLowerTent03b.addChild("lLowerTent03c",
                ModelPartBuilder.create()
                        .uv(92, 78).cuboid(-2.0F, -2.0F, -12.0F, 4.0F, 4.0F, 12.0F),
                ModelTransform.of(0.0F, 0.0F, -10.2F, -1.1694F, -0.0873F, 0.0F));
        ModelPartData lLowerTent03d = lLowerTent03c.addChild("lLowerTent03d",
                ModelPartBuilder.create()
                        .uv(92, 95).cuboid(-1.5F, -1.5F, -9.0F, 3.0F, 3.0F, 9.0F),
                ModelTransform.of(0.0F, 0.0F, -11.4F, -0.192F, 0.3142F, 0.0F));
        lLowerTent03d.addChild("lLowerTent03e",
                ModelPartBuilder.create()
                        .uv(109, 95).cuboid(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 6.0F),
                ModelTransform.of(0.0F, 0.0F, -8.7F, 0.0F, -0.1745F, 0.0F));
        ModelPartData rLowerTent03a = stomach.addChild("rLowerTent03a",
                ModelPartBuilder.create()
                        .uv(68, 59).cuboid(-3.0F, -1.0F, -3.0F, 6.0F, 17.0F, 6.0F),
                ModelTransform.of(-2.0F, 16.7F, 0.4F, 0.0F, 1.0472F, 0.0349F));
        ModelPartData rLowerTent03b = rLowerTent03a.addChild("rLowerTent03b",
                ModelPartBuilder.create()
                        .uv(92, 59).cuboid(-2.5F, -2.7F, -10.8F, 5.0F, 5.0F, 12.0F),
                ModelTransform.of(0.0F, 14.7F, 0.0F, 1.3614F, 1.3963F, 0.0F));
        ModelPartData rLowerTent03c = rLowerTent03b.addChild("rLowerTent03c",
                ModelPartBuilder.create()
                        .uv(92, 78).cuboid(-2.0F, -2.0F, -12.0F, 4.0F, 4.0F, 12.0F),
                ModelTransform.of(0.0F, 0.0F, -10.2F, -1.1694F, 0.0873F, 0.0F));
        ModelPartData rLowerTent03d = rLowerTent03c.addChild("rLowerTent03d",
                ModelPartBuilder.create()
                        .uv(92, 95).cuboid(-1.5F, -1.5F, -9.0F, 3.0F, 3.0F, 9.0F),
                ModelTransform.of(0.0F, 0.0F, -11.4F, -0.192F, -0.3142F, 0.0F));
        rLowerTent03d.addChild("rLowerTent03e",
                ModelPartBuilder.create()
                        .uv(109, 95).cuboid(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 6.0F),
                ModelTransform.of(0.0F, 0.0F, -8.7F, 0.0F, 0.1745F, 0.0F));
        ModelPartData LowerTent04a = stomach.addChild("LowerTent04a",
                ModelPartBuilder.create()
                        .uv(68, 59).cuboid(-3.0F, -1.0F, -3.0F, 6.0F, 17.0F, 6.0F),
                ModelTransform.of(0.0F, 17.7F, 1.4F, -0.2618F, -3.1416F, 0.0F));
        ModelPartData LowerTent04b = LowerTent04a.addChild("LowerTent04b",
                ModelPartBuilder.create()
                        .uv(92, 59).cuboid(-2.5F, -2.7F, -10.8F, 5.0F, 5.0F, 12.0F),
                ModelTransform.of(0.0F, 14.7F, 0.0F, 1.4835F, 0.0F, 0.0F));
        ModelPartData LowerTent04c = LowerTent04b.addChild("LowerTent04c",
                ModelPartBuilder.create()
                        .uv(92, 78).cuboid(-2.0F, -2.0F, -12.0F, 4.0F, 4.0F, 12.0F),
                ModelTransform.of(0.0F, 0.0F, -10.2F, -1.0472F, 0.0F, 0.0F));
        ModelPartData LowerTent04d = LowerTent04c.addChild("LowerTent04d",
                ModelPartBuilder.create()
                        .uv(92, 95).cuboid(-1.5F, -1.5F, -9.0F, 3.0F, 3.0F, 9.0F),
                ModelTransform.of(0.0F, 0.0F, -11.4F, -0.2269F, 0.0F, 0.0F));
        LowerTent04d.addChild("LowerTent04e",
                ModelPartBuilder.create()
                        .uv(109, 95).cuboid(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 6.0F),
                ModelTransform.of(0.0F, 0.0F, -8.7F, 0.0F, 0.0F, 0.0F));
        stomach.addChild("bSkirt01",
                ModelPartBuilder.create()
                        .uv(166, 99).cuboid(-6.5F, 0.0F, -1.0F, 13.0F, 13.0F, 2.0F),
                ModelTransform.of(0.0F, 17.3F, 4.1F, 0.2967F, 0.0F, 0.0F));
        ModelPartData head = chest.addChild("head",
                ModelPartBuilder.create()
                        .uv(56, 0).cuboid(-5.0F, -11.0F, -5.0F, 10.0F, 11.0F, 10.0F),
                ModelTransform.of(0.0F, -8.9F, 0.0F, 0.0F, 0.0F, 0.0F));
        head.addChild("hoodLTop",
                ModelPartBuilder.create()
                        .uv(53, 23).cuboid(-5.0F, -4.0F, -5.4F, 5.0F, 2.0F, 11.0F),
                ModelTransform.of(3.55F, -7.0F, 0.0F, 0.0F, 0.0F, 0.3142F));
        head.addChild("hoodRTop",
                ModelPartBuilder.create()
                        .uv(53, 23).cuboid(0.0F, -4.0F, -5.4F, 5.0F, 2.0F, 11.0F),
                ModelTransform.of(-3.55F, -7.0F, 0.0F, 0.0F, 0.0F, -0.3142F));
        head.addChild("hoodLSide01",
                ModelPartBuilder.create()
                        .uv(88, 21).cuboid(0.0F, -0.3F, -5.29F, 2.0F, 9.0F, 10.0F),
                ModelTransform.of(2.6F, -9.3F, -0.2F, 0.0F, 0.0F, -0.2793F));
        head.addChild("hoodRSide01",
                ModelPartBuilder.create()
                        .uv(88, 21).cuboid(-2.0F, -0.3F, -5.29F, 2.0F, 9.0F, 10.0F),
                ModelTransform.of(-2.6F, -9.3F, -0.2F, 0.0F, 0.0F, 0.2793F));
        head.addChild("hoodM",
                ModelPartBuilder.create()
                        .uv(44, 0).cuboid(-4.5F, -1.0F, -0.9F, 9.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, -9.4F, -4.6F, 0.0F, 0.0F, 0.0F));
        head.addChild("hoodLSide02",
                ModelPartBuilder.create()
                        .uv(97, 42).cuboid(-5.1F, -0.9F, -5.45F, 7.0F, 2.0F, 10.0F),
                ModelTransform.of(5.15F, -0.7F, -0.1F, 0.0F, 0.0F, -0.3142F));
        head.addChild("hoodRSide02",
                ModelPartBuilder.create()
                        .uv(97, 42).cuboid(-1.9F, -0.9F, -5.45F, 7.0F, 2.0F, 10.0F),
                ModelTransform.of(-5.15F, -0.7F, -0.1F, 0.0F, 0.0F, 0.3142F));
        ModelPartData hoodPipe01 = head.addChild("hoodPipe01",
                ModelPartBuilder.create()
                        .uv(98, 0).cuboid(-3.5F, -2.5F, 0.0F, 7.0F, 7.0F, 4.0F),
                ModelTransform.of(0.0F, -8.6F, 4.2F, -0.4014F, 0.0F, 0.0F));
        hoodPipe01.addChild("hoodPipe02",
                ModelPartBuilder.create()
                        .uv(108, 13).cuboid(-2.0F, -2.0F, 0.0F, 4.0F, 5.0F, 4.0F),
                ModelTransform.of(0.0F, -0.3F, 3.1F, -0.4538F, 0.0F, 0.0F));
        ModelPartData lMaskPlate = head.addChild("lMaskPlate",
                ModelPartBuilder.create()
                        .uv(34, 57).cuboid(-2.5F, -10.0F, -6.5F, 5.0F, 8.0F, 1.0F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.3491F, 0.0F));
        ModelPartData lHorn01a = lMaskPlate.addChild("lHorn01a",
                ModelPartBuilder.create()
                        .cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(1.3F, -8.5F, -6.0F, 0.0873F, 0.0F, 0.3491F));
        lHorn01a.addChild("lHorn01b",
                ModelPartBuilder.create()
                        .uv(6, 0).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, -1.8F, 0.0F, -0.1222F, 0.8727F, 0.2094F));
        ModelPartData lHorn02a = lMaskPlate.addChild("lHorn02a",
                ModelPartBuilder.create()
                        .cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.2F, -9.5F, -6.0F, 0.0873F, 0.0F, 0.2967F));
        lHorn02a.addChild("lHorn02b",
                ModelPartBuilder.create()
                        .uv(6, 0).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, -1.8F, 0.0F, -0.1222F, 0.8727F, 0.1396F));
        ModelPartData lHorn03a = lMaskPlate.addChild("lHorn03a",
                ModelPartBuilder.create()
                        .cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(-1.1F, -9.9F, -6.0F, 0.0F, 0.0F, 0.1396F));
        ModelPartData lHorn03b = lHorn03a.addChild("lHorn03b",
                ModelPartBuilder.create()
                        .cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                ModelTransform.of(0.0F, -1.7F, 0.0F, 0.0F, 0.0F, 0.4363F));
        ModelPartData lHorn03c = lHorn03b.addChild("lHorn03c",
                ModelPartBuilder.create()
                        .cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.1F, -2.7F, 0.0F, 0.0F, 0.0F, -0.576F));
        ModelPartData lHorn03d = lHorn03c.addChild("lHorn03d",
                ModelPartBuilder.create()
                        .cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.1F, -1.6F, 0.0F, 0.0F, 0.0F, -0.4363F));
        lHorn03d.addChild("lHorn03e",
                ModelPartBuilder.create()
                        .cuboid(-0.6F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F),
                ModelTransform.of(0.0F, -1.6F, 0.0F, 0.0F, 0.0F, 0.7679F));
        ModelPartData rMaskPlate = head.addChild("rMaskPlate",
                ModelPartBuilder.create()
                        .uv(34, 57).cuboid(-2.5F, -10.0F, -6.5F, 5.0F, 8.0F, 1.0F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.3491F, 0.0F));
        ModelPartData rHorn01a = rMaskPlate.addChild("rHorn01a",
                ModelPartBuilder.create()
                        .cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(-1.3F, -9.5F, -6.0F, 0.0873F, 0.0F, -0.4538F));
        rHorn01a.addChild("rHorn01b",
                ModelPartBuilder.create()
                        .uv(6, 0).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, -1.8F, 0.0F, -0.1222F, -0.8727F, -0.2094F));
        ModelPartData rHorn02a = rMaskPlate.addChild("rHorn02a",
                ModelPartBuilder.create()
                        .cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                ModelTransform.of(-0.2F, -9.8F, -6.0F, 0.0873F, 0.0F, -0.3491F));
        rHorn02a.addChild("rHorn02b",
                ModelPartBuilder.create()
                        .uv(6, 0).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(-0.1F, -2.8F, 0.0F, -0.1222F, -0.8727F, -0.1396F));
        ModelPartData rHorn03a = rMaskPlate.addChild("rHorn03a",
                ModelPartBuilder.create()
                        .cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(1.3F, -9.9F, -6.0F, 0.0F, 0.0F, -0.1396F));
        ModelPartData rHorn03b = rHorn03a.addChild("rHorn03b",
                ModelPartBuilder.create()
                        .cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                ModelTransform.of(0.0F, -1.7F, 0.0F, 0.0F, 0.0F, -0.4363F));
        ModelPartData rHorn03c = rHorn03b.addChild("rHorn03c",
                ModelPartBuilder.create()
                        .cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                ModelTransform.of(-0.1F, -2.7F, 0.0F, 0.0F, 0.0F, 0.576F));
        ModelPartData rHorn03d = rHorn03c.addChild("rHorn03d",
                ModelPartBuilder.create()
                        .cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                ModelTransform.of(-0.1F, -2.6F, 0.0F, 0.0F, 0.0F, 0.4363F));
        rHorn03d.addChild("rHorn03e",
                ModelPartBuilder.create()
                        .cuboid(-0.4F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, -2.6F, 0.0F, 0.0F, 0.0F, -0.7679F));
        chest.addChild("chestCloth",
                ModelPartBuilder.create()
                        .uv(40, 39).cuboid(-8.0F, 0.0F, -5.5F, 16.0F, 7.0F, 11.0F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        ModelPartData lArm01 = chest.addChild("lArm01",
                ModelPartBuilder.create()
                        .uv(0, 47).cuboid(-3.0F, -0.5F, -4.5F, 7.0F, 15.0F, 9.0F),
                ModelTransform.of(6.8F, -7.9F, 0.0F, 0.0F, 0.0F, -0.192F));
        ModelPartData lArm02 = lArm01.addChild("lArm02",
                ModelPartBuilder.create()
                        .uv(0, 71).cuboid(-3.0F, 0.0F, -4.0F, 6.0F, 15.0F, 8.0F),
                ModelTransform.of(0.4F, 13.0F, 0.0F, 0.0F, 0.0F, 0.0524F));
        ModelPartData lSleeve01 = lArm02.addChild("lSleeve01",
                ModelPartBuilder.create()
                        .uv(0, 95).cuboid(-2.9F, -3.0F, 0.2F, 6.0F, 6.0F, 11.0F),
                ModelTransform.of(0.0F, 12.0F, 3.8F, 0.0F, 0.0F, 0.0F));
        lSleeve01.addChild("lSleeve02",
                ModelPartBuilder.create()
                        .uv(0, 112).cuboid(-2.5F, -5.8F, -4.5F, 5.0F, 6.0F, 10.0F),
                ModelTransform.of(0.0F, -2.8F, -0.2F, -0.7854F, 0.0F, 0.0F));
        ModelPartData rArm01 = chest.addChild("rArm01",
                ModelPartBuilder.create()
                        .uv(0, 47).cuboid(-4.0F, -0.5F, -4.5F, 7.0F, 15.0F, 9.0F),
                ModelTransform.of(-6.8F, -7.9F, 0.0F, 0.0F, 0.0F, 0.1484F));
        ModelPartData rArm02 = rArm01.addChild("rArm02",
                ModelPartBuilder.create()
                        .uv(0, 71).cuboid(-3.0F, 0.0F, -4.0F, 6.0F, 15.0F, 8.0F),
                ModelTransform.of(-0.4F, 13.0F, 0.0F, 0.0F, 0.0F, 0.0087F));
        ModelPartData rSleeve01 = rArm02.addChild("rSleeve01",
                ModelPartBuilder.create()
                        .uv(0, 95).cuboid(-3.1F, -3.0F, 0.2F, 6.0F, 6.0F, 11.0F),
                ModelTransform.of(0.0F, 12.0F, 3.8F, 0.0F, 0.0F, 0.0F));
        rSleeve01.addChild("rSleeve02",
                ModelPartBuilder.create()
                        .uv(0, 112).cuboid(-2.5F, -5.8F, -4.5F, 5.0F, 6.0F, 10.0F),
                ModelTransform.of(0.0F, -2.8F, -0.2F, -0.7854F, 0.0F, 0.0F));
        ModelPartData lWing01 = chest.addChild("lWing01",
                ModelPartBuilder.create()
                        .uv(143, 49).cuboid(-1.0F, -1.5F, 0.0F, 2.0F, 4.0F, 8.0F),
                ModelTransform.of(1.8F, -5.9F, 4.7F, 0.3491F, 0.6458F, 0.0F));
        ModelPartData lWing02 = lWing01.addChild("lWing02",
                ModelPartBuilder.create()
                        .uv(143, 62).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 9.0F),
                ModelTransform.of(0.1F, 0.2F, 7.5F, 0.0873F, 0.1396F, 0.0F));
        ModelPartData lWing03 = lWing02.addChild("lWing03",
                ModelPartBuilder.create()
                        .uv(148, 74).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 11.0F, 2.0F),
                ModelTransform.of(0.1F, 0.4F, 8.0F, 0.3812F, 0.0F, 0.0F));
        ModelPartData lWing04 = lWing03.addChild("lWing04",
                ModelPartBuilder.create()
                        .uv(151, 90).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 11.0F, 1.0F),
                ModelTransform.of(0.0F, 10.9F, 0.2F, -0.4712F, 0.0F, 0.0F));
        lWing04.addChild("lFeathers03",
                ModelPartBuilder.create()
                        .uv(138, 0).cuboid(0.0F, -2.3F, -15.9F, 0.0F, 30.0F, 16.0F),
                ModelTransform.of(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        lWing03.addChild("lFeathers02",
                ModelPartBuilder.create()
                        .uv(171, 13).cuboid(-0.5F, -0.6F, -19.1F, 1.0F, 20.0F, 22.0F),
                ModelTransform.of(-0.1F, 2.3F, 0.0F, -0.4363F, 0.0F, 0.0F));
        lWing02.addChild("lFeathers01",
                ModelPartBuilder.create()
                        .uv(187, 58).cuboid(-0.5F, -0.6F, -11.6F, 1.0F, 16.0F, 23.0F),
                ModelTransform.of(0.2F, 0.0F, 0.0F, -0.1745F, 0.0F, 0.0F));
        ModelPartData rWing01 = chest.addChild("rWing01",
                ModelPartBuilder.create()
                        .uv(143, 49).cuboid(-1.0F, -1.5F, 0.0F, 2.0F, 4.0F, 8.0F),
                ModelTransform.of(-1.8F, -5.9F, 4.7F, 0.3491F, -0.6458F, 0.0F));
        ModelPartData rWing02 = rWing01.addChild("rWing02",
                ModelPartBuilder.create()
                        .uv(143, 62).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 9.0F),
                ModelTransform.of(-0.1F, 0.2F, 7.5F, 0.0873F, -0.1396F, 0.0F));
        ModelPartData rWing03 = rWing02.addChild("rWing03",
                ModelPartBuilder.create()
                        .uv(148, 74).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 11.0F, 2.0F),
                ModelTransform.of(-0.1F, 0.4F, 8.0F, 0.3812F, 0.0F, 0.0F));
        ModelPartData rWing04 = rWing03.addChild("rWing04",
                ModelPartBuilder.create()
                        .uv(151, 90).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 11.0F, 1.0F),
                ModelTransform.of(0.0F, 10.9F, 0.2F, -0.4712F, 0.0F, 0.0F));
        rWing04.addChild("rFeathers03",
                ModelPartBuilder.create()
                        .uv(138, 0).cuboid(0.0F, -2.3F, -15.9F, 0.0F, 30.0F, 16.0F),
                ModelTransform.of(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        rWing03.addChild("lFeathers3",
                ModelPartBuilder.create()
                        .uv(171, 13).cuboid(-0.5F, -0.6F, -19.1F, 1.0F, 20.0F, 22.0F),
                ModelTransform.of(0.1F, 2.3F, 0.0F, -0.4363F, 0.0F, 0.0F));
        rWing02.addChild("lFeathers4",
                ModelPartBuilder.create()
                        .uv(187, 58).cuboid(-0.5F, -0.6F, -11.6F, 1.0F, 16.0F, 23.0F),
                ModelTransform.of(-0.2F, 0.0F, 0.0F, -0.1745F, 0.0F, 0.0F));
        return TexturedModelData.of(data, 256, 128);
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