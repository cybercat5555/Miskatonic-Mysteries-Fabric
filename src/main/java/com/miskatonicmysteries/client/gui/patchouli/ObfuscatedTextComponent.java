package com.miskatonicmysteries.client.gui.patchouli;

import com.miskatonicmysteries.common.feature.Affiliation;
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
    transient String text = "";
    transient Affiliation affiliation;
    transient int level;

    @Override
    public void build(int componentX, int componentY, int pageNum) {
        this.x = componentX;
        this.y = componentY;
    }

    @Override
    public void onDisplayed(IComponentRenderContext context) {
        textRender = new BookTextRenderer((GuiBook) context.getGui(), text, x, y);
        alternateTextRender = new ObfuscatedBookTextRenderer((GuiBook) context.getGui(), text, x, y);
    }

    private boolean canPlayerRead() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        return affiliation == null || CapabilityUtil.getAffiliation(player, true).equals(affiliation);

    }

    @Override
    public void render(MatrixStack ms, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
        if (canPlayerRead()) {
            textRender.render(ms, mouseX, mouseY);
        } else {
            //        alternateTextRender.render(ms, mouseX, mouseY);
        }
    }

    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
        text = lookup.apply(IVariable.wrap("#text#")).asString();
        affiliation = Affiliation.AFFILIATION_MAP.getOrDefault(new Identifier(lookup.apply(IVariable.wrap("#affiliation_needed#")).asString()), null);
        level = lookup.apply(IVariable.wrap("#level_needed#")).asNumber().intValue();
    }
}
