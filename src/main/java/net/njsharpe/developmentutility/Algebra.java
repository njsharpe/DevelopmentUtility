package net.njsharpe.developmentutility;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.UUID;
import java.util.function.IntPredicate;

public class Algebra {

    private static final int BIG_ENOUGH_INT = 1024;
    private static final float BIG_ENOUGH_FLOAT = 1024.0F;
    private static final long UUID_VERSION = 61440L;
    private static final long UUID_VERSION_4 = 16384L;
    private static final long UUID_VARIANT = -4611686018427387904L;
    private static final long UUID_VARIANT_MAX = Long.MAX_VALUE;
    private static final float SIN_SCALE = 10430.378F;
    private static final float[] SIN = Maker.make(new float[65536], array -> {
        for(int i = 0; i < array.length; i++) {
            array[i] = (float)(Math.sin((double) i * 3.141592653589793D * 2.0D / 65536.0D));
        }
    });
    private static final Random RANDOM = new Random();
    private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[]{0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15,
            25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9};
    private static final double ONE_SIXTH = 0.16666666666666666D;
    private static final int FRAC_EXP = 8;
    private static final int LUC_SIZE = 257;
    private static final double FRAC_BIAS = Double.longBitsToDouble(4805340802404319232L);
    private static final double[] ASIN_TAB = new double[257];
    private static final double[] COS_TAB = new double[257];

    public static final float PI = 3.1415927F;
    public static final float HALF_PI = 1.5707964F;
    public static final float TWO_PI = 6.2831855F;
    public static final float DEGREES_TO_RADIANS = 0.017453292F;
    public static final float RADIANS_TO_DEGREES = 57.295776F;
    public static final float EPSILON = 1.0E-5F;
    public static final float SQRT_OF_TWO = Algebra.sqrt(2.0F);

    public static float sin(float f) {
        return SIN[(int)(f * 10430.378F) & 0xFFFF];
    }

    public static float cos(float f) {
        return SIN[(int)(f * 10430.378F + 16384.0F) & 0xFFFF];
    }

    public static float sqrt(float f) {
        return (float) Math.sqrt(f);
    }

    public static int floor(float f) {
        int n = (int) f;
        return f < (float) n ? n - 1 : n;
    }

    public static int fFloor(double d) {
        return (int)(d + 1024.0D) - 1024;
    }

    public static int floor(double d) {
        int n = (int) d;
        return d < (double) n ? n - 1 : n;
    }

    public static long lFloor(double d) {
        long l = (long) d;
        return d < (double) l ? l - 1L : 1;
    }

    public static int absFloor(double d) {
        return (int)(d >= 0.0D ? d : -d + 1.0D);
    }

    public static float abs(float f) {
        return Math.abs(f);
    }

    public static int abs(int n) {
        return Math.abs(n);
    }

    public static int ceil(float f) {
        int n = (int) f;
        return f > (float) n ? n + 1 : n;
    }

    public static int ceil(double d) {
        int n = (int) d;
        return d > (double) n ? n + 1 : n;
    }

    public static byte clamp(byte i, byte j, byte k) {
        if(i < j) return j;
        if(j > k) return k;
        return i;
    }

    public static int clamp(int i, int j, int k) {
        if(i < j) return j;
        if(j > k) return k;
        return i;
    }

    public static long clamp(long i, long j, long k) {
        if(i < j) return j;
        if(j > k) return k;
        return i;
    }

    public static float clamp(float i, float j, float k) {
        if(i < j) return j;
        if(j > k) return k;
        return i;
    }

    public static double clamp(double i, double j, double k) {
        if(i < j) return j;
        if(j > k) return k;
        return i;
    }

    public static double clampedLerp(double i, double j, double k) {
        if(k < 0.0D) return i;
        if(k > 1.0D) return j;
        return Algebra.lerp(k, i, j);
    }

    public static float clampedLerp(float i, float j, float k) {
        if(k < 0.0F) return i;
        if(k > 1.0F) return j;
        return Algebra.lerp(k, i, j);
    }

    public static double absMax(double i, double j) {
        if(i < 0.0D) i = -i;
        if(j < 0.0D) j = -j;
        return i > j ? i : j;
    }

    public static int floorDiv(int i, int j) {
        return Math.floorDiv(i, j);
    }

    public static int nextInt(Random random, int i, int j) {
        if(i >= j) return i;
        return random.nextInt(j - i + 1) + i;
    }

