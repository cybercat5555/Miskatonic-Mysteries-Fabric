package net.miskatonicmysteries;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.miskatonicmysteries.registry.ModItems; // Assuming this exists

public class MiskatonicMysteries implements ModInitializer {
    public static final String MOD_ID = "miskatonicmysteries";

    // NEW 1.20.1: Register the Item Group Key
    public static final RegistryKey<ItemGroup> MM_GROUP_KEY = RegistryKey.of(
        RegistryKeys.ITEM_GROUP, 
        new Identifier(MOD_ID, "general")
    );

    // Register the ItemGroup itself
    public static final ItemGroup MM_GROUP = Registry.register(
        Registries.ITEM_GROUP, 
        MM_GROUP_KEY,
        FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.NECRONOMICON)) // Replace with your icon item
            .displayName(Text.translatable("itemGroup.miskatonicmysteries.general"))
            .build()
    );

    @Override
    public void onInitialize() {
        // Register Items and Blocks
        ModItems.registerModItems();
        
        // Initialize GeckoLib (Required for v4)
        software.bernie.geckolib.GeckoLib.initialize();
    }
}
