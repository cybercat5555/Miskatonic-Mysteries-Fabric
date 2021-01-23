package com.miskatonicmysteries.common.lib;

import com.miskatonicmysteries.common.feature.ModCommand;
import com.miskatonicmysteries.common.feature.effect.*;
import com.miskatonicmysteries.common.feature.spell.SpellEffect;
import com.miskatonicmysteries.common.feature.spell.SpellMedium;
import com.miskatonicmysteries.common.lib.util.RegistryUtil;
import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.TrinketSlots;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplier;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SuspiciousStewItem;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;

import javax.annotation.Nullable;
import java.util.*;

import static com.miskatonicmysteries.common.lib.MMMiscRegistries.LootTables.LOOT_TABLE_INJECTS;
import static com.miskatonicmysteries.common.lib.MMMiscRegistries.LootTables.TRANQ_TABLE;
import static com.miskatonicmysteries.common.lib.MMMiscRegistries.ModTradeOffers.*;
import static com.miskatonicmysteries.common.lib.MMMiscRegistries.Sounds.*;
import static com.miskatonicmysteries.common.lib.MMMiscRegistries.StatusEffects.*;
import static com.miskatonicmysteries.common.lib.MMMiscRegistries.Trades.PSYCHONAUT_TRADES;
import static com.miskatonicmysteries.common.lib.MMMiscRegistries.Trades.YELLOW_SERF_TRADE;

public class MMMiscRegistries {
    public static class Sounds {
        public static final SoundEvent GUN_SHOT = new SoundEvent(new Identifier(Constants.MOD_ID, "gun_shot"));
        public static final SoundEvent SCARY_SOUNDS = new SoundEvent(new Identifier(Constants.MOD_ID, "scary"));
        public static final SoundEvent TELEPORT_SOUND = new SoundEvent(new Identifier(Constants.MOD_ID, "teleport"));
        public static final SoundEvent BROKE_VEIL_SPAWN = new SoundEvent(new Identifier(Constants.MOD_ID, "veil_spawn"));
        public static final SoundEvent INCANTATION_BIND_SUCCESS = new SoundEvent(new Identifier(Constants.MOD_ID, "incantation_bound"));
    }

    public static class StatusEffects {
        public static final StatusEffect MANIA = new ManiaStatusEffect();
        public static final StatusEffect TRANQUILIZED = new TranquilizedStatusEffect();
        public static final StatusEffect OVERMEDICATED = new OvermedicalizedStatusEffect();
        public static final StatusEffect LAZARUS = new LazarusStatusEffect();
        public static final StatusEffect BLEED = new BleedStatusEffect();
    }

    public static class LootTables {
        public static final Identifier TRANQ_TABLE = new Identifier(Constants.MOD_ID, "injects/tranquilizer");

        public static final Map<Identifier, Identifier> LOOT_TABLE_INJECTS = new HashMap<>();
    }

    public static class Trades {
        public static final Int2ObjectMap<TradeOffers.Factory[]> PSYCHONAUT_TRADES = new Int2ObjectArrayMap();
        public static final Int2ObjectMap<TradeOffers.Factory[]> YELLOW_SERF_TRADE = new Int2ObjectArrayMap();

        static {
            PSYCHONAUT_TRADES.put(1, new TradeOffers.Factory[]{BLOTTER_OFFER, SCIENCE_JOURNAL_OFFER, INFESTED_WHEAT_OFFER});
            PSYCHONAUT_TRADES.put(2, new TradeOffers.Factory[]{CHEMISTRY_SET_OFFER, WAX_OFFER, SUSPICIOUS_OFFER});
            PSYCHONAUT_TRADES.put(3, new TradeOffers.Factory[]{NETHER_WART_OFFER, LAUDANUM_OFFER});
            PSYCHONAUT_TRADES.put(4, new TradeOffers.Factory[]{WARPED_FUNGUS_OFFER, CRIMSON_FUNGUS_OFFER, TRANQ_OFFER});
            PSYCHONAUT_TRADES.put(5, new TradeOffers.Factory[]{RE_AGENT_OFFER});

            YELLOW_SERF_TRADE.put(1, new TradeOffers.Factory[]{OCEANIC_GOLD_OFFER, NECRONOMICON_OFFER});
            YELLOW_SERF_TRADE.put(2, new TradeOffers.Factory[]{MASK_OFFER, BLOTTER_OFFER});
            YELLOW_SERF_TRADE.put(3, new TradeOffers.Factory[]{YELLOW_ROBE_OFFER, YELLOW_SKIRT_OFFER});
            YELLOW_SERF_TRADE.put(4, new TradeOffers.Factory[]{YELLOW_HOOD_OFFER, ORNATE_DAGGER_OFFER});
            YELLOW_SERF_TRADE.put(5, new TradeOffers.Factory[]{YELLOW_SIGN_OFFER});
        }
    }


