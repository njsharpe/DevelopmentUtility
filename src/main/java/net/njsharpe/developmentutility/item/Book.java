package net.njsharpe.developmentutility.item;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Book extends AbstractItem {

    private Book(@NotNull ItemStack item) {
        super(item);
    }

    @NotNull
    @Override
    public Book clone() {
        return new Book(this.getItem().clone());
    }

    public static class Builder extends AbstractItem.Builder {

        private final String title;
        private String author;
        private List<String> pages;
        private BookMeta.Generation generation;

        public Builder(@Nullable String title) {
            super(Material.BOOK);
            if(title != null && title.length() > 32)
                throw new IllegalArgumentException("title cannot be longer than 32 characters");
            this.title = title;
        }

        public Builder(@Nullable String title, @Range(from = 1, to = Integer.MAX_VALUE) int amount) {
            super(Material.BOOK, amount);
            if(title != null && title.length() > 32)
                throw new IllegalArgumentException("title cannot be longer than 32 characters");
            this.title = title;
        }

        private Builder(@NotNull ItemStack item) {
            super(item.getType(), item.getAmount(), Optional.ofNullable(item.getItemMeta())
                    .orElseThrow(IllegalArgumentException::new));
            this.title = ((BookMeta) item.getItemMeta()).getTitle();
            this.author = ((BookMeta) item.getItemMeta()).getAuthor();
            this.pages = ((BookMeta) item.getItemMeta()).getPages();
            this.generation = ((BookMeta) item.getItemMeta()).getGeneration();
        }

        public Builder forGeneration(BookMeta.Generation generation) {
            this.generation = generation;
            return this;
        }

        public Builder withPage(@NotNull String text) {
            this.pages.add(text);
            return this;
        }

        public Builder withPage(int index, @NotNull String text) {
            this.pages.add(index, text);
            return this;
        }

        public Builder withPages(@NotNull String... pages) {
            this.pages.addAll(Arrays.asList(pages));
            return this;
        }

        public Builder withPages(@NotNull Iterable<String> pages) {
            pages.forEach(this::withPage);
            return this;
        }

        public Builder setAuthor(@NotNull String author) {
            this.author = author;
            return this;
        }

        public Builder setAuthor(@NotNull Player player) {
            this.author = player.getName();
            return this;
        }

        @Override
        protected ItemMeta create() {
            BookMeta meta = (BookMeta) super.create();
            if(this.generation != null) meta.setGeneration(this.generation);
            meta.setPages(this.pages);
            meta.setAuthor(this.author);
            meta.setTitle(this.title);
            return meta;
        }

        @Override
        public Book build() {
            ItemStack item = new ItemStack(this.material, this.amount);
            item.setItemMeta(this.create());
            return new Book(item);
        }

        public static Book.Builder rebuild(@NotNull ItemStack item) {
            return new Book.Builder(item);
        }

    }

}
