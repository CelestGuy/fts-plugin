package fr.celestgames.fts.commands;

import fr.celestgames.fts.FTSMain;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Objects;

import static org.bukkit.Bukkit.getServer;

public class InfosCommand extends PluginCommand {
    public InfosCommand(FTSMain plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command != null) {
            // FTS = From the sky
            sender.sendMessage("§6§lFTS Plugin §8» §7Version: 0.0.1");
            sender.sendMessage("§6§lFTS Plugin §8» §7Author: Celest_Guy (Théo DUVAL)");
            sender.sendMessage("§6§lFTS Plugin §8» §7Github: https://github.com/CelestGuy");
            sender.sendMessage("§6§lFTS Plugin §8» §7Website: https://celestgames.fr");
            sender.sendMessage("§6§lFTS Plugin §8» §7Discord: https://discord.gg/QQQQQQQQ");
            sender.sendMessage("§6§lFTS Plugin §8» §7Twitter: https://twitter.com/Celest_Guy");
            sender.sendMessage("§6§lFTS Plugin §8» §7Youtube: https://www.youtube.com/channel/UCu3ZDrwrNOCmeppWdWQHYyw");

            for (World world : getServer().getWorlds()) {
                Bukkit.getLogger().warning("[" + plugin.getName() + "] Monde: " + world.getName());
                if (Objects.equals(world.getName(), "lobby")) {
                    Bukkit.getLogger().warning("[" + plugin.getName() + "] Le monde lobby est présent.");
                } else {
                    Bukkit.getLogger().warning("[" + plugin.getName() + "] Le monde lobby est absent.");
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
