package com.miskatonicmysteries.mixin;

import com.miskatonicmysteries.common.handler.PacketHandler;
import com.miskatonicmysteries.common.lib.util.MiscUtil;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(Block.class)
public abstract class BlockMixin extends AbstractBlock {
    public BlockMixin(Settings settings) {
        super(settings);
    }

    @Environment(EnvType.CLIENT)
    @Inject(method = "randomDisplayTick", at = @At("HEAD"))
    public void randomDisplay(BlockState state, World world, BlockPos pos, Random random, CallbackInfo info) {
        if ((state.getBlock() instanceof AbstractBannerBlock) && random.nextInt(5) == 0 && world.getBlockEntity(pos) instanceof BannerBlockEntity
                && MiscUtil.isValidYellowSign(world.getBlockEntity(pos).toTag(new CompoundTag()))) {
            MinecraftClient client = MinecraftClient.getInstance();
            Vec3d posTracked = client.player.raycast(100, client.getTickDelta(), false).getPos();
            if (posTracked != null && pos.isWithinDistance(posTracked, 1.5F) && !MiscUtil.isImmuneToYellowSign(client.player)) {
                PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
                data.writeInt(1);
                data.writeInt(200 + random.nextInt(200));
                ClientPlayNetworking.send(PacketHandler.CLIENT_INVOKE_MANIA_PACKET, data);
            }
        }
    }
}
