package net.njsharpe.developmentutility.item;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rotatable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.UUID;

public class Skull extends AbstractItem {

    private Skull(@NotNull ItemStack item) {
        super(item);
    }

    public void place(Location location) {
        if(location.getWorld() == null) throw new IllegalArgumentException("world cannot be null");
        if(!this.getItem().getType().isBlock()) throw new IllegalStateException("item is not a block, and cannot be placed");
        Block block = location.getWorld().getBlockAt(location);
        block.setType(this.getItem().getType());
        if(block.getBlockData() instanceof Rotatable) {
            Rotatable rotatable = (Rotatable) block.getBlockData();
            rotatable.setRotation(BlockFace.SELF);
            block.setBlockData(rotatable);
        }
    }

    public void place(Location location, BlockFace facing) {
        if(location.getWorld() == null) throw new IllegalArgumentException("world cannot be null");
        if(!this.getItem().getType().isBlock()) throw new IllegalStateException("item is not a block, and cannot be placed");
        Block block = location.getWorld().getBlockAt(location);
        block.setType(this.getItem().getType());
        if(block.getBlockData() instanceof Rotatable) {
            Rotatable rotatable = (Rotatable) block.getBlockData();
            rotatable.setRotation(facing);
            block.setBlockData(rotatable);
        }
    }

    @NotNull
    @Override
    public Skull clone() {
        return new Skull(this.getItem().clone());
    }

    public static class Builder extends AbstractItem.Builder {

        private OfflinePlayer player;

        public Builder() {
            super(Material.PLAYER_HEAD);
        }

        public Builder(@Range(from = 1, to = Integer.MAX_VALUE) int amount) {
            super(Material.PLAYER_HEAD, amount);
        }

        public Builder forPlayer(UUID uuid) {
            return this.forPlayer(Bukkit.getOfflinePlayer(uuid));
        }

        public Builder forPlayer(OfflinePlayer player) {
            this.player = player;
            return this;
        }

        @Override
        protected ItemMeta create() {
            SkullMeta meta = (SkullMeta) super.create();
            if(this.player != null) meta.setOwningPlayer(this.player);
            return meta;
        }

        @Override
        public Skull build() {
            ItemStack item = new ItemStack(this.material, this.amount);
            item.setItemMeta(this.create());
            return new Skull(item);
        }
    }

}
