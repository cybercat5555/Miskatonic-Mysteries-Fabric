package com.miskatonicmysteries.common.lib;

import com.miskatonicmysteries.common.feature.ModCommand;
import com.miskatonicmysteries.common.feature.effect.LazarusStatusEffect;
import com.miskatonicmysteries.common.feature.effect.ManiaStatusEffect;
import com.miskatonicmysteries.common.feature.effect.OvermedicalizedStatusEffect;
import com.miskatonicmysteries.common.feature.effect.TranquilizedStatusEffect;
import com.miskatonicmysteries.common.feature.spell.SpellEffect;
import com.miskatonicmysteries.common.feature.spell.SpellMedium;
import com.miskatonicmysteries.common.lib.util.RegistryUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplier;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SuspiciousStewItem;
import net.minecraft.loot.LootTables;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;

import javax.annotation.Nullable;
import java.util.*;

import static com.miskatonicmysteries.common.lib.ModRegistries.ModTradeOffers.*;

public class ModRegistries {
    public static final SoundEvent GUN_SHOT = new SoundEvent(new Identifier(Constants.MOD_ID, "gun_shot"));

    public static final StatusEffect MANIA = new ManiaStatusEffect();
    public static final StatusEffect TRANQUILIZED = new TranquilizedStatusEffect();
    public static final StatusEffect OVERMEDICATED = new OvermedicalizedStatusEffect();
    public static final StatusEffect LAZARUS = new LazarusStatusEffect();

    public static final Identifier INFESTED_WHEAT_LOOT_TABLE = new Identifier(Constants.MOD_ID, "injects/infested_wheat");
    public static final Identifier TRANQ_TABLE = new Identifier(Constants.MOD_ID, "injects/tranquilizer");

    public static final Map<Identifier, Identifier> LOOT_TABLE_INJECTS = new HashMap<>();

    public static final Int2ObjectMap<TradeOffers.Factory[]> PSYCHONAUT_TRADES = new Int2ObjectArrayMap();
    public static final Int2ObjectMap<TradeOffers.Factory[]> YELLOW_SERF_TRADE = new Int2ObjectArrayMap();

    static {
        PSYCHONAUT_TRADES.put(1, new TradeOffers.Factory[]{BLOTTER_OFFER, SCIENCE_JOURNAL_OFFER, INFESTED_WHEAT_OFFER});
        PSYCHONAUT_TRADES.put(2, new TradeOffers.Factory[]{CHEMISTRY_SET_OFFER, WAX_OFFER, SUSPICIOUS_OFFER});
        PSYCHONAUT_TRADES.put(3, new TradeOffers.Factory[]{NETHER_WART_OFFER, LAUDANUM_OFFER});
        PSYCHONAUT_TRADES.put(4, new TradeOffers.Factory[]{WARPED_FUNGUS_OFFER, CRIMSON_FUNGUS_OFFER, TRANQ_OFFER});
        PSYCHONAUT_TRADES.put(5, new TradeOffers.Factory[]{RE_AGENT_OFFER});
    }

    public static void init() {
        initLootTableEdits();
        ModCommand.setup();

        RegistryUtil.register(Registry.SOUND_EVENT, "gun_shot", GUN_SHOT);

        RegistryUtil.register(Registry.STATUS_EFFECT, "mania", MANIA);
        RegistryUtil.register(Registry.STATUS_EFFECT, "tranquilized", TRANQUILIZED);
        RegistryUtil.register(Registry.STATUS_EFFECT, "overmedicated", OVERMEDICATED);
        RegistryUtil.register(Registry.STATUS_EFFECT, "lazarus", LAZARUS);

        TradeOffers.PROFESSION_TO_LEVELED_TRADE.put(ModEntities.PSYCHONAUT, PSYCHONAUT_TRADES);
        TradeOffers.PROFESSION_TO_LEVELED_TRADE.put(ModEntities.YELLOW_SERF, PSYCHONAUT_TRADES);
    }

