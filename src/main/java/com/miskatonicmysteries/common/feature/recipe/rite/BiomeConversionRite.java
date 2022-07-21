package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.client.gui.SudokuScreen;
import com.miskatonicmysteries.client.vision.VisionHandler;
import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.recipe.RiteRecipe;
import com.miskatonicmysteries.common.feature.world.MMDimensionalWorldState;
import com.miskatonicmysteries.common.feature.world.MMDimensionalWorldState.BiomeKnot;
import com.miskatonicmysteries.common.feature.world.biome.BiomeEffect;
import com.miskatonicmysteries.common.handler.ProtagonistHandler;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.SyncBiomeSpreadPacket;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMParticles;
import com.miskatonicmysteries.common.registry.MMSounds;
import com.miskatonicmysteries.common.util.BiomeUtil;
import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeCoords;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

public abstract class BiomeConversionRite extends AscensionLockedRite {
	private static final int RANGE = 16;
	protected @Nullable Affiliation knotAffiliation;

	public BiomeConversionRite(Identifier id, @Nullable Affiliation octagram, String knowledge, int stage, @Nullable Affiliation knotAffiliation,
							   Ingredient... ingredients) {
		super(id, octagram, knowledge, 0, stage, ingredients);
		this.knotAffiliation = knotAffiliation;
	}

	@Override
	public void onStart(OctagramBlockEntity octagram) {
		octagram.permanentRiteActive = true;
		octagram.clear();
		octagram.markDirty();
	}

