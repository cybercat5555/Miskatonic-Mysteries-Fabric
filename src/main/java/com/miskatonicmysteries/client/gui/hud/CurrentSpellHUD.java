package com.miskatonicmysteries.client.gui.hud;

import com.miskatonicmysteries.api.interfaces.SpellCaster;
import com.miskatonicmysteries.client.gui.SpellClientHandler;
import com.miskatonicmysteries.client.gui.widget.SelectSpellWidget;
import com.miskatonicmysteries.common.MiskatonicMysteries;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.util.Constants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class CurrentSpellHUD extends DrawableHelper {
    private int shiftTicks;
    private float alpha;

    public void render(MinecraftClient client, MatrixStack matrixStack, float tickDelta, int scaledWidth, int scaledHeight, PlayerEntity player) {
        if (SpellClientHandler.selectedSpell != null) {
            SpellCaster.of(player).ifPresent(caster -> {
                matrixStack.push();
                matrixStack.translate(scaledWidth - MiskatonicMysteries.config.client.currentSpellHUD.marginX, scaledHeight - MiskatonicMysteries.config.client.currentSpellHUD.marginY, 0);
                RenderSystem.setShaderTexture(0, SelectSpellWidget.getTexture(SpellClientHandler.selectedSpell.intensity));
                this.alpha = 1 - shiftTicks / 20F;
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.enableDepthTest();
                drawTexture(matrixStack, 0, 0, 0, 0, 26, 26, 26, 26);
                RenderSystem.setShaderTexture(0, SpellClientHandler.selectedSpell.effect.getTextureLocation());
                drawTexture(matrixStack, 4, 4, 0, 0, 18, 18, 18, 18);
                if (Screen.hasShiftDown()) {
                    if (shiftTicks < 10) {
                        this.shiftTicks++;
                    }
                }else if (this.shiftTicks > 0){
                    this.shiftTicks --;
                }
                if (this.shiftTicks  > 0 && client.world != null){
                    float ticks = (client.world.getTime() + tickDelta) * 0.1F;
                    double x = Math.sin(ticks) * 16;
                    double y = Math.cos(ticks) * 16;
                    matrixStack.push();
                    RenderSystem.setShaderTexture(0, SpellClientHandler.selectedSpell.medium.getTextureLocation());
                    matrixStack.translate(x, y, 0);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, (shiftTicks + (Screen.hasShiftDown() ?  tickDelta : -tickDelta))/ 10F);
                    drawTexture(matrixStack, 5, 2, 0, 0, 18, 18, 18, 18);
                    matrixStack.pop();
                }
                matrixStack.pop();
            });
        }
    }
}
