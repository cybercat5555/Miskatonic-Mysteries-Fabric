package com.miskatonicmysteries.common.feature.block.blockentity.energy;

import com.miskatonicmysteries.api.interfaces.Resonating;
import com.miskatonicmysteries.client.sound.ResonatorSound;
import com.miskatonicmysteries.common.feature.block.blockentity.BaseBlockEntity;
import com.miskatonicmysteries.common.feature.entity.PhantasmaEntity;
import com.miskatonicmysteries.common.registry.MMEntities;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.registry.MMParticles;
import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.base.SimpleSidedEnergyContainer;

public class ResonatorBlockEntity extends BaseBlockEntity {

	private static final int MAX_STORED_POWER = 8000;
	private static final int MAX_RADIUS = 16;
	private static final int MAX_EFFECTIVE_RUNTIME = 1200;

	public final SimpleSidedEnergyContainer energyStorage = new SimpleSidedEnergyContainer() {
		@Override
		public long getCapacity() {
			return MAX_STORED_POWER;
		}

		@Override
		public long getMaxInsert(@Nullable Direction side) {
			return side != Direction.UP ? 100 : 0;
		}

		@Override
		public long getMaxExtract(@Nullable Direction side) {
			return 0;
		}

		@Override
		protected void onFinalCommit() {
			markDirty();
		}
	};
	public float intensity;
	public int ticksRan;
	private float radius;

	public ResonatorBlockEntity(BlockPos pos, BlockState state) {
		super(MMObjects.RESONATOR_BLOCK_ENTITY_TYPE, pos, state);
	}

	public static void tick(ResonatorBlockEntity blockEntity) {
		boolean powered = blockEntity.isPowered();
		if (powered) {
			blockEntity.radius = MAX_RADIUS * blockEntity.intensity;
			if(blockEntity.ticksRan < MAX_EFFECTIVE_RUNTIME) {
				blockEntity.ticksRan++;
			}
			if (blockEntity.energyStorage.amount < 40) {
				blockEntity.world.setBlockState(blockEntity.pos, blockEntity.getCachedState().with(Properties.POWERED,
																								   false));
			} else {
				blockEntity.energyStorage.amount -= 40;
				if (blockEntity.intensity < 1) {
					blockEntity.intensity += 0.0005F;
				}
				if (blockEntity.world.isClient) {
					handleSound(blockEntity);
					handleParticles(blockEntity);
				}
				if (blockEntity.ticksRan > MAX_EFFECTIVE_RUNTIME / 2 && blockEntity.ticksRan % 200 == 0) {
					blockEntity.spawnPhantasm();
				}
				List<Entity> affectedEntities = blockEntity.world.getEntitiesByClass(Entity.class,
																					 blockEntity.getSelectionBox(),
																					 entity -> entity instanceof Resonating);
				for (Entity affectedEntity : affectedEntities) {
					Resonating.of(affectedEntity).ifPresent(resonating -> {
						float targetIntensity = blockEntity.getIntensityFromDistance(affectedEntity);
						if (resonating.getResonance() < targetIntensity) {
							resonating.setResonance(resonating.getResonance() + targetIntensity / 100F + 0.01F);
						}
					});
				}
				blockEntity.markDirty();
			}
		} else if (blockEntity.intensity > 0 || blockEntity.ticksRan > 0) {
			blockEntity.ticksRan = Math.max(blockEntity.ticksRan - 1, 0);
			blockEntity.intensity = Math.max(blockEntity.ticksRan - 0.001F, 0);
			blockEntity.markDirty();
		}
	}

