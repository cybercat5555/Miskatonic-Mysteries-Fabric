package com.miskatonicmysteries.common.lib;

import com.miskatonicmysteries.common.entity.HasturCultistEntity;
import com.miskatonicmysteries.common.entity.ProtagonistEntity;
import com.miskatonicmysteries.common.lib.util.RegistryUtil;
import com.miskatonicmysteries.mixin.villagers.MemoryModuleTypeAccessor;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerProfessionBuilder;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.GlobalPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

public class ModEntities {
    public static final EntityType<ProtagonistEntity> PROTAGONIST = FabricEntityTypeBuilder.create(SpawnGroup.MISC, ProtagonistEntity::new).spawnableFarFromPlayer().dimensions(EntityDimensions.fixed(0.6F, 1.95F)).trackRangeBlocks(48).build();
    public static final EntityType<HasturCultistEntity> HASTUR_CULTIST = FabricEntityTypeBuilder.create(SpawnGroup.MISC, HasturCultistEntity::new).spawnableFarFromPlayer().dimensions(EntityDimensions.fixed(0.6F, 1.95F)).trackRangeBlocks(16).build();
    public static final PointOfInterestType PSYCHONAUT_POI = PointOfInterestHelper.register(new Identifier(Constants.MOD_ID, "psychonaut"), 1, 1, ModObjects.CHEMISTRY_SET);
    public static final VillagerProfession PSYCHONAUT = VillagerProfessionBuilder.create().id(new Identifier(Constants.MOD_ID, "psychonaut")).workstation(PSYCHONAUT_POI).workSound(SoundEvents.BLOCK_BREWING_STAND_BREW).build();

    public static final PointOfInterestType CONGREGATION_POI = PointOfInterestHelper.register(new Identifier(Constants.MOD_ID, "yelow_serf"), 48, poi -> false, 100,
            ModObjects.TERRACOTTA_HASTUR_MURAL, ModObjects.YELLOW_TERRACOTTA_HASTUR_MURAL, ModObjects.STONE_HASTUR_MURAL,
            ModObjects.HASTUR_STATUE_GOLD, ModObjects.HASTUR_STATUE_MOSSY, ModObjects.HASTUR_STATUE_STONE, ModObjects.HASTUR_STATUE_TERRACOTTA);
    public static final VillagerProfession YELLOW_SERF = VillagerProfessionBuilder.create().id(new Identifier(Constants.MOD_ID, "yellow_serf")).workstation(PointOfInterestType.NITWIT).workSound(SoundEvents.ENTITY_VILLAGER_WORK_CLERIC).build();
    public static final MemoryModuleType<GlobalPos> CONGREGATION_POINT = MemoryModuleTypeAccessor.invokeRegister(
            Constants.MOD_ID + ":congregation_point",
            GlobalPos.CODEC
    );
    //todo prevent villagers from acquiring this profession
    //probably rework this

    public static void init() {
        RegistryUtil.register(Registry.ENTITY_TYPE, "protagonist", PROTAGONIST);
        FabricDefaultAttributeRegistry.register(PROTAGONIST, PathAwareEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 25).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.24D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.5F).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40));

        RegistryUtil.register(Registry.ENTITY_TYPE, "hastur_cultist", HASTUR_CULTIST);
        FabricDefaultAttributeRegistry.register(HASTUR_CULTIST, PathAwareEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 25).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3F).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 10).add(EntityAttributes.GENERIC_ARMOR, 4));

        RegistryUtil.register(Registry.VILLAGER_PROFESSION, "psychonaut", PSYCHONAUT);
        RegistryUtil.register(Registry.VILLAGER_PROFESSION, "yellow_serf", YELLOW_SERF);
    }
}
