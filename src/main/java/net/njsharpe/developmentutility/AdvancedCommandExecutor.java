package net.njsharpe.developmentutility;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class AdvancedCommandExecutor implements CommandExecutor {

    private final Plugin plugin;
    private final CommandTree<Consumer<CommandSender>> tree;

    public AdvancedCommandExecutor(@NotNull Plugin plugin, @NotNull String command) {
        this(plugin, new CommandTree<>(command, (s) -> {}));
    }

    public AdvancedCommandExecutor(@NotNull Plugin plugin, @NotNull CommandTree<Consumer<CommandSender>> tree) {
        this.plugin = plugin;
        this.tree = tree;
    }

    @NotNull
    public Plugin getPlugin() {
        return this.plugin;
    }

    public CommandTree<Consumer<CommandSender>> getTree() {
        return this.tree;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!this.tree.getRoot().getKey().equals(command.getName())) return true;
        this.tree.getRoot().getChildren().forEach(child -> {
            System.out.println(child.getKey() + ", " + args[child.getDepth()]);
            if(child.getKey().equals(args[child.getDepth()])) {
                if(child.getValue() == null) return;
                child.getValue().accept(sender);
            }
        });
        return true;
    }
}
