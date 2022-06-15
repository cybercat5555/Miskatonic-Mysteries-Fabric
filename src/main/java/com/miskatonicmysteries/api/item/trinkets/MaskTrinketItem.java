package com.miskatonicmysteries.api.item.trinkets;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.interfaces.MalleableAffiliated;
import com.miskatonicmysteries.api.registry.Affiliation;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;

import java.util.List;
import java.util.Optional;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;

public class MaskTrinketItem extends TrinketItem implements Affiliated {

	private final Affiliation affiliation;
	private final boolean verySpecial;

	public MaskTrinketItem(Affiliation affiliation, boolean verySpecial, Settings settings) {
		super(settings);
		this.affiliation = affiliation;
		this.verySpecial = verySpecial;
	}


	public static ItemStack getMask(PlayerEntity player) {
		Optional<List<Pair<SlotReference, ItemStack>>> masks = TrinketsApi.getTrinketComponent(player)
			.map(component ->
					 component.getEquipped(stack -> stack.getItem() instanceof MaskTrinketItem));
		if (masks.isPresent() && !masks.get().isEmpty()) {
			return masks.get().get(0).getRight();
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		MalleableAffiliated.of(entity).ifPresent(affiliation -> affiliation.setAffiliation(this.affiliation, true));
	}

	@Override
	public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		Affiliation apparentAffiliation = MiskatonicMysteriesAPI.getApparentAffiliationFromEquipment(stack, entity);
		MalleableAffiliated.of(entity).ifPresent(affiliation -> affiliation.setAffiliation(apparentAffiliation, true));
	}

	@Override
	public Affiliation getAffiliation(boolean apparent) {
		return affiliation;
	}

	@Override
	public boolean isSupernatural() {
		return verySpecial;
	}
}