    public static void init() {
        TrinketSlots.addSlot(SlotGroups.HEAD, Slots.MASK, new Identifier("trinkets", "textures/item/empty_trinket_slot_mask.png"));

        initLootTableEdits();
        ModCommand.setup();

        RegistryUtil.register(Registry.SOUND_EVENT, "gun_shot", GUN_SHOT);
        RegistryUtil.register(Registry.SOUND_EVENT, "scary", SCARY_SOUNDS);
        RegistryUtil.register(Registry.SOUND_EVENT, "teleport", TELEPORT_SOUND);
        RegistryUtil.register(Registry.SOUND_EVENT, "veil_spawn", BROKE_VEIL_SPAWN);
        RegistryUtil.register(Registry.SOUND_EVENT, "incantation_bound", INCANTATION_BIND_SUCCESS);

        RegistryUtil.register(Registry.STATUS_EFFECT, "mania", MANIA);
        RegistryUtil.register(Registry.STATUS_EFFECT, "tranquilized", TRANQUILIZED);
        RegistryUtil.register(Registry.STATUS_EFFECT, "overmedicated", OVERMEDICATED);
        RegistryUtil.register(Registry.STATUS_EFFECT, "lazarus", LAZARUS);
        RegistryUtil.register(Registry.STATUS_EFFECT, "bleed", BLEED);

        TradeOffers.PROFESSION_TO_LEVELED_TRADE.put(MMEntities.PSYCHONAUT, PSYCHONAUT_TRADES);
        TradeOffers.PROFESSION_TO_LEVELED_TRADE.put(MMEntities.YELLOW_SERF, YELLOW_SERF_TRADE);

        TradeOffers.Factory[] wanderingTraderOffers = TradeOffers.WANDERING_TRADER_TRADES.get(1);
        TradeOffers.Factory[] offers = Arrays.copyOf(wanderingTraderOffers, wanderingTraderOffers.length + 3);
        offers[wanderingTraderOffers.length] = NECRONOMICON_OFFER;
        offers[wanderingTraderOffers.length + 1] = OCEANIC_GOLD_OFFER;
        offers[wanderingTraderOffers.length + 2] = BLOTTER_OFFER;
        TradeOffers.WANDERING_TRADER_TRADES.put(1, offers);

        ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(MMObjects.INFESTED_WHEAT, 0.7F);
    }

