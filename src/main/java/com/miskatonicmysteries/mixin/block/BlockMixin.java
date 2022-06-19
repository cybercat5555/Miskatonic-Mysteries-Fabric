package com.miskatonicmysteries.mixin.block;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.banner.impl.LoomPatternContainer;
import com.miskatonicmysteries.common.MiskatonicMysteries;
import com.miskatonicmysteries.common.handler.InsanityHandler;
import com.miskatonicmysteries.common.handler.networking.packet.c2s.InvokeManiaPacket;
import com.miskatonicmysteries.common.util.Util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin extends AbstractBlock {

	public BlockMixin(Settings settings) {
		super(settings);
	}

	@Environment(EnvType.CLIENT)
	@Inject(method = "randomDisplayTick", at = @At("HEAD"))
	public void randomDisplay(BlockState state, World world, BlockPos pos, Random random, CallbackInfo info) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player != null && client.player.age % MiskatonicMysteries.config.sanity.insanityInterval == 0 && random.nextFloat() < 0.1F) {
			InsanityHandler.handleClientSideBlockChange(client.player, world, state, pos, random);
		} else if ((state.getBlock() instanceof AbstractBannerBlock) && random.nextInt(5) == 0
			&& world.getBlockEntity(pos) instanceof LoomPatternContainer.Internal internal
			&& Util.isValidYellowSign(internal.bannermm_getLoomPatternTag())) {
			Vec3d posTracked = client.player.raycast(100, client.getTickDelta(), false).getPos();
			if (posTracked != null && pos.isWithinDistance(posTracked, 1.5F) && !MiskatonicMysteriesAPI.isImmuneToYellowSign(client.player)) {
				InvokeManiaPacket.send(1, 200 + random.nextInt(200));
			}
		}

	}
}
