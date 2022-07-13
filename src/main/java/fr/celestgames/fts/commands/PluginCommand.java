package fr.celestgames.fts.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

public abstract class PluginCommand implements TabExecutor {
    @Override
    public abstract boolean onCommand(CommandSender commandSender, Command command, String label, String[] args);

    @Override
    public abstract List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args);
}
