package com.miskatonicmysteries.common.util;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RegistryUtil {
    public static Block registerBlock(Block block, String name) {
        Block registeredBlock = register(Registry.BLOCK, name, block);
        register(Registry.ITEM, name, new BlockItem(block, new Item.Settings().group(Constants.MM_GROUP)));
        return registeredBlock;
    }

    public static <T> T register(Registry<? super T> registry, String name, T entry) {
        return Registry.register(registry, new Identifier(Constants.MOD_ID, name), entry);
    }
}
