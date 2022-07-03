package com.miskatonicmysteries.client.gui.widget;

import com.miskatonicmysteries.client.gui.ConfigurePredicateScreen;
import com.miskatonicmysteries.common.handler.predicate.ConfigurableTargetPredicate;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.mixin.client.SocialInteractionsManagerAccessor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class PlayerSelectWidget extends TextFieldWidget {

	private final MinecraftClient client;
	private final ConfigurableTargetPredicate current;
	private final PlayerListWidget list;
	private @Nullable String currentSuggestion;

	public PlayerSelectWidget(ConfigurePredicateScreen parent, ConfigurableTargetPredicate currentPredicate, PlayerListWidget list) {
		super(MinecraftClient.getInstance().textRenderer, parent.width / 2 - 42, 100, 84, 12, Text.of(""));
		this.client = MinecraftClient.getInstance();
		this.current = currentPredicate;
		this.list = list;
		setChangedListener(s -> {
			Set<String> players = ((SocialInteractionsManagerAccessor) MinecraftClient.getInstance()
				.getSocialInteractionsManager()).getPlayerNameByUuid().keySet();
			String suggestion = null;
			if (!s.isBlank()) {
				List<String> possibleSuggestions = players.stream().map(p -> {
					if (p.startsWith(s)) {
						return p.substring(s.length());
					}
					return null;
				}).filter(Objects::nonNull).sorted(String::compareTo).collect(Collectors.toList());
				if (!possibleSuggestions.isEmpty()) {
					suggestion = possibleSuggestions.get(0);
					currentSuggestion = s + suggestion;
				}
			}
			setSuggestion(suggestion);
		});
		setDrawsBackground(false);
		setEditableColor(MMAffiliations.NONE.textColor);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (!super.keyPressed(keyCode, scanCode, modifiers)) {
			if (keyCode == 257) {
				UUID uuid = client.getSocialInteractionsManager().getUuid(getText());

				if (uuid != null && current.addPlayer(uuid, getText())) {
					list.updateEntries();
					setText("");
					return true;
				}
				return false;
			} else if (keyCode == GLFW.GLFW_KEY_TAB && currentSuggestion != null) {
				setText(currentSuggestion);
			}
		}
		return true;
	}

	@Override
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		RenderSystem.setShaderTexture(0, ConfigurePredicateScreen.TEXTURE);
		drawTexture(matrices, this.x - 3, this.y - 3, 0, 58, 90, 14, 128, 128);
		super.renderButton(matrices, mouseX, mouseY, delta);
	}

	@Override
	public void setSuggestion(@Nullable String suggestion) {
		super.setSuggestion(suggestion);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		return super.mouseScrolled(mouseX, mouseY, amount);
	}

	@Override
	protected boolean clicked(double mouseX, double mouseY) {
		return super.clicked(mouseX, mouseY);
	}
}
