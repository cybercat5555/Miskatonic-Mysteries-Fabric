package com.miskatonicmysteries.common.handler.predicate;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.client.gui.ConfigurePredicateScreen;
import com.miskatonicmysteries.client.gui.widget.AffiliationSelectWidget;
import com.miskatonicmysteries.client.gui.widget.CycleEnumWidget;
import com.miskatonicmysteries.client.gui.widget.NegationCheckboxWidget;
import com.miskatonicmysteries.client.gui.widget.PlayerListWidget;
import com.miskatonicmysteries.client.gui.widget.PlayerSelectWidget;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMCriteria;
import com.miskatonicmysteries.common.registry.MMRegistries;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.Constants.NBT;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;

public class ConfigurableTargetPredicate extends ConfigurablePredicate {

	public static final Identifier PLAYER_CATEGORY = new Identifier(Constants.MOD_ID, "players");
	public static final Identifier AFFILIATION_CATEGORY = new Identifier(Constants.MOD_ID, "affiliation");
	public static final Identifier MISC_CATEGORY = new Identifier(Constants.MOD_ID, "misc");
	public final List<GameProfile> players = new ArrayList<>();
	public Affiliation affiliation;
	public SelectionMode mode;
	public boolean excludePlayers;

	public ConfigurableTargetPredicate() {
		super(MMCriteria.TARGET_PREDICATE);
		this.affiliation = MMAffiliations.NONE;
		this.mode = SelectionMode.PLAYERS_ONLY;
	}

	@Override
	public boolean test(Entity entity) {
		if (mode.predicate.test(entity)) {
			if (affiliation != MMAffiliations.NONE && MiskatonicMysteriesAPI.getPracticallyApparentAffiliation(entity) != affiliation) {
				return false;
			}
			if (entity instanceof PlayerEntity p) {
				return excludePlayers != players.contains(p.getGameProfile());
			}
			return true;
		}
		return false;
	}

	@Override
	public ConfigurablePredicate readFromNbt(NbtCompound compound) {
		try {
			this.affiliation = Objects.requireNonNull(MMRegistries.AFFILIATIONS.get(new Identifier(compound.getString(NBT.AFFILIATION))));
		} catch (NullPointerException npe) {
			this.affiliation = MMAffiliations.NONE;
		}
		NbtList playerUuids = compound.getList(NBT.PLAYERS, NbtElement.COMPOUND_TYPE);
		for (NbtElement element : playerUuids) {
			if (element instanceof NbtCompound nbt) {
				players.add(NbtHelper.toGameProfile(nbt));
			}
		}
		mode = SelectionMode.values()[compound.getInt(NBT.MODE)];
		excludePlayers = compound.getBoolean(NBT.EXCLUDE_PLAYERS);
		return super.readFromNbt(compound);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void addWidgets(ConfigurePredicateScreen screen, Identifier currentCategory) {
		//think about how to arrange the widgets for sensible configuration
		super.addWidgets(screen, currentCategory);
		if (currentCategory == PLAYER_CATEGORY) {
			PlayerListWidget list = new PlayerListWidget(screen, this);
			screen.addWidgetExternally(new PlayerSelectWidget(screen, this, list));
			screen.addWidgetExternally(list);
			screen.addWidgetExternally(new NegationCheckboxWidget(screen.width / 2 + 36, 40, excludePlayers,
																  new TranslatableText(Constants.MOD_ID + ".gui.exclude_players_query"),
																  b -> excludePlayers = b));
		} else if (currentCategory == AFFILIATION_CATEGORY) {
			screen.addWidgetExternally(new AffiliationSelectWidget(screen.width / 2 - 45, screen.height / 2, affiliation, (a) -> affiliation = a));
		} else if (currentCategory == MISC_CATEGORY) {
			int x = screen.width / 2;
			int y = screen.height / 2;
			screen.addDrawableExternally((matrices, mouseX, mouseY, delta) -> {
				RenderSystem.setShaderTexture(0, ConfigurePredicateScreen.TEXTURE);
				DrawableHelper.drawTexture(matrices, x - 47, y - 17, 0, 0, 94, 40, 128, 128);
			});
			screen.addWidgetExternally(new CycleEnumWidget<>(mode.ordinal(), x, y + 6, 40,
															 changed -> mode = (SelectionMode) changed,
															 mode -> ((SelectionMode) mode).displayText,
															 SelectionMode.values()));
		}

	}

	@Override
	public NbtCompound writeNbt(NbtCompound compound) {
		compound.putString(NBT.AFFILIATION, affiliation.getId().toString());
		NbtList list = new NbtList();
		for (GameProfile player : players) {
			list.add(NbtHelper.writeGameProfile(new NbtCompound(), player));
		}
		compound.put(NBT.PLAYERS, list);
		compound.putInt(NBT.MODE, mode.ordinal());
		compound.putBoolean(NBT.EXCLUDE_PLAYERS, excludePlayers);
		return super.writeNbt(compound);
	}

	public boolean addPlayer(UUID uuid, String name) {
		GameProfile profile = new GameProfile(uuid, name);
		if (!players.contains(profile)) {
			this.players.add(profile);
			return true;
		}
		return false;
	}

	public boolean removePlayer(GameProfile profile) {
		return this.players.remove(profile);
	}

	enum SelectionMode {
		PLAYERS_ONLY(new TranslatableText(Constants.MOD_ID + ".gui.config_mode.players"), entity -> entity instanceof PlayerEntity),
		MOBS_ONLY(new TranslatableText(Constants.MOD_ID + ".gui.config_mode.mobs"), entity -> entity instanceof MobEntity),
		ALL_MOBS(new TranslatableText(Constants.MOD_ID + ".gui.config_mode.all"), entity -> entity instanceof LivingEntity);

		public final Text displayText;
		public final Predicate<Entity> predicate;

		SelectionMode(Text displayText, Predicate<Entity> predicate) {
			this.displayText = displayText;
			this.predicate = predicate;
		}

	}
}
