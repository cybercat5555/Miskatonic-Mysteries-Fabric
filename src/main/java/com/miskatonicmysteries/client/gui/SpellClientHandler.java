package com.miskatonicmysteries.client.gui;

import com.miskatonicmysteries.api.interfaces.SpellCaster;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.handler.networking.packet.SpellPacket;
import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.nbt.NbtCompound;

import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class SpellClientHandler {

	public static KeyBinding spellSelectionKey;
	public static KeyBinding castKey;
	public static Spell selectedSpell; //reset upon logging in

	public static void init() {
		spellSelectionKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key." + Constants.MOD_ID + ".select_spell",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_R,
			"category." + Constants.MOD_ID
		));
		castKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key." + Constants.MOD_ID + ".cast",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_F,
			"category." + Constants.MOD_ID
		));
		ClientLoginConnectionEvents.INIT.register((handler, client) -> SpellClientHandler.selectedSpell = null);

		ClientTickEvents.START_CLIENT_TICK.register(client -> {
			if (spellSelectionKey.isPressed() && client.currentScreen == null && SpellCaster.of(client.player)
				.map(caster -> !caster.getSpells().isEmpty()).orElse(false)) {
				client.setScreen(new SpellSelectionScreen());
			} else if (client.player != null && selectedSpell != null) {
				if (castKey.wasPressed() && SpellCaster.of(client.player).map(SpellCaster::getSpellCooldown).orElse(0) <= 0) {
					SpellPacket.sendFromClientPlayer(client.player, selectedSpell.toTag(new NbtCompound()));
				} else if (client.player.isDead()) {
					selectedSpell = null;
				}
			}
		});
	}
}
