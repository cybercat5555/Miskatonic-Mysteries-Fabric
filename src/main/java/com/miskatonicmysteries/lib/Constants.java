package com.miskatonicmysteries.lib;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;

public class Constants {
    public static final String MOD_ID = "miskatonicmysteries";
    public static final ItemGroup MM_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "group"), () -> ModObjects.OCEANIC_GOLD.getStackForRender());

    public static class NBT{
        public static final String RECEIVED_STACK = "Received Stack";
        public static final String REALIZED_STACK = "Realized Stack";
        public static final String POTENTIAL_ITEMS = "Potential Items";
    }
}
