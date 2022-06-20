package com.miskatonicmysteries.common.feature.item;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.client.MiskatonicMysteriesClient;
import com.miskatonicmysteries.common.feature.entity.HasturCultistEntity;
import com.miskatonicmysteries.common.feature.entity.TatteredPrinceEntity;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMSounds;
import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.List;

import org.jetbrains.annotations.Nullable;

public class HasturBellItem extends Item {

	public HasturBellItem() {
		super(new FabricItemSettings().group(Constants.MM_GROUP).maxCount(1).rarity(Rarity.UNCOMMON));

	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		MutableText text = new TranslatableText("item.%s.%s.tooltip".formatted(Constants.MOD_ID, Registry.ITEM.getId(this).getPath()));
		text.formatted(Formatting.GRAY);
		if (world != null && world.isClient) {
			ClientPlayerEntity player = MinecraftClient.getInstance().player;
			if (MiskatonicMysteriesAPI.getNonNullAffiliation(player, false) != MMAffiliations.HASTUR
			|| MiskatonicMysteriesAPI.getAscensionStage(player) < 1) {
				text = new TranslatableText("item.%s.%s.tooltip.alternate".formatted(Constants.MOD_ID, Registry.ITEM.getId(this).getPath()));
				text.fillStyle(Style.EMPTY.withFont(MiskatonicMysteriesClient.OBFUSCATED_FONT_ID));
				text.formatted(Formatting.YELLOW);
			}
		}
		tooltip.add(text);
		super.appendTooltip(stack, world, tooltip, context);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		world.playSound(null, user.getX(), user.getY(), user.getZ(), MMSounds.ITEM_BELL_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
		world.addParticle(ParticleTypes.NOTE,
						  user.getParticleX(1), user.getEyeY(), user.getParticleZ(1),
						  0.08125, 0, 0);
		if (!world.isClient && MiskatonicMysteriesAPI.getNonNullAffiliation(user, false) == MMAffiliations.HASTUR
			&& MiskatonicMysteriesAPI.getAscensionStage(user) > 0) {
			user.getItemCooldownManager().set(this, 20);
			List<PathAwareEntity> entities = world.getEntitiesByClass(PathAwareEntity.class, user.getBoundingBox().expand(16, 10, 16),
																	  entity -> !entity.isAttacking() && (entity instanceof HasturCultistEntity
																			  || entity instanceof TatteredPrinceEntity));
			for (PathAwareEntity entity : entities) {
				entity.getNavigation().startMovingTo(user, entity instanceof HasturCultistEntity ? 0.5F : 0.8F);
			}
		}
		return TypedActionResult.success(user.getStackInHand(hand), true);
	}
}
