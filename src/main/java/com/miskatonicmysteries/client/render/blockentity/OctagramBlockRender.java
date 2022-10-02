package com.miskatonicmysteries.client.render.blockentity;

import com.miskatonicmysteries.api.block.OctagramBlock;
import com.miskatonicmysteries.api.block.OctagramBlock.BlockOuterOctagram;
import com.miskatonicmysteries.api.item.trinkets.MaskTrinketItem;
import com.miskatonicmysteries.api.registry.Rite;
import com.miskatonicmysteries.client.gui.HudHandler;
import com.miskatonicmysteries.client.render.RenderHelper;
import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.recipe.rite.condition.RiteCondition;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

@Environment(EnvType.CLIENT)
public class OctagramBlockRender extends DrawableHelper implements BlockEntityRenderer<OctagramBlockEntity> {

	public static final Map<Identifier, Sprite> ICON_CACHE = new HashMap<>();
	private final BlockEntityRendererFactory.Context context;

	public OctagramBlockRender(BlockEntityRendererFactory.Context context) {
		this.context = context;
	}

	public static void handleDisplay(MinecraftClient client, MatrixStack stack, float delta) {
		if (client.crosshairTarget instanceof BlockHitResult r) {
			BlockPos pos = r.getBlockPos();
			BlockState block = client.world.getBlockState(pos);
			OctagramBlockEntity octagram = null;
			if (block.getBlock() instanceof OctagramBlock.BlockOuterOctagram) {
				octagram = BlockOuterOctagram.getOctagram(client.world, pos, block);
			} else if (block.getBlock() instanceof OctagramBlock) {
				octagram = (OctagramBlockEntity) client.world.getBlockEntity(pos);
			}

			if (octagram != null && !octagram.clientConditions.isEmpty() && !MaskTrinketItem.getMask(client.player).isEmpty()) {
				renderHUD(client, stack, octagram.preparedRite, octagram.clientConditions);
			}
		}
	}

	private static void renderHUD(MinecraftClient client, MatrixStack matrixStack, Rite rite, LinkedHashMap<RiteCondition, Boolean> conditions) {
		int startX = client.getWindow().getScaledWidth() / 2;
		int centerY = client.getWindow().getScaledHeight() / 2;
		matrixStack.push();
		matrixStack.translate(client.getWindow().getScaledWidth() % 2 != 0 ? 0.5 : 0, client.getWindow().getScaledHeight() % 2 != 0 ? 0.5 : 0, 0);
		drawCenteredText(matrixStack, client.textRenderer, Text.translatable(rite.getTranslationString()), startX, centerY - 14, 0xFFFFFFFF);
		startX -= conditions.size() / 2 * 12;
		centerY += 10;
		matrixStack.translate(startX, centerY, 0);
		for (Entry<RiteCondition, Boolean> entry : conditions.entrySet()) {
			HudHandler.drawIcon(matrixStack, !entry.getValue(), entry.getKey().getIconLocation());
			matrixStack.translate(12, 0, 0);
		}
		matrixStack.pop();
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
