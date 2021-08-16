package com.miskatonicmysteries.client.render.entity;

import com.miskatonicmysteries.client.model.MMModels;
import com.miskatonicmysteries.client.model.entity.TatteredPrinceModel;
import com.miskatonicmysteries.client.model.entity.dummy.TatteredPrinceDummyModel;
import com.miskatonicmysteries.common.entity.TatteredPrinceEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class TatteredPrinceRenderer extends GeoEntityRenderer<TatteredPrinceEntity> {
    public static Model dummyPrinceModel;

    public TatteredPrinceRenderer(EntityRendererFactory.Context context) {
        super(context, new TatteredPrinceModel());
        this.shadowRadius = 1;
        dummyPrinceModel = new TatteredPrinceDummyModel(context.getPart(MMModels.TATTERED_PRINCE_DUMMY));
    }
}
