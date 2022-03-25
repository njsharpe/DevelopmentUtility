package net.njsharpe.developmentutility.item;

import org.bukkit.Material;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class Item extends AbstractItem {

    private Item(@NotNull ItemStack item) {
        super(item);
    }

    @NotNull
    @Override
    public Item clone() {
        return new Item(this.getItem().clone());
    }

    public static class Builder extends AbstractItem.Builder {

        public Builder(@NotNull Material material) {
            super(material);
        }

        public Builder(@NotNull Material material, @Range(from = 1, to = Integer.MAX_VALUE) int amount) {
            super(material, amount);
        }

        @Override
        public Item build() {
            ItemStack item = new ItemStack(this.material, this.amount);
            item.setItemMeta(this.create());
            return new Item(item);
        }

    }

}
