package com.miskatonicmysteries.common.item;

import com.miskatonicmysteries.common.feature.Affiliated;
import com.miskatonicmysteries.common.feature.Affiliation;
import com.miskatonicmysteries.common.feature.sanity.Sanity;
import com.miskatonicmysteries.common.lib.util.CapabilityUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;

import java.util.function.BiConsumer;

public class BlessedSwordItem extends SwordItem implements Affiliated {
    protected Affiliation affiliation;
    protected BiConsumer<LivingEntity, LivingEntity> specialEffect;

    public BlessedSwordItem(Affiliation affiliation, int attackDamage, float attackSpeed, BiConsumer<LivingEntity, LivingEntity> specialEffect, Settings settings) {
        super(ToolMaterials.IRON, attackDamage, attackSpeed, settings);
        this.affiliation = affiliation;
        this.specialEffect = specialEffect;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (CapabilityUtil.isAffiliated(target) && CapabilityUtil.getAffiliation(target, false) != getAffiliation(true)) {
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
}
