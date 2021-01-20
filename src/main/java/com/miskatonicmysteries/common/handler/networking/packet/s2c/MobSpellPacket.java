package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.common.feature.spell.SpellEffect;
import com.miskatonicmysteries.common.feature.spell.SpellMedium;
import com.miskatonicmysteries.common.handler.networking.PacketHandler;
import com.miskatonicmysteries.common.lib.Constants;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class MobSpellPacket {
    public static final Identifier ID = new Identifier(Constants.MOD_ID, "mob_spell");

    public static void send(LivingEntity caster, SpellEffect effect, int intensity) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeInt(caster.getEntityId());
        data.writeInt(caster.getAttacking().getEntityId());
        data.writeIdentifier(effect.getId());
        data.writeInt(intensity);
        PacketHandler.sendToPlayers(caster.world, caster, data, ID);
    }

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf, PacketSender sender) {
        Entity mob = client.world.getEntityById(packetByteBuf.readInt());
        Entity target = client.world.getEntityById(packetByteBuf.readInt());
        SpellEffect effect = SpellEffect.SPELL_EFFECTS.get(packetByteBuf.readIdentifier());
        int intensity = packetByteBuf.readInt();
        if (mob instanceof MobEntity && target instanceof LivingEntity) {
            client.execute(() -> effect.effect(client.world, (MobEntity) mob, target, target.getPos(), SpellMedium.MOB_TARGET, intensity, mob));
        }
    }
}
