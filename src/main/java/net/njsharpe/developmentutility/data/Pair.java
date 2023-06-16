package net.njsharpe.developmentutility.data;

import java.util.concurrent.atomic.AtomicReference;

public final class Pair<K, V> {

    private final K key;
    private final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "[%s, %s]".formatted(this.key, this.value);
    }

    public void deconstruct(AtomicReference<K> key, AtomicReference<V> value) {
        key.set(this.key);
        value.set(this.value);
    }

}
