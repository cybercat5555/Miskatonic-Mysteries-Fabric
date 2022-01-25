package com.miskatonicmysteries.common.feature.recipe.rite.summon;

import com.miskatonicmysteries.client.render.entity.TatteredPrinceRenderer;
import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.entity.HasturCultistEntity;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMEntities;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.registry.MMParticles;
import com.miskatonicmysteries.common.registry.MMSounds;
import com.miskatonicmysteries.common.registry.MMSpellEffects;
import com.miskatonicmysteries.common.registry.MMSpellMediums;
import com.miskatonicmysteries.common.util.Constants;
import java.util.List;
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

public class PrinceSummoningRite extends SummoningRite {

	public PrinceSummoningRite() {
		super(new Identifier(Constants.MOD_ID, "summon_prince"), MMAffiliations.HASTUR, MMAffiliations.HASTUR.getId().getPath(), 0.75F, 0,
			MMEntities.TATTERED_PRINCE,
			//Ingredient.ofItems(MMObjects.INCANTATION_YOG), Ingredient.ofItems(MMObjects.YELLOW_SIGN_LOOM_PATTERN), TODO BANNER
			Ingredient.ofItems(Items.ANCIENT_DEBRIS), Ingredient.fromTag(Constants.Tags.OCEANIC_GOLD_BLOCKS_ITEM),
			Ingredient.ofItems(Items.GOLDEN_HELMET), Ingredient.ofItems(Items.DIAMOND),
			Ingredient.fromTag(Constants.Tags.HASTUR_CULTIST_OFFERINGS), Ingredient.ofItems(MMObjects.ORNATE_DAGGER));
	}

	@Override
	public boolean canCast(OctagramBlockEntity octagram) {
		if (super.canCast(octagram) && octagram.getOriginalCaster() != null) {
			World world = octagram.getWorld();
			PlayerEntity originalCaster = octagram.getOriginalCaster();
			if (originalCaster.isCreative()) {
				return true;
			}
			List serfs = world.getEntitiesByClass(HasturCultistEntity.class, octagram.getSelectionBox().expand(10, 10, 10),
				cultist -> cultist.isLoyalTo(originalCaster));
			if (serfs.size() < 4) {
				originalCaster
					.sendMessage(new TranslatableText("message.miskatonicmysteries.summon_prince_fail.cultists", serfs.size()), true);
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public void tick(OctagramBlockEntity octagram) {
		World world = octagram.getWorld();
		Vec3d pos = octagram.getSummoningPos();
		if (!octagram.getFlag(0)) {
			if (world.isClient) {
				world.addParticle(MMParticles.DRIPPING_BLOOD, pos.x + world.random.nextGaussian(),
					pos.y - 0.25F + world.random.nextFloat() * 2, pos.z + world.random.nextGaussian(), 0, 0.1F, 0);
			}
		} else {
			if (octagram.tickCount == 0) {
				world.playSound(null, pos.x, pos.y, pos.z, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 1, 1);
			}
			if (world.isClient) {
				Vec3d particlePos = pos
					.add(world.random.nextGaussian() * 2, -0.25F + world.random.nextFloat() * 5, world.random.nextGaussian() * 2);
				world.addParticle(MMParticles.AMBIENT, particlePos.x, particlePos.y, particlePos.z, 1,
					0.75 + world.random.nextFloat() * 0.25F, world.random.nextFloat() * 0.1F);
			}
			super.tick(octagram);
		}
		List<HasturCultistEntity> cultists = octagram.getWorld()
			.getEntitiesByClass(HasturCultistEntity.class, octagram.getSelectionBox().expand(10, 10, 10),
				cultist -> !cultist.isAttacking());
		for (HasturCultistEntity cultist : cultists) {
			cultist.getNavigation().startMovingTo(pos.x, pos.y, pos.z, 0.8F);
			if (cultist.getPos().distanceTo(pos) < 5) {
				cultist.getNavigation().stop();
				cultist.currentSpell = null;
				cultist.setCastTime(20);
			}
		}
	}

	@Override
	public void onFinished(OctagramBlockEntity octagram) {
		World world = octagram.getWorld();
		Vec3d pos = octagram.getSummoningPos();
		octagram.getWorld().playSound(null, pos.x, pos.y, pos.z, MMSounds.BROKE_VEIL_SPAWN, SoundCategory.PLAYERS, 1, 1);
		if (world.isClient) {
			for (int i = 0; i < 100; i++) {
				Vec3d particlePos = pos
					.add(world.random.nextGaussian() * 1.5F, -0.25F + world.random.nextFloat() * 5, world.random.nextGaussian() * 1.5F);
				world.addParticle(ParticleTypes.LARGE_SMOKE, particlePos.x, particlePos.y, particlePos.z, 0, 0, 0);
			}
		}
		List<HasturCultistEntity> cultists = octagram.getWorld()
			.getEntitiesByClass(HasturCultistEntity.class, octagram.getSelectionBox().expand(10, 10, 10),
				cultist -> !cultist.isAttacking());
		for (HasturCultistEntity cultist : cultists) {
			cultist.getNavigation().startMovingTo(pos.x, pos.y, pos.z, 0.8F);
			if (cultist.getPos().distanceTo(pos) < 5) {
				cultist.getNavigation().stop();
				cultist.currentSpell = new Spell(MMSpellMediums.GROUP, MMSpellEffects.HEAL, 0);
				cultist.setCastTime(20);
			}
		}
		super.onFinished(octagram);
	}

	@Override
	protected Model getRenderedModel(OctagramBlockEntity entity) {
		return TatteredPrinceRenderer.dummyPrinceModel;
	}
}
