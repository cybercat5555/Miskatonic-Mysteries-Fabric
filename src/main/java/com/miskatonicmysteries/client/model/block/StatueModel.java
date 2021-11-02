package com.miskatonicmysteries.client.model.block;

import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class StatueModel extends Model {

	public final ModelPart root;
	public final ModelPart body;
	public final ModelPart plinth;
	public final ModelPart head;

	public StatueModel(Function<Identifier, RenderLayer> factory, ModelPart root, ModelPart body, ModelPart plinth, ModelPart head) {
		super(factory);
		this.root = root;
		this.body = body;
		this.plinth = plinth;
		this.head = head;
	}

	public StatueModel(Function<Identifier, RenderLayer> factory, ModelPart root) {
		this(factory, root, root.getChild("body"), root.getChild("plinth"), root.getChild("body").getChild("head"));
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue,
		float alpha) {
		root.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
