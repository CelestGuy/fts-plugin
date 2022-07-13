package fr.celestgames.fts.server;

import fr.celestgames.fts.FTSPlugin;
import fr.celestgames.fts.commands.GameCommand;
import fr.celestgames.fts.commands.InfosCommand;
import fr.celestgames.fts.commands.LobbyCommand;
import fr.celestgames.fts.commands.PartyCommand;

import java.util.Objects;

public class CommandManager {
    private final FTSPlugin plugin;

    public CommandManager() {
        this.plugin = FTSPlugin.getInstance();
    }

    public void registerCommands() {
        Objects.requireNonNull(plugin.getCommand("party")).setExecutor(new PartyCommand());
        Objects.requireNonNull(plugin.getCommand("infos")).setExecutor(new InfosCommand());
        Objects.requireNonNull(plugin.getCommand("lobby")).setExecutor(new LobbyCommand());
        Objects.requireNonNull(plugin.getCommand("game")).setExecutor(new GameCommand());
    }
}
