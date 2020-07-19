package com.miskatonicmysteries.lib;

import net.minecraft.block.Block;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public static Block registerBlock(Block block, String name){
        Block registeredBlock = register(Registry.BLOCK, name, block);
        register(Registry.ITEM, name, new BlockItem(block, new Item.Settings().group(Constants.MM_GROUP)));
        return registeredBlock;
    }

    public static <T> T register(Registry<? super T> registry, String name, T entry) {
        return Registry.register(registry, new Identifier(Constants.MOD_ID, name), entry);
    }

    public static boolean areItemStackListsEqual(List<Ingredient> ings, Inventory inv) {
        List<ItemStack> checklist = new ArrayList<>();
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            if (!stack.isEmpty()) checklist.add(stack);
        }
        if (ings.size() != checklist.size()) return false;
        for (Ingredient ing : ings) {
            boolean found = false;
            for (ItemStack stack : checklist) {
                if (ing.test(stack)) {
                    found = true;
                    checklist.remove(stack);
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
    }

    public static boolean areItemStackListsExactlyEqual(List<Ingredient> ings, Inventory inv) {
        List<ItemStack> checklist = new ArrayList<>();
        for (int i = 0; i < inv.size(); i++) {
            checklist.add( inv.getStack(i));
        }
        if (ings.size() != checklist.size()) return false;
        for (Ingredient ing : ings) {
            boolean found = false;
            for (ItemStack stack : checklist) {
                if (ing.test(stack)) {
                    found = true;
                    checklist.remove(stack);
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
    }
}
