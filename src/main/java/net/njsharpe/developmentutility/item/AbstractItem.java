package net.njsharpe.developmentutility.item;

import net.njsharpe.developmentutility.Constants;
import net.njsharpe.developmentutility.Format;
import net.njsharpe.developmentutility.ObservableString;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

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

        protected final Material material;
        protected final int amount;
        private ItemMeta meta;

        private final Map<Enchantment, Integer> enchantments;
        private List<String> lore;
        private boolean glowing;

        public Builder(@NotNull Material material) {
            this(material, 1);
        }

        public Builder(@NotNull Material material, @Range(from = 1, to = Integer.MAX_VALUE) int amount) {
            this(material, amount, Objects.requireNonNull(Bukkit.getItemFactory().getItemMeta(material),
                    String.format("meta null for type '%s'; illegal for builder", material)));
        }

        protected Builder(@NotNull Material material, @Range(from = 1, to = Integer.MAX_VALUE) int amount,
                          @NotNull ItemMeta meta) {
            this.material = material;
            this.amount = amount;
            this.meta = meta;
            this.enchantments = meta.getEnchants();
            this.lore = meta.getLore() == null ? new ArrayList<>() : meta.getLore();
            this.glowing = this.enchantments.containsKey(Constants.DUMMY);
        }

        public Builder setName(String name) {
            this.meta.setDisplayName(name);
            return this;
        }

        public Builder appendLore(String line) {
            this.lore.add(line);
            return this;
        }

        public Builder appendLore(Iterable<String> lore) {
            if(this.lore.isEmpty()) {
                return this.setLore(lore);
            }
            lore.forEach(this::appendLore);
            return this;
        }

        public Builder appendLore(ObservableString lore) {
            this.lore.add(lore.toString());
            return this;
        }

        public Builder setLore(String lore, String delimiter) {
            return this.setLore(Format.toList(lore, delimiter));
        }

        public Builder setLore(Iterable<String> lore) {
            List<String> list = new ArrayList<>();
            lore.iterator().forEachRemaining(list::add);
            this.lore = list;
            return this;
        }

        public Builder insertLore(int index, String lore) {
            this.lore.set(index, lore);
            return this;
        }

        public Builder insertLore(int index, Iterable<String> lore) {
            AtomicInteger atomic = new AtomicInteger(index);
            lore.forEach(line -> this.lore.set(atomic.getAndIncrement(), line));
            return this;
        }

        public Builder setDurability(short durability) {
            if(durability < 0 || durability > this.material.getMaxDurability())
                durability = this.material.getMaxDurability();
            ((Damageable) this.meta).setDamage(durability);
            return this;
        }

        public Builder setUnbreakable() {
            this.meta.setUnbreakable(true);
            return this;
        }

        public Builder withFlags(ItemFlag... flags) {
            this.meta.addItemFlags(flags);
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

        public <T, Z> Builder withTag(NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
            this.meta.getPersistentDataContainer().set(key, type, value);
            return this;
        }

        public Builder withCustomModelData(int data) {
            this.meta.setCustomModelData(data);
            return this;
        }

        public Builder and(Supplier<ItemMeta> supplier) {
            ItemMeta meta = supplier.get();
            if(Bukkit.getItemFactory().isApplicable(meta, this.material)) {
                this.meta = this.combine(this.meta, supplier.get());
            }
            return this;
        }

        private ItemMeta combine(ItemMeta host, ItemMeta remote) {
            Map<String, Object> map = new HashMap<>(host.serialize());
            new HashMap<>(remote.serialize()).forEach((k, v) -> map.merge(k, v, (x, y) -> x));
            return (ItemMeta) ConfigurationSerialization.deserializeObject(map, ItemMeta.class);
        }

        protected ItemMeta create() {
            this.meta.setLore(this.lore);
            if(this.enchantments.isEmpty() && this.glowing) {
                this.meta.addEnchant(Constants.DUMMY, 1, true);
            } else {
                this.enchantments.forEach((k, v) -> this.meta.addEnchant(k, v, true));
            }
            return this.meta;
        }

        public abstract AbstractItem build();

    }

}