    private static void initLootTableEdits() {
        LOOT_TABLE_INJECTS.put(Blocks.WHEAT.getLootTableId(), INFESTED_WHEAT_LOOT_TABLE);

        LOOT_TABLE_INJECTS.put(LootTables.VILLAGE_BUTCHER_CHEST, TRANQ_TABLE);
        LOOT_TABLE_INJECTS.put(LootTables.VILLAGE_CARTOGRAPHER_CHEST, TRANQ_TABLE);
        LOOT_TABLE_INJECTS.put(LootTables.VILLAGE_TANNERY_CHEST, TRANQ_TABLE);
        LOOT_TABLE_INJECTS.put(LootTables.VILLAGE_PLAINS_CHEST, TRANQ_TABLE);

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
        public static final List<StatusEffect> POSSIBLE_EFFECTS = Arrays.asList(StatusEffects.NAUSEA, StatusEffects.ABSORPTION, StatusEffects.GLOWING, StatusEffects.HASTE, MANIA, TRANQUILIZED);
        public static final TradeOffers.Factory SCIENCE_JOURNAL_OFFER = new EmeraldToItemOffer(new ItemStack(ModObjects.SCIENCE_JOURNAL), 12, 3, 4, 0.15F);
        public static final TradeOffers.Factory BLOTTER_OFFER = new ProcessItemOffer(new ItemStack(Items.PAPER), 7, new ItemStack(ModObjects.BLOTTER, 3), 16, 1);
        public static final TradeOffers.Factory INFESTED_WHEAT_OFFER = new ItemToEmeraldOffer(new ItemStack(ModObjects.INFESTED_WHEAT, 3), 2, 16, 2, 0.35F);

        public static final TradeOffers.Factory CHEMISTRY_SET_OFFER = new EmeraldToItemOffer(new ItemStack(ModObjects.CHEMISTRY_SET), 12, 16, 12, 0.15F);
        public static final TradeOffers.Factory WAX_OFFER = new EmeraldToItemOffer(new ItemStack(ModObjects.WAX, 2), 5, 32, 10, 0.35F);

        public static final TradeOffers.Factory NETHER_WART_OFFER = new ItemToEmeraldOffer(new ItemStack(Items.NETHER_WART, 12), 1, 32, 10, 0.2F);
        public static final TradeOffers.Factory LAUDANUM_OFFER = new EmeraldToItemOffer(new ItemStack(ModObjects.LAUDANUM), 10, 12, 20, 0.15F);

        public static final TradeOffers.Factory WARPED_FUNGUS_OFFER = new ItemToEmeraldOffer(new ItemStack(Items.WARPED_FUNGUS, 3), 1, 32, 15, 0.25F);
        public static final TradeOffers.Factory CRIMSON_FUNGUS_OFFER = new ItemToEmeraldOffer(new ItemStack(Items.CRIMSON_FUNGUS, 4), 1, 32, 15, 0.25F);
        public static final TradeOffers.Factory TRANQ_OFFER = new ProcessItemOffer(new ItemStack(ModObjects.WAX, 3), 18, new ItemStack(ModObjects.TRANQUILIZER), 2, 35);

        public static final TradeOffers.Factory RE_AGENT_OFFER = new ProcessItemOffer(new ItemStack(Items.ROTTEN_FLESH, 24), 32, new ItemStack(ModObjects.RE_AGENT_SYRINGE), 1, 50);

        public static final TradeOffers.Factory SUSPICIOUS_OFFER = ((entity, random) -> {
            ItemStack itemStack = new ItemStack(Items.SUSPICIOUS_STEW, 1);
            SuspiciousStewItem.addEffectToStew(itemStack, POSSIBLE_EFFECTS.get(random.nextInt(POSSIBLE_EFFECTS.size())), 600);
            return new TradeOffer(new ItemStack(Items.EMERALD, 3 + random.nextInt(6)), new ItemStack(Items.BOWL), itemStack, 12, 15, 0.4F);
        });

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
