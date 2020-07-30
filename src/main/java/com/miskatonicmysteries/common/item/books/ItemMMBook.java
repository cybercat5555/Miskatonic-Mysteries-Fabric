package com.miskatonicmysteries.common.item.books;

import com.miskatonicmysteries.common.feature.Affiliated;
import com.miskatonicmysteries.lib.Constants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
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

public class ItemMMBook extends Item implements Affiliated {
    private Identifier id, affiliation;
    private boolean special;

    public ItemMMBook(Identifier id, Identifier affiliation, boolean special) {
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
