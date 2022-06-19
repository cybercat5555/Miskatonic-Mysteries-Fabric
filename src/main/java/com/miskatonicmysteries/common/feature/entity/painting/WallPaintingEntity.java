package com.miskatonicmysteries.common.feature.entity.painting;

import com.miskatonicmysteries.common.registry.MMEntities;
import com.miskatonicmysteries.common.registry.MMEntities.PaintingMotives;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.UUID;

public class WallPaintingEntity extends MagicPaintingEntity {

	public int noCollisionTicks;

	public WallPaintingEntity(EntityType<? extends WallPaintingEntity> entityType, World world) {
		super(entityType, world);
		this.motive = PaintingMotives.SHINING_GATES;
	}

	public WallPaintingEntity(World world, BlockPos pos, Direction direction, UUID owner) {
		super(MMEntities.WALL_PAINTING, world, pos, direction, PaintingMotives.SHINING_GATES, owner);
	}

	@Override
	public void onPlayerCollision(PlayerEntity player) {
		super.onPlayerCollision(player);
		if (isOwner(player) && checkBoxIntersection(player)) {
			System.out.println(world.isClient);
			if (noCollisionTicks == 0 && world.isClient) {
				Box bb = getBoundingBox();
				for (int i = 0; i < 10; i++) {
					world.addParticle(ParticleTypes.LARGE_SMOKE,
									  MathHelper.lerp(random.nextFloat(), bb.minX, bb.maxX),
									  MathHelper.lerp(random.nextFloat(), bb.minY, bb.maxY),
									  MathHelper.lerp(random.nextFloat(), bb.minZ, bb.maxZ),
									  0, 0, 0);
				}
			}
			noCollisionTicks = 20;
		}
	}

	private boolean checkBoxIntersection(PlayerEntity player) {
		Box b1 = player.getBoundingBox();
		Box b2 = getBoundingBox().expand(0.1F);
		return b1.intersects(b2);
	}

	@Override
	public boolean collidesWith(Entity other) {
		return !(other instanceof PaintingEntity) && !(other instanceof PlayerEntity p && isOwner(p)) && noCollisionTicks == 0;
	}

	@Override
	public boolean isCollidable() {
		return noCollisionTicks == 0;
	}

	@Override
	public void tick() {
		if (noCollisionTicks > 0) {
			noCollisionTicks--;
		}
	}
}
