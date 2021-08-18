package com.miskatonicmysteries.client.render;

import com.miskatonicmysteries.api.block.OctagramBlock;
import com.miskatonicmysteries.api.block.StatueBlock;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.client.render.blockentity.MasterpieceStatueBlockRender;
import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.block.blockentity.StatueBlockEntity;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.Constants;
import com.mojang.datafixers.kinds.Const;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.render.block.entity.EnchantingTableBlockEntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class ResourceHandler {
    public static final SpriteIdentifier DEFAULT_OCTAGRAM = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(Constants.MOD_ID, "block/octagram/generic_octagram"));
    public static final SpriteIdentifier DEFAULT_OCTAGRAM_MASK = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(Constants.MOD_ID, "block/octagram/mask/octagram_mask"));
    public static final SpriteIdentifier AURA_SPRITE = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(Constants.MOD_ID, "misc/aura"));
    public static final SpriteIdentifier TOTAL_DARK = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(Constants.MOD_ID, "misc/total_dark"));

    public static final SpriteIdentifier HASTUR_SIGIL_CENTER = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(Constants.MOD_ID, "misc/sigil/hastur/center"));
	public static final SpriteIdentifier HASTUR_SIGIL_INNER = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(Constants.MOD_ID, "misc/sigil/hastur/inner"));
	public static final SpriteIdentifier HASTUR_SIGIL_OUTER = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(Constants.MOD_ID, "misc/sigil/hastur/outer"));

    public static final Map<Item, SpriteIdentifier> BOOK_SPRITES = new HashMap<>();
    public static final Map<OctagramBlock, SpriteIdentifier> OCTAGRAM_SPRITES = new HashMap<>();
    public static final Map<OctagramBlock, SpriteIdentifier> OCTAGRAM_MASKS = new HashMap<>();

    public static final Map<StatueBlock, SpriteIdentifier> STATUE_SPRITES = new HashMap<>();
    public static final Identifier ASCENSION_STAR_SPRITE = new Identifier(Constants.MOD_ID, "textures/gui/ascension_star.png");


	public static void init() {
        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register((spriteAtlasTexture, registry) -> {
            registry.register(new Identifier(Constants.MOD_ID, "misc/book_necronomicon"));
            registry.register(new Identifier(Constants.MOD_ID, "misc/aura"));
            registry.register(new Identifier(Constants.MOD_ID, "block/octagram/mask/hastur_octagram_mask"));
            registry.register(new Identifier(Constants.MOD_ID, "block/octagram/mask/shub_octagram_mask"));
            registry.register(new Identifier(Constants.MOD_ID, "block/octagram/mask/cthulhu_octagram_mask"));
			registry.register(HASTUR_SIGIL_CENTER.getTextureId());
			registry.register(HASTUR_SIGIL_INNER.getTextureId());
			registry.register(HASTUR_SIGIL_OUTER.getTextureId());
            MasterpieceStatueBlockRender.TEXTURE_CACHE.forEach((gameProfile, stoneTexture) -> stoneTexture.needsUpdate = true);
        });

        ResourceHandler.addBookTextureFor(MMObjects.NECRONOMICON, new Identifier(Constants.MOD_ID, "misc/book_necronomicon"));

        ResourceHandler.addOctagramTextureFor(MMObjects.CTHULHU_OCTAGRAM, new Identifier(Constants.MOD_ID, "block/octagram/cthulhu_octagram"),
                new Identifier(Constants.MOD_ID, "block/octagram/mask/cthulhu_octagram_mask"));
        ResourceHandler.addOctagramTextureFor(MMObjects.HASTUR_OCTAGRAM, new Identifier(Constants.MOD_ID, "block/octagram/hastur_octagram"),
                new Identifier(Constants.MOD_ID, "block/octagram/mask/hastur_octagram_mask"));
        ResourceHandler.addOctagramTextureFor(MMObjects.SHUB_OCTAGRAM, new Identifier(Constants.MOD_ID, "block/octagram/shub_octagram"),
                new Identifier(Constants.MOD_ID, "block/octagram/mask/shub_octagram_mask"));

        addStatueTextureFor(MMObjects.CTHULHU_STATUE_GOLD, new Identifier(Constants.MOD_ID, "block/statue/cthulhu_statue_gold"));
        addStatueTextureFor(MMObjects.CTHULHU_STATUE_MOSSY, new Identifier(Constants.MOD_ID, "block/statue/cthulhu_statue_mossy"));
        addStatueTextureFor(MMObjects.CTHULHU_STATUE_PRISMARINE, new Identifier(Constants.MOD_ID, "block/statue/cthulhu_statue_prismarine"));
        addStatueTextureFor(MMObjects.CTHULHU_STATUE_STONE, new Identifier(Constants.MOD_ID, "block/statue/cthulhu_statue_stone"));

        addStatueTextureFor(MMObjects.HASTUR_STATUE_GOLD, new Identifier(Constants.MOD_ID, "block/statue/hastur_statue_gold"));
        addStatueTextureFor(MMObjects.HASTUR_STATUE_MOSSY, new Identifier(Constants.MOD_ID, "block/statue/hastur_statue_mossy"));
        addStatueTextureFor(MMObjects.HASTUR_STATUE_TERRACOTTA, new Identifier(Constants.MOD_ID, "block/statue/hastur_statue_terracotta"));
        addStatueTextureFor(MMObjects.HASTUR_STATUE_STONE, new Identifier(Constants.MOD_ID, "block/statue/hastur_statue_stone"));

        addStatueTextureFor(MMObjects.SHUB_STATUE_GOLD, new Identifier(Constants.MOD_ID, "block/statue/shub_statue_gold"));
        addStatueTextureFor(MMObjects.SHUB_STATUE_MOSSY, new Identifier(Constants.MOD_ID, "block/statue/shub_statue_mossy"));
        addStatueTextureFor(MMObjects.SHUB_STATUE_DEEPSLATE, new Identifier(Constants.MOD_ID, "block/statue/shub_statue_deepslate"));
        addStatueTextureFor(MMObjects.SHUB_STATUE_STONE, new Identifier(Constants.MOD_ID, "block/statue/shub_statue_stone"));
    }

    public static void addBookTextureFor(Item item, Identifier texture) {
        BOOK_SPRITES.put(item, new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, texture));
    }

    public static void addOctagramTextureFor(OctagramBlock octagram, Identifier texture, Identifier maskTexture) {
        OCTAGRAM_SPRITES.put(octagram, new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, texture));
        OCTAGRAM_MASKS.put(octagram, new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, maskTexture));
    }

    public static void addStatueTextureFor(StatueBlock statue, Identifier texture) {
        STATUE_SPRITES.put(statue, new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, texture));
    }

    public static SpriteIdentifier getBookTextureFor(ItemStack stack) {
        return BOOK_SPRITES.getOrDefault(stack.getItem(), EnchantingTableBlockEntityRenderer.BOOK_TEXTURE);
    }

    public static SpriteIdentifier getOctagramTextureFor(OctagramBlockEntity octagram) {
        return OCTAGRAM_SPRITES.getOrDefault(octagram.getCachedState().getBlock(), DEFAULT_OCTAGRAM);
    }

    public static SpriteIdentifier getOctagramMaskTextureFor(OctagramBlockEntity octagram) {
        return OCTAGRAM_MASKS.getOrDefault(octagram.getWorld().getBlockState(octagram.getPos()).getBlock(), DEFAULT_OCTAGRAM_MASK);
    }

    public static SpriteIdentifier getStatueTextureFor(StatueBlockEntity statue) {
        return STATUE_SPRITES.getOrDefault(statue.getWorld().getBlockState(statue.getPos()).getBlock(), new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("")));
    }

    public static SpriteIdentifier getMatchingOctagramTexture(Affiliation affiliation) {
        if (affiliation == null || affiliation == MMAffiliations.NONE) {
            return ResourceHandler.DEFAULT_OCTAGRAM;
        }
        for (OctagramBlock octagramBlock : ResourceHandler.OCTAGRAM_SPRITES.keySet()) {
            if (affiliation.equals(octagramBlock.getAffiliation(false))) {
                return ResourceHandler.OCTAGRAM_SPRITES.get(octagramBlock);
            }
        }
        return ResourceHandler.DEFAULT_OCTAGRAM;
    }
}
