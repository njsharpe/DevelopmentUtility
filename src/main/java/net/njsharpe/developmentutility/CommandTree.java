package net.njsharpe.developmentutility;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class CommandTree {

    private final Command root;

    public CommandTree(@NotNull String key, @NotNull Function<CommandSender, Boolean> execute,
                       @NotNull Consumer<CommandSender> failure) {
        this.root = new Command(key, execute, failure);
    }

    public Command getRoot() {
        return this.root;
    }

    public Command addCommand(@NotNull String key, @NotNull Function<CommandSender, Boolean> execute,
                                  @NotNull Consumer<CommandSender> failure) {
        return this.addCommand(key, execute, failure, this.getRoot());
    }

    public Command addCommand(@NotNull String key, @NotNull Function<CommandSender, Boolean> execute,
                                  @NotNull Consumer<CommandSender> failure, @Nullable Command parent) {
        return new Command(key, execute, failure, parent);
    }

    public static class Command {

        private final List<Command> children;
        private final String key;
        private final Function<CommandSender, Boolean> execute;
        private final Consumer<CommandSender> failure;
        private Command parent;

        public Command(@NotNull String key, @NotNull Function<CommandSender, Boolean> execute,
                       @NotNull Consumer<CommandSender> failure) {
            this(key, execute, failure, null);
        }

        public Command(@NotNull String key, @NotNull Function<CommandSender, Boolean> execute,
                       @NotNull Consumer<CommandSender> failure, @Nullable Command parent) {
            this.key = key;
            this.execute = execute;
            this.failure = failure;
            this.parent = parent;
            if(parent != null) parent.addChild(this);
            this.children = new ArrayList<>();
        }

        @NotNull
        public String getKey() {
            return this.key;
        }

        @NotNull
        public Function<CommandSender, Boolean> getExecute() {
            return this.execute;
        }

        @NotNull
        public Consumer<CommandSender> getFailure() {
            return this.failure;
        }

        @Nullable
        public Command getParent() {
            return this.parent;
        }

        @Contract("_ -> this")
        public Command setParent(@NotNull Command parent) {
            this.parent = parent;
            return this;
        }

        @NotNull
        public List<Command> getChildren() {
            return this.children;
        }

        @Contract("_,_,_ -> this")
        public Command addChild(@NotNull String key, @NotNull Function<CommandSender, Boolean> execute,
                                @NotNull Consumer<CommandSender> failure) {
            Command child = new Command(key, execute, failure, this);
            return this.addChild(child);
        }

        @Contract("_ -> this")
        public Command addChild(@NotNull Command child) {
            child.setParent(this);
            this.children.add(child);
            return this;
        }

        public int getDepth() {
            int i = 0;
            Command parent = this.parent;
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

        public boolean execute(CommandSender sender) {
            boolean failure = this.execute.apply(sender);
            if(failure) this.failure.accept(sender);
            return failure;
        }

    }

}
