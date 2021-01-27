package com.miskatonicmysteries.client.gui.hud;

import com.miskatonicmysteries.client.gui.HUDHandler;
import com.miskatonicmysteries.common.feature.interfaces.SpellCaster;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec2f;

import java.util.ArrayList;
import java.util.List;

public class SpellHUD extends DrawableHelper {
    private float lastMouseX, lastMouseY;
    private float mouseX, mouseY;
    public int scaledWidth, scaledHeight;
    private MinecraftClient client;
    private List<Spell> renderedSpells = new ArrayList<>();
    public static final int RADIUS = 32;
    private double angle = -1;
    public Spell selectedSpell;
    public float currentSpellProgress;
    public int absentTicks;

    public SpellHUD() {
        //???
    }

    public void init(MinecraftClient client) {
        scaledWidth = client.getWindow().getScaledWidth();
        scaledHeight = client.getWindow().getScaledHeight();
        this.client = client;
        renderedSpells.clear();
        SpellCaster.of(client.player).ifPresent(caster -> {
            for (Spell spell : caster.getSpells()) {
                if (spell != null) {
                    renderedSpells.add(spell);
                }
            }
        });
        mouseX = (float) (client.mouse.getX() * (double) scaledWidth / (double) client.getWindow().getWidth());
        mouseY = (float) (client.mouse.getY() * (double) scaledHeight / (double) client.getWindow().getHeight());
        angle = -1;
        selectedSpell = null;
    }

    public void tick() {
        lastMouseX = mouseX;
        lastMouseY = mouseY;
        mouseX = (int) (client.mouse.getX() * (double) scaledWidth / (double) client.getWindow().getWidth());
        mouseY = (int) (client.mouse.getY() * (double) scaledHeight / (double) client.getWindow().getHeight());
        Vec2f directionVector = new Vec2f(mouseX - lastMouseX, mouseY - lastMouseY);
        if (Math.abs(directionVector.x) > 0.5F || Math.abs(directionVector.y) > 0.5F) {
            angle = Math.toDegrees(Math.atan2(0 - directionVector.y, -1 - directionVector.x)) - 180;
            if (angle < 0) {
                angle += 360;
            }
            absentTicks = 0;
        } else if (++absentTicks > 10) {
            angle = -1;
        }
    }


    public void render(MatrixStack matrixStack, float tickDelta) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        matrixStack.translate(scaledWidth / 2F, scaledHeight / 2F, 0);
        if (renderedSpells.size() > 1) {
            double angleSize = 360F / renderedSpells.size();
            for (int i = 0; i < renderedSpells.size(); i++) {
                Spell renderedSpell = renderedSpells.get(i);
                if (renderedSpell != null) {
                    matrixStack.push();
                    double angle = (i * angleSize + 360) % 360 - 90;
                    if (angle < 0) {
                        angle += 360;
                    }
                    double angleRad = Math.toRadians(angle);
                    double x = Math.cos(angleRad) * RADIUS;
                    double y = Math.sin(angleRad) * RADIUS;
                    if (isSelected(angle)) {
                        selectedSpell = renderedSpell;
                        RenderSystem.color4f(1, 1, 1, 1);
                    } else {
                        RenderSystem.color4f(1, 1, 1, 0.5F);
                    }
                    renderSpellIcon(x, y, matrixStack, renderedSpell);

                    matrixStack.pop();
                }
            }
        } else if (renderedSpells.size() == 1) {
            selectedSpell = renderedSpells.get(0);
            HUDHandler.selectionActive = false;
        }
    }

    public void renderSpellIcon(double x, double y, MatrixStack matrixStack, Spell spell) {
        matrixStack.translate(x, y, 0);
        client.getTextureManager().bindTexture(spell.effect.getTextureLocation());
        drawTexture(matrixStack, -9, -9, 0, 0, 18, 18, 18, 18);
    }

    public boolean isSelected(double angleDeg) {
        if (angle < 0) {
            return false;
        }
        if (renderedSpells.size() <= 1) {
            return true;
        }
        double distance = Math.abs(angleDeg - angle);
        return distance < (360F / (float) renderedSpells.size()) / 2F && distance < 90;
    }
}
