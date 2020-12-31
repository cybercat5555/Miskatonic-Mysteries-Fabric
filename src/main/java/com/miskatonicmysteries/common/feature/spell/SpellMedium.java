package com.miskatonicmysteries.common.feature.spell;

import com.miskatonicmysteries.common.feature.spell.medium.GroupSpellMedium;
import com.miskatonicmysteries.common.feature.spell.medium.MobTargetMedium;
import com.miskatonicmysteries.common.feature.spell.medium.SelfSpellMedium;
import com.miskatonicmysteries.common.feature.spell.medium.VisionSpellMedium;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

import static com.miskatonicmysteries.common.lib.ModRegistries.addSpellMedium;

public abstract class SpellMedium {
    public static final Map<Identifier, SpellMedium> SPELL_MEDIUMS = new HashMap<>();

    public static final SpellMedium SELF = addSpellMedium(new SelfSpellMedium());
    public static final SpellMedium VISION = addSpellMedium(new VisionSpellMedium());
    public static final SpellMedium GROUP = addSpellMedium(new GroupSpellMedium());
    public static final SpellMedium MOB_TARGET = addSpellMedium(new MobTargetMedium());

    private final Identifier id;

    public SpellMedium(Identifier id) {
        this.id = id;
    }

    public Identifier getId() {
        return id;
    }

    public abstract boolean cast(World world, LivingEntity caster, SpellEffect effect, int intensity);
}
