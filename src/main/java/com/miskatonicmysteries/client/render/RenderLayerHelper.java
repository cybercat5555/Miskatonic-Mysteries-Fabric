package com.miskatonicmysteries.client.render;

import com.miskatonicmysteries.common.lib.Constants;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

public class RenderLayerHelper extends RenderLayer {
    public static final Identifier SKY_TEXTURE = new Identifier(Constants.MOD_ID, "textures/block/octagram/octagram_sky.png");
    public static final Identifier PORTAL_TEXTURE = new Identifier(Constants.MOD_ID, "textures/block/octagram/octagram_stars.png");

    public RenderLayerHelper(String name, VertexFormat vertexFormat, int drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }

    public static RenderLayer getOctagramLayer() {
        RenderLayer.MultiPhaseParameters param = RenderLayer.MultiPhaseParameters.builder()
                .shadeModel(new RenderPhase.ShadeModel(true))
                .texture(new RenderPhase.Texture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, false, true))
                .diffuseLighting(new RenderPhase.DiffuseLighting(true))
                .alpha(new RenderPhase.Alpha(0.04F))
                .lightmap(new RenderPhase.Lightmap(true))
                .build(true);
        return RenderLayer.of(Constants.MOD_ID + ":octagram_layer", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT, GL11.GL_QUADS, 128, true, true, param);
    }

    public static RenderLayer getPortalLayer(int layer) {
        Transparency transparency;
        Texture texture;
        Texturing texturing;
        if (layer <= 1) {
            transparency = TRANSLUCENT_TRANSPARENCY;
            texture = new RenderPhase.Texture(SKY_TEXTURE, false, false);
            texturing = new PortalTexturing(layer);
        } else {
            transparency = ADDITIVE_TRANSPARENCY;
            texture = new RenderPhase.Texture(PORTAL_TEXTURE, false, false);
            texturing = new PortalTexturing(layer);
        }

        return of(Constants.MOD_ID + ":portal", VertexFormats.POSITION_COLOR, 7, 256, false, true, RenderLayer.MultiPhaseParameters.builder().transparency(transparency).texture(texture).texturing(texturing).fog(BLACK_FOG).build(false));
    }

   /* public static class MMPortalTexturing extends Texturing {
        private final int layer;

        public MMPortalTexturing(int layer) {
            super(Constants.MOD_ID + ":portal_texturing", () -> {
                RenderSystem.matrixMode(5890);
                RenderSystem.pushMatrix();
                RenderSystem.loadIdentity();
                RenderSystem.translatef(0.5F, 0.5F, 0.0F);
                RenderSystem.scalef(0.5F, 0.5F, 1.0F);
                RenderSystem.translatef(17.0F / (float) layer, (2.0F + (float) layer / 1.5F) * ((float) (Util.getMeasuringTimeMs() % 800000L) / 800000.0F), 0.0F);
                RenderSystem.rotatef(((float) (layer * layer) * 4321.0F + (float) layer * 9.0F) * 2.0F, 0.0F, 0.0F, 1.0F);
                RenderSystem.scalef(4.5F - (float) layer / 4.0F, 4.5F - (float) layer / 4.0F, 1.0F);
                RenderSystem.mulTextureByProjModelView();
                RenderSystem.matrixMode(5888);
                GL11.glEnable(GL11.GL_STENCIL_TEST);
                RenderSystem.stencilMask(0x00);
                RenderSystem.stencilFunc(GL11.GL_EQUAL, 1, 0xFF);
                RenderSystem.stencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
                RenderSystem.setupEndPortalTexGen();
            }, () -> {
                RenderSystem.matrixMode(5890);
                RenderSystem.popMatrix();
                RenderSystem.matrixMode(5888);
                RenderSystem.clearTexGen();
                RenderSystem.clear(GL11.GL_STENCIL_BUFFER_BIT, true);
                GL11.glDisable(GL11.GL_STENCIL_TEST);
            });
            this.layer = layer;
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            } else if (object != null && this.getClass() == object.getClass()) {
                MMPortalTexturing portalTexturing = (MMPortalTexturing) object;
                return this.layer == portalTexturing.layer;
            } else {
                return false;
            }
        }

        public int hashCode() {
            return Integer.hashCode(this.layer);
        }
    }*/
}
