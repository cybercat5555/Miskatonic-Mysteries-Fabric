package com.miskatonicmysteries.client.gui;

import com.miskatonicmysteries.api.interfaces.SpellCaster;
import com.miskatonicmysteries.client.gui.widget.SelectSpellWidget;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.util.Constants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class SpellSelectionScreen extends Screen {
    private static final int totalRadius = 32;
    public int openTicks;//used to calculate "blending in" alpha

    protected SpellSelectionScreen() {
        super(new TranslatableText(Constants.MOD_ID + ".gui.spell_select"));
        this.passEvents = true;
    }

    @Override
    protected void init() {
        super.init();
        SpellCaster.of(client.player).ifPresent(caster -> {
            List<Spell> spells = caster.getSpells().stream().filter(Objects::nonNull).collect(Collectors.toList());
            double angleSize = (Math.PI * 2) / spells.size();
            int centerX = width / 2 - totalRadius / 2 + 2;
            int centerY = height / 2 - totalRadius / 2 + 2;
            for (int i = 0; i < spells.size(); i++) {
                double angle = angleSize * i + Math.PI;
                double x = centerX + (Math.sin(angle) * totalRadius);
                double y = centerY + (Math.cos(angle) * totalRadius);
                addDrawableChild(new SelectSpellWidget((int) Math.round(x), (int) Math.round(y), this, spells.get(i)));
            }
        });
    }

    private boolean isSpellSelectionKeyPressed() {
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), KeyBindingHelper.getBoundKeyOf(SpellClientHandler.spellSelectionKey).getCode());
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    public void tick() {
        super.tick();
        if (isSpellSelectionKeyPressed()) {
            if (openTicks < 5) {
                openTicks++;
            }
        } else {
            this.onClose();
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
