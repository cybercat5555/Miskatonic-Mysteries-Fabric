package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.client.gui.SudokuScreen;
import com.miskatonicmysteries.client.render.RenderHelper;
import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.entity.HasturCultistEntity;
import com.miskatonicmysteries.common.feature.entity.TatteredPrinceEntity;
import com.miskatonicmysteries.common.feature.recipe.RiteRecipe;
import com.miskatonicmysteries.common.feature.recipe.rite.condition.AscensionStageCondition;
import com.miskatonicmysteries.common.feature.recipe.rite.condition.EntityCondition;
import com.miskatonicmysteries.common.feature.recipe.rite.condition.HasturCultistCondition;
import com.miskatonicmysteries.common.feature.recipe.rite.condition.KnotCondition;
import com.miskatonicmysteries.common.feature.recipe.rite.condition.PillarsCondition;
import com.miskatonicmysteries.common.feature.recipe.rite.condition.TatteredPrinceCondition;
import com.miskatonicmysteries.common.handler.ascension.HasturAscensionHandler;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMWorld;
import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class HasturBiomeRite extends BiomeConversionRite {

	public HasturBiomeRite() {
		super(new Identifier(Constants.MOD_ID, "hastur_simulacrum"), MMAffiliations.HASTUR, MMAffiliations.HASTUR,
			  new AscensionStageCondition(MMAffiliations.HASTUR, 3), new KnotCondition(),
			  new PillarsCondition(MMAffiliations.HASTUR), new HasturCultistCondition(3), new TatteredPrinceCondition());
	}

	@Override
	public void tick(OctagramBlockEntity octagram) {
		super.tick(octagram);
		if (octagram.tickCount < 320) {
			HasturCultistCondition.moveCultists(octagram);
			TatteredPrinceCondition.movePrince(octagram);
		}
	}

	@Override
	protected int getRawBiomeId() {
		return BuiltinRegistries.BIOME.getRawId(MMWorld.HASTUR_BIOME);
	}

	@Override
	protected void onCast(OctagramBlockEntity blockEntity, PlayerEntity caster) {
		super.onCast(blockEntity, caster);
		HasturAscensionHandler.levelSimulacrum((ServerPlayerEntity) caster);
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected void openScreen() {
		MinecraftClient.getInstance().setScreen(new SudokuScreen(SudokuScreen.HASTUR_TEXTURE));
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void renderRite(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers,
						   int light, int overlay, BlockEntityRendererFactory.Context context) {
		long time = entity.getWorld().getTime();
		matrixStack.push();
		if (entity.tickCount < 120) {
			Random random = new Random(42069);
			PlayerEntity player = entity.getOriginalCaster();
			Vec3f direction = new Vec3f(0, 10000, 0);
			if (player != null && entity.tickCount > 100) {
				direction = new Vec3f((float) (player.getX() - entity.getSummoningPos().x),
									  (float) (player.getEyeY() - entity.getSummoningPos().y), (float) (player.getZ() - entity.getSummoningPos().z));
			}
			matrixStack.translate(1.5F, 0, 1.5F);
			matrixStack.multiply(Vec3f.POSITIVE_Y
									 .getDegreesQuaternion(
										 entity.getCachedState().get(HorizontalFacingBlock.FACING).getOpposite().asRotation() - 90F));
			VertexConsumer vertices = vertexConsumers.getBuffer(RenderLayer.getLightning());
			matrixStack.translate(-1F, 0, -1F);
			direction.add(-0.75F, 0, 0.75F);
			float[] rgb = {1.0F, 1.0F, 0.0F};
			drawLightCone(matrixStack, vertices, entity.tickCount, 20, time, tickDelta, random, rgb);
			matrixStack.translate(2, 0, 0);
			direction.add(0, 0, -1.5F);
			drawLightCone(matrixStack, vertices, entity.tickCount, 40, time, tickDelta, random, rgb);
			direction.add(1.5F, 0, 0);
			matrixStack.translate(0, 0, 2);
			drawLightCone(matrixStack, vertices, entity.tickCount, 60, time, tickDelta, random, rgb);
			matrixStack.translate(-2, 0, 0);
			direction.add(0, 0, 1.5F);
			drawLightCone(matrixStack, vertices, entity.tickCount, 80, time, tickDelta, random, rgb);
		} else {
			int progressTick = entity.tickCount - 120;
			Sprite centerSprite = ResourceHandler.HASTUR_SIGIL_CENTER.getSprite();
			Sprite innerSprite = ResourceHandler.HASTUR_SIGIL_INNER.getSprite();
			Sprite outerSprite = ResourceHandler.HASTUR_SIGIL_OUTER.getSprite();
			float scale = progressTick < 200 ? (progressTick + tickDelta) / 200F : 1;
			float rotationProgress = (time % 200 + tickDelta) / 200F * 360;
			float translationProgress = MathHelper.sin((time + tickDelta) / 20F);
			matrixStack.translate(1.5F, 0.5F + translationProgress * 0.25F, 1.5F);
			matrixStack.scale(scale, scale, scale);
			matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(rotationProgress));
			matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(10F));
			RenderHelper.renderCenteredTexturedPlane(3, outerSprite, matrixStack,
													 outerSprite
														 .getTextureSpecificVertexConsumer(vertexConsumers.getBuffer(RenderLayer.getCutoutMipped())),
													 15728880, overlay,
													 new float[]{1, 1, 1, 1}, true);
			matrixStack.push();
			matrixStack.translate(0, 0.15F, 0);
			matrixStack.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(rotationProgress * 2));
			RenderHelper.renderCenteredTexturedPlane(3, centerSprite, matrixStack,
													 centerSprite
														 .getTextureSpecificVertexConsumer(vertexConsumers.getBuffer(RenderLayer.getCutoutMipped())),
													 15728880, overlay,
													 new float[]{1, 1, 1, 1}, true);
			matrixStack.pop();
			matrixStack.push();
			matrixStack.translate(0, 0.3F, 0);
			matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(rotationProgress));
			RenderHelper.renderCenteredTexturedPlane(3, innerSprite, matrixStack,
													 innerSprite
														 .getTextureSpecificVertexConsumer(vertexConsumers.getBuffer(RenderLayer.getCutoutMipped())),
													 15728880, overlay,
													 new float[]{1, 1, 1, 1}, true);
			matrixStack.pop();
		}
		matrixStack.pop();
	}
}
