package com.miskatonicmysteries.common.feature.sanity;

import com.google.gson.JsonObject;
import com.miskatonicmysteries.common.feature.DataSerializable;
import com.miskatonicmysteries.common.handler.PacketHandler;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.server.function.CommandFunctionManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.HashMap;
import java.util.Map;

public class CommandDrivenInsanityEvent extends InsanityEvent implements DataSerializable<CommandDrivenInsanityEvent> {
    public final Identifier commandId;
    public CommandDrivenInsanityEvent(Identifier id, float baseChance, int insanityThreshold, Identifier function) {
        super(id, baseChance, insanityThreshold);
        commandId = function;
    }

    public boolean execute(PlayerEntity playerEntity, ISanity sanity){
        if (!playerEntity.world.isClient){
            ServerCommandSource source = playerEntity.getCommandSource();
            CommandFunctionManager manager = playerEntity.getServer().getCommandFunctionManager();
            CommandFunction.LazyContainer function = new CommandFunction.LazyContainer(commandId);
            if (function.get(manager).isPresent())
                manager.execute(function.get(manager).get(), source.withSilent().withMaxLevel(2));
        }
        return true;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    public static class Serializer extends DataSerializable.DataReader<InsanityEvent> {
        @Override
        public InsanityEvent readFromJson(Identifier id, JsonObject json) {
            System.out.println("nice");
            return new CommandDrivenInsanityEvent(id, JsonHelper.getFloat(json, "baseChance"), JsonHelper.getInt(json, "insanityThreshold"), new Identifier(JsonHelper.getString(json, "function")));
        }

        @Override
        public Map<Identifier, InsanityEvent> getAccessMap() {
            return INSANITY_EVENTS;
        }
    }
}
