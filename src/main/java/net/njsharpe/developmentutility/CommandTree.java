package net.njsharpe.developmentutility;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandTree<V> {

    private final Command<String, V> root;

    public CommandTree(@NotNull String key, @Nullable V value) {
        this.root = new Command<>(key, value);
    }

    public Command<String, V> getRoot() {
        return this.root;
    }

    public CommandTree<V> addCommand(@NotNull String key, @Nullable V value) {
        return this.addCommand(key, value, this.getRoot());
    }

    public CommandTree<V> addCommand(@NotNull String key, @Nullable V value, @Nullable Command<String, V> parent) {
        Command<String, V> command = new Command<>(key, value, parent);
        return null;
    }

    public static class Command<K, V> {

        private final List<Command<K, V>> children;
        private final K key;
        private Command<K, V> parent;
        private V value;

        public Command(@NotNull K key, @Nullable V value) {
            this(key, value, null);
        }

        public Command(@NotNull K key, @Nullable V value, @Nullable Command<K, V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
            if(parent != null) parent.addChild(this);
            this.children = new ArrayList<>();
        }

        @NotNull
        public K getKey() {
            return this.key;
        }

        @Nullable
        public V getValue() {
            return this.value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        @Nullable
        public CommandTree.Command<K, V> getParent() {
            return this.parent;
        }

        @Contract("_ -> this")
        public Command<K, V> setParent(@NotNull CommandTree.Command<K, V> parent) {
            this.parent = parent;
            return this;
        }

        @NotNull
        public List<Command<K, V>> getChildren() {
            return this.children;
        }

        @Contract("_,_ -> this")
        public Command<K, V> addChild(@NotNull K key, @Nullable V value) {
            Command<K, V> child = new Command<>(key, value, this);
            return this.addChild(child);
        }

        @Contract("_ -> this")
        public Command<K, V> addChild(@NotNull CommandTree.Command<K, V> child) {
            child.setParent(this);
            this.children.add(child);
            return this;
        }

        public int getDepth() {
            int i = 0;
            Command<K, V> parent = this.parent;
            while(parent != null) {
                parent = parent.getParent();
                i++;
            }
            return i;
        }

        public boolean isRoot() {
            return (this.parent == null);
        }

        public boolean isLeaf() {
            return (this.children.size() == 0);
        }

    }

}
