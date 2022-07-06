package fr.celestgames.fts.commands;

import fr.celestgames.fts.FTSMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class GameCommand extends PluginCommand {
    public GameCommand(FTSMain plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
