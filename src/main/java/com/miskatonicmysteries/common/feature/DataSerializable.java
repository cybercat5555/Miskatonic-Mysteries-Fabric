package com.miskatonicmysteries.common.feature;

import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public abstract class DataSerializable<T> {
    public abstract Identifier getId();
    public abstract static class DataReader<T> {
        public abstract T readFromJson(Identifier id, JsonObject json);

        public abstract Map<Identifier, T> getAccessMap();
    }
}
