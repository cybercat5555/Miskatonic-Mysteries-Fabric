package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.client.gui.HasturSudokuScreen;
import com.miskatonicmysteries.client.render.RenderHelper;
import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.client.vision.VisionHandler;
import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.entity.HasturCultistEntity;
import com.miskatonicmysteries.common.feature.entity.TatteredPrinceEntity;
import com.miskatonicmysteries.common.feature.world.MMDimensionalWorldState;
import com.miskatonicmysteries.common.feature.world.MMDimensionalWorldState.BiomeKnot;
import com.miskatonicmysteries.common.feature.world.biome.BiomeEffect;
import com.miskatonicmysteries.common.handler.ProtagonistHandler;
import com.miskatonicmysteries.common.handler.ascension.HasturAscensionHandler;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.SyncBiomeSpreadPacket;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMParticles;
import com.miskatonicmysteries.common.registry.MMWorld;
import com.miskatonicmysteries.common.util.Constants;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class HasturBiomeRite extends BiomeConversionRite {

	private static final int RADIUS = 64;
	private static final float HALF_SQRT_3 = (float) (Math.sqrt(3.0) / 2.0);

	public HasturBiomeRite() {
		super(new Identifier(Constants.MOD_ID, "hastur_biome"), MMAffiliations.HASTUR,
			(world) -> world.getRegistryManager().get(Registry.BIOME_KEY).getEntry(BuiltinRegistries.BIOME.getRawId(MMWorld.HASTUR_BIOME)),
			MMAffiliations.HASTUR.getId().getPath(), 3,
			Ingredient.ofItems(Items.EMERALD));
	}

	@Override
	public boolean canCast(OctagramBlockEntity octagram) {
		PlayerEntity caster = octagram.getOriginalCaster();
		if (octagram.getOriginalCaster() == null) {
			return false;
		}
		if (caster.isCreative()) {
			return true;
		}
		World world = octagram.getWorld();
		List<BiomeKnot> nearbyKnots = MMDimensionalWorldState.get((ServerWorld) world).getNearbyKnots(octagram.getPos(), 64)
			.stream().filter(knot -> knot.isActive() && knot.isCore()).collect(Collectors.toList());
		if (!nearbyKnots.isEmpty()) {
			caster.sendMessage(new TranslatableText("message.miskatonicmysteries.rite_fail.knots"), true);
			return false;
		}
		if (!octagram.checkPillars(MMAffiliations.HASTUR)) {
			caster.sendMessage(new TranslatableText("message.miskatonicmysteries.rite_fail.pillars"), true);
			return false;
		}
		List<HasturCultistEntity> cultists = world.getEntitiesByClass(HasturCultistEntity.class,
			octagram.getSelectionBox().expand(15, 5, 15), entity -> true);
		if (cultists.size() < 3) {
			caster.sendMessage(new TranslatableText("message.miskatonicmysteries.rite_fail.serfs"), true);
			return false;
		}

		List<TatteredPrinceEntity> princes = world.getEntitiesByClass(TatteredPrinceEntity.class,
			octagram.getSelectionBox().expand(8, 5, 8), entity -> true);
		if (princes.size() < 1) {
			caster.sendMessage(new TranslatableText("message.miskatonicmysteries.rite_fail.prince"), true);
			return false;
		}
		return super.canCast(octagram);
	}

	@Override
	public boolean shouldContinue(OctagramBlockEntity octagram) {
		if (octagram.tickCount < 320) {
			PlayerEntity caster = octagram.getOriginalCaster();
			if (caster == null || caster.getPos().distanceTo(octagram.getSummoningPos()) > 16 || caster.isDead()) {
				return false;
			}
		}
		if (octagram.getWorld().getTime() % 60 == 0 && !octagram.checkPillars(MMAffiliations.HASTUR)) {
			return false;
		}
		return super.shouldContinue(octagram);
	}

	@Override
	public void tick(OctagramBlockEntity octagram) {
		World world = octagram.getWorld();
		Random random = octagram.getWorld().getRandom();
		if (octagram.getWorld().getTime() % 60 == 0 && !octagram.checkPillars(MMAffiliations.HASTUR)) {
			octagram.setOriginalCaster(null);
			onCancelled(octagram);
			return;
		}
		if (octagram.tickCount < 320) {
			PlayerEntity caster = octagram.getOriginalCaster();
			Vec3d pos = octagram.getSummoningPos();
			List<HasturCultistEntity> cultists = octagram.getWorld().getEntitiesByClass(HasturCultistEntity.class,
				octagram.getSelectionBox().expand(15, 5, 15), cultist -> !cultist.isAttacking());
			for (HasturCultistEntity cultist : cultists) {
				cultist.getNavigation().startMovingTo(pos.x, pos.y, pos.z, 0.8F);
				if (cultist.getPos().distanceTo(pos) < 5) {
					cultist.getNavigation().stop();
					cultist.currentSpell = null;
					cultist.setCastTime(20);
				}
			}
			List<TatteredPrinceEntity> princes = octagram.getWorld().getEntitiesByClass(TatteredPrinceEntity.class,
				octagram.getSelectionBox().expand(8, 5, 8), prince -> !prince.isAttacking());
			for (TatteredPrinceEntity prince : princes) {
				prince.getNavigation().startMovingTo(pos.x, pos.y, pos.z, 0.8F);
				prince.lookAtEntity(octagram.getOriginalCaster(), 180, 45);
				if (octagram.tickCount < 20) {
					prince.startBlessing();
				}
				if (prince.getPos().distanceTo(pos) < 5) {
					prince.getNavigation().stop();
				}
			}
			if (octagram.tickCount < 100) {
				octagram.tickCount++;
			} else if (!octagram.getFlag(1)) {
				double rad = random.nextDouble() * Math.PI * 2;
				world.addParticle(MMParticles.AMBIENT_MAGIC, pos.x + Math.sin(rad) * 1.5F, pos.y, pos.z + Math.cos(rad) * 1.5F,
					-Math.sin(rad) * random.nextFloat() * 0.05,
					0F, -Math.cos(rad) * random.nextFloat() * 0.05F);
				if (caster.getPos().distanceTo(pos) < 1) {
					octagram.tickCount = MathHelper.clamp(octagram.tickCount + 1, 100, 119);
					Vec3d motionVec = new Vec3d(pos.x - caster.getX(), pos.y - caster.getY(), pos.z - caster.getZ());
					motionVec = motionVec.multiply(0.25F);
					caster.setVelocity(motionVec);
					caster.velocityModified = true;
					if (caster instanceof ClientPlayerEntity client) {
						if (octagram.tickCount == 101) {
							VisionHandler
								.setVisionSequence(client, VisionHandler.getSequence(new Identifier(Constants.MOD_ID, "fade_to_black")));
						} else if (octagram.tickCount == 110) {
							MinecraftClient.getInstance().setScreen(new HasturSudokuScreen());
						}
					}else if (caster instanceof ServerPlayerEntity server) {
						if (random.nextFloat() < 0.001f) {
							if (!ProtagonistHandler.spawnProtagonist(server.getWorld(), server) && random.nextBoolean()) {
								ProtagonistHandler.spawnProtagonistReinforcements(server.getWorld(), server);
							}
						}
					}
				} else {
					octagram.tickCount = 100;
				}
			} else {
				octagram.tickCount++;
				if (octagram.tickCount == 120) {
					if (octagram.getWorld() instanceof ServerWorld serverWorld) {
						HasturAscensionHandler.levelSimulacrum((ServerPlayerEntity) caster);
						MMDimensionalWorldState worldState = MMDimensionalWorldState.get(serverWorld);
						List<BiomeKnot> knots = worldState.getNearbyKnots(octagram.getPos(), 32)
							.stream().filter(biomeKnot -> biomeKnot.isCore() && !biomeKnot.isActive())
							.sorted(Comparator.comparingDouble(a -> a.getPos().getSquaredDistance(pos))).collect(Collectors.toList());
						boolean knotChanged = false;
						for (BiomeKnot knot : knots) {
							BiomeEffect effect = MiskatonicMysteriesAPI.getBiomeEffect(world, knot.getPos());
							if (effect != null && effect.getAffiliation(false) == MMAffiliations.HASTUR) {
								worldState.setBiomeKnot(knot.getPos(), 64, true, true);
								knotChanged = true;
								break;
							}
						}
						if (!knotChanged) {
							worldState.setBiomeKnot(octagram.getPos(), RADIUS, true, true);
							biomeSupplier.apply(world).ifPresent(biome -> {
								spreadBiome(world, octagram.getPos(), RADIUS / 4, biome);
								int biomeId = BuiltinRegistries.BIOME.getRawId(MMWorld.HASTUR_BIOME);
								PlayerLookup.tracking(serverWorld, octagram.getPos()).forEach(serverPlayer ->
									SyncBiomeSpreadPacket.send(serverPlayer, octagram.getPos(), biomeId, RADIUS / 4));
							});
						}
					}
				}
			}
		} else {
			octagram.tickCount = 320;
		}
	}

	@Override
	public void onCancelled(OctagramBlockEntity octagram) {
		super.onCancelled(octagram);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void renderRite(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers,
		int light, int overlay, BlockEntityRendererFactory.Context context) {
		long time = entity.getWorld().getTime();
		if (entity.tickCount < 120) {
			Random random = new Random(42069);
			PlayerEntity player = entity.getOriginalCaster();
			Vec3f direction = new Vec3f(0, 10000, 0);
			if (player != null && entity.tickCount > 100) {
				direction = new Vec3f((float) (player.getX() - entity.getSummoningPos().x),
					(float) (player.getEyeY() - entity.getSummoningPos().y), (float) (player.getZ() - entity.getSummoningPos().z));
			}
			matrixStack.push();
			matrixStack.translate(1.5F, 0, 1.5F);
			matrixStack.multiply(Vec3f.POSITIVE_Y
				.getDegreesQuaternion(entity.getCachedState().get(HorizontalFacingBlock.FACING).getOpposite().asRotation() - 90F));
			VertexConsumer vertices = vertexConsumers.getBuffer(RenderLayer.getLightning());
			matrixStack.translate(-1F, 0, -1F);
			direction.add(-0.75F, 0, 0.75F);
			drawLightCone(matrixStack, vertices, entity.tickCount, 20, time, tickDelta, random);
			matrixStack.translate(2, 0, 0);
			direction.add(0, 0, -1.5F);
			drawLightCone(matrixStack, vertices, entity.tickCount, 40, time, tickDelta, random);
			direction.add(1.5F, 0, 0);
			matrixStack.translate(0, 0, 2);
			drawLightCone(matrixStack, vertices, entity.tickCount, 60, time, tickDelta, random);
			matrixStack.translate(-2, 0, 0);
			direction.add(0, 0, 1.5F);
			drawLightCone(matrixStack, vertices, entity.tickCount, 80, time, tickDelta, random);
			matrixStack.pop();
		} else {
			int progressTick = entity.tickCount - 120;
			Sprite centerSprite = ResourceHandler.HASTUR_SIGIL_CENTER.getSprite();
			Sprite innerSprite = ResourceHandler.HASTUR_SIGIL_INNER.getSprite();
			Sprite outerSprite = ResourceHandler.HASTUR_SIGIL_OUTER.getSprite();
			matrixStack.push();
			float scale = progressTick < 200 ? (progressTick + tickDelta) / 200F : 1;
			float rotationProgress = (time % 200 + tickDelta) / 200F * 360;
			float translationProgress = MathHelper.sin((time + tickDelta) / 20F);
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

	@Environment(EnvType.CLIENT)
	private void drawLightCone(MatrixStack matrixStack, VertexConsumer vertices, int ticks, int startTick, long time, float tickDelta,
		Random random) {
		if (ticks >= startTick) {
			matrixStack.push();
			Matrix4f matrix = matrixStack.peek().getPositionMatrix();
			matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(5));
			matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(random.nextFloat() + time + tickDelta));
			float alpha = 0.55F - 0.45F * MathHelper.sin((time + tickDelta + startTick) / 20F);
			vertices.vertex(matrix, 0.0f, 0.0f, 0.0f).color(1.0F, 1.0F, 1.0F, alpha).next();
			vertices.vertex(matrix, -HALF_SQRT_3, 6, -0.5f).color(1.0F, 1.0F, 0, 0).next();
			vertices.vertex(matrix, HALF_SQRT_3, 6, -0.5f).color(1.0F, 1.0F, 0, 0).next();
			vertices.vertex(matrix, 0.0f, 0.0f, 0.0f).color(1.0F, 1.0F, 1.0F, alpha).next();
			vertices.vertex(matrix, HALF_SQRT_3, 6, -0.5f).color(1.0F, 1.0F, 0, 0).next();
			vertices.vertex(matrix, 0.0f, 6, 1).color(1.0F, 1.0F, 0, 0).next();
			vertices.vertex(matrix, 0.0f, 0.0f, 0.0f).color(1.0F, 1.0F, 1.0F, alpha).next();
			vertices.vertex(matrix, 0.0f, 6, 1).color(1.0F, 1.0F, 0, 0).next();
			matrixStack.pop();
		}
	}
}
