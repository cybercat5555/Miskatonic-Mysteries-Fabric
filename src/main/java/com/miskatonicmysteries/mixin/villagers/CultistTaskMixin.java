package com.miskatonicmysteries.mixin.villagers;

import net.minecraft.entity.ai.brain.task.VillagerTaskListProvider;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(VillagerTaskListProvider.class)
public class CultistTaskMixin {
/*    @Inject(method = "createMeetTasks", at = @At("HEAD"), cancellable = true)
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
            cir.setReturnValue(HasturCultistEntity.createCoreTasks(f));
        }
    }

    @Inject(method = "createRestTasks", at = @At("HEAD"), cancellable = true)
    private static void createRestTasks(VillagerProfession profession, float f, CallbackInfoReturnable<ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>>> cir) {
        if (profession == ModEntities.YELLOW_SERF) {
            cir.setReturnValue(HasturCultistEntity.createNightlyTasks(f));
        }
    }

    @Inject(method = "createPanicTasks", at = @At("HEAD"), cancellable = true)
    private static void createPanicTasks(VillagerProfession profession, float f, CallbackInfoReturnable<ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>>> cir) {
        if (profession == ModEntities.YELLOW_SERF) {
            cir.setReturnValue(ImmutableList.of(
                    Pair.of(0, new UpdateAttackTargetTask<>(HasturCultistEntity::getPossibleTarget)),
                    Pair.of(99, new ScheduleActivityTask())
                    //fight tasks, also in core
            ));
        }
    }*/
}
