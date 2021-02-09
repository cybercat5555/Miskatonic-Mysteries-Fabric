package com.miskatonicmysteries.api.item.trinkets;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.interfaces.MalleableAffiliated;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.common.util.Constants;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class MaskTrinketItem extends TrinketItem implements Affiliated {
    private Identifier texture;
    private Affiliation affiliation;
    private boolean verySpecial;

    public MaskTrinketItem(Settings settings, Identifier texture, Affiliation affiliation, boolean verySpecial) {
        super(settings.group(Constants.MM_GROUP).maxCount(1));
        this.texture = texture;
        this.affiliation = affiliation;
        this.verySpecial = verySpecial;
    }


    public static ItemStack getMask(PlayerEntity player) {
        for (int i = 0; i < TrinketsApi.getTrinketsInventory(player).size(); i++) {
            ItemStack stack = TrinketsApi.getTrinketsInventory(player).getStack(i);
            if (stack.getItem() instanceof MaskTrinketItem) return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void onEquip(PlayerEntity player, ItemStack stack) {
        MalleableAffiliated.of(player).ifPresent(affiliation -> affiliation.setAffiliation(this.affiliation, true));
    }

    @Override
    public void onUnequip(PlayerEntity player, ItemStack stack) {
        //update affiliation based on equipment left
        Affiliation apparentAffiliation = MiskatonicMysteriesAPI.getApparentAffiliationFromEquipment(stack, player);
        MalleableAffiliated.of(player).ifPresent(affiliation -> affiliation.setAffiliation(apparentAffiliation, true));
    }

    @Override
    public boolean canWearInSlot(String group, String slot) {
        return slot.equals(Slots.MASK);
    }

    @Override
    public void render(String slot, MatrixStack matrixStack, VertexConsumerProvider vertexConsumer, int light, PlayerEntityModel<AbstractClientPlayerEntity> model, AbstractClientPlayerEntity player, float headYaw, float headPitch) {
        Trinket.translateToFace(matrixStack, model, player, headYaw, headPitch);
        matrixStack.translate(0, 0.25, 0.3);
        ResourceHandler.getMaskModel(this).render(matrixStack, vertexConsumer.getBuffer(RenderLayer.getEntityCutout(texture)), light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
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