    public static float nextFloat(Random random, float i, float j) {
        if(i >= j) return i;
        return random.nextFloat() * (j - i) + i;
    }

    public static double nextDouble(Random random, double i, double j) {
        if(i >= j) return i;
        return random.nextDouble() * (j - i) + i;
    }

    public static double average(long[] array) {
        long t = 0L;
        for(long l : array) t += l;
        return (double)(t / array.length);
    }

    public static boolean equal(float i, float j) {
        return Math.abs(j - i) < 1.0E-5F;
    }

    public static boolean equal(double i, double j) {
        return Math.abs(j - i) < 9.999999747378752E-6D;
    }

    public static int positiveMod(int i, int j) {
        return Math.floorMod(i, j);
    }

    public static float positiveMod(float i, float j) {
        return (i % j + j) % j;
    }

    public static double positiveMod(double i, double j) {
        return (i % j + j) % j;
    }

    public static int wrapDegrees(int n) {
        int i = n % 360;
        if(i >= 180) i -= 360;
        if(i < -180) i += 360;
        return i;
    }

    public static float wrapDegrees(float f) {
        float i = f % 360.0F;
        if(i >= 180.0F) i -= 360.0F;
        if(i < -180.0F) i += 360.0F;
        return i;
    }

    public static double wrapDegrees(double d) {
        double i = d % 360.0D;
        if(i >= 180.0D) i -= 360.0D;
        if(i < -180.0D) i += 360.0D;
        return i;
    }

    public static float degreesDifference(float i, float j) {
        return Algebra.wrapDegrees(j - i);
    }

    public static float degreesDifferenceAbs(float i, float j) {
        return Algebra.abs(Algebra.degreesDifference(i, j));
    }

    public static float rotateIfNecessary(float i, float j, float k) {
        float x = Algebra.degreesDifference(i, j);
        float y = Algebra.clamp(x, -k, k);
        return j - y;
    }

    public static float approach(float i, float j, float k) {
        k = Algebra.abs(k);
        if(i < j) return Algebra.clamp(i + k, i, j);
        return Algebra.clamp(i - k, j, i);
    }

    public static float approachDegrees(float i, float j, float k) {
        float x = Algebra.degreesDifference(i, j);
        return Algebra.approach(i, i + x, k);
    }

    public static int getInt(String string, int n) {
        return NumberUtils.toInt(string, n);
    }

    public static int getInt(String string, int i, int j) {
        return Math.max(j, Algebra.getInt(string, i));
    }

    public static double getDouble(String string, double d) {
        try {
            return Double.parseDouble(string);
        } catch (Exception ex) {
            return d;
        }
    }

    public static double getDouble(String string, double i, double j) {
        return Math.max(j, Algebra.getDouble(string, i));
    }

    public static int smallestEncompassingPowerOfTwo(int n) {
        int i = n - 1;
        i |= i >> 1;
        i |= i >> 2;
        i |= i >> 4;
        i |= i >> 8;
        i |= i >> 16;
        return i + 1;
    }

    public static boolean isPowerOfTwo(int n) {
        return n != 0 && (n & n - 1) == 0;
    }

    public static int ceilLogSq(int n) {
        n = Algebra.isPowerOfTwo(n) ? n : Algebra.smallestEncompassingPowerOfTwo(n);
        return MULTIPLY_DE_BRUIJN_BIT_POSITION[(int)((long) n * 125613361L >> 27) & 0x1F];
    }

    public static int logSq(int n) {
        return Algebra.ceilLogSq(n) - (Algebra.isPowerOfTwo(n) ? 0 : 1);
    }

    public static int color(float i, float j, float k) {
        return Algebra.color(Algebra.floor(i * 255.0F), Algebra.floor(j * 255.0F), Algebra.floor(k * 255.0F));
    }

    public static int color(int i, int j, int k) {
        int x = i;
        x = (x << 8) + j;
        x = (x << 8) + k;
        return x;
    }

    public static int colorMultiply(int i, int j) {
        int xa = (i & 0xFF0000) >> 16;
        int xb = (j & 0xFF0000) >> 16;
        int ya = (i & 0xFF00) >> 8;
        int yb = (j & 0xFF00) >> 8;
        int za = (i & 0xFF);
        int zb = (j & 0xFF);
        int r = (int)((float) xa * (float) xb / 255.0f);
        int g = (int)((float) ya * (float) yb / 255.0f);
        int b = (int)((float) za * (float) zb / 255.0f);
        return i & 0xFF000000 | r << 16 | g << 8 | b;
    }

