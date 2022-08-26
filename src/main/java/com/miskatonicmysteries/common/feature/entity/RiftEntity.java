package com.miskatonicmysteries.common.feature.entity;

import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.EffectParticlePacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.SpawnCubesPacket;
import com.miskatonicmysteries.common.registry.MMEntities;
import com.miskatonicmysteries.common.registry.MMRegistries;
import com.miskatonicmysteries.common.registry.MMSounds;
import com.miskatonicmysteries.common.registry.MMSpellEffects;
import com.miskatonicmysteries.common.registry.MMSpellMediums;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.Constants.NBT;
import com.miskatonicmysteries.common.util.Constants.Tags;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class RiftEntity extends Entity {
	public RiftType riftType;


	public RiftEntity(EntityType<RiftEntity> entityType, World world) {
		super(entityType, world);
		riftType = RiftType.values()[world.random.nextInt(RiftType.values().length)];
	}

	@Override
	public void tick() {
		if (!world.isClient && (age + getId()) % 5 == 0 && random.nextFloat() < riftType.chance) {
			if (riftType.action.test(this)) {
				world.playSound(null, getX(), getY(), getZ(), MMSounds.SPELL_SPELL_CAST, SoundCategory.NEUTRAL, MathHelper.nextGaussian(random, 0.6F, 0.2F),
								MathHelper.nextGaussian(random, 1, 0.2F));
			}
		}
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound tag) {
		tag.putInt(Constants.NBT.RIFT_TYPE, riftType.ordinal());
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound tag) {
		if (tag.contains(NBT.RIFT_TYPE)) {
			this.riftType = RiftType.values()[tag.getInt(Constants.NBT.RIFT_TYPE)];
		}
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this, riftType.ordinal());
	}

	@Override
	public void onSpawnPacket(EntitySpawnS2CPacket packet) {
		super.onSpawnPacket(packet);
		this.riftType = RiftType.values()[packet.getEntityData()];
	}

	@Override
	protected void initDataTracker() {
		//no-op
	}

	private static boolean potionRiftEffect(RiftEntity rift) {
		World world = rift.world;
		Random random = rift.random;
		StatusEffect effect = Registry.STATUS_EFFECT.getEntryList(Tags.RIFT_EFFECTS)
			.map(named -> named.get(random.nextInt(named.size())).value())
			.orElse(StatusEffects.WITHER);
		List<Entity> targets = world.getOtherEntities(rift, rift.getBoundingBox().expand(2, 2, 2), entity -> entity instanceof LivingEntity);
		for (Entity target : targets) {
			((LivingEntity) target).addStatusEffect(new StatusEffectInstance(effect, effect == StatusEffects.LEVITATION ? 80 :
																					 20 * (5 + random.nextInt(10)), random.nextInt(2)));
		}
		EffectParticlePacket.send(rift, effect.getColor(), 10);
		return true;
	}

	private static boolean spellRiftEffect(RiftEntity rift) {
		World world = rift.world;
		Random random = rift.random;
		int amount = 1 + random.nextInt(3);
		for (int i = 0; i < amount; i++) {
			BoltEntity bolt = MMEntities.BOLT.create(world);
			SpellEffect effect = MMRegistries.SPELL_EFFECTS.getEntryList(Tags.RIFT_SPELLS)
				.map(named -> named.get(random.nextInt(named.size())).value())
				.orElse(MMSpellEffects.DAMAGE);
			bolt.setPosition(rift.getPos().add(0, 0.125F, 0));
			float yaw = 20 * random.nextInt(18);
			int pitch = 45 - 10 * random.nextInt(9);
			bolt.setYaw(yaw);
			bolt.setPitch(pitch);
			bolt.setColor(effect.getColor(null));
			Vec3d boltPos = bolt.getCameraPosVec(1);
			Vec3d directionVec = rift.getRotationVector(pitch, yaw);
			Vec3d endVec = boltPos.add(directionVec.x * 5, directionVec.y * 5, directionVec.z * 5);
			HitResult blockHit = world
				.raycast(new RaycastContext(boltPos, endVec, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, bolt));
			List<Entity> targets = world.getOtherEntities(rift, rift.getBoundingBox().expand(5, 5, 5), entity -> entity instanceof LivingEntity);
			for (Entity target : targets) {
				if (rift.checkBoltHit(directionVec, (LivingEntity) target) && target.squaredDistanceTo(bolt) < blockHit.squaredDistanceTo(rift)) {
					effect.effect(world, (LivingEntity) target, target, target.getPos(), MMSpellMediums.BOLT, 1, null);
				}
			}
			bolt.setLength(5);
			world.spawnEntity(bolt);
		}
		return true;
	}

	private boolean checkBoltHit(Vec3d boltVec, LivingEntity living) {
		Vec3d directionVec = living.getPos().subtract(getPos()).normalize();
		double angle = Math.toDegrees(Math.acos(directionVec.dotProduct(boltVec)));
		return angle < 65 - 30 * distanceTo(living) / 5;
	}

	private static boolean entityRiftEffect(RiftEntity rift) {
		World world = rift.world;
		Random random = rift.random;
		EntityType<? extends Entity> entityType = Registry.ENTITY_TYPE.getEntryList(Tags.RIFT_ENTITIES)
			.map(named -> named.get(random.nextInt(named.size())).value()).orElse(null);
		if (entityType != null) {
			Entity entity = entityType.create(world);
			entity.setPosition(rift.getPos());
			entity.setYaw(random.nextInt(360));
			world.spawnEntity(entity);
			SpawnCubesPacket.send(entity);
			return true;
		}
		return false;
	}

	public enum RiftType {
		POTION(0.03F, RiftEntity::potionRiftEffect, 0.1F, 0.8F, 0.2F, 1F),
		SPELL(0.08F, RiftEntity::spellRiftEffect, 0.95F, 0.74F, 0.26F, 1F),
		ENTITY(0.03F, RiftEntity::entityRiftEffect, 0.3F, 0.38F, 0.65F, 1F);

		public final float chance;
		public final Predicate<RiftEntity> action;
		public final float[] rgba;
		RiftType(float chance, Predicate<RiftEntity> action, float... rgba) {
			this.chance = chance;
			this.action = action;
			this.rgba = rgba;
		}
	}
}