package net.njsharpe.developmentutility.item;

import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentItemModifier {

    private final ItemStack item;
    private final ItemMeta meta;
    private List<Component> lore;

    /***
     * This class will directly modify the specified item on each function
     * call instead of being built all at once. Method functionality is
     * effectively equivalent.
     *
     * @param item The {@link ItemStack} to modify
     */
    private ConcurrentItemModifier(ItemStack item) {
        this.item = item;
        if(item.getItemMeta() == null) {
            throw new IllegalArgumentException("Item cannot be modified");
        }
        this.meta = item.getItemMeta();
        this.lore = this.meta.lore() == null ? new ArrayList<>() : this.meta.lore();
    }

    public static ConcurrentItemModifier modify(@NotNull ItemStack item) {
        return new ConcurrentItemModifier(item);
    }

    public ConcurrentItemModifier setName(Component name) {
        this.meta.displayName(name);
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ConcurrentItemModifier setAmount(int amount) {
        this.item.setAmount(amount);
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ConcurrentItemModifier appendLore(Component line) {
        this.lore.add(line);
        this.meta.lore(this.lore);
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ConcurrentItemModifier appendLore(Component... lore) {
        if(this.lore.isEmpty()) {
            return this.setLore(lore);
        }
        this.lore.addAll(Arrays.asList(lore));
        this.meta.lore(this.lore);
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ConcurrentItemModifier appendLore(Iterable<Component> lore) {
        if(this.lore.isEmpty()) {
            return this.setLore(lore);
        }
        lore.forEach(this::appendLore);
        this.meta.lore(this.lore);
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ConcurrentItemModifier setLore(Component... lore) {
        this.lore = new ArrayList<>(Arrays.asList(lore));
        this.meta.lore(this.lore);
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ConcurrentItemModifier setLore(Iterable<Component> lore) {
        List<Component> list = new ArrayList<>();
        lore.iterator().forEachRemaining(list::add);
        this.lore = list;
        this.meta.lore(this.lore);
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ConcurrentItemModifier appendLoreAt(int index, Component lore) {
        this.lore.add(index, lore);
        this.meta.lore(this.lore);
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ConcurrentItemModifier appendLoreAt(int index, Iterable<Component> lore) {
        AtomicInteger atomic = new AtomicInteger(index);
        lore.forEach(line -> this.lore.add(atomic.getAndIncrement(), line));
        this.meta.lore(this.lore);
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ConcurrentItemModifier setLoreAt(int index, Iterable<Component> lore) {
        List<Component> list = new ArrayList<>();
        lore.iterator().forEachRemaining(list::add);
        this.lore.addAll(index, list);
        this.meta.lore(this.lore);
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ConcurrentItemModifier setDurability(short durability) {
        if(durability < 0 || durability > this.item.getType().getMaxDurability())
            durability = this.item.getType().getMaxDurability();
        ((Damageable) this.meta).setDamage(durability);
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ConcurrentItemModifier setUnbreakable() {
        this.meta.setUnbreakable(true);
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ConcurrentItemModifier withFlags(ItemFlag... flags) {
        this.meta.addItemFlags(flags);
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ConcurrentItemModifier addEnchantment(Enchantment enchantment, int level) {
        this.meta.addEnchant(enchantment, level, true);
        this.item.setItemMeta(this.meta);
        return this;
    }

    public <T, Z> ConcurrentItemModifier addTag(NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
        this.meta.getPersistentDataContainer().set(key, type, value);
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ConcurrentItemModifier withCustomModelData(int data) {
        this.meta.setCustomModelData(data);
        this.item.setItemMeta(this.meta);
        return this;
    }

}
