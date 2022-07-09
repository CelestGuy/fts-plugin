package fr.celestgames.fts.commands;

import fr.celestgames.fts.FTSMain;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class LobbyCommand extends PluginCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command != null && sender instanceof Player p) {
            List<World> worlds =  p.getServer().getWorlds();

            for (World world : worlds) {
                if (world.getName().equals("lobby")) {
                    p.teleport(world.getSpawnLocation());
                    sender.sendMessage("Vous avez été téléporté au lobby.");
                    return true;
                }
            }

            sender.sendMessage("Le lobby n'existe pas.");
        } else {
            System.out.println("LobbyCommand: Command is null or sender is not a player.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return List.of();
    }
}
