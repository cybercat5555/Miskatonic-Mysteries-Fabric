package com.miskatonicmysteries.common.feature;

import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public interface DataSerializable<T> {
    Identifier getId();
    abstract class DataReader<T> {
        public abstract T readFromJson(Identifier id, JsonObject json);

        public abstract Map<Identifier, T> getAccessMap();
    }
}
