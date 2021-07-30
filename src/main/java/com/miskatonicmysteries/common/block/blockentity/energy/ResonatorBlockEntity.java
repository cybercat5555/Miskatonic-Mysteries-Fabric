package com.miskatonicmysteries.common.block.blockentity.energy;

import com.miskatonicmysteries.api.interfaces.Resonating;
import com.miskatonicmysteries.client.sound.ResonatorSound;
import com.miskatonicmysteries.common.block.blockentity.BaseBlockEntity;
import com.miskatonicmysteries.common.entity.PhantasmaEntity;
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
import net.minecraft.util.math.Vec3d;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyStorage;
import team.reborn.energy.EnergyTier;

import java.util.List;

public class ResonatorBlockEntity extends BaseBlockEntity implements EnergyStorage {
    public ResonatorBlockEntity(BlockPos pos, BlockState state) {
        super(MMObjects.RESONATOR_BLOCK_ENTITY_TYPE, pos, state);
    }

    private static final int MAX_STORED_POWER = 3200;
    private static final int MAX_RADIUS = 16;
    private float radius;
    public float intensity;
    private double energy;
    public int ticksRan;
    private static final int MAX_EFFECTIVE_RUNTIME = 1200;

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        tag.putFloat(Constants.NBT.RADIUS, radius);
        tag.putFloat(Constants.NBT.INTENSITY, intensity);
        tag.putDouble(Constants.NBT.ENERGY, energy);
        tag.putInt(Constants.NBT.TICK_COUNT, ticksRan);
        return super.writeNbt(tag);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        radius = tag.getFloat(Constants.NBT.RADIUS);
        intensity = tag.getFloat(Constants.NBT.INTENSITY);
        energy = tag.getDouble(Constants.NBT.ENERGY);
        ticksRan = tag.getInt(Constants.NBT.TICK_COUNT);
        super.readNbt(tag);
    }

    public static void tick(ResonatorBlockEntity blockEntity) {
        boolean powered = blockEntity.isPowered();
        if (powered) {
            blockEntity.radius = MAX_RADIUS * blockEntity.intensity;
            blockEntity.ticksRan++;
            if (blockEntity.energy < 16) {
                blockEntity.world.setBlockState(blockEntity.pos, blockEntity.getCachedState().with(Properties.POWERED, false));
            } else {
                blockEntity.energy -= 16;
                if (blockEntity.intensity < 1) {
                    blockEntity.intensity += 0.0005F;
                }
                if (blockEntity.world.isClient) {
                    handleSound(blockEntity);
                    blockEntity.world.addParticle(MMParticles.AMBIENT,
                            blockEntity.pos.getX() + 0.5F + blockEntity.world.random.nextGaussian() * blockEntity.radius,
                            blockEntity.pos.getY() + 0.5F + blockEntity.world.random.nextGaussian() * blockEntity.radius,
                            blockEntity.pos.getZ() + 0.5F + blockEntity.world.random.nextGaussian() * blockEntity.radius,
                            0.75F, 0, 1);
                }
                if (blockEntity.ticksRan > MAX_EFFECTIVE_RUNTIME / 2 && blockEntity.ticksRan % 240 == 0) {
                    blockEntity.spawnPhantasm();
                }
                List<Entity> affectedEntities = blockEntity.world.getEntitiesByClass(Entity.class, blockEntity.getSelectionBox(), entity -> entity instanceof Resonating);
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
            blockEntity.ticksRan--;
            blockEntity.intensity -= 0.001F;
            blockEntity.markDirty();
        }
    }

    private void spawnPhantasm() {
        for (int i = 0; i < 10; i++) {
            Vec3d pos = new Vec3d(getPos().getX() + world.random.nextGaussian() * (radius - 3), getPos().getY() + world.random.nextGaussian() * (radius - 7), getPos().getZ() + world.random.nextGaussian() * (radius - 3));
            if (world.getBlockState(new BlockPos(pos)).isSolidBlock(world, new BlockPos(pos)) && world instanceof ServerWorld) {
                PhantasmaEntity phantasma = world.getRandom().nextBoolean() ? MMEntities.ABERRATION.create(world) : MMEntities.PHANTASMA.create(world);
                phantasma.refreshPositionAndAngles(pos.getX(), pos.getY(), pos.getZ(), world.random.nextInt(360), 0);
                phantasma.initialize((ServerWorld) world, world.getLocalDifficulty(phantasma.getBlockPos()), SpawnReason.SPAWNER, null, null);
                phantasma.setResonance(0.01F);
                world.spawnEntity(phantasma);
                return;
            }
        }
    }

    private float getIntensityFromDistance(Entity affectedEntity) {
        double distance = Math.sqrt(affectedEntity.squaredDistanceTo(pos.getX() + 0.5F, pos.getY() + 0.75F, pos.getZ() + 0.5F));
        float densityFactor = ticksRan > MAX_EFFECTIVE_RUNTIME ? 1 : (float) Math.min(distance / (ticksRan / MAX_EFFECTIVE_RUNTIME), 1);
        return 1 - (intensity * (densityFactor / radius));
    }

    public boolean isPowered() {
        return getCachedState().get(Properties.POWERED);
    }

    public Box getSelectionBox() {
        return new Box(pos, pos.add(1, 1, 1)).expand(radius);
    }

    @Override
    public double getStored(EnergySide face) {
        return energy;
    }

    @Override
    public void setStored(double amount) {
        this.energy = amount;
    }

    @Override
    public double getMaxStoredPower() {
        return MAX_STORED_POWER;
    }

    @Override
    public EnergyTier getTier() {
        return EnergyTier.LOW;
    }

    @Override
    public double getMaxInput(EnergySide side) {
        return side != EnergySide.UP ? EnergyTier.LOW.getMaxInput() : 0;
    }

    @Override
    public double getMaxOutput(EnergySide side) {
        return 0;
    }

    @Environment(EnvType.CLIENT)
    public static void handleSound(ResonatorBlockEntity resonator) {
        ResonatorSound.createSound(resonator.getPos());
    }
}
