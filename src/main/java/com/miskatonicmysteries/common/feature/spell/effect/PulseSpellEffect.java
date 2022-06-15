package com.miskatonicmysteries.common.feature.spell.effect;

import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.common.feature.entity.GenericTentacleEntity;
import com.miskatonicmysteries.common.registry.MMEntities;
import com.miskatonicmysteries.common.registry.MMSpellMediums;
import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

import java.util.UUID;

import javax.annotation.Nullable;

public class PulseSpellEffect extends SpellEffect {

	private static final EntityAttributeModifier dmgMod1 = new EntityAttributeModifier(UUID
																						   .fromString("a98bd513-e606-477e-8ce1-381943a8cb0c"),
																					   "MM_SpellDMG_1", -2,
																					   EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier dmgMod2 = new EntityAttributeModifier(UUID
																						   .fromString("736f28f2-f423-44a4-b819-b3cc46395f2b"),
																					   "MM_SpellDMG_1", 0,
																					   EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier dmgMod3 = new EntityAttributeModifier(UUID
																						   .fromString("d622463f-ff36-4b42-a340-8dd209b5c2b5"),
																					   "MM_SpellDMG_1", 2,
																					   EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier[] dmgMods = {dmgMod1, dmgMod2, dmgMod3};

	public PulseSpellEffect() {
		super(new Identifier(Constants.MOD_ID, "pulse"), null, 0xf9df1b);
	}

	@Override
	public boolean effect(World world, LivingEntity caster, @Nullable Entity target, @Nullable Vec3d pos, SpellMedium medium, int intensity,
						  @Nullable Entity secondaryMedium) {
		if (target != null && !(target instanceof LivingEntity)) {
			return false;
		}

		boolean broad = caster.getRandom().nextBoolean();
		if (medium != MMSpellMediums.SELF) {
			Vec3d hitPos = caster.getPos();
			if (target != null) {
				hitPos = target.getPos();
			} else if (secondaryMedium != null && secondaryMedium != caster) {
				hitPos = secondaryMedium.getPos();
			} else {
				float yaw = -caster.getHeadYaw() / 57.295776F;
				hitPos = hitPos.add(MathHelper.sin(yaw) * 5, 0F, MathHelper.cos(yaw) * 5);
			}
			double minY = Math.min(hitPos.getY(), caster.getY());
			double startY = Math.max(hitPos.getY(), caster.getY()) + 1.0D;
			float yaw = (float) MathHelper.atan2(hitPos.getZ() - caster.getZ(), hitPos.getX() - caster.getX());
			for (int i = 0; i < 5; ++i) {
				double l = 1.5D * (double) (i + 1);
				conjureTentacle(caster, target == caster ? null : (LivingEntity) target,
								caster.getX() + (double) MathHelper.cos(yaw) * l,
								caster.getZ() + (double) MathHelper.sin(yaw) * l,
								minY, startY, yaw * 57.295776F - 90F + 20F, i, intensity, broad);
			}
		} else {
			for (int i = 0; i < 8; i++) {
				float angle = 2 * 3.1415927F * (i / 8F);
				this.conjureTentacle(caster, null, caster.getX() + (double) MathHelper.cos(angle) * 1.5, caster
					.getZ() + (double) MathHelper.sin(angle) * 1.5, caster.getY() + 2, caster.getY(), (float) Math
					.toDegrees(angle), 0, intensity, broad);
			}

		}
		return true;
	}

	private void conjureTentacle(LivingEntity caster, @Nullable LivingEntity target, double x, double z, double maxY, double y, float yaw,
								 int index, int intensity, boolean broad) {
		BlockPos blockPos = new BlockPos(x, y, z);
		boolean bl = false;
		double d = 0.0D;

		do {
			BlockPos blockPos2 = blockPos.down();
			BlockState blockState = caster.world.getBlockState(blockPos2);
			if (blockState.isSideSolidFullSquare(caster.world, blockPos2, Direction.UP)) {
				if (!caster.world.isAir(blockPos)) {
					BlockState blockState2 = caster.world.getBlockState(blockPos);
					VoxelShape voxelShape = blockState2.getCollisionShape(caster.world, blockPos);
					if (!voxelShape.isEmpty()) {
						d = voxelShape.getMax(Direction.Axis.Y);
					}
				}

				bl = true;
				break;
			}

			blockPos = blockPos.down();
		} while (blockPos.getY() >= MathHelper.floor(maxY) - 1);

		if (bl) {
			GenericTentacleEntity tentacle = MMEntities.GENERIC_TENTACLE.create(caster.world);
			tentacle.setPos(x, blockPos.getY() + d + 0.1, z);
			if (index % 2 == 1) {
				yaw -= 40;
			}
			tentacle.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE)
				.addTemporaryModifier(intensity > 2 ? dmgMod3 : dmgMods[intensity]);
			if (target != null) {
				tentacle.setSpecificTarget(target);
			}
			tentacle.setHeadYaw(yaw);
			tentacle.setOwner(caster);
			tentacle.setSimple(true);
			tentacle.setSize(0);
			tentacle.setMaxAge(broad ? 25 : 15);
			tentacle.setBroadSwing(broad);
			caster.world.spawnEntity(tentacle);
		}
	}

	@Override
	public float getCooldownBase(int intensity) {
		return 200 + intensity * 200;
	}
}
