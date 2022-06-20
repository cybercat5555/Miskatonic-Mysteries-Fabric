package com.miskatonicmysteries.common.registry;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.common.feature.entity.*;
import com.miskatonicmysteries.common.feature.entity.painting.ManosPaintingEntity;
import com.miskatonicmysteries.common.feature.entity.painting.WallPaintingEntity;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.RegistryUtil;
import com.miskatonicmysteries.mixin.villagers.MemoryModuleTypeAccessor;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerProfessionBuilder;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityType.EntityFactory;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction.Location;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.decoration.painting.PaintingMotive;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.GlobalPos;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.List;

public class MMEntities {

	public static final EntityType<ProtagonistEntity> PROTAGONIST = FabricEntityTypeBuilder.create(SpawnGroup.MISC, ProtagonistEntity::new)
		.spawnableFarFromPlayer().dimensions(EntityDimensions.fixed(0.6F, 1.95F)).trackRangeBlocks(48).build();
	public static final EntityType<HasturCultistEntity> HASTUR_CULTIST = FabricEntityTypeBuilder
		.create(SpawnGroup.AMBIENT, HasturCultistEntity::new).spawnableFarFromPlayer().dimensions(EntityDimensions.fixed(0.6F, 1.95F))
		.trackRangeBlocks(16).build();
	public static final EntityType<SpellProjectileEntity> SPELL_PROJECTILE = FabricEntityTypeBuilder
		.<SpellProjectileEntity>create(SpawnGroup.MISC, SpellProjectileEntity::new).dimensions(EntityDimensions.fixed(0.5F, 0.5F))
		.trackRangeBlocks(4).build();
	public static final EntityType<BoltEntity> BOLT = FabricEntityTypeBuilder.<BoltEntity>create(SpawnGroup.MISC, BoltEntity::new)
		.disableSaving().dimensions(EntityDimensions.fixed(0.0F, 0.0F)).trackRangeBlocks(16).trackedUpdateRate(Integer.MAX_VALUE).build();
	public static final EntityType<PhantasmaEntity> PHANTASMA = FabricEntityTypeBuilder.create(SpawnGroup.AMBIENT, PhantasmaEntity::new)
		.dimensions(EntityDimensions.fixed(0.6F, 0.6F)).trackRangeBlocks(12).build();
	public static final EntityType<AberrationEntity> ABERRATION = FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, AberrationEntity::new)
		.dimensions(EntityDimensions.fixed(0.7F, 0.7F)).trackRangeBlocks(16).build();
	public static final EntityType<TatteredPrinceEntity> TATTERED_PRINCE = FabricEntityTypeBuilder
		.create(SpawnGroup.MISC, TatteredPrinceEntity::new).dimensions(EntityDimensions.fixed(1.5F, 4)).trackRangeBlocks(48).build();
	public static final EntityType<GenericTentacleEntity> GENERIC_TENTACLE = FabricEntityTypeBuilder
		.create(SpawnGroup.MONSTER, GenericTentacleEntity::new).dimensions(EntityDimensions.fixed(0.5F, 2)).trackRangeBlocks(16).build();
	public static final EntityType<HarrowEntity> HARROW = FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, HarrowEntity::new)
		.dimensions(EntityDimensions.fixed(0.35F, 0.35F)).trackRangeBlocks(16).build();
	public static final EntityType<ByakheeEntity> BYAKHEE = FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, ByakheeEntity::new)
		.dimensions(EntityDimensions.fixed(2, 2)).trackRangeBlocks(16).build();
	public static final EntityType<HallucinationEntity> HALLUCINATION = FabricEntityTypeBuilder
		.<HallucinationEntity>create(SpawnGroup.MISC, HallucinationEntity::new).dimensions(EntityDimensions.fixed(1, 1))
		.trackRangeBlocks(16).build();
	public static final EntityType<TindalosHoundEntity> TINDALOS_HOUND = FabricEntityTypeBuilder
		.create(SpawnGroup.MONSTER, TindalosHoundEntity::new).dimensions(EntityDimensions.fixed(2F, 2F)).trackRangeBlocks(16).build();
	public static final EntityType<ManosPaintingEntity> GUARDIAN_PAINTING = FabricEntityTypeBuilder
		.create(SpawnGroup.MISC, (EntityFactory<ManosPaintingEntity>) ManosPaintingEntity::new)
		.dimensions(EntityDimensions.changing(0.5F, 0.5F)).trackRangeBlocks(10)
		.trackedUpdateRate(Integer.MAX_VALUE).build();
	public static final EntityType<WallPaintingEntity> WALL_PAINTING = FabricEntityTypeBuilder
		.create(SpawnGroup.MISC, (EntityFactory<WallPaintingEntity>) WallPaintingEntity::new)
		.dimensions(EntityDimensions.changing(0.5F, 0.5F)).trackRangeBlocks(10)
		.trackedUpdateRate(Integer.MAX_VALUE).build();
	public static final EntityType<GuardDogEntity> GUARD_DOG = FabricEntityTypeBuilder
		.create(SpawnGroup.CREATURE, (EntityFactory<GuardDogEntity>) GuardDogEntity::new)
		.dimensions(EntityDimensions.fixed(1F, 1F)).disableSaving().trackRangeBlocks(16).build();

	public static final PointOfInterestType PSYCHONAUT_POI = PointOfInterestHelper
		.register(new Identifier(Constants.MOD_ID, "psychonaut"), 1, 1, MMObjects.CHEMISTRY_SET);
	public static final VillagerProfession PSYCHONAUT = VillagerProfessionBuilder.create()
		.id(new Identifier(Constants.MOD_ID, "psychonaut")).workstation(PSYCHONAUT_POI).workSound(SoundEvents.BLOCK_BREWING_STAND_BREW)
		.build();

	public static final EntityType<FeasterEntity> FEASTER = FabricEntityTypeBuilder
		.create(SpawnGroup.MONSTER, FeasterEntity::new).dimensions(EntityDimensions.fixed(2F, 3F)).trackRangeBlocks(64).build();

	public static final MemoryModuleType<GlobalPos> CONGREGATION_POINT = MemoryModuleTypeAccessor.invokeRegister(
		Constants.MOD_ID + ":congregation_point",
		GlobalPos.CODEC
	);
	public static final PointOfInterestType HASTUR_POI = PointOfInterestHelper
		.register(new Identifier(Constants.MOD_ID, "hastur_poi"), 0, 1,
				  MMObjects.HASTUR_OCTAGRAM, MMObjects.HASTUR_STATUE_STONE, MMObjects.HASTUR_STATUE_TERRACOTTA, MMObjects.HASTUR_STATUE_GOLD,
				  MMObjects.HASTUR_STATUE_MOSSY, MMObjects.MOSSY_HASTUR_MURAL, MMObjects.STONE_HASTUR_MURAL, MMObjects.TERRACOTTA_HASTUR_MURAL,
				  MMObjects.YELLOW_TERRACOTTA_HASTUR_MURAL);

	public static final TrackedDataHandler<EntityType<?>> ENTITY_TYPE_TRACKER = new TrackedDataHandler<>() {
		@Override
		public void write(PacketByteBuf data, EntityType<?> object) {
			data.writeIdentifier(Registry.ENTITY_TYPE.getId(object));
		}

		@Override
		public EntityType<?> read(PacketByteBuf packetByteBuf) {
			return Registry.ENTITY_TYPE.get(packetByteBuf.readIdentifier());
		}

		@Override
		public EntityType<?> copy(EntityType object) {
			return object;
		}
	};

	public static void init() {
		TrackedDataHandlerRegistry.register(MiskatonicMysteriesAPI.AFFILIATION_TRACKER);
		TrackedDataHandlerRegistry.register(ENTITY_TYPE_TRACKER);
		RegistryUtil.register(Registry.ENTITY_TYPE, "protagonist", PROTAGONIST);
		FabricDefaultAttributeRegistry.register(PROTAGONIST, PathAwareEntity.createMobAttributes()
			.add(EntityAttributes.GENERIC_MAX_HEALTH, 25)
			.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.24D)
			.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.5F)
			.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40));

		RegistryUtil.register(Registry.ENTITY_TYPE, "hastur_cultist", HASTUR_CULTIST);
		FabricDefaultAttributeRegistry.register(HASTUR_CULTIST, PathAwareEntity.createMobAttributes()
			.add(EntityAttributes.GENERIC_MAX_HEALTH, 25)
			.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5)
			.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3F)
			.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 10)
			.add(EntityAttributes.GENERIC_ARMOR, 4));

		RegistryUtil.register(Registry.ENTITY_TYPE, "spell_projectile", SPELL_PROJECTILE);
		RegistryUtil.register(Registry.ENTITY_TYPE, "bolt", BOLT);

		RegistryUtil.register(Registry.ENTITY_TYPE, "phantasma", PHANTASMA);
		FabricDefaultAttributeRegistry.register(PHANTASMA, PathAwareEntity.createMobAttributes()
			.add(EntityAttributes.GENERIC_MAX_HEALTH, 10)
			.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D)
			.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.5F)
			.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 6)
			.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.65F)
			.add(EntityAttributes.GENERIC_FLYING_SPEED, 0.2F));

		RegistryUtil.register(Registry.ENTITY_TYPE, "aberration", ABERRATION);
		FabricDefaultAttributeRegistry.register(ABERRATION, HostileEntity.createHostileAttributes()
			.add(EntityAttributes.GENERIC_MAX_HEALTH, 16)
			.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5D)
			.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.5F)
			.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 12)
			.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.65F)
			.add(EntityAttributes.GENERIC_FLYING_SPEED, 0.4F));

		RegistryUtil.register(Registry.ENTITY_TYPE, "tattered_prince", TATTERED_PRINCE);
		FabricDefaultAttributeRegistry.register(TATTERED_PRINCE, PathAwareEntity.createMobAttributes()
			.add(EntityAttributes.GENERIC_MAX_HEALTH, 250)
			.add(EntityAttributes.GENERIC_ARMOR, 9)
			.add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 10)
			.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.24D)
			.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15)
			.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 24)
			.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 3)
			.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.8F));

		RegistryUtil.register(Registry.ENTITY_TYPE, "energy_tentacle", GENERIC_TENTACLE);
		FabricDefaultAttributeRegistry.register(GENERIC_TENTACLE, MobEntity.createMobAttributes()
			.add(EntityAttributes.GENERIC_MAX_HEALTH, 15)
			.add(EntityAttributes.GENERIC_ARMOR, 10)
			.add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 10)
			.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0)
			.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 7)
			.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1)
			.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 6)
			.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1));

		RegistryUtil.register(Registry.ENTITY_TYPE, "harrow", HARROW);
		FabricDefaultAttributeRegistry.register(HARROW, HarrowEntity.createHarrowAttributes()
			.add(EntityAttributes.GENERIC_MAX_HEALTH, 10)
			.add(EntityAttributes.GENERIC_ARMOR, 4)
			.add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 10)
			.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5D)
			.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5)
			.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 10)
			.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.6F));

		RegistryUtil.register(Registry.ENTITY_TYPE, "byakhee", BYAKHEE);
		FabricDefaultAttributeRegistry.register(BYAKHEE, TameableEntity.createMobAttributes()
			.add(EntityAttributes.GENERIC_MAX_HEALTH, 40)
			.add(EntityAttributes.GENERIC_ARMOR, 6)
			.add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 10)
			.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.314D)
			.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5)
			.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 10)
			.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.4F));

		RegistryUtil.register(Registry.ENTITY_TYPE, "hallucination", HALLUCINATION);
		FabricDefaultAttributeRegistry.register(HALLUCINATION, HallucinationEntity.createAttributes());

		RegistryUtil.register(Registry.ENTITY_TYPE, "tindalos_hound", TINDALOS_HOUND);
		FabricDefaultAttributeRegistry.register(TINDALOS_HOUND, TindalosHoundEntity.createAttributes());

		RegistryUtil.register(Registry.ENTITY_TYPE, "feaster", FEASTER);
		FabricDefaultAttributeRegistry.register(FEASTER, FeasterEntity.createAttributes());

		RegistryUtil.register(Registry.ENTITY_TYPE, "guard_dog", GUARD_DOG);
		FabricDefaultAttributeRegistry.register(GUARD_DOG, MobEntity.createMobAttributes()
			.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f)
			.add(EntityAttributes.GENERIC_MAX_HEALTH, 15.0)
			.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0));

		RegistryUtil.register(Registry.ENTITY_TYPE, "guardian_painting", GUARDIAN_PAINTING);
		RegistryUtil.register(Registry.ENTITY_TYPE, "wall_painting", WALL_PAINTING);

		RegistryUtil.register(Registry.VILLAGER_PROFESSION, "psychonaut", PSYCHONAUT);

		DispenserBlock.registerBehavior(Items.YELLOW_CARPET, new FallibleItemDispenserBehavior() {
			public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
				BlockPos blockPos = pointer.getPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
				List<ByakheeEntity> list = pointer.getWorld()
					.getEntitiesByClass(ByakheeEntity.class, new Box(blockPos), (entity) -> !entity.isDecorated());
				if (!list.isEmpty()) {
					ByakheeEntity entity = list.get(0);
					entity.items.setStack(1, stack.split(1));
					pointer.getWorld()
						.playSoundFromEntity(null, entity, SoundEvents.ENTITY_HORSE_SADDLE, SoundCategory.NEUTRAL, 0.5F, 1.0F);
					entity.updateSaddle();
					this.setSuccess(true);
					return stack;
				} else {
					return super.dispenseSilently(pointer, stack);
				}
			}
		});
		SpawnRestrictionAccessor.callRegister(HARROW, Location.NO_RESTRICTIONS, Type.MOTION_BLOCKING_NO_LEAVES, HarrowEntity::canSpawn);
		SpawnRestrictionAccessor.callRegister(GENERIC_TENTACLE, Location.ON_GROUND, Type.WORLD_SURFACE, GenericTentacleEntity::canSpawn);

		PaintingMotives.init();
	}

	public static class PaintingMotives {

		public static PaintingMotive GUARDIAN = new PaintingMotive(16, 16);
		public static PaintingMotive BLACK_STAR = new PaintingMotive(32, 32);
		public static PaintingMotive SHINING_GATES = new PaintingMotive(32, 32);

		public static void init() {
			RegistryUtil.register(Registry.PAINTING_MOTIVE, "guardian", GUARDIAN);
			RegistryUtil.register(Registry.PAINTING_MOTIVE, "black_star", BLACK_STAR);
			RegistryUtil.register(Registry.PAINTING_MOTIVE, "shining_gates", SHINING_GATES);
		}
	}
}
