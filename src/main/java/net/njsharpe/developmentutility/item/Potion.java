package net.njsharpe.developmentutility.item;

import net.njsharpe.developmentutility.HexColor;
import net.njsharpe.developmentutility.helper.ArrayHelper;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Potion extends AbstractItem {

    private Potion(@NotNull ItemStack item) {
        super(item);
    }

    @NotNull
    @Override
    public Potion clone() {
        return new Potion(this.getItem().clone());
    }

    public static class Builder extends AbstractItem.Builder {

        private PotionData data;
        private Map<PotionEffect, Boolean> effects;
        private Color color;

        public Builder(@NotNull Material material) {
            super(material);
            if(!ArrayHelper.contains(material, new Material[]{
                    Material.POTION, Material.LINGERING_POTION, Material.SPLASH_POTION
            })) throw new IllegalArgumentException(String.format("material '%s' is not allowed for this type",
                    material));
        }

        public Builder(@NotNull Material material, @Range(from = 1, to = Integer.MAX_VALUE) int amount) {
            super(material, amount);
            if(!ArrayHelper.contains(material, new Material[]{
                    Material.POTION, Material.LINGERING_POTION, Material.SPLASH_POTION
            })) throw new IllegalArgumentException(String.format("material '%s' is not allowed for this type",
                    material));
        }

        private Builder(@NotNull ItemStack item) {
            super(item.getType(), item.getAmount(), Optional.ofNullable(item.getItemMeta())
                    .orElseThrow(IllegalArgumentException::new));
            this.data = ((PotionMeta) item.getItemMeta()).getBasePotionData();
            this.effects = ((PotionMeta) item.getItemMeta()).getCustomEffects().stream()
                    .collect(Collectors.toMap(k -> k, v -> true));
            this.color = ((PotionMeta) item.getItemMeta()).getColor();
        }

        public Builder withEffect(PotionEffectType type, int duration, int amplifier) {
            return this.withEffect(type, duration, amplifier, false);
        }

        public Builder withEffect(PotionEffectType type, int duration, int amplifier, boolean overwrite) {
            return this.withEffect(type.createEffect(duration, amplifier), overwrite);
        }

        public Builder withEffect(PotionEffect effect) {
            return this.withEffect(effect, false);
        }

        public Builder withEffect(PotionEffect effect, boolean overwrite) {
            this.effects.put(effect, overwrite);
            return this;
        }

        public Builder setColor(int red, int green, int blue) {
            return this.setColor(Color.fromRGB(red, green, blue));
        }

        public Builder setColor(HexColor hex) {
            return this.setColor(hex.toColor());
        }

        public Builder setColor(Color color) {
            this.color = color;
            return this;
        }

        public Builder setData(PotionData data) {
            this.data = data;
            return this;
        }

        public Builder forType(PotionType type) {
            return this.setData(new PotionData(type, type.isExtendable(), type.isUpgradeable()));
        }

        @Override
        protected ItemMeta create() {
            PotionMeta meta = (PotionMeta) super.create();
            if(this.data != null) meta.setBasePotionData(this.data);
            meta.setColor(this.color);
            this.effects.forEach(meta::addCustomEffect);
            return meta;
        }

        @Override
        public Potion build() {
            ItemStack item = new ItemStack(this.material, this.amount);
            item.setItemMeta(this.create());
            return new Potion(item);
        }

        public static Potion.Builder rebuild(@NotNull ItemStack item) {
            return new Potion.Builder(item);
        }

    }

}
