package com.miskatonicmysteries.client.gui.toast;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class SpellEffectToast extends SimpleIconToast {

	private static final Text TITLE = new TranslatableText("spell_effect.miskatonicmysteries.toast.title");

	public SpellEffectToast(Identifier icon, String translation) {
		super(icon, translation);
	}

	public static void show(ToastManager manager, Identifier icon, String translation) {
		SpellEffectToast toast = manager.getToast(SpellEffectToast.class, TYPE);
		if (toast == null) {
			manager.add(new SpellEffectToast(icon, translation));
		} else {
			toast.addIcon(icon, translation);
		}
	}

	@Override
	protected Text getTitle() {
		return TITLE;
	}
}
