package com.miskatonicmysteries.common.feature;

import com.miskatonicmysteries.api.interfaces.*;
import com.miskatonicmysteries.api.registry.*;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.feature.world.MMDimensionalWorldState;
import com.miskatonicmysteries.common.feature.world.MMWorldState;
import com.miskatonicmysteries.common.handler.ProtagonistHandler;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.ModifyBlessingPacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.VisionPacket;
import com.miskatonicmysteries.common.registry.MMRegistries;
import com.miskatonicmysteries.common.util.Constants;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings({"RAW_USE", "rawtypes"})
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
                                        .then(CommandManager.argument("player", EntityArgumentType.players()).executes(context -> addSanityExpansion(context, StringArgumentType.getString(context, "name"), IntegerArgumentType.getInteger(context, "value"), EntityArgumentType.getPlayers(context, "player").toArray(new ServerPlayerEntity[0])))))))
                        .then(CommandManager.literal("remove")
                                .then(CommandManager.argument("name", StringArgumentType.string()).executes(context -> removeSanityExpansion(context, StringArgumentType.getString(context, "name"), context.getSource().getPlayer()))
                                        .then(CommandManager.argument("player", EntityArgumentType.players()).executes(context -> removeSanityExpansion(context, StringArgumentType.getString(context, "name"), EntityArgumentType.getPlayers(context, "player").toArray(new ServerPlayerEntity[EntityArgumentType.getPlayers(context, "player").size()]))))))
                        .then(CommandManager.literal("clear")
                                .executes(context -> clearSanityExpansions(context, context.getSource().getPlayer()))
                                .then(CommandManager.argument("player", EntityArgumentType.players()).executes(context -> clearSanityExpansions(context, EntityArgumentType.getPlayers(context, "player").toArray(new ServerPlayerEntity[EntityArgumentType.getPlayers(context, "player").size()])))))));

        LiteralArgumentBuilder blessingBuilder = CommandManager.literal("blessings");
        LiteralArgumentBuilder addBlessingBuilder = CommandManager.literal("addBlessing");
        LiteralArgumentBuilder removeBlessingBuilder = CommandManager.literal("removeBlessing");
        for (Identifier blessingId : MMRegistries.BLESSINGS.getIds()) {
            addBlessingBuilder.then(CommandManager.literal(blessingId.toString())
                    .executes(context -> addBlessing(context, blessingId, context.getSource().getPlayer()))
                    .then(CommandManager.argument("player", EntityArgumentType.player())
                            .executes(context -> addBlessing(context, blessingId, EntityArgumentType.getPlayer(context, "player")))));
            removeBlessingBuilder.then(CommandManager.literal(blessingId.toString())
                    .executes(context -> removeBlessing(context, blessingId, context.getSource().getPlayer()))
                    .then(CommandManager.argument("player", EntityArgumentType.player())
                            .executes(context -> removeBlessing(context, blessingId, EntityArgumentType.getPlayer(context, "player")))));
        }
        blessingBuilder.then(addBlessingBuilder);
        blessingBuilder.then(removeBlessingBuilder);
        blessingBuilder.then(CommandManager.literal("get")
                .executes(context -> giveBlessingFeedback(context, context.getSource().getPlayer()))
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .executes(context -> giveBlessingFeedback(context, EntityArgumentType.getPlayer(context, "player")))));
        builder.then(blessingBuilder);

        builder.then(CommandManager.literal("world")
                .then(CommandManager.literal("getNBT").executes(context -> giveWorldNBT(context)))
                .then(CommandManager.literal("clear").executes(context -> clearWorldNBT(context))));

        LiteralArgumentBuilder ascensionBuilder = CommandManager.literal("ascension");
        ascensionBuilder.then(CommandManager.literal("get")
                .executes(context -> giveAscensionFeedback(context, context.getSource().getPlayer()))
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .executes(context -> giveAscensionFeedback(context, EntityArgumentType.getPlayer(context, "player")))));

        LiteralArgumentBuilder setAscensionBuilder = CommandManager.literal("set");
        for (Identifier id : MMRegistries.AFFILIATIONS.getIds()) {
            setAscensionBuilder.then(CommandManager.literal(id.toString())
                    .executes(context -> setAscension(context, id, Ascendant.of(context.getSource().getPlayer()).get().getAscensionStage(), context.getSource().getPlayer()))
                    .then(CommandManager.argument("stage", IntegerArgumentType.integer(0, 10)).executes(context -> setAscension(context, id, IntegerArgumentType.getInteger(context, "stage"), context.getSource().getPlayer()))
                            .then(CommandManager.argument("player", EntityArgumentType.player()).executes(context -> setAscension(context, id, IntegerArgumentType.getInteger(context, "stage"), EntityArgumentType.getPlayer(context, "player"))))));
        }
        ascensionBuilder.then(setAscensionBuilder);
        builder.then(ascensionBuilder);
        LiteralArgumentBuilder spellBuilder = CommandManager.literal("spells");
        spellBuilder.then(CommandManager.literal("setMax").then(CommandManager.argument("value", IntegerArgumentType.integer(0, Constants.DataTrackers.SPELL_CAP)).executes(context -> setMaxSpells(context, IntegerArgumentType.getInteger(context, "value"), context.getSource().getPlayer()))
                .then(CommandManager.argument("player", EntityArgumentType.players()).executes(context -> setMaxSpells(context, IntegerArgumentType.getInteger(context, "value"), EntityArgumentType.getPlayers(context, "player").toArray(new ServerPlayerEntity[EntityArgumentType.getPlayers(context, "player").size()]))))));
        spellBuilder.then(CommandManager.literal("setPowerPool").then(CommandManager.argument("value", IntegerArgumentType.integer(0, Constants.DataTrackers.SPELL_CAP * 3)).executes(context -> setPowerPool(context, IntegerArgumentType.getInteger(context, "value"), context.getSource().getPlayer()))
                .then(CommandManager.argument("player", EntityArgumentType.players()).executes(context -> setPowerPool(context, IntegerArgumentType.getInteger(context, "value"), EntityArgumentType.getPlayers(context, "player").toArray(new ServerPlayerEntity[EntityArgumentType.getPlayers(context, "player").size()]))))));

        LiteralArgumentBuilder learnSpellBuilder = CommandManager.literal("learnEffect");
        for (Identifier effectId : MMRegistries.SPELL_EFFECTS.getIds()) {
            learnSpellBuilder.then(CommandManager.literal(effectId.toString())
                    .executes(context -> learnSpellEffect(context, effectId, context.getSource().getPlayer()))
                    .then(CommandManager.argument("player", EntityArgumentType.player())
                            .executes(context -> learnSpellEffect(context, effectId, EntityArgumentType.getPlayer(context, "player")))));
        }
        spellBuilder.then(learnSpellBuilder);

        LiteralArgumentBuilder removeSpellBuilder = CommandManager.literal("removeEffect");
        for (Identifier effectId : MMRegistries.SPELL_EFFECTS.getIds()) {
            removeSpellBuilder.then(CommandManager.literal(effectId.toString())
                    .executes(context -> removeSpellEffect(context, effectId, context.getSource().getPlayer()))
                    .then(CommandManager.argument("player", EntityArgumentType.player())
                            .executes(context -> removeSpellEffect(context, effectId, EntityArgumentType.getPlayer(context, "player")))));
        }
        spellBuilder.then(removeSpellBuilder);

        LiteralArgumentBuilder learnMedium = CommandManager.literal("learnMedium");
        for (Identifier mediumId : MMRegistries.SPELL_MEDIUMS.getIds()) {
            learnMedium.then(CommandManager.literal(mediumId.toString())
                    .executes(context -> learnMedium(context, mediumId, context.getSource().getPlayer()))
                    .then(CommandManager.argument("player", EntityArgumentType.player())
                            .executes(context -> learnMedium(context, mediumId, EntityArgumentType.getPlayer(context, "player")))));
        }
        spellBuilder.then(learnMedium);

        LiteralArgumentBuilder removeMedium = CommandManager.literal("removeMedium");
        for (Identifier mediumId : MMRegistries.SPELL_MEDIUMS.getIds()) {
            removeMedium.then(CommandManager.literal(mediumId.toString())
                    .executes(context -> removeMedium(context, mediumId, context.getSource().getPlayer()))
                    .then(CommandManager.argument("player", EntityArgumentType.player())
                            .executes(context -> removeMedium(context, mediumId, EntityArgumentType.getPlayer(context, "player")))));
        }
        spellBuilder.then(removeMedium);
        builder.then(spellBuilder);

        builder.then(CommandManager.literal("summonInvestigator")
                .executes(context -> spawnProtagonist(context, context.getSource().getPlayer()))
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .executes(context -> spawnProtagonist(context, EntityArgumentType.getPlayer(context, "player")))));

        LiteralArgumentBuilder castingBuilder = CommandManager.literal("cast");
        for (Identifier mediumId : MMRegistries.SPELL_MEDIUMS.getIds()) {
            LiteralArgumentBuilder mediumBuilder = CommandManager.literal(mediumId.toString());
            for (Identifier effectId : MMRegistries.SPELL_EFFECTS.getIds()) {
                mediumBuilder.then(CommandManager.literal(effectId.toString())
                        .executes(context -> castSpell(mediumId, effectId, 0, context.getSource().getPlayer()))
                        .then(CommandManager.argument("intensity", IntegerArgumentType.integer(0))
                                .executes(context -> castSpell(mediumId, effectId, IntegerArgumentType.getInteger(context, "intensity"), context.getSource().getPlayer()))
                                .then(CommandManager.argument("сaster", EntityArgumentType.player())
                                        .executes(context -> castSpell(mediumId, effectId, IntegerArgumentType.getInteger(context, "intensity"), EntityArgumentType.getPlayer(context, "сaster"))))));
            }
            castingBuilder.then(mediumBuilder);
        }
        builder.then(castingBuilder);

        LiteralArgumentBuilder visionBuilder = CommandManager.literal("playVision");
        visionBuilder.then(CommandManager.argument("id", IdentifierArgumentType.identifier())
                .executes(context -> displayVision(context, IdentifierArgumentType.getIdentifier(context, "id"), context.getSource().getPlayer()))
                .then(CommandManager.argument("players", EntityArgumentType.players())
                        .executes(context -> displayVision(context, IdentifierArgumentType.getIdentifier(context, "id"), EntityArgumentType.getPlayers(context, "players").toArray(new ServerPlayerEntity[0])))));
        builder.then(visionBuilder);

        CommandRegistrationCallback.EVENT.register((displatcher, b) -> displatcher.register(builder));
    }

    private static int displayVision(CommandContext<ServerCommandSource> context, Identifier visionId, ServerPlayerEntity... players) {
        for (ServerPlayerEntity player : players) {
            VisionPacket.send(player, visionId);
            context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.display_vision", visionId.toString(), player.getDisplayName()), true);
        }
        return players.length > 0 ? 15 : 0;
    }

    private static int castSpell(Identifier mediumId, Identifier effectId, int intensity, LivingEntity caster) {
        SpellMedium medium = MMRegistries.SPELL_MEDIUMS.get(mediumId);
        SpellEffect effect = MMRegistries.SPELL_EFFECTS.get(effectId);
        Spell spell = new Spell(medium, effect, intensity);
        return spell.cast(caster) ? 15 : 0;
    }

    private static int spawnProtagonist(CommandContext<ServerCommandSource> context, PlayerEntity player) {
        boolean spawned = ProtagonistHandler.spawnProtagonist(context.getSource().getWorld(), player);
        if (spawned) {
            context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.summon_investigator", player.getDisplayName()), true);
        } else {
            context.getSource().sendError(new TranslatableText("miskatonicmysteries.command.summon_investigator.failure"));
        }
        return spawned ? 15 : 0;
    }

    private static int giveWorldNBT(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(NbtHelper.toPrettyPrintedText(MMDimensionalWorldState.get(context.getSource().getWorld()).writeNbt(new NbtCompound())), false);
        return 0;
    }

    private static int clearWorldNBT(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(MMWorldState.get(context.getSource().getWorld()).clear(), false);
        return 0;
    }

    private static int playInsanityEvent(CommandContext<ServerCommandSource> context, Identifier id, ServerPlayerEntity... players) {
        InsanityEvent event = MMRegistries.INSANITY_EVENTS.get(id);
        if (event != null) {
            for (ServerPlayerEntity player : players) {
                context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.execute_insanity_event", id.toString()), false);
                Sanity.of(player).ifPresent(sanity -> event.execute(player, sanity));
            }
            return 10;
        } else
            context.getSource().sendError(new TranslatableText("miskatonicmysteries.command.execute_insanity_event.failure", id.toString()));
        return 0;
    }

    private static int clearSanityExpansions(CommandContext<ServerCommandSource> context, ServerPlayerEntity... players) {
        for (ServerPlayerEntity player : players) {
            Sanity.of(player).ifPresent(sanity -> {
                if (sanity.getSanityCapExpansions().isEmpty()) {
                    context.getSource().sendError(new TranslatableText("miskatonicmysteries.command.clear_expansions.none_failure", player.getDisplayName()));
                } else {
                    sanity.getSanityCapExpansions().forEach((s, i) -> removeSanityExpansion(context, s, player));
                }
            });
        }
        return 0;
    }

    private static int addSanityExpansion(CommandContext<ServerCommandSource> context, String name, int value, ServerPlayerEntity... players) {
        for (ServerPlayerEntity player : players) {
            Sanity.of(player).ifPresent(sanity -> {
                boolean contains = sanity.getSanityCapExpansions().containsKey(name);
                if (contains) {
                    context.getSource().sendError(new TranslatableText("miskatonicmysteries.command.add_sanity_expansion.contains_failure", name, player.getDisplayName()));
                } else {
                    ((Sanity) player).addSanityCapExpansion(name, value);
                    context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.add_sanity_expansion", name, value, player.getDisplayName()), true);
                }
            });

        }
        return 0;
    }

    private static int removeSanityExpansion(CommandContext<ServerCommandSource> context, String name, ServerPlayerEntity... players) {
        for (ServerPlayerEntity player : players) {
            Sanity.of(player).ifPresent(sanity -> {
                boolean contains = sanity.getSanityCapExpansions().containsKey(name);
                if (!contains) {
                    context.getSource().sendError(new TranslatableText("miskatonicmysteries.command.remove_sanity_expansion.failure", name, player.getDisplayName()));
                } else {
                    sanity.removeSanityCapExpansion(name);
                    context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.remove_sanity_expansion", name, player.getDisplayName()), true);
                }
            });

        }
        return 0;
    }

    private static int giveMaxSanityFeedback(CommandContext<ServerCommandSource> context, ServerPlayerEntity... players) throws CommandSyntaxException {
        int returnValue = Constants.DataTrackers.SANITY_CAP;
        for (ServerPlayerEntity player : players) {
            if (Sanity.of(player).isPresent()) {
                Sanity sanity = Sanity.of(player).get();
                int value = sanity.getMaxSanity();
                MutableText expansionText = sanity.getSanityCapExpansions().isEmpty() ? new TranslatableText("miskatonicmysteries.command.get_max_sanity.no_expansions") : new TranslatableText("miskatonicmysteries.command.get_max_sanity.expansions");
                sanity.getSanityCapExpansions().forEach((s, i) -> {
                    expansionText.append("\n");
                    expansionText.append(new LiteralText(s + ": ").append(new LiteralText(Integer.toString(i)).formatted(i <= 0 ? Formatting.RED : Formatting.GREEN)));
                });
                HoverEvent hoverInfo = new HoverEvent(HoverEvent.Action.SHOW_TEXT, expansionText);
                if (player.equals(context.getSource().getPlayer())) {
                    context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.get_max_sanity.self", value).setStyle(Style.EMPTY.withHoverEvent(hoverInfo)), false);
                } else {
                    context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.get_max_sanity", player.getDisplayName(), value).setStyle(Style.EMPTY.withHoverEvent(hoverInfo)), false);
                }
                if (value < returnValue) {
                    returnValue = value;
                }
            }

        }
        return Math.round(15 * (returnValue / (float) Constants.DataTrackers.SANITY_CAP));
    }

    private static int setSanity(CommandContext<ServerCommandSource> context, int value, ServerPlayerEntity... players) throws CommandSyntaxException {
        for (ServerPlayerEntity player : players) {
            if (Sanity.of(player).isPresent()) {
                Sanity sanity = Sanity.of(player).get();
                sanity.setSanity(value, true);
                if (player.equals(context.getSource().getPlayer())) {
                    context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.set_sanity.self", value), true);
                } else {
                    context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.set_sanity", player.getDisplayName(), value), true);
                }
            }
        }
        return Math.round(15 * (value / (float) Constants.DataTrackers.SANITY_CAP));
    }

    private static int setMaxSpells(CommandContext<ServerCommandSource> context, int value, ServerPlayerEntity... players) throws CommandSyntaxException {
        for (ServerPlayerEntity player : players) {
            if (SpellCaster.of(player).isPresent()) {
                SpellCaster caster = SpellCaster.of(player).get();
                caster.setMaxSpells(value);
                if (player.equals(context.getSource().getPlayer())) {
                    context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.set_max_spells.self", value), true);
                } else {
                    context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.set_max_spells", player.getDisplayName(), value), true);
                }
            }
        }
        return value;
    }

    private static int setPowerPool(CommandContext<ServerCommandSource> context, int value, ServerPlayerEntity... players) throws CommandSyntaxException {
        for (ServerPlayerEntity player : players) {
            if (SpellCaster.of(player).isPresent()) {
                SpellCaster caster = SpellCaster.of(player).get();
                caster.setPowerPool(value);
                if (player.equals(context.getSource().getPlayer())) {
                    context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.set_power_pool.self", value), true);
                } else {
                    context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.set_power_pool", player.getDisplayName(), value), true);
                }
            }
        }
        return value;
    }

    private static int learnSpellEffect(CommandContext<ServerCommandSource> context, Identifier effect, ServerPlayerEntity... players) throws CommandSyntaxException {
        for (ServerPlayerEntity player : players) {
            if (SpellCaster.of(player).isPresent()) {
                SpellCaster caster = SpellCaster.of(player).get();
                caster.learnEffect(MMRegistries.SPELL_EFFECTS.get(effect));
                caster.syncSpellData();
                if (player.equals(context.getSource().getPlayer())) {
                    context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.learn_spell.self", effect), true);
                } else {
                    context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.learn_spell", player.getDisplayName(), effect), true);
                }
            }
        }
        return 0;
    }

    private static int removeSpellEffect(CommandContext<ServerCommandSource> context, Identifier effect, ServerPlayerEntity... players) throws CommandSyntaxException {
        for (ServerPlayerEntity player : players) {
            if (SpellCaster.of(player).isPresent()) {
                SpellCaster caster = SpellCaster.of(player).get();
                caster.getLearnedEffects().remove(MMRegistries.SPELL_EFFECTS.get(effect));
                caster.syncSpellData();
                if (player.equals(context.getSource().getPlayer())) {
                    context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.removed_spell.self", effect), true);
                } else {
                    context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.removed_spell", player.getDisplayName(), effect), true);
                }
            }
        }
        return 0;
    }

    private static int learnMedium(CommandContext<ServerCommandSource> context, Identifier medium, ServerPlayerEntity... players) throws CommandSyntaxException {
        for (ServerPlayerEntity player : players) {
            if (SpellCaster.of(player).isPresent()) {
                SpellCaster caster = SpellCaster.of(player).get();
                caster.learnMedium(MMRegistries.SPELL_MEDIUMS.get(medium));
                caster.syncSpellData();
                if (player.equals(context.getSource().getPlayer())) {
                    context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.learn_medium.self", medium), true);
                } else {
                    context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.learn_medium", player.getDisplayName(), medium), true);
                }
            }
        }
        return 0;
    }

    private static int removeMedium(CommandContext<ServerCommandSource> context, Identifier medium, ServerPlayerEntity... players) throws CommandSyntaxException {
        for (ServerPlayerEntity player : players) {
            if (SpellCaster.of(player).isPresent()) {
                SpellCaster caster = SpellCaster.of(player).get();
                caster.getLearnedMediums().remove(MMRegistries.SPELL_MEDIUMS.get(medium));
                caster.syncSpellData();
                if (player.equals(context.getSource().getPlayer())) {
                    context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.removed_medium.self", medium), true);
                } else {
                    context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.removed_medium", player.getDisplayName(), medium), true);
                }
            }
        }
        return 0;
    }

    private static int addBlessing(CommandContext<ServerCommandSource> context, Identifier blessingId, ServerPlayerEntity... players) throws CommandSyntaxException {
        for (ServerPlayerEntity player : players) {
            if (Ascendant.of(player).isPresent()) {
                Ascendant ascendant = Ascendant.of(player).get();
                if (ascendant.getBlessings().size() < Constants.DataTrackers.MAX_BLESSINGS) {
                    Blessing blessing = MMRegistries.BLESSINGS.get(blessingId);
                    ascendant.addBlessing(blessing);
                    ModifyBlessingPacket.send(player, blessing, true);
                    ascendant.syncBlessingData();
                    if (player.equals(context.getSource().getPlayer())) {
                        context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.add_blessing.self", new TranslatableText(blessing.getTranslationString())), true);
                    } else {
                        context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.add_blessing", player.getDisplayName(), new TranslatableText(blessing.getTranslationString())), true);
                    }
                } else {
                    context.getSource().sendError(new TranslatableText("miskatonicmysteries.command.add_blessing.failure", player.getDisplayName()));
                }
            }
        }
        return 0;
    }

    private static int removeBlessing(CommandContext<ServerCommandSource> context, Identifier blessingId, ServerPlayerEntity... players) throws CommandSyntaxException {
        for (ServerPlayerEntity player : players) {
            if (Ascendant.of(player).isPresent()) {
                Ascendant ascendant = Ascendant.of(player).get();
                Blessing blessing = MMRegistries.BLESSINGS.get(blessingId);
                ascendant.removeBlessing(blessing);
                ModifyBlessingPacket.send(player, blessing, false);
                ascendant.syncBlessingData();
                if (player.equals(context.getSource().getPlayer())) {
                    context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.remove_blessing.self", new TranslatableText(blessing.getTranslationString())), true);
                } else {
                    context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.remove_blessing", player.getDisplayName(), new TranslatableText(blessing.getTranslationString())), true);
                }
            }
        }
        return 0;
    }

    private static int giveBlessingFeedback(CommandContext<ServerCommandSource> context, ServerPlayerEntity... players) throws CommandSyntaxException {
        for (ServerPlayerEntity player : players) {
            if (Ascendant.of(player).isPresent()) {
                Ascendant ascendant = Ascendant.of(player).get();
                int value = ascendant.getBlessings().size();
                MutableText blessingText = new TranslatableText("miskatonicmysteries.command.get_blessings.blessings");
                ascendant.getBlessings().forEach(blessing -> {
                    blessingText.append("\n");
                    blessingText.append(new LiteralText("-").append(new TranslatableText(blessing.getTranslationString())));
                });
                HoverEvent hoverInfo = new HoverEvent(HoverEvent.Action.SHOW_TEXT, blessingText);
                if (player.equals(context.getSource().getPlayer())) {
                    context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.get_blessings.self", value).setStyle(Style.EMPTY.withHoverEvent(hoverInfo)), false);
                } else {
                    context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.get_blessings", player.getDisplayName(), value).setStyle(Style.EMPTY.withHoverEvent(hoverInfo)), false);
                }
            }

        }
        return 0;
    }

    private static int giveAscensionFeedback(CommandContext<ServerCommandSource> context, ServerPlayerEntity... players) throws CommandSyntaxException {
        for (ServerPlayerEntity player : players) {
            if (Ascendant.of(player).isPresent() && Affiliated.of(player).isPresent()) {
                Affiliation affiliation = Affiliated.of(player).get().getAffiliation(false);
                Affiliation apparentAffiliation = Affiliated.of(player).get().getAffiliation(true);
                int stage = Ascendant.of(player).get().getAscensionStage();
                if (player.equals(context.getSource().getPlayer())) {
                    context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.get_ascension.self"), false);
                } else {
                    context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.get_ascension", player.getDisplayName()), false);
                }
                context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.get_ascension.apparent_path", apparentAffiliation.getId()), false);
                context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.get_ascension.path", affiliation.getId()), false);
                context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.get_ascension.stage", stage), false);
            }

        }
        return 0;
    }

    private static int setAscension(CommandContext<ServerCommandSource> context, Identifier affiliation, int stage, ServerPlayerEntity... players) throws CommandSyntaxException {
        for (ServerPlayerEntity player : players) {
            if (Ascendant.of(player).isPresent() && Affiliated.of(player).isPresent()) {
                MalleableAffiliated.of(player).get().setAffiliation(MMRegistries.AFFILIATIONS.get(affiliation), false);
                Ascendant.of(player).get().setAscensionStage(stage);
                if (player.equals(context.getSource().getPlayer())) {
                    context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.set_ascension.self", affiliation, stage), false);
                } else {
                    context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.set_ascension", player.getDisplayName(), affiliation, stage), false);
                }
            }

        }
        return 0;
    }

    private static int giveStatFeedback(CommandContext<ServerCommandSource> context, ServerPlayerEntity player) throws CommandSyntaxException {
        context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.stats", player.getDisplayName()).setStyle(Style.EMPTY.withBold(true)), false);
        giveSanityFeedback(context, player);
        giveMaxSanityFeedback(context, player);
        giveBlessingFeedback(context, player);
        giveAscensionFeedback(context, player);
        return 0;
    }

    private static int giveSanityFeedback(CommandContext<ServerCommandSource> context, ServerPlayerEntity... players) throws CommandSyntaxException {
        int returnValue = 0;
        for (ServerPlayerEntity player : players) {
            int value = Sanity.of(player).isPresent() ? Sanity.of(player).get().getSanity() : 0;
            if (player.equals(context.getSource().getPlayer())) {
                context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.get_sanity.self", value), false);
            } else
                context.getSource().sendFeedback(new TranslatableText("miskatonicmysteries.command.get_sanity", player.getDisplayName(), value), false);
            if (value > returnValue) returnValue = value;
        }
        return Math.round(15 * (returnValue / (float) Constants.DataTrackers.SANITY_CAP));
    }

    public static class InsanityEventArgumentType implements ArgumentType<Identifier> {
        public static InsanityEventArgumentType insanityEvent() {
            return new InsanityEventArgumentType();
        }

        public static <S> Identifier getInsanityEvent(CommandContext<S> context, String name) {
            return context.getArgument(name, Identifier.class);
        }

        @Override
        public Identifier parse(StringReader reader) throws CommandSyntaxException {
            return Identifier.fromCommandInput(reader);
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            MMRegistries.INSANITY_EVENTS.getIds().forEach(id -> {
                if (id.toString().startsWith(builder.getRemaining().toLowerCase())) builder.suggest(id.toString());
            });
            return builder.buildFuture();
        }
    }
}
