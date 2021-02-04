package com.miskatonicmysteries.common.block.blockentity.energy;

import com.miskatonicmysteries.common.block.blockentity.BaseBlockEntity;
import com.miskatonicmysteries.common.entity.PhantasmaEntity;
import com.miskatonicmysteries.common.feature.interfaces.Resonating;
import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.MMEntities;
import com.miskatonicmysteries.common.lib.MMObjects;
import com.miskatonicmysteries.common.lib.MMParticles;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyStorage;
import team.reborn.energy.EnergyTier;

import java.util.List;

public class ResonatorBlockEntity extends BaseBlockEntity implements Tickable, EnergyStorage {
    public ResonatorBlockEntity() {
        super(MMObjects.RESONATOR_BLOCK_ENTITY_TYPE);
    }

    private static final int MAX_STORED_POWER = 3200;
    private static final int MAX_RADIUS = 16;
    private float radius;
    private float intensity;
    private double energy;
    private int ticksRan;
    private static final int MAX_EFFECTIVE_RUNTIME = 1200;

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putFloat(Constants.NBT.RADIUS, radius);
        tag.putFloat(Constants.NBT.INTENSITY, intensity);
        tag.putDouble(Constants.NBT.ENERGY, energy);
        tag.putInt(Constants.NBT.TICK_COUNT, ticksRan);
        return super.toTag(tag);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        radius = tag.getFloat(Constants.NBT.RADIUS);
        intensity = tag.getFloat(Constants.NBT.INTENSITY);
        energy = tag.getDouble(Constants.NBT.ENERGY);
        ticksRan = tag.getInt(Constants.NBT.TICK_COUNT);
        super.fromTag(state, tag);
    }


    @Override
    public void tick() {
        boolean powered = isPowered();
        //todo still handle some block changes, as well as aberrations
        if (powered) {
            radius = MAX_RADIUS * intensity;
            ticksRan++;
            if (energy < 16) {
                world.setBlockState(pos, getCachedState().with(Properties.POWERED, false));
            } else {
                energy -= 16;
                if (intensity < 1) {
                    intensity += 0.0005F;
                }
                if (world.isClient) {
                    world.addParticle(MMParticles.AMBIENT, pos.getX() + 0.5F + world.random.nextGaussian() * radius, pos.getY() + 0.5F + world.random.nextGaussian() * radius, pos.getZ() + 0.5F + world.random.nextGaussian() * radius, 0.75F, 0, 1);
                }
                if (ticksRan > MAX_EFFECTIVE_RUNTIME / 2 && ticksRan % 240 == 0) {
                    spawnPhantasm();
                }
                List<Entity> affectedEntities = world.getEntitiesByClass(Entity.class, getSelectionBox(), entity -> entity instanceof Resonating);
                for (Entity affectedEntity : affectedEntities) {
                    Resonating.of(affectedEntity).ifPresent(resonating -> {
                        float targetIntensity = getIntensityFromDistance(affectedEntity);
                        if (resonating.getResonance() < targetIntensity) {
                            resonating.setResonance(resonating.getResonance() + targetIntensity / 100F + 0.01F);
                        }
                    });
                }
                markDirty();
            }
        } else if (intensity > 0 || ticksRan > 0) {
            ticksRan--;
            intensity -= 0.001F;
            markDirty();
        }
    }

    private void spawnPhantasm() {
        for (int i = 0; i < 5; i++) {
            Vec3d pos = new Vec3d(getPos().getX() + world.random.nextGaussian() * (radius - 3), getPos().getY() + world.random.nextGaussian() * (radius - 7), getPos().getZ() + world.random.nextGaussian() * (radius - 3));
            if (world.getBlockState(new BlockPos(pos)).isSolidBlock(world, new BlockPos(pos)) && world instanceof ServerWorld) {
                PhantasmaEntity phantasma = MMEntities.PHANTASMA.create(world);
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
}
