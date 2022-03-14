package net.njsharpe.developmentutility.helper;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class ArrayHelper {

    @NotNull
    @SuppressWarnings("unchecked")
    public static <E> E[] concat(@NotNull E[]... arrays) {
        int size = 0;
        for(E[] array : arrays) {
            size += array.length;
        }
        E[] out = (E[]) Array.newInstance(arrays.getClass().getComponentType().getComponentType(), size);
        int copied = 0;
        for(E[] array : arrays) {
            System.arraycopy(array, 0, out, copied, array.length);
            copied += array.length;
        }
        return out;
    }

    @NotNull
    public static <E> List<E> fromIterator(@NotNull Iterator<E> iterator) {
        List<E> list = new ArrayList<>();
        while(iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    @Contract("null,_ -> false")
    public static <E> boolean contains(@Nullable E e, @NotNull E[] array) {
        if(e == null) return false;
        for(E item : array) {
            if(item.equals(e)) return true;
        }
        return false;
    }

    public static <E> boolean anyMatch(@NotNull E[] array, @NotNull Predicate<E> predicate) {
        for(E item : array) {
            if(predicate.test(item)) return true;
        }
        return false;
    }

    public static <E> boolean allMatch(@NotNull E[] array, @NotNull Predicate<E> predicate) {
        for(E item : array) {
            if(!predicate.test(item)) return false;
        }
        return true;
    }

}
