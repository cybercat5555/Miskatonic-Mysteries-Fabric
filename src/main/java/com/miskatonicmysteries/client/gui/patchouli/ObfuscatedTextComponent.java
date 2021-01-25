package com.miskatonicmysteries.client.gui.patchouli;

import com.google.gson.annotations.SerializedName;
import com.miskatonicmysteries.common.feature.Affiliation;
import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.util.CapabilityUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.client.book.gui.BookTextRenderer;
import vazkii.patchouli.client.book.gui.GuiBook;

import java.util.function.UnaryOperator;

public class ObfuscatedTextComponent implements ICustomComponent {
    transient int x, y;
    transient BookTextRenderer textRender;
    transient ObfuscatedBookTextRenderer alternateTextRender;
    transient boolean obfuscated;
    transient String actualText;

    IVariable text;
    @SerializedName("obfuscated_text")
    IVariable obfuscatedText;
    IVariable affiliation;
    IVariable stage;

    @Override
    public void build(int componentX, int componentY, int pageNum) {
        this.x = componentX;
        this.y = componentY;
    }

    @Override
    public void onDisplayed(IComponentRenderContext context) {
        textRender = new BookTextRenderer((GuiBook) context.getGui(), actualText, x, y);
        alternateTextRender = new ObfuscatedBookTextRenderer((GuiBook) context.getGui(), actualText, x, y);
    }

    private boolean canPlayerRead(int level, Affiliation affiliation) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        return affiliation == null || CapabilityUtil.getAffiliation(player, true).equals(affiliation) && level <= CapabilityUtil.getStage(player);

    }

    @Override
    public void render(MatrixStack ms, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
        if (!obfuscated) {
            textRender.render(ms, mouseX, mouseY);
        } else {
            alternateTextRender.render(ms, mouseX, mouseY);
        }
    }

    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
        String affiliationString = lookup.apply(affiliation).asString();
        Affiliation affiliation = affiliationString == null ? null : Affiliation.AFFILIATION_MAP.getOrDefault((affiliationString.contains(":") ? new Identifier(affiliationString) : new Identifier(Constants.MOD_ID, affiliationString)), null);
        obfuscated = !canPlayerRead(lookup.apply(stage).asNumber(-1).intValue(), affiliation);
        actualText = obfuscated && obfuscatedText != null ? lookup.apply(obfuscatedText).asString() : lookup.apply(text).asString();
    }
}
