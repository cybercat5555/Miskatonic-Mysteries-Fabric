package com.miskatonicmysteries.client.model.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class CthulhuStatueModel extends StatueModel {

	public CthulhuStatueModel(ModelPart root) {
		super(RenderLayer::getEntitySolid, root, root.getChild("body"), root.getChild("plinth"),
			  root.getChild("body").getChild("neck").getChild("head"));
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData data = new ModelData();
		ModelPartData root = data.getRoot();
		ModelPartData body = root.addChild("body",
										   ModelPartBuilder.create()
											   .uv(0, 18).cuboid(-3.0F, -8.0F, -3.0F, 6.0F, 8.0F, 6.0F),
										   ModelTransform.of(0.0F, 16.4F, 0.0F, 0.13962634F, 0.0F, 0.0F));
		ModelPartData rLeg01 = body.addChild("rLeg01",
											 ModelPartBuilder.create()
												 .uv(44, 0).mirrored(true).cuboid(-3.1F, -1.5F, -5.5F, 3.0F, 3.0F, 7.0F),
											 ModelTransform.of(-0.8F, -1.5F, 1.3F, -0.20943952F, 0.0F, 0.0F));
		rLeg01.addChild("rLeg02",
						ModelPartBuilder.create()
							.uv(48, 11).mirrored(true).cuboid(-1.39F, -1.3F, -1.6F, 3.0F, 6.0F, 2.0F),
						ModelTransform.of(-1.7F, -0.1F, -4.5F, 0.06981317F, 0.0F, 0.0F));
		ModelPartData lLeg01 = body.addChild("lLeg01",
											 ModelPartBuilder.create()
												 .uv(44, 0).cuboid(0.1F, -1.5F, -5.5F, 3.0F, 3.0F, 7.0F),
											 ModelTransform.of(0.8F, -1.5F, 1.3F, -0.20943952F, 0.0F, 0.0F));
		lLeg01.addChild("lLeg02",
						ModelPartBuilder.create()
							.uv(48, 11).cuboid(-1.59F, -1.3F, -1.6F, 3.0F, 6.0F, 2.0F),
						ModelTransform.of(1.7F, -0.1F, -4.5F, 0.06981317F, 0.0F, 0.0F));
		ModelPartData Wing01 = body.addChild("Wing01",
											 ModelPartBuilder.create()
												 .uv(53, 22).mirrored(true).cuboid(-0.5F, 0.0F, 0.8F, 1.0F, 5.0F, 4.0F),
											 ModelTransform.of(-0.7F, -8.0F, 1.8F, 0.2617994F, -0.6981317F, -0.08726646F));
		ModelPartData rWing02 = Wing01.addChild("rWing02",
												ModelPartBuilder.create()
													.uv(49, 23).mirrored(true).cuboid(-0.5F, -0.2F, -0.5F, 1.0F, 7.0F, 1.0F),
												ModelTransform.of(0.1F, 4.9F, 4.3F, -0.34906584F, 0.0F, 0.0F));
		rWing02.addChild("rWing05",
						 ModelPartBuilder.create()
							 .uv(48, 23).mirrored(true).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 4.0F, 1.0F),
						 ModelTransform.of(0.0F, 0.0F, -2.8F, 0.0F, 0.0F, 0.0F));
		rWing02.addChild("rWing03",
						 ModelPartBuilder.create()
							 .uv(48, 23).mirrored(true).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 6.0F, 1.0F),
						 ModelTransform.of(0.0F, 0.0F, -0.9F, 0.0F, 0.0F, 0.0F));
		rWing02.addChild("rWing04",
						 ModelPartBuilder.create()
							 .uv(49, 23).mirrored(true).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F),
						 ModelTransform.of(0.0F, 0.0F, -1.9F, 0.0F, 0.0F, 0.0F));
		ModelPartData rArm01 = body.addChild("rArm01",
											 ModelPartBuilder.create()
												 .uv(33, 0).mirrored(true).cuboid(-2.1F, -0.5F, -1.0F, 2.0F, 4.0F, 2.0F),
											 ModelTransform.of(-1.7F, -7.0F, 1.3F, -0.5235988F, 0.0F, 0.0F));
		ModelPartData rArm02 = rArm01.addChild("rArm02",
											   ModelPartBuilder.create()
												   .uv(32, 7).mirrored(true).cuboid(-1.0F, -0.5F, -2.8F, 2.0F, 1.0F, 4.0F),
											   ModelTransform.of(-1.2F, 3.7F, -0.3F, 0.31415927F, 0.0F, -0.17453292F));
		rArm02.addChild("rArm03",
						ModelPartBuilder.create()
							.uv(32, 13).mirrored(true).cuboid(-1.0F, -0.5F, -0.8F, 2.0F, 3.0F, 1.0F),
						ModelTransform.of(0.0F, 0.0F, -3.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData lWing01 = body.addChild("lWing01",
											  ModelPartBuilder.create()
												  .uv(53, 22).cuboid(-0.5F, 0.0F, 0.8F, 1.0F, 5.0F, 4.0F),
											  ModelTransform.of(0.7F, -8.0F, 1.8F, 0.2617994F, 0.6981317F, 0.08726646F));
		ModelPartData lWing02 = lWing01.addChild("lWing02",
												 ModelPartBuilder.create()
													 .uv(49, 23).cuboid(-0.5F, -0.2F, -0.5F, 1.0F, 7.0F, 1.0F),
												 ModelTransform.of(0.1F, 4.9F, 4.3F, -0.34906584F, 0.0F, 0.0F));
		lWing02.addChild("lWing03",
						 ModelPartBuilder.create()
							 .uv(48, 23).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 6.0F, 1.0F),
						 ModelTransform.of(0.0F, 0.0F, -0.9F, 0.0F, 0.0F, 0.0F));
		lWing02.addChild("lWing04",
						 ModelPartBuilder.create()
							 .uv(49, 23).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F),
						 ModelTransform.of(0.0F, 0.0F, -1.9F, 0.0F, 0.0F, 0.0F));
		lWing02.addChild("lWing05",
						 ModelPartBuilder.create()
							 .uv(48, 23).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 4.0F, 1.0F),
						 ModelTransform.of(0.0F, 0.0F, -2.8F, 0.0F, 0.0F, 0.0F));
		ModelPartData neck = body.addChild("neck",
										   ModelPartBuilder.create()
											   .uv(5, 21).cuboid(-1.5F, -1.4F, -1.7F, 3.0F, 2.0F, 3.0F),
										   ModelTransform.of(0.0F, -7.5F, 0.4F, 0.34906584F, 0.0F, 0.0F));
		ModelPartData head = neck.addChild("head",
										   ModelPartBuilder.create()
											   .uv(25, 23).cuboid(-2.0F, -3.6F, -2.5F, 4.0F, 4.0F, 4.0F),
										   ModelTransform.of(0.0F, -1.0F, 0.0F, -0.34906584F, 0.0F, 0.0F));
		ModelPartData tentacleBase = head.addChild("tentacleBase",
												   ModelPartBuilder.create()
													   .uv(37, 18).cuboid(-1.5F, -1.8F, -2.0F, 3.0F, 3.0F, 2.0F),
												   ModelTransform.of(0.0F, -2.1F, -1.4F, 0.6981317F, 0.0F, 0.0F));
		tentacleBase.addChild("tentacle06",
							  ModelPartBuilder.create()
								  .uv(44, 25).mirrored(true).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F),
							  ModelTransform.of(0.9F, -0.5F, -1.4F, -0.7853982F, -0.08726646F, 0.0F));
		tentacleBase.addChild("tentacle03",
							  ModelPartBuilder.create()
								  .uv(43, 25).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F),
							  ModelTransform.of(0.8F, -1.3F, -1.7F, -0.7853982F, 0.0F, 0.0F));
		tentacleBase.addChild("tentacle05_1",
							  ModelPartBuilder.create()
								  .uv(44, 25).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F),
							  ModelTransform.of(-0.9F, -0.5F, -1.4F, -0.7853982F, 0.08726646F, 0.0F));
		tentacleBase.addChild("tentacle05",
							  ModelPartBuilder.create()
								  .uv(43, 24).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 6.0F, 1.0F),
							  ModelTransform.of(0.5F, -0.5F, -1.4F, -0.80285144F, 0.0F, 0.0F));
		tentacleBase.addChild("tentacle01",
							  ModelPartBuilder.create()
								  .uv(43, 26).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F),
							  ModelTransform.of(-0.8F, -1.3F, -1.7F, -0.7853982F, 0.0F, 0.0F));
		tentacleBase.addChild("tentacle02",
							  ModelPartBuilder.create()
								  .uv(43, 24).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 4.0F, 1.0F),
							  ModelTransform.of(0.0F, -1.3F, -1.7F, -0.9599311F, 0.0F, 0.0F));
		tentacleBase.addChild("tentacle04",
							  ModelPartBuilder.create()
								  .uv(43, 24).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 6.0F, 1.0F),
							  ModelTransform.of(-0.5F, -0.5F, -1.4F, -0.87266463F, 0.0F, 0.0F));
		ModelPartData lArm01 = body.addChild("lArm01",
											 ModelPartBuilder.create()
												 .uv(33, 0).cuboid(0.2F, -0.5F, -1.0F, 2.0F, 4.0F, 2.0F),
											 ModelTransform.of(1.7F, -7.0F, 1.3F, -0.5235988F, 0.0F, 0.0F));
		ModelPartData lArm02 = lArm01.addChild("lArm02",
											   ModelPartBuilder.create()
												   .uv(32, 7).cuboid(-1.0F, -0.5F, -2.8F, 2.0F, 1.0F, 4.0F),
											   ModelTransform.of(1.2F, 3.7F, -0.3F, 0.31415927F, 0.0F, 0.17453292F));
		lArm02.addChild("lArm03",
						ModelPartBuilder.create()
							.uv(32, 13).cuboid(-1.0F, -0.5F, -0.8F, 2.0F, 3.0F, 1.0F),
						ModelTransform.of(0.0F, 0.0F, -3.0F, 0.0F, 0.0F, 0.0F));
		root.addChild("plinth",
					  ModelPartBuilder.create()
						  .cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
					  ModelTransform.of(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(data, 64, 32);
	}
}
