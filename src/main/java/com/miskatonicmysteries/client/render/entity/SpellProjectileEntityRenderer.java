package com.miskatonicmysteries.client.render.entity;

import com.miskatonicmysteries.common.feature.entity.SpellProjectileEntity;
import com.miskatonicmysteries.common.registry.MMParticles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class SpellProjectileEntityRenderer extends EntityRenderer<SpellProjectileEntity> {

	public SpellProjectileEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public void render(SpellProjectileEntity entity, float yaw, float tickDelta, MatrixStack matrices,
					   VertexConsumerProvider vertexConsumers, int light) {
		super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
		if (entity.getSpell() != null) {
			Vec3d rgb = Vec3d
				.unpackRgb(entity.getSpell().getColor(entity.getOwner() instanceof LivingEntity ? (LivingEntity) entity.getOwner() : null));
			entity.world.addParticle(MMParticles.SHRINKING_MAGIC, entity.getX(), entity.getY(), entity.getZ(), rgb.x, rgb.y, rgb.z);
		}
	}

	@Override
	public Identifier getTexture(SpellProjectileEntity entity) {
		return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
	}
}
