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
        if(root instanceof CommandTree.SingletonCommand) {
            CommandTree.SingletonCommand singleton = (CommandTree.SingletonCommand) root;
            if(!singleton.getCommand().equals(command)) return true;
            if(args.length == (root.getDepth() + 1)) return root.execute(sender);
            return this.child(root, sender, args);
        }
        if(root instanceof CommandTree.MultiCommand) {
            CommandTree.MultiCommand multi = (CommandTree.MultiCommand) root;
            if(!multi.getCommands().contains(command)) return true;
            if(args.length == (root.getDepth() + 1)) return root.execute(sender);
            return this.child(root, sender, args);
        }
        return root.execute(sender);
    }

    private boolean child(CommandTree.Command root, CommandSender sender, String[] args) {
        for(CommandTree.Command child : root.getChildren()) {
            if(child instanceof CommandTree.SingletonCommand) {
                CommandTree.SingletonCommand c = (CommandTree.SingletonCommand) child;
                if(args[child.getDepth()].equals(c.getCommand()))
                    return this.run(child, sender, c.getCommand(), args);
            }
            if(child instanceof CommandTree.MultiCommand) {
                CommandTree.MultiCommand c = (CommandTree.MultiCommand) child;
                for(String option : c.getCommands()) {
                    if(args[child.getDepth()].equals(option)) return this.run(child, sender, option, args);
                }
                return true;
            }
        }
        return root.execute(sender);
    }

}
