package com.miskatonicmysteries.common.lib.util;

import com.miskatonicmysteries.common.feature.Affiliated;
import com.miskatonicmysteries.common.feature.Affiliation;
import net.minecraft.entity.Entity;

public class CapabilityUtil {
    public static boolean isAffiliated(Entity entity) {
        return entity instanceof Affiliated && ((Affiliated) entity).getAffiliation(true) != Affiliation.NONE;
    }

    public static Affiliation getAffiliation(Entity entity, boolean apparent) {
        return isAffiliated(entity) ? ((Affiliated) entity).getAffiliation(apparent) : Affiliation.NONE;
    }

    public static boolean shouldRecognizeAffiliation(Entity entity) {
        return isAffiliated(entity) && ((Affiliated) entity).isSupernatural();
    }
}
