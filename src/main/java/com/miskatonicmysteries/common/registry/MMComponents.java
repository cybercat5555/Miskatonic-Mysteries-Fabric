package com.miskatonicmysteries.common.registry;

import com.miskatonicmysteries.common.component.AscendantComponent;
import com.miskatonicmysteries.common.util.Constants;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class MMComponents implements EntityComponentInitializer {
	public static final ComponentKey<AscendantComponent> ASCENDANT_COMPONENT =
			ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(Constants.MOD_ID, "ascendant"),
					AscendantComponent.class);

	@Override
	public void registerEntityComponentFactories(@NotNull EntityComponentFactoryRegistry registry) {
		registry.registerForPlayers(ASCENDANT_COMPONENT, AscendantComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
	}
}
