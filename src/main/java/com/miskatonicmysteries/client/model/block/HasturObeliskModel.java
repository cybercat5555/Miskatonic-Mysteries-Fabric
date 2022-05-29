package com.miskatonicmysteries.client.model.block;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class HasturObeliskModel extends Model {
	private final ModelPart obelisk;

	public HasturObeliskModel(ModelPart root) {
		super(RenderLayer::getEntityCutout);
		this.obelisk = root.getChild("obelisk");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData data = new ModelData();
		ModelPartData root = data.getRoot();

		ModelPartData obelisk = root.addChild("obelisk", ModelPartBuilder
			.create().uv(0, 45).cuboid(-8.0F, -36.0F, -8.0F, 16.0F, 36.0F, 16.0F, new Dilation(0.0F))
			.uv(0, 15).cuboid(-7.0F, -45.0F, -7.0F, 14.0F, 9.0F, 14.0F, new Dilation(0.0F))
			.uv(0, 0).cuboid(-6.0F, -48.0F, -6.0F, 12.0F, 3.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		obelisk
			.addChild("cube_r1", ModelPartBuilder.create().uv(38, 21).cuboid(-7.0F, -8.0F, -10.5F, 22.0F, 4.0F, 19.0F, new Dilation(0.0F)),
				ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6545F));
		obelisk
			.addChild("cube_r2", ModelPartBuilder.create().uv(38, 20).cuboid(-12.0F, -8.0F, -11.0F, 21.0F, 4.0F, 20.0F, new Dilation(0.0F)),
				ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3927F));
		obelisk
			.addChild("cube_r3", ModelPartBuilder.create().uv(40, 20).cuboid(-10.0F, -6.0F, -12.0F, 20.0F, 4.0F, 20.0F, new Dilation(0.0F)),
				ModelTransform.of(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));
		ModelPartData crownRotator = obelisk.addChild("crownRotator", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -48.0F, 0.0F));
		ModelPartData prongN01 = crownRotator
			.addChild("prongN01", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, -1.0F, 0.0F, 1.5708F, 0.0F));
		prongN01.addChild("cube_r4", ModelPartBuilder.create().uv(80, 0).cuboid(3.0F, -6.0F, -2.0F, 2.0F, 4.0F, 4.0F, new Dilation(0.0F)),
			ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6109F));
		ModelPartData prongN02 = prongN01.addChild("prongN02",
			ModelPartBuilder.create().uv(69, 0).cuboid(-1.5628F, -3.147F, -1.5F, 2.0F, 4.0F, 3.0F, new Dilation(0.1F)),
			ModelTransform.pivot(7.0F, -3.0F, 0.0F));
		ModelPartData prongN03 = prongN02
			.addChild("prongN03", ModelPartBuilder.create(), ModelTransform.of(-1.0F, -4.0F, 0.0F, 0.0F, 0.0F, -0.8727F));
		prongN03.addChild("cube_r5", ModelPartBuilder.create().uv(59, 0).cuboid(-1.0F, -6.0F, -1.5F, 1.0F, 6.0F, 3.0F, new Dilation(0.0F)),
			ModelTransform.of(0.4131F, 1.7608F, 0.0F, 0.0F, 0.0F, -0.3054F));
		ModelPartData prongN04 = prongN03.addChild("prongN04",
			ModelPartBuilder.create().uv(45, 3).cuboid(-3.0668F, 0.1759F, -1.0F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F)),
			ModelTransform.of(0.0F, -6.0F, 0.0F, 0.0F, 0.0F, -0.7854F));
		ModelPartData prongN05 = prongN04.addChild("prongN05", ModelPartBuilder.create(), ModelTransform.pivot(4.0F, 0.0F, 0.0F));
		prongN05.addChild("cube_r6", ModelPartBuilder.create().uv(38, 0).cuboid(-0.5F, 0.0F, -1.0F, 1.0F, 4.0F, 2.0F, new Dilation(0.1F)),
			ModelTransform.of(-3.5199F, 0.3873F, 0.0F, 0.0F, 0.0F, -0.4363F));
		ModelPartData prongE01 = crownRotator
			.addChild("prongE01", ModelPartBuilder.create(), ModelTransform.of(-1.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));
		prongE01.addChild("cube_r7", ModelPartBuilder.create().uv(80, 0).cuboid(3.0F, -6.0F, -2.0F, 2.0F, 4.0F, 4.0F, new Dilation(0.0F)),
			ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6109F));
		ModelPartData prongE02 = prongE01
			.addChild("prongE02", ModelPartBuilder.create().uv(69, 0).cuboid(-1.5628F, -3.147F, -1.5F, 2.0F, 4.0F, 3.0F, new Dilation(0.1F))
					.uv(51, 11).cuboid(-1.5628F, -2.147F, -1.5F, 2.0F, 1.0F, 3.0F, new Dilation(0.2F)),
				ModelTransform.pivot(7.0F, -3.0F, 0.0F));
		prongE02.addChild("ribbonE01", ModelPartBuilder.create().uv(51, 17).cuboid(0.0F, -1.0F, 0.0F, 5.0F, 2.0F, 0.0F, new Dilation(0.0F)),
			ModelTransform.pivot(0.4372F, -1.897F, 0.1F));
		ModelPartData prongE03 = prongE02
			.addChild("prongE03", ModelPartBuilder.create(), ModelTransform.of(-1.0F, -4.0F, 0.0F, 0.0F, 0.0F, -0.8727F));
		prongE03.addChild("cube_r8", ModelPartBuilder.create().uv(59, 0).cuboid(-1.0F, -6.0F, -1.5F, 1.0F, 6.0F, 3.0F, new Dilation(0.0F)),
			ModelTransform.of(0.4131F, 1.7608F, 0.0F, 0.0F, 0.0F, -0.3054F));
		ModelPartData prongE04 = prongE03
			.addChild("prongE04", ModelPartBuilder.create().uv(45, 3).cuboid(-3.0668F, 0.1759F, -1.0F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F))
					.uv(53, 12).cuboid(-2.0668F, 0.1759F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.1F)),
				ModelTransform.of(0.0F, -6.0F, 0.0F, 0.0F, 0.0F, -0.7854F));
		prongE04.addChild("ribbonE02", ModelPartBuilder.create().uv(55, 17).cuboid(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 0.0F, new Dilation(0.0F)),
			ModelTransform.pivot(-1.0668F, 0.6759F, 0.0F));
		ModelPartData prongE05 = prongE04.addChild("prongE05", ModelPartBuilder.create(), ModelTransform.pivot(4.0F, 0.0F, 0.0F));
		prongE05.addChild("cube_r9", ModelPartBuilder.create().uv(38, 0).cuboid(-0.5F, 0.0F, -1.0F, 1.0F, 4.0F, 2.0F, new Dilation(0.1F)),
			ModelTransform.of(-3.5199F, 0.3873F, 0.0F, 0.0F, 0.0F, -0.4363F));
		ModelPartData prongS01 = crownRotator
			.addChild("prongS01", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 1.0F, 0.0F, -1.5708F, 0.0F));
		prongS01.addChild("cube_r10", ModelPartBuilder.create().uv(80, 0).cuboid(3.0F, -6.0F, -2.0F, 2.0F, 4.0F, 4.0F, new Dilation(0.0F)),
			ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6109F));
		ModelPartData prongS02 = prongS01.addChild("prongS02",
			ModelPartBuilder.create().uv(69, 0).cuboid(-1.5628F, -3.147F, -1.5F, 2.0F, 4.0F, 3.0F, new Dilation(0.1F)),
			ModelTransform.pivot(7.0F, -3.0F, 0.0F));
		ModelPartData prongS03 = prongS02
			.addChild("prongS03", ModelPartBuilder.create(), ModelTransform.of(-1.0F, -4.0F, 0.0F, 0.0F, 0.0F, -0.8727F));
		prongS03.addChild("cube_r11", ModelPartBuilder.create().uv(59, 0).cuboid(-1.0F, -6.0F, -1.5F, 1.0F, 6.0F, 3.0F, new Dilation(0.0F)),
			ModelTransform.of(0.4131F, 1.7608F, 0.0F, 0.0F, 0.0F, -0.3054F));
		ModelPartData prongS04 = prongS03.addChild("prongS04",
			ModelPartBuilder.create().uv(45, 3).cuboid(-3.0668F, 0.1759F, -1.0F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F)),
			ModelTransform.of(0.0F, -6.0F, 0.0F, 0.0F, 0.0F, -0.7854F));
		ModelPartData prongS05 = prongS04.addChild("prongS05", ModelPartBuilder.create(), ModelTransform.pivot(4.0F, 0.0F, 0.0F));
		prongS05.addChild("cube_r12", ModelPartBuilder.create().uv(38, 0).cuboid(-0.5F, 0.0F, -1.0F, 1.0F, 4.0F, 2.0F, new Dilation(0.1F)),
			ModelTransform.of(-3.5199F, 0.3873F, 0.0F, 0.0F, 0.0F, -0.4363F));
		ModelPartData prongW01 = crownRotator.addChild("prongW01", ModelPartBuilder.create(), ModelTransform.pivot(1.0F, 0.0F, 0.0F));
		prongW01.addChild("cube_r13", ModelPartBuilder.create().uv(80, 0).cuboid(3.0F, -6.0F, -2.0F, 2.0F, 4.0F, 4.0F, new Dilation(0.0F)),
			ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6109F));
		ModelPartData prongW02 = prongW01.addChild("prongW02",
			ModelPartBuilder.create().uv(69, 0).cuboid(-1.5628F, -3.147F, -1.5F, 2.0F, 4.0F, 3.0F, new Dilation(0.1F)),
			ModelTransform.pivot(7.0F, -3.0F, 0.0F));
		ModelPartData prongW03 = prongW02
			.addChild("prongW03", ModelPartBuilder.create(), ModelTransform.of(-1.0F, -4.0F, 0.0F, 0.0F, 0.0F, -0.8727F));
		prongW03.addChild("cube_r14", ModelPartBuilder.create().uv(52, 11).cuboid(-1.0F, -4.0F, -1.5F, 1.0F, 1.0F, 3.0F, new Dilation(0.1F))
				.uv(59, 0).cuboid(-1.0F, -6.0F, -1.5F, 1.0F, 6.0F, 3.0F, new Dilation(0.0F)),
			ModelTransform.of(0.4131F, 1.7608F, 0.0F, 0.0F, 0.0F, -0.3054F));
		prongW03.addChild("ribbonW", ModelPartBuilder.create().uv(52, 18).cuboid(-4.0F, -0.5F, 0.0F, 4.0F, 1.0F, 0.0F, new Dilation(0.0F)),
			ModelTransform.pivot(-1.5869F, -1.2392F, 0.0F));
		ModelPartData prongW04 = prongW03.addChild("prongW04",
			ModelPartBuilder.create().uv(45, 3).cuboid(-3.0668F, 0.1759F, -1.0F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F)),
			ModelTransform.of(0.0F, -6.0F, 0.0F, 0.0F, 0.0F, -0.7854F));
		ModelPartData prongW05 = prongW04.addChild("prongW05", ModelPartBuilder.create(), ModelTransform.pivot(4.0F, 0.0F, 0.0F));
		prongW05.addChild("cube_r15", ModelPartBuilder.create().uv(38, 0).cuboid(-0.5F, 0.0F, -1.0F, 1.0F, 4.0F, 2.0F, new Dilation(0.1F)),
			ModelTransform.of(-3.5199F, 0.3873F, 0.0F, 0.0F, 0.0F, -0.4363F));
		obelisk
			.addChild("sash01", ModelPartBuilder.create().uv(9, 107).cuboid(-12.0F, -3.0F, -9.0F, 26.0F, 3.0F, 18.0F, new Dilation(0.0F)),
				ModelTransform.of(0.0F, -21.0F, 0.0F, 0.0F, 0.0F, -0.7854F));
		obelisk
			.addChild("sash02", ModelPartBuilder.create().uv(48, 81).cuboid(-10.0F, -2.0F, -8.5F, 20.0F, 2.0F, 17.0F, new Dilation(0.0F)),
				ModelTransform.of(0.0F, -27.0F, 0.0F, 0.0F, 0.0F, 0.5236F));
		ModelPartData cloak01a = obelisk
			.addChild("cloak01a", ModelPartBuilder.create().uv(0, 98).cuboid(-5.5F, 0.0F, -0.5F, 11.0F, 7.0F, 1.0F, new Dilation(0.0F)),
				ModelTransform.of(7.0F, -45.0F, -1.25F, -0.4363F, -1.5708F, 0.0F));
		ModelPartData cloak01b = cloak01a
			.addChild("cloak01b", ModelPartBuilder.create().uv(0, 108).cuboid(-6.0F, 0.0F, -0.5F, 12.0F, 11.0F, 1.0F, new Dilation(0.0F)),
				ModelTransform.of(0.0F, 7.0F, 0.0F, 0.3491F, 0.0F, 0.0F));
		ModelPartData cloak01c = cloak01b
			.addChild("cloak01c", ModelPartBuilder.create().uv(102, 22).cuboid(-6.0F, 0.0F, -0.5F, 12.0F, 11.0F, 1.0F, new Dilation(0.0F)),
				ModelTransform.of(0.0F, 11.0F, 0.0F, 0.1309F, 0.0F, 0.0F));
		cloak01c
			.addChild("cloak01d", ModelPartBuilder.create().uv(102, 46).cuboid(-6.0F, 0.0F, -0.5F, 12.0F, 8.0F, 1.0F, new Dilation(0.0F)),
				ModelTransform.of(0.0F, 11.0F, 0.0F, -0.1309F, 0.0F, 0.0F));
		ModelPartData cloak03a = obelisk
			.addChild("cloak03a", ModelPartBuilder.create().uv(0, 98).cuboid(-5.5F, 0.0F, -0.5F, 11.0F, 7.0F, 1.0F, new Dilation(0.0F)),
				ModelTransform.of(-6.25F, -46.0F, -0.25F, -0.3491F, 1.5708F, 0.0F));
		ModelPartData cloak03b = cloak03a
			.addChild("cloak03b", ModelPartBuilder.create().uv(102, 22).cuboid(-6.0F, 0.0F, -0.5F, 12.0F, 11.0F, 1.0F, new Dilation(0.0F)),
				ModelTransform.of(0.0F, 6.75F, 0.0F, 0.3491F, 0.0F, 0.0F));
		cloak03b
			.addChild("cloak03c", ModelPartBuilder.create().uv(102, 46).cuboid(-6.0F, 0.0F, -0.5F, 12.0F, 8.0F, 1.0F, new Dilation(0.0F)),
				ModelTransform.of(0.0F, 11.0F, 0.0F, -0.1309F, 0.0F, 0.0F));
		ModelPartData cloak04a = obelisk
			.addChild("cloak04a", ModelPartBuilder.create().uv(0, 98).cuboid(-5.5F, 0.0F, -0.5F, 11.0F, 7.0F, 1.0F, new Dilation(0.0F)),
				ModelTransform.of(2.75F, -44.0F, -7.25F, -0.48F, 0.0F, 0.0F));
		ModelPartData cloak04b = cloak04a
			.addChild("cloak04b", ModelPartBuilder.create().uv(0, 108).cuboid(-6.0F, 0.0F, -0.5F, 12.0F, 8.0F, 1.0F, new Dilation(0.0F)),
				ModelTransform.of(0.0F, 6.75F, 0.0F, 0.4363F, 0.0F, 0.0F));
		ModelPartData cloak04c = cloak04b
			.addChild("cloak04c", ModelPartBuilder.create().uv(102, 22).cuboid(-6.0F, 0.0F, -0.5F, 12.0F, 8.0F, 1.0F, new Dilation(0.0F)),
				ModelTransform.pivot(0.0F, 8.0F, 0.0F));
		cloak04c
			.addChild("cloak04d", ModelPartBuilder.create().uv(102, 46).cuboid(-6.0F, 0.0F, -0.5F, 12.0F, 8.0F, 1.0F, new Dilation(0.0F)),
				ModelTransform.of(0.0F, 8.0F, 0.0F, -0.1309F, 0.0F, 0.0F));
		ModelPartData cloak02a = obelisk
			.addChild("cloak02a", ModelPartBuilder.create().uv(67, 48).cuboid(-5.5F, 0.0F, -0.5F, 11.0F, 7.0F, 11.0F, new Dilation(0.0F)),
				ModelTransform.of(7.25F, -44.0F, -3.0F, 0.0F, -1.5708F, 0.0F));
		ModelPartData cloak02b = cloak02a
			.addChild("cloak02b", ModelPartBuilder.create().uv(65, 47).cuboid(-6.0F, 0.0F, -0.5F, 12.0F, 13.0F, 12.0F, new Dilation(0.0F)),
				ModelTransform.pivot(-1.75F, 7.0F, -2.5F));
		ModelPartData cloak02c = cloak02b
			.addChild("cloak02c", ModelPartBuilder.create().uv(80, 100).cuboid(-6.0F, 0.0F, -0.5F, 12.0F, 13.0F, 12.0F, new Dilation(0.0F)),
				ModelTransform.pivot(0.0F, 13.0F, 0.0F));
		cloak02c
			.addChild("cloak02d", ModelPartBuilder.create().uv(80, 0).cuboid(-6.0F, 0.0F, -0.5F, 12.0F, 8.0F, 12.0F, new Dilation(0.0F)),
				ModelTransform.pivot(0.0F, 13.0F, 0.0F));

		return TexturedModelData.of(data, 128, 128);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue,
		float alpha) {
		obelisk.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
