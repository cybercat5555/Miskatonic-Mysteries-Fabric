package com.miskatonicmysteries.client.gui.widget;

import com.miskatonicmysteries.client.gui.ConfigurePredicateScreen;
import com.miskatonicmysteries.client.gui.widget.PlayerListWidget.PlayerEntry;
import com.miskatonicmysteries.common.handler.predicate.ConfigurableTargetPredicate;
import com.miskatonicmysteries.common.registry.MMAffiliations;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;

public class PlayerListWidget extends EntryListWidget<PlayerEntry> {

	private final MinecraftClient client;
	private final ConfigurePredicateScreen parent;
	private final ConfigurableTargetPredicate current;

	public PlayerListWidget(ConfigurePredicateScreen parent, ConfigurableTargetPredicate currentPredicate) {
		super(MinecraftClient.getInstance(), 90, 48, 124, 124 + 48, 12);
		this.left = parent.width / 2 - 45;
		this.right = left + width;
		this.client = MinecraftClient.getInstance();
		this.parent = parent;
		this.current = currentPredicate;
		setRenderBackground(false);
		setRenderHeader(false, 0);
		setRenderHorizontalShadows(false);
		updateEntries();
	}

	public void updateEntries() {
		clearEntries();
		for (GameProfile player : current.players) {
			addEntry(new PlayerEntry(player));
		}
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		RenderSystem.setShaderTexture(0, ConfigurePredicateScreen.TEXTURE);
		drawTexture(matrices, left, top, 0, 74, 90, 49, 128, 128);
		double d = this.client.getWindow().getScaleFactor();
		RenderSystem.enableScissor((int) (left * d), (int) ((double) (parent.height - this.bottom) * d),
								   (int) ((width - 9) * d),
								   (int) ((height - 3) * d));
		super.render(matrices, mouseX, mouseY, delta);
		RenderSystem.disableScissor();
		if (getMaxScroll() > 0) {
			double ratio = getScrollAmount() / getMaxScroll();
			RenderSystem.setShaderTexture(0, ConfigurePredicateScreen.TEXTURE);
			drawTexture(matrices, getScrollbarPositionX(), (top + 2) + (int) ((height - 12) * ratio), 90, 114, 6, 9, 128, 128);
		}
	}

	@Override
	protected int getScrollbarPositionX() {
		return right - 8;
	}

	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {

	}

	public class PlayerEntry extends Entry<PlayerEntry> {

		private final GameProfile profile;
		private boolean xHovered;

		public PlayerEntry(GameProfile player) {
			this.profile = player;
		}

		@Override
		public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered,
						   float tickDelta) {
			boolean xHovered = hovered && xHovered(entryHeight, x, y, mouseX, mouseY);
			this.xHovered = xHovered;
			Text xText = Text.of("X").copy().fillStyle(Style.EMPTY.withFormatting(xHovered ? Formatting.BOLD : Formatting.RESET));
			drawTextWithShadow(matrices, client.textRenderer, xText, x + 66, y, MMAffiliations.NONE.textColor);
			drawTextWithShadow(matrices, client.textRenderer, Text.of(profile.getName()), x + 76, y, MMAffiliations.NONE.textColor);
		}

		private boolean xHovered(int entryHeight, int x, int y, int mouseX, int mouseY) {
			int relativeMouseX = mouseX - x - 63;
			int relativeMouseY = mouseY - y;
			return relativeMouseY > 0 && relativeMouseY < entryHeight && relativeMouseX > 0 && relativeMouseX < 7;
		}

		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int button) {
			if (xHovered && current.removePlayer(profile)) {
				removeEntry(this);
				MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f));
				return true;
			}
			return super.mouseClicked(mouseX, mouseY, button);
		}
	}
}
