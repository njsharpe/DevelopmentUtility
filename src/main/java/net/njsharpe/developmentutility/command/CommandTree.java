package net.njsharpe.developmentutility.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class CommandTree {

    private final SingletonCommand root;

    public CommandTree(@NotNull String command) {
        this(command, (sender) -> true, (sender) -> {});
    }

    public CommandTree(@NotNull String command, @NotNull Function<CommandSender, Boolean> execute,
                       @NotNull Consumer<CommandSender> failure) {
        this.root = new SingletonCommand(command, execute, failure);
    }

    public SingletonCommand getRoot() {
        return this.root;
    }

    @Contract("_,_,_ -> new")
    public SingletonCommand addCommand(@NotNull String command, @NotNull Function<CommandSender, Boolean> execute,
                              @NotNull Consumer<CommandSender> failure) {
        return this.addCommand(command, execute, failure, this.root);
    }

    @Contract("_,_,_,_ -> new")
    public SingletonCommand addCommand(@NotNull String command, @NotNull Function<CommandSender, Boolean> execute,
                              @NotNull Consumer<CommandSender> failure, @Nullable Command parent) {
        return new SingletonCommand(command, execute, failure, parent);
    }

    @Contract("_,_,_ -> new")
    public MultiCommand addCommand(@NotNull String[] options, @NotNull Function<CommandSender, Boolean> execute,
                                   @NotNull Consumer<CommandSender> failure) {
        return this.addCommand(options, execute, failure, this.root);
    }

    @Contract("_,_,_,_ -> new")
    public MultiCommand addCommand(@NotNull String[] options, @NotNull Function<CommandSender, Boolean> execute,
                                   @NotNull Consumer<CommandSender> failure, @Nullable Command parent) {
        return new MultiCommand(Arrays.asList(options), execute, failure, parent);
    }

    @Contract("_,_,_ -> new")
    public MultiCommand addCommand(@NotNull List<String> options, @NotNull Function<CommandSender, Boolean> execute,
                              @NotNull Consumer<CommandSender> failure) {
        return this.addCommand(options, execute, failure, this.root);
    }

    @Contract("_,_,_,_ -> new")
    public MultiCommand addCommand(@NotNull List<String> options, @NotNull Function<CommandSender, Boolean> execute,
                              @NotNull Consumer<CommandSender> failure, @Nullable Command parent) {
        return new MultiCommand(options, execute, failure, parent);
    }

    public static class Command {

        private final List<Command> children;
        private final Function<CommandSender, Boolean> execute;
        private final Consumer<CommandSender> failure;
        private Command parent;

        public Command(@NotNull Function<CommandSender, Boolean> execute, @NotNull Consumer<CommandSender> failure) {
            this(execute, failure, null);
        }

        public Command(@NotNull Function<CommandSender, Boolean> execute, @NotNull Consumer<CommandSender> failure,
                       @Nullable Command parent) {
            this.execute = execute;
            this.failure = failure;
            this.parent = parent;
            if(parent != null) parent.addChild(this);
            this.children = new ArrayList<>();
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

        @Contract("_ -> this")
        public Command addChild(@NotNull Command child) {
            child.setParent(this);
            this.children.add(child);
            return this;
        }

        public int getDepth() {
            int i = -1;
            Command parent = this.parent;
            while(parent != null) {
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

        public boolean execute(CommandSender sender) {
            boolean success = this.execute.apply(sender);
            if(!success) this.failure.accept(sender);
            return success;
        }

    }

    public static class SingletonCommand extends Command {

        private final String command;

        public SingletonCommand(@NotNull String command, @NotNull Function<CommandSender, Boolean> execute,
                       @NotNull Consumer<CommandSender> failure) {
            this(command, execute, failure, null);
        }

        public SingletonCommand(@NotNull String command, @NotNull Function<CommandSender, Boolean> execute,
                       @NotNull Consumer<CommandSender> failure, @Nullable Command parent) {
            super(execute, failure, parent);
            this.command = command;
        }

        @NotNull
        public String getCommand() {
            return this.command;
        }

    }

    public static class MultiCommand extends Command {

        private final List<String> commands;

        public MultiCommand(@NotNull List<String> commands, @NotNull Function<CommandSender, Boolean> execute,
                                @NotNull Consumer<CommandSender> failure) {
            this(commands, execute, failure, null);
        }

        public MultiCommand(@NotNull List<String> commands, @NotNull Function<CommandSender, Boolean> execute,
                                @NotNull Consumer<CommandSender> failure, @Nullable Command parent) {
            super(execute, failure, parent);
            this.commands = commands;
        }

        @NotNull
        public List<String> getCommands() {
            return this.commands;
        }

    }

}
