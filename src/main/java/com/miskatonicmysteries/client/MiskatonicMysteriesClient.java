package com.miskatonicmysteries.client;

import com.miskatonicmysteries.api.block.AltarBlock;
import com.miskatonicmysteries.api.block.OctagramBlock;
import com.miskatonicmysteries.api.block.StatueBlock;
import com.miskatonicmysteries.api.interfaces.SpellCaster;
import com.miskatonicmysteries.api.item.GunItem;
import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.client.gui.EditSpellScreen;
import com.miskatonicmysteries.client.gui.HUDHandler;
import com.miskatonicmysteries.client.model.entity.phantasma.AberrationModel;
import com.miskatonicmysteries.client.model.entity.phantasma.PhantasmaModel;
import com.miskatonicmysteries.client.particle.AmbientMagicParticle;
import com.miskatonicmysteries.client.particle.CandleFlameParticle;
import com.miskatonicmysteries.client.particle.LeakParticle;
import com.miskatonicmysteries.client.particle.ShrinkingMagicParticle;
import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.client.render.ShaderHandler;
import com.miskatonicmysteries.client.render.blockentity.AltarBlockRender;
import com.miskatonicmysteries.client.render.blockentity.ChemistrySetBlockRender;
import com.miskatonicmysteries.client.render.blockentity.OctagramBlockRender;
import com.miskatonicmysteries.client.render.blockentity.StatueBlockRender;
import com.miskatonicmysteries.client.render.entity.*;
import com.miskatonicmysteries.client.sound.ResonatorSound;
import com.miskatonicmysteries.common.handler.networking.packet.SpellPacket;
import com.miskatonicmysteries.common.handler.networking.packet.SyncSpellCasterDataPacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.*;
import com.miskatonicmysteries.common.registry.*;
import com.miskatonicmysteries.common.util.NbtUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class MiskatonicMysteriesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(MMParticles.DRIPPING_BLOOD, LeakParticle.BloodFactory::new);
        ParticleFactoryRegistry.getInstance().register(MMParticles.AMBIENT, AmbientMagicParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(MMParticles.SHRINKING_MAGIC, ShrinkingMagicParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(MMParticles.FLAME, CandleFlameParticle.Factory::new);

        ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
            for (BlockPos blockPos : ResonatorSound.soundInstances.keySet()) {
                if (ResonatorSound.soundInstances.get(blockPos).isDone()) {
                    ResonatorSound.soundInstances.remove(blockPos);
                }
            }
        });
        FabricModelPredicateProviderRegistry.register(MMObjects.RIFLE, new Identifier("loading"), (stack, world, entity) -> GunItem.isLoading(stack) ? 1 : 0);
        BlockRenderLayerMap.INSTANCE.putBlock(MMObjects.CHEMISTRY_SET, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(MMObjects.CANDLE, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(MMObjects.RESONATOR, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(MMObjects.POWER_CELL, RenderLayer.getTranslucent());

        AltarBlock.ALTARS.forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout()));
        OctagramBlock.OCTAGRAMS.forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout()));
        StatueBlock.STATUES.forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout()));
        BlockRenderLayerMap.INSTANCE.putBlock(MMObjects.YELLOW_SIGN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(MMObjects.INFESTED_WHEAT_CROP, RenderLayer.getCutout());

        BlockEntityRendererRegistry.INSTANCE.register(MMObjects.CHEMISTRY_SET_BLOCK_ENTITY_TYPE, ChemistrySetBlockRender::new);
        BlockEntityRendererRegistry.INSTANCE.register(MMObjects.ALTAR_BLOCK_ENTITY_TYPE, AltarBlockRender::new);
        BlockEntityRendererRegistry.INSTANCE.register(MMObjects.OCTAGRAM_BLOCK_ENTITY_TYPE, OctagramBlockRender::new);
        BlockEntityRendererRegistry.INSTANCE.register(MMObjects.STATUE_BLOCK_ENTITY_TYPE, StatueBlockRender::new);

        EntityRendererRegistry.INSTANCE.register(MMEntities.PROTAGONIST, (entityRenderDispatcher, context) -> new ProtagonistEntityRender(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(MMEntities.HASTUR_CULTIST, (entityRenderDispatcher, context) -> new HasturCultistEntityRender(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(MMEntities.SPELL_PROJECTILE, (entityRenderDispatcher, context) -> new SpellProjectileEntityRenderer(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(MMEntities.BOLT, (entityRenderDispatcher, context) -> new BoltEntityRenderer(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(MMEntities.PHANTASMA, (entityRenderDispatcher, context) -> new PhantasmaEntityRenderer(entityRenderDispatcher, new PhantasmaModel()));
        EntityRendererRegistry.INSTANCE.register(MMEntities.ABERRATION, (entityRenderDispatcher, context) -> new PhantasmaEntityRenderer(entityRenderDispatcher, new AberrationModel()));
        EntityRendererRegistry.INSTANCE.register(MMEntities.TATTERED_PRINCE, (entityRenderDispatcher, context) -> new TatteredPrinceRenderer(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(MMEntities.GENERIC_TENTACLE, (entityRenderDispatcher, context) -> new GenericTentacleEntityRenderer(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(MMEntities.HASTUR, (entityRenderDispatcher, context) -> new HasturEntityRenderer(entityRenderDispatcher));
        new ShaderHandler().init();

        ResourceHandler.init();
        StatueBlock.STATUES.forEach(statue -> BuiltinItemRendererRegistry.INSTANCE.register(statue.asItem(), new StatueBlockRender.BuiltinItemStatueRenderer()));
        HUDHandler.init();

        ClientPlayNetworking.registerGlobalReceiver(ExpandSanityPacket.ID, ExpandSanityPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(RemoveExpansionPacket.ID, RemoveExpansionPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(SpellPacket.ID, SpellPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(MobSpellPacket.ID, (client, networkHandler, packetByteBuf, sender) -> {
            Entity mob = client.world.getEntityById(packetByteBuf.readInt());
            Entity target = client.world.getEntityById(packetByteBuf.readInt());
            SpellEffect effect = MMRegistries.SPELL_EFFECTS.get(packetByteBuf.readIdentifier());
            int intensity = packetByteBuf.readInt();
            client.execute(() -> {
                if (mob instanceof MobEntity && target instanceof LivingEntity) {
                    effect.effect(client.world, (MobEntity) mob, target, target.getPos(), MMSpellMediums.MOB_TARGET, intensity, mob, effect.backfires((LivingEntity) mob));
                }
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(InsanityEventPacket.ID, InsanityEventPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(EffectParticlePacket.ID, EffectParticlePacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(BloodParticlePacket.ID, BloodParticlePacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(SyncSpellCasterDataPacket.ID, (client, networkHandler, packetByteBuf, sender) -> {
            CompoundTag tag = packetByteBuf.readCompoundTag();
            client.execute(() -> SpellCaster.of(client.player).ifPresent(caster -> NbtUtil.readSpellData(caster, tag)));
        });
        ClientPlayNetworking.registerGlobalReceiver(OpenSpellEditorPacket.ID, (client, networkHandler, packetByteBuf, sender) -> client.execute(() -> client.openScreen(new EditSpellScreen((SpellCaster) client.player))));
        ClientPlayNetworking.registerGlobalReceiver(TeleportEffectPacket.ID, TeleportEffectPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(SyncBlessingsPacket.ID, SyncBlessingsPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(ModifyBlessingPacket.ID, ModifyBlessingPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(SoundPacket.ID, SoundPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(SyncRiteTargetPacket.ID, SyncRiteTargetPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(SyncHeldEntityPacket.ID, SyncHeldEntityPacket::handle);
    }
}
