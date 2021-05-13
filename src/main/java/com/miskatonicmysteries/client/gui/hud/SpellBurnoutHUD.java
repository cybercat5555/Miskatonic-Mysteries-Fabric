package com.miskatonicmysteries.client.gui.hud;

import com.miskatonicmysteries.api.interfaces.SpellCaster;
import com.miskatonicmysteries.common.util.Constants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class SpellBurnoutHUD extends DrawableHelper {
    public static final Identifier VIGNETTE_TEXTURE = new Identifier(Constants.MOD_ID, "textures/gui/burnout.png");

    public void render(MinecraftClient client, int scaledWidth, int scaledHeight, PlayerEntity player) {
        SpellCaster.of(player).ifPresent(caster -> {
            if (caster.getSpellCooldown() > 0) {
                float burnout = MathHelper.clamp(caster.getSpellCooldown() / 80F, 0, 0.5F);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                RenderSystem.color4f(1F, 1F, 1F, burnout);
                client.getTextureManager().bindTexture(VIGNETTE_TEXTURE);
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferBuilder = tessellator.getBuffer();
                bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE);
                bufferBuilder.vertex(0.0D, scaledHeight, -90.0D).texture(0.0F, 1.0F).next();
                bufferBuilder.vertex(scaledWidth, scaledHeight, -90.0D).texture(1.0F, 1.0F).next();
                bufferBuilder.vertex(scaledWidth, 0.0D, -90.0D).texture(1.0F, 0.0F).next();
                bufferBuilder.vertex(0.0D, 0.0D, -90.0D).texture(0.0F, 0.0F).next();
                tessellator.draw();
                RenderSystem.depthMask(true);
                RenderSystem.enableDepthTest();
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
        });
    }
}
