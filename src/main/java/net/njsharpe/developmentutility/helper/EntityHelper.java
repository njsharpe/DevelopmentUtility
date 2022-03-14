package net.njsharpe.developmentutility.helper;

import net.njsharpe.developmentutility.math.Algebra;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class EntityHelper {

    @Nullable
    public static Player getNearestPlayer(@NotNull World world, double x, double y, double z, double radius,
                                          @Nullable Predicate<Entity> predicate) {
        double d = -1.0D;
        Player player = null;
        for(Player p : world.getPlayers()) {
            if(predicate != null && !predicate.test(p)) continue;;
            double dist = EntityHelper.distanceToSqr(p, x, y, z);
            if(!(radius < 0.0D) && !(dist < radius * radius) || d != -1.0D && !(dist < d)) continue;
            d = dist;
            player = p;
        }
        return player;
    }

    @Nullable
    public static Player getNearestPlayer(@NotNull World world, Entity entity, double radius) {
        Location pos = entity.getLocation();
        return EntityHelper.getNearestPlayer(world, pos.getX(), pos.getY(), pos.getZ(), radius, false);
    }

    @Nullable
    public static Player getNearestPlayer(@NotNull Location location, double radius) {
        if(location.getWorld() == null) return null;
        return EntityHelper.getNearestPlayer(location.getWorld(), location.getX(), location.getY(), location.getZ(),
                radius, false);
    }

    @Nullable
    public static Player getNearestPlayer(@NotNull World world, double x, double y, double z, double radius,
                                          boolean includeHiddenPlayers) {
        Predicate<Entity> predicate = includeHiddenPlayers
                ? entity -> (!(entity instanceof Player)) || !EntityHelper.isSpectator(entity)
                && !((Player) entity).getGameMode().equals(GameMode.CREATIVE)
                : entity -> !EntityHelper.isSpectator(entity);
        return EntityHelper.getNearestPlayer(world, x, y, z, radius, predicate);
    }

    public static boolean isSpectator(@NotNull Entity entity) {
        if(!(entity instanceof Player)) return false;
        Player player = (Player) entity;
        return player.getGameMode().equals(GameMode.SPECTATOR);
    }

    public static double distanceToSqr(@NotNull Entity entity, double x, double y, double z) {
        Location pos = entity.getLocation();
        double deltaX = pos.getX() - x;
        double deltaY = pos.getY() - y;
        double deltaZ = pos.getZ() - z;
        return deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
    }

    public static double distanceToSqr(@NotNull Entity entity, @NotNull Entity target) {
        Location pos = target.getLocation();
        return EntityHelper.distanceToSqr(entity, pos.getX(), pos.getY(), pos.getZ());
    }

    public static double distanceTo(@NotNull Entity entity, @NotNull Entity target) {
        Location pos = target.getLocation();
        return EntityHelper.distanceToSqr(entity, pos.getX(), pos.getY(), pos.getZ());
    }

    public static float getXRot(@NotNull Entity entity) {
        return entity.getLocation().getYaw();
    }

    public static void setXRot(@NotNull Entity entity, float yaw) {
        entity.setRotation(yaw, getYRot(entity));
    }

    public static float getYRot(@NotNull Entity entity) {
        return entity.getLocation().getPitch();
    }

    public static void setYRot(@NotNull Entity entity, float pitch) {
        entity.setRotation(getXRot(entity), pitch);
    }

    public static boolean randomTeleport(@NotNull Entity entity, double x, double y, double z) {
        Location previous = entity.getLocation();
        double ox = previous.getX();
        double oy = previous.getY();
        double oz = previous.getZ();
        double yy = y;
        boolean safe = false;
        World world = entity.getWorld();
        Location location = new Location(world, x, yy, z);
        if(world.isChunkLoaded(location.getChunk())) {
            boolean ground = false;
            while(!ground && location.getY() > 0) {
                Location pos = location.getBlock().getRelative(BlockFace.DOWN).getLocation();
                BlockState state = world.getBlockState(pos);
                if(!state.getBlock().isPassable()) {
                    ground = true;
                    continue;
                }
                yy -= 1.0D;
                location = pos;
            }
            if(ground) {
                entity.teleport(location);
                if(EntityHelper.noCollision(world, entity) && !EntityHelper.containsAnyLiquid(world,
                        entity.getBoundingBox())) {
                    safe = true;
                }
            }
        }
        if(!safe) {
            entity.teleport(new Location(world, ox, oy, oz));
            return false;
        }
        return true;
    }

    public static boolean noCollision(@NotNull World world, @NotNull Entity entity) {
        Location pos = entity.getLocation();
        for(int h = Algebra.floor(pos.getY()); h < (int)(pos.getY() + entity.getHeight()); h++) {
            Block block = world.getBlockAt(pos.getBlockX(), h, pos.getBlockZ());
            if(!block.isPassable()) return false;
        }
        return true;
    }

    public static boolean containsAnyLiquid(@NotNull World world, @NotNull BoundingBox box) {
        int minX = Algebra.floor(box.getMinX());
        int maxX = Algebra.ceil(box.getMaxX());
        int minY = Algebra.floor(box.getMinY());
        int maxY = Algebra.ceil(box.getMaxY());
        int minZ = Algebra.floor(box.getMinZ());
        int maxZ = Algebra.floor(box.getMaxZ());
        for(int x = minX; x < maxX; x++) {
            for(int y = minY; y < maxY; y++) {
                for(int z = minZ; z < maxZ; z++) {
                    Location pos = new Location(world, x, y, z);
                    BlockState state = world.getBlockState(pos);
                    if(state.getBlock().isLiquid()) return true;
                }
            }
        }
        return false;
    }

}
