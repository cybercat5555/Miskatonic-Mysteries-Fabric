package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.api.registry.Rite;
import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.registry.MMParticles;
import com.miskatonicmysteries.common.registry.MMSounds;
import com.miskatonicmysteries.common.registry.MMStatusEffects;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.mixin.ZombieVillagerAccessor;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;

public class MourningDeadRite extends Rite {
    public MourningDeadRite() {
        super(new Identifier(Constants.MOD_ID, "mourning_dead"), null, 0.1F, Ingredient.ofItems(Items.GHAST_TEAR), Ingredient.ofStacks(Arrays.stream(new ItemStack[]{PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD)})), Ingredient.ofItems(Items.BLAZE_POWDER), Ingredient.ofItems(Items.CHARCOAL), Ingredient.ofItems(Items.PHANTOM_MEMBRANE));
    }

    @Override
    public void tick(OctagramBlockEntity octagram) {
        if (octagram.getWorld().isClient) {
            Vec3d position = octagram.getSummoningPos().add(octagram.getWorld().random.nextGaussian() * 5, -0.25 + octagram.getWorld().random.nextFloat() * 5, octagram.getWorld().random.nextGaussian() * 5);
            octagram.getWorld().addParticle(MMParticles.AMBIENT, position.x, position.y, position.z, 1F, 1F, 1F);
        }
        super.tick(octagram);
    }

    @Override
    public boolean isFinished(OctagramBlockEntity octagram) {
        return octagram.tickCount > 80;
    }

    @Override
    public void onFinished(OctagramBlockEntity octagram) {
        octagram.getWorld().playSound(null, octagram.getPos(), MMSounds.PRIMED_RITE_TRIGGERED, SoundCategory.AMBIENT, 1.0F, (float) octagram.getWorld().random.nextGaussian() * 0.2F + 1.0F);

        octagram.getWorld().getEntitiesByClass(MobEntity.class, octagram.getSelectionBox().expand(10, 10, 10), mob -> mob.getGroup() == EntityGroup.UNDEAD)
                .forEach(mob -> {
                    if (mob.world.isClient) {
                        for (int i = 0; i < 7; i++) {
                            mob.world.addParticle(MMParticles.AMBIENT, mob.getParticleX(1), mob.getRandomBodyY(), mob.getParticleX(1), 1F, 1F, 1F);
                        }
                    }
                    if (mob instanceof ZombieVillagerEntity && mob.getRandom().nextBoolean()){
                        ((ZombieVillagerAccessor) mob).callSetConverting(octagram.originalCaster, 20);
                    }else{
                        mob.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 10, false, false, false));
                    }
                });
        super.onFinished(octagram);
    }
}
