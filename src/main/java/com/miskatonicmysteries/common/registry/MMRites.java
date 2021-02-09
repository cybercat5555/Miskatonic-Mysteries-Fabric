package com.miskatonicmysteries.common.registry;

import com.miskatonicmysteries.api.registry.Rite;
import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.recipe.rite.*;
import net.minecraft.util.registry.Registry;

public class MMRites {
    public static final Rite OPEN_WAY = new TeleportRite();
    public static final Rite BURNED_VEIL = new BurnedVeilRite();
    public static final Rite BROKEN_VEIL = new BrokenVeilRite();
    public static final Rite HYSTERIA = new HysteriaRite();
    public static final Rite SCULPTOR_RITE = new SculptorRite();

    public static void init() {
        register(OPEN_WAY);
        register(BURNED_VEIL);
        register(BROKEN_VEIL);
        register(HYSTERIA);
        register(SCULPTOR_RITE);
    }

    private static void register(Rite rite) {
        Registry.register(MMRegistries.RITES, rite.getId(), rite);
    }

    public static Rite getRite(OctagramBlockEntity octagram) {
        return MMRegistries.RITES.stream().filter(r -> r.canCast(octagram)).findFirst().orElse(null);
    }
}
