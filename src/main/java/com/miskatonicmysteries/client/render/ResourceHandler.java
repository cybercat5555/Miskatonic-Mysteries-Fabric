package com.miskatonicmysteries.client.render;

import com.miskatonicmysteries.client.model.block.CthulhuStatueModel;
import com.miskatonicmysteries.client.model.block.HasturStatueModel;
import com.miskatonicmysteries.client.model.block.ShubStatueModel;
import com.miskatonicmysteries.common.block.BlockOctagram;
import com.miskatonicmysteries.common.block.BlockStatue;
import com.miskatonicmysteries.common.block.blockentity.BlockEntityOctagram;
import com.miskatonicmysteries.common.block.blockentity.BlockEntityStatue;
import com.miskatonicmysteries.lib.ModObjects;
import com.miskatonicmysteries.lib.util.Constants;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.block.entity.EnchantingTableBlockEntityRenderer;
import net.minecraft.client.render.entity.model.CreeperEntityModel;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class ResourceHandler {
    public static final SpriteIdentifier DEFAULT_OCTAGRAM = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(Constants.MOD_ID, "block/octagram/octagram_generic"));
    public static final Map<Item, SpriteIdentifier> BOOK_SPRITES = new HashMap<>();
    public static final Map<BlockOctagram, SpriteIdentifier> OCTAGRAM_SPRITES = new HashMap<>();
    public static final Map<Identifier, Model> STATUE_MODELS = new HashMap<>();
    public static final Map<BlockStatue, SpriteIdentifier> STATUE_SPRITES = new HashMap<>();

    public static void init() {
        ClientSpriteRegistryCallback.registerBlockAtlas((spriteAtlasTexture, registry) -> {
            registry.register(new Identifier(Constants.MOD_ID, "misc/book_necronomicon"));
        });
        ResourceHandler.addBookTextureFor(ModObjects.NECRONOMICON, new Identifier(Constants.MOD_ID, "misc/book_necronomicon"));

        ResourceHandler.addOctagramTextureFor(ModObjects.CTHULHU_OCTAGRAM, new Identifier(Constants.MOD_ID, "block/octagram/cthulhu_octagram"));
        ResourceHandler.addOctagramTextureFor(ModObjects.HASTUR_OCTAGRAM, new Identifier(Constants.MOD_ID, "block/octagram/hastur_octagram"));
        ResourceHandler.addOctagramTextureFor(ModObjects.SHUB_OCTAGRAM, new Identifier(Constants.MOD_ID, "block/octagram/shub_octagram"));

        addStatueModelFor(Constants.Affiliation.CTHULHU, new CthulhuStatueModel());
        addStatueModelFor(Constants.Affiliation.HASTUR, new HasturStatueModel());
        addStatueModelFor(Constants.Affiliation.SHUB, new ShubStatueModel());

        addStatueTextureFor(ModObjects.CTHULHU_STATUE_GOLD, new Identifier(Constants.MOD_ID, "block/statue/cthulhu_statue_gold"));
        addStatueTextureFor(ModObjects.CTHULHU_STATUE_MOSSY, new Identifier(Constants.MOD_ID, "block/statue/cthulhu_statue_mossy"));
        addStatueTextureFor(ModObjects.CTHULHU_STATUE_PRISMARINE, new Identifier(Constants.MOD_ID, "block/statue/cthulhu_statue_prismarine"));
        addStatueTextureFor(ModObjects.CTHULHU_STATUE_STONE, new Identifier(Constants.MOD_ID, "block/statue/cthulhu_statue_stone"));

        addStatueTextureFor(ModObjects.HASTUR_STATUE_GOLD, new Identifier(Constants.MOD_ID, "block/statue/hastur_statue_gold"));
        addStatueTextureFor(ModObjects.HASTUR_STATUE_MOSSY, new Identifier(Constants.MOD_ID, "block/statue/hastur_statue_mossy"));
        addStatueTextureFor(ModObjects.HASTUR_STATUE_TERRACOTTA, new Identifier(Constants.MOD_ID, "block/statue/hastur_statue_terracotta"));
        addStatueTextureFor(ModObjects.HASTUR_STATUE_STONE, new Identifier(Constants.MOD_ID, "block/statue/hastur_statue_stone"));

        addStatueTextureFor(ModObjects.SHUB_STATUE_GOLD, new Identifier(Constants.MOD_ID, "block/statue/shub_statue_gold"));
        addStatueTextureFor(ModObjects.SHUB_STATUE_MOSSY, new Identifier(Constants.MOD_ID, "block/statue/shub_statue_mossy"));
        addStatueTextureFor(ModObjects.SHUB_STATUE_BLACKSTONE, new Identifier(Constants.MOD_ID, "block/statue/shub_statue_blackstone"));
        addStatueTextureFor(ModObjects.SHUB_STATUE_STONE, new Identifier(Constants.MOD_ID, "block/statue/shub_statue_stone"));

    }

    public static void addBookTextureFor(Item item, Identifier texture) {
        BOOK_SPRITES.put(item, new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, texture));
    }

    public static void addOctagramTextureFor(BlockOctagram octagram, Identifier texture) {
        OCTAGRAM_SPRITES.put(octagram, new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, texture));
    }

    public static void addStatueModelFor(Identifier affiliation, Model model) {
        STATUE_MODELS.put(affiliation, model);
    }

    public static void addStatueTextureFor(BlockStatue statue, Identifier texture) {
        STATUE_SPRITES.put(statue, new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, texture));
    }

    public static SpriteIdentifier getBookTextureFor(ItemStack stack) {
        return BOOK_SPRITES.getOrDefault(stack.getItem(), EnchantingTableBlockEntityRenderer.BOOK_TEXTURE);
    }

    public static SpriteIdentifier getOctagramTextureFor(BlockEntityOctagram octagram) {
        return OCTAGRAM_SPRITES.getOrDefault(octagram.getWorld().getBlockState(octagram.getPos()).getBlock(), DEFAULT_OCTAGRAM);
    }

    public static Model getStatueModelFor(BlockEntityStatue statue) {
        return STATUE_MODELS.getOrDefault(statue.getAffiliation(), new CreeperEntityModel<>());
    }

    public static SpriteIdentifier getStatueTextureFor(BlockEntityStatue statue) {
        return STATUE_SPRITES.getOrDefault(statue.getWorld().getBlockState(statue.getPos()).getBlock(), new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("")));
    }
}
