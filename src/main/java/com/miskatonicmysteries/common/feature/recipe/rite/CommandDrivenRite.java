package com.miskatonicmysteries.common.feature.recipe.rite;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.miskatonicmysteries.api.interfaces.DataSerializable;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.api.registry.Rite;
import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.registry.MMRegistries;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.server.function.CommandFunctionManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class CommandDrivenRite extends Rite implements DataSerializable<Rite> {
    public final Identifier tickFunctionId;
    public final Identifier finishFunctionId;
    public final int tickCount;

    public CommandDrivenRite(Identifier id, Affiliation affiliation, Identifier tickFunctionId, Identifier finishFunctionId, int tickCount, float investigatorChance, Ingredient... ingredients) {
        super(id, affiliation, investigatorChance, ingredients);
        this.tickFunctionId = tickFunctionId;
        this.finishFunctionId = finishFunctionId;
        this.tickCount = tickCount;
    }

    @Override
    public boolean isFinished(OctagramBlockEntity octagram) {
        return octagram.tickCount >= tickCount;
    }

    @Override
    public boolean shouldContinue(OctagramBlockEntity octagram) {
        return true;
    }

    @Override
    public void tick(OctagramBlockEntity octagram) {
        if (!octagram.getWorld().isClient) {
            ServerCommandSource source = octagram.getWorld().getServer().getCommandSource().withPosition(Vec3d.ofCenter(octagram.getPos())).withEntity(octagram.getOriginalCaster());
            CommandFunctionManager manager = octagram.getWorld().getServer().getCommandFunctionManager();
            CommandFunction.LazyContainer function = new CommandFunction.LazyContainer(tickFunctionId);
            if (function.get(manager).isPresent()) {
                manager.execute(function.get(manager).get(), source.withSilent().withMaxLevel(2));
            }
        }
        super.tick(octagram);
    }

    @Override
    public void onFinished(OctagramBlockEntity octagram) {
        if (!octagram.getWorld().isClient) {
            ServerCommandSource source = octagram.getWorld().getServer().getCommandSource().withPosition(Vec3d.ofCenter(octagram.getPos())).withEntity(octagram.getOriginalCaster());
            CommandFunctionManager manager = octagram.getWorld().getServer().getCommandFunctionManager();
            CommandFunction.LazyContainer function = new CommandFunction.LazyContainer(finishFunctionId);
            if (function.get(manager).isPresent()) {
                manager.execute(function.get(manager).get(), source.withSilent().withMaxLevel(2));
            }
        }
        super.onFinished(octagram);
    }

    @Override
    public Identifier getId() {
        return super.getId();
    }

    public static class Serializer extends DataSerializable.DataReader<Rite> {
        @Override
        public Rite readFromJson(Identifier id, JsonObject json) {
            return new CommandDrivenRite(id, JsonHelper.hasElement(json, "affiliation") ? MMRegistries.AFFILIATIONS.get(new Identifier(JsonHelper.getString(json, "affiliation"))) : null, new Identifier(JsonHelper.getString(json, "tick")), new Identifier(JsonHelper.getString(json, "result")), JsonHelper.getInt(json, "duration", 100), JsonHelper.getFloat(json, "investigatorChance", 0.25F), readIngredients(JsonHelper.getArray(json, "ingredients")));
        }

        private Ingredient[] readIngredients(JsonArray json) {
            List<Ingredient> ingredients = new ArrayList<>();
            json.forEach(jsonElement -> ingredients.add(Ingredient.fromJson(jsonElement)));
            return ingredients.toArray(new Ingredient[ingredients.size()]);
        }

        @Override
        public Registry<Rite> getRegistry() {
            return MMRegistries.RITES;
        }
    }

}
