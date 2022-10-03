package com.miskatonicmysteries.client.gui;

import com.miskatonicmysteries.client.gui.widget.SudokuInfoWidget;
import com.miskatonicmysteries.client.gui.widget.SudokuTileWidget;
import com.miskatonicmysteries.common.handler.networking.packet.c2s.ClientRiteInputPacket;
import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;


import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.minecraft.util.math.random.Random;

import com.mojang.blaze3d.systems.RenderSystem;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class SudokuScreen extends Screen {

	public static final Identifier NORMAL_TEXTURE = new Identifier(Constants.MOD_ID, "textures/gui/puzzle/reversion_puzzle.png");
	public static final Identifier HASTUR_TEXTURE = new Identifier(Constants.MOD_ID, "textures/gui/puzzle/hastur_puzzle.png");
	public final Identifier style;
	public int openTicks;
	public float alpha;
	public byte[][] tiles = new byte[4][4];
	public byte[][] immutable_tiles = new byte[4][4];
	public int infoTicks = 0;
	public boolean finished;
	public int finishingTicks;
	private List<Text> infoText;

	public SudokuScreen(Identifier style) {
		super(Text.translatable(Constants.MOD_ID + ".gui.sudoku"));
		fillTiles(tiles);
		this.style = style;
	}

	private void fillTiles(byte[][] tiles) {
		fillRandomy(0, tiles);
		do { //the bad bad evil sudoku generation
			fillRandomy(1, tiles);
			fillRandomy(2, tiles);
			fillRandomy(3, tiles);
		} while (!isSolved());
		Random random = Random.create();
		int removedTiles = 0;
		while (removedTiles < 8) {
			int index = random.nextInt(16);
			if (tiles[index % 4][index / 4] != 0) {
				tiles[index % 4][index / 4] = 0;
				removedTiles++;
			}
		}
		immutable_tiles = Arrays.copyOf(tiles, tiles.length);
	}

	private void fillRandomy(int section, byte[][] tiles) {
		int startX = section % 2 * 2;
		int startY = section / 2 * 2;
		List<Byte> numsLeft = Arrays.asList((byte) 1, (byte) 2, (byte) 3, (byte) 4);
		Collections.shuffle(numsLeft);
		tiles[startX][startY] = numsLeft.get(0);
		tiles[startX + 1][startY] = numsLeft.get(1);
		tiles[startX][startY + 1] = numsLeft.get(2);
		tiles[startX + 1][startY + 1] = numsLeft.get(3);
	}

	public boolean isSolved() {
		for (int i = 0; i < 4; i++) {
			if (!(sectionValid(i) && rowValid(i) && columnValid(i))) {
				return false;
			}
		}
		return true;
	}

	public boolean sectionValid(int section) {
		int startX = section % 2 * 2;
		int startY = section / 2 * 2;
		byte[] nums = {tiles[startX][startY], tiles[startX + 1][startY], tiles[startX][startY + 1], tiles[startX + 1][startY + 1]};
		Arrays.sort(nums);
		return Arrays.compare(nums, new byte[]{1, 2, 3, 4}) == 0;
	}

	public boolean rowValid(int row) {
		byte[] nums = {tiles[0][row], tiles[1][row], tiles[2][row], tiles[3][row]};
		Arrays.sort(nums);
		return Arrays.compare(nums, new byte[]{1, 2, 3, 4}) == 0;
	}

	public boolean columnValid(int column) {
		byte[] nums = {tiles[column][0], tiles[column][1], tiles[column][2], tiles[column][3]};
		Arrays.sort(nums);
		return Arrays.compare(nums, new byte[]{1, 2, 3, 4}) == 0;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		alpha = MathHelper.clamp(openTicks / 20F, 0, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, finished && finishingTicks > 100 ? 1 - (finishingTicks - 100) / 40F : alpha);
		DrawableHelper.fill(matrices, 0, 0, this.width, this.height, 0xFF000000);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha - 0.5F);
		super.render(matrices, mouseX, mouseY, delta);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, finished ? 1 - (finishingTicks / 100F) : alpha);
		RenderSystem.setShaderTexture(0, style);
		matrices.push();
		drawTexture(matrices, width / 2 - 72, height / 2 - 72, 144, 144, 0, 0, 72, 72, 128, 128);
		matrices.pop();
		if (infoTicks > 0 && infoText != null) {
			float infoAppearance = 1;
			if (infoTicks > 190) {
				infoAppearance = 1 - (infoTicks - 190) / 10F;
			} else if (infoTicks < 10) {
				infoAppearance = infoTicks / 10F;
			}
			matrices.push();
			matrices.translate(0, infoText.size() * 16 * (1 - infoAppearance), 0);
			for (int y = 0; y < infoText.size(); y++) {
				Text orderedText = infoText.get(y);
				drawCenteredText(matrices, client.textRenderer, orderedText,
								 width - 80, height - infoText.size() * 16 + y * 16, 0xFFFFFFFF);
			}
			matrices.pop();
		}
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}

	@Override
	protected void init() {
		super.init();
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				int index = y * 4 + x;
				addDrawableChild(new SudokuTileWidget(width / 2 - 70 + x * 36, height / 2 - 68 + y * 36, index, immutable_tiles[x][y] != 0, this));
			}
		}
		addDrawableChild(new SudokuInfoWidget(width - 18, 2, this));
		infoText = splitText(Text.translatable("text.miskatonicmysteries.sudoku_info"));
	}

	@Override
	public void tick() {
		super.tick();
		openTicks++;
		if (infoTicks > 0) {
			infoTicks--;
		}
		if (finished) {
			finishingTicks++;
		}
		if (finishingTicks > 100) {
			passEvents = true;
			if (finishingTicks > 140) {
				close();
			}
		}
	}

	@Override
	public boolean shouldPause() {
		return false;
	}

	@NotNull
	private List<Text> splitText(Text text) {
		List<Text> list = new ArrayList<>();
		StringBuilder builder = new StringBuilder();
		int length = 0;
		for (String s : text.getString().split(" ")) {
			length += s.length();
			if (length > 16 || s.startsWith("<br>")) {
				list.add(Text.literal(builder.toString()));
				builder = new StringBuilder();
				length = 0;
			}
			builder.append(s.replace("<br>", ""));
			builder.append(" ");
		}
		list.add(Text.literal(builder.toString()));
		return list;
	}

	public void cycleTile(int index) {
		byte current = tiles[index % 4][index / 4];
		if (current < 4) {
			current++;
		} else {
			current = 0;
		}
		tiles[index % 4][index / 4] = current;
	}

	public void finish() {
		finished = true;
		client.world.playSound(client.player.getBlockPos(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.MASTER, 1.0F, 1.0F, false);
		ClientRiteInputPacket.send(client.player);
	}
}
