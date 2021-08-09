package com.miskatonicmysteries.client.render.blockentity;

import com.miskatonicmysteries.client.model.MMModels;
import com.miskatonicmysteries.client.model.block.MasterpieceStatueModel;
import com.miskatonicmysteries.client.render.ColorUtil;
import com.miskatonicmysteries.common.block.blockentity.MasterpieceStatueBlockEntity;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.mixin.client.PlayerSkinTextureAccessor;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.IntList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.impl.client.indigo.renderer.helper.ColorHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.*;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.resource.ResourceManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class MasterpieceStatueBlockRender implements BlockEntityRenderer<MasterpieceStatueBlockEntity> {
	public static final Map<GameProfile, StoneTexture> TEXTURE_CACHE = new HashMap<>();
	private final StoneTexture defaultTexture;
	private final TextureManager textureManager;
	private final ResourceManager resourceManager; //???
	private final MasterpieceStatueModel statueModel;
	private final PlayerEntityModel<PlayerEntity> steveModel;
	private final PlayerEntityModel<PlayerEntity> alexModel;
	private final SpriteIdentifier statueSprite = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE,
			new Identifier(Constants.MOD_ID, "block/statue/masterpiece_statue"));
	private List<Consumer<PlayerEntityModel<PlayerEntity>>> poses = new ArrayList<>();

	public MasterpieceStatueBlockRender(BlockEntityRendererFactory.Context context) {
		BuiltinItemStatueRenderer.dispatcher = context.getRenderDispatcher();
		this.textureManager = MinecraftClient.getInstance().getTextureManager();
		this.resourceManager = MinecraftClient.getInstance().getResourceManager();
		this.defaultTexture = new StoneTexture(DefaultSkinHelper.getTexture(), true);
		this.statueModel = new MasterpieceStatueModel(context.getLayerModelPart(MMModels.MASTERPIECE_STATUE));
		this.steveModel = new PlayerEntityModel<>(context.getLayerModelPart(EntityModelLayers.PLAYER), false);
		this.steveModel.child = false;
		this.alexModel = new PlayerEntityModel<>(context.getLayerModelPart(EntityModelLayers.PLAYER), true);
		this.alexModel.child = false;
		poses.add(model -> {
			setRotationAngle(model.head, -0.1047F, 0.0873F, 0.0F);
			model.hat.copyTransform(model.head);
			setRotationAngle(model.body, 0.0F, 0.0F, 0.0F);
			model.jacket.copyTransform(model.body);
			setRotationAngle(model.rightArm, -0.3927F, 0.0F, 0.0F);
			model.rightSleeve.copyTransform(alexModel.rightArm);
			setRotationAngle(model.leftArm, 0.0349F, 0.0F, 0.0F);
			model.leftSleeve.copyTransform(alexModel.leftArm);
			setRotationAngle(model.rightLeg, 0.192F, 0.0F, 0.0349F);
			model.rightPants.copyTransform(alexModel.rightPants);
			setRotationAngle(model.leftLeg, -0.1745F, 0.0F, -0.0349F);
			model.leftLeg.copyTransform(alexModel.leftPants);
		});
		poses.add(model -> {
			setRotationAngle(model.head, -0.3665F, 0.3927F, 0.0F);
			model.hat.copyTransform(model.head);
			setRotationAngle(model.body, 0.0F, 0.0F, 0.0F);
			model.jacket.copyTransform(model.body);
			setRotationAngle(model.rightArm, -2.8798F, 0.0F, -0.3927F);
			model.rightSleeve.copyTransform(alexModel.rightArm);
			setRotationAngle(model.leftArm, 0.4712F, 0.0F, -0.2182F);
			model.leftSleeve.copyTransform(alexModel.leftArm);
			setRotationAngle(model.rightLeg, 0.192F, 0.0F, 0.0349F);
			model.rightPants.copyTransform(alexModel.rightPants);
			setRotationAngle(model.leftLeg, -0.1745F, 0.0F, -0.0349F);
			model.leftLeg.copyTransform(alexModel.leftPants);

		});
		poses.add(model -> {
			setRotationAngle(model.head, -0.3665F, 0.0873F, 0.0F);
			model.hat.copyTransform(model.head);
			setRotationAngle(model.body, 0.0F, 0.0F, 0.0F);
			model.jacket.copyTransform(model.body);
			setRotationAngle(model.rightArm, -2.7489F, 0.0F, -0.1745F);
			model.rightSleeve.copyTransform(alexModel.rightArm);
			setRotationAngle(model.leftArm, -2.714F, 0.0F, 0.1745F);
			model.leftSleeve.copyTransform(alexModel.leftArm);
			setRotationAngle(model.rightLeg, -0.2443F, 0.0F, 0.0349F);
			model.rightPants.copyTransform(alexModel.rightPants);
			setRotationAngle(model.leftLeg, 0.0436F, 0.0F, -0.0349F);
			model.leftLeg.copyTransform(alexModel.leftPants);
		});
	}

	@Override
	public void render(MasterpieceStatueBlockEntity entity, float tickDelta, MatrixStack matrices,
					   VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		int rotation = entity.getCachedState() != null ? entity.getCachedState().get(Properties.ROTATION) : 0;
		matrices.translate(0.5, 1.5, 0.5);
		matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion((0.125F / 2F) * rotation * 360F));
		matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
		VertexConsumer vertexConsumer = statueSprite.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid);
		statueModel.render(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
		renderPlayer(entity.getStatueProfile(), matrices, vertexConsumers, light, overlay, entity.pose);
		matrices.pop();
	}

	private void renderPlayer(@Nullable GameProfile profile, MatrixStack matrices,
							  VertexConsumerProvider vertexConsumers, int light, int overlay, int pose) {
		RenderLayer layer = getLayer(profile);
		PlayerEntityModel<PlayerEntity> model = getModel(profile);
		matrices.translate(0, -0.375F, 0);
		poseStatue(pose, model);
		model.render(matrices, vertexConsumers.getBuffer(layer), light, overlay, 1, 1, 1, 1F);
	}

	private void poseStatue(int pose, PlayerEntityModel<PlayerEntity> model) {
		if (pose >= poses.size()) {
			setRotationAngle(model.head, 0.0F, 0.0F, 0.0F);
			model.head.copyTransform(model.hat);
			setRotationAngle(model.body, 0.0F, 0.0F, 0.0F);
			model.body.copyTransform(model.jacket);
			setRotationAngle(model.rightArm, 0.0F, 0.0F, (float) (Math.PI / 2F));
			model.rightSleeve.copyTransform(alexModel.rightArm);
			setRotationAngle(model.leftArm, 0.0F, 0.0F, (float) -(Math.PI / 2F));
			model.leftSleeve.copyTransform(alexModel.leftArm);
			setRotationAngle(model.rightLeg, 0.0F, 0.0F, 0.0F);
			model.rightPants.copyTransform(alexModel.rightPants);
			setRotationAngle(model.leftLeg, 0.0F, 0.0F, 0.0F);
			model.leftLeg.copyTransform(alexModel.leftPants);
			return;
		}
		poses.get(pose).accept(model);
	}

	private RenderLayer getLayer(@Nullable GameProfile profile) {
		return getStoneTexture(profile).renderLayer;
	}

	private StoneTexture getStoneTexture(@Nullable GameProfile profile) {
		return profile == null ? defaultTexture : TEXTURE_CACHE.compute(profile, (gameProfile, stoneTexture) -> {
			if (stoneTexture == null) {
				MinecraftClient minecraftClient = MinecraftClient.getInstance();
				Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map =
						minecraftClient.getSkinProvider().getTextures(profile);
				if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
					try {
						return new StoneTexture(minecraftClient.getSkinProvider().loadSkin(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN), false);
					} catch (NullPointerException e) {
						return defaultTexture;
					}
				}
				else {
					return new StoneTexture(DefaultSkinHelper.getTexture(PlayerEntity.getUuidFromProfile(profile)),
							true);
				}
			}
			else {
				if (stoneTexture.needsUpdate) {
					stoneTexture.updateTexture();
				}
				return stoneTexture;
			}
		});
	}

	private PlayerEntityModel<PlayerEntity> getModel(@Nullable GameProfile profile) {
		return profile != null && profile.getId() != null && DefaultSkinHelper.getModel(profile.getId()).equals(
				"default") ? steveModel : alexModel;
	}

	public void setRotationAngle(ModelPart part, float pitch, float yaw, float roll) {
		part.pitch = pitch;
		part.yaw = yaw;
		part.roll = roll;
	}

	public static class BuiltinItemStatueRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
		private static BlockEntityRenderDispatcher dispatcher;
		private final MasterpieceStatueBlockEntity dummy;

		public BuiltinItemStatueRenderer() {
			dummy = new MasterpieceStatueBlockEntity(BlockPos.ORIGIN, null);
		}

		@Override
		public void render(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices,
						   VertexConsumerProvider vertexConsumers, int light, int overlay) {
			if (dispatcher != null) {
				NbtCompound compound = stack.getTag();
				if (compound != null && compound.contains(Constants.NBT.BLOCK_ENTITY_TAG)) {
					dummy.readNbt(compound.getCompound(Constants.NBT.BLOCK_ENTITY_TAG));
				}
				else {
					dummy.pose = 0;
					dummy.setStatueProfile(null);
				}
				matrices.scale(0.5F, 0.5F, 0.5F);
				matrices.translate(0.5F, 0, 0.5F);
				dispatcher.get(dummy).render(dummy, 1, matrices, vertexConsumers, light, overlay);
			}
		}
	}

	@Environment(EnvType.CLIENT)
	public final class StoneTexture implements AutoCloseable { //credit goes to LambdAurora
		public static final Logger LOGGER = LogManager.getLogger(Constants.MOD_ID + ":texturegen");
		private static final Identifier stoneTexture = new Identifier("textures/block/stone.png");
		private final Identifier base;
		private final boolean defaultSkin;
		private final RenderLayer renderLayer;
		private final NativeImageBackedTexture texture;
		public boolean needsUpdate;

		StoneTexture(Identifier base, boolean playerSkin) {
			this.base = base;
			this.defaultSkin = playerSkin;
			texture = new NativeImageBackedTexture(new NativeImage(64, 64, true));
			Identifier id = textureManager.registerDynamicTexture("mm_stone/" + base.getPath(), texture);
			this.renderLayer = RenderLayer.getEntityCutout(id);
			this.needsUpdate = true;
		}

		public void updateTexture() {
			MinecraftClient.getInstance().execute(() -> {
				try {
					NativeImage inputImage;
					if (!defaultSkin) {
						inputImage = NativeImage.read(resourceManager.getResource(base).getInputStream());
					}
					else {
						PlayerSkinTexture skinTexture = (PlayerSkinTexture) textureManager.getTexture(base);
						File file = ((PlayerSkinTextureAccessor) skinTexture).getCacheFile();
						try {
							inputImage = NativeImage.read(new FileInputStream(file));
						} catch (FileNotFoundException e) {
							inputImage =
									NativeImage.read(resourceManager.getResource(DefaultSkinHelper.getTexture()).getInputStream());
						}
					}
					NativeImage stoneImage =
							NativeImage.read(resourceManager.getResource(stoneTexture).getInputStream());
					IntList skinPalette = ColorUtil.getPaletteFromImage(inputImage);
					Int2IntMap colorMap = ColorUtil.associateGrayscale(skinPalette);
					for (int y = 0; y < texture.getImage().getHeight(); y++) {
						for (int x = 0; x < texture.getImage().getWidth(); x++) {
							int color = colorMap.get(inputImage.getPixelColor(x, y));
							color = ColorHelper.multiplyColor(color,
									stoneImage.getPixelColor(x % stoneImage.getWidth(), y % stoneImage.getHeight()));
							texture.getImage().setPixelColor(x, y, color);
						}
					}
					needsUpdate = false;
					inputImage.close();
					this.texture.upload();
				} catch (Exception e) {
					LOGGER.error("Could not update stone texture.", e);
				}
			});
		}

		@Override
		public void close() {
			this.texture.close();
		}
	}
}
