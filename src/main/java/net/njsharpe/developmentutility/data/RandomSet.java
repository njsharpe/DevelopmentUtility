package net.njsharpe.developmentutility.data;

import net.njsharpe.developmentutility.helper.RandomHelper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class RandomSet<E> extends AbstractSet<E> {

    private final List<E> data;
    private final Map<E, Integer> indexes;

    public RandomSet() {
        this.data = new ArrayList<>();
        this.indexes = new HashMap<>();
    }

    public RandomSet(@NotNull Collection<E> items) {
        this();
        items.forEach(item -> {
            this.indexes.put(item, this.data.size());
            this.data.add(item);
        });
    }

    @Override
    public boolean add(@Nullable E e) {
        if(this.indexes.containsKey(e)) return false;
        this.indexes.put(e, this.data.size());
        this.data.add(e);
        return true;
    }

    @Contract(mutates = "this")
    @Nullable
    public E removeAt(int index) {
        if(index >= this.data.size()) return null;
        E response = this.data.get(index);
        this.indexes.remove(response);
        E last = this.data.remove(this.data.size() - 1);
        if(index < this.data.size()) {
            this.indexes.put(last, index);
            this.data.set(index, last);
        }
        return response;
    }

    @Contract(mutates = "this")
    @Override
    public boolean remove(@Nullable Object obj) {
        if(obj == null) return true;
        Integer index = this.indexes.get(obj);
        if(index == null) return false;
        this.removeAt(index);
        return true;
    }

    @Contract(pure = true)
    @NotNull
    public E get(int index) {
        return this.data.get(index);
    }

    @Contract(mutates = "this")
    @Nullable
    public E random(@NotNull Random random) {
        if(this.data.isEmpty()) return null;
        int index = RandomHelper.nextIntInclusive(random, 0, this.data.size() - 1);
        return this.removeAt(index);
    }

    @Override
    public int size() {
        return this.data.size();
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return this.data.iterator();
    }

}