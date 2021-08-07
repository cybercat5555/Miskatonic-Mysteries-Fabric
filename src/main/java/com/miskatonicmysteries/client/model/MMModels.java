package com.miskatonicmysteries.client.model;

import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.client.model.armor.HasturMaskModel;
import com.miskatonicmysteries.client.model.armor.ShubAlternateMaskModel;
import com.miskatonicmysteries.client.model.armor.ShubMaskModel;
import com.miskatonicmysteries.client.model.block.CthulhuStatueModel;
import com.miskatonicmysteries.client.model.block.HasturStatueModel;
import com.miskatonicmysteries.client.model.block.ShubStatueModel;
import com.miskatonicmysteries.client.model.block.StatueModel;
import com.miskatonicmysteries.client.model.entity.*;
import com.miskatonicmysteries.client.model.entity.dummy.TatteredPrinceDummyModel;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.util.Constants;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MMModels {
    //no-op for gecko-models
    public static final EntityModelLayer BYAKHEE = create("byakhee");
    public static final EntityModelLayer HARROW = create("harrow");
    public static final EntityModelLayer HASTUR_CULTIST = create("hastur_cultist");
    public static final EntityModelLayer ASCENDED_HASTUR_CULTIST = create("ascended_hastur_cultist");
    public static final EntityModelLayer PROTAGONIST = create("protagonist");
    public static final EntityModelLayer TATTERED_PRINCE_DUMMY = create("prince_dummy");

    public static final EntityModelLayer HASTUR_MASK = create("hastur_mask");
    public static final EntityModelLayer SHUB_MASK = create("shub_mask");
    public static final EntityModelLayer SHUB_ALT_MASK = create("shub_alt_mask");

    public static final EntityModelLayer ROBES = create("robes");

    public static final EntityModelLayer CTHULHU_STATUE = create("cthulhu_statue");
    public static final EntityModelLayer HASTUR_STATUE = create("hastur_statue");
    public static final EntityModelLayer SHUB_STATUE = create("shub_statue");
    public static final Map<Affiliation, Function<BlockEntityRendererFactory.Context, StatueModel>> STATUE_MODELS = new HashMap<>();


    @SuppressWarnings("UnstableApiUsage")
    public static void init() {
        EntityModelLayerRegistry.registerModelLayer(BYAKHEE, ByakheeEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(HARROW, HarrowEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(HASTUR_CULTIST, HasturCultistEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ASCENDED_HASTUR_CULTIST, AscendedHasturCultistEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(PROTAGONIST, ProtagonistEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(TATTERED_PRINCE_DUMMY, TatteredPrinceDummyModel::getTexturedModelData);

        EntityModelLayerRegistry.registerModelLayer(HASTUR_MASK, HasturMaskModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(SHUB_MASK, ShubMaskModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(SHUB_ALT_MASK, ShubAlternateMaskModel::getTexturedModelData);
        //todo EntityModelLayerRegistry.registerModelLayer(ROBES, CultistRobesModel::getTexturedModelData);

        EntityModelLayerRegistry.registerModelLayer(CTHULHU_STATUE, CthulhuStatueModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(HASTUR_STATUE, HasturStatueModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(SHUB_STATUE, ShubStatueModel::getTexturedModelData);

        registerStatueModel(MMAffiliations.CTHULHU, (ctx) -> new CthulhuStatueModel(ctx.getLayerModelPart(CTHULHU_STATUE)));
        registerStatueModel(MMAffiliations.HASTUR, (ctx) -> new HasturStatueModel(ctx.getLayerModelPart(HASTUR_STATUE)));
        registerStatueModel(MMAffiliations.SHUB, (ctx) -> new ShubStatueModel(ctx.getLayerModelPart(SHUB_STATUE)));
    }

    public static void registerStatueModel(Affiliation affiliation, Function<BlockEntityRendererFactory.Context, StatueModel> modelFunction) {
        STATUE_MODELS.put(affiliation, modelFunction);
    }

    private static EntityModelLayer create(String name) {
        return new EntityModelLayer(new Identifier(Constants.MOD_ID, name), "main");
    }
}
