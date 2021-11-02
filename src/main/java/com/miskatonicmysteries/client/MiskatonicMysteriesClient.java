package com.miskatonicmysteries.client;

import com.miskatonicmysteries.api.block.AltarBlock;
import com.miskatonicmysteries.api.block.OctagramBlock;
import com.miskatonicmysteries.api.block.StatueBlock;
import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.interfaces.Ascendant;
import com.miskatonicmysteries.api.interfaces.Knowledge;
import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.api.interfaces.SpellCaster;
import com.miskatonicmysteries.api.item.GunItem;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.client.gui.EditSpellScreen;
import com.miskatonicmysteries.client.gui.SpellClientHandler;
import com.miskatonicmysteries.client.model.MMModels;
import com.miskatonicmysteries.client.model.armor.HasturMaskModel;
import com.miskatonicmysteries.client.model.armor.ShubAlternateMaskModel;
import com.miskatonicmysteries.client.model.armor.ShubMaskModel;
import com.miskatonicmysteries.client.model.entity.phantasma.AberrationModel;
import com.miskatonicmysteries.client.model.entity.phantasma.PhantasmaModel;
import com.miskatonicmysteries.client.particle.AmbientMagicParticle;
import com.miskatonicmysteries.client.particle.CandleFlameParticle;
import com.miskatonicmysteries.client.particle.LeakParticle;
import com.miskatonicmysteries.client.particle.ResonatorCreatureParticle;
import com.miskatonicmysteries.client.particle.ShrinkingMagicParticle;
import com.miskatonicmysteries.client.particle.WeirdCubeParticle;
import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.client.render.ShaderHandler;
import com.miskatonicmysteries.client.render.blockentity.AltarBlockRender;
import com.miskatonicmysteries.client.render.blockentity.ChemistrySetBlockRender;
import com.miskatonicmysteries.client.render.blockentity.MasterpieceStatueBlockRender;
import com.miskatonicmysteries.client.render.blockentity.OctagramBlockRender;
import com.miskatonicmysteries.client.render.blockentity.StatueBlockRender;
import com.miskatonicmysteries.client.render.entity.BoltEntityRenderer;
import com.miskatonicmysteries.client.render.entity.ByakheeEntityRenderer;
import com.miskatonicmysteries.client.render.entity.GenericTentacleEntityRenderer;
import com.miskatonicmysteries.client.render.entity.HallucinationRenderer;
import com.miskatonicmysteries.client.render.entity.HarrowEntityRenderer;
import com.miskatonicmysteries.client.render.entity.HasturCultistEntityRender;
import com.miskatonicmysteries.client.render.entity.PhantasmaEntityRenderer;
import com.miskatonicmysteries.client.render.entity.ProtagonistEntityRender;
import com.miskatonicmysteries.client.render.entity.SpellProjectileEntityRenderer;
import com.miskatonicmysteries.client.render.entity.TatteredPrinceRenderer;
import com.miskatonicmysteries.client.render.entity.TindalosHoundEntityRenderer;
import com.miskatonicmysteries.client.render.equipment.CultistRobesArmorRenderer;
import com.miskatonicmysteries.client.render.equipment.MaskTrinketRenderer;
import com.miskatonicmysteries.client.vision.VisionHandler;
import com.miskatonicmysteries.common.handler.networking.packet.SpellPacket;
import com.miskatonicmysteries.common.handler.networking.packet.SyncSpellCasterDataPacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.BloodParticlePacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.EffectParticlePacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.ExpandSanityPacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.InsanityEventPacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.MobSpellPacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.ModifyBlessingPacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.OpenSpellEditorPacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.RemoveExpansionPacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.SoundPacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.SyncBiomeMaskPacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.SyncBlessingsPacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.SyncHeldEntityPacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.SyncKnowledgePacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.SyncRiteTargetPacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.TeleportEffectPacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.VisionPacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.toast.BlessingToastPacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.toast.KnowledgeToastPacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.toast.SpellEffectToastPacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.toast.SpellMediumToastPacket;
import com.miskatonicmysteries.common.registry.MMEntities;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.registry.MMParticles;
import com.miskatonicmysteries.common.registry.MMRegistries;
import com.miskatonicmysteries.common.registry.MMSpellMediums;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.NbtUtil;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.PatchouliAPI;

