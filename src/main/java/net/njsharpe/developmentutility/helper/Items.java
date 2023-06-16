package net.njsharpe.developmentutility.helper;

import net.njsharpe.developmentutility.math.Algebra;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class Items {

    @Nullable
    public static Item drop(@NotNull Player player, @Nullable ItemStack item, boolean setOwner) {
        if(Items.isEmpty(item)) return null;
        double y = player.getEyeLocation().getY() - 0.30000001192092896D;
        Location pos = player.getLocation();
        Item entity = player.getWorld().dropItem(new Location(pos.getWorld(), pos.getX(), y, pos.getZ()), item);
        entity.setPickupDelay(40);
        if(setOwner) entity.setOwner(player.getUniqueId());
        Random random = new Random();
        float sinX = Algebra.sin(Entities.getXRot(entity) * 0.017453292F);
        float cosX = Algebra.cos(Entities.getXRot(entity) * 0.017453292F);
        float sinY = Algebra.sin(Entities.getYRot(entity) * 0.017453292F);
        float cosY = Algebra.cos(Entities.getYRot(entity) * 0.017453292F);
        float rx = random.nextFloat() * 6.2831855F;
        float ry = random.nextFloat() * 0.02F;
        entity.setVelocity(new Vector((-sinY * cosX * 0.3F) + Math.cos(rx) * ry, -sinX * 0.3F + 0.1F +
                (random.nextFloat() - random.nextFloat()) * 0.1F, (cosY * cosX * 0.3F) + Math.sin(rx) * ry));
        return entity;
    }

    @NotNull
    public static ItemStack removeItem(@NotNull PlayerInventory inventory, int slot, int amount) {
        ItemStack[][] compartments = new ItemStack[][] { inventory.getContents(), inventory.getArmorContents(),
                inventory.getExtraContents() };
        ItemStack[] contents = new ItemStack[0];
        for(ItemStack[] items : compartments) {
            if(slot < items.length) {
                contents = items;
                break;
            }
            slot -= items.length;
        }
        if(contents.length > 0 && !Items.isEmpty(contents[slot])) {
            return Items.removeItem(contents, slot, amount);
        }
        return new ItemStack(Material.AIR);
    }

    @NotNull
    public static ItemStack removeItem(@NotNull ItemStack[] items, int index, int amount) {
        if(index < 0 || index >= items.length || Items.isEmpty(items[index]) || amount <= 0) {
            return new ItemStack(Material.AIR);
        }
        return Items.split(items[index], amount);
    }

    @NotNull
    public static ItemStack split(@NotNull ItemStack item, int amount) {
        int min = Math.min(amount, item.getAmount());
        ItemStack copy = item.clone();
        copy.setAmount(min);
        item.setAmount(item.getAmount() - min);
        return copy;
    }

    public static boolean isEmpty(@Nullable ItemStack item) {
        return item == null || item.getType().isAir() || item.getAmount() == 0;
    }

    @NotNull
    public static ItemStack grow(@NotNull ItemStack item, int amount) {
        item.setAmount(Math.min(item.getAmount() + amount, item.getType().getMaxStackSize()));
        return item;
    }

    @NotNull
    public static ItemStack shrink(@NotNull ItemStack item, int amount) {
        item.setAmount(Math.max(0, item.getAmount() - amount));
        return item;
    }

    public static <T, U> boolean tryGet(@NotNull ItemStack item, @NotNull NamespacedKey key,
                                        @NotNull PersistentDataType<T, U> type, @NotNull AtomicReference<U> ref) {
        if(!item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if(container.has(key, type) && container.get(key, type) != null) {
            ref.set(container.get(key, type));
            return true;
        }
        return false;
    }

    public static <T, U, V> boolean tryGet(@NotNull ItemStack item, @NotNull NamespacedKey key,
                                           @NotNull PersistentDataType<T, U> type, @NotNull AtomicReference<V> ref,
                                           @NotNull Function<U, V> function) {
        if(!item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if(container.has(key, type) && container.get(key, type) != null) {
            ref.set(function.apply(container.get(key, type)));
            return true;
        }
        return false;
    }

}
