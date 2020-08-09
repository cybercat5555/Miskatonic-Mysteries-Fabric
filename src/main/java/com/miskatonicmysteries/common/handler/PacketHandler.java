package com.miskatonicmysteries.common.handler;

import com.miskatonicmysteries.common.entity.EntityProtagonist;
import com.miskatonicmysteries.common.feature.sanity.ISanity;
import com.miskatonicmysteries.common.feature.sanity.InsanityEvent;
import com.miskatonicmysteries.lib.ModParticles;
import com.miskatonicmysteries.lib.util.Constants;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class PacketHandler {
    public static final Identifier SANITY_EXPAND_PACKET = new Identifier(Constants.MOD_ID, "sanity_expansion");
    public static final Identifier SANITY_REMOVE_EXPAND_PACKET = new Identifier(Constants.MOD_ID, "sanity_remove_expansion");

    public static final Identifier INSANITY_EVENT_PACKET = new Identifier(Constants.MOD_ID, "insanity_event");

    public static final Identifier PROTAGONIST_PARTICLES_PACKET = new Identifier(Constants.MOD_ID, "protag_particles");

    public static void registerC2S() {

    }

    public static void registerS2C() {
        ClientSidePacketRegistry.INSTANCE.register(SANITY_EXPAND_PACKET, (packetContext, packetByteBuf) -> {
            String name = packetByteBuf.readString();
            int amount = packetByteBuf.readInt();
            packetContext.getTaskQueue().execute(() -> ((ISanity) packetContext.getPlayer()).addSanityCapExpansion(name, amount));
        });

        ClientSidePacketRegistry.INSTANCE.register(SANITY_REMOVE_EXPAND_PACKET, (packetContext, packetByteBuf) -> {
            String name = packetByteBuf.readString();
            packetContext.getTaskQueue().execute(() -> ((ISanity) packetContext.getPlayer()).removeSanityCapExpansion(name));
        });

        ClientSidePacketRegistry.INSTANCE.register(INSANITY_EVENT_PACKET, ((packetContext, packetByteBuf) -> {
            Identifier id = packetByteBuf.readIdentifier();
            packetContext.getTaskQueue().execute(() -> InsanityEvent.INSANITY_EVENTS.get(id).execute(packetContext.getPlayer(), (ISanity) packetContext.getPlayer()));
        }));

        ClientSidePacketRegistry.INSTANCE.register(PROTAGONIST_PARTICLES_PACKET, ((packetContext, packetByteBuf) -> {
            EntityProtagonist protagonist = (EntityProtagonist) packetContext.getPlayer().world.getEntityById(packetByteBuf.readInt());
            boolean levelUp = packetByteBuf.readBoolean();
            packetContext.getTaskQueue().execute(() -> {
                int amount = 10 + protagonist.getRandom().nextInt(10);
                for (int i = 0; i < amount; i++)
                    packetContext.getPlayer().world.addParticle(ParticleTypes.SMOKE, protagonist.getX() + protagonist.getRandom().nextGaussian() * protagonist.getDimensions(EntityPose.STANDING).width, protagonist.getY() + protagonist.getRandom().nextFloat() * protagonist.getDimensions(EntityPose.STANDING).height, protagonist.getZ() + protagonist.getRandom().nextGaussian() * protagonist.getDimensions(EntityPose.STANDING).width, 0, 0, 0);
                if (levelUp) {
                    for (int i = 0; i < amount / 2; i++)
                        packetContext.getPlayer().world.addParticle(ModParticles.FLAME, protagonist.getX() + protagonist.getRandom().nextGaussian() * protagonist.getDimensions(EntityPose.STANDING).width, protagonist.getY() + protagonist.getRandom().nextFloat() * protagonist.getDimensions(EntityPose.STANDING).height, protagonist.getZ() + protagonist.getRandom().nextGaussian() * protagonist.getDimensions(EntityPose.STANDING).width, protagonist.getRandom().nextGaussian() / 10D, protagonist.getRandom().nextGaussian() / 10D, protagonist.getRandom().nextGaussian() / 10D);
                }
            });
        }));
    }

    public static void sendToPlayer(PlayerEntity player, PacketByteBuf data, Identifier packet){
        if (player instanceof ServerPlayerEntity && ((ServerPlayerEntity) player).networkHandler != null)
            ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, packet, data);
    }
}
