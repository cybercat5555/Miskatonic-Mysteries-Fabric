package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.entity.HarrowEntity;
import com.miskatonicmysteries.common.feature.entity.HasturCultistEntity;
import com.miskatonicmysteries.common.feature.entity.brain.HasturCultistBrain;
import com.miskatonicmysteries.common.feature.recipe.rite.condition.ReverseBiomeCondition;
import com.miskatonicmysteries.common.feature.world.MMDimensionalWorldState;
import com.miskatonicmysteries.common.feature.world.MMDimensionalWorldState.BiomeKnot;
import com.miskatonicmysteries.common.feature.world.biome.BiomeEffect;
import com.miskatonicmysteries.common.handler.ascension.HasturAscensionHandler;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.SyncBiomeReversionPacket;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMParticles;
import com.miskatonicmysteries.common.util.BiomeUtil;
import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeCoords;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.util.math.random.Random;
import java.util.stream.Collectors;

public class BiomeReversionRite extends BiomeConversionRite {

	public BiomeReversionRite() {
		super(new Identifier(Constants.MOD_ID, "revert_biome"), MMAffiliations.YOG, null, new ReverseBiomeCondition());
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void renderRite(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers,
						   int light, int overlay, BlockEntityRendererFactory.Context context) {
		long time = entity.getWorld().getTime();
		if (entity.tickCount < 120) {
			Random random = Random.create(42069);
			PlayerEntity player = entity.getOriginalCaster();
			Vec3f direction = new Vec3f(0, 10000, 0);
			if (player != null && entity.tickCount > 100) {
				direction = new Vec3f((float) (player.getX() - entity.getSummoningPos().x),
									  (float) (player.getEyeY() - entity.getSummoningPos().y), (float) (player.getZ() - entity.getSummoningPos().z));
			}
			matrixStack.push();
			matrixStack.translate(1.5F, 0, 1.5F);
			matrixStack.multiply(Vec3f.POSITIVE_Y
									 .getDegreesQuaternion(
										 entity.getCachedState().get(HorizontalFacingBlock.FACING).getOpposite().asRotation() - 90F));
			VertexConsumer vertices = vertexConsumers.getBuffer(RenderLayer.getLightning());
			matrixStack.translate(-1F, 0, -1F);
			direction.add(-0.75F, 0, 0.75F);
			float[] rgb = {0.5F, 0.5F, 1.0F};
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
			matrixStack.pop();
		}
	}

	@Override
	protected void handleSaboteurs(OctagramBlockEntity octagram, ServerPlayerEntity caster) {
		World world = caster.getWorld();
		if (caster.getWorld().getTime() % 60 == 0) {
			BiomeEffect effect = MiskatonicMysteriesAPI.getBiomeEffect(world, octagram.getPos());
			if (effect != null) {
				Affiliation localAffiliation = effect.getAffiliation(false);
				List<Entity> entities = world.getOtherEntities(caster, caster.getBoundingBox().expand(16, 10, 16),
															   entity -> entity instanceof MobEntity);
				for (Entity entity : entities) {
					if (entity instanceof MobEntity m && (MiskatonicMysteriesAPI.getNonNullAffiliation(m, false) == localAffiliation
						|| (m instanceof HarrowEntity harrow && !harrow.summoned))) {
						m.setTarget(caster);
						if (m instanceof HasturCultistEntity cultist) {
							HasturCultistBrain.onAttacked(cultist, caster);
						}
					}
				}
			}
		}
	}

	@Override
	protected void spread(ServerWorld world, OctagramBlockEntity octagram, MMDimensionalWorldState worldState, PlayerEntity caster) {
		List<BiomeKnot> knots = worldState.getNearbyKnots(octagram.getPos(), 0).stream()
			.filter(knot -> knot.isCore() && !knot.isActive()) //only inactive cores can be cleared
			.sorted(Comparator.comparingDouble(knot -> knot.getPos().getSquaredDistance(octagram.getSummoningPos()))).collect(Collectors.toList());
		if (!knots.isEmpty()) {
			BiomeKnot knot = knots.get(0);
			revertBiome(world, knot.getPos(), knot.getRadius());
			SyncBiomeReversionPacket.send(world, knot.getPos(), knot.getRadius());
			worldState.setBiomeKnot(knot.getPos(), -1, false, false, null);
		}
	}

	public static void revertBiome(World world, BlockPos root, int radius) {
		double radiusPower = Math.pow(radius, 2);
		List<BlockPos> changedBlocks = new ArrayList<>();
		int biomeX = BiomeCoords.fromBlock(root.getX());
		int biomeY = BiomeCoords.fromBlock(root.getY());
		int biomeZ = BiomeCoords.fromBlock(root.getZ());
		for (int x = -radius; x < radius; x++) {
			for (int z = -radius; z < radius; z++) {
				for (int y = -radius; y < radius; y++) {
					BlockPos changedPos = root.add(x * 4, y * 4, z * 4);
					double sqD = x * x + y * y + z * z;
					if (sqD <= radiusPower) {
						BiomeUtil.setBiome(world, world.getWorldChunk(changedPos), biomeX + x, biomeY + y, biomeZ + z,
										   world.getGeneratorStoredBiome(biomeX + x, biomeY + y, biomeZ + z));
						changedBlocks.add(changedPos);
					}
				}
			}
		}
		BiomeUtil.updateBiomeColor(world, changedBlocks);
	}

	@Override
	protected int getRawBiomeId() {
		return -1;
	}

	@Override
	protected void onCast(OctagramBlockEntity blockEntity, PlayerEntity caster) {
		super.onCast(blockEntity, caster);
		HasturAscensionHandler.levelSimulacrum((ServerPlayerEntity) caster);
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected void handleParticles(World world, Random random, Vec3d pos) {
		double rad = random.nextDouble() * Math.PI * 2;
		world.addParticle(MMParticles.AMBIENT_MAGIC, pos.x + Math.sin(rad) * 1.5F, pos.y, pos.z + Math.cos(rad) * 1.5F,
						  Math.sin(rad) * random.nextFloat() * 0.05,
						  0F, Math.cos(rad) * random.nextFloat() * 0.05F);

	}

	@Override
	public boolean isFinished(OctagramBlockEntity octagram) {
		return octagram.tickCount > 300;
	}

	@Override
	public void onCancelled(OctagramBlockEntity octagram) {
		super.onCancelled(octagram);
	}

	@Override
	public boolean isPermanent(OctagramBlockEntity octagram) {
		return false;
	}
}
