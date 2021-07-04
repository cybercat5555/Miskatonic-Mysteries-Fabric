package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.entity.HasturCultistEntity;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.SyncRiteTargetPacket;
import com.miskatonicmysteries.common.registry.*;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.VillageGossipType;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class GoldenFlockRite extends AscensionLockedRite {
    public GoldenFlockRite() {
        super(new Identifier(Constants.MOD_ID, "golden_flock"), MMAffiliations.HASTUR, MMAffiliations.HASTUR.getId().getPath(), 0.5F, 1,
                Ingredient.ofItems(MMObjects.OCEANIC_GOLD), Ingredient.ofItems(Items.YELLOW_WOOL), Ingredient.ofItems(Items.YELLOW_WOOL), Ingredient.ofItems(Items.YELLOW_WOOL), Ingredient.ofItems(MMObjects.ORNATE_DAGGER));
    }

    @Override
    public boolean shouldContinue(OctagramBlockEntity octagram) {
        if (octagram.getOriginalCaster() == null) {
            return false;
        }
        if (octagram.targetedEntity == null && !octagram.getWorld().isClient) {
            octagram.tickCount++;
            Vec3d pos = octagram.getSummoningPos();
            octagram.targetedEntity = octagram.getWorld().getClosestEntity(VillagerEntity.class,
                    new TargetPredicate().setPredicate(villager ->
                            villager instanceof VillagerEntity && villager.hasStatusEffect(MMStatusEffects.MANIA)), null, pos.x, pos.y, pos.z, octagram.getSelectionBox().expand(10, 5, 10));
            if (octagram.targetedEntity != null) {
                octagram.tickCount = 0;
                SyncRiteTargetPacket.send(octagram.targetedEntity, octagram);
                octagram.markDirty();
                octagram.sync();
            }
        }
        return !(octagram.targetedEntity == null && octagram.tickCount > 20);
    }

    @Override
    public void onCancelled(OctagramBlockEntity octagram) {
        super.onCancelled(octagram);
    }

    @Override
    public boolean isFinished(OctagramBlockEntity octagram) {
        return octagram.targetedEntity != null && octagram.targetedEntity.squaredDistanceTo(octagram.getSummoningPos()) < 8 && octagram.tickCount >= 200;
    }

    @Override
    public void tick(OctagramBlockEntity octagram) {
        if (octagram.targetedEntity instanceof VillagerEntity && !octagram.targetedEntity.getType().equals(MMEntities.HASTUR_CULTIST)) {
            Vec3d pos = octagram.getSummoningPos();
            if (octagram.targetedEntity.squaredDistanceTo(pos) > 100 || ((VillagerEntity) octagram.targetedEntity).isDead()) {
                octagram.targetedEntity = null;
                return;
            }
            VillagerEntity villager = (VillagerEntity) octagram.targetedEntity;
            villager.getNavigation().startMovingTo(pos.x, pos.y, pos.z, 0.75F);
            List<HasturCultistEntity> cultists = octagram.getWorld().getEntitiesByClass(HasturCultistEntity.class, octagram.getSelectionBox().expand(10, 5, 10), cultist -> !cultist.isAttacking());
            for (HasturCultistEntity cultist : cultists) {
                cultist.getNavigation().startMovingTo(pos.x, pos.y, pos.z, 0.8F);
                if (cultist.getPos().distanceTo(pos) < 5) {
                    cultist.getNavigation().stop();
                    cultist.currentSpell = null;
                    cultist.setCastTime(20);
                }
            }
            if (villager.squaredDistanceTo(pos) < 16) {
                if (villager.squaredDistanceTo(pos) < 4) {
                    Vec3d motionVec = new Vec3d(pos.x - villager.getX(), 0, pos.z - villager.getZ());
                    motionVec = motionVec.normalize().multiply(0.1F).add(0, villager.getVelocity().y, 0);
                    villager.setVelocity(motionVec);
                }
                playMusic(octagram.getWorld(), pos, octagram.tickCount);
                spawnParticles(octagram.getWorld(), pos, octagram.getWorld().random);
                octagram.tickCount++;
                villager.getJumpControl().setActive();
                villager.setJumping(true);
                if (octagram.getOriginalCaster() != null) {
                    villager.lookAtEntity(octagram.getOriginalCaster(), 20, 20);
                }
            }
        }
    }

    @Override
    public void onFinished(OctagramBlockEntity octagram) {
        if (octagram.targetedEntity instanceof VillagerEntity) {
            octagram.targetedEntity.playSound(MMSounds.MAGIC, 1, (float) (1 + ((VillagerEntity) octagram.targetedEntity).getRandom().nextGaussian() * 0.2F));
            if (octagram.getWorld().isClient) {
                for (int i = 0; i < 20; i++) {
                    MMParticles.spawnCandleParticle(octagram.getWorld(), octagram.targetedEntity.getX() + octagram.getWorld().random.nextGaussian() * octagram.targetedEntity.getWidth(), octagram.targetedEntity.getY() + octagram.getWorld().random.nextFloat() * octagram.targetedEntity.getHeight(), octagram.targetedEntity.getZ() + octagram.getWorld().random.nextFloat() * octagram.targetedEntity.getWidth(), 1, true);
                }
            }
            if (octagram.getWorld() instanceof ServerWorld) {
                VillagerEntity recipient = (VillagerEntity) octagram.targetedEntity;
                ServerWorld world = (ServerWorld) octagram.getWorld();
                HasturCultistEntity cultist = MMEntities.HASTUR_CULTIST.create(world);
                cultist.refreshPositionAndAngles(recipient.getX(), recipient.getY(), recipient.getZ(), recipient.yaw, recipient.pitch);
                cultist.initialize(world, world.getLocalDifficulty(cultist.getBlockPos()), SpawnReason.CONVERSION, null, null);
                cultist.setAiDisabled(recipient.isAiDisabled());
                if (recipient.hasCustomName()) {
                    cultist.setCustomName(recipient.getCustomName());
                    cultist.setCustomNameVisible(recipient.isCustomNameVisible());
                }
                cultist.setCastTime(20);
                cultist.setPersistent();

                world.spawnEntityAndPassengers(cultist);
                recipient.releaseTicketFor(MemoryModuleType.HOME);
                recipient.releaseTicketFor(MemoryModuleType.JOB_SITE);
                recipient.releaseTicketFor(MemoryModuleType.POTENTIAL_JOB_SITE);
                recipient.releaseTicketFor(MemoryModuleType.MEETING_POINT);
                recipient.remove();
                cultist.reinitializeBrain(world);
                if (octagram.getOriginalCaster() != null) {
                    cultist.getGossip().startGossip(octagram.getOriginalCaster().getUuid(), VillageGossipType.MAJOR_POSITIVE, 50);
                }
            }
        }
        super.onFinished(octagram);
    }

    private void playMusic(World world, Vec3d pos, int tickCount) {
        if (tickCount % 12 == 0) {
            world.playSound(null, pos.x, pos.y, pos.z, SoundEvents.BLOCK_NOTE_BLOCK_SNARE, SoundCategory.PLAYERS, 0.5F + tickCount / 200F, 1);
        } else if (tickCount % 15 == 6) {
            world.playSound(null, pos.x, pos.y, pos.z, SoundEvents.BLOCK_NOTE_BLOCK_SNARE, SoundCategory.PLAYERS, 0.4F + tickCount / 200F, 1);
        } else if (tickCount % 15 == 9) {
            world.playSound(null, pos.x, pos.y, pos.z, SoundEvents.BLOCK_NOTE_BLOCK_SNARE, SoundCategory.PLAYERS, 0.4F + tickCount / 200F, 1);
        }
    }

    private void spawnParticles(World world, Vec3d pos, Random random) {
        if (random.nextFloat() < 0.1F) {
            Vec3d particlePos = pos.add(random.nextGaussian() * 2.5F, -0.25F + random.nextFloat() * 2, random.nextGaussian() * 2.5F);
            world.addParticle(ParticleTypes.NOTE, particlePos.x, particlePos.y, particlePos.z, 1, 0.75 + random.nextFloat() * 0.25F, random.nextFloat() * 0.1F);
        }
        if (random.nextFloat() < 0.2F) {
            Vec3d particlePos = pos.add(random.nextGaussian() * 3, -0.25F + random.nextFloat() * 3, random.nextGaussian() * 3);
            world.addParticle(MMParticles.AMBIENT, particlePos.x, particlePos.y, particlePos.z, 1, 0.75 + random.nextFloat() * 0.25F, random.nextFloat() * 0.1F);
        }
    }
}
