package com.miskatonicmysteries.common.handler;

import com.miskatonicmysteries.common.feature.sanity.ISanity;
import com.miskatonicmysteries.common.feature.sanity.InsanityEvent;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.ModEntities;
import com.miskatonicmysteries.common.lib.ModParticles;
import com.miskatonicmysteries.common.lib.ModRegistries;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PacketHandler {
    public static final Identifier SANITY_EXPAND_PACKET = new Identifier(Constants.MOD_ID, "sanity_expansion");
    public static final Identifier SANITY_REMOVE_EXPAND_PACKET = new Identifier(Constants.MOD_ID, "sanity_remove_expansion");

    public static final Identifier INSANITY_EVENT_PACKET = new Identifier(Constants.MOD_ID, "insanity_event");

    public static final Identifier SPELL_PACKET = new Identifier(Constants.MOD_ID, "spell");
    public static final Identifier TARGET_PACKET = new Identifier(Constants.MOD_ID, "target");

    public static final Identifier PROTAG_PARTICLE_PACKET = new Identifier(Constants.MOD_ID, "protag_particle");


    public static final Identifier CLIENT_INVOKE_MANIA_PACKET = new Identifier(Constants.MOD_ID, "invoke_mania");

    public static void registerC2S() {
        ServerPlayNetworking.registerGlobalReceiver(CLIENT_INVOKE_MANIA_PACKET, (server, player, handler, packetByteBuf, sender) -> {
            int amplifier = packetByteBuf.readInt();
            int duration = packetByteBuf.readInt();
            server.execute(() -> {
                        player.addStatusEffect(new StatusEffectInstance(ModRegistries.MANIA, duration, amplifier, true, true));
                        ((ISanity) player).setShocked(true);
                    }
            );
        });
    }

    public static void registerS2C() {
        ClientPlayNetworking.registerGlobalReceiver(SANITY_EXPAND_PACKET, (client, networkHandler, packetByteBuf, sender) -> {
            String name = packetByteBuf.readString();
            int amount = packetByteBuf.readInt();
            client.execute(() -> ((ISanity) client.player).addSanityCapExpansion(name, amount));
        });

        ClientPlayNetworking.registerGlobalReceiver(SANITY_REMOVE_EXPAND_PACKET, (client, networkHandler, packetByteBuf, sender) -> {
            String name = packetByteBuf.readString();
            client.execute(() -> ((ISanity) client.player).removeSanityCapExpansion(name));
        });

        ClientPlayNetworking.registerGlobalReceiver(INSANITY_EVENT_PACKET, (client, networkHandler, packetByteBuf, sender) -> {
            Identifier id = packetByteBuf.readIdentifier();
            client.execute(() -> InsanityEvent.INSANITY_EVENTS.get(id).execute(client.player, (ISanity) client.player));
        });

        ClientPlayNetworking.registerGlobalReceiver(SPELL_PACKET, (client, networkHandler, packetByteBuf, sender) -> {
            CompoundTag spellTag = packetByteBuf.readCompoundTag();
            Entity entity = client.world.getEntityById(packetByteBuf.readInt());
            if (entity instanceof LivingEntity)
                client.execute(() -> Spell.fromTag(spellTag).cast((LivingEntity) entity));
        });

        ClientPlayNetworking.registerGlobalReceiver(TARGET_PACKET, (client, networkHandler, packetByteBuf, sender) -> {
            Entity mob = client.world.getEntityById(packetByteBuf.readInt());
            Entity entity = client.world.getEntityById(packetByteBuf.readInt());
            if (mob instanceof MobEntity && entity instanceof LivingEntity)
                client.execute(() -> ((MobEntity) mob).setTarget((LivingEntity) entity));
        });

        ClientPlayNetworking.registerGlobalReceiver(INSANITY_EVENT_PACKET, (client, networkHandler, packetByteBuf, sender) -> {
            Identifier id = packetByteBuf.readIdentifier();
            client.execute(() -> InsanityEvent.INSANITY_EVENTS.get(id).execute(client.player, (ISanity) client.player));
        });

        ClientPlayNetworking.registerGlobalReceiver(PROTAG_PARTICLE_PACKET, (client, networkHandler, packetByteBuf, sender) -> {
            Vec3d pos = new Vec3d(packetByteBuf.readDouble(), packetByteBuf.readDouble(), packetByteBuf.readDouble());
            client.execute(() -> {
                for (int i = 0; i < 10; i++)
                    client.world.addParticle(ModParticles.FLAME, pos.x + client.world.random.nextGaussian() * ModEntities.PROTAGONIST.getWidth(), pos.y + client.world.random.nextFloat() * ModEntities.PROTAGONIST.getHeight(), pos.z + client.world.random.nextGaussian() * ModEntities.PROTAGONIST.getWidth(), 1, 0, 0);
                for (int i = 0; i < 15; i++)
                    client.world.addParticle(ParticleTypes.LARGE_SMOKE, pos.x + client.world.random.nextGaussian() * ModEntities.PROTAGONIST.getWidth(), pos.y + client.world.random.nextFloat() * ModEntities.PROTAGONIST.getHeight(), pos.z + client.world.random.nextGaussian() * ModEntities.PROTAGONIST.getWidth(), 0, 0, 0);
            });
        });
    }

    public static void sendToPlayer(PlayerEntity player, PacketByteBuf data, Identifier packet) {
        if (player instanceof ServerPlayerEntity && ((ServerPlayerEntity) player).networkHandler != null)
            ServerPlayNetworking.send((ServerPlayerEntity) player, packet, data);
    }

    public static void sendToPlayers(World world, PacketByteBuf data, Identifier packet) {
        if (world instanceof ServerWorld) {
            world.getPlayers().forEach(p -> sendToPlayer(p, data, packet));
        }
    }
}
