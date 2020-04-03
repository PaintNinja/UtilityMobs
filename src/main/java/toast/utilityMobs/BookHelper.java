package toast.utilityMobs;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;

public abstract class BookHelper
{
    /// Checks the player's book and updates it.
    public static boolean checkBook(PlayerEntity player) {
        ItemStack held = player.getEquipmentInSlot(0);
        if (held == null || held.stackTagCompound == null || held.getItem() != Items.written_book && held.getItem() != Items.writable_book)
            return false;
        if (held.stackTagCompound.hasKey("umb")) {
            ManualHelper.manual(held, held.stackTagCompound.getByte("umb"));
        }
        else if (held.stackTagCompound.hasKey("umt")) {
            TargetHelper.write(player.getCommandSenderName(), held, held.stackTagCompound.getByte("umt"));
            return true;
        }
        return false;
    }

    /// Called when a player right clicks a living entity. If this returns true, the event is canceled.
    public static boolean interact(PlayerEntity player, LivingEntity entity) {
        ItemStack held = player.getEquipmentInSlot(0);
        if (held == null || held.stackTagCompound == null || held.getItem() != Items.written_book && held.getItem() != Items.writable_book || !held.stackTagCompound.hasKey("umt"))
            return false;
        if (player.worldObj.isRemote) {
            TargetHelper.interact(player.getCommandSenderName(), held, held.stackTagCompound.getByte("umt"), entity, player.isCrouching());
        }
        return true;
    }

    /// Sets the book's title and author.
    public static ItemStack setTitleAndAuthor(ItemStack book, String title, String author) {
        if (book.stackTagCompound == null) {
            book.setTagCompound(new CompoundNBT());
        }
        book.stackTagCompound.setString("title", title);
        book.stackTagCompound.setString("author", author);
        return book;
    }
    public static ItemStack setTitle(ItemStack book, String title) {
        if (book.stackTagCompound == null) {
            book.setTagCompound(new CompoundNBT());
        }
        book.stackTagCompound.setString("title", title);
        return book;
    }
    public static ItemStack setAuthor(ItemStack book, String author) {
        if (book.stackTagCompound == null) {
            book.setTagCompound(new CompoundNBT());
        }
        book.stackTagCompound.setString("author", author);
        return book;
    }

    /// Removes all pages from a book.
    public static ItemStack removePages(ItemStack book) {
        if (book.stackTagCompound != null && book.stackTagCompound.hasKey("pages")) {
            book.stackTagCompound.removeTag("pages");
        }
        return book;
    }

    /// Adds new pages to a book.
    public static ItemStack addPages(ItemStack book, String... pages) {
        if (pages.length > 0) {
            if (book.stackTagCompound == null) {
                book.setTagCompound(new CompoundNBT());
            }
            if (!book.stackTagCompound.hasKey("pages")) {
                book.stackTagCompound.setTag("pages", new ListNBT());
            }
            ListNBT tag = book.stackTagCompound.getTagList("pages", new StringNBT().getId());
            for (int p = 0; p < pages.length; p++) if (pages[p] != null) {
                tag.appendTag(new NBTTagString(pages[p]));
            }
        }
        return book;
    }
}