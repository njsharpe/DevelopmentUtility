package net.njsharpe.developmentutility;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class AdvancedCommandExecutor implements CommandExecutor {

    private final Plugin plugin;
    private final CommandTree tree;

    public AdvancedCommandExecutor(@NotNull Plugin plugin, @NotNull String command) {
        this(plugin, new CommandTree(command, (s) -> true, (s) -> {}));
    }

    public AdvancedCommandExecutor(@NotNull Plugin plugin, @NotNull CommandTree tree) {
        this.plugin = plugin;
        this.tree = tree;
    }

    @NotNull
    public Plugin getPlugin() {
        return this.plugin;
    }

    public CommandTree getTree() {
        return this.tree;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!this.tree.getRoot().getKey().equals(command.getName())) return true;
        if(args.length == 0) return this.tree.getRoot().execute(sender);
        for(CommandTree.Command child : this.tree.getRoot().getChildren()) {
            System.out.println(child.getDepth() + ", " + args.length);
            return child.execute(sender);
        }
        return true;
    }
}
