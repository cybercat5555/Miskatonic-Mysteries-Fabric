package com.miskatonicmysteries.lib;

import com.miskatonicmysteries.common.block.BlockChemistrySet;
import com.miskatonicmysteries.common.block.blockentity.BlockEntityChemistrySet;
import com.miskatonicmysteries.common.item.ItemRevolver;
import com.miskatonicmysteries.common.item.ItemRifle;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;

public class ModObjects {
    public static final Block CHEMISTRY_SET = new BlockChemistrySet();

    public static final Item OCEANIC_GOLD = new Item(new Item.Settings().group(Constants.MM_GROUP));

    public static final Item RIFLE = new ItemRifle();
    public static final Item REVOLVER = new ItemRevolver();
    public static final Item BULLET = new Item(new Item.Settings().group(Constants.MM_GROUP));

    public static final Item SYRINGE = new Item(new Item.Settings().group(Constants.MM_GROUP));

    public static final Item INFESTED_WHEAT = new Item(new Item.Settings().group(Constants.MM_GROUP));
    //all these have no effect yet, but need one
    public static final Item BLOTTER = new Item(new Item.Settings().group(Constants.MM_GROUP));
    public static final Item LAUDANUM = new Item(new Item.Settings().group(Constants.MM_GROUP).recipeRemainder(Items.GLASS_BOTTLE));
    public static final Item RE_AGENT_SYRINGE = new Item(new Item.Settings().group(Constants.MM_GROUP).recipeRemainder(SYRINGE));

    public static final Item TALLOW = new Item(new Item.Settings().group(Constants.MM_GROUP));

    public static final BlockEntityType<BlockEntityChemistrySet> CHEMISTRY_SET_BLOCK_ENTITY_TYPE = BlockEntityType.Builder.create(BlockEntityChemistrySet::new, CHEMISTRY_SET).build(null);

    public static void init(){
        Util.register(Registry.BLOCK_ENTITY_TYPE, "chemistry_set", CHEMISTRY_SET_BLOCK_ENTITY_TYPE);

        Util.registerBlock(CHEMISTRY_SET, "chemistry_set");
        Util.register(Registry.ITEM, "oceanic_gold", OCEANIC_GOLD);

        Util.register(Registry.ITEM, "rifle", RIFLE);
        Util.register(Registry.ITEM, "revolver", REVOLVER);
        Util.register(Registry.ITEM, "bullet", BULLET);

        Util.register(Registry.ITEM, "syringe", SYRINGE);
        Util.register(Registry.ITEM, "infested_wheat", INFESTED_WHEAT);
        Util.register(Registry.ITEM, "blotter", BLOTTER);
        Util.register(Registry.ITEM, "laudanum", LAUDANUM);
        Util.register(Registry.ITEM, "re_agent_syringe", RE_AGENT_SYRINGE);

        Util.register(Registry.ITEM, "tallow", TALLOW);
    }
}
