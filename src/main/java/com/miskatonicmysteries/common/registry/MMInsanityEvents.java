package com.miskatonicmysteries.common.registry;

import com.miskatonicmysteries.api.registry.InsanityEvent;
import com.miskatonicmysteries.common.feature.sanity.ExoticCravingsInsanityEvent;
import com.miskatonicmysteries.common.feature.sanity.SpookySoundInsanityEvent;
import net.minecraft.util.registry.Registry;

public class MMInsanityEvents {
    public static final InsanityEvent SPOOKY_SOUND = new SpookySoundInsanityEvent();
    public static final InsanityEvent EXOTIC_CRAVINGS = new ExoticCravingsInsanityEvent();

    public static void init() {
        register(SPOOKY_SOUND);
        register(EXOTIC_CRAVINGS);
    }

    private static void register(InsanityEvent event) {
        Registry.register(MMRegistries.INSANITY_EVENTS, event.getId(), event);
    }
}
