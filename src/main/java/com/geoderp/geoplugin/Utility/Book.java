package com.geoderp.geoplugin.Utility;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class Book {
    private int pageCount;
    private final int pageLimit = 100;
    private final int charLimit = 1024;
    private final int titleLimit = 32;

    ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
    BookMeta bookMeta = (BookMeta) book.getItemMeta();

    public Book() {
        this.pageCount = 0;
    }
    
    public String getTitle() {
        return bookMeta.getTitle();
    }

    public String getDisplayString() {
        return bookMeta.getDisplayName();
    }

    public String getAuthor() {
        return bookMeta.getAuthor();
    }

    public List<String> getLore() {
        return bookMeta.getLore();
    }

    public ItemStack createBook(Player player) {
        book.setItemMeta(bookMeta);
        return book;
    }
    
    public void setTitle(String title) {
        bookMeta.setTitle(title);
    }

    public void setDisplayName(String name) {
        bookMeta.setDisplayName(name);
    }

    public void setAuthor(String author) {
        bookMeta.setAuthor(author);
    }

    public void setLore(String loreString) {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(loreString);
        bookMeta.setLore(lore);
    }

    public void addPage(String text) {
        if(pageCount < pageLimit) {
            // Not exceeding page limit
            if(text.length() > charLimit) {
                String thisPage = text.substring(0,1023);
                String remaining = text.substring(1024);
                // Adds max characters to this page
                bookMeta.addPage(thisPage);
                pageCount++;

                // Add remaining text to next page (recursively if more than one page worth remaining)
                addPage(remaining);
            }
            else {
                bookMeta.addPage(text);
                pageCount++;
            }
        }
    }
}
