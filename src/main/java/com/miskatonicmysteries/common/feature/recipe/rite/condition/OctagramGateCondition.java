package com.miskatonicmysteries.common.feature.recipe.rite.condition;

import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.item.IncantationYogItem;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class OctagramGateCondition extends MultiRiteCondition {
	public OctagramGateCondition() {
		super(new Identifier(Constants.MOD_ID, "gate_condition"),
			new TranslatableText("message.miskatonicmysteries.rite_fail.gate_condition.incantation"),
			new TranslatableText("message.miskatonicmysteries.rite_fail.gate_condition.not_present"),
			new TranslatableText("message.miskatonicmysteries.rite_fail.gate_condition.self_reference"),
			new TranslatableText("message.miskatonicmysteries.rite_fail.gate_condition.bad_affiliation"),
			new TranslatableText("message.miskatonicmysteries.rite_fail.gate_condition.already_bound"));
	}

	@Override
	public boolean test(OctagramBlockEntity octagramBlockEntity) {
		ItemStack incantation = octagramBlockEntity.getStack(MMObjects.INCANTATION_YOG);
		if (incantation.isEmpty() || IncantationYogItem.getPosition(incantation) == null ||
			IncantationYogItem.getWorld((ServerWorld) octagramBlockEntity.getWorld(), incantation) == null) {
			messageType = 0;
			return false;
		}
		BlockPos octagramPos = IncantationYogItem.getPosition(incantation);
		ServerWorld boundWorld = IncantationYogItem.getWorld((ServerWorld) octagramBlockEntity.getWorld(), incantation);
		if (!(boundWorld.getBlockEntity(octagramPos) instanceof OctagramBlockEntity)) {
			messageType = 1;
			return false;
		}
		if (octagramPos.equals(octagramBlockEntity.getPos())) {
			messageType = 2;
			return false;
		}
		OctagramBlockEntity otherOctagram = (OctagramBlockEntity) boundWorld.getBlockEntity(octagramPos);
		if (otherOctagram.getAffiliation(false) != octagramBlockEntity.getAffiliation(false)) {
			messageType = 3;
			return false;
		}
		if (otherOctagram.boundPos != null) {
			messageType = 4;
			return false;
		}
		return true;
	}
}
