package net.njsharpe.developmentutility.helper;

import net.njsharpe.developmentutility.math.Algebra;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Random;
import java.util.function.BiConsumer;
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

    public static boolean teleport(@NotNull LivingEntity entity, @NotNull Random random,
                                   @Range(from = 0, to = 64) int distance,
                                   @NotNull BiConsumer<Location, Location> success) {
        Location pos = entity.getLocation();
        double x = pos.getX() + (random.nextDouble() - 0.5D) * distance;
        double y = pos.getY() + (double)(random.nextInt(16) - distance);
        double z = pos.getZ() + (random.nextDouble() - 0.5D) * distance;
        return EntityHelper.teleport(entity, random, x, y, z, success);
    }

    public static boolean teleport(@NotNull LivingEntity entity, @NotNull Random random, double x, double y, double z,
                                   @NotNull BiConsumer<Location, Location> success) {
        World world = entity.getWorld();
        Location pos = new Location(world, x, y, z);
        while(pos.getY() > world.getMinHeight() && !world.getBlockAt(pos).isPassable()) {
            pos.add(0, -1, 0);
        }
        Block block = world.getBlockAt(pos);
        if(!block.isPassable() || block.isLiquid()) {
            return false;
        }
        Location old = entity.getLocation();
        boolean teleport = EntityHelper.randomTeleport(entity, x, y, z);
        if(teleport) {
            EntityTeleportEvent event = new EntityTeleportEvent(entity, old, pos);
            Bukkit.getServer().getPluginManager().callEvent(event);
            success.accept(old, pos);
        }
        return teleport;
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

    public static Vector getViewVector(@NotNull Entity entity, float f) {
        return VectorHelper.calculateViewVector(EntityHelper.getViewXRot(entity, f), EntityHelper.getViewYRot(entity, f));
    }

    public static float getViewXRot(@NotNull Entity entity, float f) {
        Location old = entity.getLocation();
        if(f == 1.0F) return EntityHelper.getXRot(entity);
        return Algebra.lerp(f, old.getYaw(), EntityHelper.getXRot(entity));
    }

    public static float getViewYRot(@NotNull Entity entity, float f) {
        Location old = entity.getLocation();
        if(f == 1.0F) return EntityHelper.getYRot(entity);
        return Algebra.lerp(f, old.getPitch(), EntityHelper.getYRot(entity));
    }

    public static void push(@NotNull LivingEntity entity, double x, double z) {
        float np = (float) Math.sqrt(x * x + z * z);
        float kp = 0.8F;

        Vector velocity = entity.getVelocity();
        double dx = velocity.getX();
        double dy = velocity.getX();
        double dz = velocity.getX();

        dx /= 2.0D;
        dy /= 2.0D;
        dz /= 2.0D;
        dx -= x / np * kp;
        dy += kp;
        dz -= z / np * kp;

        if(dy > 0.4000000059604645D) {
            dy = 0.4000000059604645D;
        }
        entity.setVelocity(new Vector(dx, dy, dz));
    }

}
