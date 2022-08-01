package com.miskatonicmysteries.common.feature.recipe.rite.condition;

import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.function.Predicate;

public abstract class RiteCondition implements Predicate<OctagramBlockEntity> {
	protected final Identifier iconLocation;
	protected Text message, description;
	protected boolean checkWhileRunning;
	public RiteCondition(Identifier id) {
		this.iconLocation = new Identifier(id.getNamespace(), String.format("textures/gui/rite_conditions/%s.png", id.getPath()));
		this.message = new TranslatableText(String.format("message.%s.rite_fail.%s", id.getNamespace(), id.getPath()));
		this.description = new TranslatableText(String.format("desc.%s.rite_fail.%s", id.getNamespace(), id.getPath()));
		this.checkWhileRunning = false;
	}

	public boolean checkStartCondition(OctagramBlockEntity octagram) {
		if (test(octagram)) {
			return true;
		}
		sendFailMessage(octagram.getOriginalCaster());
		return false;
	}

	public boolean checkWhileRunning(OctagramBlockEntity octagram) {
		return test(octagram);
	}

	public boolean shouldCheckWhileRunning() {
		return checkWhileRunning;
	}

	protected void sendFailMessage(PlayerEntity caster) {
		caster.sendMessage(message, true);
	}

	public Identifier getIconLocation() {
		return iconLocation;
	}

	@Environment(EnvType.CLIENT)
	public void renderIcon(MatrixStack matrixStack, int x, int y) {

	}

	public Text getDescription() {
		return description;
	}
}
