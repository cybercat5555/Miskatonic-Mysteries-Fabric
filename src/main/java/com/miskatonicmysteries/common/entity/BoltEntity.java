package com.miskatonicmysteries.common.entity;

import com.miskatonicmysteries.common.registry.MMEntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.world.World;

public class BoltEntity extends Entity {
    private int ambientTick;
    public long seed;
    protected static final TrackedData<Float> LENGTH = DataTracker.registerData(BoltEntity.class, TrackedDataHandlerRegistry.FLOAT);
    protected static final TrackedData<Integer> COLOR = DataTracker.registerData(BoltEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public float[] color = {1, 0, 0, 1};

    public BoltEntity(EntityType<BoltEntity> entityType, World world) {
        super(entityType, world);
        this.ignoreCameraFrustum = true;
        this.ambientTick = 6;
        this.seed = this.random.nextLong();
        setLength(10);
    }

    public BoltEntity(LivingEntity caster, double length, int color) {
        this(MMEntities.BOLT, caster.world);
        this.yaw = caster.yaw;
        this.pitch = caster.pitch;
        setPos(caster.getX(), caster.getEyeY(), caster.getZ());
        setLength((float) length);
        setColor(color);
    }

    public void setLength(float length) {
        dataTracker.set(LENGTH, length);
    }

    public float getLength() {
        return dataTracker.get(LENGTH);
    }

    public void setColor(int color) {
        dataTracker.set(COLOR, color);
    }

    public int getColor() {
        return dataTracker.get(COLOR);
    }

    @Override
    protected void initDataTracker() {
        dataTracker.startTracking(LENGTH, 0F);
        dataTracker.startTracking(COLOR, 0);
    }

    @Override
    public void tick() {
        ambientTick--;
        if (ambientTick < 0) {
            remove();
        }
        super.tick();
    }

    @Environment(EnvType.CLIENT)
    public boolean shouldRender(double distance) {
        double d = 64.0D * getRenderDistanceMultiplier();
        return distance < d * d;
    }

    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    protected void writeCustomDataToTag(CompoundTag tag) {

    }

    @Override
    protected void readCustomDataFromTag(CompoundTag tag) {

    }

}