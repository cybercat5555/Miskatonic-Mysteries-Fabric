package com.miskatonicmysteries.api.interfaces;

import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public interface DataSerializable<T> {
    Identifier getId();

    abstract class DataReader<T> {
        public abstract T readFromJson(Identifier id, JsonObject json);

        public abstract Registry<T> getRegistry();
    }
}
