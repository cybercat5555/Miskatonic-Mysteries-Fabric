// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

package com.miskatonicmysteries.client.model.entity;

import com.miskatonicmysteries.common.feature.entity.GuardDogEntity;

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
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class GuardDogEntityModel extends EntityModel<GuardDogEntity> {

	private final ModelPart rightHindLeg;
	private final ModelPart leftHindLeg;
	private final ModelPart rightFrontLeg;
	private final ModelPart leftFrontLeg;
	private final ModelPart head;
	private final ModelPart tail;
	private final ModelPart root;

	public GuardDogEntityModel(ModelPart root) {
		super(RenderLayer::getEntitySolid);
		this.root = root;
		ModelPart body = root.getChild("body");
		this.rightHindLeg = body.getChild("rHindLeg01");
		this.leftHindLeg = body.getChild("lHindLeg01");
		this.rightFrontLeg = body.getChild("rForeleg01");
		this.leftFrontLeg = body.getChild("lForeleg01");
		this.head = body.getChild("neck").getChild("head");
		this.tail = body.getChild("tail");

	}

	public static TexturedModelData getTexturedModelData() {
		ModelData data = new ModelData();
		ModelPartData root = data.getRoot();

		ModelPartData body = root
			.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-3.5F, -3.0F, -8.0F, 7.0F, 6.0F, 9.0F, new Dilation(0.0F)),
					  ModelTransform.pivot(0.0F, 11.0F, -1.0F));

		ModelPartData cube_r1 = body
			.addChild("cube_r1", ModelPartBuilder.create().uv(0, 15).cuboid(-3.0F, -3.0F, 0.0F, 6.0F, 5.0F, 8.0F, new Dilation(0.0F)),
					  ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		ModelPartData tail = body
			.addChild("tail", ModelPartBuilder.create().uv(21, 22).cuboid(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 8.0F, new Dilation(0.0F)),
					  ModelTransform.of(0.0F, -3.0F, 8.0F, -0.6109F, 0.0F, 0.0F));

		ModelPartData lForeleg01 = body.addChild("lForeleg01", ModelPartBuilder.create(), ModelTransform.pivot(2.75F, 0.0F, -6.0F));

		ModelPartData cube_r2 = lForeleg01
			.addChild("cube_r2", ModelPartBuilder.create().uv(36, 0).cuboid(-1.0F, -1.0F, -1.0F, 3.0F, 7.0F, 4.0F, new Dilation(0.0F)),
					  ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2182F, 0.0F, 0.0F));

		ModelPartData lForeleg02 = lForeleg01
			.addChild("lForeleg02", ModelPartBuilder.create().uv(36, 12).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F)),
					  ModelTransform.pivot(0.5F, 5.0F, 2.5F));

		ModelPartData lForepaw = lForeleg02
			.addChild("lForepaw", ModelPartBuilder.create().uv(23, 0).cuboid(-1.5F, 0.0F, -2.0F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F)),
					  ModelTransform.pivot(0.0F, 6.0F, 0.0F));

		ModelPartData rForeleg01 = body.addChild("rForeleg01", ModelPartBuilder.create(), ModelTransform.pivot(-2.75F, 0.0F, -6.0F));

		ModelPartData cube_r3 = rForeleg01.addChild("cube_r3", ModelPartBuilder.create().uv(36, 0).mirrored()
														.cuboid(-2.0F, -1.0F, -1.0F, 3.0F, 7.0F, 4.0F, new Dilation(0.0F)).mirrored(false),
													ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2182F, 0.0F, 0.0F));

		ModelPartData rForeleg02 = rForeleg01.addChild("rForeleg02", ModelPartBuilder.create().uv(36, 12).mirrored()
			.cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-0.5F, 5.0F, 2.5F));

		ModelPartData rForepaw = rForeleg02.addChild("rForepaw", ModelPartBuilder.create().uv(23, 0).mirrored()
			.cuboid(-1.5F, 0.0F, -2.0F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 6.0F, 0.0F));

		ModelPartData lHindLeg01 = body
			.addChild("lHindLeg01", ModelPartBuilder.create().uv(50, 0).cuboid(-2.0F, -2.5F, -2.0F, 3.0F, 9.0F, 4.0F, new Dilation(0.0F)),
					  ModelTransform.of(3.5F, -0.25F, 5.0F, 0.0F, -0.0873F, -0.0873F));

		ModelPartData lHindLeg02 = lHindLeg01
			.addChild("lHindLeg02", ModelPartBuilder.create().uv(49, 14).cuboid(-1.0F, -2.5F, -1.0F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F)),
					  ModelTransform.of(-0.5F, 6.25F, 2.0F, -0.1309F, 0.0F, 0.0873F));

		ModelPartData lHindPaw = lHindLeg02
			.addChild("lHindPaw", ModelPartBuilder.create().uv(23, 0).cuboid(-1.5F, -1.0F, -2.0F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F)),
					  ModelTransform.of(0.0F, 6.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		ModelPartData rHindLeg01 = body.addChild("rHindLeg01", ModelPartBuilder.create().uv(50, 0).mirrored()
													 .cuboid(-1.0F, -2.5F, -2.0F, 3.0F, 9.0F, 4.0F, new Dilation(0.0F)).mirrored(false),
												 ModelTransform.of(-3.5F, -0.25F, 5.0F, 0.0F, 0.0873F, 0.0873F));

		ModelPartData rHindLeg02 = rHindLeg01.addChild("rHindLeg02", ModelPartBuilder.create().uv(49, 14).mirrored()
														   .cuboid(-1.0F, -2.5F, -1.0F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F)).mirrored(false),
													   ModelTransform.of(0.5F, 6.25F, 2.0F, -0.1309F, 0.0F, -0.0873F));

		ModelPartData rHindPaw = rHindLeg02.addChild("rHindPaw", ModelPartBuilder.create().uv(23, 0).mirrored()
														 .cuboid(-1.5F, -1.0F, -2.0F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F)).mirrored(false),
													 ModelTransform.of(0.0F, 6.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		ModelPartData bone = body.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData neck = body
			.addChild("neck", ModelPartBuilder.create().uv(24, 34).cuboid(-2.5F, -2.6F, -2.95F, 5.0F, 5.0F, 3.0F, new Dilation(-0.2F)),
					  ModelTransform.of(0.0F, -0.4F, -7.3F, -0.2618F, 0.0F, 0.0F));

		ModelPartData head = neck
			.addChild("head", ModelPartBuilder.create().uv(0, 32).cuboid(-3.0F, -2.4F, -5.0F, 6.0F, 5.0F, 5.0F, new Dilation(0.0F))
						  .uv(0, 43).cuboid(-1.5F, -0.5F, -8.3F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F))
						  .uv(11, 43).cuboid(0.75F, -4.25F, -3.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
						  .uv(18, 44).cuboid(1.25F, -5.25F, -3.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
						  .uv(11, 43).mirrored().cuboid(-2.75F, -4.25F, -3.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
						  .uv(18, 44).mirrored().cuboid(-2.25F, -5.25F, -3.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false),
					  ModelTransform.of(0.0F, -0.1F, -2.0F, 0.2618F, 0.0F, 0.0F));

		return TexturedModelData.of(data, 64, 64);
	}

	@Override
	public void setAngles(GuardDogEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.pitch = headPitch * ((float) Math.PI / 180);
		this.head.yaw = netHeadYaw * ((float) Math.PI / 180);
		this.tail.pitch = -0.6109F;
	}

	@Override
	public void animateModel(GuardDogEntity entity, float limbAngle, float limbDistance, float tickDelta) {
		super.animateModel(entity, limbAngle, limbDistance, tickDelta);
		this.rightHindLeg.pitch = MathHelper.cos(limbAngle * 0.6662f) * 1.2f * limbDistance;
		this.leftHindLeg.pitch = MathHelper.cos(limbAngle * 0.6662f + (float) Math.PI) * 1.2f * limbDistance;
		this.rightFrontLeg.pitch = MathHelper.cos(limbAngle * 0.6662f + (float) Math.PI) * 1.2f * limbDistance;
		this.leftFrontLeg.pitch = MathHelper.cos(limbAngle * 0.6662f) * 1.2f * limbDistance;
	}

	@Override
	public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green,
					   float blue, float alpha) {
		root.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}


	public void setRotationAngle(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}
}