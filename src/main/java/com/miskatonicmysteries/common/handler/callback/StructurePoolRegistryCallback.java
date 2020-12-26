package com.miskatonicmysteries.common.handler.callback;

import com.miskatonicmysteries.common.feature.world.structures.ModifiableStructurePool;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface StructurePoolRegistryCallback {
    Event<StructurePoolRegistryCallback> EVENT = EventFactory.createArrayBacked(StructurePoolRegistryCallback.class,
            listeners -> pool -> {
                for (StructurePoolRegistryCallback listener : listeners) {
                    listener.add(pool);
                }
            });

    void add(ModifiableStructurePool pool);
}