package com.miskatonicmysteries.api.interfaces;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public interface RenderTransformable {

	@Environment(EnvType.CLIENT) int getSquishTicks();

	@Environment(EnvType.CLIENT) void squish();
}
