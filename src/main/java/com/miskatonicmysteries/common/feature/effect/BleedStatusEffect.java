package com.miskatonicmysteries.common.feature.effect;

import com.miskatonicmysteries.common.handler.PacketHandler;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.network.PacketByteBuf;

public class BleedStatusEffect extends StatusEffect {
    public BleedStatusEffect() {
        super(StatusEffectType.HARMFUL, 0xFF0000);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (!entity.world.isClient && entity.world.random.nextFloat() < 0.1 + (0.1 * amplifier)) {
            PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
            data.writeInt(entity.getEntityId());
            PacketHandler.sendToPlayers(entity.world, entity, data, PacketHandler.BLOOD_PARTICLE_PACKET);
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
