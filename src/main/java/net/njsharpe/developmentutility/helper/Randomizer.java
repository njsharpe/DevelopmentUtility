package net.njsharpe.developmentutility.helper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

public class Randomizer {

    public static int nextIntInclusive(@NotNull Random random, @Range(from = 0, to = Integer.MAX_VALUE) int min,
                                       @Range(from = 0, to = Integer.MAX_VALUE) int max) {
        if(min == max) return min;
        if(max < min) throw new IllegalStateException("Max cannot be greater than min");
        return random.nextInt((max - min) + 1) + min;
    }

    @Nullable
    public static <E> E getRandomFor(@NotNull Random random, @NotNull Collection<E> collection) {
        Iterator<E> iterator = collection.iterator();
        if(collection.size() == 0) return null;
        int choice = random.nextInt(collection.size());
        int i = 0;
        while(iterator.hasNext()) {
            if(i == choice) return iterator.next();
            i++;
        }
        throw new IllegalStateException(String.format("Rolled %s into max of %s", choice, collection.size()));
    }

    @Nullable
    public static <E> E getRandomFor(@NotNull Random random, @NotNull E[] array) {
        if(array.length == 0) return null;
        int choice = random.nextInt(array.length);
        for(int i = 0; i < array.length; i++) {
            if(i == choice) return array[i];
        }
        throw new IllegalStateException(String.format("Rolled %s into max of %s", choice, array.length));
    }

}
