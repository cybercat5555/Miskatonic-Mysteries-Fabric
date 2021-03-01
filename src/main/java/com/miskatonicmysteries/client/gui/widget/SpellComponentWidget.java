package com.miskatonicmysteries.client.gui.widget;

import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.client.gui.EditSpellScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class SpellComponentWidget extends AbstractButtonWidget {
    public Identifier linkedId;
    public Identifier textureLocation;
    public EditSpellScreen screen;
    public Type type;

    public SpellComponentWidget(int x, int y, int width, int height, Type type, Identifier linkedId, EditSpellScreen screen) {
        super(x, y, width, height, NarratorManager.EMPTY);
        this.linkedId = linkedId;
        this.textureLocation = new Identifier(linkedId.getNamespace(), "textures/gui/spell_widgets/" + linkedId.getPath() + ".png");
        this.screen = screen;
        this.type = type;
    }

    public SpellComponentWidget(int x, int y, SpellEffect effect, EditSpellScreen screen) {
        super(x, y, 24, 24, NarratorManager.EMPTY);
        this.linkedId = effect.getId();
        this.screen = screen;
        this.type = Type.EFFECT;
        this.textureLocation = effect.getTextureLocation();
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (isSelected()) {
            screen.selectedEffect = null;
        } else {
            screen.selectedEffect = this;
        }
    }

    public boolean isSelected() {
        return this.equals(screen.selectedEffect);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, isHovered() ? 0.75F : this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        minecraftClient.getTextureManager().bindTexture(EditSpellScreen.BOOK_TEXTURE);
        drawTexture(matrices, this.x, this.y, 0, 182 + (isSelected() ? 39 : 0), 24, 24, 512, 256);
        minecraftClient.getTextureManager().bindTexture(textureLocation);
        drawTexture(matrices, this.x + 3, this.y + 3, 0, 0, 18, 18, 18, 18);
        this.renderBg(matrices, minecraftClient, mouseX, mouseY);
    }

    public enum Type {
        MEDIUM,
        EFFECT
    }
}
