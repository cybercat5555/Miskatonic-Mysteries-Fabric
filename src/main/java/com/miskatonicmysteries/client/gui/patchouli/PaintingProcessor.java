package com.miskatonicmysteries.client.gui.patchouli;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.decoration.painting.PaintingMotive;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class PaintingProcessor implements IComponentProcessor {

	protected PaintingMotive motive;
	protected String path;

	@Override
	public void setup(IVariableProvider variables) {
		this.motive = Registry.PAINTING_MOTIVE.get(new Identifier(variables.get("painting").asString()));
		Identifier id = MinecraftClient.getInstance().getPaintingManager().getPaintingSprite(motive).getId();
		this.path = String.format("%s:textures/%s.png", id.getNamespace(), id.getPath());
	}

	@Override
	public IVariable process(String key) {
		return switch (key) {
			case "path" -> IVariable.wrap(path);
			case "width" -> IVariable.wrap(motive.getWidth());
			case "height" -> IVariable.wrap(motive.getHeight());
			case "xcoord" -> IVariable.wrap(80 - motive.getWidth() / 2);
			case "ycoord" -> IVariable.wrap(80 - motive.getHeight() / 2);
			default -> null;
		};
	}
}
