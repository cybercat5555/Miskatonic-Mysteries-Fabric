package com.miskatonicmysteries.client.render;

import com.miskatonicmysteries.client.model.armor.HasturMaskModel;
import com.miskatonicmysteries.client.model.armor.ShubAlternateMaskModel;
import com.miskatonicmysteries.client.model.armor.ShubMaskModel;
import com.miskatonicmysteries.client.model.block.CthulhuStatueModel;
import com.miskatonicmysteries.client.model.block.HasturStatueModel;
import com.miskatonicmysteries.client.model.block.ShubStatueModel;
import com.miskatonicmysteries.common.block.OctagramBlock;
import com.miskatonicmysteries.common.block.StatueBlock;
import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.block.blockentity.StatueBlockEntity;
import com.miskatonicmysteries.common.feature.Affiliation;
import com.miskatonicmysteries.common.item.armor.CultistArmor;
import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.MMObjects;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderingRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.block.entity.EnchantingTableBlockEntityRenderer;
import net.minecraft.client.render.entity.model.CreeperEntityModel;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResourceHandler {
    public static final SpriteIdentifier DEFAULT_OCTAGRAM = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(Constants.MOD_ID, "block/octagram/octagram_generic"));
    public static final SpriteIdentifier DEFAULT_OCTAGRAM_MASK = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(Constants.MOD_ID, "block/octagram/mask/octagram_mask"));
    public static final SpriteIdentifier AURA_SPRITE = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(Constants.MOD_ID, "misc/aura"));

    public static final Map<Item, SpriteIdentifier> BOOK_SPRITES = new HashMap<>();
    public static final Map<OctagramBlock, SpriteIdentifier> OCTAGRAM_SPRITES = new HashMap<>();
    public static final Map<OctagramBlock, SpriteIdentifier> OCTAGRAM_MASKS = new HashMap<>();

    public static final Map<Affiliation, Model> STATUE_MODELS = new HashMap<>();
    public static final Map<StatueBlock, SpriteIdentifier> STATUE_SPRITES = new HashMap<>();


    public static final Map<Item, Model> MASK_MODELS = new HashMap<>();

    public static void init() {
        ClientSpriteRegistryCallback.registerBlockAtlas((spriteAtlasTexture, registry) -> {
            registry.register(new Identifier(Constants.MOD_ID, "misc/book_necronomicon"));
            registry.register(new Identifier(Constants.MOD_ID, "misc/aura"));
            registry.register(new Identifier(Constants.MOD_ID, "block/octagram/mask/hastur_octagram_mask"));
            registry.register(new Identifier(Constants.MOD_ID, "block/octagram/mask/shub_octagram_mask"));
            registry.register(new Identifier(Constants.MOD_ID, "block/octagram/mask/cthulhu_octagram_mask"));
        });
        ResourceHandler.addBookTextureFor(MMObjects.NECRONOMICON, new Identifier(Constants.MOD_ID, "misc/book_necronomicon"));

        ResourceHandler.addOctagramTextureFor(MMObjects.CTHULHU_OCTAGRAM, new Identifier(Constants.MOD_ID, "block/octagram/cthulhu_octagram"),
                new Identifier(Constants.MOD_ID, "block/octagram/mask/cthulhu_octagram_mask"));
        ResourceHandler.addOctagramTextureFor(MMObjects.HASTUR_OCTAGRAM, new Identifier(Constants.MOD_ID, "block/octagram/hastur_octagram"),
                new Identifier(Constants.MOD_ID, "block/octagram/mask/hastur_octagram_mask"));
        ResourceHandler.addOctagramTextureFor(MMObjects.SHUB_OCTAGRAM, new Identifier(Constants.MOD_ID, "block/octagram/shub_octagram"),
                new Identifier(Constants.MOD_ID, "block/octagram/mask/shub_octagram_mask"));

        addStatueModelFor(Affiliation.CTHULHU, new CthulhuStatueModel());
        addStatueModelFor(Affiliation.HASTUR, new HasturStatueModel());
        addStatueModelFor(Affiliation.SHUB, new ShubStatueModel());

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
        addStatueTextureFor(MMObjects.SHUB_STATUE_BLACKSTONE, new Identifier(Constants.MOD_ID, "block/statue/shub_statue_blackstone"));
        addStatueTextureFor(MMObjects.SHUB_STATUE_STONE, new Identifier(Constants.MOD_ID, "block/statue/shub_statue_stone"));

        addMaskModel(MMObjects.ELEGANT_MASK, new HasturMaskModel());
        addMaskModel(MMObjects.FERAL_MASK, new ShubMaskModel());
        addMaskModel(MMObjects.WILD_MASK, new ShubAlternateMaskModel());

        //Botania code
        List<Item> armors = Registry.ITEM.stream()
                .filter(i -> i instanceof CultistArmor
                        && Registry.ITEM.getId(i).getNamespace().equals(Constants.MOD_ID))
                .collect(Collectors.toList());

        ArmorRenderingRegistry.ModelProvider p = (entity, stack, slot, original) -> ((CultistArmor) stack.getItem()).getArmorModel(entity, stack, slot, original);
        ArmorRenderingRegistry.registerModel(p, armors);

        ArmorRenderingRegistry.TextureProvider t = (entity, stack, slot, secondLayer, suffix, original) -> ((CultistArmor) stack.getItem()).getArmorTexture(stack, slot);
        ArmorRenderingRegistry.registerTexture(t, armors);
    }

    public static void addBookTextureFor(Item item, Identifier texture) {
        BOOK_SPRITES.put(item, new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, texture));
    }

    public static void addOctagramTextureFor(OctagramBlock octagram, Identifier texture, Identifier maskTexture) {
        OCTAGRAM_SPRITES.put(octagram, new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, texture));
        OCTAGRAM_MASKS.put(octagram, new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, maskTexture));
    }

    public static void addStatueModelFor(Affiliation affiliation, Model model) {
        STATUE_MODELS.put(affiliation, model);
    }

    public static void addStatueTextureFor(StatueBlock statue, Identifier texture) {
        STATUE_SPRITES.put(statue, new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, texture));
    }

    public static void addMaskModel(Item mask, Model model) {
        MASK_MODELS.put(mask, model);
    }

    public static SpriteIdentifier getBookTextureFor(ItemStack stack) {
        return BOOK_SPRITES.getOrDefault(stack.getItem(), EnchantingTableBlockEntityRenderer.BOOK_TEXTURE);
    }

    public static SpriteIdentifier getOctagramTextureFor(OctagramBlockEntity octagram) {
        return OCTAGRAM_SPRITES.getOrDefault(octagram.getWorld().getBlockState(octagram.getPos()).getBlock(), DEFAULT_OCTAGRAM);
    }

    public static SpriteIdentifier getOctagramMaskTextureFor(OctagramBlockEntity octagram) {
        return OCTAGRAM_MASKS.getOrDefault(octagram.getWorld().getBlockState(octagram.getPos()).getBlock(), DEFAULT_OCTAGRAM_MASK);
    }

    public static Model getStatueModelFor(StatueBlockEntity statue) {
        return STATUE_MODELS.getOrDefault(statue.getAffiliation(false), new CreeperEntityModel<>());
    }

    public static SpriteIdentifier getStatueTextureFor(StatueBlockEntity statue) {
        return STATUE_SPRITES.getOrDefault(statue.getWorld().getBlockState(statue.getPos()).getBlock(), new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("")));
    }

    public static Model getMaskModel(Item mask) {
        return MASK_MODELS.getOrDefault(mask, new CreeperEntityModel<>());
    }

}
