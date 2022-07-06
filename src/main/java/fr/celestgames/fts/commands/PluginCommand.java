package fr.celestgames.fts.commands;

import fr.celestgames.fts.FTSMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

public abstract class PluginCommand implements TabExecutor {
    protected final FTSMain plugin;

    public PluginCommand(FTSMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public abstract boolean onCommand(CommandSender commandSender, Command command, String label, String[] args);

    @Override
    public abstract List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args);
}
