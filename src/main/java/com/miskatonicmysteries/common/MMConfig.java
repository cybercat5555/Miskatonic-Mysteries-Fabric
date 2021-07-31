package com.miskatonicmysteries.common;

import com.miskatonicmysteries.common.util.Constants;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Config(name = Constants.MOD_ID)
public class MMConfig implements ConfigData {
    public int modUpdateInterval = 20;

    @Environment(EnvType.CLIENT)
    @ConfigEntry.Gui.CollapsibleObject
    public Client client = new Client();

    @ConfigEntry.Gui.CollapsibleObject
    public Entities entities = new Entities();

    @ConfigEntry.Gui.CollapsibleObject
    public Items items = new Items();

    @ConfigEntry.Gui.CollapsibleObject
    public Sanity sanity = new Sanity();

    @ConfigEntry.Gui.CollapsibleObject
    public World world = new World();



    public static class Entities {
        @ConfigEntry.BoundedDiscrete(max = Constants.DataTrackers.SANITY_CAP)
        @ConfigEntry.Gui.Tooltip
        public int protagonistAggressionThreshold = 700;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 1)
        public float yellowSerfPercentage = 0.25F;

        @ConfigEntry.Gui.Tooltip
        public boolean subtlety = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 1)
        public float statueEffectChance = 0.4F;
    }

    public static class Items {
        public boolean masksConcealNameplates = true;
    }

    public static class Sanity {
        @ConfigEntry.BoundedDiscrete(max = Constants.DataTrackers.SANITY_CAP)
        @ConfigEntry.Gui.Tooltip
        public int deadlyInsanityThreshold = 50;

        @ConfigEntry.Gui.Tooltip
        public int insanityEventAttempts = 3;

        @ConfigEntry.Gui.Tooltip
        public int insanityInterval = 2000;

        @ConfigEntry.Gui.Tooltip
        public float shockRemoveChance = 0.01F;

        @ConfigEntry.Gui.Tooltip
        public int tranquilizedSanityBonus = 25;

        @ConfigEntry.Gui.Tooltip
        public float tranquilizedSanityCapRegainChance = 0.1F;
    }

    public static class World {

        @ConfigEntry.BoundedDiscrete(max = 150, min = 1)
        @ConfigEntry.Gui.Tooltip(count = 2)
        @ConfigEntry.Gui.RequiresRestart
        public int psychonautHouseWeight = 3;

        @ConfigEntry.BoundedDiscrete(max = 150, min = 1)
        @ConfigEntry.Gui.Tooltip(count = 2)
        @ConfigEntry.Gui.RequiresRestart
        public int hasturShrineWeight = 60;

        @ConfigEntry.Gui.Tooltip(count = 2)
        public float infestedWheatChance = 0.05F;
    }

    public static class Client {
        public boolean useShaders = true;

        @ConfigEntry.Gui.CollapsibleObject
        public CurrentSpellHUD currentSpellHUD = new CurrentSpellHUD();

        public static class CurrentSpellHUD {
            public int marginX = 32;
            public int marginY = 32;
        }
    }
}
