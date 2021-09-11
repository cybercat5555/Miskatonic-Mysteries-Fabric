package com.miskatonicmysteries.common.feature.recipe.instability_event;

import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.entity.BoltEntity;
import com.miskatonicmysteries.common.registry.MMEntities;
import com.miskatonicmysteries.common.registry.MMSounds;
import com.miskatonicmysteries.common.registry.MMSpellEffects;
import com.miskatonicmysteries.common.registry.MMSpellMediums;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class SpellEffectInstabilityEvent extends InstabilityEvent {
    public SpellEffectInstabilityEvent() {
        super(new Identifier(Constants.MOD_ID, "spell"), 0.2F, 0.4F);
    }

    @Override
    public boolean cast(OctagramBlockEntity blockEntity, float instability) {
        World world = blockEntity.getWorld();
        Random random = world.getRandom();
        List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, blockEntity.getSelectionBox()
                .expand(5), (entity) -> true);
        for (LivingEntity victim : entities) {
            BoltEntity bolt = MMEntities.BOLT.create(world);
            bolt.setPosition(blockEntity.getSummoningPos());
            bolt.setYaw(random.nextInt(360));
            bolt.setPitch(-random.nextInt(180));
            SpellEffect effect = pickSpellEffect(random, instability);
            bolt.setColor(effect.getColor(victim));
            effect.effect(world, victim, victim, victim.getPos(), MMSpellMediums.BOLT, 0, null);
            world.spawnEntity(bolt);
            if (random.nextFloat() > instability) {
                break;
            }
        }
        world.playSound(null, blockEntity.getPos(),  MMSounds.MAGIC, SoundCategory.NEUTRAL, MathHelper.nextGaussian(random, 1, 0.2F), MathHelper.nextGaussian(random, 1, 0.2F));
        return false;
    }

    private SpellEffect pickSpellEffect(Random random, float instability) {
        int selectionNum = random.nextInt(4);
        return switch (selectionNum) {
            case 1 -> MMSpellEffects.KNOCKBACK;
            case 2 -> MMSpellEffects.IGNITE;
            case 3 -> MMSpellEffects.HEAL;
            default -> MMSpellEffects.DAMAGE;
        };
    }
}
