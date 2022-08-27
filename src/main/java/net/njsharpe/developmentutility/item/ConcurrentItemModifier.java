package net.njsharpe.developmentutility.item;

import net.njsharpe.developmentutility.Format;
import net.njsharpe.developmentutility.helper.StringHelper;
import net.njsharpe.developmentutility.item.persistence.ObjectTagType;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConcurrentItemModifier {

    private final ItemStack item;
    private final ItemMeta meta;
    private List<String> lore;

    /***
     * Unlike {@link AbstractItem.Builder}, this class will directly
     * modify the specified item on each function call instead of being
     * built all at once. Method functionality is effectively equivalent.
     *
     * @param item The {@link ItemStack} to modify
     */
    private ConcurrentItemModifier(ItemStack item) {
        this.item = item;
        if(item.getItemMeta() == null) {
            throw new IllegalArgumentException("Item cannot be modified!");
        }
        this.meta = item.getItemMeta();
        this.lore = this.meta.getLore() == null ? new ArrayList<>() : this.meta.getLore();
    }

    public static ConcurrentItemModifier modify(@NotNull ItemStack item) {
        return new ConcurrentItemModifier(item);
    }

    public ConcurrentItemModifier setName(String name) {
        this.meta.setDisplayName(name);
        return this;
    }

    public ConcurrentItemModifier setAmount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ConcurrentItemModifier appendLore(String line) {
        this.lore.add(line);
        this.meta.setLore(this.lore);
        return this;
    }

    public ConcurrentItemModifier appendLore(String... lore) {
        if(this.lore.isEmpty()) {
            return this.setLore(lore);
        }
        this.lore.addAll(Arrays.asList(lore));
        this.meta.setLore(this.lore);
        return this;
    }

    public ConcurrentItemModifier appendLore(Iterable<String> lore) {
        if(this.lore.isEmpty()) {
            return this.setLore(lore);
        }
        lore.forEach(this::appendLore);
        this.meta.setLore(this.lore);
        return this;
    }

    public ConcurrentItemModifier setLore(String lore, String delimiter) {
        return this.setLore(Format.toList(lore, delimiter));
    }

    public ConcurrentItemModifier setLore(String... lore) {
        this.lore = new ArrayList<>(Arrays.asList(lore));
        this.meta.setLore(this.lore);
        return this;
    }

    public ConcurrentItemModifier setLore(Iterable<String> lore) {
        List<String> list = new ArrayList<>();
        lore.iterator().forEachRemaining(list::add);
        this.lore = list;
        this.meta.setLore(this.lore);
        return this;
    }

    public ConcurrentItemModifier appendLoreAt(int index, String lore) {
        this.lore.add(index, lore);
        this.meta.setLore(this.lore);
        return this;
    }

    public ConcurrentItemModifier appendLoreAt(int index, Iterable<String> lore) {
        AtomicInteger atomic = new AtomicInteger(index);
        lore.forEach(line -> this.lore.add(atomic.getAndIncrement(), line));
        this.meta.setLore(this.lore);
        return this;
    }

    public ConcurrentItemModifier setLoreAt(int index, String lore, String delimiter) {
        return this.setLoreAt(index, Format.toList(lore, delimiter));
    }

    public ConcurrentItemModifier setLoreAt(int index, Iterable<String> lore) {
        List<String> list = new ArrayList<>();
        lore.iterator().forEachRemaining(list::add);
        this.lore.addAll(index, list);
        this.meta.setLore(this.lore);
        return this;
    }

    public ConcurrentItemModifier appendDynamicLore(String lore, Serializable... values) {
        StringBuilder builder = new StringBuilder();
        Pattern pattern = Pattern.compile("(\\{\\d})");
        Matcher matcher = pattern.matcher(lore);

        while(matcher.find()) {
            int i = Integer.parseInt(StringHelper.trimEnds(matcher.group()));
            if(i < 0 || i > values.length - 1) {
                throw new IndexOutOfBoundsException("integer found but out of bounds: %s of %s"
                        .formatted(i, values.length));
            }
            NamespacedKey key = NamespacedKey.fromString("%s:dynamic_lore_%s".formatted(NamespacedKey.BUKKIT, i));
            if(key == null) {
                throw new RuntimeException("could not create key %s:dynamic_lore_%s".formatted(NamespacedKey.BUKKIT, i));
            }
            matcher.appendReplacement(builder, String.valueOf(values[i]));
            this.meta.getPersistentDataContainer().set(key, new ObjectTagType(), values[i]);
        }

        matcher.appendTail(builder);
        this.lore.add(builder.toString());
        this.meta.setLore(this.lore);
        return this;
    }

    public ConcurrentItemModifier setDurability(short durability) {
        if(durability < 0 || durability > this.item.getType().getMaxDurability())
            durability = this.item.getType().getMaxDurability();
        ((Damageable) this.meta).setDamage(durability);
        return this;
    }

    public ConcurrentItemModifier setUnbreakable() {
        this.meta.setUnbreakable(true);
        return this;
    }

    public ConcurrentItemModifier withFlags(ItemFlag... flags) {
        this.meta.addItemFlags(flags);
        return this;
    }

    public ConcurrentItemModifier addEnchantment(Enchantment enchantment, int level) {
        this.meta.addEnchant(enchantment, level, true);
        return this;
    }

    public <T, Z> ConcurrentItemModifier addTag(NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
        this.meta.getPersistentDataContainer().set(key, type, value);
        return this;
    }

    public ConcurrentItemModifier withCustomModelData(int data) {
        this.meta.setCustomModelData(data);
        return this;
    }

}
