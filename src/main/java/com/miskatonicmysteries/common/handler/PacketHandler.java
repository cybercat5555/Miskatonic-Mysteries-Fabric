package com.miskatonicmysteries.common.handler;

import com.miskatonicmysteries.client.gui.EditSpellScreen;
import com.miskatonicmysteries.common.feature.sanity.InsanityEvent;
import com.miskatonicmysteries.common.feature.sanity.Sanity;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.feature.spell.SpellCaster;
import com.miskatonicmysteries.common.feature.spell.SpellEffect;
import com.miskatonicmysteries.common.feature.spell.SpellMedium;
import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.ModEntities;
import com.miskatonicmysteries.common.lib.ModParticles;
import com.miskatonicmysteries.common.lib.ModRegistries;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PacketHandler {
    public static final Identifier SANITY_EXPAND_PACKET = new Identifier(Constants.MOD_ID, "sanity_expansion");
    public static final Identifier SANITY_REMOVE_EXPAND_PACKET = new Identifier(Constants.MOD_ID, "sanity_remove_expansion");

    public static final Identifier INSANITY_EVENT_PACKET = new Identifier(Constants.MOD_ID, "insanity_event");

    public static final Identifier SPELL_PACKET = new Identifier(Constants.MOD_ID, "spell");
    public static final Identifier MOB_SPELL_MEDIUM_PACKET = new Identifier(Constants.MOD_ID, "mob_spell");

    public static final Identifier PROTAG_PARTICLE_PACKET = new Identifier(Constants.MOD_ID, "protag_particle");
    public static final Identifier EFFECT_PARTICLE_PACKET = new Identifier(Constants.MOD_ID, "effect_particle");
    public static final Identifier BLOOD_PARTICLE_PACKET = new Identifier(Constants.MOD_ID, "blood_particle");

    public static final Identifier CLIENT_INVOKE_MANIA_PACKET = new Identifier(Constants.MOD_ID, "invoke_mania");

    public static final Identifier SYNC_SPELLCASTER_DATA_PACKET = new Identifier(Constants.MOD_ID, "sync_spell");
    public static final Identifier OPEN_SPELL_EDIT_PACKET = new Identifier(Constants.MOD_ID, "open_spellbook");

    public static void registerC2S() {
        ServerPlayNetworking.registerGlobalReceiver(CLIENT_INVOKE_MANIA_PACKET, (server, player, handler, packetByteBuf, sender) -> {
            int amplifier = packetByteBuf.readInt();
            int duration = packetByteBuf.readInt();
            server.execute(() -> {
                        player.addStatusEffect(new StatusEffectInstance(ModRegistries.MANIA, duration, amplifier, false, true));
                        ((Sanity) player).setSanity(((Sanity) player).getSanity() - 5, false);
                        ((Sanity) player).setShocked(true);
                    }
            );
        });

        ServerPlayNetworking.registerGlobalReceiver(SYNC_SPELLCASTER_DATA_PACKET, (server, player, handler, packetByteBuf, sender) -> {
            CompoundTag tag = packetByteBuf.readCompoundTag();
            server.execute(() -> {
                if (player instanceof SpellCaster) {
                    SpellCaster caster = (SpellCaster) player;
                    caster.getSpells().clear();
                    for (int i = 0; i < tag.getList(Constants.NBT.SPELL_LIST, 10).size(); i++) {
                        caster.getSpells().add(i, Spell.fromTag((CompoundTag) tag.getList(Constants.NBT.SPELL_LIST, 10).get(i)));
                    }
                    caster.getLearnedEffects().clear();
                    tag.getList(Constants.NBT.SPELL_EFFECTS, 8).forEach(effectString -> {
                        Identifier id = new Identifier(effectString.asString());
                        if (SpellEffect.SPELL_EFFECTS.containsKey(id)) {
                            caster.getLearnedEffects().add(SpellEffect.SPELL_EFFECTS.get(id));
                        }
                    });
                    caster.getAvailableMediums().clear();
                    tag.getList(Constants.NBT.SPELL_MEDIUMS, 10).forEach(mediumTag -> {
                        Identifier id = new Identifier(((CompoundTag) mediumTag).getString(Constants.NBT.SPELL_MEDIUM));
                        if (SpellMedium.SPELL_MEDIUMS.containsKey(id)) {
                            caster.setMediumAvailability(SpellMedium.SPELL_MEDIUMS.get(id), ((CompoundTag) mediumTag).getInt("Amount"));
                        }
                    });
                }
            });
        });
    }

    public static void registerS2C() {
        ClientPlayNetworking.registerGlobalReceiver(SANITY_EXPAND_PACKET, (client, networkHandler, packetByteBuf, sender) -> {
            String name = packetByteBuf.readString();
            int amount = packetByteBuf.readInt();
            client.execute(() -> ((Sanity) client.player).addSanityCapExpansion(name, amount));
        });

        ClientPlayNetworking.registerGlobalReceiver(SANITY_REMOVE_EXPAND_PACKET, (client, networkHandler, packetByteBuf, sender) -> {
            String name = packetByteBuf.readString();
            client.execute(() -> ((Sanity) client.player).removeSanityCapExpansion(name));
        });

        ClientPlayNetworking.registerGlobalReceiver(SPELL_PACKET, (client, networkHandler, packetByteBuf, sender) -> {
            CompoundTag spellTag = packetByteBuf.readCompoundTag();
            Entity entity = client.world.getEntityById(packetByteBuf.readInt());
            if (entity instanceof LivingEntity)
                client.execute(() -> Spell.fromTag(spellTag).cast((LivingEntity) entity));
        });

        ClientPlayNetworking.registerGlobalReceiver(MOB_SPELL_MEDIUM_PACKET, (client, networkHandler, packetByteBuf, sender) -> {
            Entity mob = client.world.getEntityById(packetByteBuf.readInt());
            Entity target = client.world.getEntityById(packetByteBuf.readInt());
            SpellEffect effect = SpellEffect.SPELL_EFFECTS.get(packetByteBuf.readIdentifier());
            int intensity = packetByteBuf.readInt();
            if (mob instanceof MobEntity && target instanceof LivingEntity)
                client.execute(() -> effect.effect(client.world, (MobEntity) mob, target, target.getPos(), SpellMedium.MOB_TARGET, intensity, mob));
        });

        ClientPlayNetworking.registerGlobalReceiver(INSANITY_EVENT_PACKET, (client, networkHandler, packetByteBuf, sender) -> {
            Identifier id = packetByteBuf.readIdentifier();
            client.execute(() -> InsanityEvent.INSANITY_EVENTS.get(id).execute(client.player, (Sanity) client.player));
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

        ClientPlayNetworking.registerGlobalReceiver(EFFECT_PARTICLE_PACKET, (client, networkHandler, packetByteBuf, sender) -> {
            Vec3d pos = new Vec3d(packetByteBuf.readDouble(), packetByteBuf.readDouble(), packetByteBuf.readDouble());
            Vec3d rgb = Vec3d.unpackRgb(packetByteBuf.readInt());
            client.execute(() -> {
                client.world.addParticle(ParticleTypes.ENTITY_EFFECT, pos.x + client.world.random.nextGaussian() * 0.75F, pos.y + client.world.random.nextGaussian(), pos.z + client.world.random.nextGaussian() * 0.75F, rgb.x, rgb.y, rgb.z);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(BLOOD_PARTICLE_PACKET, (client, networkHandler, packetByteBuf, sender) -> {
            Entity entity = client.world.getEntityById(packetByteBuf.readInt());
            client.execute(() -> {
                client.world.addParticle(ModParticles.DRIPPING_BLOOD, entity.getX() + client.world.getRandom().nextGaussian() * 0.5F * entity.getWidth(), entity.getY() + client.world.getRandom().nextGaussian() * 0.5F * entity.getHeight(), entity.getZ() + client.world.getRandom().nextGaussian() * 0.5F * entity.getWidth(), 0, 0, 0);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(SYNC_SPELLCASTER_DATA_PACKET, (client, networkHandler, packetByteBuf, sender) -> {
            CompoundTag tag = packetByteBuf.readCompoundTag();
            client.execute(() -> {
                if (client.player instanceof SpellCaster) {
                    SpellCaster caster = (SpellCaster) client.player;
                    caster.getSpells().clear();
                    for (int i = 0; i < tag.getList(Constants.NBT.SPELL_LIST, 10).size(); i++) {
                        caster.getSpells().add(i, Spell.fromTag((CompoundTag) tag.getList(Constants.NBT.SPELL_LIST, 10).get(i)));
                    }
                    caster.getLearnedEffects().clear();
                    tag.getList(Constants.NBT.SPELL_EFFECTS, 8).forEach(effectString -> {
                        Identifier id = new Identifier(effectString.asString());
                        if (SpellEffect.SPELL_EFFECTS.containsKey(id)) {
                            caster.getLearnedEffects().add(SpellEffect.SPELL_EFFECTS.get(id));
                        }
                    });
                    caster.getAvailableMediums().clear();
                    tag.getList(Constants.NBT.SPELL_MEDIUMS, 10).forEach(mediumTag -> {
                        Identifier id = new Identifier(((CompoundTag) mediumTag).getString(Constants.NBT.SPELL_MEDIUM));
                        if (SpellMedium.SPELL_MEDIUMS.containsKey(id)) {
                            caster.setMediumAvailability(SpellMedium.SPELL_MEDIUMS.get(id), ((CompoundTag) mediumTag).getInt("Amount"));
                        }
                    });
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(OPEN_SPELL_EDIT_PACKET, (client, networkHandler, packetByteBuf, sender) -> {
            client.execute(() -> client.openScreen(new EditSpellScreen((SpellCaster) client.player)));
        });
    }

    public static void sendToPlayer(PlayerEntity player, PacketByteBuf data, Identifier packet) {
        if (player instanceof ServerPlayerEntity && ((ServerPlayerEntity) player).networkHandler != null)
            ServerPlayNetworking.send((ServerPlayerEntity) player, packet, data);
    }

    public static void sendToPlayers(World world, BlockPos trackPos, PacketByteBuf data, Identifier packet) {
        if (world instanceof ServerWorld) {
            PlayerLookup.tracking((ServerWorld) world, trackPos).forEach(p -> sendToPlayer(p, data, packet));
            world.getPlayers().forEach(p -> sendToPlayer(p, data, packet));
        }
    }

    public static void sendToPlayers(World world, Entity trackingEntity, PacketByteBuf data, Identifier packet) {
        if (world instanceof ServerWorld) {
            PlayerLookup.tracking(trackingEntity).forEach(p -> sendToPlayer(p, data, packet));
            world.getPlayers().forEach(p -> sendToPlayer(p, data, packet));
        }
    }

    public static void sendToPlayers(World world, BlockEntity trackingBlockEntity, PacketByteBuf data, Identifier packet) {
        if (world instanceof ServerWorld) {
            PlayerLookup.tracking(trackingBlockEntity).forEach(p -> sendToPlayer(p, data, packet));
            world.getPlayers().forEach(p -> sendToPlayer(p, data, packet));
        }
    }
}
