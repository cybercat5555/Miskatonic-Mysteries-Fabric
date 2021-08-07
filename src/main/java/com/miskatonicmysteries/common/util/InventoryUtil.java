package com.miskatonicmysteries.common.util;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class InventoryUtil {
    public static boolean areItemStackListsExactlyEqual(List<Ingredient> ings, Inventory inv) {
        List<ItemStack> checklist = new ArrayList<>();
        for (int i = 0; i < inv.size(); i++) {
            checklist.add(inv.getStack(i));
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
            if (!found) {
                return false;
            }
        }
        return true;
    }

    public static int getSlotForItemInHotbar(PlayerEntity player, Item item) {
        for (int i = 0; i < 9; i++) {
            if (player.getInventory().getStack(i).getItem().equals(item)) return i;
        }
        return -1;
    }

    public static void giveItem(World world, PlayerEntity player, ItemStack item) {
        if (!world.isClient) {
            world.spawnEntity(new ItemEntity(world, player.getX(), player.getY() + 0.5, player.getZ(), item)); //might spawn those more efficiently
        }
    }
}
