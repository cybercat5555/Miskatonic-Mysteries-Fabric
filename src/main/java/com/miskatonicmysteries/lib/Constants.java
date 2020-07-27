package com.miskatonicmysteries.lib;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class Constants {
    public static final String MOD_ID = "miskatonicmysteries";
    public static final ItemGroup MM_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "group"), () -> ModObjects.OCEANIC_GOLD.getStackForRender());
    public static class NBT{
        public static final String RECEIVED_STACK = "Received Stack";
        public static final String REALIZED_STACK = "Realized Stack";
        public static final String POTENTIAL_ITEMS = "Potential Items";
        public static final String SHOTS = "Shots";
        public static final String LOADING = "Loading";

        public static final String MISK_DATA = "MiskMyst Data";
        public static final String SANITY = "Sanity";
        public static final String SHOCKED = "Shocked";
        public static final String SANITY_EXPANSIONS = "Sanity Expansions";
    }

    public static class DataTrackers {
        public static final int SANITY_CAP = 1000;
        public static final TrackedData<Integer> SANITY = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
        public static final TrackedData<Boolean> SHOCKED = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    }
    public static class DamageSources extends DamageSource{
        protected DamageSources(String name) {
            super(Constants.MOD_ID + "." + name);
        }

        public static final DamageSource GUN = new DamageSources("gun").setProjectile();
        public static final DamageSource SLEEP = new DamageSources("sleep").setBypassesArmor();
        public static final DamageSource INSANITY = new DamageSources("insanity") {
            @Override
            public Text getDeathMessage(LivingEntity entity) {
                return new TranslatableText(String.format("death.attack." + name + ".%d", entity.getRandom().nextInt(3)), entity.getDisplayName());
            }
        }.setBypassesArmor();
        public static final DamageSource PROTAGONIST = new DamageSources("protagonist") {
            @Override
            public Text getDeathMessage(LivingEntity entity) {
                return new TranslatableText(String.format("death.attack." + name + ".%d", entity.getRandom().nextInt(4)), entity.getDisplayName());
            }
        };
    }

    public static class BlockSettings {
        public static final AbstractBlock.Settings OCEANIC_GOLD = AbstractBlock.Settings.of(Material.METAL).strength(1F, 5F).requiresTool();
    }
}
