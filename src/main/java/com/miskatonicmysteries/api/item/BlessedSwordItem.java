package com.miskatonicmysteries.api.item;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.api.registry.Affiliation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.text.Text;

import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.BiConsumer;

import org.jetbrains.annotations.Nullable;

public class BlessedSwordItem extends SwordItem implements Affiliated {

	protected Affiliation affiliation;
	protected BiConsumer<LivingEntity, LivingEntity> specialEffect;

	public BlessedSwordItem(Affiliation affiliation, int attackDamage, float attackSpeed,
							BiConsumer<LivingEntity, LivingEntity> specialEffect, Settings settings) {
		super(ToolMaterials.IRON, attackDamage, attackSpeed, settings);
		this.affiliation = affiliation;
		this.specialEffect = specialEffect;
	}

	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (MiskatonicMysteriesAPI.isDefiniteAffiliated(target)
			&& MiskatonicMysteriesAPI.getNonNullAffiliation(target, false) != getAffiliation(true)) {
			target.damage(DamageSource.MAGIC, 2);
		}
		if (target.getRandom().nextFloat() < 0.40F) {
			if (attacker instanceof Sanity && attacker.getRandom().nextBoolean()) {
				((Sanity) attacker).setSanity(((Sanity) attacker).getSanity() - 2, true);
			}
			specialEffect.accept(target, attacker);
		}

		return super.postHit(stack, target, attacker);
	}

	@Override
	public Affiliation getAffiliation(boolean apparent) {
		return affiliation;
	}

	@Override
	public boolean isSupernatural() {
		return true;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(Text.translatable(getTranslationKey() + ".tooltip").formatted(Formatting.ITALIC));
		super.appendTooltip(stack, world, tooltip, context);
	}
}
