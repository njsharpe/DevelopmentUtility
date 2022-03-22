package net.njsharpe.developmentutility.enchantment;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Optional;

public class DummyEnchantment extends Enchantment {

    private static final NamespacedKey KEY = NamespacedKey.fromString(String.format("%s:dummy", NamespacedKey.BUKKIT));

    public DummyEnchantment() {
        super(Optional.ofNullable(KEY).orElseThrow(() ->
            new IllegalArgumentException(String.format("could not create key %s for enchantment",
                    String.format("%s:dummy", NamespacedKey.BUKKIT)))
        ));
    }

    @NotNull
    @Override
    public String getName() {
        return "";
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public int getStartLevel() {
        return 0;
    }

    @NotNull
    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.BREAKABLE;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(@NotNull Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(@NotNull ItemStack item) {
        return false;
    }

    public static DummyEnchantment register() {
        try {
            Field field = Enchantment.class.getDeclaredField("acceptingNew");
            field.setAccessible(true);
            field.set(null, true);

            DummyEnchantment enchantment = new DummyEnchantment();
            if(Enchantment.getByKey(enchantment.getKey()) == null) Enchantment.registerEnchantment(enchantment);
            return enchantment;
        } catch (Exception ex) {
            throw new RuntimeException("could not register enchantment", ex);
        }
    }

}
