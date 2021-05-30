package com.miskatonicmysteries.common.feature.recipe.rite.summon;

import com.miskatonicmysteries.client.render.entity.ByakheeEntityRenderer;
import com.miskatonicmysteries.client.render.entity.TatteredPrinceRenderer;
import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.entity.HasturCultistEntity;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.registry.*;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.client.model.Model;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ByakheeSummoningRite extends SummoningRite {
    public ByakheeSummoningRite() {
        super(new Identifier(Constants.MOD_ID, "summon_byakhee"), MMAffiliations.HASTUR, MMAffiliations.HASTUR.getId().getPath(), 0.25F, 1, MMEntities.BYAKHEE,
                Ingredient.ofItems(MMObjects.INCANTATION_YOG), Ingredient.ofItems(Items.PHANTOM_MEMBRANE), Ingredient.ofItems(Items.PHANTOM_MEMBRANE), Ingredient.ofItems(Items.PHANTOM_MEMBRANE),
                Ingredient.fromTag(Constants.Tags.OCEANIC_GOLD_BLOCKS_ITEM), Ingredient.ofItems(Items.DIAMOND), Ingredient.ofItems(Items.EMERALD), Ingredient.ofItems(Items.ENDER_EYE));
    }

    @Override
    public void tick(OctagramBlockEntity octagram) {
        World world = octagram.getWorld();
        Vec3d pos = octagram.getSummoningPos();
        if (!octagram.getFlag(0)) {
            if (world.isClient) {
                world.addParticle(MMParticles.DRIPPING_BLOOD, pos.x + world.random.nextGaussian(), pos.y - 0.25F + world.random.nextFloat() * 2, pos.z + world.random.nextGaussian(), 0, 0.1F, 0);
            }
        } else {
            if (octagram.tickCount == 0) {
                world.playSound(null, pos.x, pos.y, pos.z, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 1, 1);
            }
            if (world.isClient) {
                Vec3d particlePos = pos.add(world.random.nextGaussian() * 2, -0.25F + world.random.nextFloat() * 5, world.random.nextGaussian() * 2);
                world.addParticle(MMParticles.AMBIENT, particlePos.x, particlePos.y, particlePos.z, 1, 0.75 + world.random.nextFloat() * 0.25F, world.random.nextFloat() * 0.1F);
            }
            super.tick(octagram);
        }
    }

    @Override
    public void onFinished(OctagramBlockEntity octagram) {
        World world = octagram.getWorld();
        Vec3d pos = octagram.getSummoningPos();
        octagram.getWorld().playSound(null, pos.x, pos.y, pos.z, MMSounds.BROKE_VEIL_SPAWN, SoundCategory.PLAYERS, 1, 1);
        if (world.isClient) {
            for (int i = 0; i < 100; i++) {
                Vec3d particlePos = pos.add(world.random.nextGaussian() * 1.5F, -0.25F + world.random.nextFloat() * 5, world.random.nextGaussian() * 1.5F);
                world.addParticle(ParticleTypes.LARGE_SMOKE, particlePos.x, particlePos.y, particlePos.z, 0, 0, 0);
            }
        }
        super.onFinished(octagram);
    }

    @Override
    protected Model getRenderedModel(OctagramBlockEntity entity) {
        return ByakheeEntityRenderer.MODEL;
    }
}
