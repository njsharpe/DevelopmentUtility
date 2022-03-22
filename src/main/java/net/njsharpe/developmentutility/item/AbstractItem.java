package net.njsharpe.developmentutility.item;

import net.njsharpe.developmentutility.Constants;
import net.njsharpe.developmentutility.Format;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractItem implements Cloneable {

    private final ItemStack item;

    protected AbstractItem(@NotNull ItemStack item) {
        this.item = item;
    }

    public void toInventory(Inventory inventory) {
        inventory.addItem(this.item);
    }

    public void toInventory(int slot, Inventory inventory) {
        inventory.setItem(slot, this.item);
    }

    public void equip(EquipmentSlot slot, EntityEquipment equipment) {
        equipment.setItem(slot, this.item);
    }

    public void drop(Location location) {
        if(location.getWorld() == null) throw new IllegalArgumentException("world cannot be null");
        location.getWorld().dropItem(location, this.item);
    }

    @NotNull
    public ItemStack getItem() {
        return this.item;
    }

    @NotNull
    @Override
    public abstract AbstractItem clone();

    public static abstract class Builder {

        protected final Map<Enchantment, Integer> enchantments = new HashMap<>();

        protected final Material material;
        protected final int amount;

        protected String name;
        protected List<String> lore;
        protected short durability;
        protected boolean unbreakable;
        protected boolean glowing;
        protected ItemFlag[] flags;

        public Builder(@NotNull Material material) {
            this(material, 1);
        }

        public Builder(@NotNull Material material, @Range(from = 1, to = Integer.MAX_VALUE) int amount) {
            this.material = material;
            this.amount = amount;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setLore(String lore) {
            return this.setLore(lore, "\\|");
        }

        public Builder setLore(String lore, String delimiter) {
            return this.setLore(Format.toList(lore, delimiter));
        }

        public Builder setLore(List<String> lore) {
            this.lore = lore;
            return this;
        }

        public Builder setDurability(short durability) {
            this.durability = durability;
            return this;
        }

        public Builder setUnbreakable() {
            this.unbreakable = true;
            return this;
        }

        public Builder withFlags(ItemFlag... flags) {
            this.flags = flags;
            return this;
        }

        public Builder setGlowing() {
            this.glowing = true;
            return this;
        }

        public Builder withEnchantment(Enchantment enchantment, int level) {
            this.enchantments.put(enchantment, level);
            return this;
        }

        public Builder withEnchantments(Map<Enchantment, Integer> enchantments) {
            this.enchantments.putAll(enchantments);
            return this;
        }

        protected ItemMeta create() {
            ItemMeta meta = Bukkit.getItemFactory().getItemMeta(this.material);
            if(meta != null) {
                meta.setDisplayName(this.name);
                meta.setLore(this.lore);
                meta.setUnbreakable(this.unbreakable);
                Damageable damageable = (Damageable) meta;
                if(this.durability <= this.material.getMaxDurability()) damageable.setDamage(this.durability);
                if(this.enchantments.isEmpty() && this.glowing) {
                    meta.addEnchant(Constants.DUMMY, 1, true);
                } else {
                    this.enchantments.forEach((k, v) -> meta.addEnchant(k, v, true));
                    meta.addItemFlags(this.flags);
                }
            }
            return meta;
        }

        public abstract AbstractItem build();

    }

}
