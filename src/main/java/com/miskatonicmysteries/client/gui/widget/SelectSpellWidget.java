package com.miskatonicmysteries.client.gui.widget;

import com.miskatonicmysteries.client.gui.SpellClientHandler;
import com.miskatonicmysteries.client.gui.SpellSelectionScreen;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.util.Constants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class SelectSpellWidget extends ClickableWidget {

	public Spell spell;
	public SpellSelectionScreen screen;
	private int hoverTicks = 0;

	public SelectSpellWidget(int x, int y, SpellSelectionScreen screen, Spell spell) {
		super(x, y, 28, 28, NarratorManager.EMPTY);
		this.spell = spell;
		this.screen = screen;
		this.active = true;
	}

	public static Identifier getTexture(int potency) {
		return new Identifier(Constants.MOD_ID, String.format("textures/gui/spell_widgets/potency/potency_%d.png", potency));
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		if (!isSelected()) {
			SpellClientHandler.selectedSpell = spell;
		} else {
			SpellClientHandler.selectedSpell = null;
		}
	}

	private boolean isSelected() {
		return SpellClientHandler.selectedSpell == spell;
	}

	@Override
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		MinecraftClient minecraftClient = MinecraftClient.getInstance();
		RenderSystem.setShaderTexture(0, getTexture(spell.intensity));
		this.alpha = (screen.openTicks + MinecraftClient.getInstance().getTickDelta()) / 5F;
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, Math.min(isHovered() || isSelected() ? 1 : 0.5F, this.alpha));
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		drawTexture(matrices, this.x + 1, this.y + 1, 0, 0, this.width - 2, this.height - 2, 26, 26);
		RenderSystem.setShaderTexture(0, spell.effect.getTextureLocation());
		drawTexture(matrices, this.x + 5, this.y + 5, 0, 0, 18, 18, 18, 18);
		if (isHovered() && hoverTicks < 20) {
			hoverTicks++;
		} else if (hoverTicks > 0) {
			hoverTicks--;
		}
		if (hoverTicks > 0) {
			float ticks = (minecraftClient.world.getTime() + delta) * 0.1F;
			double x = this.x + (Math.sin(ticks)) * 16;
			double y = this.y + (Math.cos(ticks)) * 16;
			matrices.push();
			RenderSystem.setShaderTexture(0, spell.medium.getTextureLocation());
			matrices.translate(x, y, 0);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, (hoverTicks + (isHovered() ? -delta : delta)) / 10F);
			drawTexture(matrices, 5, 2, 0, 0, 18, 18, 18, 18);
			matrices.pop();
		}
		this.renderBackground(matrices, minecraftClient, mouseX, mouseY);
	}

	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {
		super.appendDefaultNarrations(builder);
	}
}
