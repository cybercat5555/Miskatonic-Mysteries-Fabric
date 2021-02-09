package com.miskatonicmysteries.client.gui.patchouli;

import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.handler.InsanityHandler;
import com.miskatonicmysteries.common.util.Constants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

import java.util.Optional;
import java.util.function.UnaryOperator;

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
        TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
        textureManager.bindTexture(BASE);
        ms.push();
        ms.translate(x, y, 0);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.alphaFunc(GL11.GL_GREATER, 0.003921569F);
        RenderSystem.color4f(1F, 1F, 1F, 0.8F + 0.2F * alphaFactor);
        DrawableHelper.drawTexture(ms, 0, 0, 0, 0, 270, 180, 512, 256);

        RenderSystem.color4f(1F, 1F, 1F, 1 - alphaFactor);
        textureManager.bindTexture(overlay);
        DrawableHelper.drawTexture(ms, 0, 0, 0, 0, 270, 180, 512, 256);
        RenderSystem.disableBlend();
        ms.pop();
    }

    @Override
    public void onDisplayed(IComponentRenderContext context) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        alphaFactor = Sanity.of(player).map(InsanityHandler::calculateSanityFactor).orElse(0F);
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
