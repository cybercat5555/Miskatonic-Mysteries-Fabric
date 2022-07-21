package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.recipe.RiteRecipe;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.registry.MMParticles;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.InventoryUtil;

import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class EnchantCanvasRite extends AscensionLockedRite {

	public EnchantCanvasRite() {
		super(new Identifier(Constants.MOD_ID, "enchant_canvas"), MMAffiliations.HASTUR, "", 0.05F, 2);
	}

	@Override
	public boolean canCast(OctagramBlockEntity octagram, RiteRecipe baseRecipe) {
		List<Ingredient> checkList = baseRecipe.ingredients.stream().filter(i -> !i.test(new ItemStack(Items.PAINTING))).collect(Collectors.toList());
		List<ItemStack> checkItems = new ArrayList<>(octagram.getItems());
		for (Ingredient ingredient : checkList) {
			for (ItemStack item : List.copyOf(checkItems)) {
				if (ingredient.test(item)) {
					checkItems.remove(item);
					break;
				}
			}
		}
		if (checkItems.isEmpty()) {
			return false;
		} else {
			for (ItemStack item : checkItems) {
				if (item.getItem() != Items.PAINTING) {
					return false;
				}
			}
		}
		return super.canCast(octagram, baseRecipe);
	}

	@Override
	public void tick(OctagramBlockEntity octagram) {
		super.tick(octagram); //idk some particles
		World world = octagram.getWorld();
		if (world.isClient) {
			Random random = world.getRandom();
			Vec3d pos = octagram.getSummoningPos();
			if (random.nextFloat() < 0.1F) {
				world.addParticle(MMParticles.AMBIENT_MAGIC,
								  pos.getX() + random.nextGaussian(),
								  pos.getY() + random.nextFloat() * 2,
								  pos.getZ() + random.nextGaussian(),
								  0, 0, 0);
			}
			if (random.nextFloat() < 0.25F) {
				for (int i = 0; i < 8; i++) {
					Item item = octagram.getItems().get(i).getItem();
					if (item == Items.PAINTING) {
						double rad = -Math.PI * 2 * (i / 8.0F) + random.nextGaussian() * 0.25F;
						world.addParticle(ParticleTypes.ENCHANT, pos.x - Math.sin(rad) * 1.5F, pos.y + 0.1, pos.z - Math.cos(rad) * 1.5F,
										  Math.sin(rad) * 1.5F, 0, Math.cos(rad) * 1.5F);

					} else if (item instanceof DyeItem) {
						double rad = -Math.PI * 2 * (i / 8.0F) + random.nextGaussian() * 0.1F;
						world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, item.getDefaultStack()),
										  pos.x - Math.sin(rad), pos.y + 0.1, pos.z - Math.cos(rad),
										  Math.sin(rad) * 0.1F, 0.1, Math.cos(rad) * 0.1F);
					}
				}
			}
		}
	}

	@Override
	public boolean isFinished(OctagramBlockEntity octagram) {
		return octagram.tickCount > 100;
	}

	@Override
	public void onFinished(OctagramBlockEntity octagram) {
		List<Integer> paintingIndexes = octagram.getItems().stream().filter(item -> item.getItem() == Items.PAINTING)
			.map(stack -> octagram.getItems().indexOf(stack)).collect(Collectors.toList());
		World world = octagram.getWorld();
		super.onFinished(octagram);
		world.playSound(octagram.getPos().getX(), octagram.getPos().getY(), octagram.getPos().getZ(),
						SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1, 1, false);
		if (!world.isClient) {
			for (Integer index : paintingIndexes) {
				octagram.getItems().set(index, new ItemStack(MMObjects.ENCHANTED_CANVAS, 1));
			}
		}
		octagram.markDirty();
	}
}
