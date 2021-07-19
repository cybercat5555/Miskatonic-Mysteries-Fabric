package com.miskatonicmysteries.client.model;

import com.miskatonicmysteries.common.util.Constants;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SheepEntityModel;
import net.minecraft.util.Identifier;

public class MMModels {
    //no-op for gecko-models
    public static final EntityModelLayer BYAKHEE = create("byakhee");
    public static final EntityModelLayer HARROW = create("harrow");
    public static final EntityModelLayer HASTUR_CULTIST = create("hastur_cultist");
    public static final EntityModelLayer ASCENDED_HASTUR_CULTIST = create("ascended_hastur_cultist");
    public static final EntityModelLayer PROTAGONIST = create("protagonist");
    public static final EntityModelLayer TATTERED_PRINCE_DUMMY = create("prince_Dummy");

    public static final EntityModelLayer HASTUR_MASK = create("hastur_mask");
    public static final EntityModelLayer SHUB_MASK = create("shub_mask");
    public static final EntityModelLayer SHUB_ALT_MASK = create("shub_alt_mask");
    public static final EntityModelLayer ROBES = create("robes");

    public static final EntityModelLayer CTHULHU_STATUE = create("cthulhu_statue");
    public static final EntityModelLayer HASTUR_STATUE = create("hastur_statue");
    public static final EntityModelLayer SHUB_STATUE = create("shub_statue");


    public static void init(){
        //todo fix models
        EntityModelLayerRegistry.registerModelLayer(BYAKHEE, SheepEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(HARROW, SheepEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(HASTUR_CULTIST, SheepEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ASCENDED_HASTUR_CULTIST, SheepEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(PROTAGONIST, SheepEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(TATTERED_PRINCE_DUMMY, SheepEntityModel::getTexturedModelData);

        EntityModelLayerRegistry.registerModelLayer(HASTUR_MASK, SheepEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(SHUB_MASK, SheepEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(SHUB_ALT_MASK, SheepEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ROBES, SheepEntityModel::getTexturedModelData);

        EntityModelLayerRegistry.registerModelLayer(CTHULHU_STATUE, SheepEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(HASTUR_STATUE, SheepEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(SHUB_STATUE, SheepEntityModel::getTexturedModelData);
    }

    private static EntityModelLayer create(String name) {
        return new EntityModelLayer(new Identifier(Constants.MOD_ID, name), "main");
    }
}
