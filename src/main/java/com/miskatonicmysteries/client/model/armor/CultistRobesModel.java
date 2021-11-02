// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

package com.miskatonicmysteries.client.model.armor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.LivingEntity;

@Environment(EnvType.CLIENT)
public class CultistRobesModel extends BipedEntityModel<LivingEntity> {

	public final ModelPart armorBody;
	public final ModelPart lowerLeftSkirt;
	public final ModelPart lowerRightSkirt;
	public final ModelPart hoodShawl;

	public CultistRobesModel(ModelPart root) {
		super(root, RenderLayer::getArmorCutoutNoCull);
		lowerLeftSkirt = leftLeg.getChild("armorLeftLeg").getChild("lowerLeftSkirt");
		armorBody = body.getChild("armorBody");
		hoodShawl = body.getChild("hoodShawl");
		lowerRightSkirt = rightLeg.getChild("armorRightLeg").getChild("lowerRightSkirt");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData data = BipedEntityModel.getModelData(Dilation.NONE, 0);
		ModelPartData head = data.getRoot().getChild(EntityModelPartNames.HEAD);
		ModelPartData body = data.getRoot().getChild(EntityModelPartNames.BODY);
		ModelPartData rightLeg = data.getRoot().getChild(EntityModelPartNames.RIGHT_LEG);
		ModelPartData leftLeg = data.getRoot().getChild(EntityModelPartNames.LEFT_LEG);
		ModelPartData rightArm = data.getRoot().getChild(EntityModelPartNames.RIGHT_ARM);
		ModelPartData leftArm = data.getRoot().getChild(EntityModelPartNames.LEFT_ARM);

		ModelPartData armorRightLeg = rightLeg.addChild("armorRightLeg",
			ModelPartBuilder.create(),
			ModelTransform.of(3.9F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		armorRightLeg.addChild("tunicRightFront",
			ModelPartBuilder.create()
				.uv(53, 65).cuboid(-4.0F, 0.0F, 0.0F, 4.0F, 7.0F, 1.0F),
			ModelTransform.of(-2.01F, 0.0F, -2.1F, -0.1222F, 0.0F, 0.0F));
		armorRightLeg.addChild("tunicRight",
			ModelPartBuilder.create()
				.uv(24, 65).cuboid(-4.0F, 0.0F, 0.0F, 4.0F, 7.0F, 1.0F),
			ModelTransform.of(-6.01F, 0.0F, -2.1F, -0.1222F, 1.5708F, 0.0F));
		ModelPartData tunicRightBack = armorRightLeg.addChild("tunicRightBack",
			ModelPartBuilder.create()
				.uv(53, 65).cuboid(-4.0F, 0.0F, -1.0F, 4.0F, 7.0F, 1.0F),
			ModelTransform.of(-2.01F, 0.0F, 2.1F, 0.1222F, 0.0F, 0.0F));
		tunicRightBack.addChild("tunicRightBack2",
			ModelPartBuilder.create(),
			ModelTransform.of(0.01F, 10.0F, -1.0F, 0.2618F, 0.0F, 0.0F));
		ModelPartData lowerRightSkirt = armorRightLeg.addChild("lowerRightSkirt",
			ModelPartBuilder.create(),
			ModelTransform.of(-4.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData tunicLowerFront2 = lowerRightSkirt.addChild("tunicLowerFront2",
			ModelPartBuilder.create()
				.uv(53, 71).cuboid(-4.0F, 7.0F, 0.0F, 4.0F, 3.0F, 1.0F),
			ModelTransform.of(1.99F, 0.0F, -2.1F, -0.1222F, 0.0F, 0.0F));
		tunicLowerFront2.addChild("tunicRightFront4",
			ModelPartBuilder.create()
				.uv(53, 77).cuboid(-4.0F, 0.0F, -1.0F, 4.0F, 2.0F, 1.0F),
			ModelTransform.of(0.01F, 10.0F, 1.0F, -0.2618F, 0.0F, 0.0F));
		ModelPartData tunicRight3 = lowerRightSkirt.addChild("tunicRight3",
			ModelPartBuilder.create()
				.uv(24, 72).cuboid(-4.0F, 7.0F, 0.0F, 4.0F, 3.0F, 1.0F),
			ModelTransform.of(-2.01F, 0.0F, -2.1F, -0.1222F, 1.5708F, 0.0F));
		tunicRight3.addChild("tunicRight4",
			ModelPartBuilder.create()
				.uv(23, 78).cuboid(-4.0F, 0.0F, -1.0F, 4.0F, 2.0F, 1.0F),
			ModelTransform.of(0.01F, 10.0F, 1.0F, -0.2618F, 0.0F, 0.0F));
		ModelPartData tunicRightBack3 = lowerRightSkirt.addChild("tunicRightBack3",
			ModelPartBuilder.create()
				.uv(53, 72).cuboid(-4.0F, 7.0F, -1.0F, 4.0F, 3.0F, 1.0F),
			ModelTransform.of(1.99F, 0.0F, 2.1F, 0.1222F, 0.0F, 0.0F));
		tunicRightBack3.addChild("tunicRightBack4",
			ModelPartBuilder.create()
				.uv(53, 77).cuboid(-4.0F, 0.0F, 0.0F, 4.0F, 2.0F, 1.0F),
			ModelTransform.of(0.01F, 10.0F, -1.0F, 0.2618F, 0.0F, 0.0F));
		ModelPartData tunicLowerLeft2 = lowerRightSkirt.addChild("tunicLowerLeft2",
			ModelPartBuilder.create()
				.uv(23, 72).cuboid(-4.0F, 6.5125F, -4.9702F, 4.0F, 3.0F, 1.0F),
			ModelTransform.of(6.01F, 0.0F, -2.1F, 0.1222F, 1.5708F, 0.0F));
		tunicLowerLeft2.addChild("tunicLeft3",
			ModelPartBuilder.create()
				.uv(23, 78).cuboid(-4.0F, -1.4984F, -3.7087F, 4.0F, 2.0F, 1.0F),
			ModelTransform.of(0.01F, 10.0F, -1.0F, 0.2618F, 0.0F, 0.0F));
		ModelPartData armorBody = body.addChild("armorBody",
			ModelPartBuilder.create()
				.uv(0, 81).cuboid(-4.5F, -0.01F, -2.5F, 9.0F, 12.0F, 5.0F)
				.uv(1, 104).cuboid(-1.5F, 4.5F, -3.0F, 3.0F, 3.0F, 1.0F)
				.uv(8, 109).cuboid(2.55F, -0.75F, -2.65F, 1.0F, 1.0F, 5.0F)
				.mirrored(true).cuboid(-3.55F, -0.75F, -2.65F, 1.0F, 1.0F, 5.0F)
				.uv(6, 113).cuboid(-3.3F, -0.76F, 1.36F, 6.0F, 1.0F, 1.0F),
			ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		armorBody.addChild("medallionStrapR_r1",
			ModelPartBuilder.create()
				.uv(2, 111).mirrored(true).cuboid(-1.3F, -4.5F, 0.1F, 1.0F, 6.0F, 1.0F),
			ModelTransform.of(-1.0F, 3.5F, -3.0F, 0.0F, 0.0F, -0.4363F));
		armorBody.addChild("medallionStrapL_r1",
			ModelPartBuilder.create()
				.uv(2, 111).cuboid(0.3F, -4.5F, 0.1F, 1.0F, 6.0F, 1.0F),
			ModelTransform.of(1.0F, 3.5F, -3.0F, 0.0F, 0.0F, 0.4363F));
		body.addChild("hoodShawl",
			ModelPartBuilder.create()
				.uv(82, 76).mirrored(true).cuboid(-5.0F, -0.25F, -3.0F, 10.0F, 1.0F, 6.0F, new Dilation(0.25F, 0.25F, 0.25F))
				.uv(83, 91).cuboid(-5.0F, 1.25F, -3.0F, 10.0F, 4.0F, 6.0F, new Dilation(0.25F, 0.25F, 0.25F)),
			ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData lowerBodySkirt = armorBody.addChild("lowerBodySkirt",
			ModelPartBuilder.create(),
			ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData lowerLeftSkirt2 = lowerBodySkirt.addChild("lowerLeftSkirt2",
			ModelPartBuilder.create(),
			ModelTransform.of(9.0F, 5.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData tunicLowerFront3 = lowerLeftSkirt2.addChild("tunicLowerFront3",
			ModelPartBuilder.create()
				.uv(53, 72).cuboid(-1.01F, -3.1F, -0.77F, 4.0F, 3.0F, 1.0F),
			ModelTransform.of(-6.0F, 10.0F, -2.6F, -3.0194F, 0.0F, -3.1416F));
		tunicLowerFront3.addChild("tunicFrontFringe_r1",
			ModelPartBuilder.create()
				.uv(53, 77).cuboid(-2.0F, 0.8F, -0.6F, 4.0F, 2.0F, 1.0F),
			ModelTransform.of(1.0F, -1.0F, -0.4F, 0.2618F, 0.0F, 0.0F));
		ModelPartData tunicLowerLeft3 = lowerLeftSkirt2.addChild("tunicLowerLeft3",
			ModelPartBuilder.create()
				.uv(23, 72).cuboid(-4.0F, 7.0F, -1.0F, 4.0F, 3.0F, 1.0F),
			ModelTransform.of(-5.49F, 0.0F, -2.1F, 0.1222F, 1.5708F, 0.0F));
		tunicLowerLeft3.addChild("tunicLeft7",
			ModelPartBuilder.create()
				.uv(23, 78).cuboid(-4.0F, 0.0F, 0.0F, 4.0F, 2.0F, 1.0F),
			ModelTransform.of(0.01F, 10.0F, -1.0F, 0.2618F, 0.0F, 0.0F));
		ModelPartData tunicLeftBack5 = lowerLeftSkirt2.addChild("tunicLeftBack5",
			ModelPartBuilder.create()
				.uv(53, 72).cuboid(-4.0F, 7.0F, 0.0F, 4.0F, 3.0F, 1.0F),
			ModelTransform.of(-9.01F, 0.0F, 1.6F, 3.0194F, 0.0F, 3.1416F));
		tunicLeftBack5.addChild("tunicLeftBack6",
			ModelPartBuilder.create()
				.uv(53, 77).cuboid(-4.0F, 0.0F, -1.0F, 4.0F, 2.0F, 1.0F),
			ModelTransform.of(0.01F, 10.0F, 1.0F, -0.2618F, 0.0F, 0.0F));
		ModelPartData lowerRightSkirt2 = lowerBodySkirt.addChild("lowerRightSkirt2",
			ModelPartBuilder.create(),
			ModelTransform.of(-2.0F, 5.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData tunicLowerFront4 = lowerRightSkirt2.addChild("tunicLowerFront4",
			ModelPartBuilder.create()
				.uv(53, 71).cuboid(-4.0F, 7.0F, 0.0F, 4.0F, 3.0F, 1.0F),
			ModelTransform.of(1.99F, 0.0F, -1.6F, -0.1222F, 0.0F, 0.0F));
		tunicLowerFront4.addChild("tunicRightFront2",
			ModelPartBuilder.create()
				.uv(53, 77).cuboid(-4.0F, 0.0F, -1.0F, 4.0F, 2.0F, 1.0F),
			ModelTransform.of(0.01F, 10.0F, 1.0F, -0.2618F, 0.0F, 0.0F));
		ModelPartData tunicRight7 = lowerRightSkirt2.addChild("tunicRight7",
			ModelPartBuilder.create()
				.uv(24, 72).cuboid(-4.0F, 7.0F, 0.0F, 4.0F, 3.0F, 1.0F),
			ModelTransform.of(-1.51F, 0.0F, -2.1F, -0.1222F, 1.5708F, 0.0F));
		tunicRight7.addChild("tunicRight8",
			ModelPartBuilder.create()
				.uv(23, 78).cuboid(-4.0F, 0.0F, -1.0F, 4.0F, 2.0F, 1.0F),
			ModelTransform.of(0.01F, 10.0F, 1.0F, -0.2618F, 0.0F, 0.0F));
		ModelPartData tunicRightBack5 = lowerRightSkirt2.addChild("tunicRightBack5",
			ModelPartBuilder.create()
				.uv(53, 72).cuboid(-4.0F, 7.0F, -1.0F, 4.0F, 3.0F, 1.0F),
			ModelTransform.of(1.99F, 0.0F, 1.6F, 0.1222F, 0.0F, 0.0F));
		tunicRightBack5.addChild("tunicRightBack6",
			ModelPartBuilder.create()
				.uv(53, 77).cuboid(-4.0F, 0.0F, 0.0F, 4.0F, 2.0F, 1.0F),
			ModelTransform.of(0.01F, 10.0F, -1.0F, 0.2618F, 0.0F, 0.0F));
		ModelPartData armorLeftLeg = leftLeg.addChild("armorLeftLeg",
			ModelPartBuilder.create(),
			ModelTransform.of(-3.9F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData tunicLeftFront = armorLeftLeg.addChild("tunicLeftFront",
			ModelPartBuilder.create()
				.uv(53, 65).cuboid(-4.0F, 0.0F, -1.0F, 4.0F, 7.0F, 1.0F),
			ModelTransform.of(1.99F, 0.0F, -2.1F, -3.0194F, 0.0F, 3.1416F));
		tunicLeftFront.addChild("tunicFront",
			ModelPartBuilder.create(),
			ModelTransform.of(0.01F, 10.0F, -1.0F, 0.2618F, 0.0F, 0.0F));
		ModelPartData tunicLeft = armorLeftLeg.addChild("tunicLeft",
			ModelPartBuilder.create()
				.uv(23, 65).cuboid(-4.0F, 0.0F, -1.0F, 4.0F, 7.0F, 1.0F),
			ModelTransform.of(6.01F, 0.0F, -2.1F, 0.1222F, 1.5708F, 0.0F));
		tunicLeft.addChild("tunicLeft2",
			ModelPartBuilder.create(),
			ModelTransform.of(0.01F, 10.0F, -1.0F, 0.2618F, 0.0F, 0.0F));
		ModelPartData tunicLeftBack = armorLeftLeg.addChild("tunicLeftBack",
			ModelPartBuilder.create()
				.uv(53, 65).cuboid(-4.0F, 0.0F, 0.0F, 4.0F, 7.0F, 1.0F),
			ModelTransform.of(1.99F, 0.0F, 2.1F, 3.0194F, 0.0F, 3.1416F));
		tunicLeftBack.addChild("tunicLeftBack2",
			ModelPartBuilder.create(),
			ModelTransform.of(0.01F, 10.0F, 1.0F, -0.2618F, 0.0F, 0.0F));
		ModelPartData lowerLeftSkirt = armorLeftLeg.addChild("lowerLeftSkirt",
			ModelPartBuilder.create(),
			ModelTransform.of(11.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData tunicLowerFront = lowerLeftSkirt.addChild("tunicLowerFront",
			ModelPartBuilder.create()
				.uv(53, 72).cuboid(-1.01F, -3.1F, -0.77F, 4.0F, 3.0F, 1.0F),
			ModelTransform.of(-6.0F, 10.0F, -3.1F, -3.0194F, 0.0F, -3.1416F));
		tunicLowerFront.addChild("tunicFrontFringe_r2",
			ModelPartBuilder.create()
				.uv(53, 77).cuboid(-2.0F, 0.8F, -0.6F, 4.0F, 2.0F, 1.0F),
			ModelTransform.of(1.0F, -1.0F, -0.4F, 0.2618F, 0.0F, 0.0F));
		ModelPartData tunicLowerLeft = lowerLeftSkirt.addChild("tunicLowerLeft",
			ModelPartBuilder.create()
				.uv(23, 72).cuboid(-4.0F, 7.0F, -1.0F, 4.0F, 3.0F, 1.0F),
			ModelTransform.of(-4.99F, 0.0F, -2.1F, 0.1222F, 1.5708F, 0.0F));
		tunicLowerLeft.addChild("tunicLeft4",
			ModelPartBuilder.create()
				.uv(23, 78).cuboid(-4.0F, 0.0F, 0.0F, 4.0F, 2.0F, 1.0F),
			ModelTransform.of(0.01F, 10.0F, -1.0F, 0.2618F, 0.0F, 0.0F));
		ModelPartData tunicLeftBack3 = lowerLeftSkirt.addChild("tunicLeftBack3",
			ModelPartBuilder.create()
				.uv(53, 72).cuboid(-4.0F, 7.0F, 0.0F, 4.0F, 3.0F, 1.0F),
			ModelTransform.of(-9.01F, 0.0F, 2.1F, 3.0194F, 0.0F, 3.1416F));
		tunicLeftBack3.addChild("tunicLeftBack4",
			ModelPartBuilder.create()
				.uv(53, 77).cuboid(-4.0F, 0.0F, -1.0F, 4.0F, 2.0F, 1.0F),
			ModelTransform.of(0.01F, 10.0F, 1.0F, -0.2618F, 0.0F, 0.0F));
		ModelPartData tunicRight2 = lowerLeftSkirt.addChild("tunicRight2",
			ModelPartBuilder.create()
				.uv(24, 72).cuboid(-4.0F, 6.3907F, 4.9627F, 4.0F, 3.0F, 1.0F),
			ModelTransform.of(-14.01F, 0.0F, -2.1F, -0.1222F, 1.5708F, 0.0F));
		tunicRight2.addChild("tunicRight5",
			ModelPartBuilder.create()
				.uv(23, 78).cuboid(-4.0F, -1.873F, 3.6359F, 4.0F, 2.0F, 1.0F),
			ModelTransform.of(0.01F, 10.0F, 1.0F, -0.2618F, 0.0F, 0.0F));
		armorLeftLeg.addChild("tunicRight6",
			ModelPartBuilder.create()
				.uv(24, 65).cuboid(-3.99F, -0.5F, 3.9F, 4.0F, 7.0F, 1.0F),
			ModelTransform.of(-2.01F, 0.0F, -2.1F, -0.1222F, 1.5708F, 0.0F));
		ModelPartData armorHead = head.addChild("armorHead",
			ModelPartBuilder.create(),
			ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData mainHood = armorHead.addChild("mainHood",
			ModelPartBuilder.create()
				.uv(74, 0).cuboid(-4.0F, -9.0F, -4.0F, 8.0F, 10.0F, 8.0F, new Dilation(0.55F, 0.55F, 0.55F)),
			ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		mainHood.addChild("hoodFringeL01",
			ModelPartBuilder.create()
				.uv(64, 40).mirrored(true).cuboid(-1.2F, -0.8F, -5.09F, 1.0F, 10.0F, 9.0F),
			ModelTransform.of(-2.3F, -9.2F, 0.1F, 0.0F, 0.0F, 0.2269F));
		mainHood.addChild("hoodFringeR01",
			ModelPartBuilder.create()
				.uv(64, 40).cuboid(0.9F, -0.6F, -5.09F, 1.0F, 10.0F, 9.0F),
			ModelTransform.of(1.7F, -9.2F, 0.1F, 0.0F, 0.0F, -0.2269F));
		mainHood.addChild("hoodFringeL02",
			ModelPartBuilder.create()
				.uv(88, 39).cuboid(-0.4F, -0.6F, -5.07F, 4.0F, 1.0F, 9.0F),
			ModelTransform.of(-3.3F, -9.45F, 0.1F, 0.0F, 0.0F, -0.2618F));
		mainHood.addChild("hoodFringeR03",
			ModelPartBuilder.create()
				.uv(88, 39).cuboid(-0.8F, -0.6F, -5.08F, 4.0F, 1.0F, 9.0F),
			ModelTransform.of(0.6F, -10.15F, 0.1F, 0.0F, 0.0F, 0.2618F));
		ModelPartData hoodPipe01 = mainHood.addChild("hoodPipe01",
			ModelPartBuilder.create()
				.uv(75, 23).cuboid(-4.5F, -2.5F, 0.0F, 9.0F, 10.0F, 2.0F, new Dilation(-0.01F, -0.01F, -0.01F)),
			ModelTransform.of(0.0F, -7.3F, 2.7F, 0.0F, 0.0F, 0.0F));
		hoodPipe01.addChild("hoodPipe02",
			ModelPartBuilder.create()
				.uv(109, 33).cuboid(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 4.0F),
			ModelTransform.of(0.0F, -0.3F, 1.0F, -0.4538F, 0.0F, 0.0F));
		mainHood.addChild("hoodLSide02",
			ModelPartBuilder.create()
				.uv(79, 51).cuboid(-0.5F, -0.9F, -5.25F, 6.0F, 2.0F, 9.0F),
			ModelTransform.of(-5.15F, 0.1F, 0.3F, 0.0F, 0.0F, 0.3142F));
		mainHood.addChild("hoodRSide02",
			ModelPartBuilder.create()
				.uv(79, 51).cuboid(-5.4F, -0.9F, -5.25F, 6.0F, 2.0F, 9.0F),
			ModelTransform.of(5.15F, 0.1F, 0.3F, 0.0F, 0.0F, -0.3142F));
		rightArm.addChild("armorRightArm",
			ModelPartBuilder.create()
				.uv(47, 82).cuboid(-4.5F, -2.9F, -2.5F, 5.0F, 12.0F, 5.0F, new Dilation(-0.1F, -0.1F, -0.1F))
				.uv(50, 100).mirrored(true).cuboid(-4.3F, 5.0F, 2.4F, 4.0F, 4.0F, 2.0F),
			ModelTransform.of(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData tunicLeft5 = rightLeg.addChild("tunicLeft5",
			ModelPartBuilder.create()
				.uv(23, 65).cuboid(-4.0F, -0.4875F, -4.9702F, 4.0F, 7.0F, 1.0F),
			ModelTransform.of(6.01F, 0.0F, -2.1F, 0.1222F, 1.5708F, 0.0F));
		tunicLeft5.addChild("tunicLeft6",
			ModelPartBuilder.create(),
			ModelTransform.of(0.01F, 10.0F, -1.0F, 0.2618F, 0.0F, 0.0F));
		leftArm.addChild("armorLeftArm",
			ModelPartBuilder.create()
				.uv(47, 82).mirrored(true).cuboid(-0.5F, -2.9F, -2.5F, 5.0F, 12.0F, 5.0F, new Dilation(-0.1F, -0.1F, -0.1F))
				.uv(50, 100).mirrored(false).cuboid(0.3F, 5.0F, 2.4F, 4.0F, 4.0F, 2.0F),
			ModelTransform.of(-1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(data, 128, 128);
	}
}