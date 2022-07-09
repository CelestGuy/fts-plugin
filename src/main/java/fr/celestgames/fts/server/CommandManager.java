package fr.celestgames.fts.server;

import fr.celestgames.fts.FTSMain;
import fr.celestgames.fts.commands.GameCommand;
import fr.celestgames.fts.commands.InfosCommand;
import fr.celestgames.fts.commands.LobbyCommand;
import fr.celestgames.fts.commands.PartyCommand;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;
import java.util.Objects;

public class CommandManager {
    private final FTSMain plugin;

    public CommandManager() {
        this.plugin = FTSMain.getInstance();
    }

    public void registerCommands() {
        Objects.requireNonNull(plugin.getCommand("party")).setExecutor(new PartyCommand());
        Objects.requireNonNull(plugin.getCommand("infos")).setExecutor(new InfosCommand());
        Objects.requireNonNull(plugin.getCommand("lobby")).setExecutor(new LobbyCommand());
        Objects.requireNonNull(plugin.getCommand("game")).setExecutor(new GameCommand());
    }
}