    public static int colorMultiply(int n, float i, float j, float k) {
        int x = (n & 0xFF0000) >> 16;
        int y = (n & 0xFF00) >> 8;
        int z = (n & 0xFF);
        int r = (int)((float) x * i);
        int g = (int)((float) y * j);
        int b = (int)((float) z * k);
        return n & 0xFF000000 | r << 16 | g << 8 | b;
    }

    public static float frac(float f) {
        return f - (float) Algebra.floor(f);
    }

    public static double frac(double d) {
        return d - (double) Algebra.floor(d);
    }

    public static long frac(long l) {
        return l - (long) Algebra.floor(l);
    }

    public static Vector catmullRomSplinePos(Vector w, Vector x, Vector y, Vector z, double d) {
        double i = ((-d + 2.0D) * d - 1.0D) * d * 0.5D;
        double j = ((3.0D * d - 5.0D) * d * d + 2.0D) * 0.5D;
        double k = ((-3.0D * d + 4.0D) * d + 1.0D) * d * 0.5D;
        double l = (d - 1.0D) * d * d * 0.5D;
        return new Vector(w.getX() * i + x.getX() * j + y.getX() * k + z.getX() * l,
                w.getY() * i + x.getY() * j + y.getY() * k + z.getY() * l,
                w.getZ() * i + x.getZ() * j + y.getZ() * k + z.getZ() * l);
    }

    public static long getSeed(Vector vector) {
        return Algebra.getSeed(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
    }

    public static long getSeed(int i, int j, int k) {
        long l = (i * 3129871L) ^ (long) k * 116129781L ^ (long) j;
        l = l * l * 42317861L + l * 11L;
        return l >> 16;
    }

    public static UUID createInsecureUUID(Random random) {
        long x = random.nextLong() & 0xFFFFFFFFFFFF0FFFL | 0x4000L;
        long y = random.nextLong() & 0x3FFFFFFFFFFFFFFFL | Long.MIN_VALUE;
        return new UUID(x, y);
    }

    public static UUID createInsecureUUID() {
        return Algebra.createInsecureUUID(RANDOM);
    }

    public static double inverseLerp(double i, double j, double k) {
        return (i - j) / (k - j);
    }

    public static boolean rayIntersectsBoundingBox(Vector i, Vector j, BoundingBox box) {
        double addX = (box.getMinX() + box.getMaxX()) * 0.5D;
        double subX = (box.getMaxX() - box.getMinX()) * 0.5D;
        double x = i.getX() - addX;
        if(Math.abs(x) > subX && x * j.getX() >= 0.0D) return false;
        double addY = (box.getMinY() + box.getMaxY()) * 0.5D;
        double subY = (box.getMaxY() - box.getMinY()) * 0.5D;
        double y = i.getY() - addY;
        if(Math.abs(y) > subY && y * j.getY() >= 0.0D) return false;
        double addZ = (box.getMinZ() + box.getMaxZ()) * 0.5D;
        double subZ = (box.getMaxZ() - box.getMinZ()) * 0.5D;
        double z = i.getZ() - addZ;
        if(Math.abs(z) > subZ && z * j.getZ() >= 0.0D) return false;
        double absX = Math.abs(j.getX());
        double absY = Math.abs(j.getY());
        double absZ = Math.abs(j.getZ());
        double bounds = j.getY() * z - j.getZ() * y;
        if(Math.abs(bounds) > subY * absZ + subZ * absY) return false;
        bounds = j.getZ() * x - j.getX() * z;
        if(Math.abs(bounds) > subX * absZ + subZ * absX) return false;
        bounds = j.getX() * y - j.getY() * x;
        return Math.abs(bounds) < subX * absY + subY * absX;
    }

    public static double atan2(double i, double j) {
        double x;
        double y = j * j + i * i;
        boolean ni = i < 0.0D;
        boolean lt = i > j;
        boolean nj = j < 0.0D;
        if (Double.isNaN(y)) return Double.NaN;
        if (ni) i = -i;
        if (nj) j = -j;
        if (lt) {
            x = j;
            j = i;
            i = x;
        }
        x = Algebra.fastInvSqrt(y);
        double ba = FRAC_BIAS + (i *= x);
        int n = (int) Double.doubleToRawLongBits(ba);
        double asin = ASIN_TAB[n];
        double cos = COS_TAB[n];
        double bb = ba - FRAC_BIAS;
        double a = i * cos - (j *= x) * bb;
        double b = (6.0 + a * a) * a * 0.16666666666666666;
        double c = asin + b;
        if (lt) c = 1.5707963267948966 - c;
        if (nj) c = 3.141592653589793 - c;
        if (ni) c = -c;
        return c;
    }

    public static float fastInvSqrt(float f) {
        float f2 = 0.5F * f;
        int n = Float.floatToIntBits(f);
        n = 1597463007 - (n >> 1);
        f = Float.intBitsToFloat(n);
        f *= 1.5F - f2 * f * f;
        return f;
    }

    public static double fastInvSqrt(double d) {
        double x = 0.5D * d;
        long l = Double.doubleToRawLongBits(d);
        l = 6910469410427058090L - (l >> 1);
        d = Double.longBitsToDouble(l);
        d *= 1.5D - x * d * d;
        return d;
    }

    public static float fastInvCubeRoot(float f) {
        int n = Float.floatToIntBits(f);
        n = 1419967116 - n / 3;
        float x = Float.intBitsToFloat(n);
        x = 0.6666667F * x + 1.0F / (3.0F * x * x * f);
        x = 0.6666667F * x + 1.0F / (3.0F * x * x * f);
        return x;
    }

    public static int hsvToRgb(float h, float s, float v) {
        float rr;
        float rg;
        int n = (int)(h * 6.0F) % 6;
        float w = h * 6.0F - (float) n;
        float x = v * (1.0F - s);
        float y = v * (1.0F - w * s);
        float z = v * (1.0F - (1.0F - w) * s);
        float rb;
        switch (n) {
            case 0: {
                rr = v;
                rg = z;
                rb = x;
                break;
            }
            case 1: {
                rr = y;
                rg = v;
                rb = x;
                break;
            }
            case 2: {
                rr = x;
                rg = v;
                rb = z;
                break;
            }
            case 3: {
                rr = x;
                rg = y;
                rb = v;
                break;
            }
            case 4: {
                rr = z;
                rg = x;
                rb = v;
                break;
            }
            case 5: {
                rr = v;
                rg = x;
                rb = y;
                break;
            }
            default: {
                throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was "
                        + h + ", " + s + ", " + v);
            }
        }
        int r = Algebra.clamp((int)(rr * 255.0f), 0, 255);
        int g = Algebra.clamp((int)(rg * 255.0f), 0, 255);
        int b = Algebra.clamp((int)(rb * 255.0f), 0, 255);
        return r << 16 | g << 8 | b;
    }

