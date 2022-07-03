package com.miskatonicmysteries.common.feature.entity.painting;

import com.miskatonicmysteries.common.registry.MMEntities;
import com.miskatonicmysteries.common.registry.MMEntities.PaintingMotives;
import com.miskatonicmysteries.common.registry.MMStatusEffects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffectInstance;
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
import java.util.UUID;

public class HomeyPaintingEntity extends MagicPaintingEntity {

	private Box detectionBox;

	public HomeyPaintingEntity(EntityType<? extends HomeyPaintingEntity> entityType, World world) {
		super(entityType, world);
		this.motive = PaintingMotives.SOUP_TIME;
	}

	public HomeyPaintingEntity(World world, BlockPos pos, Direction direction, PlayerEntity owner) {
		super(MMEntities.HOMEY_PAINTING, world, pos, direction, PaintingMotives.SOUP_TIME, owner);
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
		this.detectionBox = box.expand(8);
	}

	@Override
	public void tick() {
		super.tick();
		if (!world.isClient) {
			if (age % 60 == 0) {
				List<LivingEntity> targets = world.getEntitiesByClass(LivingEntity.class, detectionBox, getPredicate());
				for (LivingEntity target : targets) {
					target.addStatusEffect(new StatusEffectInstance(MMStatusEffects.HOMELY, 300, 0, false, false, true), this);
				}
			}
		}
	}

	@Override
	public void onSpawnPacket(EntitySpawnS2CPacket packet) {
		super.onSpawnPacket(packet);
	}
}
