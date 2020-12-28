package com.miskatonicmysteries.common.feature.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public abstract class SpellMedium {
    public static final Map<Identifier, SpellMedium> SPELL_MEDIUMS = new HashMap<>();

    private final Identifier id;

    public SpellMedium(Identifier id) {
        this.id = id;
    }

    public Identifier getId() {
        return id;
    }

    public abstract boolean cast(World world, LivingEntity caster, SpellEffect effect, int intensity);
}
