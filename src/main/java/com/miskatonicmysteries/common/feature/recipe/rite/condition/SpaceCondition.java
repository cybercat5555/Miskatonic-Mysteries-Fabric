package com.miskatonicmysteries.common.feature.recipe.rite.condition;

import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.util.Constants;


import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class SpaceCondition extends RiteCondition {
	private final int radiusX, radiusY, radiusZ;
	public SpaceCondition(int radiusX, int radiusY, int radiusZ) {
		super(new Identifier(Constants.MOD_ID, "space"));
		this.radiusX = radiusX;
		this.radiusY = radiusY;
		this.radiusZ = radiusZ;
	}

	public SpaceCondition(int radius) {
		this(radius, radius, radius);
	}

	@Override
	public boolean test(OctagramBlockEntity octagram) {
		for (BlockPos iterateOutward : BlockPos.iterateOutwards(octagram.getPos().add(0, radiusY, 0), radiusX, radiusY, radiusZ)) {
			if (octagram.getWorld().getBlockState(iterateOutward).isSolidBlock(octagram.getWorld(), iterateOutward)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean shouldCheckWhileRunning() {
		return true;
	}
}
