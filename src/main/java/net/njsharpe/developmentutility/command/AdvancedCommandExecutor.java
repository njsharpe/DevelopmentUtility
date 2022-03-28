package net.njsharpe.developmentutility.command;

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
        return this.run(this.tree.getRoot(), sender, command.getName(), args);
    }

    private boolean run(CommandTree.Command root, CommandSender sender, String command, String[] args) {
        if(!root.getCommand().equals(command)) return true;
        if(args.length == (root.getDepth() + 1)) return root.execute(sender);
        for(CommandTree.Command child : root.getChildren()) {
            if(args[child.getDepth()].equals(child.getCommand()))
                return this.run(child, sender, child.getCommand(), args);
        }
        return root.execute(sender);
    }

}
