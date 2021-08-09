package com.miskatonicmysteries.client.model.block;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class MasterpieceStatueModel extends Model {
	private ModelPart root;

	public MasterpieceStatueModel(ModelPart root) {
		super(RenderLayer::getEntityCutout);
		this.root = root;
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData data = new ModelData();
		ModelPartData root = data.getRoot();
		root.addChild("plinth",
				ModelPartBuilder.create().uv(65, 0).cuboid(-7.5F, -3.0F, -7.5F, 15.0F, 3.0F, 15.0F).uv(66, 20).cuboid(-6.5F, -6.0F, -6.5F, 13.0F, 3.0F, 13.0F), ModelTransform.of(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData tentacles = root.addChild("tentacles", ModelPartBuilder.create(), ModelTransform.of(0.0F, 24.0F,
				0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData tentacle01a = tentacles.addChild("tentacle01a",
				ModelPartBuilder.create().uv(66, 38).cuboid(-2.5F, -12.0F, -2.5F, 5.0F, 12.0F, 5.0F),
				ModelTransform.of(0.0F, 0.25F, 5.5F, -0.0873F, 0.0F, 0.0F));
		ModelPartData tentacle01b = tentacle01a.addChild("tentacle01b",
				ModelPartBuilder.create().uv(87, 39).cuboid(-2.0F, -12.0F, -2.0F, 4.0F, 12.0F, 4.0F),
				ModelTransform.of(0.0F, -10.75F, 0.0F, 0.3054F, 0.0F, -0.1309F));
		ModelPartData tentacle01c = tentacle01b.addChild("tentacle01c",
				ModelPartBuilder.create().uv(105, 40).cuboid(-1.5F, -12.0F, -1.5F, 3.0F, 12.0F, 3.0F),
				ModelTransform.of(0.0F, -10.75F, 0.0F, -0.4363F, 0.0F, 0.2618F));
		tentacle01c.addChild("tentacle01d", ModelPartBuilder.create().uv(118, 41).cuboid(-1.0F, -11.0F, -1.0F, 2.0F,
				11.0F, 2.0F), ModelTransform.of(0.0F, -11.75F, 0.0F, 0.3054F, 0.0F, 0.0F));
		ModelPartData tentacle02a = tentacles.addChild("tentacle02a",
				ModelPartBuilder.create().uv(66, 38).cuboid(-2.5F, -12.0F, -2.5F, 5.0F, 12.0F, 5.0F),
				ModelTransform.of(-5.0F, 0.25F, 0.5F, -0.0873F, -1.0472F, 0.0F));
		ModelPartData tentacle02b = tentacle02a.addChild("tentacle02b",
				ModelPartBuilder.create().uv(87, 39).cuboid(-2.0F, -12.0F, -2.0F, 4.0F, 12.0F, 4.0F),
				ModelTransform.of(0.0F, -10.75F, 0.0F, 0.3054F, 0.0F, -0.1309F));
		ModelPartData tentacle02c = tentacle02b.addChild("tentacle02c",
				ModelPartBuilder.create().uv(105, 40).cuboid(-1.5F, -12.0F, -1.5F, 3.0F, 12.0F, 3.0F),
				ModelTransform.of(0.0F, -10.75F, 0.0F, -0.3491F, 0.0F, 0.2618F));
		tentacle02c.addChild("tentacle02d", ModelPartBuilder.create().uv(118, 41).cuboid(-1.0F, -11.0F, -1.0F, 2.0F,
				11.0F, 2.0F), ModelTransform.of(0.0F, -11.75F, 0.0F, 0.3054F, 0.0F, 0.0F));
		ModelPartData tentacle03a = tentacles.addChild("tentacle03a",
				ModelPartBuilder.create().uv(66, 38).cuboid(-2.5F, -12.0F, -2.5F, 5.0F, 12.0F, 5.0F),
				ModelTransform.of(5.0F, 0.25F, 0.5F, -0.0873F, 1.0472F, 0.0F));
		ModelPartData tentacle03b = tentacle03a.addChild("tentacle03b",
				ModelPartBuilder.create().uv(87, 39).cuboid(-2.0F, -12.0F, -2.0F, 4.0F, 12.0F, 4.0F),
				ModelTransform.of(0.0F, -10.75F, 0.0F, 0.3054F, 0.0F, -0.1309F));
		ModelPartData tentacle03c = tentacle03b.addChild("tentacle03c",
				ModelPartBuilder.create().uv(105, 40).cuboid(-1.5F, -12.0F, -1.5F, 3.0F, 12.0F, 3.0F),
				ModelTransform.of(0.0F, -10.75F, 0.0F, -0.3491F, 0.0F, 0.2618F));
		tentacle03c.addChild("tentacle03d", ModelPartBuilder.create().uv(118, 41).cuboid(-1.0F, -11.0F, -1.0F, 2.0F,
				11.0F, 2.0F), ModelTransform.of(0.0F, -11.75F, 0.0F, 0.3054F, 0.0F, -0.2182F));
		ModelPartData tentacles2 = root.addChild("tentacles2", ModelPartBuilder.create(), ModelTransform.of(0.0F,
				24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData tentacleSmall01a = tentacles2.addChild("tentacleSmall01a",
				ModelPartBuilder.create().uv(87, 39).cuboid(-2.0F, -10.5F, -2.0F, 4.0F, 11.0F, 4.0F),
				ModelTransform.of(-4.0F, -1.5F, 5.0F, 0.829F, 0.0F, 0.0F));
		ModelPartData tentacleSmall01b = tentacleSmall01a.addChild("tentacleSmall01b",
				ModelPartBuilder.create().uv(105, 40).cuboid(-1.0F, -4.0F, -1.5F, 3.0F, 5.0F, 3.0F),
				ModelTransform.of(-0.5F, -10.5F, 0.0F, 0.4363F, -0.2618F, 0.6109F));
		ModelPartData tentacleSmall01c = tentacleSmall01b.addChild("tentacleSmall01c",
				ModelPartBuilder.create().uv(118, 41).cuboid(-1.0F, -5.0F, -1.0F, 2.0F, 6.0F, 2.0F),
				ModelTransform.of(0.5F, -3.0F, 0.0F, 0.0F, 0.0F, 0.6545F));
		tentacleSmall01c.addChild("tentacleSmall01d", ModelPartBuilder.create().uv(120, 42).cuboid(-0.5F, -4.0F, -0.5F
				, 1.0F, 5.0F, 1.0F), ModelTransform.of(0.0F, -5.0F, 0.0F, 0.0F, 0.0F, 0.6545F));
		ModelPartData tentacleSmall02a = tentacles2.addChild("tentacleSmall02a",
				ModelPartBuilder.create().uv(87, 39).mirrored(true).cuboid(-2.0F, -10.5F, -2.0F, 4.0F, 11.0F, 4.0F),
				ModelTransform.of(4.0F, -1.5F, 5.0F, 0.7854F, 0.0F, 0.0F));
		ModelPartData tentacleSmall02b = tentacleSmall02a.addChild("tentacleSmall02b",
				ModelPartBuilder.create().uv(105, 40).mirrored(true).cuboid(-2.0F, -4.0F, -1.5F, 3.0F, 5.0F, 3.0F),
				ModelTransform.of(0.5F, -10.5F, 0.0F, -0.4363F, 0.2618F, -0.6109F));
		ModelPartData tentacleSmall02c = tentacleSmall02b.addChild("tentacleSmall02c",
				ModelPartBuilder.create().uv(118, 41).mirrored(true).cuboid(-1.0F, -5.0F, -1.0F, 2.0F, 6.0F, 2.0F),
				ModelTransform.of(-0.5F, -3.0F, 0.0F, 0.3491F, 0.0F, -0.5236F));
		tentacleSmall02c.addChild("tentacleSmall02d",
				ModelPartBuilder.create().uv(120, 42).mirrored(true).cuboid(-0.5F, -4.0F, -0.5F, 1.0F, 5.0F, 1.0F),
				ModelTransform.of(0.0F, -5.0F, 0.0F, 0.0F, 0.0F, -0.829F));
		ModelPartData tentacleSmall03a = tentacles2.addChild("tentacleSmall03a",
				ModelPartBuilder.create().uv(87, 39).mirrored(true).cuboid(-2.0F, -10.5F, -2.0F, 4.0F, 11.0F, 4.0F),
				ModelTransform.of(4.0F, -1.5F, 0.0F, 0.3491F, 0.0F, 0.3927F));
		ModelPartData tentacleSmall03b = tentacleSmall03a.addChild("tentacleSmall03b",
				ModelPartBuilder.create().uv(105, 40).mirrored(true).cuboid(-2.0F, -4.0F, -1.5F, 3.0F, 5.0F, 3.0F),
				ModelTransform.of(0.5F, -10.5F, 0.0F, 0.1745F, 0.2618F, 0.4363F));
		ModelPartData tentacleSmall03c = tentacleSmall03b.addChild("tentacleSmall03c",
				ModelPartBuilder.create().uv(118, 41).mirrored(true).cuboid(-1.0F, -5.0F, -1.0F, 2.0F, 6.0F, 2.0F),
				ModelTransform.of(-0.5F, -3.0F, 0.0F, 0.3491F, 0.0F, -0.5236F));
		tentacleSmall03c.addChild("tentacleSmall03d",
				ModelPartBuilder.create().uv(120, 42).mirrored(true).cuboid(-0.5F, -4.0F, -0.5F, 1.0F, 5.0F, 1.0F),
				ModelTransform.of(0.0F, -5.0F, 0.0F, 0.0F, 0.0F, -0.829F));
		ModelPartData tentacleSmall04a = tentacles2.addChild("tentacleSmall04a",
				ModelPartBuilder.create().uv(87, 39).cuboid(-2.0F, -10.5F, -2.0F, 4.0F, 11.0F, 4.0F),
				ModelTransform.of(-4.0F, -1.5F, 0.0F, 0.3491F, 0.0F, -0.3927F));
		ModelPartData tentacleSmall04b = tentacleSmall04a.addChild("tentacleSmall04b",
				ModelPartBuilder.create().uv(105, 40).cuboid(-1.0F, -4.0F, -1.5F, 3.0F, 5.0F, 3.0F),
				ModelTransform.of(-0.5F, -10.5F, 0.0F, 0.1745F, -0.2618F, -0.4363F));
		ModelPartData tentacleSmall04c = tentacleSmall04b.addChild("tentacleSmall04c",
				ModelPartBuilder.create().uv(118, 41).cuboid(-1.0F, -5.0F, -1.0F, 2.0F, 6.0F, 2.0F),
				ModelTransform.of(0.5F, -3.0F, 0.0F, 0.3491F, 0.0F, 0.5236F));
		tentacleSmall04c.addChild("tentacleSmall04d", ModelPartBuilder.create().uv(120, 42).cuboid(-0.5F, -4.0F, -0.5F
				, 1.0F, 5.0F, 1.0F), ModelTransform.of(0.0F, -5.0F, 0.0F, 0.0F, 0.0F, 0.829F));
		return TexturedModelData.of(data, 128, 64);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green,
					   float blue, float alpha) {
		root.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
