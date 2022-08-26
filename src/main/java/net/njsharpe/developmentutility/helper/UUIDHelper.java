package net.njsharpe.developmentutility.helper;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.UUID;

public class UUIDHelper {

    public static final UUID EMPTY = new UUID(0L, 0L);

    public static int[] toList(UUID uuid) {
        long most = uuid.getMostSignificantBits();
        long least = uuid.getLeastSignificantBits();
        return Arrays.stream(new int[]{ (int)(most >> 32), (int)most, (int)(least >> 32), (int)least }).toArray();
    }

    public static UUID toUUID(int[] array) {
        if(array.length != 4) throw new IllegalArgumentException("invalid int array size");
        long most = ((long) array[0] << 32) + (array[1]);
        long least = ((long) array[2] << 32) + (array[3]);
        return new UUID(most, least);
    }

    public static byte[] toBytes(UUID uuid) {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        return buffer.array();
    }

    public static UUID toUUID(byte[] bytes) {
        if(bytes.length != 16) throw new IllegalArgumentException("invalid byte array size");
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        long most = buffer.getLong();
        long least = buffer.getLong();
        return new UUID(most, least);
    }

}
