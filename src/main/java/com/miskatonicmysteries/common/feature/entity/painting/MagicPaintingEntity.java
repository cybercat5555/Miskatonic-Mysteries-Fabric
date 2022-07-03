package com.miskatonicmysteries.common.feature.entity.painting;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.interfaces.HasConfigurablePredicate;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.handler.networking.packet.c2s.SyncConfigurationPacket;
import com.miskatonicmysteries.common.handler.predicate.ConfigurablePredicate;
import com.miskatonicmysteries.common.handler.predicate.ConfigurableTargetPredicate;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.Constants.NBT;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.decoration.painting.PaintingMotive;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

public abstract class MagicPaintingEntity extends PaintingEntity implements Affiliated, HasConfigurablePredicate {

	protected static final TrackedData<Optional<UUID>> OWNER_UUID = DataTracker
		.registerData(MagicPaintingEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
	protected static final TrackedData<ConfigurablePredicate> CONFIG_DATA = DataTracker
		.registerData(MagicPaintingEntity.class, MiskatonicMysteriesAPI.CONFIG_TRACKER);

	public MagicPaintingEntity(EntityType<? extends MagicPaintingEntity> entityType, World world) {
		super(entityType, world);
	}

	public MagicPaintingEntity(EntityType<? extends MagicPaintingEntity> entityType, World world, BlockPos pos, Direction direction,
							   PaintingMotive motive, PlayerEntity owner) {
		this(entityType, world);
		this.motive = motive;
		this.attachmentPos = pos;
		setFacing(direction);
		setPos(attachmentPos.getX(), attachmentPos.getY(), attachmentPos.getZ());
		setOwnerUuid(owner.getUuid());
		setPredicate(buildDefaultPredicate());
	}

	protected ConfigurableTargetPredicate buildDefaultPredicate() {
		ConfigurableTargetPredicate p = new ConfigurableTargetPredicate();
		UUID ownerUUID = getOwnerUuid();
		if (ownerUUID != null) {
			PlayerEntity owner = world.getPlayerByUuid(getOwnerUuid());
			if (owner != null) {
				p.addPlayer(owner.getGameProfile().getId(), owner.getGameProfile().getName());
			}
		}
		return p;
	}

	public @Nullable UUID getOwnerUuid() {
		return this.dataTracker.get(OWNER_UUID).orElse(null);
	}

	public void setOwnerUuid(@Nullable UUID uuid) {
		this.dataTracker.set(OWNER_UUID, uuid == null ? Optional.empty() : Optional.of(uuid));
	}

	public boolean isOwner(PlayerEntity player) {
		return dataTracker.get(OWNER_UUID).map(uuid -> player.getUuid().equals(uuid)).orElse(false);
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(OWNER_UUID, Optional.empty());
		this.dataTracker.startTracking(CONFIG_DATA, buildDefaultPredicate());
	}

	@Override
	public Affiliation getAffiliation(boolean apparent) {
		return MMAffiliations.HASTUR;
	}

	@Override
	public boolean isSupernatural() {
		return true;
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		this.dataTracker.get(OWNER_UUID).ifPresent(uuid -> {
			nbt.putUuid(NBT.OWNER, uuid);
		});
		nbt.put(NBT.PREDICATE, getPredicate().writeNbt(new NbtCompound()));
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		if (nbt.contains(NBT.OWNER)) {
			setOwnerUuid(nbt.getUuid(NBT.OWNER));
		}
		setPredicate(nbt.contains(NBT.PREDICATE) ? ConfigurablePredicate.fromNbt(nbt.getCompound(NBT.PREDICATE)) : buildDefaultPredicate());

	}

	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		if (hand == Hand.MAIN_HAND && world.isClient && isOwner(player)) {
			openConfigurationScreen();
			return ActionResult.SUCCESS;
		}
		return super.interact(player, hand);
	}

	@Override
	public void onBreak(@Nullable Entity entity) {
		if (!this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
			return;
		}
		this.playSound(SoundEvents.ENTITY_PAINTING_BREAK, 1.0f, 1.0f);
		if (entity instanceof PlayerEntity p) {
			if (p.getAbilities().creativeMode) {
				return;
			}
		}
		this.dropItem(MMObjects.ENCHANTED_CANVAS);
	}

	@Override
	public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
		this.setPosition(x, y, z);
		this.setRotation(yaw, pitch);
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return new EntitySpawnS2CPacket(getId(), getUuid(), attachmentPos.getX(), attachmentPos.getY(), attachmentPos.getZ(), 0F,
										getHorizontalFacing().asRotation(), getType(), 0,
										getVelocity());
	}

	@Override
	public ItemStack getPickBlockStack() {
		return new ItemStack(MMObjects.ENCHANTED_CANVAS);
	}

	@Override
	public void onSpawnPacket(EntitySpawnS2CPacket packet) {
		super.onSpawnPacket(packet);
		setFacing(Direction.fromRotation(packet.getYaw() * 360 / 256.0f));
		attachmentPos = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
		updateAttachmentPosition();
	}

	@Override
	public ConfigurablePredicate getPredicate() {
		return dataTracker.get(CONFIG_DATA);
	}

	@Override
	public void setPredicate(ConfigurablePredicate config) {
		this.dataTracker.set(CONFIG_DATA, config);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void finishConfiguration(ConfigurablePredicate configured) {
		System.out.println(configured.writeNbt(new NbtCompound()));
		SyncConfigurationPacket.send(this, configured);
	}
}
