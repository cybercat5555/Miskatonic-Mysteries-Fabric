package com.miskatonicmysteries.lib;

import com.miskatonicmysteries.common.entity.EntityProtagonist;
import com.miskatonicmysteries.lib.util.Util;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntityWithAi;
import net.minecraft.util.registry.Registry;

public class ModEntities {
    public static final EntityType<EntityProtagonist> PROTAGONIST = FabricEntityTypeBuilder.create(SpawnGroup.MISC, EntityProtagonist::new).spawnableFarFromPlayer().dimensions(EntityDimensions.fixed(0.6F, 1.95F)).trackable(48, 6).build();

    public static void init() {
        Util.register(Registry.ENTITY_TYPE, "protagonist", PROTAGONIST);
        FabricDefaultAttributeRegistry.register(PROTAGONIST, MobEntityWithAi.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 25).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.5F).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40));
    }
}
