package com.miskatonicmysteries.client.gui.widget;

import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.client.gui.EditSpellScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class MediumComponentWidget extends SpellComponentWidget {
    public SpellMedium medium;

    public MediumComponentWidget(int x, int y, SpellMedium medium, EditSpellScreen screen) {
        super(x, y, 32, 32, Type.MEDIUM, medium.getId(), screen);
        this.textureLocation = medium.getTextureLocation();
        this.medium = medium;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (isSelected()) {
            screen.selectedMedium = null;
        } else if (isValidClickButton(0)) {
            screen.selectedMedium = this;
        }
    }

    @Override
    protected boolean isValidClickButton(int button) {
        return screen.learnedMediums.contains(medium) && super.isValidClickButton(button);
    }

    @Override
    public boolean isSelected() {
        return this.equals(screen.selectedMedium);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, isHovered() && isValidClickButton(0) ? 0.75F : !screen.learnedMediums.contains(medium) ? 0.25F : this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        minecraftClient.getTextureManager().bindTexture(textureLocation);
        drawTexture(matrices, this.x + 7, this.y + 7, 0, 0, 18, 18, 18, 18);
        minecraftClient.getTextureManager().bindTexture(EditSpellScreen.BOOK_TEXTURE);
        drawTexture(matrices, this.x, this.y, 26, 182 + (isSelected() ? 39 : 0), 32, 32, 512, 256);
        this.renderBg(matrices, minecraftClient, mouseX, mouseY);
    }
}
