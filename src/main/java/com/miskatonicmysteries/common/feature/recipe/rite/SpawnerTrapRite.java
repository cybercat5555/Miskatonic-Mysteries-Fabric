package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.api.interfaces.DropManipulator;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.registry.MMParticles;
import com.miskatonicmysteries.common.registry.MMSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SpawnerTrapRite extends TriggeredRite {
    private final Predicate<EntityType<?>> spawnPredicate;
    private final ParticleEffect spawnParticles;
    private final float[] color;

    public SpawnerTrapRite(Identifier id, @Nullable Affiliation octagram, Predicate<EntityType<?>> spawnPredicate, ParticleEffect spawnParticles, float[] color, Ingredient... ingredients) {
        super(id, octagram, 0.01F, 90, ingredients);
        this.spawnPredicate = spawnPredicate;
        this.spawnParticles = spawnParticles;
        this.color = color;
    }

    @Override
    public boolean canCast(OctagramBlockEntity octagram) {
        if (super.canCast(octagram)) {
            if (octagram.getWorld().isClient) {
                return true;
            }
            for (BlockPos iterateOutward : BlockPos.iterateOutwards(octagram.getPos().add(0, 1, 0), 1, 1, 1)) {
                if (octagram.getWorld().getBlockState(iterateOutward).isSolidBlock(octagram.getWorld(), iterateOutward)) {
                    if (octagram.getOriginalCaster() != null) {
                        octagram.getOriginalCaster().sendMessage(new TranslatableText("message.miskatonicmysteries.needs_space"), true);
                    }
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void tick(OctagramBlockEntity octagram) {
        if (octagram.triggered && octagram.tickCount > ticksNeeded && octagram.tickCount % 200 == 0) {
            World world = octagram.getWorld();
            int amount = 1 + world.random.nextInt(2);
            octagram.getWorld().playSound(null, octagram.getPos(), MMSounds.BROKE_VEIL_SPAWN, SoundCategory.AMBIENT, 1.0F, (float) world.random.nextGaussian() * 0.2F + 1.0F);
            if (!world.isClient) {
                for (int i = 0; i < amount; i++) {
                    List<EntityType<?>> possibleTypes = Registry.ENTITY_TYPE.stream().filter(spawnPredicate).collect(Collectors.toList());
                    EntityType<?> type = possibleTypes.get(world.random.nextInt(possibleTypes.size()));
                    Entity entity = type.create(world);
                    Vec3d position = octagram.getSummoningPos().add(octagram.getWorld().random.nextGaussian(), -0.25F + octagram.getWorld().random.nextFloat(), octagram.getWorld().random.nextGaussian());
                    int yaw = world.random.nextInt(360);
                    entity.updatePositionAndAngles(position.x, position.y, position.z, yaw, 0);
                    if (entity instanceof LivingEntity) {
                        ((LivingEntity) entity).bodyYaw = yaw;
                        ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 1200, 0, true, true));
                        ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 1200, 0, true, true));
                        ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 1200, 0, true, true));
                        if (entity instanceof MobEntity) {
                            ((MobEntity) entity).initialize((ServerWorld) world, world.getLocalDifficulty(entity.getBlockPos()), SpawnReason.EVENT, null, null);
                            entity.world.getOtherEntities(entity, entity.getBoundingBox().expand(8, 3, 8), target -> target instanceof LivingEntity && !(target instanceof Monster) && EntityPredicates.EXCEPT_CREATIVE_SPECTATOR_OR_PEACEFUL.test(target)).stream().findAny().ifPresent(value -> ((MobEntity) entity).setTarget((LivingEntity) value));
                        }
                        ((DropManipulator) entity).setDropOveride(true);
                    }
                    world.spawnEntity(entity);
                }
            } else {
                for (int i = 0; i < 9; i++) {
                    Vec3d position = octagram.getSummoningPos().add(octagram.getWorld().random.nextGaussian() * 0.5F, -0.25 + octagram.getWorld().random.nextFloat(), octagram.getWorld().random.nextGaussian() * 0.5F);
                    octagram.getWorld().addParticle(spawnParticles, position.x, position.y, position.z, 0, 0, 0);
                }
            }
        } else if (octagram.getWorld().isClient && (octagram.tickCount < ticksNeeded || octagram.triggered) && octagram.getWorld().random.nextFloat() < 0.25F) {
            Vec3d position = octagram.getSummoningPos().add(octagram.getWorld().random.nextGaussian(), -0.5 + octagram.getWorld().random.nextFloat(), octagram.getWorld().random.nextGaussian() * 0.5F);
            octagram.getWorld().addParticle(MMParticles.AMBIENT, position.x, position.y, position.z, color[0], color[1], color[2]);
        }
        if (octagram.triggered || octagram.tickCount < ticksNeeded) {
            super.tick(octagram);
        }
    }

    @Override
    public void trigger(OctagramBlockEntity octagram, Entity triggeringEntity) {
        super.trigger(octagram, triggeringEntity);
        if (triggeringEntity instanceof LivingEntity) {
            ((LivingEntity) triggeringEntity).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 400, 1, true, true));
        }
    }

    @Override
    public boolean isFinished(OctagramBlockEntity octagram) {
        return octagram.triggered && octagram.tickCount >= 1200;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void renderRite(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay, BlockEntityRendererFactory.Context context) {
        if (entity.triggered && entity.tickCount > ticksNeeded) {
            float alpha = (entity.tickCount - ticksNeeded) > ticksNeeded ? 1 : (entity.tickCount - ticksNeeded) / (float) ticksNeeded;
            renderPortalOctagram(alpha, color, entity, tickDelta, matrixStack, vertexConsumers, light, overlay, context);
        } else {
            super.renderRite(entity, tickDelta, matrixStack, vertexConsumers, light, overlay, context);
        }
    }
}