    private static void initLootTableEdits() {
        LOOT_TABLE_INJECTS.put(net.minecraft.loot.LootTables.VILLAGE_BUTCHER_CHEST, TRANQ_TABLE);
        LOOT_TABLE_INJECTS.put(net.minecraft.loot.LootTables.VILLAGE_CARTOGRAPHER_CHEST, TRANQ_TABLE);
        LOOT_TABLE_INJECTS.put(net.minecraft.loot.LootTables.VILLAGE_TANNERY_CHEST, TRANQ_TABLE);
        LOOT_TABLE_INJECTS.put(net.minecraft.loot.LootTables.VILLAGE_PLAINS_CHEST, TRANQ_TABLE);

        LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, identifier, builder, lootTableSetter) -> {
            if (LOOT_TABLE_INJECTS.containsKey(identifier)) {
                FabricLootSupplier lootSupplier = (FabricLootSupplier) FabricLootSupplierBuilder.of(lootManager.getTable(LOOT_TABLE_INJECTS.get(identifier))).build();
                lootTableSetter.set(builder.withPools(lootSupplier.getPools()).build());
            }
        });
    }

    public static SpellEffect addSpellEffect(SpellEffect effect) {
        SpellEffect.SPELL_EFFECTS.put(effect.getId(), effect);
        return effect;
    }

    public static SpellMedium addSpellMedium(SpellMedium medium) {
        SpellMedium.SPELL_MEDIUMS.put(medium.getId(), medium);
        return medium;
    }

    public static class ModTradeOffers {
        public static final List<StatusEffect> POSSIBLE_EFFECTS = Arrays.asList(net.minecraft.entity.effect.StatusEffects.NAUSEA, net.minecraft.entity.effect.StatusEffects.ABSORPTION, net.minecraft.entity.effect.StatusEffects.GLOWING, net.minecraft.entity.effect.StatusEffects.HASTE, MANIA, TRANQUILIZED);
        public static final TradeOffers.Factory SCIENCE_JOURNAL_OFFER = new EmeraldToItemOffer(new ItemStack(MMObjects.SCIENCE_JOURNAL), 12, 3, 4, 0.15F);
        public static final TradeOffers.Factory BLOTTER_OFFER = new ProcessItemOffer(new ItemStack(Items.PAPER), 7, new ItemStack(MMObjects.BLOTTER, 3), 16, 2);
        public static final TradeOffers.Factory INFESTED_WHEAT_OFFER = new ItemToEmeraldOffer(new ItemStack(MMObjects.INFESTED_WHEAT, 3), 2, 16, 2, 0.35F);

        public static final TradeOffers.Factory CHEMISTRY_SET_OFFER = new EmeraldToItemOffer(new ItemStack(MMObjects.CHEMISTRY_SET), 12, 16, 12, 0.15F);
        public static final TradeOffers.Factory WAX_OFFER = new EmeraldToItemOffer(new ItemStack(MMObjects.WAX, 2), 5, 32, 10, 0.35F);

        public static final TradeOffers.Factory NETHER_WART_OFFER = new ItemToEmeraldOffer(new ItemStack(Items.NETHER_WART, 12), 1, 32, 10, 0.2F);
        public static final TradeOffers.Factory LAUDANUM_OFFER = new EmeraldToItemOffer(new ItemStack(MMObjects.LAUDANUM), 10, 12, 20, 0.15F);

        public static final TradeOffers.Factory WARPED_FUNGUS_OFFER = new ItemToEmeraldOffer(new ItemStack(Items.WARPED_FUNGUS, 3), 1, 32, 15, 0.25F);
        public static final TradeOffers.Factory CRIMSON_FUNGUS_OFFER = new ItemToEmeraldOffer(new ItemStack(Items.CRIMSON_FUNGUS, 4), 1, 32, 15, 0.25F);
        public static final TradeOffers.Factory TRANQ_OFFER = new ProcessItemOffer(new ItemStack(MMObjects.WAX, 3), 18, new ItemStack(MMObjects.TRANQUILIZER), 2, 35);

        public static final TradeOffers.Factory RE_AGENT_OFFER = new ProcessItemOffer(new ItemStack(Items.ROTTEN_FLESH, 24), 32, new ItemStack(MMObjects.RE_AGENT_SYRINGE), 1, 50);

        public static final TradeOffers.Factory SUSPICIOUS_OFFER = ((entity, random) -> {
            ItemStack itemStack = new ItemStack(Items.SUSPICIOUS_STEW, 1);
            SuspiciousStewItem.addEffectToStew(itemStack, POSSIBLE_EFFECTS.get(random.nextInt(POSSIBLE_EFFECTS.size())), 600);
            return new TradeOffer(new ItemStack(Items.EMERALD, 3 + random.nextInt(6)), new ItemStack(Items.BOWL), itemStack, 12, 15, 0.4F);
        });

        public static final TradeOffers.Factory OCEANIC_GOLD_OFFER = new ProcessItemOffer(new ItemStack(Items.GOLD_BLOCK), 10, new ItemStack(MMObjects.OCEANIC_GOLD_BLOCK, 1), 16, 2);
        public static final TradeOffers.Factory NECRONOMICON_OFFER = new EmeraldToItemOffer(new ItemStack(MMObjects.NECRONOMICON), 12, 3, 10, 0.15F);
        public static final TradeOffers.Factory CHALK_OFFER = new EmeraldToItemOffer(new ItemStack(MMObjects.HASTUR_CHALK), 2, 12, 20, 0.15F);
        public static final TradeOffers.Factory MASK_OFFER = new ProcessItemOffer(new ItemStack(Items.IRON_INGOT), 12, new ItemStack(MMObjects.ELEGANT_MASK, 1), 1, 25);
        public static final TradeOffers.Factory YELLOW_ROBE_OFFER = new ProcessItemOffer(new ItemStack(Items.YELLOW_WOOL, 8), 8, new ItemStack(MMObjects.YELLOW_ROBE, 1), 2, 30);
        public static final TradeOffers.Factory YELLOW_SKIRT_OFFER = new ProcessItemOffer(new ItemStack(Items.YELLOW_WOOL, 7), 5, new ItemStack(MMObjects.YELLOW_SKIRT, 1), 2, 30);
        public static final TradeOffers.Factory YELLOW_HOOD_OFFER = new ProcessItemOffer(new ItemStack(Items.YELLOW_WOOL, 5), 5, new ItemStack(MMObjects.YELLOW_HOOD, 1), 2, 30);
        public static final TradeOffers.Factory ORNATE_DAGGER_OFFER = new ProcessItemOffer(new ItemStack(Items.IRON_SWORD), 7, new ItemStack(MMObjects.ORNATE_DAGGER, 1), 6, 35);
        public static final TradeOffers.Factory YELLOW_SIGN_OFFER = new ProcessItemOffer(new ItemStack(Items.PAPER, 4), 20, new ItemStack(MMObjects.YELLOW_SIGN_LOOM_PATTERN), 1, 50);

        public static class EmeraldToItemOffer implements TradeOffers.Factory {
            private final ItemStack sell;
            private final int price;
            private final int maxUses;
            private final int experience;
            private final float multiplier;

            public EmeraldToItemOffer(ItemStack stack, int price, int maxUses, int experience, float multiplier) {
                this.sell = stack;
                this.price = price;
                this.maxUses = maxUses;
                this.experience = experience;
                this.multiplier = multiplier;
            }

            public TradeOffer create(Entity entity, Random random) {
                return new TradeOffer(new ItemStack(Items.EMERALD, this.price + random.nextInt(3)), sell, this.maxUses, this.experience, this.multiplier);
            }
        }

        public static class ItemToEmeraldOffer implements TradeOffers.Factory {
            private final ItemStack buy;
            private final int price;
            private final int maxUses;
            private final int experience;
            private final float multiplier;

            public ItemToEmeraldOffer(ItemStack stack, int price, int maxUses, int experience, float multiplier) {
                this.buy = stack;
                this.price = price;
                this.maxUses = maxUses;
                this.experience = experience;
                this.multiplier = multiplier;
            }

            public TradeOffer create(Entity entity, Random random) {
                return new TradeOffer(new ItemStack(buy.getItem(), buy.getCount() + random.nextInt(5)), new ItemStack(Items.EMERALD, this.price), this.maxUses, this.experience, this.multiplier);
            }
        }

        public static class ProcessItemOffer implements TradeOffers.Factory {
            private final ItemStack secondBuy;
            private final int price;
            private final ItemStack sell;
            private final int maxUses;
            private final int experience;
            private final float multiplier;

            public ProcessItemOffer(ItemStack input, int price, ItemStack sellItem, int maxUses, int experience) {
                this.secondBuy = input;
                this.price = price;
                this.sell = sellItem;
                this.maxUses = maxUses;
                this.experience = experience;
                this.multiplier = 0.1F;
            }

            @Nullable
            public TradeOffer create(Entity entity, Random random) {
                return new TradeOffer(new ItemStack(Items.EMERALD, this.price), secondBuy, sell, this.maxUses, this.experience, this.multiplier);
            }
        }
    }
}
