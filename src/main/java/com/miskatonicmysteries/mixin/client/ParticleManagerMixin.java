package com.miskatonicmysteries.mixin.client;

import com.google.common.collect.ImmutableList;
import com.miskatonicmysteries.common.registry.MMParticles;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ParticleManager.class)
public class ParticleManagerMixin {

	@Mutable
	@Final
	@Shadow
	private static List<ParticleTextureSheet> PARTICLE_TEXTURE_SHEETS;

	@Inject(at = @At("RETURN"), method = "<init>")
	private void addTypes(ClientWorld world, TextureManager textureManager, CallbackInfo ci) {
		PARTICLE_TEXTURE_SHEETS = ImmutableList.<ParticleTextureSheet>builder().addAll(PARTICLE_TEXTURE_SHEETS)
			.add(MMParticles.ParticleTextureSheets.GLOWING)
			.build();
	}
}
