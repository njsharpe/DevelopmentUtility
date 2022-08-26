package net.njsharpe.developmentutility.helper;

import net.njsharpe.developmentutility.DamageSource;
import net.njsharpe.developmentutility.math.Algebra;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.function.Consumer;

public class ItemHelper {

    public static boolean hurt(@NotNull ItemStack item, int n, @NotNull Random random) {
        int i;
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return false;
        if(meta.isUnbreakable()) return false;
        if(n > 0) {
            i = meta.getEnchantLevel(Enchantment.DURABILITY);
            int j = 0;
            for(int k = 0; i > 0 && k < n; k++) {
                if(!(random.nextInt(n + 1) > 0)) continue;
                j++;
            }
            if((n -= j) <= 0) return false;
        }
        Damageable damageable = (Damageable) meta;
        i = damageable.getDamage() + n;
        damageable.setDamage(i);
        return i >= item.getType().getMaxDurability();
    }

    public static <T extends LivingEntity> void hurtAndBreak(@NotNull ItemStack item, int n, @NotNull T t,
                                                             @NotNull Consumer<T> consumer) {
        ItemMeta meta = item.getItemMeta();
        if(meta != null && meta.isUnbreakable()) return;
        Damageable damageable = (Damageable) meta;
        if(ItemHelper.hurt(item, n, new Random())) {
            consumer.accept(t);
            item.setAmount(item.getAmount() - 1);
            if(t instanceof Player) ((Player) t).incrementStatistic(Statistic.BREAK_ITEM, item.getType());
        }
        damageable.setDamage(0);
    }

    public static void hurtArmor(@NotNull EntityEquipment inventory, @NotNull DamageSource cause, float f, int[] array) {
        if(!(inventory.getHolder() instanceof Player player)) return;
        if(f <= 0.0F) return;
        if((f /= 4.0F) < 1.0F) f = 1.0F;
        for(int n : array) {
            ItemStack item = inventory.getArmorContents()[n];
            if(item == null) continue;
            if(cause.isFire() && ItemHelper.isFireResistant(item)) continue;
            ItemHelper.hurtAndBreak(item, (int) f, player, (p) -> {
                PlayerItemBreakEvent e = new PlayerItemBreakEvent(player, item);
                Bukkit.getServer().getPluginManager().callEvent(e);
            });
        }
    }

    public static boolean isFireResistant(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return false;
        return meta.hasEnchant(Enchantment.PROTECTION_FIRE);
    }

    @Nullable
    public static Item drop(@NotNull Player player, @Nullable ItemStack item, boolean thrower) {
        if(ItemHelper.isEmpty(item)) return null;
        double y = player.getEyeLocation().getY() - 0.30000001192092896D;
        Location pos = player.getLocation();
        Item entity = player.getWorld().dropItem(new Location(pos.getWorld(), pos.getX(), y, pos.getZ()), item);
        entity.setPickupDelay(40);
        if(thrower) entity.setOwner(player.getUniqueId());
        Random random = new Random();
        float sinX = Algebra.sin(EntityHelper.getXRot(entity) * 0.017453292F);
        float cosX = Algebra.cos(EntityHelper.getXRot(entity) * 0.017453292F);
        float sinY = Algebra.sin(EntityHelper.getYRot(entity) * 0.017453292F);
        float cosY = Algebra.cos(EntityHelper.getYRot(entity) * 0.017453292F);
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
        if(contents.length > 0 && !ItemHelper.isEmpty(contents[slot])) {
            return ItemHelper.removeItem(contents, slot, amount);
        }
        return new ItemStack(Material.AIR);
    }

    @NotNull
    public static ItemStack removeItem(@NotNull ItemStack[] items, int index, int amount) {
        if(index < 0 || index >= items.length || ItemHelper.isEmpty(items[index]) || amount <= 0) {
            return new ItemStack(Material.AIR);
        }
        return ItemHelper.split(items[index], amount);
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

}
