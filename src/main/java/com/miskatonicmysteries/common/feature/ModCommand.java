package com.miskatonicmysteries.common.feature;

import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.interfaces.Ascendant;
import com.miskatonicmysteries.api.interfaces.MalleableAffiliated;
import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.api.interfaces.SpellCaster;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.api.registry.Blessing;
import com.miskatonicmysteries.api.registry.InsanityEvent;
import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.feature.world.MMDimensionalWorldState;
import com.miskatonicmysteries.common.feature.world.MMWorldState;
import com.miskatonicmysteries.common.handler.ProtagonistHandler;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.ModifyBlessingPacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.VisionPacket;
import com.miskatonicmysteries.common.registry.MMRegistries;
import com.miskatonicmysteries.common.util.Constants;


import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

@SuppressWarnings({"RAW_USE", "rawtypes"})
public class ModCommand {

	public static void setup() {
		ArgumentTypeRegistry.registerArgumentType(new Identifier(Constants.MOD_ID, "insanity_event"), ModCommand.InsanityEventArgumentType.class, ConstantArgumentSerializer.of(IdentifierArgumentType::identifier));
		LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal("miskmyst")
			.requires(source -> source.hasPermissionLevel(2));

		LiteralArgumentBuilder<ServerCommandSource> stats = CommandManager.literal("stats");
		stats.executes(context -> giveStatFeedback(context, context.getSource().getPlayer()))
			.then(queryPlayer()
					  .executes(context -> giveStatFeedback(context, EntityArgumentType
						  .getPlayer(context, "player"))));
		builder.then(stats);

		LiteralArgumentBuilder<ServerCommandSource> sanity = CommandManager.literal("sanity");

		sanity.then(CommandManager.literal("get")
						.executes(context -> giveSanityFeedback(context, context.getSource().getPlayer()))
						.then(queryPlayers().executes(context -> giveSanityFeedback(context, collectPlayers(context)))));
		sanity.then(CommandManager.literal("getMax")
						.executes(context -> giveMaxSanityFeedback(context, context.getSource().getPlayer()))
						.then(queryPlayers().executes(context -> giveMaxSanityFeedback(context, collectPlayers(context)))));
		sanity.then(CommandManager.literal("set").then(
			CommandManager.argument("value", IntegerArgumentType.integer(0, Constants.DataTrackers.SANITY_CAP)).executes(
				context -> setSanity(context, IntegerArgumentType.getInteger(context, "value"), context.getSource().getPlayer()))
				.then(queryPlayers()
						  .executes(context -> setSanity(context, IntegerArgumentType.getInteger(context, "value"), collectPlayers(context))))));
		sanity.then(CommandManager.literal("playInsanityEvent").then(
			CommandManager.argument("id", InsanityEventArgumentType.insanityEvent())
				.executes(context -> playInsanityEvent(context, InsanityEventArgumentType.getInsanityEvent(context, "id"),
													   context.getSource().getPlayer()))
				.then(queryPlayers().executes(context -> playInsanityEvent(context, InsanityEventArgumentType.getInsanityEvent(context, "id"),
																		   collectPlayers(context))))));
		LiteralArgumentBuilder<ServerCommandSource> sanityExpansion = CommandManager.literal("sanityExpansion");
		sanityExpansion.then(CommandManager.literal("add").then(
			CommandManager.argument("name", StringArgumentType.string())
				.then(CommandManager.argument("value", IntegerArgumentType.integer())
						  .executes(context -> addSanityExpansion(context, StringArgumentType.getString(context, "name"),
																  IntegerArgumentType.getInteger(context, "value"),
																  context.getSource().getPlayer()))
						  .then(queryPlayers().executes(context -> addSanityExpansion(context, StringArgumentType.getString(context, "name"),
																					  IntegerArgumentType.getInteger(context, "value"),
																					  collectPlayers(context)))))));
		sanityExpansion.then(CommandManager.literal("remove").then(
			CommandManager.argument("name", StringArgumentType.string())
				.executes(context -> removeSanityExpansion(
					context, StringArgumentType.getString(context, "name"),
					context.getSource().getPlayer()))
				.then(queryPlayers().executes(context -> removeSanityExpansion(
					context, StringArgumentType.getString(context, "name"),
					collectPlayers(context))))));
		sanityExpansion.then(CommandManager.literal("clear").executes(context -> clearSanityExpansions(context, context.getSource().getPlayer()))
								 .then(queryPlayers().executes(context -> clearSanityExpansions(context, collectPlayers(context)))));
		sanity.then(sanityExpansion);
		builder.then(sanity);

		LiteralArgumentBuilder<ServerCommandSource> blessingBuilder = CommandManager.literal("blessings");
		LiteralArgumentBuilder<ServerCommandSource> addBlessingBuilder = CommandManager.literal("addBlessing");
		LiteralArgumentBuilder<ServerCommandSource> removeBlessingBuilder = CommandManager.literal("removeBlessing");

		for (Identifier blessingId : MMRegistries.BLESSINGS.getIds()) {
			addBlessingBuilder.then(CommandManager.literal(blessingId.toString())
										.executes(context -> addBlessing(context, blessingId, context.getSource().getPlayer()))
										.then(queryPlayer().executes(context -> addBlessing(context, blessingId, collectPlayer(context)))));
			removeBlessingBuilder.then(CommandManager.literal(blessingId.toString())
										   .executes(context -> removeBlessing(context, blessingId, context.getSource().getPlayer()))
										   .then(queryPlayer().executes(context -> removeBlessing(context, blessingId, collectPlayer(context)))));
		}

		blessingBuilder.then(addBlessingBuilder);
		blessingBuilder.then(removeBlessingBuilder);
		blessingBuilder.then(CommandManager.literal("get").executes(context -> giveBlessingFeedback(context, context.getSource().getPlayer()))
								 .then(queryPlayer().executes(context -> giveBlessingFeedback(context, collectPlayer(context)))));
		builder.then(blessingBuilder);

		builder.then(CommandManager.literal("world")
						 .then(CommandManager.literal("getNBT").executes(ModCommand::giveWorldNBT))
						 .then(CommandManager.literal("clear").executes(ModCommand::clearWorldNBT)));

		LiteralArgumentBuilder<ServerCommandSource> ascensionBuilder = CommandManager.literal("ascension");
		ascensionBuilder.then(CommandManager.literal("get").executes(context -> giveAscensionFeedback(context, context.getSource().getPlayer()))
								  .then(queryPlayer().executes(context -> giveAscensionFeedback(context, collectPlayer(context)))));

		LiteralArgumentBuilder<ServerCommandSource> setAscensionBuilder = CommandManager.literal("set");
		for (Identifier id : MMRegistries.AFFILIATIONS.getIds()) {
			setAscensionBuilder.then(CommandManager.literal(id.toString())
										 .executes(context -> setAscension(context, id,
																		   Ascendant.of(context.getSource().getPlayer()).get().getAscensionStage(),
																		   context.getSource().getPlayer()))
										 .then(CommandManager.argument("stage", IntegerArgumentType.integer(0, 10))
												   .executes(context -> setAscension(context, id,
																					 IntegerArgumentType.getInteger(context, "stage"),
																					 context.getSource().getPlayer()))
												   .then(queryPlayer()
															 .executes(context -> setAscension(context, id,
																							   IntegerArgumentType.getInteger(context, "stage"),
																							   collectPlayer(context))))));
		}
		ascensionBuilder.then(setAscensionBuilder);
		builder.then(ascensionBuilder);
		LiteralArgumentBuilder<ServerCommandSource> spellBuilder = CommandManager.literal("spells");
		spellBuilder.then(CommandManager.literal("setMax").then(
			CommandManager.argument("value", IntegerArgumentType.integer(0, Constants.DataTrackers.SPELL_CAP))
				.executes(context -> setMaxSpells(context, IntegerArgumentType.getInteger(context, "value"), context.getSource().getPlayer()))
				.then(queryPlayers().executes(context -> setMaxSpells(context,
																	  IntegerArgumentType.getInteger(context, "value"),
																	  collectPlayers(context))))));
		spellBuilder.then(CommandManager.literal("setPowerPool").then(
			CommandManager.argument("value", IntegerArgumentType.integer(0, Constants.DataTrackers.SPELL_CAP * 3))
				.executes(context -> setPowerPool(context, IntegerArgumentType.getInteger(context, "value"), context.getSource().getPlayer()))
				.then(queryPlayers().executes(context -> setPowerPool(context,
																	  IntegerArgumentType.getInteger(context, "value"),
																	  collectPlayers(context))))));

		LiteralArgumentBuilder<ServerCommandSource> learnSpellBuilder = CommandManager.literal("learnEffect");
		LiteralArgumentBuilder<ServerCommandSource> removeSpellBuilder = CommandManager.literal("removeEffect");
		for (Identifier effectId : MMRegistries.SPELL_EFFECTS.getIds()) {
			learnSpellBuilder.then(CommandManager.literal(effectId.toString())
									   .executes(context -> learnSpellEffect(context, effectId, context.getSource().getPlayer()))
									   .then(queryPlayer().executes(context -> learnSpellEffect(context, effectId, collectPlayer(context)))));
			removeSpellBuilder.then(CommandManager.literal(effectId.toString()).executes(
				context -> removeSpellEffect(context, effectId, context.getSource().getPlayer()))
										.then(queryPlayer().executes(context -> removeSpellEffect(context, effectId, collectPlayer(context)))));
		}
		spellBuilder.then(learnSpellBuilder);
		spellBuilder.then(removeSpellBuilder);

		LiteralArgumentBuilder<ServerCommandSource> learnMedium = CommandManager.literal("learnMedium");
		LiteralArgumentBuilder<ServerCommandSource> removeMedium = CommandManager.literal("removeMedium");
		LiteralArgumentBuilder<ServerCommandSource> castingBuilder = CommandManager.literal("cast");
		for (Identifier mediumId : MMRegistries.SPELL_MEDIUMS.getIds()) {
			learnMedium.then(CommandManager.literal(mediumId.toString())
								 .executes(context -> learnMedium(context, mediumId, context.getSource().getPlayer()))
								 .then(queryPlayer().executes(context -> learnMedium(context, mediumId, collectPlayer(context)))));
			removeMedium.then(
				CommandManager.literal(mediumId.toString()).executes(context -> removeMedium(context, mediumId, context.getSource().getPlayer()))
					.then(queryPlayer()
							  .executes(context -> removeMedium(context, mediumId, EntityArgumentType
								  .getPlayer(context, "player")))));
			LiteralArgumentBuilder<ServerCommandSource> mediumBuilder = CommandManager.literal(mediumId.toString());
			for (Identifier effectId : MMRegistries.SPELL_EFFECTS.getIds()) {
				mediumBuilder.then(CommandManager.literal(effectId.toString())
									   .executes(context -> castSpell(mediumId, effectId, 0, context.getSource().getPlayer()))
									   .then(CommandManager.argument("intensity", IntegerArgumentType.integer(0))
												 .executes(context -> castSpell(mediumId, effectId,
																				IntegerArgumentType.getInteger(context, "intensity"),
																				context.getSource().getPlayer()))
												 .then(queryPlayer().executes(context -> castSpell(mediumId, effectId,
																								   IntegerArgumentType
																									   .getInteger(context, "intensity"),
																								   collectPlayer(context))))));
			}
			castingBuilder.then(mediumBuilder);
		}
		spellBuilder.then(learnMedium);
		spellBuilder.then(removeMedium);
		builder.then(castingBuilder);
		builder.then(spellBuilder);

		builder.then(CommandManager.literal("summonInvestigator")
						 .executes(context -> spawnProtagonist(context, context.getSource().getPlayer()))
						 .then(queryPlayer()
								   .executes(context -> spawnProtagonist(context, EntityArgumentType
									   .getPlayer(context, "player")))));

		LiteralArgumentBuilder<ServerCommandSource> visionBuilder = CommandManager.literal("playVision");
		visionBuilder.then(CommandManager.argument("id", IdentifierArgumentType.identifier()).executes(
			context -> displayVision(context, IdentifierArgumentType.getIdentifier(context, "id"), context.getSource().getPlayer()))
							   .then(queryPlayers().executes(context -> displayVision(context,
																					  IdentifierArgumentType.getIdentifier(context, "id"),
																					  collectPlayers(context)))));
		builder.then(visionBuilder);

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(builder));
	}

	private static int giveStatFeedback(CommandContext<ServerCommandSource> context, ServerPlayerEntity player)
		throws CommandSyntaxException {
		context.getSource().sendFeedback(
				Text.translatable("miskatonicmysteries.command.stats", player.getDisplayName()).setStyle(Style.EMPTY.withBold(true)), false);
		giveSanityFeedback(context, player);
		giveMaxSanityFeedback(context, player);
		giveBlessingFeedback(context, player);
		giveAscensionFeedback(context, player);
		return 0;
	}

	private static int giveMaxSanityFeedback(CommandContext<ServerCommandSource> context, ServerPlayerEntity player)
		throws CommandSyntaxException {
		return giveMaxSanityFeedback(context, List.of(player));
	}

	private static int giveMaxSanityFeedback(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players)
		throws CommandSyntaxException {
		int returnValue = Constants.DataTrackers.SANITY_CAP;
		for (ServerPlayerEntity player : players) {
			if (Sanity.of(player).isPresent()) {
				Sanity sanity = Sanity.of(player).get();
				int value = sanity.getMaxSanity();
				MutableText expansionText = sanity.getSanityCapExpansions().isEmpty()
											? Text.translatable("miskatonicmysteries.command.get_max_sanity.no_expansions")
											: Text.translatable("miskatonicmysteries.command.get_max_sanity.expansions");
				sanity.getSanityCapExpansions().forEach((s, i) -> {
					expansionText.append("\n");
					expansionText.append(Text.literal(s + ": ").append(Text.literal(Integer.toString(i))
																			  .formatted(i <= 0 ? Formatting.RED : Formatting.GREEN)));
				});
				HoverEvent hoverInfo = new HoverEvent(HoverEvent.Action.SHOW_TEXT, expansionText);
				if (player.equals(context.getSource().getPlayer())) {
					context.getSource().sendFeedback(Text.translatable("miskatonicmysteries.command.get_max_sanity.self", value)
														 .setStyle(Style.EMPTY.withHoverEvent(hoverInfo)), false);
				} else {
					context.getSource().sendFeedback(
							Text.translatable("miskatonicmysteries.command.get_max_sanity", player.getDisplayName(), value)
							.setStyle(Style.EMPTY.withHoverEvent(hoverInfo)), false);
				}
				if (value < returnValue) {
					returnValue = value;
				}
			}

		}
		return Math.round(15 * (returnValue / (float) Constants.DataTrackers.SANITY_CAP));
	}

	private static int giveBlessingFeedback(CommandContext<ServerCommandSource> context, ServerPlayerEntity player)
		throws CommandSyntaxException {
		return giveBlessingFeedback(context, List.of(player));
	}

	private static int giveBlessingFeedback(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players)
		throws CommandSyntaxException {
		for (ServerPlayerEntity player : players) {
			if (Ascendant.of(player).isPresent()) {
				Ascendant ascendant = Ascendant.of(player).get();
				int value = ascendant.getBlessings().size();
				MutableText blessingText = Text.translatable("miskatonicmysteries.command.get_blessings.blessings");
				ascendant.getBlessings().forEach(blessing -> {
					blessingText.append("\n");
					blessingText.append(Text.literal("-").append(Text.translatable(blessing.getTranslationString())));
				});
				HoverEvent hoverInfo = new HoverEvent(HoverEvent.Action.SHOW_TEXT, blessingText);
				if (player.equals(context.getSource().getPlayer())) {
					context.getSource().sendFeedback(Text.translatable("miskatonicmysteries.command.get_blessings.self", value)
														 .setStyle(Style.EMPTY.withHoverEvent(hoverInfo)), false);
				} else {
					context.getSource().sendFeedback(
						Text.translatable("miskatonicmysteries.command.get_blessings", player.getDisplayName(), value)
							.setStyle(Style.EMPTY.withHoverEvent(hoverInfo)), false);
				}
			}

		}
		return 0;
	}


	private static int giveAscensionFeedback(CommandContext<ServerCommandSource> context, ServerPlayerEntity player)
		throws CommandSyntaxException {
		return giveAscensionFeedback(context, List.of(player));
	}

	private static int giveAscensionFeedback(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players)
		throws CommandSyntaxException {
		for (ServerPlayerEntity player : players) {
			if (Ascendant.of(player).isPresent() && Affiliated.of(player).isPresent()) {
				Affiliation affiliation = Affiliated.of(player).get().getAffiliation(false);
				Affiliation apparentAffiliation = Affiliated.of(player).get().getAffiliation(true);
				int stage = Ascendant.of(player).get().getAscensionStage();
				if (player.equals(context.getSource().getPlayer())) {
					context.getSource().sendFeedback(Text.translatable("miskatonicmysteries.command.get_ascension.self"), false);
				} else {
					context.getSource()
						.sendFeedback(Text.translatable("miskatonicmysteries.command.get_ascension", player.getDisplayName()), false);
				}
				context.getSource().sendFeedback(
					Text.translatable("miskatonicmysteries.command.get_ascension.apparent_path", apparentAffiliation.getId()), false);
				context.getSource()
					.sendFeedback(Text.translatable("miskatonicmysteries.command.get_ascension.path", affiliation.getId()), false);
				context.getSource().sendFeedback(Text.translatable("miskatonicmysteries.command.get_ascension.stage", stage), false);
			}

		}
		return 0;
	}

	private static int giveSanityFeedback(CommandContext<ServerCommandSource> context, ServerPlayerEntity player)
		throws CommandSyntaxException {
		return giveSanityFeedback(context, List.of(player));
	}

	private static int giveSanityFeedback(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players)
		throws CommandSyntaxException {
		int returnValue = 0;
		for (ServerPlayerEntity player : players) {
			int value = Sanity.of(player).isPresent() ? Sanity.of(player).get().getSanity() : 0;
			if (player.equals(context.getSource().getPlayer())) {
				context.getSource().sendFeedback(Text.translatable("miskatonicmysteries.command.get_sanity.self", value), false);
			} else {
				context.getSource()
					.sendFeedback(Text.translatable("miskatonicmysteries.command.get_sanity", player.getDisplayName(), value), false);
			}
			if (value > returnValue) {
				returnValue = value;
			}
		}
		return Math.round(15 * (returnValue / (float) Constants.DataTrackers.SANITY_CAP));
	}

	private static RequiredArgumentBuilder<ServerCommandSource, EntitySelector> queryPlayers() {
		return CommandManager.argument("players", EntityArgumentType.players());
	}

	private static Collection<ServerPlayerEntity> collectPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return EntityArgumentType.getPlayers(context, "players");
	}

	private static RequiredArgumentBuilder<ServerCommandSource, EntitySelector> queryPlayer() {
		return CommandManager.argument("player", EntityArgumentType.player());
	}

	private static ServerPlayerEntity collectPlayer(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return EntityArgumentType.getPlayer(context, "player");
	}

	private static int setSanity(CommandContext<ServerCommandSource> context, int value, ServerPlayerEntity player) throws CommandSyntaxException {
		return setSanity(context, value, List.of(player));
	}

	private static int setSanity(CommandContext<ServerCommandSource> context, int value, Collection<ServerPlayerEntity> players)
		throws CommandSyntaxException {
		for (ServerPlayerEntity player : players) {
			if (Sanity.of(player).isPresent()) {
				Sanity sanity = Sanity.of(player).get();
				sanity.setSanity(value, true);
				if (player.equals(context.getSource().getPlayer())) {
					context.getSource().sendFeedback(Text.translatable("miskatonicmysteries.command.set_sanity.self", value), true);
				} else {
					context.getSource()
						.sendFeedback(Text.translatable("miskatonicmysteries.command.set_sanity", player.getDisplayName(), value),
									  true);
				}
			}
		}
		return Math.round(15 * (value / (float) Constants.DataTrackers.SANITY_CAP));
	}

	private static int playInsanityEvent(CommandContext<ServerCommandSource> context, Identifier id, ServerPlayerEntity player) {
		return playInsanityEvent(context, id, List.of(player));
	}

	private static int playInsanityEvent(CommandContext<ServerCommandSource> context, Identifier id, Collection<ServerPlayerEntity> players) {
		InsanityEvent event = MMRegistries.INSANITY_EVENTS.get(id);
		if (event != null) {
			for (ServerPlayerEntity player : players) {
				context.getSource()
					.sendFeedback(Text.translatable("miskatonicmysteries.command.execute_insanity_event", id.toString()), false);
				Sanity.of(player).ifPresent(sanity -> event.execute(player, sanity));
			}
			return 10;
		} else {
			context.getSource()
				.sendError(Text.translatable("miskatonicmysteries.command.execute_insanity_event.failure", id.toString()));
		}
		return 0;
	}

	private static int addSanityExpansion(CommandContext<ServerCommandSource> context, String name, int value, ServerPlayerEntity player) {
		return addSanityExpansion(context, name, value, List.of(player));
	}

	private static int addSanityExpansion(CommandContext<ServerCommandSource> context, String name, int value,
										  Collection<ServerPlayerEntity> players) {
		for (ServerPlayerEntity player : players) {
			Sanity.of(player).ifPresent(sanity -> {
				boolean contains = sanity.getSanityCapExpansions().containsKey(name);
				if (contains) {
					context.getSource().sendError(
						Text.translatable("miskatonicmysteries.command.add_sanity_expansion.contains_failure", name,
											 player.getDisplayName()));
				} else {
					((Sanity) player).addSanityCapExpansion(name, value);
					context.getSource().sendFeedback(
						Text.translatable("miskatonicmysteries.command.add_sanity_expansion", name, value, player.getDisplayName()),
						true);
				}
			});

		}
		return 0;
	}

	private static int removeSanityExpansion(CommandContext<ServerCommandSource> context, String name, ServerPlayerEntity player) {
		return removeSanityExpansion(context, name, List.of(player));
	}

	private static int removeSanityExpansion(CommandContext<ServerCommandSource> context, String name, Collection<ServerPlayerEntity> players) {
		for (ServerPlayerEntity player : players) {
			Sanity.of(player).ifPresent(sanity -> {
				boolean contains = sanity.getSanityCapExpansions().containsKey(name);
				if (!contains) {
					context.getSource().sendError(
						Text.translatable("miskatonicmysteries.command.remove_sanity_expansion.failure", name, player.getDisplayName()));
				} else {
					sanity.removeSanityCapExpansion(name);
					context.getSource().sendFeedback(
						Text.translatable("miskatonicmysteries.command.remove_sanity_expansion", name, player.getDisplayName()), true);
				}
			});

		}
		return 0;
	}

	private static int clearSanityExpansions(CommandContext<ServerCommandSource> context, ServerPlayerEntity player) {
		return clearSanityExpansions(context, List.of(player));
	}

	private static int clearSanityExpansions(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		for (ServerPlayerEntity player : players) {
			Sanity.of(player).ifPresent(sanity -> {
				if (sanity.getSanityCapExpansions().isEmpty()) {
					context.getSource().sendError(
						Text.translatable("miskatonicmysteries.command.clear_expansions.none_failure", player.getDisplayName()));
				} else {
					sanity.getSanityCapExpansions().forEach((s, i) -> removeSanityExpansion(context, s, player));
				}
			});
		}
		return 0;
	}

	private static int addBlessing(CommandContext<ServerCommandSource> context, Identifier blessingId, ServerPlayerEntity player)
		throws CommandSyntaxException {
		return addBlessing(context, blessingId, List.of(player));
	}

	private static int addBlessing(CommandContext<ServerCommandSource> context, Identifier blessingId, Collection<ServerPlayerEntity> players)
		throws CommandSyntaxException {
		for (ServerPlayerEntity player : players) {
			if (Ascendant.of(player).isPresent()) {
				Ascendant ascendant = Ascendant.of(player).get();
				if (ascendant.getBlessings().size() < Constants.DataTrackers.MAX_BLESSINGS) {
					Blessing blessing = MMRegistries.BLESSINGS.get(blessingId);
					ascendant.addBlessing(blessing);
					ModifyBlessingPacket.send(player, blessing, true);
					if (player.equals(context.getSource().getPlayer())) {
						context.getSource().sendFeedback(Text.translatable("miskatonicmysteries.command.add_blessing.self",
																			  Text.translatable(blessing.getTranslationString())), true);
					} else {
						context.getSource().sendFeedback(
							Text.translatable("miskatonicmysteries.command.add_blessing", player.getDisplayName(),
												 Text.translatable(blessing.getTranslationString())), true);
					}
				} else {
					context.getSource()
						.sendError(Text.translatable("miskatonicmysteries.command.add_blessing.failure", player.getDisplayName()));
				}
			}
		}
		return 0;
	}

	private static int removeBlessing(CommandContext<ServerCommandSource> context, Identifier blessingId, ServerPlayerEntity player)
		throws CommandSyntaxException {
		return removeBlessing(context, blessingId, List.of(player));
	}

	private static int removeBlessing(CommandContext<ServerCommandSource> context, Identifier blessingId, Collection<ServerPlayerEntity> players)
		throws CommandSyntaxException {
		for (ServerPlayerEntity player : players) {
			if (Ascendant.of(player).isPresent()) {
				Ascendant ascendant = Ascendant.of(player).get();
				Blessing blessing = MMRegistries.BLESSINGS.get(blessingId);
				ascendant.removeBlessing(blessing);
				ModifyBlessingPacket.send(player, blessing, false);
				if (player.equals(context.getSource().getPlayer())) {
					context.getSource().sendFeedback(Text.translatable("miskatonicmysteries.command.remove_blessing.self",
																		  Text.translatable(blessing.getTranslationString())), true);
				} else {
					context.getSource().sendFeedback(
						Text.translatable("miskatonicmysteries.command.remove_blessing", player.getDisplayName(),
											 Text.translatable(blessing.getTranslationString())), true);
				}
			}
		}
		return 0;
	}

	private static int giveWorldNBT(CommandContext<ServerCommandSource> context) {
		context.getSource()
			.sendFeedback(NbtHelper.toPrettyPrintedText(MMWorldState.get(context.getSource().getWorld()).writeNbt(new NbtCompound())),
						  false);
		context.getSource().sendFeedback(
			NbtHelper.toPrettyPrintedText(MMDimensionalWorldState.get(context.getSource().getWorld()).writeNbt(new NbtCompound())), false);
		return 0;
	}

	private static int clearWorldNBT(CommandContext<ServerCommandSource> context) {
		context.getSource().sendFeedback(MMWorldState.get(context.getSource().getWorld()).clear(), false);
		return 0;
	}

	private static int setAscension(CommandContext<ServerCommandSource> context, Identifier affiliation, int stage, ServerPlayerEntity player)
		throws CommandSyntaxException {
		return setAscension(context, affiliation, stage, List.of(player));
	}

	private static int setAscension(CommandContext<ServerCommandSource> context, Identifier affiliation, int stage,
									Collection<ServerPlayerEntity> players
	) throws CommandSyntaxException {
		for (ServerPlayerEntity player : players) {
			if (Ascendant.of(player).isPresent() && Affiliated.of(player).isPresent()) {
				MalleableAffiliated.of(player).get().setAffiliation(MMRegistries.AFFILIATIONS.get(affiliation), false);
				Ascendant.of(player).get().setAscensionStage(stage);
				if (player.equals(context.getSource().getPlayer())) {
					context.getSource()
						.sendFeedback(Text.translatable("miskatonicmysteries.command.set_ascension.self", affiliation, stage), false);
				} else {
					context.getSource().sendFeedback(
						Text.translatable("miskatonicmysteries.command.set_ascension", player.getDisplayName(), affiliation, stage),
						false);
				}
			}

		}
		return 0;
	}

	private static int setMaxSpells(CommandContext<ServerCommandSource> context, int value, ServerPlayerEntity player)
		throws CommandSyntaxException {
		return setMaxSpells(context, value, List.of(player));
	}

	private static int setMaxSpells(CommandContext<ServerCommandSource> context, int value, Collection<ServerPlayerEntity> players)
		throws CommandSyntaxException {
		for (ServerPlayerEntity player : players) {
			if (SpellCaster.of(player).isPresent()) {
				SpellCaster caster = SpellCaster.of(player).get();
				caster.setMaxSpells(value);
				if (player.equals(context.getSource().getPlayer())) {
					context.getSource().sendFeedback(Text.translatable("miskatonicmysteries.command.set_max_spells.self", value), true);
				} else {
					context.getSource()
						.sendFeedback(Text.translatable("miskatonicmysteries.command.set_max_spells", player.getDisplayName(), value),
									  true);
				}
			}
		}
		return value;
	}

	private static int setPowerPool(CommandContext<ServerCommandSource> context, int value, ServerPlayerEntity player) throws CommandSyntaxException {
		return setPowerPool(context, value, List.of(player));
	}

	private static int setPowerPool(CommandContext<ServerCommandSource> context, int value, Collection<ServerPlayerEntity> players)
		throws CommandSyntaxException {
		for (ServerPlayerEntity player : players) {
			if (SpellCaster.of(player).isPresent()) {
				SpellCaster caster = SpellCaster.of(player).get();
				caster.setPowerPool(value);
				if (player.equals(context.getSource().getPlayer())) {
					context.getSource().sendFeedback(Text.translatable("miskatonicmysteries.command.set_power_pool.self", value), true);
				} else {
					context.getSource()
						.sendFeedback(Text.translatable("miskatonicmysteries.command.set_power_pool", player.getDisplayName(), value),
									  true);
				}
			}
		}
		return value;
	}

	private static int learnSpellEffect(CommandContext<ServerCommandSource> context, Identifier effect, ServerPlayerEntity player)
		throws CommandSyntaxException {
		return learnSpellEffect(context, effect, List.of(player));
	}

	private static int learnSpellEffect(CommandContext<ServerCommandSource> context, Identifier effect, Collection<ServerPlayerEntity> players)
		throws CommandSyntaxException {
		for (ServerPlayerEntity player : players) {
			if (SpellCaster.of(player).isPresent()) {
				SpellCaster caster = SpellCaster.of(player).get();
				SpellEffect spellEffect = MMRegistries.SPELL_EFFECTS.get(effect);
				caster.learnEffect(spellEffect);
				caster.syncSpellData();
				if (player.equals(context.getSource().getPlayer())) {
					context.getSource().sendFeedback(Text.translatable("miskatonicmysteries.command.learn_spell.self",
																		  Text.translatable(spellEffect.getTranslationString())), true);
				} else {
					context.getSource().sendFeedback(
						Text.translatable("miskatonicmysteries.command.learn_spell", player.getDisplayName(),
											 Text.translatable(spellEffect.getTranslationString())), true);
				}
			}
		}
		return 0;
	}

	private static int removeSpellEffect(CommandContext<ServerCommandSource> context, Identifier effect, ServerPlayerEntity player)
		throws CommandSyntaxException {
		return removeSpellEffect(context, effect, List.of(player));
	}

	private static int removeSpellEffect(CommandContext<ServerCommandSource> context, Identifier effect, Collection<ServerPlayerEntity> players)
		throws CommandSyntaxException {
		for (ServerPlayerEntity player : players) {
			if (SpellCaster.of(player).isPresent()) {
				SpellCaster caster = SpellCaster.of(player).get();
				SpellEffect spellEffect = MMRegistries.SPELL_EFFECTS.get(effect);
				caster.getLearnedEffects().remove(spellEffect);
				caster.syncSpellData();
				if (player.equals(context.getSource().getPlayer())) {
					context.getSource().sendFeedback(Text.translatable("miskatonicmysteries.command.removed_spell.self",
																		  Text.translatable(spellEffect.getTranslationString())), true);
				} else {
					context.getSource().sendFeedback(
						Text.translatable("miskatonicmysteries.command.removed_spell", player.getDisplayName(),
											 Text.translatable(spellEffect.getTranslationString())), true);
				}
			}
		}
		return 0;
	}

	private static int learnMedium(CommandContext<ServerCommandSource> context, Identifier medium, ServerPlayerEntity player)
		throws CommandSyntaxException {
		return learnMedium(context, medium, List.of(player));
	}

	private static int learnMedium(CommandContext<ServerCommandSource> context, Identifier medium, Collection<ServerPlayerEntity> players)
		throws CommandSyntaxException {
		for (ServerPlayerEntity player : players) {
			if (SpellCaster.of(player).isPresent()) {
				SpellCaster caster = SpellCaster.of(player).get();
				SpellMedium spellMedium = MMRegistries.SPELL_MEDIUMS.get(medium);
				caster.learnMedium(spellMedium);
				caster.syncSpellData();
				if (player.equals(context.getSource().getPlayer())) {
					context.getSource().sendFeedback(Text.translatable("miskatonicmysteries.command.learn_medium.self",
																		  Text.translatable(spellMedium.getTranslationString())), true);
				} else {
					context.getSource().sendFeedback(
						Text.translatable("miskatonicmysteries.command.learn_medium", player.getDisplayName(),
											 Text.translatable(spellMedium.getTranslationString())), true);
				}
			}
		}
		return 0;
	}

	private static int removeMedium(CommandContext<ServerCommandSource> context, Identifier medium, ServerPlayerEntity player)
		throws CommandSyntaxException {
		return removeMedium(context, medium, List.of(player));
	}

	private static int removeMedium(CommandContext<ServerCommandSource> context, Identifier medium, Collection<ServerPlayerEntity> players)
		throws CommandSyntaxException {
		for (ServerPlayerEntity player : players) {
			if (SpellCaster.of(player).isPresent()) {
				SpellCaster caster = SpellCaster.of(player).get();
				SpellMedium spellMedium = MMRegistries.SPELL_MEDIUMS.get(medium);
				caster.getLearnedMediums().remove(spellMedium);
				caster.syncSpellData();
				if (player.equals(context.getSource().getPlayer())) {
					context.getSource().sendFeedback(Text.translatable("miskatonicmysteries.command.remove_medium.self",
																		  Text.translatable(spellMedium.getTranslationString())), true);
				} else {
					context.getSource().sendFeedback(
						Text.translatable("miskatonicmysteries.command.remove_medium", player.getDisplayName(),
											 Text.translatable(spellMedium.getTranslationString())), true);
				}
			}
		}
		return 0;
	}

	private static int spawnProtagonist(CommandContext<ServerCommandSource> context, PlayerEntity player) {
		boolean spawned = ProtagonistHandler.spawnProtagonist(context.getSource().getWorld(), player);
		if (spawned) {
			context.getSource()
				.sendFeedback(Text.translatable("miskatonicmysteries.command.summon_investigator", player.getDisplayName()), true);
		} else {
			context.getSource().sendError(Text.translatable("miskatonicmysteries.command.summon_investigator.failure"));
		}
		return spawned ? 15 : 0;
	}

	private static int castSpell(Identifier mediumId, Identifier effectId, int intensity, LivingEntity caster) {
		SpellMedium medium = MMRegistries.SPELL_MEDIUMS.get(mediumId);
		SpellEffect effect = MMRegistries.SPELL_EFFECTS.get(effectId);
		Spell spell = new Spell(medium, effect, intensity);
		return spell.cast(caster) ? 15 : 0;
	}

	private static int displayVision(CommandContext<ServerCommandSource> context, Identifier visionId, ServerPlayerEntity player) {
		return displayVision(context, visionId, List.of(player));
	}

	private static int displayVision(CommandContext<ServerCommandSource> context, Identifier visionId, Collection<ServerPlayerEntity> players) {
		for (ServerPlayerEntity player : players) {
			VisionPacket.send(player, visionId);
			context.getSource().sendFeedback(
				Text.translatable("miskatonicmysteries.command.display_vision", visionId.toString(), player.getDisplayName()), true);
		}
		return players.size() > 0 ? 15 : 0;
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
				if (id.toString().startsWith(builder.getRemaining().toLowerCase())) {
					builder.suggest(id.toString());
				}
			});
			return builder.buildFuture();
		}
	}
}
