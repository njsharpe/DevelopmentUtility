package net.njsharpe.developmentutility.helper;

import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class ProjectileHelper {

    public static void shoot(@NotNull Projectile projectile, @NotNull Random random, double x, double y, double z,
                             float pitch, float yaw) {
        Vector vector = new Vector(random.nextGaussian() * 0.007499999832361937 * yaw, random.nextGaussian() *
                0.007499999832361937 * yaw, random.nextGaussian() * 0.007499999832361937 * yaw);
        Vector velocity = new Vector(x, y, z).normalize().add(vector).multiply(pitch);
        projectile.setVelocity(velocity);
        double hDist = VectorHelper.horizontalDistance(velocity);
        projectile.setRotation((float)(Math.atan2(velocity.getX(), velocity.getZ()) * 57.2957763671875),
                (float)(Math.atan2(velocity.getY(), hDist) * 57.2957763671875));
    }

}
