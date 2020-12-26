package com.miskatonicmysteries.common.lib;

import com.miskatonicmysteries.common.entity.EntityHasturCultist;
import com.miskatonicmysteries.common.entity.EntityProtagonist;
import com.miskatonicmysteries.common.lib.util.RegistryUtil;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerProfessionBuilder;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

public class ModEntities {
    public static final EntityType<EntityProtagonist> PROTAGONIST = FabricEntityTypeBuilder.create(SpawnGroup.MISC, EntityProtagonist::new).spawnableFarFromPlayer().dimensions(EntityDimensions.fixed(0.6F, 1.95F)).trackRangeBlocks(48).build();
    public static final EntityType<EntityHasturCultist> HASTUR_CULTIST = FabricEntityTypeBuilder.create(SpawnGroup.MISC, EntityHasturCultist::new).spawnableFarFromPlayer().dimensions(EntityDimensions.fixed(0.6F, 1.95F)).trackRangeBlocks(16).build();
    public static final PointOfInterestType PSYCHONAUT_POI = PointOfInterestHelper.register(new Identifier(Constants.MOD_ID, "psychonaut"), 1, 1, ModObjects.CHEMISTRY_SET);
    public static final VillagerProfession PSYCHONAUT = VillagerProfessionBuilder.create().id(new Identifier(Constants.MOD_ID, "psychonaut")).workstation(PSYCHONAUT_POI).workSound(SoundEvents.BLOCK_BREWING_STAND_BREW).build();

    public static void init() {
        RegistryUtil.register(Registry.ENTITY_TYPE, "protagonist", PROTAGONIST);
        FabricDefaultAttributeRegistry.register(PROTAGONIST, PathAwareEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 25).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.5F).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40));

        RegistryUtil.register(Registry.ENTITY_TYPE, "hastur_cultist", HASTUR_CULTIST);
        FabricDefaultAttributeRegistry.register(HASTUR_CULTIST, PathAwareEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 20).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.5F).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 10).add(EntityAttributes.GENERIC_ARMOR, 7));

        RegistryUtil.register(Registry.VILLAGER_PROFESSION, "psychonaut", PSYCHONAUT);
    }
}
