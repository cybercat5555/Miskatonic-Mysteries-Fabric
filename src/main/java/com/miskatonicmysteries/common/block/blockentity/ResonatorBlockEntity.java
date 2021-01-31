package com.miskatonicmysteries.common.block.blockentity;

import com.miskatonicmysteries.common.feature.interfaces.Resonating;
import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.MMObjects;
import com.miskatonicmysteries.common.lib.MMParticles;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Box;

import java.util.List;

public class ResonatorBlockEntity extends BaseBlockEntity implements Tickable {
    public ResonatorBlockEntity() {
        super(MMObjects.RESONATOR_BLOCK_ENTITY_TYPE);
    }

    private static final int MAX_RADIUS = 16;
    private float radius;
    private float intensity;

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putFloat(Constants.NBT.RADIUS, radius);
        tag.putFloat(Constants.NBT.INTENSITY, intensity);
        return super.toTag(tag);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        radius = tag.getFloat(Constants.NBT.RADIUS);
        intensity = tag.getFloat(Constants.NBT.INTENSITY);
        super.fromTag(state, tag);
    }


    @Override
    public void tick() {
        boolean powered = isPowered();
        radius = MAX_RADIUS * intensity;
        if (powered) {
            if (intensity < 1) {
                intensity += 0.0005F;
            }
            if (world.isClient) {
                world.addParticle(MMParticles.AMBIENT, pos.getX() + 0.5F + world.random.nextGaussian() * radius, pos.getY() + 0.5F + world.random.nextGaussian() * radius, pos.getZ() + 0.5F + world.random.nextGaussian() * radius, 0.75F, 0, 1);
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
        } else if (intensity > 0) {
            intensity -= 0.01F;
        }
    }

    private float getIntensityFromDistance(Entity affectedEntity) {
        double distance = Math.sqrt(affectedEntity.squaredDistanceTo(pos.getX() + 0.5F, pos.getY() + 0.75F, pos.getZ() + 0.5F));
        return distance < radius / 4F ? intensity : 1 - (float) (intensity * (distance / radius));
    }

    public boolean isPowered() {
        return getCachedState().get(Properties.POWERED);
    }

    public Box getSelectionBox() {
        return new Box(pos, pos.add(1, 1, 1)).expand(radius);
    }
}
