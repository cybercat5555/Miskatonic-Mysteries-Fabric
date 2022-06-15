package com.miskatonicmysteries.api.banner;

import com.miskatonicmysteries.api.banner.impl.LoomPatternContainer;
import com.miskatonicmysteries.api.banner.impl.LoomPatternsInternal;
import com.miskatonicmysteries.api.banner.loom.LoomPattern;
import com.miskatonicmysteries.api.banner.loom.LoomPatterns;
import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.event.registry.RegistryIdRemapCallback;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;

import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.loot.function.CopyNbtLootFunction;
import net.minecraft.loot.provider.nbt.ContextLootNbtProvider;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;
import java.util.List;

public class MMBannerRegistry {

	public static final Registry<LoomPattern> LOOM_PATTERN_REGISTRY = FabricRegistryBuilder
		.createSimple(LoomPattern.class, new Identifier(Constants.MOD_ID, "loom_patterns"))
		.attribute(RegistryAttribute.SYNCED)
		.buildAndRegister();


	private static final List<Identifier> BANNER_LOOT_TABLES = Arrays.asList(
		new Identifier("minecraft", "blocks/black_banner"),
		new Identifier("minecraft", "blocks/red_banner"),
		new Identifier("minecraft", "blocks/green_banner"),
		new Identifier("minecraft", "blocks/brown_banner"),
		new Identifier("minecraft", "blocks/blue_banner"),
		new Identifier("minecraft", "blocks/purple_banner"),
		new Identifier("minecraft", "blocks/cyan_banner"),
		new Identifier("minecraft", "blocks/light_gray_banner"),
		new Identifier("minecraft", "blocks/gray"),
		new Identifier("minecraft", "blocks/pink_banner"),
		new Identifier("minecraft", "blocks/lime_banner"),
		new Identifier("minecraft", "blocks/yellow_banner"),
		new Identifier("minecraft", "blocks/light_blue_banner"),
		new Identifier("minecraft", "blocks/magenta_banner"),
		new Identifier("minecraft", "blocks/orange_banner"),
		new Identifier("minecraft", "blocks/white_banner")
	);

	public static void registerBanner() {
		RegistryIdRemapCallback.event(LOOM_PATTERN_REGISTRY).register(state -> LoomPatternsInternal.remapLoomIndices());

		// registry sync is longer called on the server or in singleplayer, so we must set up the indices here using
		// a registry item added callback.
		for (LoomPattern p : LOOM_PATTERN_REGISTRY) {
			LoomPatternsInternal.addPattern(p);
		}

		RegistryEntryAddedCallback.event(LOOM_PATTERN_REGISTRY).register((raw, id, p) -> LoomPatternsInternal.addPattern(p));
		LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, lootTableId, supplier, setter) -> {
			if (BANNER_LOOT_TABLES.contains(lootTableId)) {
				supplier.withFunction(CopyNbtLootFunction
										  .builder(ContextLootNbtProvider.BLOCK_ENTITY)
										  .withOperation(LoomPatternContainer.NBT_KEY, "BlockEntityTag." + LoomPatternContainer.NBT_KEY)
										  .build()
				);
			}
		});
	}

	public static void registerBannerClient() {
		ClientSpriteRegistryCallback.event(TexturedRenderLayers.BANNER_PATTERNS_ATLAS_TEXTURE)
			.register((texture, registry) -> {
				for (LoomPattern pattern : LoomPatterns.REGISTRY) {
					registry.register(pattern.getSpriteId("banner"));
				}
			});
		ClientSpriteRegistryCallback.event(TexturedRenderLayers.SHIELD_PATTERNS_ATLAS_TEXTURE)
			.register((texture, registry) -> {
				for (LoomPattern pattern : LoomPatterns.REGISTRY) {
					registry.register(pattern.getSpriteId("shield"));
				}
			});
	}

}
