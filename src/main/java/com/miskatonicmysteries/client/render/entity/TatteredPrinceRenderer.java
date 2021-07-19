package com.miskatonicmysteries.client.render.entity;

import com.miskatonicmysteries.client.model.entity.dummy.TatteredPrinceDummyModel;
import com.miskatonicmysteries.client.model.entity.TatteredPrinceModel;
import com.miskatonicmysteries.common.entity.TatteredPrinceEntity;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import software.bernie.geckolib3.renderer.geo.GeoEntityRenderer;

public class TatteredPrinceRenderer extends GeoEntityRenderer<TatteredPrinceEntity> {
    public static final Model DUMMY_PRINCE_MODEL = new TatteredPrinceDummyModel();
    public TatteredPrinceRenderer(EntityRenderDispatcher dispatcher) {
        super(dispatcher, new TatteredPrinceModel());
        this.shadowRadius = 1;
    }
}
