package com.miskatonicmysteries.lib;

import com.miskatonicmysteries.common.block.BlockChemistrySet;
import com.miskatonicmysteries.common.block.blockentity.BlockEntityChemistrySet;
import com.miskatonicmysteries.common.item.ItemRevolver;
import com.miskatonicmysteries.common.item.ItemRifle;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class ModObjects {
    public static final Item OCEANIC_GOLD = new Item(new Item.Settings().group(Constants.MM_GROUP));
    public static final Item RIFLE = new ItemRifle();
    public static final Item REVOLVER = new ItemRevolver();
    public static final Block CHEMISTRY_SET = new BlockChemistrySet();

    public static final BlockEntityType<BlockEntityChemistrySet> CHEMISTRY_SET_BLOCK_ENTITY_TYPE = BlockEntityType.Builder.create(BlockEntityChemistrySet::new, CHEMISTRY_SET).build(null);
    public static void init(){
        Util.register(Registry.BLOCK_ENTITY_TYPE, "chemistry_set", CHEMISTRY_SET_BLOCK_ENTITY_TYPE);

        Util.registerBlock(CHEMISTRY_SET, "chemistry_set");
        Util.register(Registry.ITEM, "oceanic_gold", OCEANIC_GOLD);
        Util.register(Registry.ITEM, "rifle", RIFLE);
        Util.register(Registry.ITEM, "revolver", REVOLVER);
    }
}
