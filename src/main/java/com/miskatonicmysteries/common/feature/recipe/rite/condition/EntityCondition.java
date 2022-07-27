package com.miskatonicmysteries.common.feature.recipe.rite.condition;

import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Predicate;

public class EntityCondition<T extends Entity> extends RiteCondition{
	protected final Identifier id;
	private final Class<T> entityClass;
	private final int amount;
	private final Predicate<T> predicate;
	protected int checkedAmount;
	public EntityCondition(Identifier id, Class<T> entityClass, int amount, Predicate<T> predicate) {
		super(id);
		this.id = id;
		this.entityClass = entityClass;
		this.amount = amount;
		this.predicate = predicate;
	}

	@Override
	public boolean test(OctagramBlockEntity octagramBlockEntity) {
		List<T> entities = octagramBlockEntity.getWorld()
			.getEntitiesByClass(entityClass, octagramBlockEntity.getSelectionBox().expand(15, 5, 15), predicate);
		this.checkedAmount = entities.size();
		return checkedAmount >= amount;
	}

	@Override
	protected void sendFailMessage(PlayerEntity caster) {
		caster.sendMessage(new TranslatableText(String.format("message.%s.rite_fail.%s", id.getNamespace(), id.getPath()), amount, checkedAmount), true);
	}
}