    public static int murmurHash3Mixer(int n) {
        n ^= n >>> 16;
        n *= -2048144789;
        n ^= n >>> 13;
        n *= -1028477387;
        n ^= n >>> 16;
        return n;
    }

    public static long murmurHash3Mixer(long l) {
        l ^= l >>> 33;
        l *= -49064778989728563L;
        l ^= l >>> 33;
        l *= -4265267296055464877L;
        l ^= l >>> 33;
        return l;
    }

    public static double[] cumulativeSum(double... array) {
        float f = 0.0F;
        for (double d : array) {
            f = (float)((double) f + d);
        }
        int n = 0;
        while (n < array.length) {
            double[] copy = array;
            int i = n++;
            copy[i] = copy[i] / (double) f;
        }
        for (n = 0; n < array.length; n++) {
            array[n] = (n == 0 ? 0.0D : array[n - 1]) + array[n];
        }
        return array;
    }

    public static int getRandomForDistributionIntegral(Random random, double[] array) {
        double d = random.nextDouble();
        for (int i = 0; i < array.length; ++i) {
            if (!(d < array[i])) continue;
            return i;
        }
        return array.length;
    }

    public static double[] binNormalDistribution(double x, double y, double z, int i, int j) {
        double[] array = new double[j - i + 1];
        int k = 0;
        for (int n = i; n <= j; n++) {
            array[k] = Math.max(0.0D, x * StrictMath.exp(-((double) n - z) * ((double) n - z) / (2.0D * y * y)));
            k++;
        }
        return array;
    }

