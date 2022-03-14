package net.njsharpe.developmentutility.helper;

import org.bukkit.util.Vector;

public class VectorHelper {

    public static double horizontalDistance(Vector vector) {
        return Math.sqrt(vector.getX() * vector.getX() + vector.getZ() * vector.getZ());
    }

    public static Vector calculateViewVector(float x, float y) {
        float px = x * 0.017453292F;
        float ny = -y * 0.017453292F;
        double cosY = Math.cos(ny);
        double sinY = Math.sin(ny);
        double cosX = Math.cos(px);
        double sinX = Math.sin(px);
        return new Vector(sinY * cosX, -sinX, cosY * cosX);
    }

}
