package com.miskatonicmysteries.client.gui.toast;

import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class SpellEffectToast extends SimpleIconToast{
	private static final Text TITLE = new TranslatableText("spell_effect.miskatonicmysteries.toast.title");

	public SpellEffectToast(Identifier icon, String translation) {
		super(icon, translation);
	}

	@Override
	protected Text getTitle() {
		return TITLE;
	}

	public static void show(ToastManager manager, Identifier icon, String translation) {
		SpellEffectToast toast = manager.getToast(SpellEffectToast.class, TYPE);
		if (toast == null) {
			manager.add(new SpellEffectToast(icon, translation));
		}
		else {
			toast.addIcon(icon, translation);
		}
	}
}
