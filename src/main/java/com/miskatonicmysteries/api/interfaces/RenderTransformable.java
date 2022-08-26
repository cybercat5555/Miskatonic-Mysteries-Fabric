package com.miskatonicmysteries.api.interfaces;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public interface RenderTransformable {

	@Environment(EnvType.CLIENT) int mm_getSquishTicks();

	@Environment(EnvType.CLIENT) void mm_squish();
}
