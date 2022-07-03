package com.miskatonicmysteries.common.feature.entity.painting;

import com.miskatonicmysteries.common.handler.predicate.ConfigurableTargetPredicate;
import com.miskatonicmysteries.common.registry.MMEntities;
import com.miskatonicmysteries.common.registry.MMEntities.PaintingMotives;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;

public class PulsePaintingEntity extends MagicPaintingEntity {

	private Box detectionBox;

	public PulsePaintingEntity(EntityType<? extends PulsePaintingEntity> entityType, World world) {
		super(entityType, world);
		this.motive = PaintingMotives.BLACK_STAR;
	}

	public PulsePaintingEntity(World world, BlockPos pos, Direction direction, PlayerEntity owner) {
		super(MMEntities.PULSE_PAINTING, world, pos, direction, PaintingMotives.BLACK_STAR, owner);
	}

	@Override
	protected ConfigurableTargetPredicate buildDefaultPredicate() {
		ConfigurableTargetPredicate t = super.buildDefaultPredicate();
		t.excludePlayers = true;
		return t;
	}

	@Override
	public void onSpawnPacket(EntitySpawnS2CPacket packet) {
		super.onSpawnPacket(packet);
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
			if (age % 20 == 0) {
				calculateDetectionBox();
			}
			List<Entity> targets = world.getOtherEntities(this, detectionBox, getPredicate());
			Vec3d direction = new Vec3d(getHorizontalFacing().getUnitVector()).multiply(0.1);
			for (Entity target : targets) {
				target.addVelocity(direction.getX(), direction.getY() + 0.1, direction.getZ());
				target.velocityDirty = true;
				target.velocityModified = true;
			}
		}
	}
}
