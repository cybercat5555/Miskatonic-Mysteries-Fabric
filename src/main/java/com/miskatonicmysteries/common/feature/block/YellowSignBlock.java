package com.miskatonicmysteries.common.feature.block;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.block.SignBlock;
import com.miskatonicmysteries.common.handler.networking.packet.c2s.InvokeManiaPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class YellowSignBlock extends SignBlock {
    public YellowSignBlock() {
        super(FabricBlockSettings.of(Material.CARPET, MapColor.YELLOW).noCollision().hardness(1).resistance(3F));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(5) == 0) {
            MinecraftClient client = MinecraftClient.getInstance();
            Vec3d posTracked = client.player.raycast(100, client.getTickDelta(), false).getPos();
            if (posTracked != null && pos.isWithinDistance(posTracked, 1.5F) && !MiskatonicMysteriesAPI.isImmuneToYellowSign(client.player)) {
                InvokeManiaPacket.send(1, 200 + random.nextInt(200));
            }
        }
        super.randomDisplayTick(state, world, pos, random);
    }
}
