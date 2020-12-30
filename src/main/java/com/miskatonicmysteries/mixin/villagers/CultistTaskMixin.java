package com.miskatonicmysteries.mixin.villagers;

import com.google.common.collect.ImmutableList;
import com.miskatonicmysteries.common.entity.ai.task.HealthCareTask;
import com.miskatonicmysteries.common.entity.ai.task.RecruitTask;
import com.miskatonicmysteries.common.lib.ModEntities;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(VillagerTaskListProvider.class)
public class CultistTaskMixin {
    @Inject(method = "createMeetTasks", at = @At("HEAD"), cancellable = true)
    private static void createMeetTasks(VillagerProfession profession, float f, CallbackInfoReturnable<ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>>> cir) {
        if (profession == ModEntities.YELLOW_SERF) {
            cir.setReturnValue(ImmutableList.of(
                    Pair.of(2, new RandomTask(ImmutableList.of(
                            Pair.of(new GoToIfNearbyTask(MemoryModuleType.MEETING_POINT, 0.4F, 40), 2),
                            Pair.of(new GoToIfNearbyTask(ModEntities.CONGREGATION_POINT, 0.4F, 80), 2),
                            Pair.of(new MeetVillagerTask(), 2),
                            Pair.of(new RecruitTask(), 3)))),
                    Pair.of(2, new MeetVillagerTask()),
                    Pair.of(3, new RecruitTask()),
                    Pair.of(2, new VillagerWalkTowardsTask(MemoryModuleType.MEETING_POINT, f, 6, 100, 200)),
                    Pair.of(3, new ForgetCompletedPointOfInterestTask(PointOfInterestType.MEETING, MemoryModuleType.MEETING_POINT)),
                    Pair.of(99, new ScheduleActivityTask())
            ));
        }
    }

    @Inject(method = "createCoreTasks", at = @At("HEAD"), cancellable = true)
    private static void createCoreTasks(VillagerProfession profession, float f, CallbackInfoReturnable<ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>>> cir) {
        if (profession == ModEntities.YELLOW_SERF) {
            cir.setReturnValue(ImmutableList.of(
                    Pair.of(0, new StayAboveWaterTask(0.8F)),
                    Pair.of(0, new OpenDoorsTask()),
                    Pair.of(0, new LookAroundTask(45, 90)),
                    Pair.of(0, new WakeUpTask()),
                    Pair.of(0, new StartRaidTask()),
                    Pair.of(0, new ForgetCompletedPointOfInterestTask(profession.getWorkStation(), MemoryModuleType.JOB_SITE)),
                    Pair.of(0, new ForgetCompletedPointOfInterestTask(profession.getWorkStation(), MemoryModuleType.POTENTIAL_JOB_SITE)),
                    Pair.of(1, new WanderAroundTask()),
                    Pair.of(1, new MeetVillagerTask()),
                    Pair.of(1, new HealthCareTask()),
                    Pair.of(5, new WalkToNearestVisibleWantedItemTask(f, false, 4)),
                    Pair.of(6, new FindPointOfInterestTask(ModEntities.CONGREGATION_POI, ModEntities.CONGREGATION_POINT, MemoryModuleType.HOME, true, Optional.empty())),
                    Pair.of(10, new FindPointOfInterestTask(PointOfInterestType.MEETING, MemoryModuleType.MEETING_POINT, true, Optional.of((byte) 14)))));
        }
    }
}
