package com.miskatonicmysteries.common.handler;

import com.google.gson.*;
import com.miskatonicmysteries.common.feature.DataSerializable;
import com.miskatonicmysteries.common.feature.sanity.CommandDrivenInsanityEvent;
import com.miskatonicmysteries.common.feature.sanity.InsanityEvent;
import com.miskatonicmysteries.common.feature.sanity.InsanityInducer;
import com.miskatonicmysteries.lib.Constants;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class DataHandler extends JsonDataLoader {
    public static final Map<Identifier, DataSerializable.DataReader> DATA_READERS = new HashMap<>();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogManager.getLogger();
    public DataHandler() {
        super(GSON, Constants.MOD_ID);
        DATA_READERS.put(new Identifier(Constants.MOD_ID, "insanity_inducer"), new InsanityInducer.Serializer());
        DATA_READERS.put(new Identifier(Constants.MOD_ID, "insanity_event"), new CommandDrivenInsanityEvent.Serializer());
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> loader, ResourceManager manager, Profiler profiler) {
        loader.forEach((id, element) -> {
            String type = JsonHelper.getString((JsonObject) element, "type");
            try{
                DataSerializable.DataReader reader = DATA_READERS.get(new Identifier(type));
                reader.getAccessMap().put(id, reader.readFromJson(id, (JsonObject) element));
            }catch (IllegalArgumentException | JsonParseException | NullPointerException exception){
                LOGGER.error("Parsing error loading data module {} for Miskatonic Mysteries", id, exception);
                exception.printStackTrace();
            }
        });
    }
}
