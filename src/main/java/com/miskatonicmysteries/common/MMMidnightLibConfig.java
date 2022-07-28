package com.miskatonicmysteries.common;

import com.miskatonicmysteries.common.util.Constants;
import eu.midnightdust.lib.config.MidnightConfig;

public class MMMidnightLibConfig extends MidnightConfig {

	@Comment public static Comment entities;

	@Entry(min=0,max=Constants.DataTrackers.SANITY_CAP)
	public static int protagonistAggressionThreshold = 700;

	@Entry(min = 0, max = 1)
	public static float yellowSerfPercentage = 0.25F;

	@Entry(min = 0, max = 1)
	public static float statueEffectChance = 0.4F;

	@Entry(min = 0, max = 1)
	public static float lightningVassalChance = 0.1F;

	@Comment public static Comment mechanics;


	@Entry
	public static boolean subtlety = true;
	@Entry
	public static int modUpdateInterval = 20;
	@Entry
	public static int maxStabilizers = 12;


	@Comment public static Comment items;
	@Entry
	public static boolean masksConcealNameplates = true;

	@Comment public static Comment sanity;
	@Entry(min = 0, max = Constants.DataTrackers.SANITY_CAP)
	public static int deadlyInsanityThreshold = 50;
	@Entry
	public static int insanityEventAttempts = 3;
	@Entry
	public static int insanityInterval = 2000;
	@Entry
	public static float shockRemoveChance = 0.01F;
	@Entry
	public static int tranquilizedSanityBonus = 25;
	@Entry
	public static float tranquilizedSanityCapRegainChance = 0.1F;
	@Entry
	public static float villagerStopTradingPercentage = 0.25F;


	@Comment public static Comment world;
	@Entry(max = 5000, min = 0)
	public static int psychonautHouseWeight = 5;
	@Entry(max = 5000, min = 0)
	public static int hasturShrineWeight = 1200;
	@Entry
	public static float infestedWheatChance = 0.05F;


	@Comment public static Comment client;
	@Client @Entry
	public static boolean useShaders = true;
	@Client @Entry
	public static boolean forceChunkColorUpdates = true;
	@Client @Entry
	public static int marginX = 32;
	@Client @Entry
	public static int marginY = 32;
}

