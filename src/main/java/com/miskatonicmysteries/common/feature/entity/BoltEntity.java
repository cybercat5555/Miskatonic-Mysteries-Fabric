package com.miskatonicmysteries.common.feature.entity;

import com.miskatonicmysteries.common.registry.MMEntities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BoltEntity extends Entity {

	protected static final TrackedData<Float> LENGTH = DataTracker.registerData(BoltEntity.class, TrackedDataHandlerRegistry.FLOAT);
	protected static final TrackedData<Integer> COLOR = DataTracker.registerData(BoltEntity.class, TrackedDataHandlerRegistry.INTEGER);
	public long seed;
	public float[] color = {1, 0, 0, 1};
	private int ambientTick;

	public BoltEntity(LivingEntity caster, double length, int color) {
		this(MMEntities.BOLT, caster.world);
		setYaw(caster.getYaw());
		setPitch(caster.getPitch());
		setPos(caster.getX(), caster.getEyeY(), caster.getZ());
		setLength((float) length);
		setColor(color);
	}

	public BoltEntity(EntityType<BoltEntity> entityType, World world) {
		super(entityType, world);
		this.ignoreCameraFrustum = true;
		this.ambientTick = 6;
		this.seed = this.random.nextLong();
		setLength(10);
	}

	public BoltEntity(World world, Vec3d pos, Vec3d direction, int color) {
		this(MMEntities.BOLT, world);
		lookAt(EntityAnchor.FEET, direction);
		setPos(pos.getX(), pos.getY(), pos.getZ());
		setLength((float) direction.length());
		setColor(color);
	}


	public float getLength() {
		return dataTracker.get(LENGTH);
	}

	public void setLength(float length) {
		dataTracker.set(LENGTH, length);
	}

	public int getColor() {
		return dataTracker.get(COLOR);
	}

	public void setColor(int color) {
		dataTracker.set(COLOR, color);
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
			remove(RemovalReason.DISCARDED);
		}
		super.tick();
	}

	@Environment(EnvType.CLIENT)
	public boolean shouldRender(double distance) {
		double d = 64.0D * getRenderDistanceMultiplier();
		return distance < d * d;
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound tag) {

	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound tag) {

	}

	public Packet<?> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this);
	}

}