package com.miskatonicmysteries.common.handler.networking.packet;

import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.feature.spell.SpellCaster;
import com.miskatonicmysteries.common.feature.spell.SpellEffect;
import com.miskatonicmysteries.common.feature.spell.SpellMedium;
import com.miskatonicmysteries.common.handler.networking.PacketHandler;
import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.util.CapabilityUtil;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class SyncSpellCasterDataPacket {
    public static final Identifier ID = new Identifier(Constants.MOD_ID, "sync_spell");

    public static void send(boolean client, PlayerEntity user, SpellCaster caster) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        CompoundTag spellCompound = CapabilityUtil.writeSpellData(caster, new CompoundTag());
        data.writeCompoundTag(spellCompound);
        if (client) {
            ClientPlayNetworking.send(ID, data);
        } else {
            PacketHandler.sendToPlayer(user, data, ID);
        }
    }

    public static void handleFromClient(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf packetByteBuf, PacketSender sender) {
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
    }

    public static void handleFromServer(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf, PacketSender sender) {
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
    }
}
