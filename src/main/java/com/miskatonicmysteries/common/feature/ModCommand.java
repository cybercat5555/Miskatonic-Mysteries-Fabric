package com.miskatonicmysteries.common.feature;

import com.miskatonicmysteries.common.feature.sanity.ISanity;
import com.miskatonicmysteries.common.feature.sanity.InsanityEvent;
import com.miskatonicmysteries.lib.Constants;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.command.arguments.ItemStackArgument;
import net.minecraft.server.command.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModCommand {
    public static void setup() {
        LiteralArgumentBuilder builder = CommandManager.literal("miskmyst").requires(source -> source.hasPermissionLevel(2));
        builder.then(CommandManager.literal("stats")
                .executes(context -> giveStatFeedback(context, context.getSource().getPlayer()))
                .then(CommandManager.argument("player", EntityArgumentType.player()).executes(context -> giveStatFeedback(context, EntityArgumentType.getPlayer(context, "player")))));
        builder.then(CommandManager.literal("sanity")
                .then(CommandManager.literal("get").executes(context -> giveSanityFeedback(context, context.getSource().getPlayer()))
                        .then(CommandManager.argument("player", EntityArgumentType.players()).executes(context -> giveSanityFeedback(context, EntityArgumentType.getPlayers(context, "player").toArray(new ServerPlayerEntity[EntityArgumentType.getPlayers(context, "player").size()])))))

                .then(CommandManager.literal("getMax").executes(context -> giveMaxSanityFeedback(context, context.getSource().getPlayer()))
                        .then(CommandManager.argument("player", EntityArgumentType.players()).executes(context -> giveMaxSanityFeedback(context, EntityArgumentType.getPlayers(context, "player").toArray(new ServerPlayerEntity[EntityArgumentType.getPlayers(context, "player").size()])))))

                .then(CommandManager.literal("set").then(CommandManager.argument("value", IntegerArgumentType.integer(0, Constants.DataTrackers.SANITY_CAP)).executes(context -> setSanity(context, IntegerArgumentType.getInteger(context, "value"), context.getSource().getPlayer()))
                        .then(CommandManager.argument("player", EntityArgumentType.players()).executes(context -> setSanity(context, IntegerArgumentType.getInteger(context, "value"), EntityArgumentType.getPlayers(context, "player").toArray(new ServerPlayerEntity[EntityArgumentType.getPlayers(context, "player").size()]))))))

                .then(CommandManager.literal("playInsanityEvent").then(CommandManager.argument("id", InsanityEventArgumentType.insanityEvent()).executes(context -> playInsanityEvent(context, InsanityEventArgumentType.getInsanityEvent(context, "id"), context.getSource().getPlayer()))
                        .then(CommandManager.argument("player", EntityArgumentType.players()).executes(context -> playInsanityEvent(context, InsanityEventArgumentType.getInsanityEvent(context, "id"), EntityArgumentType.getPlayers(context, "player").toArray(new ServerPlayerEntity[EntityArgumentType.getPlayers(context, "player").size()]))))))

                .then(CommandManager.literal("sanityExpansion")
                        .then(CommandManager.literal("add")
                                .then(CommandManager.argument("name", StringArgumentType.string()).then(CommandManager.argument("value", IntegerArgumentType.integer()).executes(context -> addSanityExpansion(context, StringArgumentType.getString(context, "name"), IntegerArgumentType.getInteger(context, "value"), context.getSource().getPlayer()))
                                        .then(CommandManager.argument("player", EntityArgumentType.players()).executes(context -> addSanityExpansion(context, StringArgumentType.getString(context, "name"), IntegerArgumentType.getInteger(context, "value"), EntityArgumentType.getPlayers(context, "player").toArray(new ServerPlayerEntity[EntityArgumentType.getPlayers(context, "player").size()])))))))
                        .then(CommandManager.literal("remove")
                                .then(CommandManager.argument("name", StringArgumentType.string()).executes(context -> removeSanityExpansion(context, StringArgumentType.getString(context, "name"), context.getSource().getPlayer()))
                                        .then(CommandManager.argument("player", EntityArgumentType.players()).executes(context -> removeSanityExpansion(context, StringArgumentType.getString(context, "name"), EntityArgumentType.getPlayers(context, "player").toArray(new ServerPlayerEntity[EntityArgumentType.getPlayers(context, "player").size()]))))))
                        .then(CommandManager.literal("clear")
                                .executes(context -> clearSanityExpansions(context, context.getSource().getPlayer()))
                                .then(CommandManager.argument("player", EntityArgumentType.players()).executes(context -> clearSanityExpansions(context, EntityArgumentType.getPlayers(context, "player").toArray(new ServerPlayerEntity[EntityArgumentType.getPlayers(context, "player").size()])))))));

        //       .then(CommandManager.literal("mutations"))); for later
        //builder.then(CommandManager.literal("world").executes()); for world data once it's in

        CommandRegistrationCallback.EVENT.register((displatcher, b) -> displatcher.register(builder));
    }

    private static int playInsanityEvent(CommandContext<ServerCommandSource> context, Identifier id, ServerPlayerEntity... players) {
        InsanityEvent event = InsanityEvent.INSANITY_EVENTS.get(id);
        if (event != null) {
            for (ServerPlayerEntity player : players) {
                context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.execute_insanity_event", id.toString()), false);
                event.execute(player, (ISanity) player);
            }
            return 10;
        }else context.getSource().sendError(new TranslatableText("miskatonicmysteries.command.execute_insanity_event.failure", id.toString()));
        return 0;
    }

    private static int clearSanityExpansions(CommandContext<ServerCommandSource> context, ServerPlayerEntity... players) {
        for (ServerPlayerEntity player : players) {
            if (((ISanity) player).getSanityCapExpansions().isEmpty())
                context.getSource().sendError(new TranslatableText("miskatonicmysteries.command.clear_expansions.none_failure", player.getDisplayName()));
            else
                ((ISanity) player).getSanityCapExpansions().forEach((s, i) -> removeSanityExpansion(context, s, player));
        }
        return 0;
    }

    private static int addSanityExpansion(CommandContext<ServerCommandSource> context, String name, int value, ServerPlayerEntity... players) {
        for (ServerPlayerEntity player : players) {
            boolean contains = ((ISanity) player).getSanityCapExpansions().keySet().contains(name);
            if (contains) {
                context.getSource().sendError(new TranslatableText("miskatonicmysteries.command.add_sanity_expansion.contains_failure", name, player.getDisplayName()));
            } else {
                ((ISanity) player).addSanityCapExpansion(name, value);
                context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.add_sanity_expansion", name, value, player.getDisplayName()), true);
            }
        }
        return 0;
    }

    private static int removeSanityExpansion(CommandContext<ServerCommandSource> context, String name, ServerPlayerEntity... players) {
        for (ServerPlayerEntity player : players) {
            boolean contains = ((ISanity) player).getSanityCapExpansions().containsKey(name);
            if (!contains) {
                context.getSource().sendError(new TranslatableText("miskatonicmysteries.command.remove_sanity_expansion.failure", name, player.getDisplayName()));
            } else {
                ((ISanity) player).removeSanityCapExpansion(name);
                context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.remove_sanity_expansion", name, player.getDisplayName()), true);
            }
        }
        return 0;
    }

    private static int giveMaxSanityFeedback(CommandContext<ServerCommandSource> context, ServerPlayerEntity... players) throws CommandSyntaxException {
        int returnValue = Constants.DataTrackers.SANITY_CAP;
        for (ServerPlayerEntity player : players) {
            int value = ((ISanity) player).getMaxSanity();
            MutableText expansionText = ((ISanity) player).getSanityCapExpansions().isEmpty() ? new TranslatableText("miskatonicmysteries.command.get_max_sanity.no_expansions") : new TranslatableText("miskatonicmysteries.command.get_max_sanity.expansions");
            ((ISanity) player).getSanityCapExpansions().forEach((s, i) -> {
                expansionText.append("\n");
                expansionText.append(new LiteralText(s + ": ").append(new LiteralText(Integer.toString(i)).formatted(i <= 0 ? Formatting.RED : Formatting.GREEN)));
            });
            HoverEvent hoverInfo = new HoverEvent(HoverEvent.Action.SHOW_TEXT, expansionText);
            if (player.equals(context.getSource().getPlayer())) {
                context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.get_max_sanity.self", value).setStyle(Style.EMPTY.setHoverEvent(hoverInfo)), false);
            } else
                context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.get_max_sanity", player.getDisplayName(), value).setStyle(Style.EMPTY.setHoverEvent(hoverInfo)), false);
            if (value < returnValue) returnValue = value;
        }
        return Math.round(15 * (returnValue / (float) Constants.DataTrackers.SANITY_CAP));
    }

    private static int setSanity(CommandContext<ServerCommandSource> context, int value, ServerPlayerEntity... players) throws CommandSyntaxException {
        for (ServerPlayerEntity player : players) {
            ((ISanity) player).setSanity(value);
            if (player.equals(context.getSource().getPlayer())) {
                context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.set_sanity.self", value), true);
            } else
                context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.set_sanity", player.getDisplayName(), value), true);
        }
        return Math.round(15 * (value / (float) Constants.DataTrackers.SANITY_CAP));
    }

    private static int giveStatFeedback(CommandContext<ServerCommandSource> context, ServerPlayerEntity player) throws CommandSyntaxException {
        context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.stats", player.getDisplayName()).setStyle(Style.EMPTY.withBold(true)), false);
        giveSanityFeedback(context, player);
        giveMaxSanityFeedback(context, player);

        return 0;
    }

    private static int giveSanityFeedback(CommandContext<ServerCommandSource> context, ServerPlayerEntity... players) throws CommandSyntaxException {
        int returnValue = 0;
        for (ServerPlayerEntity player : players) {
            int value = ((ISanity) player).getSanity();
            if (player.equals(context.getSource().getPlayer())) {
                context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.get_sanity.self", value), false);
            } else
                context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.get_sanity", player.getDisplayName(), value), false);
            if (value > returnValue) returnValue = value;
        }
        return Math.round(15 * (returnValue / (float) Constants.DataTrackers.SANITY_CAP));
    }

    public static class InsanityEventArgumentType implements ArgumentType<Identifier>{
        public static InsanityEventArgumentType insanityEvent(){
            return new InsanityEventArgumentType();
        }

        public static <S> Identifier getInsanityEvent(CommandContext<S> context, String name) {
            return context.getArgument(name, Identifier.class);
        }

        @Override
        public Identifier parse(StringReader reader) throws CommandSyntaxException{
            return Identifier.fromCommandInput(reader);
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            InsanityEvent.INSANITY_EVENTS.keySet().forEach(id -> {
                if (id.toString().startsWith(builder.getRemaining().toLowerCase())) builder.suggest(id.toString());
            });
            return builder.buildFuture();
        }
    }
}