	@Override
	public void tick(OctagramBlockEntity octagram) {
		World world = octagram.getWorld();
		Random random = octagram.getWorld().getRandom();
		if (octagram.tickCount < 320) {
			PlayerEntity caster = octagram.getOriginalCaster();
			Vec3d pos = octagram.getSummoningPos();
			if (octagram.tickCount > 0 && octagram.tickCount < 100 && octagram.tickCount % 20 == 0) {
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), MMSounds.RITE_SPOTLIGHT, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
			}
			if (octagram.tickCount < 100) {
				octagram.tickCount++;
			} else if (!octagram.getFlag(1)) {
				if (world.isClient) {
					handleParticles(world, random, pos);
				}
				if (caster.getPos().distanceTo(pos) < 1) {
					octagram.tickCount = MathHelper.clamp(octagram.tickCount + 1, 100, 119);
					Vec3d motionVec = new Vec3d(pos.x - caster.getX(), pos.y - caster.getY(), pos.z - caster.getZ());
					motionVec = motionVec.multiply(0.25F);
					caster.setVelocity(motionVec);
					caster.velocityModified = true;
					if (world.isClient) {
						if (octagram.tickCount == 101) {
							VisionHandler.setVisionSequence((ClientPlayerEntity) caster,
															VisionHandler.getSequence(new Identifier(Constants.MOD_ID, "fade_to_black")));
						} else if (octagram.tickCount == 110) {
							openScreen();
						}
					} else {
						handleSaboteurs(octagram, (ServerPlayerEntity) caster);
					}
				} else {
					octagram.tickCount = 100;
				}
			} else {
				octagram.tickCount++;
				if (octagram.tickCount == 120) {
					if (octagram.getWorld() instanceof ServerWorld serverWorld) {
						onCast(octagram, caster);
						MMDimensionalWorldState worldState = MMDimensionalWorldState.get(serverWorld);
						List<BiomeKnot> knots = worldState.getNearbyKnots(octagram.getPos(), RANGE * 2)
							.stream().filter(biomeKnot -> biomeKnot.isCore() && !biomeKnot.isActive())
							.sorted(Comparator.comparingDouble(a -> a.getPos().getSquaredDistance(pos))).collect(Collectors.toList());
						boolean knotChanged = false;
						if (knotAffiliation != null) {
							for (BiomeKnot knot : knots) {
								BiomeEffect effect = MiskatonicMysteriesAPI.getBiomeEffect(world, knot.getPos());
								if (effect != null && effect.getAffiliation(false) == MMAffiliations.HASTUR) {
									worldState.setBiomeKnot(knot.getPos(), 64, true, true, knotAffiliation);
									knotChanged = true;
									break;
								}
							}
						}
						if (!knotChanged) {
							spread((ServerWorld) world, octagram, worldState, caster);
						}
					}
				}
			}
		} else {
			octagram.tickCount = 320;
		}
	}

	protected void handleSaboteurs(OctagramBlockEntity octagram, ServerPlayerEntity caster) {
		Random random = caster.getRandom();
		if (random.nextFloat() < 0.001f) {
			if (!ProtagonistHandler.spawnProtagonist(caster.getWorld(), caster) && random.nextBoolean()) {
				ProtagonistHandler.spawnProtagonistReinforcements(caster.getWorld(), caster);
			}
		}
	}

	protected void spread(ServerWorld world, OctagramBlockEntity octagram, MMDimensionalWorldState worldState, PlayerEntity caster) {
		worldState.setBiomeKnot(octagram.getPos(), RANGE * 4, true, true, knotAffiliation);
		int rawBiomeId = getRawBiomeId();
		world.getRegistryManager().get(Registry.BIOME_KEY).getEntry(rawBiomeId).ifPresent(biome -> {
			spreadBiome(world, octagram.getPos(), RANGE, biome);
			SyncBiomeSpreadPacket.send(world, octagram.getPos(), rawBiomeId, RANGE);
		});
	}

	protected abstract int getRawBiomeId();

	public static void spreadBiome(World world, BlockPos root, int radius, RegistryEntry<Biome> biome) {
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
						BiomeUtil.setBiome(world, world.getWorldChunk(changedPos), biomeX + x, biomeY + y, biomeZ + z, biome);
						changedBlocks.add(changedPos);
					}
				}
			}
		}
		BiomeUtil.updateBiomeColor(world, changedBlocks);
	}

	protected void onCast(OctagramBlockEntity octagram, PlayerEntity caster) {

	}

	@Environment(EnvType.CLIENT)
	protected void handleParticles(World world, Random random, Vec3d pos) {
		double rad = random.nextDouble() * Math.PI * 2;
		world.addParticle(MMParticles.AMBIENT_MAGIC, pos.x + Math.sin(rad) * 1.5F, pos.y, pos.z + Math.cos(rad) * 1.5F,
						  -Math.sin(rad) * random.nextFloat() * 0.05,
						  0F, -Math.cos(rad) * random.nextFloat() * 0.05F);
	}

	@Environment(EnvType.CLIENT)
	protected void openScreen() {
		MinecraftClient.getInstance().setScreen(new SudokuScreen(SudokuScreen.NORMAL_TEXTURE));
	}

	@Override
	public boolean isFinished(OctagramBlockEntity octagram) {
		return false;
	}

	@Override
	public void onCancelled(OctagramBlockEntity octagram) {
		super.onCancelled(octagram);
		if (octagram.getWorld() instanceof ServerWorld serverWorld) {
			MMDimensionalWorldState.get(serverWorld).setBiomeKnot(octagram.getPos(), RANGE * 4, false, true, knotAffiliation);
		}
	}

	@Override
	public boolean isPermanent(OctagramBlockEntity octagram) {
		return true;
	}

	@Override
	public boolean shouldContinue(OctagramBlockEntity octagram) {
		if (octagram.getWorld().getTime() % 60 == 0 && !checkPillars(octagram)) {
			return false;
		}
		if (octagram.tickCount < 120) {
			PlayerEntity caster = octagram.getOriginalCaster();
			if (caster == null || caster.getPos().distanceTo(octagram.getSummoningPos()) > 16 || caster.isDead()) {
				return false;
			}
		}
		return super.shouldContinue(octagram);
	}

	protected boolean checkPillars(OctagramBlockEntity octagram) {
		return octagram.checkPillars(knotAffiliation);
	}

	@Override
	public boolean canCast(OctagramBlockEntity octagram, RiteRecipe baseRecipe) {
		PlayerEntity caster = octagram.getOriginalCaster();
		if (octagram.getOriginalCaster() == null) {
			return false;
		}
		if (caster.isCreative()) {
			return true;
		}
		World world = octagram.getWorld();
		if (knotAffiliation != null) {
			List<BiomeKnot> nearbyKnots = MMDimensionalWorldState.get((ServerWorld) world).getNearbyKnots(octagram.getPos(), 64)
				.stream().filter(knot -> knot.isActive() && knot.isCore()).collect(Collectors.toList());
			if (!nearbyKnots.isEmpty()) {
				caster.sendMessage(new TranslatableText("message.miskatonicmysteries.rite_fail.knots"), true);
				return false;
			}
		}
		if (!checkPillars(octagram)) {
			caster.sendMessage(new TranslatableText("message.miskatonicmysteries.rite_fail.pillars"), true);
			return false;
		}
		return super.canCast(octagram, baseRecipe);
	}

	@Environment(EnvType.CLIENT)
	protected void drawLightCone(MatrixStack matrixStack, VertexConsumer vertices, int ticks, int startTick, long time, float tickDelta,
								 Random random, float[] rgb) {
		if (ticks >= startTick) {
			matrixStack.push();
			Matrix4f matrix = matrixStack.peek().getPositionMatrix();
			matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(5));
			matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(random.nextFloat() + time + tickDelta));
			float alpha = 0.55F - 0.45F * MathHelper.sin((time + tickDelta + startTick) / 20F);
			vertices.vertex(matrix, 0.0f, 0.0f, 0.0f).color(1.0F, 1.0F, 1.0F, alpha).next();
			vertices.vertex(matrix, -1.73205080757F, 6, -0.5f).color(rgb[0], rgb[1], rgb[2], 0).next();
			vertices.vertex(matrix, 1.73205080757F, 6, -0.5f).color(rgb[0], rgb[1], rgb[2], 0).next();
			vertices.vertex(matrix, 0.0f, 0.0f, 0.0f).color(1.0F, 1.0F, 1.0F, alpha).next();
			vertices.vertex(matrix, 1.73205080757F, 6, -0.5f).color(rgb[0], rgb[1], rgb[2], 0).next();
			vertices.vertex(matrix, 0.0f, 6, 1).color(rgb[0], rgb[1], rgb[2], 0).next();
			vertices.vertex(matrix, 0.0f, 0.0f, 0.0f).color(1.0F, 1.0F, 1.0F, alpha).next();
			vertices.vertex(matrix, 0.0f, 6, 1).color(rgb[0], rgb[1], rgb[2], 0).next();
			matrixStack.pop();
		}
	}

}
