package com.miskatonicmysteries.common.feature.entity.painting;

import com.miskatonicmysteries.common.feature.entity.GuardDogEntity;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.SmokeEffectPacket;
import com.miskatonicmysteries.common.registry.MMEntities;
import com.miskatonicmysteries.common.registry.MMEntities.PaintingMotives;
import com.miskatonicmysteries.common.util.Constants.NBT;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class ManosPaintingEntity extends MagicPaintingEntity {

	protected static final TrackedData<Byte> PAINTING_STATUS = DataTracker.registerData(ManosPaintingEntity.class, TrackedDataHandlerRegistry.BYTE);
	private Box detectionBox;
	private int restTicks;

	public ManosPaintingEntity(EntityType<? extends ManosPaintingEntity> entityType, World world) {
		super(entityType, world);
		this.motive = PaintingMotives.GUARDIAN;
	}

	public ManosPaintingEntity(World world, BlockPos pos, Direction direction, UUID owner) {
		super(MMEntities.GUARDIAN_PAINTING, world, pos, direction, MMEntities.PaintingMotives.GUARDIAN, owner);
	}


	@Override
	protected void updateAttachmentPosition() {
		super.updateAttachmentPosition();
		if (getHorizontalFacing() != null) {
			calculateDetectionBox();
		}
	}

	private void calculateDetectionBox() {
		Box box = getBoundingBox();
		Vec3d stretchVec = new Vec3d(getHorizontalFacing().getUnitVector()).multiply(8);
		HitResult blockHit = world.raycast(new RaycastContext(getPos(), getPos().add(stretchVec), RaycastContext.ShapeType.COLLIDER,
															  RaycastContext.FluidHandling.NONE, this));
		if (blockHit != null) {
			stretchVec = blockHit.getPos().subtract(getPos());
		}
		this.detectionBox = box.stretch(stretchVec);
	}

	@Override
	public void tick() {
		super.tick();
		if (!world.isClient) {
			byte status = getPaintingStatus();
			if (status == 0) {
				if (age % 20 == 0) {
					calculateDetectionBox();
				}
				List<Entity> targets = world.getOtherEntities(this, detectionBox, this::canTarget);
				if (!targets.isEmpty()) {
					GuardDogEntity dog = new GuardDogEntity(this);
					Direction direction = getHorizontalFacing();
					Vec3d directionVec = new Vec3d(direction.getUnitVector());
					Vec3d pos = getPos().add(directionVec.multiply(0.5F)).add(0, -0.5F, 0);
					dog.setVelocity(directionVec.multiply(0.25F));
					dog.refreshPositionAndAngles(pos.getX(), pos.getY(), pos.getZ(), direction.getOpposite().asRotation(), 0F);
					world.spawnEntity(dog);
					dog.setTarget((LivingEntity) targets.get(0));
					setPaintingStatus((byte) 1);
					SmokeEffectPacket.send(this);
				}
			} else if (status == 2) {
				if (restTicks > 0) {
					restTicks--;
				} else {
					setPaintingStatus((byte) 0);
				}
			}
		}
	}

	public boolean canTarget(Entity entity) {
		return (entity instanceof MobEntity && entity instanceof Monster) || (entity instanceof PlayerEntity p && !isOwner(p) && !p.isCreative());
	}

	public byte getPaintingStatus() {
		return this.dataTracker.get(PAINTING_STATUS);
	}

	public void setPaintingStatus(byte flags) {
		this.dataTracker.set(PAINTING_STATUS, (byte) MathHelper.clamp(flags, 0, 2));
	}

	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		if (!player.world.isClient && hand == Hand.MAIN_HAND) {
			setPaintingStatus((byte) (getPaintingStatus() + (player.isSneaking() ? 1 : -1)));
		}
		return super.interact(player, hand);
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(PAINTING_STATUS, (byte) 0);
	}

	@Override
	public void onSpawnPacket(EntitySpawnS2CPacket packet) {
		super.onSpawnPacket(packet);
		this.motive = PaintingMotives.GUARDIAN;
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		setPaintingStatus(nbt.getByte(NBT.PAINTING_STATUS));
		this.restTicks = nbt.getInt(NBT.REST_TICKS);
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		byte status = getPaintingStatus();
		nbt.putByte(NBT.PAINTING_STATUS, status == 1 ? 0 : status); //never save dog being active, as it dogs are never saved
		nbt.putInt(NBT.REST_TICKS, restTicks);
	}

	public void startResting() {
		this.restTicks = 600;
	}
}