	private static void handleParticles(ResonatorBlockEntity blockEntity) {
		float particleChance = blockEntity.intensity;
		if (blockEntity.world.random.nextFloat() < particleChance) {
			if (blockEntity.world.random.nextBoolean()) {
				blockEntity.world.addParticle(MMParticles.RESONATOR_CREATURE,
											  blockEntity.pos.getX() + 0.5F + blockEntity.world.random.nextGaussian() * blockEntity.radius,
											  blockEntity.pos.getY() + 0.5F + blockEntity.world.random.nextGaussian() * blockEntity.radius,
											  blockEntity.pos.getZ() + 0.5F + blockEntity.world.random.nextGaussian() * blockEntity.radius,
											  0, 0, 0);
			} else {
				blockEntity.world.addParticle(MMParticles.AMBIENT,
											  blockEntity.pos.getX() + 0.5F + blockEntity.world.random.nextGaussian() * blockEntity.radius,
											  blockEntity.pos.getY() + 0.5F + blockEntity.world.random.nextGaussian() * blockEntity.radius,
											  blockEntity.pos.getZ() + 0.5F + blockEntity.world.random.nextGaussian() * blockEntity.radius,
											  0.75F, 0, 1);
			}
		}
	}

	@Environment(EnvType.CLIENT)
	public static void handleSound(ResonatorBlockEntity resonator) {
		ResonatorSound.createSound(resonator.getPos());
	}

	private void spawnPhantasm() {
		if (world instanceof ServerWorld) {
			if (world.random.nextFloat() > intensity - 0.1F) {
				return;
			}
			for (int i = 0; i < 8; i++) {
				Vec3d pos = new Vec3d(getPos().getX() + world.random.nextGaussian() * (radius - 3),
									  getPos().getY() + world.random.nextFloat() * radius - 7,
									  getPos().getZ() + world.random.nextGaussian() * (radius - 3));
				BlockPos blockPos = new BlockPos(pos);
				if (!world.getBlockState(blockPos).isSolidBlock(world, blockPos) || world.random.nextFloat() < 0.25F) {
					PhantasmaEntity phantasma = world.getRandom().nextBoolean() ? MMEntities.ABERRATION.create(world)
																				: MMEntities.PHANTASMA.create(world);
					phantasma.refreshPositionAndAngles(pos.getX(), pos.getY(), pos.getZ(), world.random.nextInt(360),
													   0);
					phantasma.initialize((ServerWorld) world, world.getLocalDifficulty(phantasma.getBlockPos()),
										 SpawnReason.SPAWNER, null, null);
					phantasma.setResonance(0.01F);
					world.spawnEntity(phantasma);
					return;
				}
			}
		}
	}

	private float getIntensityFromDistance(Entity affectedEntity) {
		double distance = Math.sqrt(affectedEntity.squaredDistanceTo(pos.getX() + 0.5F, pos.getY() + 0.75F,
																	 pos.getZ() + 0.5F));
		float densityFactor = ticksRan > MAX_EFFECTIVE_RUNTIME ? 1 :
							  (float) Math.min(distance / ((float) ticksRan / MAX_EFFECTIVE_RUNTIME), 1);
		return 1 - (intensity * (densityFactor / radius));
	}

	public boolean isPowered() {
		return getCachedState().get(Properties.POWERED);
	}

	public Box getSelectionBox() {
		return new Box(pos, pos.add(1, 1, 1)).expand(radius);
	}

	@Override
	public void readNbt(NbtCompound tag) {
		radius = tag.getFloat(Constants.NBT.RADIUS);
		intensity = tag.getFloat(Constants.NBT.INTENSITY);
		energyStorage.amount = tag.getLong(Constants.NBT.ENERGY);
		ticksRan = tag.getInt(Constants.NBT.TICK_COUNT);
		super.readNbt(tag);
	}

	@Override
	public void writeNbt(NbtCompound tag) {
		tag.putFloat(Constants.NBT.RADIUS, radius);
		tag.putFloat(Constants.NBT.INTENSITY, intensity);
		tag.putLong(Constants.NBT.ENERGY, energyStorage.amount);
		tag.putInt(Constants.NBT.TICK_COUNT, ticksRan);
	}
}
