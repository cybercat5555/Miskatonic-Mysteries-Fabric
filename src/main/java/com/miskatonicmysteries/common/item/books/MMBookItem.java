package com.miskatonicmysteries.common.item.books;

import com.miskatonicmysteries.common.feature.Affiliated;
import com.miskatonicmysteries.common.lib.Constants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.common.base.PatchouliSounds;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;

import java.util.List;

public class MMBookItem extends Item implements Affiliated {
    private Identifier id, affiliation;
    private boolean special;

    public MMBookItem(Identifier id, Identifier affiliation, boolean special) {
        super(new Settings().maxCount(1).group(Constants.MM_GROUP));
        this.id = id;
        this.affiliation = affiliation;
        this.special = special;
    }

    public Book getBook() {
        return BookRegistry.INSTANCE.books.get(id);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        Book book = getBook();
        if (player instanceof ServerPlayerEntity) {
            PatchouliAPI.instance.openBookGUI((ServerPlayerEntity) player, book.id);
            SoundEvent sfx = PatchouliSounds.getSound(book.openSound, PatchouliSounds.book_open);
            player.playSound(sfx, 1.0F, (float) (0.7D + Math.random() * 0.4D));
        }
        return TypedActionResult.success(player.getStackInHand(hand));
    }

    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        super.appendTooltip(stack, worldIn, tooltip, flagIn);
        Book book = getBook();
        if (book != null && book.contents != null) {
            tooltip.add(book.getSubtitle().formatted(Formatting.GRAY));
        }
        if (stack.getTag() != null && stack.getTag().contains(Constants.NBT.KNOWLEDGE)) {
            tooltip.add(new TranslatableText("knowledge.contains").formatted(Formatting.GRAY, Formatting.ITALIC));
            stack.getTag().getList(Constants.NBT.KNOWLEDGE, 8).forEach(s -> tooltip.add(new TranslatableText("knowledge." + s.asString())));
        }
    }

    public static ItemStack addKnowledge(String knowledge, ItemStack stack) {
        if (stack.getTag() == null) stack.setTag(new CompoundTag());
        if (!stack.getTag().contains(Constants.NBT.KNOWLEDGE))
            stack.getTag().put(Constants.NBT.KNOWLEDGE, new ListTag());
        stack.getTag().getList(Constants.NBT.KNOWLEDGE, 8).add(StringTag.of(knowledge));
        return stack;
    }

    public static boolean hasKnowledge(String knowledge, ItemStack book) {
        return book.getTag() != null && book.getTag().contains(Constants.NBT.KNOWLEDGE) && book.getTag().getList(Constants.NBT.KNOWLEDGE, 8).stream().anyMatch(tag -> tag.asString().equals(knowledge));
    }

    @Override
    public Identifier getAffiliation() {
        return affiliation;
    }

    @Override
    public boolean isSupernatural() {
        return special;
    }
}
