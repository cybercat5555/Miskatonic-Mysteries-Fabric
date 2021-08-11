package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.interfaces.Ascendant;
import com.miskatonicmysteries.common.block.MasterpieceStatueBlock;
import com.miskatonicmysteries.common.block.blockentity.MasterpieceStatueBlockEntity;
import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.handler.SchedulingHandler;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.SyncRiteTargetPacket;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.registry.MMParticles;
import com.miskatonicmysteries.common.util.Constants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.state.property.Properties;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class MasterpieceRite extends TriggeredRite {
	public MasterpieceRite() {
		super(new Identifier(Constants.MOD_ID, "masterpiece"), MMAffiliations.HASTUR, 0.5F, 60,
				Ingredient.ofItems(Items.STONE), Ingredient.ofItems(Items.STONE), Ingredient.ofItems(Items.DEEPSLATE),
				Ingredient.ofItems(Items.DEEPSLATE), Ingredient.ofItems(Items.BLACKSTONE),
				Ingredient.ofItems(Items.BLACKSTONE), Ingredient.ofItems(Items.DIAMOND),
				Ingredient.ofItems(Items.IRON_INGOT));
	}

	@Override
	public boolean canCast(OctagramBlockEntity octagram) {
		if (super.canCast(octagram)) {
			if (!octagram.doesCasterHaveKnowledge(MMAffiliations.HASTUR.getId().getPath())) {
				if (octagram.getOriginalCaster() != null) {
					octagram.getOriginalCaster().sendMessage(new TranslatableText("message.miskatonicmysteries.rite_fail.knowledge"), true);
				}
				return false;
			}
			if (Ascendant.of(octagram.getOriginalCaster()).isPresent()) {
				Ascendant ascendant = Ascendant.of(octagram.getOriginalCaster()).get();
				if (!MMAffiliations.HASTUR.equals(MiskatonicMysteriesAPI.getNonNullAffiliation(octagram.getOriginalCaster(), false))) {
					octagram.getOriginalCaster().sendMessage(new TranslatableText("message.miskatonicmysteries.ascension_path_fail"), true);
					return false;
				}
				if (ascendant.getAscensionStage() < 2) {
					octagram.getOriginalCaster().sendMessage(new TranslatableText("message.miskatonicmysteries.ascension_stage_fail"), true);
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public void trigger(OctagramBlockEntity octagram, Entity triggeringEntity) {
		super.trigger(octagram, triggeringEntity);
		octagram.targetedEntity = triggeringEntity;
		if (triggeringEntity != null) {
			SyncRiteTargetPacket.send(triggeringEntity, octagram);
		}
	}

	@Override
	public void tick(OctagramBlockEntity octagram) {
		if (octagram.tickCount < ticksNeeded || octagram.triggered) {
			super.tick(octagram);
		}
		if (octagram.tickCount > ticksNeeded) {
			Vec3d pos = octagram.getSummoningPos();
			if (!octagram.getWorld().isClient && (octagram.targetedEntity == null || (octagram.targetedEntity == octagram.getOriginalCaster() && octagram.targetedEntity.isSneaking()))) {
				LivingEntity foundEntity = octagram.getWorld().getClosestEntity(LivingEntity.class,
						TargetPredicate.createAttackable().setBaseMaxDistance(4).setPredicate(entity -> entity instanceof VillagerEntity || (entity instanceof PlayerEntity p && !p.isCreative() && (p != octagram.getOriginalCaster() || !p.isSneaking()))), null, pos.x, pos.y, pos.z, octagram.getSelectionBox());
				octagram.targetedEntity = foundEntity;
				if (!octagram.getWorld().isClient && octagram.targetedEntity != null) {
					SyncRiteTargetPacket.send(octagram.targetedEntity, octagram);
				}
			}

			if (octagram.targetedEntity instanceof LivingEntity l) {
				Vec3d motionVec = new Vec3d(pos.x - l.getX(), pos.y - l.getY(), pos.z - l.getZ());
				motionVec = motionVec.multiply(0.25F);
				l.setVelocity(motionVec);
				l.velocityModified = true;
			}

			if (octagram.getWorld().isClient && octagram.tickCount % 20 < 10) {
				spawnParticles(octagram.getWorld(), pos);
			}
		}
	}

	@Environment(EnvType.CLIENT)
	private void spawnParticles(World world, Vec3d pos) {
		for (int i = 0; i < 8; i++) {
			Vec3d particlePos = pos.add(Math.sin(Math.PI / 4 * i) * 2, 0, Math.cos(Math.PI / 4 * i) * 2);
			Vec3d motionVec = new Vec3d(pos.x - particlePos.x, pos.y - particlePos.y, pos.z - particlePos.z);
			motionVec = motionVec.multiply(0.025);
			world.addParticle(MMParticles.AMBIENT_MAGIC, true, particlePos.x, particlePos.y, particlePos.z,
					motionVec.x, motionVec.y, motionVec.z);
		}
	}

	@Override
	public boolean isFinished(OctagramBlockEntity octagram) {
		return octagram.triggered && octagram.tickCount >= 600 + ticksNeeded;
	}

	@Override
	public boolean listen(OctagramBlockEntity blockEntity, World world, GameEvent event, Entity entity, BlockPos pos) {
		if (!world.isClient && event == GameEvent.ENTITY_KILLED && entity == blockEntity.targetedEntity) {
			ItemStack statueStack = new ItemStack(MMObjects.MASTERPIECE_STATUE);
			if (entity instanceof PlayerEntity player) {
				MasterpieceStatueBlock.setSculptureData(statueStack, blockEntity.getOriginalCaster(), player,
						MasterpieceStatueBlockEntity.selectRandomPose(world.random));
			}
			BlockState statueState = MMObjects.MASTERPIECE_STATUE.getDefaultState().with(Properties.ROTATION,
					MathHelper.floor((double) (entity.getYaw() * 16.0F / 360.0F) + 0.5D) & 15);
			MasterpieceStatueBlockEntity statue = new MasterpieceStatueBlockEntity(pos, statueState);
			BlockPos targetPos = blockEntity.getPos();
			SchedulingHandler.addTask((server) -> {
				world.breakBlock(targetPos, true, blockEntity.getOriginalCaster());
				world.removeBlockEntity(targetPos);
				world.setBlockState(targetPos, statueState);
				world.addBlockEntity(statue);
				statue.setCreator(blockEntity.getOriginalCaster());
				if (entity instanceof PlayerEntity p) {
					statue.setStatueProfile(p.getGameProfile());
					statue.pose = MasterpieceStatueBlockEntity.selectRandomPose(world.random);
					if (blockEntity.getOriginalCaster() == null) {
						statue.setCreator(p);
					}
				}
				statue.markDirty();
			});
			return true;
		}
		return super.listen(blockEntity, world, event, entity, pos);
	}
}
