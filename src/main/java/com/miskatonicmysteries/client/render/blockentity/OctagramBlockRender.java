package com.miskatonicmysteries.client.render.blockentity;

import com.miskatonicmysteries.client.render.RenderHelper;
import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.recipe.rite.condition.HasturCultistCondition;
import com.miskatonicmysteries.common.feature.recipe.rite.condition.ReverseBiomeCondition;
import com.miskatonicmysteries.common.feature.recipe.rite.condition.RiteCondition;
import com.miskatonicmysteries.common.feature.recipe.rite.condition.TatteredPrinceCondition;
import com.miskatonicmysteries.common.registry.MMSpellMediums;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

@Environment(EnvType.CLIENT)
public class OctagramBlockRender extends DrawableHelper implements BlockEntityRenderer<OctagramBlockEntity> {

	public static final Map<Identifier, Sprite> ICON_CACHE = new HashMap<>();
	private final BlockEntityRendererFactory.Context context;

	public OctagramBlockRender(BlockEntityRendererFactory.Context context) {
		this.context = context;
	}

	@Override
	public void render(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers,
					   int light, int overlay) {
		Sprite sprite = ResourceHandler.getOctagramTextureFor(entity).getSprite();
		VertexConsumer buffer = sprite.getTextureSpecificVertexConsumer(vertexConsumers.getBuffer(RenderLayer.getCutoutMipped()));
		matrixStack.push();
		Direction direction = entity.getCachedState().get(HorizontalFacingBlock.FACING);
		byte overrideRender = entity.currentRite != null ? entity.currentRite
			.beforeRender(entity, tickDelta, matrixStack, vertexConsumers, light, overlay, context) : 3;
		matrixStack.push();
		matrixStack.translate(0.5, 0, 0.5);
		matrixStack.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(direction.getOpposite().asRotation()));
		matrixStack.translate(-1.5, 0.001, -1.5);

		if ((overrideRender & 1) == 1) {
			RenderHelper.renderTexturedPlane(3, sprite, matrixStack, buffer, light, overlay, new float[]{1, 1, 1, 1});
		}

		if (entity.currentRite != null) {
			entity.currentRite.renderRite(entity, tickDelta, matrixStack, vertexConsumers, light, overlay, context);
		}
		matrixStack.pop();
		matrixStack.translate(0.5F, 0, 0.5F);
		if ((overrideRender >> 1 & 1) == 1) {
			renderItems(entity, vertexConsumers, matrixStack, light);

		}
		if (entity.currentRite != null) {
			entity.currentRite.renderRiteItems(entity, tickDelta, matrixStack, vertexConsumers, light, overlay, context);
		}
		matrixStack.pop();

		matrixStack.push();
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null && !entity.clientConditions.isEmpty()) {
			renderConditions(entity.clientConditions, tickDelta, matrixStack, player);
		}
		matrixStack.pop();
	}

	private void renderConditions(LinkedHashMap<RiteCondition, Boolean> conditionMap, float tickDelta, MatrixStack matrixStack, ClientPlayerEntity player) {
		int number = conditionMap.size();
		matrixStack.translate(0.5,  0, 0.5);
		matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
		matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(player.getYaw(tickDelta)));
		double anglePer = Math.PI * 2.0 / number * 0.225;
		double angle = -(number - 1) / 2.0 *  anglePer + Math.PI;
		for (Entry<RiteCondition, Boolean> entry : conditionMap.entrySet()) {
			double x = Math.sin(angle) * 2;
			double y = Math.cos(angle) * 2;
			matrixStack.push();
			matrixStack.translate(x, y, 0);
			matrixStack.multiply(Vec3f.NEGATIVE_X.getDegreesQuaternion(player.getPitch(tickDelta)));
			drawIcon(matrixStack, entry.getValue(), entry.getKey().getIconLocation());
			matrixStack.pop();
			angle += anglePer;
		}
	}

	private void renderConditionsOld(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, ClientPlayerEntity player) {
		Vec3d cameraVec = player.getRotationVecClient().normalize();
		Vec3d cameraPos = player.getCameraPosVec(tickDelta);
		matrixStack.translate(0.5F, 1, 0.5F);
		matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
		int number = 4;
		matrixStack.push();
		float fraction = 1F / number;
		float cycle = (tickDelta + entity.getWorld().getTime()) / 60F;
		for (int i = 0; i < 8; i++) {
			float p = fraction * i * MathHelper.TAU + cycle;
			float x = MathHelper.sin(p);
			float z = MathHelper.cos(p);
			Vec3d posVec = new Vec3d(entity.getPos().getX() - x + 0.5, entity.getPos().getY() + 1, entity.getPos().getZ() + z + 0.5);
			Vec3d directionVec = posVec.subtract(cameraPos).normalize();
			double angle = Math.toDegrees(Math.acos(directionVec.dotProduct(cameraVec)));
			boolean selected = posVec.isInRange(cameraPos, 2) && angle < 10;
			matrixStack.push();
			matrixStack.translate(x, 0, z);
			matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(player.getYaw(tickDelta)));
			matrixStack.multiply(Vec3f.NEGATIVE_X.getDegreesQuaternion(player.getPitch(tickDelta)));
			drawIcon(matrixStack, selected, MMSpellMediums.SELF.getTextureLocation());
			matrixStack.pop();
		}
		matrixStack.pop();
	}

	private static void drawIcon(MatrixStack matrixStack, boolean selected, Identifier texture) {
		Matrix4f matrix = matrixStack.peek().getPositionMatrix();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		BufferBuilder bufferBuilder =  MinecraftClient.getInstance().getBufferBuilders().getBlockBufferBuilders().get(RenderLayer.getCutoutMipped());
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		RenderSystem.setShaderTexture(0, texture);
		float alpha = selected ? 1 : 0.25F;
		RenderSystem.setShaderColor(1, 1, 1, alpha);
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		bufferBuilder.vertex(matrix, -0.25F, 0.25F, 0).texture(0, 1).next();
		bufferBuilder.vertex(matrix, 0.25F, 0.25F, 0).texture(1, 1).next();
		bufferBuilder.vertex(matrix, 0.25F, -0.25F, 0).texture(1, 0).next();
		bufferBuilder.vertex(matrix, -0.25F, -0.25F, 0).texture(0, 0).next();
		bufferBuilder.end();
		BufferRenderer.draw(bufferBuilder);
	}
	public static void renderItems(OctagramBlockEntity entity, VertexConsumerProvider vertexConsumers, MatrixStack matrixStack, int light) {
		int seed = (int) entity.getPos().asLong();
		for (int i = 0; i < entity.size(); i++) {
			matrixStack.push();
			matrixStack.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(0.125F * i * 360F));
			matrixStack.translate(0, 0, -1.1);
			matrixStack.multiply(Vec3f.NEGATIVE_X.getDegreesQuaternion(90));
			MinecraftClient.getInstance().getItemRenderer()
				.renderItem(entity.getStack(i), ModelTransformation.Mode.GROUND, light, OverlayTexture.DEFAULT_UV, matrixStack,
							vertexConsumers, seed);
			matrixStack.pop();
		}
	}
}
