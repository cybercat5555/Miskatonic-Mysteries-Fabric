package com.miskatonicmysteries.common.feature.spell;

import com.miskatonicmysteries.common.handler.PacketHandler;
import com.miskatonicmysteries.common.lib.Constants;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class Spell {
    public SpellEffect effect;
    public SpellMedium medium;
    public int intensity;

    public Spell(SpellMedium medium, SpellEffect effect, int intensity) {
        this.medium = medium;
        this.effect = effect;
        this.intensity = intensity;
    }


    public boolean cast(LivingEntity caster) {
        if (!caster.world.isClient) {
            PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
            data.writeCompoundTag(toTag(new CompoundTag()));
            data.writeInt(caster.getEntityId());
            PacketHandler.sendToPlayers(caster.world, caster, data, PacketHandler.SPELL_PACKET);
        }
        return effect.canCast(caster, medium) && medium.cast(caster.world, caster, effect, intensity);
    }

    public CompoundTag toTag(CompoundTag tag) {
        tag.putString(Constants.NBT.SPELL_EFFECT, effect.getId().toString());
        tag.putString(Constants.NBT.SPELL_MEDIUM, medium.getId().toString());
        tag.putInt(Constants.NBT.INTENSITY, intensity);
        return tag;
    }

    public static Spell fromTag(CompoundTag tag) {
        return tag.isEmpty() ? null : new Spell(SpellMedium.SPELL_MEDIUMS.get(new Identifier(tag.getString(Constants.NBT.SPELL_MEDIUM))), SpellEffect.SPELL_EFFECTS.get(new Identifier(tag.getString(Constants.NBT.SPELL_EFFECT))), tag.getInt(Constants.NBT.INTENSITY));
    }
}
