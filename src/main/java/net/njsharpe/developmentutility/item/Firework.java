package net.njsharpe.developmentutility.item;

import net.njsharpe.developmentutility.helper.ArrayHelper;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class Firework extends AbstractItem {

    private Firework(@NotNull ItemStack item) {
        super(item);
    }

    @NotNull
    @Override
    public Firework clone() {
        return new Firework(this.getItem().clone());
    }

    public static class Builder extends AbstractItem.Builder {

        private List<FireworkEffect> effects;
        private int power;

        public Builder() {
            super(Material.FIREWORK_ROCKET);
        }

        public Builder(@Range(from = 1, to = Integer.MAX_VALUE) int amount) {
            super(Material.FIREWORK_ROCKET, amount);
        }

        private Builder(@NotNull ItemStack item) {
            super(item.getType(), item.getAmount(), Optional.ofNullable(item.getItemMeta())
                    .orElseThrow(IllegalArgumentException::new));
            this.effects = ((FireworkMeta) item.getItemMeta()).getEffects();
            this.power = ((FireworkMeta) item.getItemMeta()).getPower();
        }

        public Builder withEffect(@NotNull FireworkEffect effect) {
            this.effects.add(effect);
            return this;
        }

        public Builder withEffects(@NotNull FireworkEffect... effects) {
            this.effects.addAll(Arrays.asList(effects));
            return this;
        }

        public Builder withEffects(@NotNull Iterable<FireworkEffect> effects) {
            effects.forEach(this::withEffect);
            return this;
        }

        public Builder setPower(int power) {
            this.power = power;
            return this;
        }

        @Override
        protected ItemMeta create() {
            FireworkMeta firework = (FireworkMeta) super.create();
            firework.addEffects(this.effects);
            firework.setPower(this.power);
            return firework;
        }

        @Override
        public Firework build() {
            ItemStack item = new ItemStack(this.material, this.amount);
            item.setItemMeta(this.create());
            return new Firework(item);
        }

        public static Firework.Builder rebuild(@NotNull ItemStack item) {
            return new Firework.Builder(item);
        }

    }

}
