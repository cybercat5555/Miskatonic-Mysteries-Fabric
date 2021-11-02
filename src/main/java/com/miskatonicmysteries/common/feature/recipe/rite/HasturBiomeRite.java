package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.client.render.RenderHelper;
import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMWorld;
import com.miskatonicmysteries.common.util.Constants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;

public class HasturBiomeRite extends BiomeConversionRite {

	public HasturBiomeRite() {
		super(new Identifier(Constants.MOD_ID, "hastur_biome"), MMAffiliations.HASTUR,
			(world) -> world.getRegistryManager().get(Registry.BIOME_KEY).get(BuiltinRegistries.BIOME.getId(MMWorld.HASTUR_BIOME)), "", 3,
			Ingredient.ofItems(Items.EMERALD));
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void renderRite(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers,
		int light, int overlay, BlockEntityRendererFactory.Context context) {
		Sprite centerSprite = ResourceHandler.HASTUR_SIGIL_CENTER.getSprite();
		Sprite innerSprite = ResourceHandler.HASTUR_SIGIL_INNER.getSprite();
		Sprite outerSprite = ResourceHandler.HASTUR_SIGIL_OUTER.getSprite();
		matrixStack.push();
		float scale = entity.tickCount < 200 ? (entity.tickCount + tickDelta) / 200F : 1;
		float rotationProgress = ((entity.tickCount % 200) + tickDelta) / 200F * 360;
		float translationProgress = MathHelper.sin((entity.tickCount + tickDelta) / 20F);
		matrixStack.translate(1.5F, 0.5F + translationProgress * 0.25F, 1.5F);
		matrixStack.scale(scale, scale, scale);
		matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(rotationProgress));
		matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(10F));
		RenderHelper.renderCenteredTexturedPlane(3, outerSprite, matrixStack,
			outerSprite.getTextureSpecificVertexConsumer(vertexConsumers.getBuffer(RenderLayer.getCutoutMipped())), 15728880, overlay,
			new float[]{1, 1, 1, 1}, true);
		matrixStack.push();
		matrixStack.translate(0, 0.15F, 0);
		matrixStack.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(rotationProgress * 2));
		RenderHelper.renderCenteredTexturedPlane(3, centerSprite, matrixStack,
			centerSprite.getTextureSpecificVertexConsumer(vertexConsumers.getBuffer(RenderLayer.getCutoutMipped())), 15728880, overlay,
			new float[]{1, 1, 1, 1}, true);
		matrixStack.pop();
		matrixStack.push();
		matrixStack.translate(0, 0.3F, 0);
		matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(rotationProgress));
		RenderHelper.renderCenteredTexturedPlane(3, innerSprite, matrixStack,
			innerSprite.getTextureSpecificVertexConsumer(vertexConsumers.getBuffer(RenderLayer.getCutoutMipped())), 15728880, overlay,
			new float[]{1, 1, 1, 1}, true);
		matrixStack.pop();
		matrixStack.pop();
	}
}
