package com.miskatonicmysteries.common.component;

import com.miskatonicmysteries.api.interfaces.Ascendant;
import com.miskatonicmysteries.api.registry.Blessing;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.toast.BlessingToastPacket;
import com.miskatonicmysteries.common.registry.MMComponents;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.NbtUtil;
import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class AscendantComponent implements Ascendant, ComponentV3, AutoSyncedComponent, CommonTickingComponent {

	private final List<Blessing> blessings = new ArrayList<>();
	private final LivingEntity provider;  // or World, or whatever you are attaching to
	private int stage;
	private boolean syncBlessings = true;

	public AscendantComponent(LivingEntity provider) {
		this.provider = provider;
	}

	@Override
	public void addBlessing(Blessing blessing) {
		if (!blessings.contains(blessing) && blessings.size() < Constants.DataTrackers.MAX_BLESSINGS) {
			if (provider instanceof ServerPlayerEntity p) {
				BlessingToastPacket.send(p, blessing);
			}
			blessings.add(blessing);
			blessing.onAcquired(provider);
		}
		this.syncBlessings = true;
		MMComponents.ASCENDANT_COMPONENT.sync(provider);
	}

	@Override
	public boolean removeBlessing(Blessing blessing) {
		if (blessings.contains(blessing)) {
			blessing.onRemoved(provider);
			syncBlessings = true;
			MMComponents.ASCENDANT_COMPONENT.sync(provider);
			return blessings.remove(blessing);
		}
		return false;
	}

	@Override
	public List<Blessing> getBlessings() {
		return blessings;
	}

	@Override
	public int getAscensionStage() {
		return stage;
	}

	@Override
	public void setAscensionStage(int level) {
		this.stage = level;
		MMComponents.ASCENDANT_COMPONENT.sync(provider);
	}

	@Override
	public void readFromNbt(@NotNull NbtCompound tag) {
		NbtUtil.readBlessingData(this, tag);
		setAscensionStage(tag.getInt(Constants.NBT.ASCENSION_STAGE));
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
		NbtUtil.writeBlessingData(this, tag);
		tag.putInt(Constants.NBT.ASCENSION_STAGE, getAscensionStage());
	}

	@Override
	public void applySyncPacket(PacketByteBuf buf) {
		if (buf.readBoolean()) {
			AutoSyncedComponent.super.applySyncPacket(buf);
			this.syncBlessings = false;
		} else {
			setAscensionStage(buf.readInt());
		}
	}

	@Override
	public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
		if (syncBlessings) {
			buf.writeBoolean(true);
			AutoSyncedComponent.super.writeSyncPacket(buf, recipient);
		} else {
			buf.writeBoolean(false);
			buf.writeInt(getAscensionStage());
		}
	}

	@Override
	public void tick() {
		for (Blessing blessing : blessings) {
			blessing.tick(provider);
		}
	}
}
