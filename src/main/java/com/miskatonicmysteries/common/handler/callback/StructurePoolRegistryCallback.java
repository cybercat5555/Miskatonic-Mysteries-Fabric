package com.miskatonicmysteries.common.handler.callback;

import com.miskatonicmysteries.common.feature.world.structures.ModifiableStructurePool;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

//code taken from https://github.com/FoundationGames/Sandwichable/tree/master/remappedSrc/io/github/foundationgames/sandwichable (credit goes to Foundationgames and Draylar)
public interface StructurePoolRegistryCallback {
    Event<StructurePoolRegistryCallback> EVENT = EventFactory.createArrayBacked(StructurePoolRegistryCallback.class,
            listeners -> pool -> {
                for (StructurePoolRegistryCallback listener : listeners) {
                    listener.add(pool);
                }
            });

    void add(ModifiableStructurePool pool);
}