@Environment(EnvType.CLIENT)
public class MiskatonicMysteriesClient implements ClientModInitializer {

	public static final Identifier OBFUSCATED_FONT_ID = new Identifier(Constants.MOD_ID,
		"obfuscated_font");

	@Override
	public void onInitializeClient() {
		KeyBinding.updateKeysByCode();
		MMModels.init();
		registerPatchoulies();
		registerParticleFactories();
		registerTrinketRenderers();
		registerArmorRenderers();
		registerBlockRenderers();
		registerEntityRenderers();
		ShaderHandler.init();
		ResourceHandler.init();
		registerItemRenderers();
		SpellClientHandler.init();
		VisionHandler.init();
		registerPackets();
		MMClientEvents.init();
	}

	private void registerBlockRenderers() {
		BlockRenderLayerMap.INSTANCE
			.putBlock(MMObjects.CHEMISTRY_SET, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(MMObjects.RESONATOR, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(MMObjects.POWER_CELL, RenderLayer.getTranslucent());
		AltarBlock.ALTARS
			.forEach(
				block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout()));
		OctagramBlock.OCTAGRAMS.forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block,
			RenderLayer.getCutout()));
		StatueBlock.STATUES
			.forEach(
				block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout()));
		BlockRenderLayerMap.INSTANCE.putBlock(MMObjects.WARDING_MARK, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(MMObjects.YELLOW_SIGN, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE
			.putBlock(MMObjects.INFESTED_WHEAT_CROP, RenderLayer.getCutout());

		BlockEntityRendererRegistry.INSTANCE.register(MMObjects.CHEMISTRY_SET_BLOCK_ENTITY_TYPE,
			ChemistrySetBlockRender::new);
		BlockEntityRendererRegistry.INSTANCE
			.register(MMObjects.ALTAR_BLOCK_ENTITY_TYPE, AltarBlockRender::new);
		BlockEntityRendererRegistry.INSTANCE
			.register(MMObjects.OCTAGRAM_BLOCK_ENTITY_TYPE, OctagramBlockRender::new);
		BlockEntityRendererRegistry.INSTANCE
			.register(MMObjects.STATUE_BLOCK_ENTITY_TYPE, StatueBlockRender::new);
		BlockEntityRendererRegistry.INSTANCE
			.register(MMObjects.MASTERPIECE_STATUE_BLOCK_ENTITY_TYPE,
				MasterpieceStatueBlockRender::new);
	}

	private void registerEntityRenderers() {
		EntityRendererRegistry.register(MMEntities.PROTAGONIST, ProtagonistEntityRender::new);
		EntityRendererRegistry.register(MMEntities.HASTUR_CULTIST, HasturCultistEntityRender::new);
		EntityRendererRegistry
			.register(MMEntities.SPELL_PROJECTILE, SpellProjectileEntityRenderer::new);
		EntityRendererRegistry.register(MMEntities.BOLT, BoltEntityRenderer::new);
		EntityRendererRegistry.register(MMEntities.PHANTASMA,
			(context) -> new PhantasmaEntityRenderer(context, new PhantasmaModel()));
		EntityRendererRegistry.register(MMEntities.ABERRATION,
			(context) -> new PhantasmaEntityRenderer(context, new AberrationModel()));
		EntityRendererRegistry.register(MMEntities.TATTERED_PRINCE, TatteredPrinceRenderer::new);
		EntityRendererRegistry
			.register(MMEntities.GENERIC_TENTACLE, GenericTentacleEntityRenderer::new);
		EntityRendererRegistry.register(MMEntities.HARROW, HarrowEntityRenderer::new);
		EntityRendererRegistry.register(MMEntities.BYAKHEE, ByakheeEntityRenderer::new);
		EntityRendererRegistry.register(MMEntities.HALLUCINATION, HallucinationRenderer::new);
		EntityRendererRegistry
			.register(MMEntities.TINDALOS_HOUND, TindalosHoundEntityRenderer::new);
	}

	private void registerItemRenderers() {
		FabricModelPredicateProviderRegistry
			.register(MMObjects.RIFLE, new Identifier("loading"), (stack, world,
				entity, seed) -> GunItem
				.isLoading(stack) ? 1 : 0);
		StatueBlock.STATUES
			.forEach(statue -> BuiltinItemRendererRegistry.INSTANCE.register(statue.asItem(),
				new StatueBlockRender.BuiltinItemStatueRenderer()));
		BuiltinItemRendererRegistry.INSTANCE.register(MMObjects.MASTERPIECE_STATUE,
			new MasterpieceStatueBlockRender.BuiltinItemStatueRenderer());
	}

	private void registerPackets() {
		ClientPlayNetworking
			.registerGlobalReceiver(ExpandSanityPacket.ID, ExpandSanityPacket::handle);
		ClientPlayNetworking
			.registerGlobalReceiver(RemoveExpansionPacket.ID, RemoveExpansionPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(SpellPacket.ID, SpellPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(MobSpellPacket.ID,
			(client, networkHandler, packetByteBuf, sender) -> {
				Entity mob = client.world.getEntityById(packetByteBuf.readInt());
				Entity target = client.world.getEntityById(packetByteBuf.readInt());
				SpellEffect effect = MMRegistries.SPELL_EFFECTS.get(packetByteBuf.readIdentifier());
				int intensity = packetByteBuf.readInt();
				client.execute(() -> {
					if (mob instanceof MobEntity && target instanceof LivingEntity) {
						effect.effect(client.world, (MobEntity) mob, target, target
								.getPos(), MMSpellMediums.MOB_TARGET,
							intensity, mob);
					}
				});
			});
		ClientPlayNetworking
			.registerGlobalReceiver(InsanityEventPacket.ID, (client, networkHandler, packetByteBuf,
				sender) -> {
				Identifier id = packetByteBuf.readIdentifier();
				if (client.player != null) {
					client.execute(() -> MMRegistries.INSANITY_EVENTS.get(id).execute(client.player,
						(Sanity) client.player));
				}
			});
		ClientPlayNetworking
			.registerGlobalReceiver(EffectParticlePacket.ID, EffectParticlePacket::handle);
		ClientPlayNetworking
			.registerGlobalReceiver(BloodParticlePacket.ID, BloodParticlePacket::handle);
		ClientPlayNetworking
			.registerGlobalReceiver(SyncSpellCasterDataPacket.ID, (client, networkHandler,
				packetByteBuf, sender) -> {
				NbtCompound tag = packetByteBuf.readNbt();
				client.execute(
					() -> SpellCaster.of(client.player)
						.ifPresent(caster -> NbtUtil.readSpellData(caster,
							tag)));
			});
		ClientPlayNetworking
			.registerGlobalReceiver(OpenSpellEditorPacket.ID,
				(client, networkHandler, packetByteBuf,
					sender) -> client
					.execute(
						() -> client.openScreen(new EditSpellScreen((SpellCaster) client.player))));
		ClientPlayNetworking
			.registerGlobalReceiver(TeleportEffectPacket.ID, TeleportEffectPacket::handle);
		ClientPlayNetworking
			.registerGlobalReceiver(SyncBiomeMaskPacket.ID, SyncBiomeMaskPacket::handle);
		ClientPlayNetworking
			.registerGlobalReceiver(SyncBlessingsPacket.ID, SyncBlessingsPacket::handle);
		ClientPlayNetworking
			.registerGlobalReceiver(SyncKnowledgePacket.ID, SyncKnowledgePacket::handle);
		ClientPlayNetworking
			.registerGlobalReceiver(ModifyBlessingPacket.ID, ModifyBlessingPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(SoundPacket.ID, SoundPacket::handle);
		ClientPlayNetworking
			.registerGlobalReceiver(SyncRiteTargetPacket.ID, SyncRiteTargetPacket::handle);
		ClientPlayNetworking
			.registerGlobalReceiver(SyncHeldEntityPacket.ID, SyncHeldEntityPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(VisionPacket.ID, VisionPacket::handle);
		ClientPlayNetworking
			.registerGlobalReceiver(KnowledgeToastPacket.ID, KnowledgeToastPacket::handle);
		ClientPlayNetworking
			.registerGlobalReceiver(SpellEffectToastPacket.ID, SpellEffectToastPacket::handle);
		ClientPlayNetworking
			.registerGlobalReceiver(SpellMediumToastPacket.ID, SpellMediumToastPacket::handle);
		ClientPlayNetworking
			.registerGlobalReceiver(BlessingToastPacket.ID, BlessingToastPacket::handle);
	}

	private void registerArmorRenderers() {
		ArmorRenderer.register(new CultistRobesArmorRenderer(new Identifier(Constants.MOD_ID,
				"textures/model/armor" + "/yellow_robes.png")), MMObjects.YELLOW_HOOD,
			MMObjects.YELLOW_ROBE,
			MMObjects.YELLOW_SKIRT);
		ArmorRenderer.register(new CultistRobesArmorRenderer(new Identifier(Constants.MOD_ID,
				"textures/model/armor" + "/dark_robes.png")), MMObjects.DARK_HOOD, MMObjects.DARK_ROBE,
			MMObjects.DARK_SKIRT);
	}

	private void registerParticleFactories() {
		ParticleFactoryRegistry.getInstance()
			.register(MMParticles.DRIPPING_BLOOD, LeakParticle.BloodFactory::new);
		ParticleFactoryRegistry.getInstance()
			.register(MMParticles.AMBIENT, AmbientMagicParticle.DefaultFactory::new);
		ParticleFactoryRegistry.getInstance().register(MMParticles.AMBIENT_MAGIC,
			AmbientMagicParticle.MagicFactory::new);
		ParticleFactoryRegistry.getInstance().register(MMParticles.SHRINKING_MAGIC,
			ShrinkingMagicParticle.Factory::new);
		ParticleFactoryRegistry.getInstance()
			.register(MMParticles.FLAME, CandleFlameParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(MMParticles.RESONATOR_CREATURE,
			ResonatorCreatureParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(MMParticles.WEIRD_CUBE,
			provider -> new WeirdCubeParticle.Factory());
	}

	private void registerPatchoulies() {
		PatchouliAPI.get().registerFunction("obfs", (param, iStyleStack) -> {
			String[] args = param.split(";");
			Affiliation affiliation = args[0].equals("") ? null :
				MMRegistries.AFFILIATIONS.get(new Identifier(args[0]));
			int stage = args.length > 1 ? Integer.parseInt(args[1]) : 0;
			boolean hasStage =
				Ascendant.of(MinecraftClient.getInstance().player)
					.map(ascendant -> ascendant.getAscensionStage() >= stage).orElse(false);
			boolean hasAffiliation =
				Affiliated.of(MinecraftClient.getInstance().player)
					.map(affiliated -> affiliation == null
						|| affiliated.getAffiliation(false) == affiliation)
					.orElse(false);
			boolean canRead = hasStage && hasAffiliation;
			if (!canRead) {
				iStyleStack.modifyStyle(
					style -> style.withFormatting(Formatting.OBFUSCATED)); //todo implement custom
				// font once patchy allows that
				return args.length > 2 ? args[2] : "";
			}
			return "";
		});
		PatchouliAPI.get().registerFunction("expandknowledge", (param, iStyleStack) -> {
			int index = Integer.parseInt(param);
			return Knowledge.of(MinecraftClient.getInstance().player)
				.map(knowledge -> index < knowledge.getKnowledge().size() ? "\u2022 " + I18n
					.translate(
						"knowledge.miskatonicmysteries." + knowledge.getKnowledge().get(index))
					: "")
				.orElse("");
		});
	}

	private void registerTrinketRenderers() {
		TrinketRendererRegistry.registerRenderer(MMObjects.ELEGANT_MASK,
			new MaskTrinketRenderer(
				(ctx) -> new HasturMaskModel(ctx.getModelPart(MMModels.HASTUR_MASK)),
				new Identifier(Constants.MOD_ID, "textures/model/mask/elegant_mask.png")));
		TrinketRendererRegistry.registerRenderer(MMObjects.FERAL_MASK,
			new MaskTrinketRenderer(
				(ctx) -> new ShubMaskModel(ctx.getModelPart(MMModels.SHUB_MASK)),
				new Identifier(Constants.MOD_ID, "textures/model/mask/feral_mask.png")));
		TrinketRendererRegistry.registerRenderer(MMObjects.WILD_MASK,
			new MaskTrinketRenderer(
				(ctx) -> new ShubAlternateMaskModel(ctx.getModelPart(MMModels.SHUB_ALT_MASK)),
				new Identifier(Constants.MOD_ID, "textures/model/mask/wild_mask.png")));
	}
}
