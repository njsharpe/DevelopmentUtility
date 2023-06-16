package net.njsharpe.developmentutility.enchantment;

import io.papermc.paper.enchantments.EnchantmentRarity;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityCategory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class DummyEnchantment extends Enchantment {

    private static final NamespacedKey KEY = NamespacedKey.fromString("%s:dummy".formatted(NamespacedKey.BUKKIT));

    public DummyEnchantment() {
        super(Optional.ofNullable(KEY).orElseThrow(() ->
            new IllegalArgumentException("Could not create generic key for enchantment")
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

    @Override
    @NotNull
    public Component displayName(int level) {
        return Component.empty();
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }

    @Override
    @NotNull
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.COMMON;
    }

    @Override
    public float getDamageIncrease(int level, @NotNull EntityCategory entityCategory) {
        return 0;
    }

    @Override
    @NotNull
    public Set<EquipmentSlot> getActiveSlots() {
        return Collections.emptySet();
    }

    @Override
    @NotNull
    public String translationKey() {
        return "enchantment.%s.dummy".formatted(NamespacedKey.BUKKIT);
    }
}
