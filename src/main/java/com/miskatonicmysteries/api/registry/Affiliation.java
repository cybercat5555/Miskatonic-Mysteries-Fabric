package com.miskatonicmysteries.api.registry;

import com.miskatonicmysteries.api.interfaces.Ascendant;
import com.miskatonicmysteries.common.registry.MMRegistries;
import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Affiliation {

	public final int textColor, textColorSecondary;
	public Set<Blessing> blessingPool = new HashSet<>();
	protected Identifier id;
	protected float[] color;

	public Affiliation(Identifier id, float[] color, Blessing... blessings) {
		this(id, color, 0xFFFFFFFF, 0xFFFFFF, blessings);
	}

	public Affiliation(Identifier id, float[] color, int textColor, int textColorSecondary, Blessing... blessings) {
		this.id = id;
		this.color = color;
		this.textColor = textColor;
		this.textColorSecondary = textColorSecondary;
		blessingPool.addAll(Arrays.asList(blessings));
	}

	public static Affiliation fromTag(NbtCompound tag) {
		return MMRegistries.AFFILIATIONS.get(new Identifier(tag.getString(Constants.NBT.AFFILIATION)));
	}

	public Blessing findRandomBlessing(LivingEntity entity, Ascendant ascendant) {
		List<Blessing> possibleBlessings =
			blessingPool.stream().filter(blessing -> !ascendant.getBlessings().contains(blessing)).collect(Collectors.toList());
		return possibleBlessings.size() > 0 ?
			   possibleBlessings.get(entity.getRandom().nextInt(possibleBlessings.size())) : null;
	}

	public NbtCompound toTag(NbtCompound tag) {
		tag.putString(Constants.NBT.AFFILIATION, id.toString());
		return tag;
	}

	public float[] getColor() {
		return color;
	}

	public int getIntColor() {
		int red = ((int) (color[0] * 255) << 16) & 0x00FF0000;
		int green = ((int) (color[1] * 255) << 8) & 0x0000FF00;
		int blue = (int) (color[2] * 255) & 0x000000FF;

		return 0xFF000000 | red | green | blue;
	}

	@Override
	public String toString() {
		return getId().toString();
	}

	public Identifier getId() {
		return id;
	}

	public Identifier getToastTextureLocation() {
		return new Identifier(getId().getNamespace(), String.format("textures/gui/toasts/toast_%s.png", id.getPath()));
	}

	public Text getLocalizedName() {
		return new TranslatableText(String.format("affiliation.%s.%s", id.getNamespace(), id.getPath()));
	}

	public Identifier getIconTextureLocation() {
		return new Identifier(getId().getNamespace(), String.format("textures/gui/icons/%s.png", id.getPath()));
	}
}
