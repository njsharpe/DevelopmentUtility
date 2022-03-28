package net.njsharpe.developmentutility.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class CommandTree {

    private final Command root;

    public CommandTree(@NotNull String command) {
        this(command, (sender) -> true, (sender) -> {});
    }

    public CommandTree(@NotNull String command, @NotNull Function<CommandSender, Boolean> execute,
                       @NotNull Consumer<CommandSender> failure) {
        this.root = new Command(command, execute, failure);
    }

    public Command getRoot() {
        return this.root;
    }

    @Contract("_,_,_ -> new")
    public Command addCommand(@NotNull String command, @NotNull Function<CommandSender, Boolean> execute,
                              @NotNull Consumer<CommandSender> failure) {
        return this.addCommand(command, execute, failure, this.root);
    }

    @Contract("_,_,_,_ -> new")
    public Command addCommand(@NotNull String command, @NotNull Function<CommandSender, Boolean> execute,
                              @NotNull Consumer<CommandSender> failure, @Nullable Command parent) {
        return new Command(command, execute, failure, parent);
    }

    public static class Command {

        private final List<Command> children;
        private final String command;
        private final Function<CommandSender, Boolean> execute;
        private final Consumer<CommandSender> failure;
        private Command parent;

        public Command(@NotNull String command, @NotNull Function<CommandSender, Boolean> execute,
                       @NotNull Consumer<CommandSender> failure) {
            this(command, execute, failure, null);
        }

        public Command(@NotNull String command, @NotNull Function<CommandSender, Boolean> execute,
                       @NotNull Consumer<CommandSender> failure, @Nullable Command parent) {
            this.command = command;
            this.execute = execute;
            this.failure = failure;
            this.parent = parent;
            if (parent != null) parent.addChild(this);
            this.children = new ArrayList<>();
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

        @Contract("_ -> this")
        public Command addChild(@NotNull Command child) {
            child.setParent(this);
            this.children.add(child);
            return this;
        }

        public int getDepth() {
            int i = -1;
            Command parent = this.parent;
            while (parent != null) {
                parent = parent.parent;
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

        @NotNull
        public String getCommand() {
            return this.command;
        }

        public boolean execute(CommandSender sender) {
            boolean success = this.execute.apply(sender);
            if (!success) this.failure.accept(sender);
            return success;
        }

    }

}
