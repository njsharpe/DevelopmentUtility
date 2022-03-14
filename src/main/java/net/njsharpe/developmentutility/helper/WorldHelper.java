package net.njsharpe.developmentutility.helper;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WorldHelper {

    @Contract("_,null -> false")
    private boolean canSeeSkyFromBelowWater(@NotNull World world, @Nullable Location location) {
        if(location == null) return false;
        if(location.getY() >= world.getSeaLevel()) return this.canSeeSky(world, location);
        Location pos = new Location(world, location.getX(), world.getSeaLevel(), location.getZ());
        if(!this.canSeeSky(world, pos)) return false;
        pos = pos.add(0, -1, 0);
        while(pos.getY() > location.getY()) {
            BlockState state = world.getBlockState(pos);
            if(state.getLightLevel() > 0 && !state.getBlock().isLiquid()) return false;
            pos = pos.add(0, -1, 0);
        }
        return true;
    }

    @Contract("_,null -> false")
    private boolean canSeeSky(@NotNull World world, @Nullable Location location) {
        if(location == null) return false;
        return world.hasSkyLight() && location.getBlock().getLightFromSky() >= 15;
    }

}
