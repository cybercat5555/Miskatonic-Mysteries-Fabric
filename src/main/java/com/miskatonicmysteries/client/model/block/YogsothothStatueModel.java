package com.miskatonicmysteries.client.model.block;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.RenderLayer;

public class YogsothothStatueModel extends StatueModel {

	public YogsothothStatueModel(ModelPart root) {
		super(RenderLayer::getEntitySolid, root, root.getChild("body"), root.getChild("plinth"),
			  root.getChild("body"));
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData data = new ModelData();
		ModelPartData root = data.getRoot();

		ModelPartData body = root.addChild("body",
										   ModelPartBuilder.create(),
										   ModelTransform.of(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		body.addChild("orb04_r1",
					  ModelPartBuilder.create()
						  .uv(49, 0).cuboid(-1.0F, -0.75F, -1.35F, 2.0F, 2.0F, 3.0F, new Dilation(0.16F, 0.16F, 0.16F)),
					  ModelTransform.of(-2.0F, -10.0F, 0.0F, 0.0F, 0.0F, 0.3491F));
		body.addChild("orb03_r1",
					  ModelPartBuilder.create()
						  .uv(54, 6).cuboid(-1.0F, -0.75F, -1.35F, 2.0F, 2.0F, 3.0F, new Dilation(0.16F, 0.16F, 0.16F)),
					  ModelTransform.of(2.0F, -10.0F, 0.0F, 0.0F, 0.0F, 0.7854F));
		body.addChild("orb02_r1",
					  ModelPartBuilder.create()
						  .uv(38, 0).cuboid(-1.5F, -2.25F, -1.25F, 2.0F, 2.0F, 3.0F),
					  ModelTransform.of(1.5F, -16.0F, 0.0F, 0.0F, 0.0F, 0.48F));
		body.addChild("orb01_r1",
					  ModelPartBuilder.create()
						  .uv(25, 0).cuboid(-1.25F, -3.25F, -1.3F, 3.0F, 3.0F, 3.0F, new Dilation(0.1F, 0.1F, 0.1F)),
					  ModelTransform.of(-2.0F, -13.0F, 0.0F, 0.0F, 0.0F, 0.7854F));
		ModelPartData wisp01a = body.addChild("wisp01a",
											  ModelPartBuilder.create()
												  .uv(0, 26).cuboid(-1.5F, -3.0F, -1.5F, 3.0F, 3.0F, 3.0F),
											  ModelTransform.of(-2.0F, -7.0F, 0.0F, 0.0F, 0.0F, -0.48F));
		ModelPartData wisp01b = wisp01a.addChild("wisp01b",
												 ModelPartBuilder.create()
													 .uv(52, 11).cuboid(-1.5F, -4.0F, -1.5F, 3.0F, 4.0F, 3.0F, new Dilation(-0.2F, -0.2F, -0.2F)),
												 ModelTransform.of(0.0F, -2.25F, 0.0F, 0.0F, 0.0F, 0.1745F));
		wisp01b.addChild("wisp01c",
						 ModelPartBuilder.create()
							 .uv(56, 19).cuboid(-0.5F, -4.0F, -1.5F, 2.0F, 4.0F, 2.0F, new Dilation(-0.1F, -0.1F, -0.1F)),
						 ModelTransform.of(-0.5F, -3.5F, 0.5F, 0.0F, 0.0F, 0.1309F));
		ModelPartData wisp02a = body.addChild("wisp02a",
											  ModelPartBuilder.create()
												  .uv(23, 25).cuboid(-1.5F, -4.0F, -1.5F, 3.0F, 4.0F, 3.0F),
											  ModelTransform.of(0.0F, -7.0F, 0.25F, 0.0F, 0.0F, -0.3927F));
		ModelPartData wisp02b = wisp02a.addChild("wisp02b",
												 ModelPartBuilder.create()
													 .uv(23, 17).cuboid(-1.5F, -4.0F, -1.5F, 3.0F, 4.0F, 3.0F, new Dilation(-0.2F, -0.2F, -0.2F)),
												 ModelTransform.of(0.0F, -3.25F, 0.0F, 0.0F, 0.0F, 0.1745F));
		ModelPartData wisp02c = wisp02b.addChild("wisp02c",
												 ModelPartBuilder.create()
													 .uv(40, 9).cuboid(-1.0F, -2.95F, -2.0F, 3.0F, 3.0F, 3.0F, new Dilation(-0.25F, -0.25F, -0.25F)),
												 ModelTransform.of(-0.5F, -3.25F, 0.5F, 0.0F, 0.0F, 0.2182F));
		ModelPartData wisp02d = wisp02c.addChild("wisp02d",
												 ModelPartBuilder.create()
													 .uv(52, 12).cuboid(-1.0F, -2.75F, -2.0F, 3.0F, 3.0F, 3.0F, new Dilation(-0.3F, -0.3F, -0.3F)),
												 ModelTransform.of(0.0F, -2.25F, 0.0F, 0.0F, 0.0F, 0.48F));
		ModelPartData wisp02e = wisp02d.addChild("wisp02e",
												 ModelPartBuilder.create()
													 .uv(56, 19).cuboid(-1.0F, -2.75F, -1.25F, 2.0F, 3.0F, 2.0F),
												 ModelTransform.of(0.5F, -2.0F, -0.25F, 0.0F, 0.0F, 0.0873F));
		wisp02e.addChild("wisp02f",
						 ModelPartBuilder.create()
							 .uv(56, 25).cuboid(-1.0F, -3.75F, -1.25F, 2.0F, 4.0F, 2.0F, new Dilation(-0.2F, -0.2F, -0.2F)),
						 ModelTransform.of(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, -0.7418F));
		ModelPartData wisp03a = body.addChild("wisp03a",
											  ModelPartBuilder.create()
												  .uv(40, 25).cuboid(-1.5F, -4.0F, -1.5F, 3.0F, 4.0F, 3.0F),
											  ModelTransform.of(1.25F, -7.0F, 0.0F, 0.0F, 0.0F, 0.0873F));
		ModelPartData wisp03b = wisp03a.addChild("wisp03b",
												 ModelPartBuilder.create()
													 .uv(40, 17).cuboid(-1.5F, -4.0F, -1.5F, 3.0F, 4.0F, 3.0F, new Dilation(-0.2F, -0.2F, -0.2F)),
												 ModelTransform.of(0.0F, -3.25F, 0.0F, 0.0F, 0.0F, -0.4363F));
		ModelPartData wisp03c = wisp03b.addChild("wisp03c",
												 ModelPartBuilder.create()
													 .uv(40, 9).cuboid(-1.0F, -2.95F, -2.0F, 3.0F, 3.0F, 3.0F, new Dilation(-0.25F, -0.25F, -0.25F)),
												 ModelTransform.of(-0.5F, -3.25F, 0.5F, 0.0F, 0.0F, 0.3491F));
		ModelPartData wisp03d = wisp03c.addChild("wisp03d",
												 ModelPartBuilder.create()
													 .uv(52, 12).cuboid(-1.0F, -2.75F, -2.0F, 3.0F, 3.0F, 3.0F, new Dilation(-0.3F, -0.3F, -0.3F)),
												 ModelTransform.of(0.0F, -2.25F, 0.0F, 0.0F, 0.0F, 0.4363F));
		ModelPartData wisp03e = wisp03d.addChild("wisp03e",
												 ModelPartBuilder.create()
													 .uv(56, 19).mirrored(true).cuboid(-1.0F, -3.75F, -1.25F, 2.0F, 4.0F, 2.0F),
												 ModelTransform.of(0.5F, -2.0F, -0.25F, 0.0F, 0.0F, -0.3054F));
		wisp03e.addChild("wisp03f",
						 ModelPartBuilder.create()
							 .uv(56, 25).mirrored(true).cuboid(-1.0F, -3.75F, -1.25F, 2.0F, 4.0F, 2.0F, new Dilation(-0.1F, -0.1F, -0.1F)),
						 ModelTransform.of(0.0F, -3.0F, 0.0F, 0.0F, 0.0F, -0.3054F));
		ModelPartData wisp04a = body.addChild("wisp04a",
											  ModelPartBuilder.create()
												  .uv(23, 25).cuboid(-1.5F, -4.0F, -1.5F, 3.0F, 4.0F, 3.0F),
											  ModelTransform.of(2.25F, -7.0F, 0.25F, 0.0F, 0.0F, 0.1309F));
		ModelPartData wisp04b = wisp04a.addChild("wisp04b",
												 ModelPartBuilder.create()
													 .uv(23, 17).cuboid(-1.5F, -4.0F, -1.5F, 3.0F, 4.0F, 3.0F, new Dilation(-0.2F, -0.2F, -0.2F)),
												 ModelTransform.of(0.0F, -3.25F, 0.0F, 0.0F, 0.0F, -0.3927F));
		ModelPartData wisp04c = wisp04b.addChild("wisp04c",
												 ModelPartBuilder.create()
													 .uv(40, 9).cuboid(-1.0F, -2.95F, -2.0F, 3.0F, 3.0F, 3.0F, new Dilation(-0.25F, -0.25F, -0.25F)),
												 ModelTransform.of(-0.5F, -3.25F, 0.5F, 0.0F, 0.0F, 0.3054F));
		ModelPartData wisp04d = wisp04c.addChild("wisp04d",
												 ModelPartBuilder.create()
													 .uv(52, 11).cuboid(-1.0F, -3.75F, -2.0F, 3.0F, 4.0F, 3.0F, new Dilation(-0.3F, -0.3F, -0.3F)),
												 ModelTransform.of(0.0F, -2.25F, 0.0F, 0.0F, 0.0F, 0.48F));
		ModelPartData wisp04e = wisp04d.addChild("wisp04e",
												 ModelPartBuilder.create()
													 .uv(56, 19).cuboid(-1.0F, -3.75F, -1.25F, 2.0F, 4.0F, 2.0F),
												 ModelTransform.of(0.5F, -3.0F, -0.25F, 0.0F, 0.0F, -0.1309F));
		ModelPartData wisp04f = wisp04e.addChild("wisp04f",
												 ModelPartBuilder.create(),
												 ModelTransform.of(-0.25F, -3.0F, 0.0F, 0.0F, 0.0F, -0.3927F));
		wisp04f.addChild("cube_r1",
						 ModelPartBuilder.create()
							 .uv(56, 25).cuboid(-1.0F, -3.75F, -1.25F, 2.0F, 4.0F, 2.0F, new Dilation(-0.1F, -0.1F, -0.1F)),
						 ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
		root.addChild("plinth",
					  ModelPartBuilder.create()
						  .cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
					  ModelTransform.of(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		return TexturedModelData.of(data, 64, 32);
	}
}