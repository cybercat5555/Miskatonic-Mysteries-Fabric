package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.api.interfaces.SpellCaster;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.entity.BoltEntity;
import com.miskatonicmysteries.common.registry.MMEntities;
import com.miskatonicmysteries.common.registry.MMParticles;
import com.miskatonicmysteries.common.registry.MMSounds;
import com.miskatonicmysteries.common.registry.MMStatusEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Optional;
import java.util.Random;

public class SpellGivingRite extends AscensionLockedRite {
    private final SpellEffect grantedEffect;

    public SpellGivingRite(SpellEffect effect, String knowledge, Identifier id, @Nullable Affiliation octagram, int stage, Ingredient... ingredients) {
        super(id, octagram, knowledge, 0.25F, stage, ingredients);
        this.grantedEffect = effect;
    }

    @Override
    public boolean canCast(OctagramBlockEntity octagram) {
        if (super.canCast(octagram)) {
            Optional<SpellCaster> caster = SpellCaster.of(octagram.getOriginalCaster());
            if (caster.isPresent() && caster.get().getLearnedEffects().contains(grantedEffect)) {
                octagram.getOriginalCaster().sendMessage(new TranslatableText("message.miskatonicmysteries.spell_rite_fail"), true);
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldContinue(OctagramBlockEntity octagram) {
        return octagram.getOriginalCaster() != null;
    }

    @Override
    public boolean isFinished(OctagramBlockEntity octagram) {
        return octagram.getOriginalCaster() != null && octagram.tickCount >= 240;
    }

    @Override
    public void tick(OctagramBlockEntity octagram) {
        Vec3d pos = octagram.getSummoningPos();
        PlayerEntity player = octagram.getOriginalCaster();
        if (player == null) {
            octagram.setOriginalCaster(null);
            onCancelled(octagram);
        }
        Vec3d motionVec = new Vec3d(pos.x - player.getX(), pos.y - player.getY(), pos.z - player.getZ());
        motionVec = motionVec.multiply(0.5F);
        player.setVelocity(motionVec.add(0, player.getVelocity().y, 0));
        if (player.squaredDistanceTo(pos) < 4) {
            octagram.tickCount++;
            spawnParticles(octagram.getWorld(), player.getPos(), player.getRandom(), octagram.tickCount, player);
            if (octagram.tickCount % 40 == 1) {
                player.playSound(MMSounds.MAGIC, 0.6F, 1.3F);
            }
        } else if (player.squaredDistanceTo(pos) > 100) {
            octagram.tickCount = 0;
            octagram.setOriginalCaster(null);
            onCancelled(octagram);
            return;
        }
    }

    @Override
    public void onFinished(OctagramBlockEntity octagram) {
        PlayerEntity player = octagram.getOriginalCaster();
        player.setVelocity(0, 0.5F, 0);
        player.playSound(MMSounds.MAGIC, 1, (float) (1 + player.getRandom().nextGaussian() * 0.2F));
        if (octagram.getWorld().isClient) {
            for (int i = 0; i < 20; i++) {
                MMParticles.spawnCandleParticle(octagram.getWorld(), player.getX() + octagram.getWorld().random.nextGaussian() * player.getWidth(), player.getY() + octagram.getWorld().random.nextFloat() * player.getHeight(), player.getZ() + octagram.getWorld().random.nextFloat() * player.getWidth(), 1.5F, true);
            }
        } else {
            Vec3d pos = player.getPos();
            for (int i = 0; i < 5; i++) {
                spawnBolt(player, pos, octagram.getWorld(), 0.5F, player.getRandom());
            }
            player.addStatusEffect(new StatusEffectInstance(MMStatusEffects.MANIA, 100 + octagram.getWorld().random.nextInt(200), 1));
            Sanity.of(player).ifPresent(sanity -> {
                int reductionAmount = 15 - player.getRandom().nextInt(8);
                sanity.addSanityCapExpansion("spell." + grantedEffect.getId().getPath(), -reductionAmount);
            });
            SpellCaster.of(player).ifPresent(caster -> {
                caster.learnEffect(grantedEffect);
                caster.syncSpellData();
            });
        }
        super.onFinished(octagram);
    }

    private void spawnBolt(PlayerEntity player, Vec3d pos, World world, float height, Random random) {
        BoltEntity bolt = MMEntities.BOLT.create(world);
        bolt.setPos(pos.x, pos.y + height, pos.z);
        bolt.setYaw(random.nextInt(360));
        bolt.setPitch(random.nextInt(360));
        bolt.setColor(grantedEffect.getColor(player));
        world.spawnEntity(bolt);
    }

    private void spawnParticles(World world, Vec3d pos, Random random, int ticks, PlayerEntity player) {
        if (!world.isClient) {
            if (ticks % 40 == 0) {
                for (int i = 0; i < 2; i++) {
                    spawnBolt(player, pos, world, 1.5F, random);
                }
            }
        } else {
            Color color = new Color(grantedEffect.getColor(player));
            double rad = Math.PI * (ticks / 20F);
            double y;
            if (ticks % 40 > 20) {
                y = 1 - ((ticks % 20) / 20F);
            } else {
                y = (ticks % 20) / 20F;
            }
            world.addParticle(MMParticles.SHRINKING_MAGIC, pos.x + Math.sin(rad), pos.y + y, pos.z + Math.cos(rad), color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F);
            world.addParticle(MMParticles.SHRINKING_MAGIC, pos.x + Math.sin(rad + Math.PI), pos.y + y, pos.z + Math.cos(rad + Math.PI), color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F);
        }
    }
}
