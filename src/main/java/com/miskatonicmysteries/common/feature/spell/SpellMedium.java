package com.miskatonicmysteries.common.feature.spell;

import com.miskatonicmysteries.common.feature.spell.medium.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

import static com.miskatonicmysteries.common.lib.MMMiscRegistries.addSpellMedium;

public abstract class SpellMedium {
    public static final Map<Identifier, SpellMedium> SPELL_MEDIUMS = new HashMap<>();

    public static final SpellMedium SELF = addSpellMedium(new SelfSpellMedium());
    public static final SpellMedium VISION = addSpellMedium(new VisionSpellMedium());
    public static final SpellMedium BOLT = addSpellMedium(new BoltSpellMedium());
    public static final SpellMedium GROUP = addSpellMedium(new GroupSpellMedium());
    public static final SpellMedium MOB_TARGET = addSpellMedium(new MobTargetMedium());
    public static final SpellMedium PROJECTILE = addSpellMedium(new ProjectileSpellMedium());

    private final Identifier id;

    public SpellMedium(Identifier id) {
        this.id = id;
    }

    public Identifier getId() {
        return id;
    }

    public Identifier getTextureLocation() {
        return new Identifier(id.getNamespace(), "textures/gui/spell_widgets/medium/" + id.getPath() + ".png");
    }

    public abstract boolean cast(World world, LivingEntity caster, SpellEffect effect, int intensity);
}
