package com.miskatonicmysteries.common.lib;

import com.miskatonicmysteries.common.feature.Affiliation;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.tag.TagRegistry;
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
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.tag.Tag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;

public class Constants {
    public static final String MOD_ID = "miskatonicmysteries";
    public static final ItemGroup MM_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "group"), () -> new ItemStack(MMObjects.NECRONOMICON));
    public static final float BLOCK_BIT = 0.0625F;

    public static class Tags {
        public static final Tag<Item> ALTAR_BOOKS = TagRegistry.item(new Identifier(MOD_ID, "altar_books"));
        public static final Tag<Item> OCEANIC_GOLD_BLOCKS_ITEM = TagRegistry.item(new Identifier(MOD_ID, "oceanic_gold_blocks"));
        public static final Tag<Block> OCEANIC_GOLD_BLOCKS = TagRegistry.block(new Identifier(MOD_ID, "oceanic_gold_blocks"));
        public static final Tag<Block> SUBTLE_BLOCKS = TagRegistry.block(new Identifier(MOD_ID, "subtle_blocks"));
        public static final Tag<Block> SUSPICIOUS_BLOCKS = TagRegistry.block(new Identifier(MOD_ID, "suspicious_blocks"));

        public static final Tag<Item> RED_MEAT = TagRegistry.item(new Identifier(MOD_ID, "red_meat"));
        public static final Tag<Item> YELLOW_DYE = TagRegistry.item(new Identifier(MOD_ID, "yellow_dye"));
        public static final Tag<Item> RITE_TOOLS = TagRegistry.item(new Identifier(MOD_ID, "rite_tools"));
        public static final Tag<EntityType<?>> BURNED_VEIL_MOBS = TagRegistry.entityType(new Identifier(MOD_ID, "burned_veil_mobs"));
        public static final Tag<EntityType<?>> BROKEN_VEIL_MOBS = TagRegistry.entityType(new Identifier(MOD_ID, "broken_veil_mobs"));
    }

    public static class NBT {
        public static final String RECEIVED_STACK = "Received Stack";
        public static final String REALIZED_STACK = "Realized Stack";
        public static final String POTENTIAL_ITEMS = "Potential Items";
        public static final String SHOTS = "Shots";
        public static final String LOADING = "Loading";

        public static final String MISK_DATA = "MiskMyst Data";
        public static final String SANITY = "Sanity";
        public static final String SHOCKED = "Shocked";
        public static final String SANITY_EXPANSIONS = "Sanity Expansions";
        public static final String WORK_PROGRESS = "Work Progress";
        public static final String VARIANT = "Variant";
        public static final String STAGE = "Stage";
        public static final String ALTERNATE_WEAPON = "Alternate Weapon";
        public static final String CHARGING = "Charging";
        public static final String PLAYER_UUID = "Player UUID";
        public static final String PROTAGONISTS = "Protagonists";
        public static final String SPAWNED = "Spawned";
        public static final String SPELL_EFFECT = "Spell Effect";
        public static final String SPELL_MEDIUM = "Spell Medium";
        public static final String INTENSITY = "Intensity";
        public static final String CASTING = "Casting";
        public static final String SPELL = "Spell";
        public static final String TICK_COUNT = "Tick Count";
        public static final String RITE = "Rite";
        public static final String KNOWLEDGE = "Knowledge";
        public static final String PERMANENT_RITE = "Permanent Rite";
        public static final String AFFILIATION = "Affiliation";
        public static final String APPARENT_AFFILIATION = "Apparent Affiliation";
        public static final String POSITION = "Connected Position";
        public static final String DIMENSION = "Connected Dimension";
        public static final String BLOCK_ENTITY_TAG = "BlockEntityTag";
        public static final String BANNER_BASE = "Base";
        public static final String BANNER_PATTERN = "Pattern";
        public static final String BANNER_COLOR = "Color";
        public static final String BANNER_PP_TAG = "Bannerpp_LoomPatterns";
        public static final String POWER_POOL = "Max Power";
        public static final String MAX_SPELLS = "Max Spells";
        public static final String SPELL_LIST = "Spell List";
        public static final String SPELL_EFFECTS = "Effect List";
        public static final String SPELL_MEDIUMS = "Medium Map";
        public static final String TRIGGERED = "Triggered";
        public static final String SHOULD_DROP = "Should Drop";
    }

    public static class DataTrackers {
        public static final TrackedDataHandler<Affiliation> AFFILIATION_TRACKER = new TrackedDataHandler<Affiliation>() {
            public void write(PacketByteBuf packetByteBuf, Affiliation affiliation) {
                packetByteBuf.writeIdentifier(affiliation.getId());
            }

            public Affiliation read(PacketByteBuf packetByteBuf) {
                return Affiliation.AFFILIATION_MAP.getOrDefault(packetByteBuf.readIdentifier(), Affiliation.NONE);
            }

            public Affiliation copy(Affiliation affiliation) {
                return affiliation;
            }
        };
        public static final int SANITY_CAP = 1000;
        public static final int PROTAGONIST_MAX_LEVEL = 3;

        public static final TrackedData<Integer> SANITY = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
        public static final TrackedData<Boolean> SHOCKED = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

        public static final TrackedData<Integer> POWER_POOL = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
        public static final TrackedData<Integer> MAX_SPELLS = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
        public static final TrackedData<Integer> LEVEL = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
        public static final int SPELL_CAP = 10;


        public static final TrackedData<Affiliation> AFFILIATION = DataTracker.registerData(PlayerEntity.class, AFFILIATION_TRACKER);
        public static final TrackedData<Affiliation> APPARENT_AFFILIATION = DataTracker.registerData(PlayerEntity.class, AFFILIATION_TRACKER);


        static {
            TrackedDataHandlerRegistry.register(AFFILIATION_TRACKER);
        }
    }

    public static class DamageSources extends DamageSource {
        protected DamageSources(String name) {
            super(Constants.MOD_ID + "." + name);
        }

        public static final DamageSource SLEEP = new DamageSources("sleep").setBypassesArmor();
        public static final DamageSource INSANITY = new DamageSources("insanity") {
            @Override
            public Text getDeathMessage(LivingEntity entity) {
                return new TranslatableText(String.format("death.attack." + name + ".%d", entity.getRandom().nextInt(3)), entity.getDisplayName());
            }
        }.setBypassesArmor();

        public static class ProtagonistDamageSource extends EntityDamageSource {
            public ProtagonistDamageSource(@Nullable Entity source) {
                super(Constants.MOD_ID + ".protagonist", source);
            }

            @Override
            public Text getDeathMessage(LivingEntity entity) {
                return new TranslatableText(String.format("death.attack." + name + ".%d", entity.getRandom().nextInt(4)), entity.getDisplayName());
            }
        }
    }

    public static class BlockSettings {
        public static final AbstractBlock.Settings OCEANIC_GOLD = AbstractBlock.Settings.of(Material.METAL).strength(1F, 5F).requiresTool();
    }

    public static class Misc {
        public static final String NECRONOMICON_EXTENSION = "readBook";
    }
}
