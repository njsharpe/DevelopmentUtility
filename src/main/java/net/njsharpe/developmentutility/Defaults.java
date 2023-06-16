package net.njsharpe.developmentutility;

import java.lang.reflect.Array;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Defaults {

    public static <T> T orGet(Supplier<T> supplier, T def) {
        T value = supplier.get();
        return value == null ? def : value;
    }

    public static <T, X extends Throwable> T orThrow(Supplier<T> supplier, Supplier<X> throwable) throws X {
        T value = supplier.get();
        if(value == null) throw throwable.get();
        return value;
    }

    public static <T> T tryGet(Supplier<T> supplier, T def) {
        try {
            T value = supplier.get();
            return value == null ? def : value;
        } catch (Exception ignore) {
            return def;
        }
    }

    public static <T> T orGet(T t, Consumer<T> consumer, T def) {
        consumer.accept(t);
        return t == null ? def : t;
    }

    public static <T, X extends Throwable> T orThrow(T t, Consumer<T> consumer, Supplier<X> throwable) throws X {
        consumer.accept(t);
        if(t == null) throw throwable.get();
        return t;
    }

    public static <T> T tryGet(T t, Consumer<T> consumer, T def) {
        try {
            consumer.accept(t);
            return t == null ? def : t;
        } catch (Exception ignore) {
            return def;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T valueOf(Class<T> clazz) {
        return (T) Array.get(Array.newInstance(clazz, 1), 0);
    }

}
