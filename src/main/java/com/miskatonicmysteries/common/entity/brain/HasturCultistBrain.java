package com.miskatonicmysteries.common.entity.brain;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.miskatonicmysteries.api.interfaces.Ascendant;
import com.miskatonicmysteries.common.entity.HasturCultistEntity;
import com.miskatonicmysteries.common.entity.ProtagonistEntity;
import com.miskatonicmysteries.common.entity.ai.task.CastSpellTask;
import com.miskatonicmysteries.common.entity.ai.task.HealthCareTask;
import com.miskatonicmysteries.common.entity.ai.task.RecruitTask;
import com.miskatonicmysteries.common.entity.ai.task.TacticalApproachTask;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.EffectParticlePacket;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMBlessings;
import com.miskatonicmysteries.common.registry.MMEntities;
import com.miskatonicmysteries.common.util.CapabilityUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.List;
import java.util.Optional;

public class HasturCultistBrain {
    private static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES = ImmutableList.of(MemoryModuleType.UNIVERSAL_ANGER, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ANGRY_AT, MemoryModuleType.HOME, MemoryModuleType.JOB_SITE, MemoryModuleType.POTENTIAL_JOB_SITE, MemoryModuleType.MEETING_POINT, MemoryModuleType.MOBS, MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.WALK_TARGET, MemoryModuleType.LOOK_TARGET, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.BREED_TARGET, MemoryModuleType.PATH, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.NEAREST_BED, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.NEAREST_HOSTILE, MemoryModuleType.SECONDARY_JOB_SITE, MemoryModuleType.HIDING_PLACE, MemoryModuleType.HEARD_BELL_TIME, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.LAST_SLEPT, MemoryModuleType.LAST_WOKEN, MemoryModuleType.LAST_WORKED_AT_POI, MemoryModuleType.GOLEM_DETECTED_RECENTLY);
    private static final ImmutableList<SensorType<? extends Sensor<? super VillagerEntity>>> SENSORS = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.NEAREST_BED, SensorType.HURT_BY, SensorType.VILLAGER_HOSTILES, SensorType.VILLAGER_BABIES, SensorType.SECONDARY_POIS, SensorType.GOLEM_DETECTED);


    public static void init(HasturCultistEntity entity, Brain<VillagerEntity> brain) {
        brain.setTaskList(Activity.CORE, 0, createCoreTasks(entity, 0.65F));
        brain.setTaskList(Activity.IDLE, VillagerTaskListProvider.createIdleTasks(MMEntities.YELLOW_SERF, 0.65F), ImmutableSet.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_ABSENT)));
        brain.setTaskList(Activity.FIGHT, 10, createFightTasks(entity, 0.65F), MemoryModuleType.ATTACK_TARGET);
        brain.setTaskList(Activity.MEET, createMeetTasks(0.65F), ImmutableSet.of(Pair.of(MemoryModuleType.MEETING_POINT, MemoryModuleState.VALUE_PRESENT), Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_ABSENT)));

        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.resetPossibleActivities();
        //can't attack if there's a congregation point nearby, too busy with it?
    }


    public static Brain.Profile<VillagerEntity> createProfile() {
        return Brain.createProfile(MEMORY_MODULES, SENSORS);
    }

    public static void tickActivities(HasturCultistEntity entity) {
        Brain<VillagerEntity> brain = entity.getBrain();
        brain.resetPossibleActivities(ImmutableList.of(Activity.MEET, Activity.FIGHT, Activity.IDLE));

        if (entity.isCasting()) {
            if (entity.currentSpell != null && !entity.world.isClient) {
                EffectParticlePacket.send(entity);
            }
            entity.setCastTime(entity.getCastTime() - 1);
        }
        if (!entity.world.isClient && entity.currentSpell != null && entity.getCastTime() <= 0) {
            entity.currentSpell.cast(entity);
            entity.currentSpell = null;
            entity.getBrain().remember(MemoryModuleType.ATTACK_COOLING_DOWN, true, 40);
        }
    }


    public static void onAttacked(HasturCultistEntity cultist, LivingEntity attacker) {
        if (!(attacker instanceof HasturCultistEntity)) {
            if (attacker instanceof PlayerEntity && shouldAttack(attacker)) {
                if (!LookTargetUtil.isNewTargetTooFar(cultist, attacker, 4.0D)) {
                    cultist.getBrain().forget(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
                    cultist.getBrain().remember(MemoryModuleType.ANGRY_AT, attacker.getUuid(), 600L);
                    getNearbyCultists(cultist).forEach((nearbyCultist) -> {
                        nearbyCultist.getBrain().forget(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
                        nearbyCultist.getBrain().remember(MemoryModuleType.ANGRY_AT, attacker.getUuid(), 600L);
                    });
                }
            }
        }
    }

    private static List<HasturCultistEntity> getNearbyCultists(HasturCultistEntity cultist) {
        return cultist.getEntityWorld().getEntitiesByClass(HasturCultistEntity.class, cultist.getBoundingBox().expand(10, 4, 10), null);
    }

    private static Optional<LivingEntity> getAngryAt(AbstractPiglinEntity piglin) {
        return LookTargetUtil.getEntity(piglin, MemoryModuleType.ANGRY_AT);
    }

    private static boolean shouldAttack(LivingEntity target) {
        return EntityPredicates.EXCEPT_CREATIVE_SPECTATOR_OR_PEACEFUL.test(target);
    }


    private static boolean isAscended(LivingEntity entity) {
        return entity instanceof HasturCultistEntity && ((HasturCultistEntity) entity).isAscended();
    }

    public static Optional<? extends LivingEntity> getBestTarget(VillagerEntity cultist) {
        Brain<VillagerEntity> brain = cultist.getBrain();
        Optional<LivingEntity> optional = LookTargetUtil.getEntity(cultist, MemoryModuleType.ANGRY_AT);
        if (optional.isPresent() && shouldAttack(optional.get())) {
            return optional;
        } else {
            if (brain.hasMemoryModule(MemoryModuleType.VISIBLE_MOBS)) {
                Optional<List<LivingEntity>> mobs = brain.getOptionalMemory(MemoryModuleType.VISIBLE_MOBS);
                if (mobs.isPresent()) {
                    List<LivingEntity> mobList = mobs.get();
                    LivingEntity bestTarget = null;
                    for (LivingEntity livingEntity : mobList) {
                        if (Ascendant.of(livingEntity).isPresent() && CapabilityUtil.hasBlessing(Ascendant.of(livingEntity).get(), MMBlessings.ROYAL_ENTOURAGE)) {
                            if (livingEntity.getAttacker() != null) {
                                bestTarget = livingEntity.getAttacker();
                                break;
                            } else if (livingEntity.getAttacking() != null && !(livingEntity.getAttacking() instanceof VillagerEntity)) {
                                bestTarget = livingEntity.getAttacking();
                                break;
                            }
                        }
                        if (CapabilityUtil.getAffiliation(livingEntity, true) == MMAffiliations.SHUB
                                || livingEntity instanceof ProtagonistEntity || (livingEntity instanceof Monster && !(livingEntity instanceof CreeperEntity))) {
                            if (bestTarget == null || livingEntity.distanceTo(cultist) < bestTarget.distanceTo(cultist)) {
                                bestTarget = livingEntity;
                            }
                        }
                    }
                    if (bestTarget != null) {
                        return Optional.of(bestTarget);
                    }
                }
            }
            if (brain.hasMemoryModule(MemoryModuleType.NEAREST_VISIBLE_PLAYER)) {
                Optional<PlayerEntity> player = brain.getOptionalMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER);
                if (player.isPresent() && cultist.getReputation(player.get()) <= -100) {
                    return player;
                }
            }
        }
        return Optional.empty();
    }

    private static boolean isPreferredAttackTarget(HasturCultistEntity cultist, LivingEntity target) {
        return getBestTarget(cultist).filter((livingEntity2) -> livingEntity2 == target).isPresent();
    }

    public static ImmutableList<Task<? super VillagerEntity>> createCoreTasks(HasturCultistEntity cultist, float f) {
        return ImmutableList.of(
                new StayAboveWaterTask(0.8F), new OpenDoorsTask(),
                new LookAroundTask(45, 90),
                new UpdateAttackTargetTask<>(HasturCultistBrain::getBestTarget),
                new ForgetAngryAtTargetTask<>(),
                new WakeUpTask(),
                new StartRaidTask(),
                new WanderAroundTask(),
                new MeetVillagerTask(),
                new HealthCareTask(),
                new WalkToNearestVisibleWantedItemTask(f, false, 4),
                new FindPointOfInterestTask(MMEntities.CONGREGATION_POI, MMEntities.CONGREGATION_POINT, MemoryModuleType.HOME, true, Optional.empty()),
                new FindPointOfInterestTask(PointOfInterestType.MEETING, MemoryModuleType.MEETING_POINT, true, Optional.of((byte) 14)),
                new ForgetAttackTargetTask<>((livingEntity) -> !isPreferredAttackTarget(cultist, livingEntity)));
    }

    public static ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> createMeetTasks(float f) {
        return ImmutableList.of(
                Pair.of(2, new RandomTask(ImmutableList.of(
                        Pair.of(new GoToIfNearbyTask(MemoryModuleType.MEETING_POINT, 0.4F, 40), 2),
                        Pair.of(new GoToIfNearbyTask(MMEntities.CONGREGATION_POINT, 0.4F, 80), 2),
                        Pair.of(new MeetVillagerTask(), 2),
                        Pair.of(new RecruitTask(), 3)))),
                Pair.of(2, new MeetVillagerTask()),
                Pair.of(3, new RecruitTask()),
                Pair.of(2, new VillagerWalkTowardsTask(MemoryModuleType.MEETING_POINT, f, 6, 100, 200)),
                Pair.of(3, new ForgetCompletedPointOfInterestTask(PointOfInterestType.MEETING, MemoryModuleType.MEETING_POINT)),
                Pair.of(99, new ScheduleActivityTask()));
    }

    public static ImmutableList<Task<? super VillagerEntity>> createFightTasks(HasturCultistEntity cultist, float f) {
        return ImmutableList.of(
                new ForgetAttackTargetTask<>((livingEntity) -> !isPreferredAttackTarget(cultist, livingEntity)),
                new ConditionalTask<>(HasturCultistBrain::isAscended, new CastSpellTask()),
                new TacticalApproachTask(f, mob -> mob instanceof HasturCultistEntity && ((HasturCultistEntity) mob).isCasting()),
                new MeleeAttackTask(15));
    }

}
