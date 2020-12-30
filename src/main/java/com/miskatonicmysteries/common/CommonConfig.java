package com.miskatonicmysteries.common;

import blue.endless.jankson.Comment;
import com.miskatonicmysteries.common.lib.Constants;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;

@Config(name = Constants.MOD_ID)
public class CommonConfig implements ConfigData {
    @Comment(value = "Determines the intervals in ticks in which values that are not absolutely essential are updated")
    public int modUpdateInterval = 20;

    @Comment(value = "The chance for players to be un-shocked after each update interval")
    public float shockRemoveChance = 0.01F;

    @Comment(value = "Determines the intervals in ticks after which insanity events may occur")
    public int insanityInterval = 2000;

    @Comment(value = "Determines how often another insanity event is looked for, if the currently selected one fails")
    public int insanityEventAttempts = 3;

    @Comment(value = "Once the player has a sanity value lower than this, Mania can be very deadly")
    public int deadlyInsanityThreshold = 50;

    @Comment(value = "The base-amount of sanity regenerated after sleeping while tranquilized")
    public int tranquilizedSanityBonus = 25;

    @Comment(value = "The chance of regaining a bit of normally permanently lost sanity after sleeping while tranquilized")
    public float tranquilizedSanityCapRegainChance = 0.1F;

    @Comment(value = "Every player with a sanity level equal or below this will be attacked by Protagonists")
    public int protagonistAggressionThreshold = 700;

    @Comment(value = "The chance for Psychonaut Houses to spawn in villages; the higher the value, the more likely they are to appear")
    public int psychonautHouseWeight = 3;

    @Comment(value = "The chance for Hastur Shrines to form the center of villages; the higher the value, the more likely they are to appear")
    public int hasturShrineWeight = 4;

    @Comment(value = "The percentage of Yellow Serfs that according villages will aim for (when recruiting)")
    public float hasturCultistPercentage = 0.25F;
}