    public static double[] binBiModalNormalDistribution(double u, double v, double w, double x, double y, double z,
                                                        int i, int j) {
        double[] array = new double[j - i + 1];
        int k = 0;
        for (int a = i; a <= j; ++a) {
            array[k] = Math.max(0.0D, u * StrictMath.exp(-((double) a - w) * ((double) a - w) / (2.0D * v * v)) +
                    x * StrictMath.exp(-((double) a - z) * ((double) a - z) / (2.0D * y * y)));
            ++k;
        }
        return array;
    }

    public static double[] binLogDistribution(double x, double y, int i, int j) {
        double[] array = new double[j - i + 1];
        int k = 0;
        for (int n = i; n <= j; ++n) {
            array[k] = Math.max(x * StrictMath.log(n) + y, 0.0D);
            k++;
        }
        return array;
    }

    public static int binarySearch(int i, int j, IntPredicate predicate) {
        int k = j - i;
        while (k > 0) {
            int x = k / 2;
            int y = i + x;
            if (predicate.test(y)) {
                k = x;
                continue;
            }
            i = y + 1;
            k -= x + 1;
        }
        return i;
    }

    public static float lerp(float i, float j, float k) {
        return j + i * (k - j);
    }

    public static double lerp(double i, double j, double k) {
        return j + i * (k - j);
    }

    public static double lerp2(double i, double j, double k, double l, double m, double n) {
        return Algebra.lerp(j, Algebra.lerp(i, k, l), Algebra.lerp(i, m, n));
    }

    public static double lerp3(double i, double j, double k, double l, double m, double n, double o, double p,
                               double q, double r, double s) {
        return Algebra.lerp(k, Algebra.lerp2(i, j, l, m, n, o), Algebra.lerp2(i, j, p, q, r, s));
    }

    public static double smoothStep(double d) {
        return d * d * d * (d * (d * 6.0D - 15.0D) + 10.0D);
    }

    public static double smoothStepDerivative(double d) {
        return 30.0D * d * d * (d - 1.0D) * (d - 1.0D);
    }

    public static int sign(double d) {
        if (d == 0.0D) return 0;
        return d > 0.0D ? 1 : -1;
    }

    public static float rotLerp(float i, float j, float k) {
        return j + i * Algebra.wrapDegrees(k - j);
    }

    public static float diffuseLight(float i, float j, float k) {
        return Math.min(i * i * 0.6F + j * j * ((3.0F + j) / 4.0F) + k * k * 0.8F, 1.0F);
    }

    @Deprecated
    public static float rotlerp(float i, float j, float k) {
        float f;
        for(f = j - i; f < -180.0F; f += 360.0F);
        while (f >= 180.0F) {
            f -= 360.0F;
        }
        return i + k * f;
    }

    @Deprecated
    public static float rotWrap(double d) {
        while (d >= 180.0D) d -= 360.0D;
        while (d < -180.0D) d += 360.0D;
        return (float) d;
    }

    public static float triangleWave(float i, float j) {
        return (Math.abs(i % j - j * 0.5F) - j * 0.25F) / (j * 0.25F);
    }

    public static float square(float f) {
        return f * f;
    }

    public static double square(double d) {
        return d * d;
    }

    public static int square(int n) {
        return n * n;
    }

    public static double clampedMap(double i, double j, double k, double l, double m) {
        return Algebra.clampedLerp(l, m, Algebra.inverseLerp(i, j, k));
    }

    public static double map(double i, double j, double k, double l, double m) {
        return Algebra.lerp(Algebra.inverseLerp(i, j, k), l, m);
    }

    public static double wobble(double d) {
        return d + (2.0D * new Random(Algebra.floor(d * 3000.0D)).nextDouble() - 1.0D) * 1.0E-7 / 2.0D;
    }

    public static int roundToward(int i, int j) {
        return (i + j - 1) / j * j;
    }

    public static int randomBetweenInclusive(Random random, int i, int k) {
        return random.nextInt(k - i + 1) + i;
    }

    public static float randomBetween(Random random, float i, float j) {
        return random.nextFloat() * (j - i) + i;
    }

    public static float normal(Random random, float i, float j) {
        return i + (float) random.nextGaussian() * j;
    }

    public static double length(int n, double i, int j) {
        return Math.sqrt((double)(n * n) + i * i + (double)(j * j));
    }

    static {
        for (int i = 0; i < 257; ++i) {
            double d = (double)i / 256.0;
            double d2 = Math.asin(d);
            Algebra.COS_TAB[i] = Math.cos(d2);
            Algebra.ASIN_TAB[i] = d2;
        }
    }

}
