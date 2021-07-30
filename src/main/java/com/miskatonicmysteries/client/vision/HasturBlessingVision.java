package com.miskatonicmysteries.client.vision;

import com.miskatonicmysteries.client.gui.hud.SpellBurnoutHUD;
import com.miskatonicmysteries.common.util.Constants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class HasturBlessingVision extends VisionSequence{
    private static final Identifier YELLOW_SIGN_TEXTURE = new Identifier(Constants.MOD_ID, "textures/block/yellow_sign.png");
    private static final int totalLength = 280;
    @Override
    public void render(MinecraftClient client, ClientPlayerEntity player, MatrixStack stack, float tickDelta) {
        ticks++;
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();
        float backgroundProgress;
        float signProgress = 0;
        if (ticks > 160){
            backgroundProgress = MathHelper.clamp(1 - (ticks - 160) / 40F, 0, 1);
        }else{
            backgroundProgress = MathHelper.clamp(ticks / 80F, 0, 1);
        }
        if (ticks > 180){
            signProgress = MathHelper.clamp(1 - (ticks - 180) / 100F, 0, 1);
        }else if (ticks > 20){
            signProgress =  MathHelper.clamp((ticks - 20) / 100F, 0, 1);
        }
        float colorProgress = Math.min(signProgress, backgroundProgress);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        //background
       // RenderSystem.shadeModel(7425); todo what this these calls do
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(0.0D, height, 0.0D).color(colorProgress * 0.5F, colorProgress * 0.5F, 0F, backgroundProgress).next();
        bufferBuilder.vertex(width,height, 0.0D).color(colorProgress * 0.5F, colorProgress * 0.5F, 0F, backgroundProgress).next();
        bufferBuilder.vertex(width, 0.0D, -90.0D).color(0, 0, 0F, backgroundProgress).next();
        bufferBuilder.vertex(0.0D, 0.0D, -90.0D).color(0, 0, 0F, backgroundProgress).next();
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
        RenderSystem.enableTexture();
       // RenderSystem.shadeModel(7424);

        RenderSystem.setShaderColor(1F, 1F, 1F, signProgress);
        client.getTextureManager().bindTexture(YELLOW_SIGN_TEXTURE);
        float signX = width / 2F - 64;
        float signY = height / 2F - 64;
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(signX, signY + 128, -90.0D).texture(0.0F, 1.0F).next();
        bufferBuilder.vertex(signX + 128,signY + 128, -90.0D).texture(1.0F, 1.0F).next();
        bufferBuilder.vertex(signX + 128, signY, -90.0D).texture(1.0F, 0.0F).next();
        bufferBuilder.vertex(signX, signY, -90.0D).texture(0.0F, 0.0F).next();
        tessellator.draw();
        //render vignette
        RenderSystem.setShaderColor(1F, 1F, 1F, colorProgress * 0.75F);
        client.getTextureManager().bindTexture(SpellBurnoutHUD.VIGNETTE_TEXTURE);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(0.0D, height, -90.0D).texture(0.0F, 1.0F).next();
        bufferBuilder.vertex(width,height, -90.0D).texture(1.0F, 1.0F).next();
        bufferBuilder.vertex(width, 0.0D, -90.0D).texture(1.0F, 0.0F).next();
        bufferBuilder.vertex(0.0D, 0.0D, -90.0D).texture(0.0F, 0.0F).next();
        tessellator.draw();

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        if (ticks >= totalLength - 1){
            VisionHandler.setVisionSequence(player, null);
            ticks = 0;
        }
    }
}
