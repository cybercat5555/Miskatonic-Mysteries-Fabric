package com.miskatonicmysteries.client.gui.patchouli;

import com.google.gson.annotations.SerializedName;
import com.miskatonicmysteries.client.gui.EditSpellScreen;
import com.miskatonicmysteries.common.feature.Affiliation;
import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.util.CapabilityUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.client.book.gui.GuiBook;

import java.util.function.UnaryOperator;

import static com.miskatonicmysteries.client.gui.patchouli.ObfuscatedBookTextRenderer.OBFUSCATED_FONT_ID;

public class ObfuscatedHeaderComponent implements ICustomComponent {
    transient int x, y;
    transient boolean obfuscated;
    transient Text renderedText;
    transient Text obfuscatedTextString;
    transient int actualStage;
    transient Affiliation actualAffiliation;

    IVariable text;
    @SerializedName("obfuscated_text")
    IVariable obfuscatedText;
    IVariable affiliation;
    IVariable stage;

    @Override
    public void build(int componentX, int componentY, int pageNum) {
        this.x = componentX;
        this.y = componentY;

        if (x == -1) {
            x = GuiBook.PAGE_WIDTH / 2;
        }
        if (y == -1) {
            y = 0;
        }
    }

    @Override
    public void onDisplayed(IComponentRenderContext context) {
        obfuscated = !canPlayerRead(actualStage, actualAffiliation);
    }

    private boolean canPlayerRead(int level, Affiliation affiliation) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        return (affiliation == null || CapabilityUtil.getAffiliation(player, false).equals(affiliation)) && level <= CapabilityUtil.getStage(player);
    }

    @Override
    public void render(MatrixStack ms, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
        EditSpellScreen.drawCenteredText(MinecraftClient.getInstance().textRenderer, ms, obfuscated ? obfuscatedTextString : renderedText, x, y, context.getHeaderColor());
    }

    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
        String affiliationString = lookup.apply(affiliation).asString();
        actualAffiliation = affiliationString == null ? null : Affiliation.AFFILIATION_MAP.getOrDefault((affiliationString.contains(":") ? new Identifier(affiliationString) : new Identifier(Constants.MOD_ID, affiliationString)), null);
        actualStage = lookup.apply(stage).asNumber(-1).intValue();
        renderedText = new LiteralText(lookup.apply(text).asString(""));
        obfuscatedTextString = new LiteralText(lookup.apply(obfuscatedText).asString(renderedText.getString())).fillStyle(Style.EMPTY.withFont(OBFUSCATED_FONT_ID));
    }
}
