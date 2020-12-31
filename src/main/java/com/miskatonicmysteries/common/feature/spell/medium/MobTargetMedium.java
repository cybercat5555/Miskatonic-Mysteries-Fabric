package com.miskatonicmysteries.common.feature.spell.medium;

import com.miskatonicmysteries.common.feature.spell.SpellEffect;
import com.miskatonicmysteries.common.feature.spell.SpellMedium;
import com.miskatonicmysteries.common.handler.PacketHandler;
import com.miskatonicmysteries.common.lib.Constants;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class MobTargetMedium extends SpellMedium {
    public MobTargetMedium() {
        super(new Identifier(Constants.MOD_ID, "mob_target"));
    }

    @Override
    public boolean cast(World world, LivingEntity caster, SpellEffect effect, int intensity) {
        if (caster instanceof MobEntity && ((MobEntity) caster).getTarget() != null) {
            if (!world.isClient) {
                PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
                data.writeInt(caster.getEntityId());
                data.writeInt(((MobEntity) caster).getTarget().getEntityId());
                PacketHandler.sendToPlayers(caster.world, data, PacketHandler.TARGET_PACKET);
            }
            if (caster.canSee(((MobEntity) caster).getTarget())) {
                return !caster.isDead() && effect.effect(caster.world, caster, ((MobEntity) caster).getTarget(), ((MobEntity) caster).getTarget().getPos(), this, intensity);
            }
        }
        if (caster instanceof PlayerEntity) return VISION.cast(world, caster, effect, intensity);
        return false;
    }
}
