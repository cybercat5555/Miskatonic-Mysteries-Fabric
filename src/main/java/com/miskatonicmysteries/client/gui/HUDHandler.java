package com.miskatonicmysteries.client.gui;

import com.miskatonicmysteries.api.interfaces.SpellCaster;
import com.miskatonicmysteries.client.gui.hud.SpellBurnoutHUD;
import com.miskatonicmysteries.client.gui.hud.SpellHUD;
import com.miskatonicmysteries.common.handler.networking.packet.SpellPacket;
import com.miskatonicmysteries.common.util.Constants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.impl.client.rendering.RenderingCallbackInvoker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.nbt.CompoundTag;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class HUDHandler {
    private static KeyBinding spellSelectionKey;
    public static boolean selectionActive = false;

    public static SpellHUD spellHUD;
    public static SpellBurnoutHUD burnoutHUD;

    public static void init() {
        spellHUD = new SpellHUD();
        burnoutHUD = new SpellBurnoutHUD();
        spellSelectionKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key." + Constants.MOD_ID + ".hud",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category." + Constants.MOD_ID
        ));

        ClientTickEvents.START_CLIENT_TICK.register(client -> {

            if (spellSelectionKey.isPressed()) {
                if (SpellCaster.of(client.player).map(caster -> caster.getSpellCooldown()).orElse(0) > 0){
                    selectionActive = false;
                }else if (!selectionActive) {
                    selectionActive = true;
                    spellHUD.init(client);
                } else {
                    spellHUD.tick();
                }
            } else {
                selectionActive = false;
            }

            if (client.player != null && spellHUD.selectedSpell != null) {
                System.out.println(SpellCaster.of(client.player).map(SpellCaster::getSpellCooldown).orElse(0));
                if (client.options.keyUse.isPressed()) {
                    spellHUD.currentSpellProgress += 0.1F;
                    if (spellHUD.currentSpellProgress >= 1) {
                        SpellPacket.sendFromClientPlayer(client.player, spellHUD.selectedSpell.toTag(new CompoundTag()));
                        spellHUD.currentSpellProgress = 0;
                        spellHUD.selectedSpell = null;
                    }
                } else {
                    spellHUD.currentSpellProgress = 0;
                }
            }

        });

        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            burnoutHUD.render(client, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight(), client.player);
            if (selectionActive) {
                spellHUD.render(matrixStack, tickDelta);
            }
            if ((!selectionActive || spellHUD.renderedSpells.size() <= 1) && spellHUD.selectedSpell != null) {
                matrixStack.push();
                RenderSystem.color4f(1, 1, 1, 0.15F + spellHUD.currentSpellProgress * 0.85F);
                spellHUD.renderSpellIcon(spellHUD.scaledWidth / 2F, spellHUD.scaledHeight / 2F, matrixStack, spellHUD.selectedSpell);
                RenderSystem.color4f(1, 1, 1, 1);
                matrixStack.pop();
            }
        });
    }
}
