package com.miskatonicmysteries.client.gui;

import com.miskatonicmysteries.api.interfaces.SpellCaster;
import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.client.gui.widget.CombinedSpellWidget;
import com.miskatonicmysteries.client.gui.widget.MediumComponentWidget;
import com.miskatonicmysteries.client.gui.widget.SpellComponentWidget;
import com.miskatonicmysteries.client.gui.widget.SpellPowerWidget;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.handler.networking.packet.SyncSpellCasterDataPacket;
import com.miskatonicmysteries.common.registry.MMSpellMediums;
import com.miskatonicmysteries.common.util.Constants;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

import java.util.*;

@Environment(EnvType.CLIENT)
public class EditSpellScreen extends Screen {
    public static final Identifier BOOK_TEXTURE = new Identifier(Constants.MOD_ID, "textures/gui/spellbook.png");
    public Spell[] spells;
    public SpellCaster user;
    public final List<SpellEffect> learnedEffects = new ArrayList<>();
    public final Set<SpellMedium> learnedMediums = new HashSet<>();

    public MediumComponentWidget selectedMedium;
    public SpellComponentWidget selectedEffect;
    public int power = -1;
    public int availablePower;

    public EditSpellScreen(SpellCaster player) {
        super(NarratorManager.EMPTY);
        this.user = player;
        learnedEffects.addAll(user.getLearnedEffects());
        learnedMediums.addAll(user.getLearnedMediums());
        spells = new Spell[player.getMaxSpells()];
        for (int i = 0; i < player.getMaxSpells(); i++) {
            if (i < player.getSpells().size()) {
                spells[i] = player.getSpells().get(i);
            }
        }
        updateAvailablePower();
    }

    @Override
    public void onClose() {
        user.getSpells().clear();
        for (int i = 0; i < spells.length; i++) {
            user.getSpells().add(i, spells[i]);
        }
        SyncSpellCasterDataPacket.send(true, MinecraftClient.getInstance().player, user);
        super.onClose();
    }

    public void updateAvailablePower() {
        availablePower = user.getPowerPool();
        for (Spell spell : spells) {
            if (spell != null) {
                availablePower -= spell.intensity + 1;
                if (availablePower < 0) {
                    clearSpells();
                }
            }
        }
    }

    public void clearSpells() {
        Arrays.fill(spells, null);
    }

    //have a list of available components, components used in spells are removed from it
    @Override
    protected void init() {
        for (int i = 0; i < user.getMaxSpells(); i++) {
            addButton(new CombinedSpellWidget((this.width - 192) / 4 + 254, 48 + (20 * i), i, this));
        }

        for (int i = 0; i < learnedEffects.size(); i++) {
            addButton(new SpellComponentWidget((this.width - 192) / 4 + 30 * (i % 4), 64 + (30 * (i / 4)), learnedEffects.get(i), this));
        }

        addButton(new MediumComponentWidget((this.width - 192) / 4 + 128, 50 + 32, MMSpellMediums.SELF, this));
        addButton(new MediumComponentWidget((this.width - 192) / 4 + 140 + 64, 50 + 32, MMSpellMediums.PROJECTILE, this));
        addButton(new MediumComponentWidget((this.width - 192) / 4 + 138, 34 + 3 * 32, MMSpellMediums.BOLT, this));
        addButton(new MediumComponentWidget((this.width - 192) / 4 + 130 + 64, 34 + 3 * 32, MMSpellMediums.GROUP, this));
        addButton(new MediumComponentWidget((this.width - 192) / 4 + 134 + 32, 30 + 32, MMSpellMediums.VISION, this));

        addButton(new SpellPowerWidget((this.width - 192) / 4 + 134, 38 + 4 * 32, 0, this));
        addButton(new SpellPowerWidget((this.width - 192) / 4 + 134 + 32, 38 + 4 * 32, 1, this));
        addButton(new SpellPowerWidget((this.width - 192) / 4 + 134 + 64, 38 + 4 * 32, 2, this));
    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.setFocused(null);
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.alphaFunc(GL11.GL_GREATER, 0.003921569F);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.client.getTextureManager().bindTexture(BOOK_TEXTURE);

        drawTexture(matrices, (this.width - 192) / 4 - 16, 32, 0, 0, 270, 180, 512, 256);
        drawTexture(matrices, (this.width - 192) / 4 + 134 + 31, 36 + 2 * 32, 174, 204, 34, 34, 512, 256);
        drawTexture(matrices, (this.width - 192) / 4 + 134 + 32, 36 + 2 * 32, 242, 204, 34, 34, 512, 256);
        float powerPercentage = availablePower / (float) user.getMaxSpells();
        matrices.push();
        RenderSystem.color4f(1, 1, 1, powerPercentage);
        drawTexture(matrices, (this.width - 192) / 4 + 134 + 31, 36 + 2 * 32, 208, 204, 34, 34, 512, 256);
        matrices.pop();
        drawCenteredText(textRenderer, matrices, new TranslatableText(Constants.MOD_ID + ".gui.spell_effects"), (this.width - 192) / 4 + 58, 48, 0x111111);
        drawCenteredText(textRenderer, matrices, new TranslatableText(Constants.MOD_ID + ".gui.spell_mediums"), (this.width - 192) / 4 + 184, 48, 0x111111);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        super.render(matrices, mouseX, mouseY, delta);
    }

    public static void drawCenteredText(TextRenderer renderer, MatrixStack stack, Text text, int x, int y, int color) {
        OrderedText orderedText = text.asOrderedText();
        renderer.draw(stack, orderedText, (float) (x - renderer.getWidth(orderedText) / 2), (float) y, color);
    }
}
