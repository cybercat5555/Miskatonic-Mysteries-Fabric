package com.miskatonicmysteries.client.gui.patchouli;

import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.handler.InsanityHandler;
import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.function.UnaryOperator;

import com.mojang.blaze3d.systems.RenderSystem;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

public class InkblotComponent implements ICustomComponent {

	private static final Identifier BASE = new Identifier(Constants.MOD_ID, "textures/gui/inkblots/inkblot_normal.png");
	private static final Identifier DEFAULT = new Identifier(Constants.MOD_ID, "textures/gui/inkblots/inkblot_insane_none.png");
	transient int x, y;
	transient float alphaFactor = 0F;
	transient Identifier overlay;

	@Override
	public void build(int componentX, int componentY, int pageNum) {
		this.x = componentX;
		this.y = componentY;
	}

	@Override
	public void render(MatrixStack ms, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
		RenderSystem.setShaderTexture(0, BASE);
		ms.push();
		ms.translate(x, y, 0);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShaderColor(1F, 1F, 1F, 0.8F + 0.2F * alphaFactor);
		DrawableHelper.drawTexture(ms, 0, 0, 0, 0, 270, 180, 512, 256);
		RenderSystem.setShaderColor(1F, 1F, 1F, 1 - alphaFactor);
		RenderSystem.setShaderTexture(0, overlay);
		DrawableHelper.drawTexture(ms, 0, 0, 0, 0, 270, 180, 512, 256);
		RenderSystem.disableBlend();
		ms.pop();
	}

	@Override
	public void onDisplayed(IComponentRenderContext context) {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		alphaFactor = InsanityHandler.calculateSanityFactor(Sanity.of(player));
		if (MinecraftClient.getInstance().options.debugEnabled) {
			System.out.println(alphaFactor);
		}
		Optional<Affiliated> affiliated = Affiliated.of(player);
		overlay = affiliated.map(this::getOverlayFromAffiliation).orElse(DEFAULT);
	}

	private Identifier getOverlayFromAffiliation(Affiliated affiliated) {
		Affiliation affiliation = affiliated.getAffiliation(false);
		Identifier id = affiliation.getId();
		return new Identifier(id.getNamespace(), "textures/gui/inkblots/inkblot_insane_" + id.getPath() + ".png");
	}

	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {

	}
}
