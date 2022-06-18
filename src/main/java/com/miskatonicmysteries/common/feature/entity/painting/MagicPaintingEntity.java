package com.miskatonicmysteries.common.feature.entity.painting;

import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.Constants.NBT;

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
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

public abstract class MagicPaintingEntity extends PaintingEntity implements Affiliated { //extend painting entity for some logic

	protected static final TrackedData<Optional<UUID>> OWNER_UUID = DataTracker
		.registerData(TameableEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

	public MagicPaintingEntity(EntityType<? extends MagicPaintingEntity> entityType, World world) {
		super(entityType, world);
	}

	public MagicPaintingEntity(EntityType<? extends MagicPaintingEntity> entityType, World world, BlockPos pos, Direction direction,
							   PaintingMotive motive, UUID owner) {
		super(entityType, world);
		this.motive = motive;
		attachmentPos = pos;
		setFacing(direction);
		setPos(attachmentPos.getX() + 0.5F, attachmentPos.getY() + 0.5F, attachmentPos.getZ() + 0.5F);
		setOwnerUuid(owner);
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
	public void onSpawnPacket(EntitySpawnS2CPacket packet) {
		super.onSpawnPacket(packet);
		setFacing(Direction.fromRotation(packet.getYaw() * 360 / 256.0f));
		attachmentPos = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
		updateAttachmentPosition();
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		this.dataTracker.get(OWNER_UUID).ifPresent(uuid -> {
			nbt.putUuid(NBT.OWNER, uuid);
		});
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		if (nbt.contains(NBT.OWNER)) {
			setOwnerUuid(nbt.getUuid(NBT.OWNER));
		}
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
	public Packet<?> createSpawnPacket() {
		return new EntitySpawnS2CPacket(getId(), getUuid(), getX(), getY(), getZ(), 0F, getHorizontalFacing().asRotation(), getType(), 0,
										getVelocity());
	}

	@Override
	public ItemStack getPickBlockStack() {
		return new ItemStack(MMObjects.ENCHANTED_CANVAS);
	}
}
