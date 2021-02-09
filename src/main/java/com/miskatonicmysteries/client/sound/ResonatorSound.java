package com.miskatonicmysteries.client.sound;

import com.miskatonicmysteries.common.block.blockentity.energy.ResonatorBlockEntity;
import com.miskatonicmysteries.common.registry.MMSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;

public class ResonatorSound extends MovingSoundInstance implements TickableSoundInstance {
    private final ClientPlayerEntity player;
    private final ClientWorld world;
    private final BlockPos pos;
    public static final Map<BlockPos, ResonatorSound> soundInstances = new HashMap<>();

    protected ResonatorSound(BlockPos pos) {
        super(MMSounds.RESONATOR_HUMMING, SoundCategory.BLOCKS);
        this.player = MinecraftClient.getInstance().player;
        this.world = MinecraftClient.getInstance().world;
        this.pos = pos;
        this.x = pos.getX() + 0.5F;
        this.y = pos.getY() + 0.5F;
        this.z = pos.getZ() + 0.5F;
        this.volume = 0.5F;
        this.pitch = 1;
        this.looping = true;
        this.repeat = true;
    }

    public static void createSound(BlockPos pos) {
        if (soundInstances.containsKey(pos)) {
            return;
        }
        ResonatorSound sound = new ResonatorSound(pos);
        soundInstances.put(pos, sound);
        MinecraftClient.getInstance().getSoundManager().play(sound);
    }

    @Override
    public boolean isDone() {
        return player == null || !(world.getBlockEntity(pos) instanceof ResonatorBlockEntity) || !((ResonatorBlockEntity) world.getBlockEntity(pos)).isPowered();
    }


    @Override
    public void tick() {
        checkVolume();
    }

    private void checkVolume() {
        this.volume = (1 - (float) (player.squaredDistanceTo(new Vec3d(x, y, z)) / 64F)) / 3F;
    }
}
