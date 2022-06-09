package com.miskatonicmysteries.common.util;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.feature.block.PowerCellBlock;
import com.miskatonicmysteries.common.registry.MMObjects;
import javax.annotation.Nullable;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Constants {

	public static final String MOD_ID = "miskatonicmysteries";
	public static final ItemGroup MM_GROUP = FabricItemGroupBuilder.create(new Identifier(MOD_ID, "group"))
		.icon(() -> new ItemStack(MMObjects.NECRONOMICON)).appendItems(list -> {
			for (Item item : Registry.ITEM) {
				if (Registry.ITEM.getId(item).getNamespace().equals(MOD_ID)) {
					if (item.equals(BlockItem.fromBlock(MMObjects.POWER_CELL))) {
						list.add(PowerCellBlock.getFilledStack());
					} else {
						list.add(new ItemStack(item));
					}
				}
			}
		})
		.build();
	public static final float BLOCK_BIT = 0.0625F;

	public static class Tags {

		public static final TagKey<Item> ALTAR_BOOKS = TagKey.of(Registry.ITEM_KEY, new Identifier(MOD_ID, "altar_books"));
		public static final TagKey<Item> OCEANIC_GOLD_BLOCKS_ITEM = TagKey.of(Registry.ITEM_KEY, new Identifier(MOD_ID, "oceanic_gold_blocks"));
		public static final TagKey<Block> OCEANIC_GOLD_BLOCKS = TagKey.of(Registry.BLOCK_KEY, new Identifier(MOD_ID, "oceanic_gold_blocks"));
		public static final TagKey<Block> SUBTLE_BLOCKS = TagKey.of(Registry.BLOCK_KEY, new Identifier(MOD_ID, "subtle_blocks"));
		public static final TagKey<Block> SUSPICIOUS_BLOCKS = TagKey.of(Registry.BLOCK_KEY, new Identifier(MOD_ID, "suspicious_blocks"));
		public static final TagKey<Block> STABILIZERS = TagKey.of(Registry.BLOCK_KEY, new Identifier(MOD_ID, "stabilizers"));
		public static final TagKey<Block> WEAK_STABILIZERS = TagKey.of(Registry.BLOCK_KEY, new Identifier(MOD_ID, "weak_stabilizers"));
		public static final TagKey<Block> STRONG_STABILIZERS = TagKey.of(Registry.BLOCK_KEY, new Identifier(MOD_ID, "strong_stabilizers"));

		public static final TagKey<Block> PILLAR_BOTTOM = TagKey.of(Registry.BLOCK_KEY, new Identifier(MOD_ID, "pillar_bottom"));
		public static final TagKey<Block> PILLAR_MIDDLE = TagKey.of(Registry.BLOCK_KEY, new Identifier(MOD_ID, "pillar_middle"));
		public static final TagKey<Block> PILLAR_TOP = TagKey.of(Registry.BLOCK_KEY, new Identifier(MOD_ID, "pillar_top"));


		public static final TagKey<Item> RED_MEAT = TagKey.of(Registry.ITEM_KEY, new Identifier(MOD_ID, "red_meat"));
		public static final TagKey<Item> YELLOW_DYE = TagKey.of(Registry.ITEM_KEY, new Identifier(MOD_ID, "yellow_dye"));
		public static final TagKey<Item> RITE_TOOLS = TagKey.of(Registry.ITEM_KEY, new Identifier(MOD_ID, "rite_tools"));
		public static final TagKey<Item> GROSS_FOOD = TagKey.of(Registry.ITEM_KEY, new Identifier(MOD_ID, "gross_food"));
		public static final TagKey<Item> WARDING_MARK_DYE = TagKey.of(Registry.ITEM_KEY, new Identifier(MOD_ID, "warding_mark_dye"));
		public static final TagKey<Item> CULTIST_ARMOR = TagKey.of(Registry.ITEM_KEY, new Identifier(MOD_ID, "cultist_armor"));

		public static final TagKey<Item> HASTUR_CULTIST_OFFERINGS = TagKey.of(Registry.ITEM_KEY, new Identifier(MOD_ID, "hastur_cultist_offerings"));
		public static final TagKey<Item> HASTUR_STATUES = TagKey.of(Registry.ITEM_KEY, new Identifier(MOD_ID, "hastur_statues"));

		public static final TagKey<EntityType<?>> BURNED_VEIL_MOBS = TagKey.of(Registry.ENTITY_TYPE_KEY,  new Identifier(MOD_ID, "burned_veil_mobs"));
		public static final TagKey<EntityType<?>> BROKEN_VEIL_MOBS = TagKey.of(Registry.ENTITY_TYPE_KEY,  new Identifier(MOD_ID, "broken_veil_mobs"));
		public static final TagKey<EntityType<?>> VALID_SACRIFICES = TagKey.of(Registry.ENTITY_TYPE_KEY,  new Identifier(MOD_ID, "valid_sacrifices"));
	}

	public static class NBT {

		public static final String RECEIVED_STACK = "ReceivedStack";
		public static final String REALIZED_STACK = "RealizedStack";
		public static final String POTENTIAL_ITEMS = "PotentialItems";
		public static final String SHOTS = "Shots";
		public static final String LOADING = "Loading";

		public static final String MISK_DATA = "MiskMystData";
		public static final String SANITY = "Sanity";
		public static final String SHOCKED = "Shocked";
		public static final String SANITY_EXPANSIONS = "SanityExpansions";
		public static final String WORK_PROGRESS = "WorkProgress";
		public static final String VARIANT = "Variant";
		public static final String STAGE = "Stage";
		public static final String ALTERNATE_WEAPON = "AlternateWeapon";
		public static final String CHARGING = "Charging";
		public static final String PLAYER_UUID = "PlayerUUID";
		public static final String PROTAGONISTS = "Protagonists";
		public static final String SPAWNED = "Spawned";
		public static final String SPELL_EFFECT = "SpellEffect";
		public static final String SPELL_MEDIUM = "SpellMedium";
		public static final String INTENSITY = "Intensity";
		public static final String CASTING = "Casting";
		public static final String SPELL = "Spell";
		public static final String TICK_COUNT = "TickCount";
		public static final String RITE = "Rite";
		public static final String KNOWLEDGE = "Knowledge";
		public static final String PERMANENT_RITE = "PermanentRite";
		public static final String AFFILIATION = "Affiliation";
		public static final String APPARENT_AFFILIATION = "ApparentAffiliation";
		public static final String POSITION = "ConnectedPosition";
		public static final String DIMENSION = "ConnectedDimension";
		public static final String BLOCK_ENTITY_TAG = "BlockEntityTag";
		public static final String BANNER_BASE = "Base";
		public static final String BANNER_PATTERN = "Pattern";
		public static final String BANNER_COLOR = "Color";
		public static final String BANNER_PP_TAG = "Bannerpp_LoomPatterns";
		public static final String POWER_POOL = "MaxPower";
		public static final String MAX_SPELLS = "MaxSpells";
		public static final String SPELL_LIST = "SpellList";
		public static final String SPELL_EFFECTS = "EffectList";
		public static final String SPELL_MEDIUMS = "MediumList";
		public static final String TRIGGERED = "Triggered";
		public static final String SHOULD_DROP = "ShouldDrop";
		public static final String BLESSINGS = "Blessings";
		public static final String ASCENSION_STAGE = "AscensionStage";
		public static final String RESONANCE = "Resonance";
		public static final String RADIUS = "Radius";
		public static final String ENERGY = "Energy";
		public static final String PLAYER_NAME = "PlayerName";
		public static final String SPELL_COOLDOWN = "SpellCooldown";
		public static final String BROAD_SWING = "BroadSwing";
		public static final String OWNER = "Owner";
		public static final String TARGET = "Target";
		public static final String APPEASE_TICKS = "AppeaseTicks";
		public static final String MONSTER = "isMonster";
		public static final String FLAGS = "Flags";
		public static final String HALLUCINATION = "Hallucination";
		public static final String MAX_AGE = "MaxAge";
		public static final String WARDING_MARKS = "WardingMarks";
		public static final String GLIDING = "Gliding";
		public static final String KNOTS = "Knots";
		public static final String STATUE_OWNER = "StatueOwner";
		public static final String POSE = "Pose";
		public static final String NEXT_ID = "NextAvailableID";
		public static final String PARTIES = "Parties";
		public static final String ID = "Id";
		public static final String CENTER_POS = "CenterPos";
		public static final String PARTY_POWER = "PartyPower";
		public static final String STATUS = "Status";
		public static final String BONUS_COOLDOWN = "BonusCooldown";
		public static final String MUSIC_SOURCES = "MusicSources";
		public static final String HIDDEN = "MM_Hidden";
		public static final String INSTABILITY = "Instability";
		public static final String SIMPLE = "IsSimple";
		public static final String PHASE_TICKS = "PhaseTicks";
		public static final String PHASE_DIRECTION = "PhaseDirection";
		public static final String HOUNDS = "Hounds";
		public static final String ACTIVE = "Active";
		public static final String KNOT_POS = "KnotPos";
		public static final String IS_CORE = "IsCore";
	}

	public static class DataTrackers {

		public static final int SANITY_CAP = 1000;
		public static final int PROTAGONIST_MAX_LEVEL = 3;

		public static final TrackedData<Integer> SANITY = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
		public static final TrackedData<Boolean> SHOCKED = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

		public static final TrackedData<Integer> POWER_POOL = DataTracker
			.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
		public static final TrackedData<Integer> MAX_SPELLS = DataTracker
			.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
		public static final TrackedData<Integer> STAGE = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
		public static final TrackedData<Integer> SPELL_COOLDOWN = DataTracker
			.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
		public static final int SPELL_CAP = 10;
		public static final int MIN_SPELLS = 3;

		public static final TrackedData<Affiliation> AFFILIATION = DataTracker
			.registerData(PlayerEntity.class, MiskatonicMysteriesAPI.AFFILIATION_TRACKER);
		public static final TrackedData<Affiliation> APPARENT_AFFILIATION = DataTracker
			.registerData(PlayerEntity.class, MiskatonicMysteriesAPI.AFFILIATION_TRACKER);
		public static final int MAX_BLESSINGS = 3;

		public static final TrackedData<Float> RESONANCE = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);
	}

	public static class DamageSources extends DamageSource {

		public static final DamageSource SLEEP = new DamageSources("sleep").setBypassesArmor();
		public static final DamageSource INSANITY = new DamageSources("insanity") {
			@Override
			public Text getDeathMessage(LivingEntity entity) {
				return new TranslatableText(String.format("death.attack." + name + ".%d", entity.getRandom().nextInt(3)),
					entity.getDisplayName());
			}
		}.setBypassesArmor();

		protected DamageSources(String name) {
			super(Constants.MOD_ID + "." + name);
		}

		public static class ProtagonistDamageSource extends EntityDamageSource {

			public ProtagonistDamageSource(@Nullable Entity source) {
				super(Constants.MOD_ID + ".protagonist", source);
			}

			@Override
			public Text getDeathMessage(LivingEntity entity) {
				return new TranslatableText(String.format("death.attack." + name + ".%d", entity.getRandom().nextInt(4)),
					entity.getDisplayName());
			}
		}
	}

	public static class BlockSettings {

		public static final AbstractBlock.Settings OCEANIC_GOLD = AbstractBlock.Settings.of(Material.METAL).strength(1F, 5F).requiresTool();
		public static final AbstractBlock.Settings ELDERIAN = AbstractBlock.Settings.of(Material.STONE).strength(1F, 5F).requiresTool();
	}

	public static class Misc {

		public static final String NECRONOMICON_EXTENSION = "readBook";
		public static final String ATE_ORB_EXTENSION = "orb";
		public static final String WITCH_KNOWLEDGE = "witch";
		public static final String EVOKER_KNOWLEDGE = "evoker";
		public static final String ATE_CIRRHOSUS_FLESH = "cirrhosus";
	}
}
