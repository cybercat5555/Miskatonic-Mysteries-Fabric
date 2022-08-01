package com.miskatonicmysteries.client.gui.patchouli;

import com.miskatonicmysteries.api.registry.Rite;
import com.miskatonicmysteries.client.render.blockentity.OctagramBlockRender;
import com.miskatonicmysteries.common.feature.recipe.rite.condition.RiteCondition;
import com.miskatonicmysteries.common.registry.MMRegistries;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.miskatonicmysteries.common.util.Util.trimText;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

public class RiteConditionsComponent implements ICustomComponent {
	transient int x, y;
	transient Rite rite;

	@Override
	public void build(int componentX, int componentY, int pageNum) {
		this.x = componentX;
		this.y = componentY;
	}

	@Override
	public void render(MatrixStack ms, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
		ms.push();
		int currentX = x + 42 - rite.startConditions.length * 6;
		int currentY = y;
		ms.translate(currentX, currentY, 0);
		for (RiteCondition condition : rite.startConditions) {
			OctagramBlockRender.drawIcon(ms, false, condition.getIconLocation());
			if (context.isAreaHovered(mouseX, mouseY, currentX - 5, currentY - 5, 8, 8)) {
				List<Text> matchList = trimText(condition.getDescription().getString());
				context.setHoverTooltipComponents(matchList);
			}
			currentX += 12;
			ms.translate(12, 0, 0);
		}
		ms.pop();
	}

	@Override
	public void onDisplayed(IComponentRenderContext context) {

	}

	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
		Identifier id = new Identifier(lookup.apply(IVariable.wrap("#rite_id#")).asString());
		rite = MMRegistries.RITES.get(id);
	}
}
