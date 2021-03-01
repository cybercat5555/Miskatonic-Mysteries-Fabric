package com.miskatonicmysteries.client.gui.widget;

import com.miskatonicmysteries.client.gui.EditSpellScreen;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.registry.MMRegistries;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class CombinedSpellWidget extends AbstractButtonWidget {
    public EditSpellScreen screen;
    public int index;

    public CombinedSpellWidget(int x, int y, int index, EditSpellScreen screen) {
        super(x, y, 48, 20, NarratorManager.EMPTY);
        this.screen = screen;
        this.index = index;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        screen.spells[index] = null;
        if ((screen.selectedMedium != null && screen.selectedEffect != null && screen.power >= 0)) {
            screen.spells[index] = new Spell(screen.selectedMedium.medium, MMRegistries.SPELL_EFFECTS.get(screen.selectedEffect.linkedId), screen.power);
            screen.selectedMedium = null;
            screen.selectedEffect = null;
            screen.power = -1;
        }
        screen.updateAvailablePower();
    }

    @Override
    protected boolean isValidClickButton(int button) {
        return ((screen.selectedMedium != null && screen.selectedEffect != null && screen.power >= 0) || screen.spells[index] != null) && super.isValidClickButton(button);
    }


    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        minecraftClient.getTextureManager().bindTexture(EditSpellScreen.BOOK_TEXTURE);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, isHovered() && isValidClickButton(0) ? 0.75F : this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        if (index == screen.user.getMaxSpells() - 1) {
            drawTexture(matrices, this.x, this.y, 272, 33 + 3 * 20, this.width, this.height + 20, 512, 256);
        } else {
            int part = index == 0 ? 0 : 1 + (index % 2);
            drawTexture(matrices, this.x, this.y, 272, 33 + part * 20, this.width, this.height, 512, 256);
        }

        if (screen.spells[index] != null) {
            for (int i = 0; i < screen.spells[index].intensity + 1; i++) {
                drawTexture(matrices, this.x + 38, this.y + 14 - 6 * i, 352, 96, 9, 5, 512, 256);
            }
            minecraftClient.getTextureManager().bindTexture(screen.spells[index].medium.getTextureLocation());
            drawTexture(matrices, this.x + 1, this.y + 1, 0, 0, 18, 18, 18, 18);
            minecraftClient.getTextureManager().bindTexture(screen.spells[index].effect.getTextureLocation());
            drawTexture(matrices, this.x + 19, this.y + 1, 0, 0, 18, 18, 18, 18);
        }
        this.renderBg(matrices, minecraftClient, mouseX, mouseY);
    }
}
